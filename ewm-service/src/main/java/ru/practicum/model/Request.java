package ru.practicum.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@Data
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "event")
    private Long event;

    @Column(name = "requester")
    private Long requester;

    @Column(name = "status")
    private String status;


    public Request() {
    }

    public Request(Long id, LocalDateTime created, Long event, Long requester, String status) {
        this.id = id;
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}
