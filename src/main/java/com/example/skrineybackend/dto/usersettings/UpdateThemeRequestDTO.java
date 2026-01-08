package com.example.skrineybackend.dto.usersettings;

import com.example.skrineybackend.enums.UserTheme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateThemeRequestDTO {
  @Schema(description = "New user's currency", requiredMode = Schema.RequiredMode.REQUIRED)
  private UserTheme theme;
}
