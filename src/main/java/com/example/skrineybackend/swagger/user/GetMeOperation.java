package com.example.skrineybackend.swagger.user;

import com.example.skrineybackend.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Авторизация пользователя по jwt",
    description = "Авторизует пользователя по jwt",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Пользователь успешно авторизован",
          content = @Content(schema = @Schema(implementation = UserDTO.class))),
    })
public @interface GetMeOperation {}
