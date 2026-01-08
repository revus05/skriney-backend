package com.example.skrineybackend.dto.transaction;

import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateTransactionRequestDTO {
  @Schema(description = "Transaction amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private BigDecimal amount;

  @Schema(description = "Transaction category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private Currency currency;

  @Schema(
      description = "Optional user description",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String description;

  @Schema(
      description = "Connection to bank account",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String bankAccountUuid;

  @Schema(description = "transaction category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String categoryUuid;
}
