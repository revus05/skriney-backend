package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.balance.BalanceSummaryDTO;
import com.example.skrineybackend.dto.balance.DailyBalanceDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.DailyBalance;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.enums.BalancePeriod;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.DailyBalanceRepo;
import com.example.skrineybackend.repository.TransactionRepo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {
  private final DailyBalanceRepo dailyBalanceRepo;
  private final BankAccountRepo bankAccountRepo;
  private final TransactionRepo transactionRepo;

  @Transactional
  public void updateBalance(BankAccount bankAccount, LocalDate date, BigDecimal amount) {
    DailyBalance dailyBalance =
        dailyBalanceRepo
            .findByBankAccount_UuidAndDate(bankAccount.getUuid(), date)
            .orElseGet(() -> createDailyBalance(bankAccount, date));

    dailyBalance.setDailyChange(dailyBalance.getDailyChange().add(amount));
    if (amount.compareTo(BigDecimal.ZERO) > 0) {
      dailyBalance.setDailyIncome(dailyBalance.getDailyIncome().add(amount));
    } else {
      dailyBalance.setDailyExpenses(dailyBalance.getDailyExpenses().add(amount));
    }
    dailyBalance.setTotalBalance(dailyBalance.getTotalBalance().add(amount));
    bankAccount.setBalance(bankAccount.getBalance().add(amount));

    dailyBalanceRepo.save(dailyBalance);

    recalcNextDays(bankAccount.getUuid(), date);
  }

  private DailyBalance createDailyBalance(BankAccount bankAccount, LocalDate date) {
    DailyBalance db = new DailyBalance(bankAccount);
    db.setDate(date);

    Optional<DailyBalance> prev =
        dailyBalanceRepo.findTopByBankAccount_UuidAndDateBeforeOrderByDateDesc(
            bankAccount.getUuid(), date);

    db.setTotalBalance(prev.map(DailyBalance::getTotalBalance).orElse(BigDecimal.ZERO));

    return db;
  }

  private void recalcNextDays(String bankAccountUuid, LocalDate startDate) {
    List<DailyBalance> balances =
        dailyBalanceRepo.findAllByBankAccount_UuidOrderByDateAsc(bankAccountUuid);

    BigDecimal runningTotal = BigDecimal.ZERO;
    for (DailyBalance db : balances) {
      if (!db.getDate().isBefore(startDate)) {
        runningTotal = runningTotal.add(db.getDailyChange());
        db.setTotalBalance(runningTotal);
        dailyBalanceRepo.save(db);
      } else {
        runningTotal = db.getTotalBalance();
      }
    }
  }

  public List<DailyBalanceDTO> getUserBalanceTimeline(
      String userUuid, BalancePeriod period, String bankAccountUuid) {
    BigDecimal currentBalance = getCurrentBalance(userUuid, bankAccountUuid);
    Instant fromDateTime = calculateFromDateTime(period);

    List<Transaction> transactions =
        fetchTransactionsDescending(userUuid, bankAccountUuid, fromDateTime);

    LocalDate fromDate = fromDateTime.atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate toDate = LocalDate.now();

    TreeMap<LocalDate, DaySummary> daySummaryMap = new TreeMap<>(Comparator.reverseOrder());

    for (LocalDate date = toDate; !date.isBefore(fromDate); date = date.minusDays(1)) {
      daySummaryMap.put(date, new DaySummary());
    }

    for (Transaction tx : transactions) {
      LocalDate date = tx.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();

      daySummaryMap
          .computeIfAbsent(date, k -> new DaySummary())
          .addTransaction(tx.getAmount(), tx.getBankAccount().getUuid());
    }

    List<DailyBalanceDTO> result = new ArrayList<>();
    BigDecimal runningBalance = currentBalance;

    for (Map.Entry<LocalDate, DaySummary> entry : daySummaryMap.entrySet()) {
      LocalDate date = entry.getKey();
      DaySummary summary = entry.getValue();

      BigDecimal totalBalanceAtEndOfDay = runningBalance;

      result.add(
          new DailyBalanceDTO(
              date,
              summary.dailyIncome,
              summary.dailyExpenses,
              summary.dailyChange,
              totalBalanceAtEndOfDay,
              bankAccountUuid));

      runningBalance = runningBalance.subtract(summary.dailyChange);
    }

    Collections.reverse(result);

    if (result.isEmpty()) {
      result.add(
          new DailyBalanceDTO(
              LocalDate.now(),
              BigDecimal.ZERO,
              BigDecimal.ZERO,
              BigDecimal.ZERO,
              currentBalance,
              bankAccountUuid));
    }

    return result;
  }

  private static class DaySummary {
    BigDecimal dailyIncome = BigDecimal.ZERO;
    BigDecimal dailyExpenses = BigDecimal.ZERO;
    BigDecimal dailyChange = BigDecimal.ZERO;
    String bankAccountUuid;

    void addTransaction(BigDecimal amount, String bankAccountUuid) {
      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        this.dailyIncome = this.dailyIncome.add(amount);
      } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
        this.dailyExpenses = this.dailyExpenses.add(amount.abs());
      }
      this.dailyChange = this.dailyChange.add(amount);

      if (this.bankAccountUuid == null) {
        this.bankAccountUuid = bankAccountUuid;
      }
    }
  }

  private Instant calculateFromDateTime(BalancePeriod period) {
    Instant now = Instant.now();

    return switch (period) {
      case LAST_7_DAYS -> now.minus(7, ChronoUnit.DAYS);
      case LAST_30_DAYS -> now.minus(30, ChronoUnit.DAYS);
      case LAST_3_MONTHS -> now.minus(90, ChronoUnit.DAYS);
      case LAST_1_YEAR -> now.minus(365, ChronoUnit.DAYS);
      case ALL_TIME -> Instant.EPOCH;
    };
  }

  private BigDecimal getCurrentBalance(String userUuid, String bankAccountUuid) {
    if (bankAccountUuid == null || bankAccountUuid.isBlank()) {
      return bankAccountRepo.sumBalanceByUserUuid(userUuid).orElse(BigDecimal.ZERO);
    } else {
      return bankAccountRepo
          .findByUuidAndUser_Uuid(bankAccountUuid, userUuid)
          .map(BankAccount::getBalance)
          .orElse(BigDecimal.ZERO);
    }
  }

  private List<Transaction> fetchTransactionsDescending(
      String userUuid, String bankAccountUuid, Instant fromDateTime) {
    if (bankAccountUuid == null || bankAccountUuid.isBlank()) {
      return transactionRepo.findByUser_UuidAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
          userUuid, fromDateTime);
    } else {
      return transactionRepo
          .findByUser_UuidAndBankAccount_UuidAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
              userUuid, bankAccountUuid, fromDateTime);
    }
  }

  public BalanceSummaryDTO getUserBalanceSummary(String userUuid) {
    List<BankAccount> allBankAccounts =
        bankAccountRepo.findAllByUser_UuidOrderByCreatedAtAsc(userUuid);

    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusDays(30);

    BigDecimal totalBalance = BigDecimal.ZERO;
    BigDecimal totalIncome = BigDecimal.ZERO;
    BigDecimal totalExpense = BigDecimal.ZERO;

    for (BankAccount account : allBankAccounts) {
      List<DailyBalance> balances =
          dailyBalanceRepo.findAllByBankAccount_UuidAndDateBetweenOrderByDateAsc(
              account.getUuid(), startDate, endDate);

      for (DailyBalance day : balances) {
        totalIncome = totalIncome.add(day.getDailyIncome());
        totalExpense = totalExpense.add(day.getDailyExpenses().abs());
      }

      int index = balances.size() - 1;

      totalBalance = totalBalance.add(balances.get(index).getTotalBalance());
    }

    return new BalanceSummaryDTO(totalBalance, totalIncome, totalExpense);
  }
}
