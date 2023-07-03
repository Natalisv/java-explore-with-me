package ru.practicum.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
public class CompilationDtoUpdate {

    private List<Long> events;

    private Boolean pinned;

    @Length(min = 1, max = 50)
    private String title;

}
