package com.example.skrineybackend.dto;

import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class TransactionDTO {
    @Schema(description = "Transaction UUID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uuid;

    @Schema(description = "Transaction sum", requiredMode = Schema.RequiredMode.REQUIRED)
    private double sum;

    @Schema(description = "Transaction category", requiredMode = Schema.RequiredMode.REQUIRED)
    private Currency currency;

    @Schema(description = "Optional user description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(description = "Creation timestamp", type = "string", format = "date-time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @Schema(description = "Last update timestamp", type = "string", format = "date-time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant updatedAt;

    @Schema(description = "Connection to bank account", requiredMode = Schema.RequiredMode.REQUIRED)
    private BankAccountDTO bankAccount;

    @Schema(description = "transaction category", requiredMode = Schema.RequiredMode.REQUIRED)
    private CategoryDTO category;

    public TransactionDTO(Transaction transaction) {
        this.uuid = transaction.getUuid();
        this.sum = transaction.getSum();
        this.currency = transaction.getCurrency();
        this.description = transaction.getDescription();
        this.bankAccount = new BankAccountDTO(transaction.getBankAccount());
        this.category = new CategoryDTO(transaction.getCategory());
        this.createdAt = transaction.getCreatedAt();
        this.updatedAt = transaction.getUpdatedAt();
    }
}
