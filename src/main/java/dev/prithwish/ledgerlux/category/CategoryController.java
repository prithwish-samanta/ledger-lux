package dev.prithwish.ledgerlux.category;

import dev.prithwish.ledgerlux.common.annotation.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> list(@CurrentUser String userId) {
        return ResponseEntity.ok(service.listCategories(userId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @CurrentUser String userId,
            @Valid @RequestBody CategoryRequest req) {
        CategoryResponse res = service.createCategory(userId, req);
        return ResponseEntity.status(201).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @CurrentUser String userId,
            @PathVariable("id") String id,
            @Valid @RequestBody CategoryRequest req) {
        CategoryResponse res = service.updateCategory(userId, id, req);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @CurrentUser String userId,
            @PathVariable("id") String id) {
        service.deleteCategory(userId, id);
        return ResponseEntity.noContent().build();
    }
}
