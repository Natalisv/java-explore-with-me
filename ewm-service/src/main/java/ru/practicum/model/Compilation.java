package ru.practicum.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "compilation")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title")
    private String title;

    public Compilation() {
    }

    public Compilation(Long id, Boolean pinned, String title) {
        this.id = id;
        this.pinned = pinned;
        this.title = title;
    }
}
