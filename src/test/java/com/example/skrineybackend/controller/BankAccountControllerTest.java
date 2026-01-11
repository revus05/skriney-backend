package com.example.skrineybackend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.skrineybackend.AbstractIntegrationTest;
import com.example.skrineybackend.dto.bankaccount.CreateBankAccountRequestDTO;
import com.example.skrineybackend.dto.bankaccount.UpdateBankAccountRequestDTO;
import com.example.skrineybackend.enums.Currency;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

@Transactional
@Testcontainers
class BankAccountControllerTest extends AbstractIntegrationTest {
  @Test
  void create_BankAccount_ValidRequest_ReturnsSuccessResponse() throws Exception {
    CreateBankAccountRequestDTO requestBody =
        new CreateBankAccountRequestDTO(new BigDecimal("100.12"), Currency.BYN, "account title");

    String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

    mockMvc
        .perform(
            post("/bank-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .cookie(getAuthCookie()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
        .andExpect(jsonPath("$.data.title").value("account title"))
        .andExpect(jsonPath("$.data.balance").value("100.12"))
        .andExpect(jsonPath("$.data.currency").value("BYN"));
  }

  @Test
  void create_BankAccount_NoTitle_ReturnsErrorResponse() throws Exception {
    String requestBodyJson = "{\"balance\": 100.12, \"currency\": \"BYN\"}";

    mockMvc
        .perform(
            post("/bank-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .cookie(getAuthCookie()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$.message").value("Ошибка валидации"))
        .andExpect(jsonPath("$.data.title").value("Название счета обязательно"));
  }

  @Test
  void create_BankAccount_NoCurrency_ReturnsErrorResponse() throws Exception {
    String requestBodyJson = "{\"title\": \"account title\"}";

    mockMvc
        .perform(
            post("/bank-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .cookie(getAuthCookie()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$.message").value("Ошибка валидации"))
        .andExpect(jsonPath("$.data.currency").value("Валюта обязательна"));
  }

  @Test
  void get_BankAccount_ValidRequest_ReturnsSuccessResponse() throws Exception {
    CreateBankAccountRequestDTO requestBody1 =
        new CreateBankAccountRequestDTO(new BigDecimal("100.12"), Currency.BYN, "account title 1");
    CreateBankAccountRequestDTO requestBody2 =
        new CreateBankAccountRequestDTO(new BigDecimal("200.12"), Currency.USD, "account title 2");

    String requestBodyJson1 = new ObjectMapper().writeValueAsString(requestBody1);
    String requestBodyJson2 = new ObjectMapper().writeValueAsString(requestBody2);

    mockMvc.perform(
        post("/bank-accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBodyJson1)
            .cookie(getAuthCookie()));
    mockMvc.perform(
        post("/bank-accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBodyJson2)
            .cookie(getAuthCookie()));

    mockMvc
        .perform(get("/bank-accounts").cookie(getAuthCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("Счета пользователя успешно получены"))
        .andExpect(jsonPath("$.data[0].title").value("account title 1"))
        .andExpect(jsonPath("$.data[0].balance").value("100.12"))
        .andExpect(jsonPath("$.data[1].title").value("account title 2"))
        .andExpect(jsonPath("$.data[1].balance").value("200.12"));
  }

  @Test
  void get_BankAccount_ValidRequest_ReturnsEmptyResponse() throws Exception {
    mockMvc
        .perform(get("/bank-accounts").cookie(getAuthCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("Счета пользователя успешно получены"))
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data", hasSize(0)));
  }

  @Test
  void delete_BankAccount_ValidRequest_ReturnsSuccessResponse() throws Exception {
    CreateBankAccountRequestDTO requestBody =
        new CreateBankAccountRequestDTO(new BigDecimal("100.12"), Currency.BYN, "account title");

    String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

    MvcResult createdBankAccountResult =
        mockMvc
            .perform(
                post("/bank-accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
                    .cookie(getAuthCookie()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
            .andExpect(jsonPath("$.data.title").value("account title"))
            .andExpect(jsonPath("$.data.balance").value("100.12"))
            .andExpect(jsonPath("$.data.currency").value("BYN"))
            .andReturn();

    String responseJson = createdBankAccountResult.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = mapper.readTree(responseJson);
    String accountUuid = jsonNode.path("data").path("uuid").asText();

    mockMvc
        .perform(delete("/bank-accounts/" + accountUuid).cookie(getAuthCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("Счет успешно удален"));

    mockMvc
        .perform(get("/bank-accounts/" + accountUuid).cookie(getAuthCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Счет не найден"));
  }

  @Test
  void delete_BankAccount_InvalidUuid_ReturnsErrorResponse() throws Exception {
    mockMvc
        .perform(delete("/bank-accounts/" + "randomUUID").cookie(getAuthCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("$.message").value("Счет не найден"));
  }

  @Test
  void patch_BankAccount_ValidRequest_ReturnsSuccessResponse() throws Exception {
    CreateBankAccountRequestDTO requestBody =
        new CreateBankAccountRequestDTO(new BigDecimal("100.12"), Currency.BYN, "account title");

    String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

    MvcResult createdBankAccountResult =
        mockMvc
            .perform(
                post("/bank-accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
                    .cookie(getAuthCookie()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
            .andExpect(jsonPath("$.data.title").value("account title"))
            .andExpect(jsonPath("$.data.balance").value("100.12"))
            .andReturn();

    String responseJson = createdBankAccountResult.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = mapper.readTree(responseJson);
    String accountUuid = jsonNode.path("data").path("uuid").asText();

    UpdateBankAccountRequestDTO updateBankAccountRequest =
        new UpdateBankAccountRequestDTO("updated title", "😊");

    String updateBodyJson = new ObjectMapper().writeValueAsString(updateBankAccountRequest);

    mockMvc
        .perform(
            patch("/bank-accounts/" + accountUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateBodyJson)
                .cookie(getAuthCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("Счет пользователя успешно обновлен"))
        .andExpect(jsonPath("$.data.title").value("updated title"))
        .andExpect(jsonPath("$.data.emoji").value("😊"));
  }
}
