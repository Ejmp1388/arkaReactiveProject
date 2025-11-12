package com.msvc.orders.web;


import com.msvc.orders.exception.ConflictException;
import com.msvc.orders.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<Map<String, Object>> handleNotFound(NotFoundException ex) {
        return Mono.just(Map.of(
                "error", "NOT_FOUND",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<Map<String, Object>> handleConflict(ConflictException ex) {
        return Mono.just(Map.of(
                "error", "CONFLICT",
                "message", ex.getMessage()
        ));
    }

    // catch-all para que no te vuelva a salir 500 en la cara
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String, Object>> handleGeneric(Exception ex) {
        return Mono.just(Map.of(
                "error", "BAD_REQUEST",
                "message", ex.getMessage()
        ));
    }
}
