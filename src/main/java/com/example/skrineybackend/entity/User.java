package com.example.skrineybackend.entity;

import com.example.skrineybackend.dto.SignUpUserRequestDTO;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Hidden
@Getter
@Setter
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    public User() {}

    public User(SignUpUserRequestDTO requestBody) {
        this.username = requestBody.getUsername();
        this.email = requestBody.getEmail();
        this.password = requestBody.getPassword();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    private String image;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();
}