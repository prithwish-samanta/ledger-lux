package dev.prithwish.ledgerlux.category;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    // Entity → DTO
    CategoryResponse toResponse(Category txn);

    // DTO → Entity
    Category toEntity(CategoryRequest req);
}
