package ru.practicum.shareit.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;

@ControllerAdvice

public class Handler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundEx.class)
    public ResponseEntity<Object> handleNotFound(NotFoundEx exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentEx.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentEx exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private class ErrorResponse {
        @JsonProperty("error")
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}
