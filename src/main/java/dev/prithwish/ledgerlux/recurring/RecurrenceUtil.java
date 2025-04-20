package dev.prithwish.ledgerlux.recurring;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class RecurrenceUtil {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final ZoneId ZONE = ZoneId.systemDefault();

    private RecurrenceUtil() {
    }

    public static Date computeNextRun(RecurringRequest req) {
        LocalTime time = LocalTime.parse(req.timeOfDay(), TIME_FORMATTER);
        LocalDate today = LocalDate.now(ZONE);
        LocalDateTime next;
        switch (req.pattern()) {
            case "DAILY":
                next = LocalDateTime.of(today.plusDays(1), time);
                break;
            case "WEEKLY":
                DayOfWeek dow = DayOfWeek.valueOf(req.dayOfWeek());
                LocalDate nextDate = today.with(TemporalAdjusters.nextOrSame(dow));
                next = LocalDateTime.of(nextDate, time);
                if (next.isBefore(LocalDateTime.now(ZONE))) {
                    next = next.plusWeeks(1);
                }
                break;
            case "MONTHLY":
                int dom = req.dayOfMonth();
                LocalDate monthDate = today.withDayOfMonth(Math.min(dom, today.lengthOfMonth()));
                next = LocalDateTime.of(monthDate, time);
                if (next.isBefore(LocalDateTime.now(ZONE))) {
                    LocalDate nextMonth = today.plusMonths(1);
                    int day = Math.min(dom, nextMonth.lengthOfMonth());
                    next = LocalDateTime.of(LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), day), time);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown pattern: " + req.pattern());
        }
        return Date.from(next.atZone(ZONE).toInstant());
    }

    public static Date computeNextRun(RecurringTransaction rt) {
        RecurringRequest req = new RecurringRequest(
                rt.getPattern(),
                rt.getDayOfMonth(),
                rt.getDayOfWeek(),
                rt.getTimeOfDay(), 0d, null, null, null
        );
        return computeNextRun(req);
    }
}
