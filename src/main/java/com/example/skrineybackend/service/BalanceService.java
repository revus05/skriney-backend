package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.balance.BalanceSummaryDTO;
import com.example.skrineybackend.dto.balance.DailyBalanceDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.DailyBalance;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.DailyBalanceRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final DailyBalanceRepo dailyBalanceRepo;
    private final BankAccountRepo bankAccountRepo;

    @Transactional
    public void updateBalance(BankAccount bankAccount, LocalDate date, BigDecimal amount) {
        DailyBalance dailyBalance = dailyBalanceRepo.findByBankAccount_UuidAndDate(bankAccount.getUuid(), date)
            .orElseGet(() -> createDailyBalance(bankAccount, date));

        dailyBalance.setDailyChange(dailyBalance.getDailyChange().add(amount));
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            dailyBalance.setDailyIncome(dailyBalance.getDailyIncome().add(amount));
        } else {
            dailyBalance.setDailyExpenses(dailyBalance.getDailyExpenses().add(amount));
        }
        dailyBalance.setTotalBalance(dailyBalance.getTotalBalance().add(amount));

        dailyBalanceRepo.save(dailyBalance);

        recalcNextDays(bankAccount.getUuid(), date);
    }

    private DailyBalance createDailyBalance(BankAccount bankAccount, LocalDate date) {
        DailyBalance db = new DailyBalance();
        db.setBankAccount(bankAccount);
        db.setDate(date);

        Optional<DailyBalance> prev = dailyBalanceRepo
                .findTopByBankAccount_UuidAndDateBeforeOrderByDateDesc(bankAccount.getUuid(), date);

        db.setTotalBalance(prev.map(DailyBalance::getTotalBalance).orElse(BigDecimal.ZERO));

        return db;
    }

    private void recalcNextDays(String bankAccountUuid, LocalDate startDate) {
        List<DailyBalance> balances = dailyBalanceRepo.findAllByBankAccount_UuidOrderByDateAsc(bankAccountUuid);

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

    public List<DailyBalanceDTO> getUserBalanceTimeline(String userUuid) {
        return dailyBalanceRepo.findAllByBankAccount_User_Uuid(userUuid)
            .stream().map(DailyBalanceDTO::new).toList();
    }

    public BalanceSummaryDTO getUserBalanceSummary(String userUuid) {
        List<BankAccount> allBankAccounts = bankAccountRepo.findAllByUser_Uuid(userUuid);

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        BigDecimal totalBalance = BigDecimal.ZERO;
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (BankAccount account : allBankAccounts) {
            List<DailyBalance> balances = dailyBalanceRepo
                .findAllByBankAccount_UuidAndDateBetweenOrderByDateAsc(
                    account.getUuid(), startDate, endDate);

            for (DailyBalance day : balances) {
                totalIncome = totalIncome.add(day.getDailyIncome());
                totalExpense = totalExpense.add(day.getDailyExpenses().abs());
            }

            totalBalance = totalBalance.add(balances.get(balances.size() - 1).getTotalBalance());
        }

        return new BalanceSummaryDTO(totalBalance, totalIncome, totalExpense);
    }
}
