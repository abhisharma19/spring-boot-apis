package com.change.management.apichangemanagement.domain.exception;

public enum MessageType {
    ERROR("Error"),
    WARNING("Warning"),
    INFO("Info");

    private final String value;

    MessageType(final String newValue) {
        this.value = newValue;
    }

    public String getValue() {
        return value;
    }
}
