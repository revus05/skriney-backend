package com.example.skrineybackend.entity;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Hidden
@Getter
@Setter
@Table(name = "currency_rate")
public class CurrencyRate {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  @Column(nullable = false, length = 3)
  private String baseCurrency;

  @Column(nullable = false, length = 3)
  private String targetCurrency;

  @Column(nullable = false, precision = 19, scale = 8)
  private BigDecimal rate;

  @Column(nullable = false)
  private Instant rateDate;

  @Column(nullable = false)
  private String source;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt = Instant.now();
}
