package com.example.skrineybackend.exception;

public class InvalidTransactionAmount extends RuntimeException {
  public InvalidTransactionAmount(String message) {
    super(message);
  }
}
