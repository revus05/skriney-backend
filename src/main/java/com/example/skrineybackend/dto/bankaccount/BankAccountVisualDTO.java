package com.example.skrineybackend.dto.bankaccount;

import com.example.skrineybackend.entity.BankAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankAccountVisualDTO {
  @Schema(description = "Bank Account uuid", requiredMode = Schema.RequiredMode.REQUIRED)
  private String uuid;

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

  public BankAccountVisualDTO(BankAccount bankAccount) {
    this.uuid = bankAccount.getUuid();
    this.title = bankAccount.getTitle();
    this.emoji = bankAccount.getEmoji();
    this.createdAt = bankAccount.getCreatedAt();
    this.updatedAt = bankAccount.getUpdatedAt();
  }
}
