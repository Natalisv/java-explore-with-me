package ru.practicum.service.compilation;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CompilationDtoNew;
import ru.practicum.dto.CompilationDtoUpdate;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation(CompilationDtoNew compilationDto);

    void deleteCompilation(Long id);

    CompilationDto updateCompilation(CompilationDtoUpdate compilationDtoNew, Long id);

    CompilationDto getCompilation(Long id);

    List<CompilationDto> getCompilations(Boolean pinned , Integer from, Integer size);
}
