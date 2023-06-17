package ru.practicum.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Validated
public class CompilationDtoNew {

    private List<Long> events;

    private Boolean pinned;

    @NotEmpty
    @NotBlank
    @NotNull
    @Length(min = 1, max = 50)
    private String title;

    public CompilationDtoNew() {
    }

    ;

}
