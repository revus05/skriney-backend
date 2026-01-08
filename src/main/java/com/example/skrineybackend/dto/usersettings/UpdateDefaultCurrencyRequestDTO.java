package com.example.skrineybackend.dto.usersettings;

import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateDefaultCurrencyRequestDTO {
  @Schema(description = "New user's currency", requiredMode = Schema.RequiredMode.REQUIRED)
  private Currency currency;
}
