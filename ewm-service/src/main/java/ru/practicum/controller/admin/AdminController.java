package ru.practicum.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ExistException;
import ru.practicum.model.User;
import ru.practicum.service.category.CategoryServiceImpl;
import ru.practicum.service.compilation.CompilationService;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userService;
    private final CategoryServiceImpl categoryService;

    private final EventService eventService;

    private final CompilationService compilationService;

    public AdminController(UserServiceImpl userService, CategoryServiceImpl categoryService,
                           EventService eventService, CompilationService compilationService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.compilationService = compilationService;
    }

    /*Пользователи*/

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(required = false) List<Long> ids,
                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return userService.getAllUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    /*Категории*/

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto)
            throws ConflictException {
        return categoryService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) throws ConflictException {
        categoryService.deleteCategory(catId);
    }

    /*События*/

    @GetMapping("/events")
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                               @RequestParam(required = false) List<String> states,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "0") Integer confirmedRequests,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) throws ExistException {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId, @Valid @RequestBody EventUpdateDto eventFullDto)
            throws ExistException {
        return eventService.updateEventByAdmin(eventId, eventFullDto);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody CompilationDtoNew compilation) {
        return compilationService.addCompilation(compilation);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@Valid @RequestBody CompilationDtoUpdate compilationDtoNew, @PathVariable Long compId) {
        return compilationService.updateCompilation(compilationDtoNew, compId);
    }

}
