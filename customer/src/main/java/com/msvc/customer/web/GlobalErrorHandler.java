package com.msvc.customer.web;


import com.msvc.customer.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ProblemDetail> onValidation(MethodArgumentNotValidException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation error");
        pd.setDetail(ex.getBindingResult().toString());
        return Mono.just(pd);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ProblemDetail> onIllegalArg(IllegalArgumentException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Not found");
        pd.setDetail(ex.getMessage());
        return Mono.just(pd);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<Map<String, Object>> handleNotFound(NotFoundException ex) {
        return Mono.just(Map.of(
                "error", "NOT_FOUND",
                "message", ex.getMessage()
        ));
    }
}
