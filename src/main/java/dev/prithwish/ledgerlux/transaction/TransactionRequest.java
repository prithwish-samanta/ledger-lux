package dev.prithwish.ledgerlux.transaction;

import java.util.Date;
import java.util.List;

public record TransactionRequest(
        Date date,
        double amount,
        String category,
        List<String> tags,
        String notes
) {
}
