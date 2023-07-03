package ru.practicum.controller.pub;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatClient;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.exception.ExistException;
import ru.practicum.service.category.CategoryServiceImpl;
import ru.practicum.service.compilation.CompilationService;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
public class PublicController {

    private final CategoryServiceImpl categoryService;

    private final EventService eventService;

    private final StatClient statClient;

    private final CompilationService compilationService;

    public PublicController(CategoryServiceImpl categoryService, EventService eventService, StatClient statClient,
                            CompilationService compilationService) {
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.statClient = statClient;
        this.compilationService = compilationService;
    }

    /*Категории*/

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }

    /*События*/

    @GetMapping("/events")
    public List<EventShortDto> getEventsByAdmin(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                @Positive @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) String rangeStart,
                                                @RequestParam(required = false) String rangeEnd,
                                                @RequestParam(required = false) String sort,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                HttpServletRequest httpServletRequest) throws ExistException {
        statClient.postHit(httpServletRequest);

        return eventService.getEventsPublic(text, paid, onlyAvailable, categories, rangeStart, rangeEnd, sort, from, size);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEvent(@PathVariable Long eventId, HttpServletRequest httpServletRequest) {
        statClient.postHit(httpServletRequest);
        return eventService.getEvent(eventId);
    }

    /*Подборки*/

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return compilationService.getCompilations(pinned, from, size);

    }

    @GetMapping("/compilations/{id}")
    public CompilationDto getCompilations(@PathVariable Long id) {
        return compilationService.getCompilation(id);
    }
}
