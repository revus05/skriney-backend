package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.CurrencyRate;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRateRepo extends JpaRepository<CurrencyRate, String> {

  Optional<CurrencyRate> findTopByBaseCurrencyAndTargetCurrencyOrderByRateDateDesc(
      String baseCurrency, String targetCurrency);

  boolean existsByBaseCurrencyAndTargetCurrencyAndRateDate(
      String baseCurrency, String targetCurrency, Instant rateDate);
}
