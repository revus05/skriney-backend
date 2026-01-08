package com.example.skrineybackend.dto.user;

import com.example.skrineybackend.dto.usersettings.UserSettingsDTO;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.enums.UserColor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
  @Schema(description = "User uuid", requiredMode = Schema.RequiredMode.REQUIRED)
  private String uuid;

  @Schema(description = "User image URL", requiredMode = Schema.RequiredMode.REQUIRED)
  @Nullable
  private String image;

  @Schema(description = "User color if no image", requiredMode = Schema.RequiredMode.REQUIRED)
  private UserColor color;

  @Schema(description = "Username", requiredMode = Schema.RequiredMode.REQUIRED)
  private String username;

  @Schema(description = "Email address", requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;

  @Schema(description = "User settings", requiredMode = Schema.RequiredMode.REQUIRED)
  private UserSettingsDTO userSettings;

  @Schema(description = "User's telegram id", requiredMode = Schema.RequiredMode.REQUIRED)
  @Nullable
  private Long telegramId;

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

  public UserDTO(User user) {
    this.uuid = user.getUuid();
    this.image = user.getImage();
    this.color = user.getColor();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.userSettings = new UserSettingsDTO(user.getSettings());
    this.telegramId = user.getTelegramId();
    this.createdAt = user.getCreatedAt();
    this.updatedAt = user.getUpdatedAt();
  }
}
