package org.example.util;

public class InputParsingException extends Exception {
    public InputParsingException(String message) {
        super(message);
    }

    public InputParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
