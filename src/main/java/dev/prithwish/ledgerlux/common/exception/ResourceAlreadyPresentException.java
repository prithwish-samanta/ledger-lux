package dev.prithwish.ledgerlux.common.exception;

public class ResourceAlreadyPresentException extends RuntimeException {
    public ResourceAlreadyPresentException(String message) {
        super(message);
    }
}
