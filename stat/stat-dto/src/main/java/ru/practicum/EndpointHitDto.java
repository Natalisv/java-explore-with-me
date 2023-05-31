package ru.practicum;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EndpointHitDto {

    private String app;

    private String uri;

    private String ip;

    private String timestamp;

    public EndpointHitDto() {
    }

    public EndpointHitDto(String app, String uri, String ip, String timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}
