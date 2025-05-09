package domus.challenge.controller.advice;

import domus.challenge.exception.InvalidThresholdException;
import domus.challenge.model.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidThresholdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<CustomErrorResponse> handleInvalidThreshold(InvalidThresholdException ex) {
        return Mono.just(new CustomErrorResponse(
                "Bad Request",
                ex.getMessage(),
                Instant.now()
        ));
    }
}