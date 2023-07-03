package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(ValidationException e) {
        return Map.of("error", "Bad request");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleOtherExceptions(IllegalArgumentException e) {
        return Map.of("error", "Not found");
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> validationAnnotationHandler(MethodArgumentNotValidException e) {
        return ResponseEntity.status(400).body(e.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, Objects.requireNonNull(FieldError::getDefaultMessage))));
    }

}
