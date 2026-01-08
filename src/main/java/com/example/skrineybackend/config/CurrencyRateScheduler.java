package com.example.skrineybackend.config;

import com.example.skrineybackend.enums.Currency;
import com.example.skrineybackend.service.CurrencyRateService;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRateScheduler {

  private final CurrencyRateService service;

  public CurrencyRateScheduler(CurrencyRateService service) {
    this.service = service;
  }

  @Scheduled(cron = "0 0 3 * * *")
  public void updateRates() {
    Set<String> allCurrencyCodes =
        Arrays.stream(Currency.values()).map(Enum::name).collect(Collectors.toSet());

    service.updateRates(allCurrencyCodes);
  }
}
