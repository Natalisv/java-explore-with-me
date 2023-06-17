package ru.practicum.controller.priv;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ExistException;
import ru.practicum.service.event.EventServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class PrivateController {

    private final EventServiceImpl eventService;

    public PrivateController(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId, @Valid @RequestBody EventFullDtoNew eventFullDtoNew)
            throws ConflictException, ExistException {
        return eventService.addEvent(userId, eventFullDtoNew);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDtoNew> getUserEvents(@PathVariable Long userId,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size)
            throws ExistException {
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) throws ExistException,
            IllegalAccessException {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateUserEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                        @Valid @RequestBody EventUpdateDto eventFullDto) throws ConflictException,
            ExistException {
        return eventService.updateEvent(userId, eventId, eventFullDto);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequestToEvent(@PathVariable Long userId,
                                                     @RequestParam(name = "eventId") Long eventId)
            throws ConflictException, ExistException {
        return eventService.addRequestToEvent(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequest(@PathVariable Long userId) throws ExistException {
        return eventService.getUserRequest(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId)
            throws ExistException {
        return eventService.cancelRequest(userId, requestId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventRequest(@PathVariable Long userId, @PathVariable Long eventId)
            throws ExistException {
        return eventService.getUserEventRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public Map<String, List<ParticipationRequestDto>> updateStatus(@RequestBody RequestStatusUpdate requestStatusUpdate,
                                                                   @PathVariable Long userId, @PathVariable Long eventId) throws ConflictException, ExistException {
        return eventService.updateStatus(requestStatusUpdate, userId, eventId);
    }
}
