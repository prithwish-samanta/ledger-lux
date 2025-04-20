package dev.prithwish.ledgerlux.recurring;

import java.util.List;

public record RecurringResponse(
        String id,
        String pattern,
        Integer dayOfMonth,
        String dayOfWeek,
        String timeOfDay,
        double amount,
        String category,
        List<String> tags,
        String notes
) {
}
