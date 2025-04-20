package dev.prithwish.ledgerlux.category;

import dev.prithwish.ledgerlux.common.exception.ResourceAlreadyPresentException;
import dev.prithwish.ledgerlux.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryRepository repo, CategoryMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public List<CategoryResponse> listCategories(String userId) {
        return repo.findByUserId(userId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse createCategory(String userId, CategoryRequest request) {
        repo.findByUserIdAndName(userId, request.name()).ifPresent(c -> {
            throw new ResourceAlreadyPresentException("Category already exists");
        });
        Category cat = mapper.toEntity(request);
        cat.setUserId(userId);
        cat.setCreatedAt(new Date());
        Category saved = repo.save(cat);
        return mapper.toResponse(saved);
    }

    @Override
    public CategoryResponse updateCategory(String userId, String categoryId, CategoryRequest request) {
        Category cat = repo.findById(categoryId)
                .filter(c -> c.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        cat.setName(request.name());
        cat.setUpdatedAt(new Date());
        Category updated = repo.save(cat);
        return mapper.toResponse(updated);
    }

    @Override
    public void deleteCategory(String userId, String categoryId) {
        Category cat = repo.findById(categoryId)
                .filter(c -> c.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        repo.delete(cat);
    }
}
