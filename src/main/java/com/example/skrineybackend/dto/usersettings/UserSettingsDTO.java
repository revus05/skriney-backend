package com.example.skrineybackend.dto.usersettings;

import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.entity.UserSettings;
import com.example.skrineybackend.enums.Currency;
import com.example.skrineybackend.enums.UserTheme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSettingsDTO {

    @Schema(description = "User's ui theme")
    private UserTheme userTheme;

    @Schema(description = "User's default currency")
    private Currency defaultCurrency;

    @Schema(description = "User's default category")
    private CategoryDTO defaultCategory;

    public UserSettingsDTO(UserSettings userSettings) {
        this.defaultCurrency = userSettings.getDefaultCurrency();
        this.userTheme = userSettings.getUserTheme();
        if (userSettings.getDefaultCategory() != null) {
            this.defaultCategory = new CategoryDTO(userSettings.getDefaultCategory());
        }
    }
}
