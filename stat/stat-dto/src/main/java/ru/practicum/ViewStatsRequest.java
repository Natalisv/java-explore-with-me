package ru.practicum;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class ViewStatsRequest {

    private String start;

    private String end;

    private List<String> uris = new ArrayList<>();

    private Boolean unique;

    public ViewStatsRequest(String start, String end, List<String> uris, Boolean unique) {
        this.start = start;
        this.end = end;
        this.uris.addAll(uris);
        this.unique = unique;
    }
}
