package com.example.skrineybackend.exception;

public class NoUserSettingsFoundException extends RuntimeException {
  public NoUserSettingsFoundException(String message) {
    super(message);
  }
}
