package dev.prithwish.ledgerlux.recurring;

import java.util.List;

public interface RecurringService {
    List<RecurringResponse> listRules(String userId);

    RecurringResponse createRule(String userId, RecurringRequest request);

    RecurringResponse updateRule(String userId, String ruleId, RecurringRequest request);

    void deleteRule(String userId, String ruleId);
}
