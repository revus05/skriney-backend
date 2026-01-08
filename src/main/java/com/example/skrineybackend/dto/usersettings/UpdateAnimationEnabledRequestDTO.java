package com.example.skrineybackend.dto.usersettings;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAnimationEnabledRequestDTO {
  @Schema(description = "New animation enabled state", requiredMode = Schema.RequiredMode.REQUIRED)
  private boolean animationEnabled;
}
