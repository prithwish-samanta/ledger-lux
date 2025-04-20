package dev.prithwish.ledgerlux.common.exception;

public class AuthTokenExpiredException extends RuntimeException {
    public AuthTokenExpiredException(String message) {
        super(message);
    }
}
