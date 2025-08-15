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
        Map<String, String> fields = new HashMap<>();
        fields.put(ex.getField(), ex.getMessage());
        return new Response("Запись с такими данными уже существует", HttpStatus.CONFLICT, fields);
    }

    @ExceptionHandler(NoBankAccountFoundException.class)
    public Response handleNoBankAccountFoundException(NoBankAccountFoundException ex) {
        return new Response(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoCategoryFoundException.class)
    public Response handleNoCategoryFoundException(NoCategoryFoundException ex) {
        return new Response(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public Response handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new Response(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoUserSettingsFoundException.class)
    public Response handleNoUserSettingsFoundException(NoUserSettingsFoundException ex) {
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