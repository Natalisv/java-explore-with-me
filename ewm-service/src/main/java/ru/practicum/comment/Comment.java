package ru.practicum.comment;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Builder
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "text")
    @Length(max = 500)
    private String text;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "event_id")
    private Long eventId;

    public Comment() {
    }

    public Comment(Long id, Long authorId, String text, LocalDateTime date, Long eventId) {
        this.id = id;
        this.authorId = authorId;
        this.text = text;
        this.date = date;
        this.eventId = eventId;
    }

    public Comment(String text) {
        this.text = text;
    }
}
