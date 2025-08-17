package com.example.skrineybackend.dto.user;

import com.example.skrineybackend.dto.usersettings.UserSettingsDTO;
import com.example.skrineybackend.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class UserDTO {
    @Schema(description = "User uuid")
    private String uuid;

    @Schema(description = "User image URL")
    private String image;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Email address")
    private String email;

    @Schema(description = "User settings")
    private UserSettingsDTO userSettings;

    @Schema(description = "Creation timestamp", type = "string", format = "date-time")
    private Instant createdAt;

    @Schema(description = "Last update timestamp", type = "string", format = "date-time")
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