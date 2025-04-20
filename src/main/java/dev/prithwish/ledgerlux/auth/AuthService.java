package dev.prithwish.ledgerlux.auth;

public interface AuthService {
    SignUpResponse register(RegisterRequest req);

    AuthResponse login(LoginRequest req);

    AuthResponse refreshToken(String refreshToken);
}
