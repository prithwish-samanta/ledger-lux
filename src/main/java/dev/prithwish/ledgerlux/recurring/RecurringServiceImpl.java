package dev.prithwish.ledgerlux.recurring;

import dev.prithwish.ledgerlux.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Service
public class RecurringServiceImpl implements RecurringService {
    private final RecurringTransactionRepository repo;
    private final RecurringTransactionMapper mapper;

    public RecurringServiceImpl(RecurringTransactionRepository repo, RecurringTransactionMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public List<RecurringResponse> listRules(String userId) {
        return repo.findByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public RecurringResponse createRule(String userId, RecurringRequest req) {
        RecurringTransaction rt = mapper.toEntity(req);
        rt.setUserId(userId);
        rt.setCreatedAt(new Date());
        rt.setNextRun(computeNextRun(req));
        RecurringTransaction saved = repo.save(rt);
        return mapper.toResponse(saved);
    }

    @Override
    public RecurringResponse updateRule(String userId, String ruleId, RecurringRequest req) {
        RecurringTransaction rt = repo.findById(ruleId)
                .filter(r -> r.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Recurring rule not found"));
        rt.setPattern(req.pattern());
        rt.setDayOfMonth(req.dayOfMonth());
        rt.setDayOfWeek(req.dayOfWeek());
        rt.setTimeOfDay(req.timeOfDay());
        rt.setAmount(req.amount());
        rt.setCategory(req.category());
        rt.setTags(req.tags());
        rt.setNotes(req.notes());
        rt.setUpdatedAt(new Date());
        rt.setNextRun(computeNextRun(req));
        RecurringTransaction updated = repo.save(rt);
        return mapper.toResponse(updated);
    }

    @Override
    public void deleteRule(String userId, String ruleId) {
        RecurringTransaction rt = repo.findById(ruleId)
                .filter(r -> r.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Recurring rule not found"));
        repo.delete(rt);
    }

    /**
     * Compute the next execution datetime based on a pattern and timeOfDay.
     */
    private Date computeNextRun(RecurringRequest req) {
        LocalTime time = LocalTime.parse(req.timeOfDay(), DateTimeFormatter.ofPattern("HH:mm"));
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime next;
        LocalDate today = LocalDate.now(zone);

        switch (req.pattern()) {
            case "DAILY":
                next = LocalDateTime.of(today.plusDays(1), time);
                break;
            case "WEEKLY":
                DayOfWeek dow = DayOfWeek.valueOf(req.dayOfWeek());
                LocalDate nextDate = today.with(TemporalAdjusters.nextOrSame(dow));
                next = LocalDateTime.of(nextDate, time);
                // if today and time already passed, move to next week
                if (next.isBefore(LocalDateTime.now(zone))) {
                    next = next.plusWeeks(1);
                }
                break;
            case "MONTHLY":
                int dom = req.dayOfMonth();
                LocalDate monthDate = today.withDayOfMonth(Math.min(dom, today.lengthOfMonth()));
                next = LocalDateTime.of(monthDate, time);
                if (next.isBefore(LocalDateTime.now(zone))) {
                    LocalDate nextMonth = today.plusMonths(1);
                    int day = Math.min(dom, nextMonth.lengthOfMonth());
                    next = LocalDateTime.of(LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), day), time);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown pattern: " + req.pattern());
        }
        return Date.from(next.atZone(zone).toInstant());
    }
}
