package br.com.finance.ms_transaction.transaction.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class QuoteDto {

        @JsonProperty("cotacao_compra")
        private BigDecimal purchaseRate;

        @JsonProperty("tipo_boletim")
        private String reportType;

        public BigDecimal getPurchaseRate() { return purchaseRate; }
        public String getReportType() { return reportType; }

}
