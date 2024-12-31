package com.nebarrow.validation;

public class ValidationResult {
    private static String resultMessage;

    public ValidationResult(String message) {
        resultMessage = message;
    }

    public boolean hasErrors() {
        return !resultMessage.isEmpty();
    }

    public String getMessage() {
        return resultMessage;
    }
}
