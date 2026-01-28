package br.com.finance.ms_transaction.transaction.infra.gateway;

import br.com.finance.ms_transaction.transaction.application.gateways.ExchangeRateGateway;
import br.com.finance.ms_transaction.transaction.infra.dto.QuoteDto;
import br.com.finance.ms_transaction.transaction.infra.dto.ExchangeRateResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ExchangeRateGatewayImpl implements ExchangeRateGateway {


    private final WebClient webClient;

    public ExchangeRateGatewayImpl(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://brasilapi.com.br/api/cambio/v1")
                .build();
    }


    @Override
    public BigDecimal getRateToBRL(String currency, LocalDate date) {

        if ("BRL".equalsIgnoreCase(currency)) {
            return BigDecimal.ONE;
        }

        ExchangeRateResponseDto response = webClient.get()
                .uri("/cotacao/{moeda}/{data}", currency, date)
                .retrieve()
                .bodyToMono(ExchangeRateResponseDto.class)
                .block();

        if(response == null || response.getQuotes() == null || response.getQuotes().isEmpty()){
            throw new IllegalStateException("Nenhuma cotação retornada pela BrailAPI");
        }

        return response.getQuotes().stream()
                .filter(c -> "FECHAMENTO PTAX".equalsIgnoreCase(c.getReportType()))
                .findFirst()
                .map(QuoteDto::getPurchaseRate)
                .orElseThrow(() ->
                        new IllegalStateException("Cotação PTAX não encontrada"));

    }

}
