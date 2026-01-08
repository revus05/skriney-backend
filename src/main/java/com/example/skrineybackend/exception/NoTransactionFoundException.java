package com.example.skrineybackend.exception;

public class NoTransactionFoundException extends RuntimeException {
  public NoTransactionFoundException(String message) {
    super(message);
  }
}
