package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.model.Location;
import ru.practicum.model.User;

@Data
@Validated
@Builder
public class EventFullDto {

    private Long id;

    private String annotation;

    private CategoryDto category;


    private Integer confirmedRequests;

    private String createdOn;

    private String description;

    private String eventDate;

    private User initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private String publishedOn;

    private Boolean requestModeration;

    private State state;

    private String stateAction;

    private String title;

    private Integer views;

}
