package dev.prithwish.ledgerlux.transaction;

import org.springframework.data.domain.Page;

import java.util.Date;

public interface TransactionService {
    Page<TransactionResponse> listTransactions(String userId, Date startDate, Date endDate, int page, int size);

    TransactionResponse getTransaction(String userId, String transactionId);

    TransactionResponse createTransaction(String userId, TransactionRequest request);

    TransactionResponse updateTransaction(String userId, String transactionId, TransactionRequest request);

    void deleteTransaction(String userId, String transactionId);
}
