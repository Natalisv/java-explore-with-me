package ru.practicum.comment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ExistException;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{userId}/{eventId}")
    public Comment addComment(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody @NotNull Comment comment)
            throws ExistException {
        if (comment.getText() == null || comment.getText().isEmpty()) {
            throw new ExistException("Не указан текст комментария");
        }
        return commentService.addComment(userId, eventId, comment.getText());
    }

    @GetMapping("/{commentId}")
    public Comment getComment(@PathVariable Long commentId) throws ExistException {
        return commentService.getComment(commentId);
    }

    @DeleteMapping("/{userId}/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Comment deleteComment(@PathVariable Long userId, @PathVariable Long commentId)
            throws ExistException {
        return commentService.deleteComment(userId, commentId);
    }

    @PatchMapping("/{userId}/{commentId}")
    public Comment updateComment(@PathVariable Long userId, @PathVariable Long commentId, @RequestBody @NotNull Comment comment)
            throws ExistException, ConflictException {
        return commentService.updateComment(userId, commentId, comment.getText());
    }

}
