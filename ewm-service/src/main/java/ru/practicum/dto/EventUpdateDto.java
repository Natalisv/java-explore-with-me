package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import ru.practicum.model.Location;
import ru.practicum.model.User;

@Data
@Validated
@Builder
public class EventUpdateDto {

    private Long id;

    @Length(min = 20, max = 2000)
    private String annotation;

    private Long category;


    private Integer confirmedRequests;

    private String createdOn;

    @Length(min = 20, max = 7000)
    private String description;

    private String eventDate;

    private User initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private String publishedOn;

    private Boolean requestModeration;

    private String stateAction;

    @Length(min = 3, max = 120)
    private String title;

    private Integer views;

}
