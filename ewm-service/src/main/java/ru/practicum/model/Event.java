package ru.practicum.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Builder
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation")
    @Length(max = 2000)
    private String annotation;

    @Column(name = "category")
    private Long category;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description")
    @Length(max = 7000)
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "initiator")
    private Long initiator;

    @Column(name = "location")
    private Long location;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "state")
    private String state;

    @Column(name = "title")
    @Length(max = 120)
    private String title;

    @Column(name = "views")
    private Integer views;

    @Column(name = "compilation")
    private Long compilation;


    public Event() {
    }

    public Event(Long id, String annotation, Long category, Integer confirmedRequests, LocalDateTime createdOn,
                 String description, LocalDateTime eventDate, Long initiator, Long location, Boolean paid,
                 Integer participantLimit, LocalDateTime publishedOn, Boolean requestModeration, String state,
                 String title, Integer views, Long compilation) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
        this.compilation = compilation;
    }
}
