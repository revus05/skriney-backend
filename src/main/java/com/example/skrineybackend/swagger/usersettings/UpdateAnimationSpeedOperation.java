package com.example.skrineybackend.swagger.usersettings;

import com.example.skrineybackend.dto.usersettings.UpdateAnimationSpeedRequestDTO;
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
    summary = "Обновление скорости анимаций интерфейса",
    description = "Обновляет скорость анимаций интерфейса пользователя",
    requestBody = @RequestBody(
        description = "Новая скорость анимаций",
        content = @Content(
            schema = @Schema(implementation = UpdateAnimationSpeedRequestDTO.class),
            examples = @ExampleObject(
                name = "Пример запроса",
                value = """
                    {
                      "animationSpeed": "SLOW"
                    }"""
            )
        )
    ),
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Скорость анимаций успешно обновлена",
            content = @Content(schema = @Schema(implementation = UserSettingsDTO.class))
        ),
    }
)
public @interface UpdateAnimationSpeedOperation {
}
