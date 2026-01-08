package com.example.skrineybackend.config;

import com.example.skrineybackend.dto.currencyrate.MoneyConvertResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MoneyConvertClient {

  private final RestTemplate restTemplate = new RestTemplate();

  private static final String URL = "https://cdn.moneyconvert.net/api/latest.json";

  public MoneyConvertResponseDTO getLatestRates() {
    return restTemplate.getForObject(URL, MoneyConvertResponseDTO.class);
  }
}
