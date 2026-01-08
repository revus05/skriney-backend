package com.example.skrineybackend.swagger.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Выход пользователя",
    description = "Удаляет jwt куки",
    responses = {
      @ApiResponse(responseCode = "200", description = "Пользователь успешно вышел"),
    })
public @interface SignOutOperation {}
