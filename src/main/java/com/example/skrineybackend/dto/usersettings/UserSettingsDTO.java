package com.example.skrineybackend.dto.usersettings;

import com.example.skrineybackend.dto.bankaccount.BankAccountVisualDTO;
import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.entity.UserSettings;
import com.example.skrineybackend.enums.Currency;
import com.example.skrineybackend.enums.Language;
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

  @Schema(description = "Interface animation enabled", requiredMode = Schema.RequiredMode.REQUIRED)
  private boolean animationEnabled;

  @Schema(
      description = "User's default category",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED,
      nullable = true)
  private CategoryDTO defaultCategory;

  @Schema(
      description = "User's default bank account",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED,
      nullable = true)
  private BankAccountVisualDTO defaultBankAccount;

  @Schema(description = "User's language", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private Language language;

  public UserSettingsDTO(UserSettings userSettings) {
    this.defaultCurrency = userSettings.getDefaultCurrency();
    this.userTheme = userSettings.getUserTheme();
    this.animationEnabled = userSettings.isAnimationEnabled();
    if (userSettings.getDefaultCategory() != null) {
      this.defaultCategory = new CategoryDTO(userSettings.getDefaultCategory());
    }
    if (userSettings.getDefaultBankAccount() != null) {
      this.defaultBankAccount = new BankAccountVisualDTO(userSettings.getDefaultBankAccount());
    }
    this.language = userSettings.getLanguage();
  }
}
