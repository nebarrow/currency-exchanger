package com.nebarrow.exception;

import java.sql.SQLException;

public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException(String message) {
        super(message);
    }

    public ElementNotFoundException(String message, SQLException e) {
        super(message, e);
    }
}
