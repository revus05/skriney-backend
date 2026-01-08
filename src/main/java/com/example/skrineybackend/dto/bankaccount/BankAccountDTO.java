package com.example.skrineybackend.dto.bankaccount;

import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankAccountDTO {
  @Schema(description = "Bank Account uuid", requiredMode = Schema.RequiredMode.REQUIRED)
  private String uuid;

  @Schema(description = "Money on the bank account", requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal balance;

  @Schema(description = "Bank Account currency", requiredMode = Schema.RequiredMode.REQUIRED)
  private Currency currency;

  @Schema(description = "Bank Account title", requiredMode = Schema.RequiredMode.REQUIRED)
  private String title;

  @Schema(description = "Bank Account emoji", requiredMode = Schema.RequiredMode.REQUIRED)
  private String emoji;

  @Schema(
      description = "Are bank account money used to calculate total user balance",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private boolean isInTotalBalance;

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

  public BankAccountDTO(BankAccount bankAccount) {
    this.uuid = bankAccount.getUuid();
    this.balance = bankAccount.getBalance();
    this.currency = bankAccount.getCurrency();
    this.title = bankAccount.getTitle();
    this.emoji = bankAccount.getEmoji();
    this.isInTotalBalance = bankAccount.isInTotalBalance();
    this.createdAt = bankAccount.getCreatedAt();
    this.updatedAt = bankAccount.getUpdatedAt();
  }
}
