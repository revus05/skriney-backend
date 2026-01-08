package com.example.skrineybackend.dto.currencyrate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoneyConvertResponseDTO {
  private String base;
  private String source;
  private Instant rateDate;
  private Map<String, BigDecimal> rates;
}
