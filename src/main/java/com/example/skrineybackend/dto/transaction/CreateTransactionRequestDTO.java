package com.example.skrineybackend.dto.transaction;

import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTransactionRequestDTO {
    @Schema(description = "Transaction sum", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Сумма транзакции обязательна")
    private double sum;

    @Schema(description = "Transaction category", requiredMode = Schema.RequiredMode.REQUIRED)
    private Currency currency;

    @Schema(description = "Optional user description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 128, message = "Описание не может быть длиннее 128 символов")
    private String description;

    @Schema(description = "Bank account uuid", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Uuid счета обязателен")
    private String bankAccountUuid;

    @Schema(description = "Transaction category uuid", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Uuid категории транзакции обязателен")
    private String categoryUuid;
}
