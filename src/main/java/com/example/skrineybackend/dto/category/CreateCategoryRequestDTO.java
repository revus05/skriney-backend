package com.example.skrineybackend.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateCategoryRequestDTO {
  @Schema(description = "Category title", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Название категории обязательно")
  @Size(max = 64, message = "Название категории не может быть длиннее 64 символов")
  private String title;

  @Schema(description = "Category emoji", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String emoji;
}
