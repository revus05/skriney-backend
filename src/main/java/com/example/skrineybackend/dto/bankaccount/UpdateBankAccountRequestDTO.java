package com.example.skrineybackend.dto.bankaccount;

import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateBankAccountRequestDTO {
  @Schema(description = "Bank Account currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private Currency currency;

  @Schema(description = "Bank Account title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String title;

  @Schema(description = "Bank Account emoji", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String emoji;

  public UpdateBankAccountRequestDTO(Currency currency, String title, String emoji) {
    this.currency = currency;
    this.title = title;
    this.emoji = emoji;
  }
}
