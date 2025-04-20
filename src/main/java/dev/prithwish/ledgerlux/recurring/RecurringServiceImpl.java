package dev.prithwish.ledgerlux.recurring;

import dev.prithwish.ledgerlux.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static dev.prithwish.ledgerlux.recurring.RecurrenceUtil.computeNextRun;

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
}
