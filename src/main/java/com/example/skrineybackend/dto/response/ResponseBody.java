package com.example.skrineybackend.dto.response;

public record ResponseBody(int status, String message, Object data) {
  public ResponseBody(int status, String message) {
    this(status, message, null);
  }
}
