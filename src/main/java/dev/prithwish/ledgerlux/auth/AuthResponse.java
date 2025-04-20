package dev.prithwish.ledgerlux.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
}
