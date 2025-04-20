package dev.prithwish.ledgerlux.scheduler;

import dev.prithwish.ledgerlux.recurring.RecurringTransaction;
import dev.prithwish.ledgerlux.recurring.RecurringTransactionRepository;
import dev.prithwish.ledgerlux.transaction.Transaction;
import dev.prithwish.ledgerlux.transaction.TransactionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static dev.prithwish.ledgerlux.recurring.RecurrenceUtil.computeNextRun;

@Component
public class RecurringScheduler {
    private final RecurringTransactionRepository recurringRepo;
    private final TransactionRepository txnRepo;

    public RecurringScheduler(RecurringTransactionRepository recurringRepo, TransactionRepository txnRepo) {
        this.recurringRepo = recurringRepo;
        this.txnRepo = txnRepo;
    }

    @Scheduled(cron = "0 0 * * * *")  // runs at the top of every hour
    public void processRecurringTransactions() {
        Date now = new Date();
        List<RecurringTransaction> due = recurringRepo.findByNextRunBefore(now);
        for (RecurringTransaction rule : due) {
            // create transaction
            Transaction txn = new Transaction();
            txn.setUserId(rule.getUserId());
            txn.setDate(rule.getNextRun());
            txn.setAmount(rule.getAmount());
            txn.setCategory(rule.getCategory());
            txn.setTags(rule.getTags());
            txn.setNotes(rule.getNotes());
            txn.setCreatedAt(new Date());
            txnRepo.save(txn);
            // update rule.nextRun
            rule.setNextRun(computeNextRun(rule));
            rule.setUpdatedAt(new Date());
            recurringRepo.save(rule);
        }
    }
}
