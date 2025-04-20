package dev.prithwish.ledgerlux.transaction;

import dev.prithwish.ledgerlux.common.annotation.CurrentUser;
import dev.prithwish.ledgerlux.common.dto.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService txnService;

    public TransactionController(TransactionService txnService) {
        this.txnService = txnService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<TransactionResponse>> list(
            @CurrentUser String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<TransactionResponse> transactionPage = txnService.listTransactions(userId, startDate, endDate, page, size);
        PageResponse<TransactionResponse> resp = new PageResponse<>(
                transactionPage.getContent(),
                transactionPage.getNumber(),
                transactionPage.getSize(),
                transactionPage.getTotalElements(),
                transactionPage.getTotalPages()
        );
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> get(
            @CurrentUser String userId,
            @PathVariable("id") String id) {
        TransactionResponse res = txnService.getTransaction(userId, id);
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @CurrentUser String userId,
            @Valid @RequestBody TransactionRequest req) {
        TransactionResponse res = txnService.createTransaction(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
            @CurrentUser String userId,
            @PathVariable("id") String id,
            @Valid @RequestBody TransactionRequest req) {
        TransactionResponse res = txnService.updateTransaction(userId, id, req);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @CurrentUser String userId,
            @PathVariable("id") String id) {
        txnService.deleteTransaction(userId, id);
        return ResponseEntity.noContent().build();
    }
}
