package com.nebarrow.exception;

import java.sql.SQLException;

public class ElementAlreadyExistsException extends RuntimeException {
    public ElementAlreadyExistsException(String message) {
        super(message);
    }

    public ElementAlreadyExistsException(String message, SQLException e) {
        super(message, e);
    }
}
