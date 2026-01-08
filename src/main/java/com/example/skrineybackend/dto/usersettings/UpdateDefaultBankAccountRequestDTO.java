package com.example.skrineybackend.dto.usersettings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDefaultBankAccountRequestDTO {
  @Schema(description = "New bank account uuid", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Uuid счета обязателен")
  private String uuid;
}
