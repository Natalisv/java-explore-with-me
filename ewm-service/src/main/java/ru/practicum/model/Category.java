package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
@Builder
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(
            targetEntity = Event.class,
            mappedBy = "category",
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<Long> eventId;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(Long id, String name, List<Long> eventId) {
        this.id = id;
        this.name = name;
        this.eventId = eventId;
    }
}
