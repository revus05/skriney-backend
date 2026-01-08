package com.example.skrineybackend.exception;

import com.example.skrineybackend.dto.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public Response handleHttpMessageNotReadableException() {
    return new Response("Неверный формат JSON", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Response handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }

    return new Response("Ошибка валидации", HttpStatus.BAD_REQUEST, errors);
  }

  @ExceptionHandler(InvalidTransactionAmount.class)
  public Response InvalidTransactionAmount(InvalidTransactionAmount ex) {
    return new Response(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public Response handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
    Map<String, String> fields = new HashMap<>();
    fields.put(ex.getField(), ex.getMessage());
    return new Response("Запись с такими данными уже существует", HttpStatus.CONFLICT, fields);
  }

  @ExceptionHandler(NoBankAccountFoundException.class)
  public Response handleNoBankAccountFoundException(NoBankAccountFoundException ex) {
    return new Response(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NoCategoryFoundException.class)
  public Response handleNoCategoryFoundException(NoCategoryFoundException ex) {
    return new Response(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public Response handleInvalidCredentialsException(UnauthorizedException ex) {
    return new Response(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(NoUserSettingsFoundException.class)
  public Response handleNoUserSettingsFoundException(NoUserSettingsFoundException ex) {
    return new Response(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NoTransactionFoundException.class)
  public Response handleNoTransactionFoundException(NoTransactionFoundException ex) {
    return new Response(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public Response handleGeneralException(Exception ex) {
    return new Response("Неизвестная ошибка: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
