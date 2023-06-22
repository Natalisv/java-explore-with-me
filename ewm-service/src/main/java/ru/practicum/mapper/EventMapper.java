package ru.practicum.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.ViewStats;
import ru.practicum.ViewStatsRequest;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventFullDtoNew;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.EventShortDtoNew;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventMapper {

    private final CategoryRepository categoryRepository;


    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final StatClient statClient;

    public EventMapper(CategoryRepository categoryRepository, UserRepository userRepository,
                       LocationRepository locationRepository, StatClient statClient) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.statClient = statClient;
    }

    public Event toEvent(EventFullDtoNew eventFullDtoNew) {
        return Event.builder()
                .annotation(eventFullDtoNew.getAnnotation())
                .category(eventFullDtoNew.getCategory())
                .description(eventFullDtoNew.getDescription())
                .createdOn(LocalDateTime.now())
                .eventDate(LocalDateTime.parse(eventFullDtoNew.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .paid(eventFullDtoNew.getPaid() != null ? eventFullDtoNew.getPaid() : false)
                .participantLimit(eventFullDtoNew.getParticipantLimit() != null ? eventFullDtoNew.getParticipantLimit() : 0)
                .requestModeration(eventFullDtoNew.getRequestModeration())
                .title(eventFullDtoNew.getTitle())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        List<ViewStats> list = statClient.getStats(
                new ViewStatsRequest("2000-01-01 00:00:00", "2100-01-01 00:00:00", List.of("/events/" + event.getId()),
                        true));
        Long views = null;
        if (list != null) {
            views = list.get(0).getHits();
        }
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(categoryRepository.findById(event.getCategory()).get()))
                .confirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0)
                .description(event.getDescription())
                .createdOn(String.valueOf(event.getCreatedOn()))
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiator(userRepository.findById(event.getInitiator()).get())
                .location(locationRepository.findById(event.getLocation()).get())
                .paid(event.getPaid() != null ? event.getPaid() : false)
                .participantLimit(event.getParticipantLimit())
                .publishedOn(String.valueOf(event.getPublishedOn()))
                .requestModeration(event.getRequestModeration() != null ? event.getRequestModeration() : true)
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public EventShortDtoNew toEventShortDtoNew(Event event) {
        return EventShortDtoNew.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .categoryDto(CategoryMapper.toCategoryDto(categoryRepository.findById(event.getCategory()).get()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiator(userRepository.findById(event.getInitiator()).get())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(categoryRepository.findById(event.getCategory()).get()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiator(userRepository.findById(event.getInitiator()).get())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

}
