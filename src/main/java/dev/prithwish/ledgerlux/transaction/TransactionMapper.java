package dev.prithwish.ledgerlux.transaction;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    // Entity → DTO
    TransactionResponse toResponse(Transaction txn);

    // DTO → Entity
    Transaction toEntity(TransactionRequest req);
}
