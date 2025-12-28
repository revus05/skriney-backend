package com.example.skrineybackend.repository;

import com.example.skrineybackend.dto.category.CategoryStatDTO;
import com.example.skrineybackend.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepo extends CrudRepository<Transaction, String> {
    List<Transaction> findByUser_UuidOrderByCreatedAtDesc(String userUuid);
    Optional<Transaction> findByUuidAndUser_Uuid(String uuid, String userUuid);
    @Query("""
        SELECT new com.example.skrineybackend.dto.category.CategoryStatDTO(
            tx.category.uuid,
            SUM(tx.amount)
        )
        FROM Transaction tx
        WHERE tx.bankAccount.user.uuid = :userUuid
        GROUP BY tx.category.uuid
    """)
    List<CategoryStatDTO> getUserCategoryStats(String userUuid);
}
