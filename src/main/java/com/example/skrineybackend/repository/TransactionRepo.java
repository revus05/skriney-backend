package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.Transaction;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepo extends CrudRepository<Transaction, String> {
  List<Transaction> findByUser_UuidOrderByCreatedAtDesc(String userUuid);

  List<Transaction> findByUser_UuidAndBankAccount_UuidOrderByCreatedAtDesc(
      String userUuid, String bankAccountUuid);

  List<Transaction> findByUser_UuidAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
      String userUuid, Instant startDateTime);

  List<Transaction>
      findByUser_UuidAndBankAccount_UuidAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
          String userUuid, String bankAccountUuid, Instant startDateTime);

  Optional<Transaction> findByUuidAndUser_Uuid(String uuid, String userUuid);

  @Query(
      """
      SELECT tx
      FROM Transaction tx
      WHERE tx.user.uuid = :userUuid
        AND tx.amount > 0
""")
  List<Transaction> findAllUserIncome(String userUuid);

  @Query(
      """
      SELECT tx
      FROM Transaction tx
      WHERE tx.user.uuid = :userUuid
        AND tx.amount < 0
""")
  List<Transaction> findAllUserExpenses(String userUuid);
}
