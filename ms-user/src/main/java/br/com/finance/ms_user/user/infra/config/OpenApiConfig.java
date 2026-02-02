package br.com.finance.ms_user.user.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class OpenApiConfig {

    @Bean
    public OpenAPI userOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Users API")
                        .description("Microserviço responsável pelo registro de usuários e retorno do JWT")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Thiago Amaral")
                        )
                );
    }
}
