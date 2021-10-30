package com.change.management.apichangemanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenderChangeException extends RuntimeException {

    private static final long serialVersionUID = 7837582758395738592L;

    private String message;

    public TenderChangeException() {

    }

    public TenderChangeException(String message) {
        super(message);
    }

}
