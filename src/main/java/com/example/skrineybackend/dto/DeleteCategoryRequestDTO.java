package com.example.skrineybackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteCategoryRequestDTO {
    @Schema(description = "Delete category uuid", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "UUid категории обязательно")
    private String uuid;
}
