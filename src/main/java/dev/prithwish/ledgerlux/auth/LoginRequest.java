package dev.prithwish.ledgerlux.auth;

public record LoginRequest(
        String email,
        String password
) {
}
