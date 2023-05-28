package ru.practicum.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.ViewStatsRequest;

import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class StatClient {

    private final String application;

    private final String statsServiceUri;

    private final ObjectMapper json;

    private final HttpClient httpClient;

    public StatClient(@Value("ewm-main-service") String application,
                      @Value("http://localhost:9090") String statsServiceUri,
                      ObjectMapper json) {
        this.application = application;
        this.statsServiceUri = statsServiceUri;
        this.json = json;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    public void postHit(HttpServletRequest userRequest) {
        EndpointHit hit = EndpointHit.builder()
                .app(application)
                .ip(userRequest.getRemoteAddr())
                .uri(userRequest.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        try {
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(json.writeValueAsString(hit));
            HttpRequest hitRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + "/hit"))
                    .POST(bodyPublisher)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .build();

            HttpResponse<Void> response = httpClient.send(hitRequest, HttpResponse.BodyHandlers.discarding());
            log.info("Ответ сервиса статистики: {}", response);
        } catch (Exception e) {
            log.error("Ответ не получен");
        }
    }

    public List<ViewStats> getStats(ViewStatsRequest request) {
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(statsServiceUri + "/stats")
                    .queryParam("start", request.getStart())
                    .queryParam("end", request.getEnd())
                    .queryParam("uris", request.getUris())
                    .queryParam("unique", request.getUnique())
                    .build().toUri();

            HttpRequest statsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(uri.toString()))
                    .GET()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(statsRequest, HttpResponse.BodyHandlers.ofString());
            List<ViewStats> list = new ArrayList<>();
            if (response.statusCode() == 200) {
                ViewStats[] array = json.readValue(response.body(), ViewStats[].class);
                list = Arrays.asList(array);
            }
            return list;
        } catch (Exception e) {
            log.error("Не удалось получить статистику");
        }
        return Collections.emptyList();
    }

}
