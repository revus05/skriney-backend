package com.example.skrineybackend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "daily_balance")
public class DailyBalance {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  @Column(nullable = false, precision = 19, scale = 2)
  private LocalDate date = LocalDate.now();

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal dailyChange = BigDecimal.ZERO;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal dailyIncome = BigDecimal.ZERO;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal dailyExpenses = BigDecimal.ZERO;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal totalBalance = BigDecimal.ZERO;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bank_account_id", nullable = false)
  private BankAccount bankAccount;

  public DailyBalance() {}

  public DailyBalance(BankAccount bankAccount) {
    this.bankAccount = bankAccount;
  }
}
