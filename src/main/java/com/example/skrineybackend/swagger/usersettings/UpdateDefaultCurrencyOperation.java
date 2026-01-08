package com.example.skrineybackend.swagger.usersettings;

import com.example.skrineybackend.dto.usersettings.UpdateDefaultCurrencyRequestDTO;
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
    summary = "Обновление валюты по умолчанию",
    description = "Обновляет валюту по умолчанию у пользователя",
    requestBody =
        @RequestBody(
            description = "Новая валюта по умолчанию",
            content =
                @Content(
                    schema = @Schema(implementation = UpdateDefaultCurrencyRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                      "currency": "BYN"
                    }"""))),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Валюта по умолчанию успешно обновлена",
          content = @Content(schema = @Schema(implementation = UserSettingsDTO.class))),
    })
public @interface UpdateDefaultCurrencyOperation {}
