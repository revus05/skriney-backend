package com.example.skrineybackend.applicationservice;

import com.example.skrineybackend.dto.balance.BalanceSummaryDTO;
import com.example.skrineybackend.dto.balance.DailyBalanceDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.enums.BalancePeriod;
import com.example.skrineybackend.exception.NoBankAccountFoundException;
import com.example.skrineybackend.exception.UnauthorizedException;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.UserRepo;
import com.example.skrineybackend.service.BalanceService;
import com.example.skrineybackend.service.TransactionService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceApplicationService {
  private final BalanceService balanceService;
  private final TransactionService transactionService;

  private final UserRepo userRepo;
  private final BankAccountRepo bankAccountRepo;

  public List<DailyBalanceDTO> getUserBalanceTimeline(
      String userUuid, BalancePeriod period, String bankAccountUuid) {
    BankAccount bankAccount = null;

    if (bankAccountUuid != null) {
      bankAccount = getBankAccountIfUserAuthorizedAndOwnsIt(bankAccountUuid, userUuid);
    }

    Instant startDateTime = balanceService.getStartDateTimeFromPeriod(period);

    List<Transaction> transactions =
        transactionService.getTransactions(userUuid, bankAccountUuid, startDateTime);

    return balanceService.getUserBalanceTimeline(
        userUuid, transactions, startDateTime, bankAccount);
  }

  public BalanceSummaryDTO getUserTotalBalanceInUsd(String userUuid) {
    return new BalanceSummaryDTO(
        balanceService.getUserTotalBalanceInUsd(userUuid),
        balanceService.getUserTotalIncome(userUuid),
        balanceService.getUserTotalExpenses(userUuid));
  }

  private BankAccount getBankAccountIfUserAuthorizedAndOwnsIt(
      String bankAccountUuid, String userUuid) {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    return bankAccountRepo
        .findByUuidAndUser_Uuid(bankAccountUuid, userUuid)
        .orElseThrow(() -> new NoBankAccountFoundException("Счет не найден"));
  }
}
