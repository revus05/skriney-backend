package com.example.skrineybackend.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyConverter {
  public static long toCents(BigDecimal amount) {
    return amount
        .multiply(BigDecimal.valueOf(100))
        .setScale(0, RoundingMode.HALF_UP)
        .longValueExact();
  }
}
