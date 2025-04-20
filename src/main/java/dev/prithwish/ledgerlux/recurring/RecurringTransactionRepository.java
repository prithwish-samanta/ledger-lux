package dev.prithwish.ledgerlux.recurring;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecurringTransactionRepository extends MongoRepository<RecurringTransaction, String> {
    List<RecurringTransaction> findByUserId(String userId);

    List<RecurringTransaction> findByNextRunBefore(Date timestamp);
}
