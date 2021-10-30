package com.change.management.apichangemanagement.exception;

import com.change.management.apichangemanagement.domain.exception.Error;

public class BadRequestException extends TenderChangeException {

    private final transient Error error;

    public BadRequestException(Error error) {
        super("Bad Request");
        this.error = error;
    }

    public BadRequestException(Error error, String message) {
        super(message);
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
