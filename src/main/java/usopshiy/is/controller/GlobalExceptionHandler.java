package usopshiy.is.controller;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import usopshiy.is.dto.MessageInfo;
import usopshiy.is.exception.AlreadyExistsException;
import usopshiy.is.exception.NotFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 — entity not found
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageInfo handleNotFound(NotFoundException ex) {
        log.warn("Not found: {}", ex.getMessage());
        return new MessageInfo(ex.getMessage());
    }

    // 409 — business rule conflict (e.g. duplicate username)
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public MessageInfo handleConflict(AlreadyExistsException ex) {
        log.warn("Conflict: {}", ex.getMessage());
        return new MessageInfo(ex.getMessage());
    }

    // 400 — @Valid/@Validated bean validation failure on @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageInfo handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", details);
        return new MessageInfo("Validation error: " + details);
    }

    // 400 — @Validated constraint violations on path/query params
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageInfo handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Constraint violation: {}", ex.getMessage());
        return new MessageInfo(ex.getMessage());
    }

    // 400 — any other known runtime exception (e.g. bad operation type, bad state)
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageInfo handleRuntime(RuntimeException ex) {
        log.warn("Runtime error: {}", ex.getMessage());
        return new MessageInfo(ex.getMessage());
    }

    // 500 — unexpected
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageInfo handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return new MessageInfo("Internal server error");
    }
}