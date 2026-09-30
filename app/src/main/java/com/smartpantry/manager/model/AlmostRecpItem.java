package com.smartpantry.manager.model;

// model for a recipe that is missing exactly one ingredient
public class AlmostRecpItem {

    private RecpEntity recp;
    private String missingIngName;
    private double missingQty;
    private String missingUnit;

    public AlmostRecpItem(RecpEntity recp, String missingIngName, double missingQty, String missingUnit) {
        this.recp = recp;
        this.missingIngName = missingIngName;
        this.missingQty = missingQty;
        this.missingUnit = missingUnit;
    }

    public RecpEntity getRecp() {
        return recp;
    }

    public String getMissingIngName() {
        return missingIngName;
    }

    public double getMissingQty() {
        return missingQty;
    }

    public String getMissingUnit() {
        return missingUnit;
    }
}
