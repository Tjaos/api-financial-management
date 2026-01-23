package br.com.finance.ms_transaction.transaction.infra.gateway;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.infra.mapper.TransactionMapper;
import br.com.finance.ms_transaction.transaction.infra.persistence.TransactionEntity;
import br.com.finance.ms_transaction.transaction.infra.persistence.TransactionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    public TransactionRepositoryImpl(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = TransactionMapper.toEntity(transaction);
        TransactionEntity saved = jpaRepository.save(entity);
        return TransactionMapper.toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(TransactionMapper::toDomain);
    }

    @Override
    public List<Transaction> findByUserId(UUID userId) {
        return jpaRepository.findAllByUserId(userId)
                .stream()
                .map(TransactionMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
