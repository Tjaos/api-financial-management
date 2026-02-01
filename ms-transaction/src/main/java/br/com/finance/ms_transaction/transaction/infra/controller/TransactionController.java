package br.com.finance.ms_transaction.transaction.infra.controller;

import br.com.finance.ms_transaction.transaction.application.usecases.*;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.infra.dto.TransactionRequestDto;
import br.com.finance.ms_transaction.transaction.infra.dto.TransactionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Transactions", description = "Operações relacionadas a transações financeiras")
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

    @Operation(
            summary = "Criar uma nova transação",
            description = "Cria uma transação financeira e publica um evento no Kafka"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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

    @Operation(
            summary = "Obter uma transação por ID",
            description = "Obtém os detalhes de uma transação financeira específica pelo seu ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação obtida com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getById(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("id") UUID id) {

        UUID userId = jwtUserExtractor.extractUserId(authorization);

        Transaction transaction = getTransactionById.getTransaction(id, userId);

        return ResponseEntity.ok(toResponse(transaction));
    }

    @Operation(
            summary = "Obter todas as transações do usuário",
            description = "Obtém uma lista de todas as transações financeiras associadas ao usuário autenticado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transações listadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @GetMapping
    public List<TransactionResponseDto> listByUser(
            @RequestHeader("Authorization") String authorization) {

        UUID userId = jwtUserExtractor.extractUserId(authorization);

        return listTransactionsByUser.list(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Operation(
            summary = "Atualizar dados de uma transação",
            description = "Atualiza os detalhes de uma transação financeira existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transações atualizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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

    @Operation(
            summary = "Excluir uma transação",
            description = "Exclui uma transação financeira existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Transação excluída com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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
