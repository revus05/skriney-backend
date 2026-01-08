package com.example.skrineybackend.swagger.user;

import com.example.skrineybackend.dto.user.SignInUserRequestDTO;
import com.example.skrineybackend.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Авторизация пользователя",
    description = "Авторизует пользователя",
    requestBody =
        @RequestBody(
            description = "Данные для авторизации пользователя",
            content =
                @Content(
                    schema = @Schema(implementation = SignInUserRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                        "email": "jwtson6@example.comm",
                        "password": "securePassword123312"
                    }"""))),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Пользователь успешно авторизован",
          content = @Content(schema = @Schema(implementation = UserDTO.class))),
    })
public @interface SignInOperation {}
