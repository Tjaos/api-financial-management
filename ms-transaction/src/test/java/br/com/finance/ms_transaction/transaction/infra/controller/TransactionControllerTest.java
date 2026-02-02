package br.com.finance.ms_transaction.transaction.infra.controller;

import br.com.finance.ms_transaction.transaction.application.usecases.*;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateTransaction createTransaction;

    @MockitoBean
    private GetTransactionById getTransactionById;

    @MockitoBean
    private ListTransactionByUser listTransactionsByUser;

    @MockitoBean
    private UpdateTransaction updateTransaction;

    @MockitoBean
    private DeleteTransaction deleteTransaction;

    @MockitoBean
    private JwtUserExtractor jwtUserExtractor;

    private Transaction mockTransaction(UUID userId) {
        return new Transaction(
                UUID.randomUUID(),
                userId,
                TransactionType.PURCHASE,
                BigDecimal.valueOf(100),
                "BRL",
                "Food",
                "Lunch",
                TransactionStatus.PENDING,
                Instant.now()
        );
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        UUID userId = UUID.randomUUID();
        Transaction transaction = mockTransaction(userId);

        when(jwtUserExtractor.extractUserId(any()))
                .thenReturn(userId);

        when(createTransaction.create(
                eq(userId),
                any(),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(transaction);

        String body = """
                {
                  "type": "PURCHASE",
                  "amount": 100,
                  "currency": "BRL",
                  "category": "Food",
                  "description": "Lunch"
                }
                """;

        mockMvc.perform(post("/transactions")
                        .header("Authorization", "Bearer token")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.amount").value(100));

        verify(createTransaction).create(any(), any(), any(), any(), any(), any());
    }

    @Test
    void shouldGetTransactionById() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = mockTransaction(userId);

        when(jwtUserExtractor.extractUserId(any()))
                .thenReturn(userId);

        when(getTransactionById.getTransaction(transactionId, userId))
                .thenReturn(transaction);

        mockMvc.perform(get("/transactions/{id}", transactionId)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()));

        verify(getTransactionById).getTransaction(transactionId, userId);
    }

    @Test
    void shouldListTransactionsByUser() throws Exception {
        UUID userId = UUID.randomUUID();

        when(jwtUserExtractor.extractUserId(any()))
                .thenReturn(userId);

        when(listTransactionsByUser.list(userId))
                .thenReturn(List.of(
                        mockTransaction(userId),
                        mockTransaction(userId)
                ));

        mockMvc.perform(get("/transactions")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(listTransactionsByUser).list(userId);
    }

    @Test
    void shouldUpdateTransaction() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = mockTransaction(userId);

        when(jwtUserExtractor.extractUserId(any()))
                .thenReturn(userId);

        when(updateTransaction.update(
                eq(transactionId),
                eq(userId),
                any(),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(transaction);

        String body = """
                {
                  "type": "DEPOSIT",
                  "amount": 200,
                  "currency": "BRL",
                  "category": "Salary",
                  "description": "Monthly"
                }
                """;

        mockMvc.perform(put("/transactions/{id}", transactionId)
                        .header("Authorization", "Bearer token")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()));

        verify(updateTransaction).update(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void shouldDeleteTransaction() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        when(jwtUserExtractor.extractUserId(any()))
                .thenReturn(userId);

        doNothing().when(deleteTransaction).delete(userId, transactionId);

        mockMvc.perform(delete("/transactions/{id}", transactionId)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());

        verify(deleteTransaction).delete(userId, transactionId);
    }
}