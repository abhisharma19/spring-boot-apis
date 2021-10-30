package com.change.management.apichangemanagement.domain;

public enum BillDenominations {

    ONE(1),
    TWO(2),
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100);

    private final int value;

    BillDenominations(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
