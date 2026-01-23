package br.com.finance.ms_transaction.transaction.infra.config;

import br.com.finance.ms_transaction.transaction.infra.controller.JwtUserExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtUserExtractor jwtUserExtractor() {
        return new JwtUserExtractor(
                "chave-secreta-exemplo-para-geracao-de-tokens-1234567890"
        );
    }
}
