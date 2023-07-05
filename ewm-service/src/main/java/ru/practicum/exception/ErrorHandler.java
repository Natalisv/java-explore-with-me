package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    public static final String ERROR = "error";

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> validationAnnotationHandler(MethodArgumentNotValidException e) {
        log.warn(ERROR, e.getMessage());
        return ResponseEntity.status(400).body(e.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, Objects.requireNonNull(FieldError::getDefaultMessage))));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> throwableHandler(Throwable e) {
        log.warn(ERROR, e.getMessage());
        return ResponseEntity.status(500).body(Map.of("serverError", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> misReqPatamHandler(MissingServletRequestParameterException e) {
        log.warn(ERROR, e.getMessage());
        return ResponseEntity.status(400).body(Map.of("missing request parameters", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.warn(ERROR, e.getMessage());
        return ResponseEntity.status(404).body(Map.of("reason", "The required object was not found.",
                "message", "Entity with id= was not found"));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> runtimeExceptionHandler(ConflictException e) {
        log.warn(ERROR, e.getMessage());
        return ResponseEntity.status(409).body(Map.of("reason", "For the requested operation the conditions are not met.",
                "message", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleExistExceptions(ExistException e) {
        return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> constrExceptionHandler(ConstraintViolationException e) {
        log.warn(ERROR, e.getMessage());
        return ResponseEntity.status(409).body(Map.of("reason", "For the requested operation the conditions are not met.",
                "message", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> integrityViolationExceptionHandlerc(DataIntegrityViolationException e) {
        log.warn(ERROR, e.getMessage());
        return ResponseEntity.status(409).body(Map.of("reason", "For the requested operation the conditions are not met.",
                "message", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.warn(ERROR, e.getMessage());
        return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
    }

}
