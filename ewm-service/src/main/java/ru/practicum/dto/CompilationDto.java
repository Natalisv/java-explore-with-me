package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.Event;

import java.util.List;

@Data
@Builder
public class CompilationDto {

    private Long id;

    private List<Event> events;

    private Boolean pinned;

    private String title;

}
