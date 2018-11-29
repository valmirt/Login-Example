package br.dev.valmirt.login.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    // Add an exception handler for CustomerNotFoundException

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NotFoundException exc) {

        // create CustomerErrorResponse

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        // return ResponseEntity

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(AuthorizationException exc) {

        // create CustomerErrorResponse

        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        // return ResponseEntity

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(DataException exc) {

        // create CustomerErrorResponse

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        // return ResponseEntity

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


//    // Add another exception handler ... to catch any exception (catch all)
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> handleException(Exception exc) {
//
//        // create CustomerErrorResponse
//
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                exc.getMessage(),
//                System.currentTimeMillis());
//
//        // return ResponseEntity
//
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
}
