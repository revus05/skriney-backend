package com.example.skrineybackend.swagger.usersettings;

import com.example.skrineybackend.dto.usersettings.UpdateDefaultCategoryRequestDTO;
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
    summary = "Обновление категории по умолчанию",
    description = "Обновляет категорию по умолчанию у пользователя",
    requestBody =
        @RequestBody(
            description = "Новая категория по умолчанию",
            content =
                @Content(
                    schema = @Schema(implementation = UpdateDefaultCategoryRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                      "uuid": "8744afb6-ac40-4a81-a8a2-faaa9dc7aca8"
                    }"""))),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Категория по умолчанию успешно обновлена",
          content = @Content(schema = @Schema(implementation = UserSettingsDTO.class))),
    })
public @interface UpdateDefaultCategoryOperation {}
