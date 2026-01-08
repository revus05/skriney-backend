package com.example.skrineybackend.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Getter
public class Response extends ResponseEntity<Object> {

  public Response(String message, HttpStatusCode status) {
    super(new ResponseBody(status.value(), message), status);
  }

  public Response(String message, HttpStatusCode status, Object data) {
    super(new ResponseBody(status.value(), message, data), status);
  }
}
