package com.example.skrineybackend.entity;

import com.example.skrineybackend.dto.CreateAccountRequestDTO;
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
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
public class Account {
    public Account() {}

    public Account(CreateAccountRequestDTO requestBody) {
        this.balance = requestBody.getBalance();
        this.currency = requestBody.getCurrency();
        this.name = requestBody.getName();
        this.isInTotalBalance = requestBody.isInTotalBalance();
        this.changePercent = 0;
        this.description = requestBody.getDescription();
        this.image = requestBody.getImage();

        if (requestBody.getColour() == null) {
            this.colour = "blue";
        }
        if (requestBody.getCurrency() == null) {
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
    private String name;

    @Column(nullable = false)
    private String colour;

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
