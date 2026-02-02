package br.com.finance.ms_user.user.infra.controller;

import br.com.finance.ms_user.user.application.usecases.BulkCreateUsers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserUploadController {

    private final BulkCreateUsers bulkCreateUsers;

    public UserUploadController(BulkCreateUsers bulkCreateUsers) {
        this.bulkCreateUsers = bulkCreateUsers;
    }

    @Operation(
            summary = "Upload de usuários via planilha",
            description = "Faz o upload de uma planilha para criação em massa de usuários"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Upload realizado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping("/upload")
    public ResponseEntity<String> uploadUsers(@RequestParam("file") MultipartFile file) {
        try {
            int total = bulkCreateUsers.upload(file.getInputStream());
            return ResponseEntity.ok("Usuários processados: " + total);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar planilha");
        }
    }
}
