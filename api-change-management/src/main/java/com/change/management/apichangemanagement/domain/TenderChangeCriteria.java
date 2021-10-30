package com.change.management.apichangemanagement.domain;

public class TenderChangeCriteria {

    private BillDenominations denomination;

    private boolean allowMaximumCoins = false;

    public BillDenominations getDenomination() {
        return denomination;
    }

    public void setDenomination(BillDenominations denomination) {
        this.denomination = denomination;
    }

    public boolean isAllowMaximumCoins() {
        return allowMaximumCoins;
    }

    public void setAllowMaximumCoins(boolean allowMaximumCoins) {
        this.allowMaximumCoins = allowMaximumCoins;
    }
}
