package com.example.skrineybackend.dto.user;

import com.example.skrineybackend.dto.usersettings.UserSettingsDTO;
import com.example.skrineybackend.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class UserDTO {
    @Schema(description = "User uuid", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uuid;

    @Schema(description = "User image URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @Nullable
    private String image;

    @Schema(description = "Username", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "Email address", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "User settings", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserSettingsDTO userSettings;

    @Schema(description = "Creation timestamp", type = "string", format = "date-time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @Schema(description = "Last update timestamp", type = "string", format = "date-time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant updatedAt;

    public UserDTO(User user) {
        this.uuid = user.getUuid();
        this.image = user.getImage();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.userSettings = new UserSettingsDTO(user.getSettings());
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}