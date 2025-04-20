package dev.prithwish.ledgerlux.auth;

import java.util.Date;

public record SignUpResponse(
        String id,
        String email,
        String displayName,
        String currency,
        String timezone,
        Date createdAt
) {
}
