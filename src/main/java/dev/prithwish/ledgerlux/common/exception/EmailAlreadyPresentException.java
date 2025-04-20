package dev.prithwish.ledgerlux.common.exception;

public class EmailAlreadyPresentException extends RuntimeException {
    public EmailAlreadyPresentException(String message) {
        super(message);
    }
}
