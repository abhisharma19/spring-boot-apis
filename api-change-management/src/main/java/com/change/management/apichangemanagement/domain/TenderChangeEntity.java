package com.change.management.apichangemanagement.domain;

import java.io.Serializable;

public class TenderChangeEntity implements Serializable {

    private static final long serialVersionUID = 6783516463278235627L;

    private int pennyCount;
    private int nickelCount;
    private int dimeCount;
    private int quarterCount;

    public int getPennyCount() {
        return pennyCount;
    }

    public void setPennyCount(int pennyCount) {
        this.pennyCount = pennyCount;
    }

    public int getNickelCount() {
        return nickelCount;
    }

    public void setNickelCount(int nickelCount) {
        this.nickelCount = nickelCount;
    }

    public int getDimeCount() {
        return dimeCount;
    }

    public void setDimeCount(int dimeCount) {
        this.dimeCount = dimeCount;
    }

    public int getQuarterCount() {
        return quarterCount;
    }

    public void setQuarterCount(int quarterCount) {
        this.quarterCount = quarterCount;
    }
}
