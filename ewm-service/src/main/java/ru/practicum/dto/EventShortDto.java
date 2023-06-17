package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.User;

@Data
@Builder
public class EventShortDto {

    private Long id;

    private String annotation;

    private String description;

    private CategoryDto category;

    private Integer confirmedRequests;

    private String eventDate;

    private User initiator;

    private Boolean paid;

    private String title;

    private Integer views;

    public EventShortDto() {
    }

    public EventShortDto(Long id, String annotation, String description, CategoryDto category, Integer confirmedRequests,
                         String eventDate, User initiator, Boolean paid, String title, Integer views) {
        this.id = id;
        this.annotation = annotation;
        this.description = description;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.paid = paid;
        this.title = title;
        this.views = views;
    }
}
