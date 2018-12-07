package br.dev.valmirt.login.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NotFoundException exc) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),
                new Date(System.currentTimeMillis()));

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(AuthorizationException exc) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                exc.getMessage(),
                new Date(System.currentTimeMillis()));

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(DataException exc) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.PRECONDITION_FAILED.value(),
                exc.getMessage(),
                new Date(System.currentTimeMillis()));

        return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(AccessDeniedException exc) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                exc.getMessage(),
                new Date(System.currentTimeMillis()));

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
