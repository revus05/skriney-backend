package com.example.skrineybackend.entity;

import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Hidden
@Getter
@Setter
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private BigDecimal amountInUsd;

  @Column(nullable = false)
  private Currency currency;

  @Column() private String description;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private Instant updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bank_account_id")
  private BankAccount bankAccount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public Transaction() {}

  public Transaction(
      CreateTransactionRequestDTO createTransactionRequestDTO,
      BankAccount bankAccount,
      Category category,
      User user,
      BigDecimal amountInUsd) {
    this.amount = createTransactionRequestDTO.getAmount();
    this.currency = createTransactionRequestDTO.getCurrency();
    this.description = createTransactionRequestDTO.getDescription();
    this.bankAccount = bankAccount;
    this.category = category;
    this.user = user;
    this.amountInUsd = amountInUsd;
  }
}
