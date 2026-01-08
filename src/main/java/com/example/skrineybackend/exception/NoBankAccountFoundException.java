package com.example.skrineybackend.exception;

public class NoBankAccountFoundException extends RuntimeException {
  public NoBankAccountFoundException(String message) {
    super(message);
  }
}
