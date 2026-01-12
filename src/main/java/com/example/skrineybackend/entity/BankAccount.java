package com.example.skrineybackend.entity;

import com.example.skrineybackend.dto.bankaccount.CreateBankAccountRequestDTO;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Hidden
@Getter
@Setter
@Table(name = "bank_accounts")
@EntityListeners(AuditingEntityListener.class)
public class BankAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal balanceInUsd;

  @Column(nullable = false)
  private String title;

  @Column private String emoji;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private Instant updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "bankAccount")
  private List<Transaction> transactions = new ArrayList<>();

  @PreRemove
  private void preRemove() {
    if (transactions != null) {
      transactions.forEach(transaction -> transaction.setBankAccount(null));
    }
  }

  public BankAccount() {}

  public BankAccount(CreateBankAccountRequestDTO dto, User user) {
    this.title = dto.getTitle();
    this.balanceInUsd = BigDecimal.ZERO;
    this.user = user;
  }
}
