package dev.prithwish.ledgerlux.category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> listCategories(String userId);

    CategoryResponse createCategory(String userId, CategoryRequest request);

    CategoryResponse updateCategory(String userId, String categoryId, CategoryRequest request);

    void deleteCategory(String userId, String categoryId);
}
