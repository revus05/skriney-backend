package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.dto.usersettings.*;
import com.example.skrineybackend.service.UserSettingsService;
import com.example.skrineybackend.swagger.usersettings.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-settings")
@Tag(name = "Настройки пользователя", description = "Управление настройками пользователя")
@RequiredArgsConstructor
public class UserSettingsController {

  private final UserSettingsService userSettingsService;

  @UpdateDefaultCurrencyOperation
  @PostMapping("/update-default-currency")
  public Response updateDefaultCurrency(
      @Valid @RequestBody UpdateDefaultCurrencyRequestDTO updateDefaultCurrencyRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserSettingsDTO updatedUserSettings =
        userSettingsService.updateDefaultCurrency(
            updateDefaultCurrencyRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response(
        "Валюта по умолчанию успешно обновлена", HttpStatus.OK, updatedUserSettings);
  }

  @UpdateThemeOperation
  @PostMapping("/update-theme")
  public Response updateTheme(@Valid @RequestBody UpdateThemeRequestDTO updateThemeRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserSettingsDTO updatedUserSettings =
        userSettingsService.updateTheme(
            updateThemeRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Тема успешно обновлена", HttpStatus.OK, updatedUserSettings);
  }

  @UpdateDefaultCategoryOperation
  @PostMapping("/update-default-category")
  public Response updateDefaultCategory(
      @Valid @RequestBody UpdateDefaultCategoryRequestDTO updateDefaultCategoryRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserSettingsDTO updatedUserSettings =
        userSettingsService.updateDefaultCategory(
            updateDefaultCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response(
        "Категория по умолчанию успешно обновлена", HttpStatus.OK, updatedUserSettings);
  }

  @UpdateDefaultBankAccountOperation
  @PostMapping("/update-default-bank-account")
  public Response updateDefaultBankAccount(
      @Valid @RequestBody UpdateDefaultBankAccountRequestDTO updateDefaultBankAccountRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserSettingsDTO updatedUserSettings =
        userSettingsService.updateDefaultBankAccount(
            updateDefaultBankAccountRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Счет по умолчанию успешно обновлен", HttpStatus.OK, updatedUserSettings);
  }

  @UpdateLanguageOperation
  @PostMapping("/update-language")
  public Response updateLanguage(
      @Valid @RequestBody UpdateLanguageRequestDTO updateLanguageRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserSettingsDTO updatedUserSettings =
        userSettingsService.updateLanguage(
            updateLanguageRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response(
        "Язык интерфейса пользователя успешно обновлен", HttpStatus.OK, updatedUserSettings);
  }

  @UpdateAnimationEnabledOperation
  @PostMapping("/update-animation-enabled")
  public Response updateLanguage(
      @Valid @RequestBody UpdateAnimationEnabledRequestDTO updateAnimationEnabledRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserSettingsDTO updatedUserSettings =
        userSettingsService.updateAnimationEnabled(
            updateAnimationEnabledRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Скорость анимаций успешно обновлена", HttpStatus.OK, updatedUserSettings);
  }
}
