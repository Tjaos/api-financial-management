package br.com.finance.ms_transaction.transaction.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI transactionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Transaction API")
                        .description("Microserviço responsável pelo registro de transações financeiras")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Thiago Amaral")
                        )
                );
    }
}