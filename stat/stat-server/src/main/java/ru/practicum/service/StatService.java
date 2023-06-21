package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStats;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.repository.StatRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class StatService {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private final StatRepository statRepository;

    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public void addHit(EndpointHitDto endpointHit) {
        if (isValid(endpointHit).equals(Boolean.TRUE)) {
            statRepository.save(EndpointHitMapper.toEndpointHit(endpointHit));
            log.info("Информация сохранена");
        } else {
            log.error("Невалидная сущность");
            throw new ValidationException("Невалидная сущность");
        }
    }

    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
        if (uris != null && !uris.isEmpty()) {
            if (unique.equals(Boolean.FALSE)) {
                log.info("Получена статистика по времени для определенных uris");
                return statRepository.findByTimeAndUri(startDate, endDate, uris.toArray(new String[0]));
            } else {
                log.info("Получена статистика по времени и уникальным ip для определенных uris");
                return statRepository.findByTimeAndUriDistinctIp(startDate, endDate, uris.toArray(new String[0]));
            }
        } else {
            if (unique.equals(Boolean.FALSE)) {
                log.info("Получена статистика по времени для всех uris");
                return statRepository.findAllByTime(startDate, endDate);
            } else {
                log.info("Получена статистика по времени и уникальным ip для всех uris");
                return statRepository.findAllByTimeDistinctIp(startDate, endDate);
            }
        }
    }

    private Boolean isValid(EndpointHitDto endpointHit) {
        return endpointHit.getApp() != null && !endpointHit.getApp().isEmpty() && endpointHit.getIp() != null &&
                !endpointHit.getIp().isEmpty() && endpointHit.getUri() != null && !endpointHit.getUri().isEmpty();
    }
}
