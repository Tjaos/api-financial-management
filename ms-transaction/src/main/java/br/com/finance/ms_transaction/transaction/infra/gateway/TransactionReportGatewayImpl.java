package br.com.finance.ms_transaction.transaction.infra.gateway;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionReportGateway;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.infra.mapper.TransactionMapper;
import br.com.finance.ms_transaction.transaction.infra.persistence.TransactionJpaRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class TransactionReportGatewayImpl implements TransactionReportGateway {

    private final TransactionJpaRepository repository;
    private final TransactionMapper mapper;

    public TransactionReportGatewayImpl(TransactionJpaRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public List<Transaction> findApprovedByUserIdAndStatusAndCreatedAtBetween(
            UUID userId,
            TransactionStatus status,
            Instant start,
            Instant end
    ) {
        return repository
                .findApprovedByUserIdAndStatusAndCreatedAtBetween(
                        userId,
                        status,
                        start,
                        end
                )
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

}
