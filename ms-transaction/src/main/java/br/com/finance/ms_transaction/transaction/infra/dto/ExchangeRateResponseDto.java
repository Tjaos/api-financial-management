package br.com.finance.ms_transaction.transaction.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExchangeRateResponseDto{

        @JsonProperty("cotacoes")
        private List<QuoteDto> quotes;

        public List<QuoteDto> getQuotes() {
            return quotes;
        }
}


