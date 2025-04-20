package dev.prithwish.ledgerlux.auth;

public record RegisterRequest(
        String email,
        String password,
        String displayName,
        String currency,
        String timezone
) {
}
