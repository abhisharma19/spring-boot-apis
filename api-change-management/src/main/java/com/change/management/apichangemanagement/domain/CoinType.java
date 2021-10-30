package com.change.management.apichangemanagement.domain;

public enum CoinType {

    PENNY("Penny"),
    NICKEL("Nickel"),
    DIME("Dime"),
    QUARTER("Quarter");

    private final String value;

    CoinType(final String newValue) {
        value = newValue;
    }

    public String getValue() { return value; }
}
