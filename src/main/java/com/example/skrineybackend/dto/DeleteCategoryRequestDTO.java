package com.example.skrineybackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteCategoryRequestDTO {
    @NotBlank(message = "UUid категории обязательно")
    private String uuid;
}
