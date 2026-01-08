package com.example.skrineybackend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInUserRequestDTO {
  @Schema(description = "Email address", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Email обязателен")
  @Email(message = "Email должен быть валидным")
  private String email;

  @Schema(description = "User password", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Пароль обязателен")
  @Size(min = 8, max = 128, message = "Пароль должен содержать от 8 до 128 символов")
  private String password;

  public SignInUserRequestDTO(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
