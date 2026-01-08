package com.example.skrineybackend.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
  private final String field;

  public UserAlreadyExistsException(String field, String message) {
    super(message);
    this.field = field;
  }
}
