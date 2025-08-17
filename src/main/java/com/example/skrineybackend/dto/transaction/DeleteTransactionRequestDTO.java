package com.example.skrineybackend.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteTransactionRequestDTO {
    @Schema(description = "Delete transaction uuid", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Uuid транзакции обязательно")
    private String uuid;
}
