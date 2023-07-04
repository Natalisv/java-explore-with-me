package ru.practicum.comment;

import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ExistException;

public interface CommentService {

    Comment addComment(Long userId, Long eventId, String text) throws ExistException;

    Comment getComment(Long id) throws ExistException;

    Comment deleteComment(Long userId, Long id) throws ExistException;

    Comment updateComment(Long userId, Long id, String text) throws ExistException, ConflictException;
}
