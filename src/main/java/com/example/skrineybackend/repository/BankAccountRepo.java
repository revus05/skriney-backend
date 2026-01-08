package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.BankAccount;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepo extends CrudRepository<BankAccount, String> {
  Optional<BankAccount> findByUuidAndUser_Uuid(String uuid, String userUuid);

  List<BankAccount> findAllByUser_UuidOrderByCreatedAtAsc(String userUuid);

  @Query("SELECT COALESCE(SUM(a.balance), 0) FROM BankAccount a WHERE a.user.uuid = :userUuid")
  Optional<BigDecimal> sumBalanceByUserUuid(@Param("userUuid") String userUuid);
}
