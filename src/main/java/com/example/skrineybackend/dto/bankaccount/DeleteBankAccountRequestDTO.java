package com.example.skrineybackend.dto.bankaccount;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteBankAccountRequestDTO {
    @Schema(description = "Delete bank account uuid", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Uuid счета обязательно")
    private String uuid;
}
