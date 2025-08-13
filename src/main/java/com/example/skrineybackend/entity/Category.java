package com.example.skrineybackend.entity;

import com.example.skrineybackend.dto.CreateCategoryRequestDTO;
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
@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class)
public class Category {
    public Category() {}

    public Category(CreateCategoryRequestDTO requestBody) {
        this.title = requestBody.getTitle();
        this.emoji = requestBody.getEmoji();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(nullable = false)
    private String title;

    @Column
    private String emoji;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
}
