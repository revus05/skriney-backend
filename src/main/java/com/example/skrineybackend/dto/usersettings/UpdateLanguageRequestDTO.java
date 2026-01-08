package com.example.skrineybackend.dto.usersettings;

import com.example.skrineybackend.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateLanguageRequestDTO {
  @Schema(description = "New user's language", requiredMode = Schema.RequiredMode.REQUIRED)
  private Language language;
}
