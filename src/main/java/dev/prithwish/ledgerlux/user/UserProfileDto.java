package dev.prithwish.ledgerlux.user;

import java.util.Date;

public record UserProfileDto(
        String id,
        String email,
        String displayName,
        String currency,
        String timezone,
        Date createdAt
) {
}
