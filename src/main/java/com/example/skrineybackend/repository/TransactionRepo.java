package com.example.skrineybackend.repository;

import com.example.skrineybackend.dto.category.CategoryStatDTO;
import com.example.skrineybackend.entity.Transaction;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepo extends CrudRepository<Transaction, String> {
  List<Transaction> findByUser_UuidOrderByCreatedAtDesc(String userUuid);

  List<Transaction>
      findByUser_UuidAndBankAccount_UuidAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
          String userUuid, String bankAccountUuid, Instant fromDateTime);

  List<Transaction> findByUser_UuidAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
      String userUuid, Instant fromDateTime);

  Optional<Transaction> findByUuidAndUser_Uuid(String uuid, String userUuid);

  @Query(
      """
    SELECT new com.example.skrineybackend.dto.category.CategoryStatDTO(
     tx.category.uuid,
     SUM(tx.amount)
    )
    FROM Transaction tx
    WHERE tx.bankAccount.user.uuid = :userUuid
    AND tx.createdAt >= :startDate
    AND (:bankAccountUuid IS NULL OR tx.bankAccount.uuid = :bankAccountUuid)
    GROUP BY tx.category.uuid
""")
  List<CategoryStatDTO> getUserCategoryStats(
      String userUuid, Instant startDate, String bankAccountUuid);
}
