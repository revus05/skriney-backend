package com.example.skrineybackend.dto;

import com.example.skrineybackend.entity.UserSettings;
import com.example.skrineybackend.enums.Currency;
import com.example.skrineybackend.enums.UserTheme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSettingsDTO {
    public UserSettingsDTO(UserSettings userSettings) {
        this.defaultCurrency = userSettings.getDefaultCurrency();
        this.userTheme = userSettings.getUserTheme();
    }

    @Schema(description = "User's ui theme", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserTheme userTheme;

    @Schema(description = "User's default currency", requiredMode = Schema.RequiredMode.REQUIRED)
    private Currency defaultCurrency;
}
