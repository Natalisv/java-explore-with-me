package ru.practicum.service.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CompilationDtoNew;
import ru.practicum.dto.CompilationDtoUpdate;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompilationServiceImpl implements CompilationService {

    private final EventRepository eventRepository;

    private final CompilationRepository compilationRepository;

    private final CompilationMapper compilationMapper;

    public CompilationServiceImpl(EventRepository eventRepository, CompilationRepository compilationRepository, CompilationMapper compilationMapper) {
        this.eventRepository = eventRepository;
        this.compilationRepository = compilationRepository;
        this.compilationMapper = compilationMapper;
    }

    @Override
    public CompilationDto addCompilation(CompilationDtoNew compilationDto) {
        if (compilationDto.getEvents() != null && !compilationDto.getEvents().isEmpty()) {
            compilationDto.getEvents().forEach(c -> {
                Event event = eventRepository.findById(c).get();
                event.setCompilation(c);
            });
        }
        Compilation savedCompilation = compilationRepository.save(CompilationMapper.toCompilation(compilationDto));
        return compilationMapper.toCompilationDto(savedCompilation, compilationDto);
    }

    @Override
    public void deleteCompilation(Long id) {
        if (compilationRepository.findById(id).isPresent()) {
            List<Event> list = eventRepository.findByCompilation(id);
            if (list != null && !list.isEmpty()) {
                list.forEach(e -> e.setCompilation(null));
            }
            compilationRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public CompilationDto updateCompilation(CompilationDtoUpdate compilationDtoNew, Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException();
        });
        if (compilationDtoNew.getEvents() != null) {
            List<Event> list = eventRepository.findByCompilation(id);
            list.forEach(e -> e.setCompilation(null));
            compilationDtoNew.getEvents().forEach(c -> {
                Event event = eventRepository.findById(c).get();
                event.setCompilation(id);
                eventRepository.save(event);
            });
        }

        if (compilationDtoNew.getPinned() != null) {
            compilation.setPinned(compilationDtoNew.getPinned());
        }
        if (compilationDtoNew.getTitle() != null) {
            compilation.setTitle(compilationDtoNew.getTitle());
        }
        return compilationMapper.toCompilationDto(compilation, compilationDtoNew);
    }

    @Override
    public CompilationDto getCompilation(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException();
        });
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> list;
        if (pinned != null) {
            list = compilationRepository.findByPinned(pinned);
        } else {
            Pageable page = PageRequest.of(from, size);
            Page<Compilation> compilationPage = compilationRepository.findAll(page);
            list = new ArrayList<>(compilationPage.getContent());
        }
        if (list != null && !list.isEmpty()) {
            List<CompilationDto> compilationDtoList = list.stream().map(compilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
            return compilationDtoList.stream().skip(from).limit(size).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }

    }
}
