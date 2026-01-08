package com.example.skrineybackend.dto.usersettings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDefaultCategoryRequestDTO {
  @Schema(description = "New category uuid", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Uuid категории обязателен")
  private String uuid;
}
