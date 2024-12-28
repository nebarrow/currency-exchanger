package com.nebarrow.exception;

import java.sql.SQLException;

public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, SQLException e) {
        super(message, e);
    }
}
