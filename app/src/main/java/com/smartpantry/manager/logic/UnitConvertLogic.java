package com.smartpantry.manager.logic;

// handles unit conversions and compatibility checks for grocery measurements
public class UnitConvertLogic {

    // checks if two units belong to the same measurement type
    public static boolean areUnitsCompatible(String u1, String u2) {
        if (u1 == null || u2 == null) {
            return false;
        }
        String unitA = u1.trim().toLowerCase();
        String unitB = u2.trim().toLowerCase();

        if (unitA.equals(unitB)) {
            return true;
        }

        // handles weight compatibility (grams, kilograms, ounces, pounds)
        boolean isWeightA = unitA.equals("g") || unitA.equals("kg") || unitA.equals("oz") || unitA.equals("lb");
        boolean isWeightB = unitB.equals("g") || unitB.equals("kg") || unitB.equals("oz") || unitB.equals("lb");
        if (isWeightA && isWeightB) {
            return true;
        }

        // handles volume compatibility (milliliters, liters, fluid ounces)
        boolean isVolumeA = unitA.equals("ml") || unitA.equals("l") || unitA.equals("fl oz");
        boolean isVolumeB = unitB.equals("ml") || unitB.equals("l") || unitB.equals("fl oz");
        if (isVolumeA && isVolumeB) {
            return true;
        }

        return false;
    }

    // converts quantities to base units (grams for weight, ml for volume)
    public static double toBaseUnit(double qty, String unit) {
        if (unit == null) {
            return qty;
        }
        String normUnit = unit.trim().toLowerCase();

        // handles metric weight conversion
        if (normUnit.equals("kg")) {
            return qty * 1000.0;
        }

        // handles metric volume conversion
        if (normUnit.equals("l")) {
            return qty * 1000.0;
        }

        // handles imperial weight conversion
        if (normUnit.equals("oz")) {
            return qty * 28.3495;
        }
        if (normUnit.equals("lb")) {
            return qty * 453.592;
        }

        // handles imperial volume conversion
        if (normUnit.equals("fl oz")) {
            return qty * 29.5735;
        }

        // base units (g, ml, pcs) remain unchanged
        return qty;
    }

    // handles formatting quantity to hide doubles unless a user enters them
    public static String formatQty(double val) {
        if (val == (long) val) {
            return String.valueOf((long) val);
        } else {
            return String.valueOf(val);
        }
    }
}
