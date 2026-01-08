package com.example.skrineybackend.entity;

import com.example.skrineybackend.dto.user.SignUpUserRequestDTO;
import com.example.skrineybackend.enums.UserColor;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Hidden
@Getter
@Setter
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  private String image;

  @Column(nullable = false)
  private UserColor color;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(unique = true)
  private Long telegramId;

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

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Transaction> transactions = new ArrayList<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserSettings settings;

  public User() {}

  public User(SignUpUserRequestDTO requestBody) {
    this.username = requestBody.getUsername();
    this.email = requestBody.getEmail();
    this.password = requestBody.getPassword();
    this.color = getRandomColor();

    UserSettings settings = new UserSettings();
    settings.setUser(this);
    this.settings = settings;
  }

  private UserColor getRandomColor() {
    Random random = new Random();

    UserColor[] colors = UserColor.values();
    return colors[random.nextInt(colors.length)];
  }
}
