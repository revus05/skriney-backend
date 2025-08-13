package com.example.skrineybackend.entity;

import com.example.skrineybackend.dto.CreateTransactionRequestDTO;
import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Hidden
@Getter
@Setter
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    public Transaction() {}

    public Transaction(CreateTransactionRequestDTO createTransactionRequestDTO, BankAccount bankAccount, Category category) {
        this.sum = createTransactionRequestDTO.getSum();
        this.currency = Currency.BYN;
        this.description = createTransactionRequestDTO.getDescription();
        this.bankAccount = bankAccount;
        this.category = category;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(nullable = false)
    private double sum;

    @Column(nullable = false)
    private Currency currency;

    @Column()
    private String description;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
