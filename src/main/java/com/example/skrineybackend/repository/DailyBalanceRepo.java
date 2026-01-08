package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.DailyBalance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface DailyBalanceRepo extends CrudRepository<DailyBalance, String> {
  Optional<DailyBalance> findByBankAccount_UuidAndDate(String bankAccountUuid, LocalDate date);

  Optional<DailyBalance> findTopByBankAccount_UuidAndDateBeforeOrderByDateDesc(
      String bankAccountUuid, LocalDate date);

  List<DailyBalance> findAllByBankAccount_UuidOrderByDateAsc(String bankAccountUuid);

  List<DailyBalance> findAllByBankAccount_User_Uuid(String userUuid);

  List<DailyBalance> findAllByBankAccount_UuidAndDateBetweenOrderByDateAsc(
      String bankAccountUuid, LocalDate startDate, LocalDate endDate);
}
