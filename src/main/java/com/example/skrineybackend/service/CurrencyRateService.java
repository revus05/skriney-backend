package com.example.skrineybackend.service;

import com.example.skrineybackend.config.MoneyConvertClient;
import com.example.skrineybackend.dto.currencyrate.MoneyConvertResponseDTO;
import com.example.skrineybackend.entity.CurrencyRate;
import com.example.skrineybackend.repository.CurrencyRateRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Service

public class CurrencyRateService {

    private static final String SOURCE = "MONEY_CONVERT";

    private final CurrencyRateRepo repository;
    private final MoneyConvertClient client;

    public CurrencyRateService(
            CurrencyRateRepo repository,
            MoneyConvertClient client) {
        this.repository = repository;
        this.client = client;
    }

    @Transactional
    public void updateRates(Set<String> targetCurrencies) {
        MoneyConvertResponseDTO response = client.getLatestRates();
        Instant today = Instant.now();

        for (String target : targetCurrencies) {

            BigDecimal rate = response.getRates().get(target);
            if (rate == null) {
                continue;
            }

            boolean exists = repository
                    .existsByBaseCurrencyAndTargetCurrencyAndRateDate(
                            response.getBase(), target, today);

            if (exists) {
                continue;
            }

            CurrencyRate entity = new CurrencyRate();
            entity.setBaseCurrency(response.getBase());
            entity.setTargetCurrency(target);
            entity.setRate(rate);
            entity.setRateDate(today);
            entity.setSource(SOURCE);

            repository.save(entity);
        }
    }
}

