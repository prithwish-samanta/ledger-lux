package dev.prithwish.ledgerlux.recurring;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecurringTransactionMapper {
    // Entity → DTO
    RecurringResponse toResponse(RecurringTransaction txn);

    // DTO → Entity
    RecurringTransaction toEntity(RecurringRequest req);
}
