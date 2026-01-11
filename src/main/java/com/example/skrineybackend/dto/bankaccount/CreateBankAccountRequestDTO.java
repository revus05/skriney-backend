package com.example.skrineybackend.dto.bankaccount;

import com.example.skrineybackend.enums.Currency;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBankAccountRequestDTO {
  @Schema(
      description = "Initial bank account balance",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private BigDecimal initialBalance;

  @Schema(description = "Bank account currency", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "Валюта обязательна")
  private Currency currency;

  @Schema(description = "Bank account title", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Название счета обязательно")
  @Size(max = 64, message = "Название счета не может быть длиннее 64 символов")
  private String title;

  @JsonCreator
  public CreateBankAccountRequestDTO(
      @JsonProperty("initialBalance") BigDecimal initialBalance,
      @JsonProperty("currency") Currency currency,
      @JsonProperty("title") String title) {
    this.initialBalance = initialBalance;
    this.currency = currency;
    this.title = title;
  }
}
