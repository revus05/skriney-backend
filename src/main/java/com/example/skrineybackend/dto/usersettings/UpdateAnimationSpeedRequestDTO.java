package com.example.skrineybackend.dto.usersettings;

import com.example.skrineybackend.enums.AnimationSpeed;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAnimationSpeedRequestDTO {
    @Schema(description = "New animation speed", requiredMode = Schema.RequiredMode.REQUIRED)
    private AnimationSpeed animationSpeed;
}
