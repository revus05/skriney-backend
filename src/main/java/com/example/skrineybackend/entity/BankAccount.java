package com.example.skrineybackend.entity;

import com.example.skrineybackend.dto.CreateBankAccountRequestDTO;
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
@Table(name = "bank_accounts")
@EntityListeners(AuditingEntityListener.class)
public class BankAccount {
    public BankAccount() {}

    public BankAccount(CreateBankAccountRequestDTO createBankAccountRequestDTO) {
        this.balance = createBankAccountRequestDTO.getBalance();
        this.currency = createBankAccountRequestDTO.getCurrency();
        this.title = createBankAccountRequestDTO.getTitle();
        this.isInTotalBalance = createBankAccountRequestDTO.isInTotalBalance();
        this.changePercent = 0;
        this.description = createBankAccountRequestDTO.getDescription();
        this.image = createBankAccountRequestDTO.getImage();

        if (createBankAccountRequestDTO.getColor() == null) {
            this.color = "blue";
        }
        if (createBankAccountRequestDTO.getCurrency() == null) {
            this.currency = Currency.BYN;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private boolean isInTotalBalance;

    @Column(nullable = false)
    private double changePercent;

    private String description;

    private String image;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
