package dev.prithwish.ledgerlux.transaction;

import dev.prithwish.ledgerlux.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final String DEFAULT_TRANSACTION_NOT_FOUND_MSG = "Transaction not found for id: ";

    private final TransactionRepository txnRepo;
    private final TransactionMapper mapper;

    public TransactionServiceImpl(TransactionRepository txnRepo, TransactionMapper mapper) {
        this.txnRepo = txnRepo;
        this.mapper = mapper;
    }

    @Override
    public Page<TransactionResponse> listTransactions(String userId, Date startDate, Date endDate, int page, int size) {
        // Sort by transaction date descending
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<Transaction> transactionsPage;
        if (startDate != null && endDate != null) {
            transactionsPage = txnRepo.findByUserIdAndDateBetween(userId, startDate, endDate, pageable);
        } else {
            transactionsPage = txnRepo.findByUserId(userId, pageable);
        }
        return transactionsPage.map(mapper::toResponse);
    }

    @Override
    public TransactionResponse getTransaction(String userId, String transactionId) {
        Transaction transaction = txnRepo.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(DEFAULT_TRANSACTION_NOT_FOUND_MSG + transactionId));
        return mapper.toResponse(transaction);
    }

    @Override
    public TransactionResponse createTransaction(String userId, TransactionRequest request) {
        Transaction txn = mapper.toEntity(request);
        txn.setUserId(userId);
        txn.setCreatedAt(new Date());
        Transaction saved = txnRepo.save(txn);
        return mapper.toResponse(saved);
    }

    @Override
    public TransactionResponse updateTransaction(String userId, String transactionId, TransactionRequest request) {
        Transaction txn = txnRepo.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(DEFAULT_TRANSACTION_NOT_FOUND_MSG + transactionId));
        txn.setDate(request.date());
        txn.setAmount(request.amount());
        txn.setCategory(request.category());
        txn.setTags(request.tags());
        txn.setNotes(request.notes());
        txn.setUpdatedAt(new Date());
        Transaction updated = txnRepo.save(txn);
        return mapper.toResponse(updated);
    }

    @Override
    public void deleteTransaction(String userId, String transactionId) {
        txnRepo.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(DEFAULT_TRANSACTION_NOT_FOUND_MSG + transactionId));
        txnRepo.deleteById(transactionId);
    }
}
