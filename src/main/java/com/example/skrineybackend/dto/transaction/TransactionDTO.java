package com.example.skrineybackend.dto.transaction;

import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionDTO {
  @Schema(description = "Transaction uuid", requiredMode = Schema.RequiredMode.REQUIRED)
  private String uuid;

  @Schema(description = "Transaction amount", requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal amount;

  @Schema(description = "Transaction amount in USD", requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal amountInUsd;

  @Schema(description = "Transaction category", requiredMode = Schema.RequiredMode.REQUIRED)
  private Currency currency;

  @Schema(description = "Optional user description", requiredMode = Schema.RequiredMode.REQUIRED)
  @Nullable
  private String description;

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

  @Schema(
      description = "Connection to bank account",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String bankAccountUuid;

  @Schema(description = "transaction category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private CategoryDTO category;

  public TransactionDTO(Transaction transaction) {
    this.uuid = transaction.getUuid();
    this.amount = transaction.getAmount();
    this.amountInUsd = transaction.getAmountInUsd();
    this.currency = transaction.getCurrency();
    this.description = transaction.getDescription();
    if (transaction.getBankAccount() != null) {
      this.bankAccountUuid = transaction.getBankAccount().getUuid();
    }
    if (transaction.getCategory() != null) {
      this.category = new CategoryDTO(transaction.getCategory());
    }
    this.createdAt = transaction.getCreatedAt();
    this.updatedAt = transaction.getUpdatedAt();
  }
}
