package ru.practicum.service.event;

import ru.practicum.dto.*;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ExistException;

import java.util.List;
import java.util.Map;

public interface EventService {

    EventFullDto addEvent(Long userId, EventFullDtoNew eventFullDtoNew) throws ConflictException, ExistException;

    List<EventShortDtoNew> getUserEvents(Long userId, Integer from, Integer size) throws ExistException;

    EventFullDto getUserEvent(Long userId, Long eventId) throws ExistException, IllegalAccessException;

    EventFullDto updateEvent(Long userId, Long eventId, EventUpdateDto eventFullDto) throws ConflictException,
            ExistException;

    ParticipationRequestDto getEventRequest(Long userId, Long eventId);

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart ,
                                        String rangeEnd , Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, EventUpdateDto eventFullDto) throws ExistException;

    EventFullDto getEvent(Long eventId);

    List<EventShortDto> getEventsPublic(String text, Boolean paid, Boolean onlyAvailable,  List<Long> categories,
                                       String rangeStart, String rangeEnd, String sort, Integer from, Integer size)
            throws ExistException;

    ParticipationRequestDto addRequestToEvent(Long userId, Long eventId) throws ConflictException, ExistException;

    List<ParticipationRequestDto> getUserRequest(Long userId) throws ExistException;

    ParticipationRequestDto cancelRequest(Long userId, Long requestId) throws ExistException;

    List<ParticipationRequestDto> getUserEventRequest(Long userId, Long eventId) throws ExistException;

    Map<String, List<ParticipationRequestDto>> updateStatus(RequestStatusUpdate requestStatusUpdate, Long userId,
                                                            Long eventId) throws ConflictException, ExistException;

}
