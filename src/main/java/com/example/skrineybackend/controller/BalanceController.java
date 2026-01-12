package com.example.skrineybackend.controller;

import com.example.skrineybackend.applicationservice.BalanceApplicationService;
import com.example.skrineybackend.dto.balance.BalanceSummaryDTO;
import com.example.skrineybackend.dto.balance.DailyBalanceDTO;
import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.enums.BalancePeriod;
import com.example.skrineybackend.swagger.balance.GetUserBalanceSummaryOperation;
import com.example.skrineybackend.swagger.balance.GetUserBalanceTimelineOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
@Tag(name = "Баланс", description = "Управление балансом пользователя")
@RequiredArgsConstructor
public class BalanceController {
  private final BalanceApplicationService balanceApplicationService;

  @GetUserBalanceTimelineOperation
  @GetMapping()
  public Response getUserBalanceTimeline(
      @RequestParam(required = false, defaultValue = "LAST_30_DAYS") BalancePeriod period,
      @RequestParam(required = false) String bankAccountUuid) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    List<DailyBalanceDTO> dailyBalancesDTO =
        balanceApplicationService.getUserBalanceTimeline(
            ((UserDetails) auth.getPrincipal()).getUsername(), period, bankAccountUuid);
    return new Response("Ежедневные балансы успешно получены", HttpStatus.OK, dailyBalancesDTO);
  }

  @GetUserBalanceSummaryOperation
  @GetMapping("/summary")
  public Response getUserTotalBalance() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    BalanceSummaryDTO balanceSummaryDTO =
        balanceApplicationService.getUserTotalBalanceInUsd(
            ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Баланс успешно получен", HttpStatus.OK, balanceSummaryDTO);
  }
}
