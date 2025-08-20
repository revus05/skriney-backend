package com.example.skrineybackend.entity;

import com.example.skrineybackend.enums.Currency;
import com.example.skrineybackend.enums.UserTheme;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Hidden
@Getter
@Setter
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(nullable = false)
    private UserTheme userTheme = UserTheme.SYSTEM;

    @Column()
    private Currency defaultCurrency = Currency.USD;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_category_id")
    private Category defaultCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_bankaccount_id")
    private BankAccount defaultBankAccount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
