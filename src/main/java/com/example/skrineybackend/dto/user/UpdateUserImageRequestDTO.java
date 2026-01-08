package com.example.skrineybackend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserImageRequestDTO {
  @Schema(description = "Image url", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Url изображения обязателен")
  private String image;
}
