package com.msvc.shopcart.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<Map<String, Object>> notFound(NotFoundException ex) {
        return Mono.just(Map.of("error", "NOT_FOUND", "message", ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<Map<String, Object>> conflict(ConflictException ex) {
        return Mono.just(Map.of("error", "CONFLICT", "message", ex.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String, Object>> badRequest(Exception ex) {
        return Mono.just(Map.of("error", "BAD_REQUEST", "message", ex.getMessage()));
    }
}

