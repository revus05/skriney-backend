package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.usersettings.*;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.UserSettings;
import com.example.skrineybackend.exception.NoBankAccountFoundException;
import com.example.skrineybackend.exception.NoCategoryFoundException;
import com.example.skrineybackend.exception.NoUserSettingsFoundException;
import com.example.skrineybackend.exception.UnauthorizedException;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.UserRepo;
import com.example.skrineybackend.repository.UserSettingsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSettingsService {
    private final UserSettingsRepo userSettingsRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final BankAccountRepo bankAccountRepo;

    public UserSettingsDTO updateDefaultCurrency(UpdateDefaultCurrencyRequestDTO updateDefaultCurrencyRequestDTO, String userUuid) throws UnauthorizedException, NoUserSettingsFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));
        UserSettings userSettings = userSettingsRepo.findByUserUuid(userUuid).orElseThrow(() -> new NoUserSettingsFoundException("Нет настроек для пользователя"));

        userSettings.setDefaultCurrency(updateDefaultCurrencyRequestDTO.getCurrency());
        userSettingsRepo.save(userSettings);

        return new UserSettingsDTO(userSettings);
    }

    public UserSettingsDTO updateDefaultCategory(UpdateDefaultCategoryRequestDTO updateDefaultCategoryRequestDTO, String userUuid) throws UnauthorizedException, NoUserSettingsFoundException, NoCategoryFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));
        UserSettings userSettings = userSettingsRepo.findByUserUuid(userUuid).orElseThrow(() -> new NoUserSettingsFoundException("Нет настроек для пользователя"));

        Category category = categoryRepo.findById(updateDefaultCategoryRequestDTO.getUuid()).orElseThrow(() -> new NoCategoryFoundException("Категория не найдена или не принадлежит пользователю"));

        userSettings.setDefaultCategory(category);
        userSettingsRepo.save(userSettings);

        return new UserSettingsDTO(userSettings);
    }

    public UserSettingsDTO updateDefaultBankAccount(UpdateDefaultBankAccountRequestDTO updateDefaultBankAccountRequestDTO, String userUuid) throws UnauthorizedException, NoUserSettingsFoundException, NoCategoryFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));
        UserSettings userSettings = userSettingsRepo.findByUserUuid(userUuid).orElseThrow(() -> new NoUserSettingsFoundException("Нет настроек для пользователя"));

        BankAccount bankAccount = bankAccountRepo.findById(updateDefaultBankAccountRequestDTO.getUuid()).orElseThrow(() -> new NoBankAccountFoundException("Счет не найден или не принадлежит пользователю"));

        userSettings.setDefaultBankAccount(bankAccount);
        userSettingsRepo.save(userSettings);

        return new UserSettingsDTO(userSettings);
    }

    public UserSettingsDTO updateTheme(UpdateThemeRequestDTO updateThemeRequestDTO, String userUuid) throws UnauthorizedException, NoUserSettingsFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));
        UserSettings userSettings = userSettingsRepo.findByUserUuid(userUuid).orElseThrow(() -> new NoUserSettingsFoundException("Нет настроек для пользователя"));

        userSettings.setUserTheme(updateThemeRequestDTO.getTheme());
        userSettingsRepo.save(userSettings);

        return new UserSettingsDTO(userSettings);
    }

    public UserSettingsDTO updateLanguage(UpdateLanguageRequestDTO updateLanguageRequestDTO, String userUuid) throws UnauthorizedException, NoUserSettingsFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));
        UserSettings userSettings = userSettingsRepo.findByUserUuid(userUuid).orElseThrow(() -> new NoUserSettingsFoundException("Нет настроек для пользователя"));

        userSettings.setLanguage(updateLanguageRequestDTO.getLanguage());
        userSettingsRepo.save(userSettings);

        return new UserSettingsDTO(userSettings);
    }
}
