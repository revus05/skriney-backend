package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.balance.DailyBalanceDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.CurrencyRate;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.enums.BalancePeriod;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.CurrencyRateRepo;
import com.example.skrineybackend.repository.TransactionRepo;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
  private final BankAccountRepo bankAccountRepo;
  private final TransactionRepo transactionRepo;
  private final CurrencyRateRepo currencyRateRepo;

  public List<DailyBalanceDTO> getUserBalanceTimeline(
      String userUuid,
      List<Transaction> transactions,
      Instant startDateTime,
      @Nullable BankAccount bankAccount) {

    BigDecimal currentBalance;
    if (bankAccount != null) {
      currentBalance = bankAccount.getBalanceInUsd();
    } else {
      currentBalance = getUserTotalBalanceInUsd(userUuid);
    }

    return generateDaySummaryMap(startDateTime, transactions, currentBalance);
  }

  private List<DailyBalanceDTO> generateDaySummaryMap(
      Instant startDateTime, List<Transaction> transactions, BigDecimal currentBalance) {
    LocalDate startDate;
    if (startDateTime == null) {
      startDate =
          transactions
              .get(transactions.size() - 1)
              .getCreatedAt()
              .atZone(ZoneId.systemDefault())
              .toLocalDate();
    } else {
      startDate = startDateTime.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    LocalDate toDate = LocalDate.now();

    TreeMap<LocalDate, BigDecimal[]> daySummaryMap = new TreeMap<>(Comparator.reverseOrder());

    for (LocalDate date = toDate; !date.isBefore(startDate); date = date.minusDays(1)) {
      daySummaryMap.put(date, new BigDecimal[] {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO});
    }

    for (Transaction tx : transactions) {
      LocalDate date = tx.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
      BigDecimal[] sums =
          daySummaryMap.computeIfAbsent(
              date, d -> new BigDecimal[] {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO});

      BigDecimal rate =
          currencyRateRepo
              .findTopByBaseCurrencyAndTargetCurrencyOrderByRateDateDesc(
                  "USD", tx.getCurrency().name())
              .orElse(new CurrencyRate())
              .getRate();

      int CURRENCY_SCALE =
          java.util.Currency.getInstance(tx.getCurrency().name()).getDefaultFractionDigits();

      BigDecimal amountInUsd = tx.getAmount().divide(rate, CURRENCY_SCALE, RoundingMode.HALF_EVEN);
      if (amountInUsd.signum() > 0) {
        sums[0] = sums[0].add(amountInUsd);
      } else if (amountInUsd.signum() < 0) {
        sums[1] = sums[1].add(amountInUsd.abs());
      }
      sums[2] = sums[2].add(amountInUsd);
    }

    List<DailyBalanceDTO> result = new ArrayList<>();
    BigDecimal runningBalance = currentBalance;

    for (Map.Entry<LocalDate, BigDecimal[]> entry : daySummaryMap.entrySet()) {
      LocalDate date = entry.getKey();
      BigDecimal[] sums = entry.getValue();

      result.add(new DailyBalanceDTO(date, sums[0], sums[1], sums[2], runningBalance));

      runningBalance = runningBalance.subtract(sums[2]);
    }

    Collections.reverse(result);

    if (result.isEmpty()) {
      result.add(
          new DailyBalanceDTO(
              LocalDate.now(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentBalance));
    }

    return result;
  }

  public Instant getStartDateTimeFromPeriod(BalancePeriod period) {
    Instant now = Instant.now();

    return switch (period) {
      case LAST_7_DAYS -> now.minus(7, ChronoUnit.DAYS);
      case LAST_30_DAYS -> now.minus(30, ChronoUnit.DAYS);
      case LAST_3_MONTHS -> now.minus(90, ChronoUnit.DAYS);
      case LAST_1_YEAR -> now.minus(365, ChronoUnit.DAYS);
      case ALL_TIME -> null;
    };
  }

  public BigDecimal getUserTotalBalanceInUsd(String userUuid) {
    List<BankAccount> bankAccounts =
        bankAccountRepo.findAllByUser_UuidOrderByCreatedAtAsc(userUuid);

    BigDecimal balanceInUsd = BigDecimal.ZERO;

    for (BankAccount bankAccount : bankAccounts) {
      balanceInUsd = balanceInUsd.add(bankAccount.getBalanceInUsd());
    }

    return balanceInUsd;
  }

  public BigDecimal getUserTotalIncome(String userUuid) {
    List<Transaction> incomeTransactions = transactionRepo.findAllUserIncome(userUuid);

    BigDecimal TotalIncomeInUsd = BigDecimal.ZERO;

    for (Transaction tx : incomeTransactions) {
      BigDecimal rate =
          currencyRateRepo
              .findTopByBaseCurrencyAndTargetCurrencyOrderByRateDateDesc(
                  "USD", tx.getCurrency().name())
              .orElse(new CurrencyRate())
              .getRate();

      int CURRENCY_SCALE =
          java.util.Currency.getInstance(tx.getCurrency().name()).getDefaultFractionDigits();

      BigDecimal amountInUsd = tx.getAmount().divide(rate, CURRENCY_SCALE, RoundingMode.HALF_EVEN);

      TotalIncomeInUsd = TotalIncomeInUsd.add(amountInUsd);
    }

    return TotalIncomeInUsd;
  }

  public BigDecimal getUserTotalExpenses(String userUuid) {
    List<Transaction> incomeTransactions = transactionRepo.findAllUserExpenses(userUuid);

    BigDecimal TotalExpensesInUsd = BigDecimal.ZERO;

    for (Transaction tx : incomeTransactions) {
      BigDecimal rate =
          currencyRateRepo
              .findTopByBaseCurrencyAndTargetCurrencyOrderByRateDateDesc(
                  "USD", tx.getCurrency().name())
              .orElse(new CurrencyRate())
              .getRate();

      int CURRENCY_SCALE =
          java.util.Currency.getInstance(tx.getCurrency().name()).getDefaultFractionDigits();

      BigDecimal amountInUsd = tx.getAmount().divide(rate, CURRENCY_SCALE, RoundingMode.HALF_EVEN);

      TotalExpensesInUsd = TotalExpensesInUsd.add(amountInUsd);
    }

    return TotalExpensesInUsd;
  }
}
