package ru.practicum.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ExistException;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Comment addComment(Long userId, Long eventId, String text) throws ExistException {
        if (userRepository.findById(userId).isPresent() && eventRepository.findById(eventId).isPresent()) {
            Comment comment = new Comment();
            comment.setText(text);
            comment.setAuthorId(userId);
            comment.setEventId(eventId);
            comment.setDate(LocalDateTime.now());
            return commentRepository.save(comment);
        } else {
            throw new ExistException("Пользователь или событие не найден");
        }
    }

    @Override
    public Comment getComment(Long id) throws ExistException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException();
        });
        if (comment != null) {
            return comment;
        } else {
            throw new ExistException("Комментарий не найден");
        }
    }

    @Override
    public Comment deleteComment(Long userId, Long id) throws ExistException {
        if (userRepository.findById(userId).isPresent() && commentRepository.findById(id).isPresent() &&
                commentRepository.findById(id).get().getAuthorId().equals(userId)) {
            commentRepository.deleteById(id);
        } else {
            throw new ExistException("Пользователь или комментарий не найден");
        }
        return null;
    }

    @Override
    public Comment updateComment(Long userId, Long id, String text) throws ExistException, ConflictException {
        if (userRepository.findById(userId).isPresent() && commentRepository.findById(id).isPresent()) {
            Comment comment = commentRepository.findById(id).get();
            if (comment.getAuthorId().equals(userId)) {
                comment.setText(text);
                comment.setDate(LocalDateTime.now());
                return commentRepository.save(comment);
            } else {
                throw new ConflictException("Пользователь не может редактировать комментарий");
            }
        } else {
            throw new ExistException("Пользователь или комментарий не найден");
        }
    }

}
