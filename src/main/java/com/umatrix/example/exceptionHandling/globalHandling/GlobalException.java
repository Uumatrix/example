package com.umatrix.example.exceptionHandling.globalHandling;


import com.umatrix.example.exceptionHandling.CustomExceptions.UserAlreadyExists;
import com.umatrix.example.exceptionHandling.CustomExceptions.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFound ex, WebRequest request) {
        return new ResponseEntity<>("Not Found exception : " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExists ex, WebRequest request) {
        return new ResponseEntity<>("User already exists : " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    //for bean validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    //for userRole check
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>("Access Denied, user role does not have priority: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}
