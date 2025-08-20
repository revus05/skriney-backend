package com.example.skrineybackend.dto.usersettings;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
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

    @Schema(description = "User's ui theme", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserTheme userTheme;

    @Schema(description = "User's default currency", requiredMode = Schema.RequiredMode.REQUIRED)
    private Currency defaultCurrency;

    @Schema(description = "User's default category", requiredMode = Schema.RequiredMode.REQUIRED)
    private CategoryDTO defaultCategory;

    @Schema(description = "User's default bank account", requiredMode = Schema.RequiredMode.REQUIRED)
    private BankAccountDTO defaultBankAccount;

    public UserSettingsDTO(UserSettings userSettings) {
        this.defaultCurrency = userSettings.getDefaultCurrency();
        this.userTheme = userSettings.getUserTheme();
        if (userSettings.getDefaultCategory() != null) {
            this.defaultCategory = new CategoryDTO(userSettings.getDefaultCategory());
        }
        if (userSettings.getDefaultBankAccount() != null) {
            this.defaultBankAccount = new BankAccountDTO(userSettings.getDefaultBankAccount());
        }
    }
}
