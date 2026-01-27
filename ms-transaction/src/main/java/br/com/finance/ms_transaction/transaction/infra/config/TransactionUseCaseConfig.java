package br.com.finance.ms_transaction.transaction.infra.config;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.application.usecases.*;
import br.com.finance.ms_transaction.transaction.infra.gateway.TransactionProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionUseCaseConfig {

    @Bean
    public CreateTransaction createTransaction(TransactionRepository repository, TransactionProducer producer) {
        return new CreateTransaction(repository, producer);
    }

    @Bean
    public GetTransactionById getTransactionById(TransactionRepository repository) {
        return new GetTransactionById(repository);
    }

    @Bean
    public ListTransactionByUser listTransactionsByUser(TransactionRepository repository) {
        return new ListTransactionByUser(repository);
    }

    @Bean
    public UpdateTransaction updateTransaction(TransactionRepository repository) {
        return new UpdateTransaction(repository);
    }

    @Bean
    public DeleteTransaction deleteTransaction(TransactionRepository repository) {
        return new DeleteTransaction(repository);
    }
}
