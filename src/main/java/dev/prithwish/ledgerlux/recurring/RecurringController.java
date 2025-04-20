package dev.prithwish.ledgerlux.recurring;

import dev.prithwish.ledgerlux.common.annotation.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions/recurring")
public class RecurringController {
    private final RecurringService service;

    public RecurringController(RecurringService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RecurringResponse>> list(@CurrentUser String userId) {
        return ResponseEntity.ok(service.listRules(userId));
    }

    @PostMapping
    public ResponseEntity<RecurringResponse> create(@CurrentUser String userId,
                                                    @Valid @RequestBody RecurringRequest req) {
        RecurringResponse resp = service.createRule(userId, req);
        return ResponseEntity.status(201).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringResponse> update(@CurrentUser String userId,
                                                    @PathVariable("id") String id,
                                                    @Valid @RequestBody RecurringRequest req) {
        RecurringResponse resp = service.updateRule(userId, id, req);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@CurrentUser String userId,
                                       @PathVariable("id") String id) {
        service.deleteRule(userId, id);
        return ResponseEntity.noContent().build();
    }
}
