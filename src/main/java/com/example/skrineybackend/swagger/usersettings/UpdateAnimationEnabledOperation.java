package com.example.skrineybackend.swagger.usersettings;

import com.example.skrineybackend.dto.usersettings.UpdateAnimationEnabledRequestDTO;
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
    summary = "Обновление отображения анимаций интерфейса",
    description = "Включает/аотключает анимации интерфейса пользователя",
    requestBody =
        @RequestBody(
            description = "Новое заничение для отображения анимаций",
            content =
                @Content(
                    schema = @Schema(implementation = UpdateAnimationEnabledRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                      "animationEnabled": true
                    }"""))),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Скорость анимаций успешно обновлена",
          content = @Content(schema = @Schema(implementation = UserSettingsDTO.class))),
    })
public @interface UpdateAnimationEnabledOperation {}
