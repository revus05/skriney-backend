package com.example.skrineybackend.entity;

import com.example.skrineybackend.dto.category.CreateCategoryRequestDTO;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
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
@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class)
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  @Column(nullable = false)
  private String title;

  @Column @Nullable private String emoji;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private Instant updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "category")
  private List<Transaction> transactions = new ArrayList<>();

  @PreRemove
  private void preRemove() {
    if (transactions != null) {
      transactions.forEach(transaction -> transaction.setCategory(null));
    }
  }

  public Category() {}

  public Category(CreateCategoryRequestDTO requestBody, User user) {
    this.title = requestBody.getTitle();
    this.emoji = requestBody.getEmoji();
    this.user = user;
  }
}
