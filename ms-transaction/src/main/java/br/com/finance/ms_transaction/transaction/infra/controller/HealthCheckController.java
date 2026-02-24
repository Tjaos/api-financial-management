package br.com.finance.ms_transaction.transaction.infra.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health", description = "Rota para verificar a saúde do serviço ms-transaction")
@RestController
public class HealthCheckController {

    @Operation(
            summary = "Verificar saúde do serviço de transações",
            description = "Endpoint para verificar se o serviço de transações está funcionando corretamente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Serviço está saudável"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

}
