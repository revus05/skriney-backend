package com.example.skrineybackend.dto.bankaccount;

import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankAccountDTO {
  @Schema(description = "Bank Account uuid", requiredMode = Schema.RequiredMode.REQUIRED)
  private String uuid;

  @Schema(description = "Money on the bank account", requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal balanceInUsd;

  @Schema(description = "Each currency balance", requiredMode = Schema.RequiredMode.REQUIRED)
  private Map<Currency, BigDecimal> currencyBalances;

  @Schema(description = "Bank Account title", requiredMode = Schema.RequiredMode.REQUIRED)
  private String title;

  @Schema(description = "Bank Account emoji", requiredMode = Schema.RequiredMode.REQUIRED)
  private String emoji;

  @Schema(
      description = "Creation timestamp",
      type = "string",
      format = "date-time",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Instant createdAt;

  @Schema(
      description = "Last update timestamp",
      type = "string",
      format = "date-time",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Instant updatedAt;

  public BankAccountDTO(BankAccount bankAccount, Map<Currency, BigDecimal> currencyBalances) {
    this.uuid = bankAccount.getUuid();
    this.balanceInUsd = bankAccount.getBalanceInUsd();
    this.currencyBalances = currencyBalances;
    this.title = bankAccount.getTitle();
    this.emoji = bankAccount.getEmoji();
    this.createdAt = bankAccount.getCreatedAt();
    this.updatedAt = bankAccount.getUpdatedAt();
  }
}
