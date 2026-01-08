package com.example.skrineybackend.swagger.user;

import com.example.skrineybackend.dto.user.UpdateUserImageRequestDTO;
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
    summary = "Обновление изображения профиля пользователя",
    description = "Обновляет изображение профиля пользователя",
    requestBody =
        @RequestBody(
            description = "Данные для обновления изображения профиля",
            content =
                @Content(
                    schema = @Schema(implementation = UpdateUserImageRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                        "url": "/files/{uuid}"
                    }"""))),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Изображение профиля успешно изменено",
          content = @Content(schema = @Schema(implementation = UserDTO.class))),
    })
public @interface UpdateUserImageOperation {}
