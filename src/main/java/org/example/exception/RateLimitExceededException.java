package org.example.exception;

public class RateLimitExceededException extends NotificationException {

    public RateLimitExceededException(String message) {
        super(message);
    }

}