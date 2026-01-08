package com.example.skrineybackend.swagger.usersettings;

import com.example.skrineybackend.dto.usersettings.UpdateLanguageRequestDTO;
import com.example.skrineybackend.dto.usersettings.UserSettingsDTO;
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
    summary = "Обновление языка пользователя",
    description = "Обновляет язык интерфейса пользователя",
    requestBody =
        @RequestBody(
            description = "Новый язык пользователя",
            content =
                @Content(
                    schema = @Schema(implementation = UpdateLanguageRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                      "language": "EN"
                    }"""))),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Язык интерфейса успешно обновлен",
          content = @Content(schema = @Schema(implementation = UserSettingsDTO.class))),
    })
public @interface UpdateLanguageOperation {}
