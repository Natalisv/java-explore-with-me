package ru.practicum.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CompilationDtoNew;
import ru.practicum.dto.CompilationDtoUpdate;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CompilationMapper {

    private final EventRepository eventRepository;

    public CompilationMapper(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public static Compilation toCompilation(CompilationDtoNew compilationDtoNew) {
        return Compilation.builder()
                .pinned(compilationDtoNew.getPinned() != null ? compilationDtoNew.getPinned() : false)
                .title(compilationDtoNew.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation, CompilationDtoNew compilationDto) {
        List<Event> list = new ArrayList<>();
        if (compilationDto.getEvents() != null) {
            list = eventRepository.findByIds(compilationDto.getEvents().toArray(new Long[0]));
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(list)
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation, CompilationDtoUpdate compilationDto) {
        List<Event> list = new ArrayList<>();
        if (compilationDto.getEvents() != null) {
            list = eventRepository.findByIds(compilationDto.getEvents().toArray(new Long[0]));
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(list)
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        List<Event> list = eventRepository.findByCompilation(compilation.getId());
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(list.isEmpty() ? Collections.emptyList() : list)
                .build();
    }

}
