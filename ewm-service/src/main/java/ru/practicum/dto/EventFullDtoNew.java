package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import ru.practicum.model.Location;
import ru.practicum.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Validated
@Builder
public class EventFullDtoNew {

    private Long id;

    @NotNull
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    private CategoryDto categoryDto;

    private Integer confirmedRequests;

    private String createdOn;

    @NotNull
    @NotBlank
    @NotEmpty
    @Length(min = 20, max = 7000)
    private String description;

    @NotNull
    @NotBlank
    private String eventDate;

    private User initiator;

    @NotNull
    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private String publishedOn;

    private Boolean requestModeration;

    private State state;

    private String stateAction;

    @NotNull
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;

    private Integer views;

}
