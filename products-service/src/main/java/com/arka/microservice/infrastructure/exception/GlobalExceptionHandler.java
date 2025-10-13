package com.arka.microservice.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return Mono.just(Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "message", ex.getMessage()
        ));
    }
}
