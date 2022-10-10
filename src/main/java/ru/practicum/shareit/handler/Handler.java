package ru.practicum.shareit.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exception.*;

@RestControllerAdvice

public class Handler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundEx.class)
    public ResponseEntity<Object> handleNotFound(NotFoundEx exception, WebRequest request) {
        return new ResponseEntity<>(new Handler.ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentEx.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentEx exception, WebRequest request) {
        return new ResponseEntity<>(new Handler.ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Data
    private class ErrorResponse {
        @JsonProperty("error")
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}
