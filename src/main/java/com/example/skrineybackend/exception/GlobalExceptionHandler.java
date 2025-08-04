package com.example.skrineybackend.exception;

import com.example.skrineybackend.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new Response("Ошибка валидации", HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public Response handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new Response(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public Response handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new Response(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoUserFoundException.class)
    public Response handleNoUserFoundException(NoUserFoundException ex) {
        return new Response(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public Response handleGeneralException(Exception ex) {
        return new Response("Неизвестная ошибка: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}