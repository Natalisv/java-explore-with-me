package ru.practicum.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.*;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ExistException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.Request;
import ru.practicum.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;

    private final EventMapper eventMapper;

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            LocationRepository locationRepository, CategoryRepository categoryRepository,
                            RequestRepository requestRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public EventFullDto addEvent(Long userId, EventFullDtoNew eventFullDtoNew) throws ExistException {
        if (userRepository.findById(userId).isPresent() && categoryRepository.findById(eventFullDtoNew.getCategory()).isPresent()) {
            if (Boolean.TRUE.equals(checkTime(eventFullDtoNew))) {
                Event event = eventMapper.toEvent(eventFullDtoNew);
                Location location = locationRepository.save(eventFullDtoNew.getLocation());
                event.setInitiator(userId);
                event.setState(State.PENDING);
                event.setLocation(location.getId());
                Event savedEvent = eventRepository.save(event);
                return eventMapper.toEventFullDto(savedEvent);
            } else {
                throw new ExistException("Указанная дата не актуальна");
            }
        } else {
            throw new ExistException("Invalid request");
        }
    }

    @Override
    public List<EventShortDtoNew> getUserEvents(Long userId, Integer from, Integer size) throws ExistException {
        if (userRepository.findById(userId).isPresent()) {
            List<Event> listEvent = eventRepository.findByInitiator(userId);
            List<EventShortDtoNew> list = listEvent.stream().map(eventMapper::toEventShortDtoNew).collect(Collectors.toList());
            return list != null ? list.stream().skip(from).limit(size).collect(Collectors.toList()) : Collections.emptyList();
        } else {
            throw new ExistException("Invalid request");
        }
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) throws ExistException, IllegalAccessException {
        if (userRepository.findById(userId).isPresent()) {
            Event event = eventRepository.findById(eventId).get();
            if (Objects.equals(event.getInitiator(), userId)) {
                return eventMapper.toEventFullDto(event);
            } else {
                throw new ExistException("Invalid request");
            }
        } else {
            throw new IllegalAccessException();
        }
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, EventUpdateDto eventFullDto) throws ConflictException,
            ExistException {
        if (userRepository.findById(userId).isPresent()) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> {
                throw new IllegalArgumentException();
            });
            if (event.getState().equals(State.CANCELED) || event.getState().equals(State.PENDING)) {
                Event updatedEvent = this.updateEvent(event, eventFullDto);
                if (eventFullDto.getStateAction() != null && eventFullDto.getStateAction().equals("CANCEL_REVIEW")) {
                    updatedEvent.setState(State.CANCELED);
                }
                return eventMapper.toEventFullDto(eventRepository.save(updatedEvent));
            } else {
                throw new ConflictException("Событие не удовлетворяет правилам редактирования");
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public ParticipationRequestDto getEventRequest(Long userId, Long eventId) {
        return null;
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<Event> listEvent = new ArrayList<>();
        List<EventFullDto> listEventFullDto;
        if (users != null && categories != null) {
            listEvent = eventRepository.findByUsersAndCategories(users.toArray(new Long[0]), categories.toArray(new Long[0]));
        } else if (users != null) {
            listEvent = eventRepository.findByUsers(users.toArray(new Long[0]));
        } else if (categories != null) {
            listEvent = eventRepository.findByCategories(categories.toArray(new Long[0]));
        } else {
            Pageable page = PageRequest.of(from, size);
            Page<Event> eventPage = eventRepository.findAll(page);
            listEvent.addAll(eventPage.getContent());
        }
        if (rangeStart != null && rangeEnd != null) {
            LocalDateTime rangeStartDate = LocalDateTime.parse(rangeStart,
                    DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
            LocalDateTime rangeEndDate = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
            listEvent = listEvent.stream()
                    .filter(e -> e.getEventDate().isAfter(rangeStartDate) && e.getEventDate().isBefore(rangeEndDate))
                    .collect(Collectors.toList());
        } else if (rangeStart != null) {
            LocalDateTime rangeStartDate = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
            listEvent = listEvent.stream().filter(e -> e.getEventDate().isAfter(rangeStartDate))
                    .collect(Collectors.toList());
        } else if (rangeEnd != null) {
            LocalDateTime rangeEndDate = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
            listEvent = listEvent.stream().filter(e -> e.getEventDate().isBefore(rangeEndDate))
                    .collect(Collectors.toList());
        }
        if (states != null) {
            listEvent = listEvent.stream().filter(e -> states.contains(e.getState().toString()))
                    .collect(Collectors.toList());
        }
        listEventFullDto = listEvent.stream().map(eventMapper::toEventFullDto).collect(Collectors.toList());

        return listEventFullDto != null ? listEventFullDto.stream().skip(from).limit(size)
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public EventFullDto getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new IllegalArgumentException();
        });
        if (event.getState().equals(State.PUBLISHED)) {
            return eventMapper.toEventFullDto(event);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, Boolean paid, Boolean onlyAvailable, List<Long> categories,
                                               String rangeStart, String rangeEnd, String sort, Integer from, Integer size)
            throws ExistException {
        List<Event> listEvent;
        List<EventShortDto> eventShortDto;
        if (categories != null) {
            listEvent = eventRepository.findByCategories(categories.toArray(new Long[0]));
        } else {
            listEvent = eventRepository.findAll();
        }

        listEvent = listEvent.stream().filter(e -> e.getState().equals(State.PUBLISHED)).collect(Collectors.toList());

        if (rangeStart != null && rangeEnd != null) {
            LocalDateTime rangeStartDate = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
            LocalDateTime rangeEndDate = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
            if (rangeStartDate.isAfter(rangeEndDate)) {
                throw new ExistException("Дата начала события должна быть позже окончания события");
            }
            listEvent = listEvent.stream()
                    .filter(e -> e.getEventDate().isAfter(rangeStartDate) && e.getEventDate().isBefore(rangeEndDate))
                    .collect(Collectors.toList());
        } else if (rangeStart != null) {
            LocalDateTime rangeStartDate = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
            listEvent = listEvent.stream().filter(e -> e.getEventDate().isAfter(rangeStartDate))
                    .collect(Collectors.toList());
        } else if (rangeEnd != null) {
            LocalDateTime rangeEndDate = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
            listEvent = listEvent.stream().filter(e -> e.getEventDate().isBefore(rangeEndDate))
                    .collect(Collectors.toList());
        } else {
            listEvent = listEvent.stream().filter(e -> e.getEventDate().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }
        if (text != null) {
            listEvent = listEvent.stream().filter(e -> e.getAnnotation().toLowerCase().contains(text.toLowerCase()) ||
                            e.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (paid != null) {
            listEvent = listEvent.stream().filter(e -> e.getPaid().equals(paid))
                    .collect(Collectors.toList());
        }
        if (sort != null && !sort.isEmpty()) {
            if (sort.equals("EVENT_DATE")) {
                listEvent = listEvent.stream()
                        .sorted(Comparator.comparing(Event::getEventDate)).collect(Collectors.toList());
            } else if (sort.equals("EVENT_VIEWS")) {
                listEvent = listEvent
                        .stream().sorted(Comparator.comparing(Event::getViews)).collect(Collectors.toList());
            }
        }

        eventShortDto = listEvent.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());

        return eventShortDto != null ? eventShortDto.stream().skip(from).limit(size)
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, EventUpdateDto eventFullDto) throws ExistException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new IllegalArgumentException();
        });
        Event updatedEvent = this.updateEvent(event, eventFullDto);
        if (eventFullDto.getStateAction() != null) {
            if (eventFullDto.getStateAction().equals("PUBLISH_EVENT") &&
                    event.getState().equals(State.PENDING)) {
                updatedEvent.setState(State.PUBLISHED);
            } else if (event.getState().equals(State.PENDING) && eventFullDto.getStateAction().equals("CANCEL_EVENT")) {
                updatedEvent.setState(State.CANCELED);
            } else {
                throw new DataIntegrityViolationException("Событие не удовлетворяет правилам редактирования");
            }
        }
        return eventMapper.toEventFullDto(eventRepository.save(updatedEvent));
    }

    @Override
    public ParticipationRequestDto addRequestToEvent(Long userId, Long eventId) throws ConflictException, ExistException {
        if (userRepository.findById(userId).isPresent()) {
            Event event = eventRepository.findById(eventId).get();
            if (!Objects.equals(event.getInitiator(), userId) && event.getState().equals(State.PUBLISHED) &&
                    requestRepository.findByEventAndRequester(eventId, userId) == null) {
                if (event.getConfirmedRequests() != null && event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    throw new ConflictException("Достигнут лимит запросов на участие");
                }
                Request request = Request.builder()
                        .requester(userId)
                        .created(LocalDateTime.now())
                        .event(eventId)
                        .build();
                if (event.getRequestModeration().equals(Boolean.FALSE) || event.getParticipantLimit() == 0) {
                    request.setStatus(State.CONFIRMED.toString());
                } else {
                    request.setStatus(State.PENDING.toString());
                }
                event.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() + 1 : 1);
                Request savedRequest = requestRepository.save(request);
                return RequestMapper.toRequestDto(savedRequest);
            } else {
                throw new ConflictException("Невозможно добавить запрос");
            }
        } else {
            throw new ExistException("Пользователь не найден");
        }
    }

    @Override
    public List<ParticipationRequestDto> getUserRequest(Long userId) throws ExistException {
        if (userRepository.findById(userId).isPresent()) {
            List<Request> list = requestRepository.findByRequester(userId);
            if (list == null || list.isEmpty()) {
                return Collections.emptyList();
            } else {
                return list.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
            }
        } else {
            throw new ExistException("Пользователь не найден");
        }
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) throws ExistException {
        if (userRepository.findById(userId).isPresent()) {
            Request request = requestRepository.findById(requestId).orElseThrow(() -> {
                throw new IllegalArgumentException();
            });
            if (!request.getStatus().equals(State.CANCELED.toString())) {
                request.setStatus(State.CANCELED.toString());
                Event event = eventRepository.findById(request.getEvent()).orElseThrow(() -> {
                    throw new IllegalArgumentException();
                });
                event.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() - 1 : 0);
                return RequestMapper.toRequestDto(request);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new ExistException("Пользователь не найден");
        }
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequest(Long userId, Long eventId) throws ExistException {
        if (userRepository.findById(userId).isPresent()) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> {
                throw new IllegalArgumentException();
            });
            if (event.getInitiator().equals(userId)) {
                List<Request> list = requestRepository.findByEvent(eventId);
                if (list != null && !list.isEmpty()) {
                    return list.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new ExistException("Пользователь не найден");
        }
    }

    @Override
    public Map<String, List<ParticipationRequestDto>> updateStatus(RequestStatusUpdate requestStatusUpdate,
                                                                   Long userId, Long eventId)
            throws ConflictException, ExistException {
        if (userRepository.findById(userId).isPresent()) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> {
                throw new IllegalArgumentException();
            });
            List<Long> requestsIds = requestStatusUpdate.getRequestIds();
            List<Request> requests = requestRepository.getByEventAndId(eventId, requestsIds.toArray(new Long[0]));
            List<Request> confirmedRequests = new ArrayList<>();
            List<Request> rejectedRequests = new ArrayList<>();
            Map<String, List<ParticipationRequestDto>> result = new HashMap<>();

            if (event.getInitiator().equals(userId)) {
                if (event.getRequestModeration().equals(Boolean.FALSE) || event.getParticipantLimit() == 0) {
                    return new HashMap<>();
                } else if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                    throw new ConflictException("Достигнут лимит по заявка на данное событие");
                } else {
                    if (requests != null && !requests.isEmpty()) {
                        for (Request request : requests) {
                            if (requestStatusUpdate.getStatus().equals(State.CONFIRMED.toString())) {
                                if (request.getStatus().equals(State.PENDING.toString()) &&
                                        event.getConfirmedRequests() < event.getParticipantLimit()) {
                                    request.setStatus(requestStatusUpdate.getStatus());
                                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                                    confirmedRequests.add(request);
                                } else {
                                    throw new ConflictException("Достигнут лимит по заявка на данное событие");
                                }
                            } else {
                                request.setStatus(requestStatusUpdate.getStatus());
                                rejectedRequests.add(request);
                            }
                        }
                        result.put("confirmedRequests", confirmedRequests
                                .stream().map(RequestMapper::toRequestDto).collect(Collectors.toList()));
                        result.put("rejectedRequests", rejectedRequests
                                .stream().map(RequestMapper::toRequestDto).collect(Collectors.toList()));
                        return result;
                    } else {
                        throw new ConflictException("Достигнут лимит по заявка на данное событие");
                    }
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new ExistException("Пользователь не найден");
        }
    }

    private Boolean checkTime(EventFullDtoNew eventFullDtoNew) {
        String eventDate = eventFullDtoNew.getEventDate();
        LocalDateTime eventDateDate = LocalDateTime.parse(eventDate, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
        eventDateDate = eventDateDate.plusHours(2);
        return eventDateDate.isAfter(LocalDateTime.now());
    }

    private Boolean checkTime(EventUpdateDto eventFullDto) {
        String eventDate = eventFullDto.getEventDate();
        LocalDateTime eventDateDate = LocalDateTime.parse(eventDate, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
        eventDateDate = eventDateDate.plusHours(2);
        return eventDateDate.isAfter(LocalDateTime.now());
    }

    private Event updateEvent(Event event, EventUpdateDto eventFullDto) throws ExistException {
        if (eventFullDto.getEventDate() != null) {
            if (Boolean.TRUE.equals(checkTime(eventFullDto))) {
                event.setEventDate(LocalDateTime.parse(eventFullDto.getEventDate(),
                        DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS)));
            } else {
                throw new ExistException("Запрос составлен некорректно");
            }
        }

        if (eventFullDto.getAnnotation() != null) {
            event.setAnnotation(eventFullDto.getAnnotation());
        }
        if (eventFullDto.getDescription() != null) {
            event.setDescription(eventFullDto.getDescription());
        }
        if (eventFullDto.getLocation() != null) {
            Location location = locationRepository.save(eventFullDto.getLocation());
            event.setLocation(location.getId());
        }
        if (eventFullDto.getCategory() != null && categoryRepository.findById(eventFullDto.getCategory()).isPresent()) {
            event.setCategory(eventFullDto.getCategory());
        }
        if (eventFullDto.getPaid() != null) {
            event.setPaid(eventFullDto.getPaid());
        }
        if (eventFullDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventFullDto.getParticipantLimit());
        }
        if (eventFullDto.getRequestModeration() != null) {
            event.setRequestModeration(eventFullDto.getRequestModeration());
        }
        if (eventFullDto.getTitle() != null) {
            event.setTitle(eventFullDto.getTitle());
        }
        return event;
    }
}
