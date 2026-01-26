package br.com.finance.ms_transaction.transaction.infra.controller;

import br.com.finance.ms_transaction.transaction.application.usecases.*;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.infra.dto.TransactionRequestDto;
import br.com.finance.ms_transaction.transaction.infra.dto.TransactionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final CreateTransaction createTransaction;
    private final GetTransactionById getTransactionById;
    private final ListTransactionByUser listTransactionsByUser;
    private final UpdateTransaction updateTransaction;
    private final DeleteTransaction deleteTransaction;
    private final JwtUserExtractor jwtUserExtractor;

    public TransactionController(CreateTransaction createTransaction,
                                 GetTransactionById getTransactionById,
                                 ListTransactionByUser listTransactionsByUser,
                                 UpdateTransaction updateTransaction,
                                 DeleteTransaction deleteTransaction,
                                 JwtUserExtractor jwtUserExtractor) {
        this.createTransaction = createTransaction;
        this.getTransactionById = getTransactionById;
        this.listTransactionsByUser = listTransactionsByUser;
        this.updateTransaction = updateTransaction;
        this.deleteTransaction = deleteTransaction;
        this.jwtUserExtractor = jwtUserExtractor;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> create(
            @RequestHeader("Authorization") String authorization,
            @RequestBody TransactionRequestDto dto) {

        UUID userId = jwtUserExtractor.extractUserId(authorization);

        Transaction created = createTransaction.create(
                userId,
                dto.type(),
                dto.amount(),
                dto.currency(),
                dto.category(),
                dto.description()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getById(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("id") UUID id) {

        UUID userId = jwtUserExtractor.extractUserId(authorization);

        Transaction transaction = getTransactionById.getTransaction(id, userId);

        return ResponseEntity.ok(toResponse(transaction));
    }

    @GetMapping
    public List<TransactionResponseDto> listByUser(
            @RequestHeader("Authorization") String authorization) {

        UUID userId = jwtUserExtractor.extractUserId(authorization);

        return listTransactionsByUser.list(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> update(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("id") UUID id,
            @RequestBody TransactionRequestDto dto) {

        UUID userId = jwtUserExtractor.extractUserId(authorization);

        Transaction updated = updateTransaction.update(
                id,
                userId,
                dto.type(),
                dto.amount(),
                dto.currency(),
                dto.category(),
                dto.description()
        );

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("id") UUID id) {

        UUID userId = jwtUserExtractor.extractUserId(authorization);

        deleteTransaction.delete(userId, id);

        return ResponseEntity.noContent().build();
    }

    private TransactionResponseDto toResponse(Transaction t) {
        return new TransactionResponseDto(
                t.getId(),
                t.getUserId(),
                t.getType(),
                t.getAmount(),
                t.getCurrency(),
                t.getCategory(),
                t.getStatus(),
                t.getDescription(),
                t.getCreatedAt()
        );
    }
}
