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

        // handles volume compatibility (milliliters, liters, fluid ounces, gallons)
        boolean isVolumeA = unitA.equals("ml") || unitA.equals("l") || unitA.equals("fl oz") || unitA.equals("gal");
        boolean isVolumeB = unitB.equals("ml") || unitB.equals("l") || unitB.equals("fl oz") || unitB.equals("gal");
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
        if (normUnit.equals("gal")) {
            return qty * 3785.41;
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

    // handles formatting display quantity and unit based on active metric or imperial preference
    public static String formatDisplayQtyAndUnit(double rawQty, String rawUnit, boolean isImperial) {
        if (rawUnit == null) {
            return formatQty(rawQty);
        }
        String normUnit = rawUnit.trim().toLowerCase();

        if (normUnit.equals("pcs")) {
            return formatQty(rawQty) + " pcs";
        }

        // handles weight formatting
        boolean isWeight = normUnit.equals("g") || normUnit.equals("kg") || normUnit.equals("oz") || normUnit.equals("lb");
        if (isWeight) {
            double baseGrams = toBaseUnit(rawQty, normUnit);
            if (isImperial) {
                if (baseGrams >= 453.592) {
                    double lbs = Math.round((baseGrams / 453.592) * 10.0) / 10.0;
                    return formatQty(lbs) + " lb";
                } else {
                    double oz = Math.round((baseGrams / 28.3495) * 10.0) / 10.0;
                    if (oz <= 0.0 && baseGrams > 0) {
                        return "< 0.1 oz";
                    }
                    return formatQty(oz) + " oz";
                }
            } else {
                if (baseGrams >= 1000.0) {
                    double kg = Math.round((baseGrams / 1000.0) * 10.0) / 10.0;
                    return formatQty(kg) + " kg";
                } else {
                    double g = Math.round(baseGrams * 10.0) / 10.0;
                    return formatQty(g) + " g";
                }
            }
        }

        // handles volume formatting
        boolean isVolume = normUnit.equals("ml") || normUnit.equals("l") || normUnit.equals("fl oz") || normUnit.equals("gal");
        if (isVolume) {
            double baseMl = toBaseUnit(rawQty, normUnit);
            if (isImperial) {
                if (baseMl >= 3785.41) {
                    double gal = Math.round((baseMl / 3785.41) * 10.0) / 10.0;
                    return formatQty(gal) + " gal";
                } else {
                    double flOz = Math.round((baseMl / 29.5735) * 10.0) / 10.0;
                    if (flOz <= 0.0 && baseMl > 0) {
                        return "< 0.1 fl oz";
                    }
                    return formatQty(flOz) + " fl oz";
                }
            } else {
                if (baseMl >= 1000.0) {
                    double l = Math.round((baseMl / 1000.0) * 10.0) / 10.0;
                    return formatQty(l) + " L";
                } else {
                    double ml = Math.round(baseMl * 10.0) / 10.0;
                    return formatQty(ml) + " ml";
                }
            }
        }

        return formatQty(rawQty) + " " + rawUnit;
    }
}
