package com.smartpantry.manager.logic;

import com.smartpantry.manager.model.AlmostRecpItem;
import com.smartpantry.manager.model.RecpEntity;
import com.smartpantry.manager.model.RecpIngredEntity;
import com.smartpantry.manager.model.StockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// checks recipes against available pantry stock
public class RecpMatchLogic {

    // basic nonleftover staples for now that people normally have in thier kitchens, I might add others here later
    private static boolean isPantryStaple(String ingNm) {
        String norm = IngredParseLogic.normTerm(ingNm);
        return norm.equals("salt") || norm.equals("black pepper")
                || norm.equals("water") || norm.equals("sugar");
    }

    // finds recipes where user has all required ingredients
    public static List<RecpEntity> findMatchingRecp(
            List<StockEntity> stkLst,
            List<RecpEntity> allRecpLst,
            Map<Integer, List<RecpIngredEntity>> recpIngMap) {

        List<RecpEntity> mtchRecpLst = new ArrayList<>();

        // loop through each recipe in catalog
        for (RecpEntity curRecp : allRecpLst) {
            List<RecpIngredEntity> reqIngLst = recpIngMap.get(curRecp.getRecpId());
            if (reqIngLst == null || reqIngLst.isEmpty()) {
                continue;
            }

            boolean allIngOk = true;

            // check each required ingredient for this recipe
            for (RecpIngredEntity reqIng : reqIngLst) {
                // skip checking if this is an assumed kitchen staple
                if (isPantryStaple(reqIng.getIngNm())) {
                    continue;
                }

                boolean reqMet = false;

                // look through pantry stock items
                for (StockEntity stkItm : stkLst) {
                    if (IngredParseLogic.isMatch(stkItm.getItmNm(), reqIng.getIngNm())) {
                        String reqUnt = reqIng.getUntMeas() != null ? reqIng.getUntMeas().trim().toLowerCase() : "";
                        String stkUnt = stkItm.getUntLbl() != null ? stkItm.getUntLbl().trim().toLowerCase() : "";

                        // handles unit conversion and quantity comparison
                        if (UnitConvertLogic.areUnitsCompatible(reqUnt, stkUnt)) {
                            double baseStkQty = UnitConvertLogic.toBaseUnit(stkItm.getQtyVal(), stkUnt);
                            double baseReqQty = UnitConvertLogic.toBaseUnit(reqIng.getReqQty(), reqUnt);

                            if (baseStkQty >= baseReqQty) {
                                reqMet = true;
                                break;
                            }
                        } else if (stkUnt.equals("pcs")) {
                            // handles converting stocked pieces to grams for recipe requirements
                            double pieceWeight = UnitConvertLogic.getEstimatedPieceWeight(stkItm.getItmNm());
                            double estStkGrams = stkItm.getQtyVal() * pieceWeight;
                            double baseReqQty = UnitConvertLogic.toBaseUnit(reqIng.getReqQty(), reqUnt);

                            if (estStkGrams >= baseReqQty) {
                                reqMet = true;
                                break;
                            }
                        } else if (reqUnt.equals("pcs")) {
                            // handles converting required pieces to grams against stocked weight
                            double pieceWeight = UnitConvertLogic.getEstimatedPieceWeight(reqIng.getIngNm());
                            double estReqGrams = reqIng.getReqQty() * pieceWeight;
                            double baseStkQty = UnitConvertLogic.toBaseUnit(stkItm.getQtyVal(), stkUnt);

                            if (baseStkQty >= estReqGrams) {
                                reqMet = true;
                                break;
                            }
                        } else {
                            // handles fallback for non-convertible units
                            if (stkItm.getQtyVal() >= reqIng.getReqQty()) {
                                reqMet = true;
                                break;
                            }
                        }
                    }
                }

                // if any ingredient is missing then recipe fails
                if (!reqMet) {
                    allIngOk = false;
                    break;
                }
            }

            // recipe only added if all ingredients are satisfied
            if (allIngOk) {
                mtchRecpLst.add(curRecp);
            }
        }

        return mtchRecpLst;
    }

    // finds recipes where the pantry is missing exactly one required ingredient
    public static List<AlmostRecpItem> findAlmostMatchingRecp(
            List<StockEntity> stkLst,
            List<RecpEntity> allRecpLst,
            Map<Integer, List<RecpIngredEntity>> recpIngMap) {

        List<AlmostRecpItem> almostList = new ArrayList<>();

        for (RecpEntity curRecp : allRecpLst) {
            List<RecpIngredEntity> reqIngLst = recpIngMap.get(curRecp.getRecpId());
            if (reqIngLst == null || reqIngLst.isEmpty()) {
                continue;
            }

            List<RecpIngredEntity> missingIngreds = new ArrayList<>();

            for (RecpIngredEntity reqIng : reqIngLst) {
                // skip checking if this is an assumed kitchen staple
                if (isPantryStaple(reqIng.getIngNm())) {
                    continue;
                }

                boolean reqMet = false;

                // look through pantry stock items
                for (StockEntity stkItm : stkLst) {
                    if (IngredParseLogic.isMatch(stkItm.getItmNm(), reqIng.getIngNm())) {
                        String reqUnt = reqIng.getUntMeas() != null ? reqIng.getUntMeas().trim().toLowerCase() : "";
                        String stkUnt = stkItm.getUntLbl() != null ? stkItm.getUntLbl().trim().toLowerCase() : "";

                        // handles unit conversion and quantity comparison
                        if (UnitConvertLogic.areUnitsCompatible(reqUnt, stkUnt)) {
                            double baseStkQty = UnitConvertLogic.toBaseUnit(stkItm.getQtyVal(), stkUnt);
                            double baseReqQty = UnitConvertLogic.toBaseUnit(reqIng.getReqQty(), reqUnt);

                            if (baseStkQty >= baseReqQty) {
                                reqMet = true;
                                break;
                            }
                        } else if (stkUnt.equals("pcs")) {
                            // handles converting stocked pieces to grams for recipe requirements
                            double pieceWeight = UnitConvertLogic.getEstimatedPieceWeight(stkItm.getItmNm());
                            double estStkGrams = stkItm.getQtyVal() * pieceWeight;
                            double baseReqQty = UnitConvertLogic.toBaseUnit(reqIng.getReqQty(), reqUnt);

                            if (estStkGrams >= baseReqQty) {
                                reqMet = true;
                                break;
                            }
                        } else if (reqUnt.equals("pcs")) {
                            // handles converting required pieces to grams against stocked weight
                            double pieceWeight = UnitConvertLogic.getEstimatedPieceWeight(reqIng.getIngNm());
                            double estReqGrams = reqIng.getReqQty() * pieceWeight;
                            double baseStkQty = UnitConvertLogic.toBaseUnit(stkItm.getQtyVal(), stkUnt);

                            if (baseStkQty >= estReqGrams) {
                                reqMet = true;
                                break;
                            }
                        } else {
                            // handles fallback for non-convertible units
                            if (stkItm.getQtyVal() >= reqIng.getReqQty()) {
                                reqMet = true;
                                break;
                            }
                        }
                    }
                }

                // if ingredient is not satisfied, record as missing
                if (!reqMet) {
                    missingIngreds.add(reqIng);
                }
            }

            // recipe qualifies as almost there if exactly one ingredient is missing
            if (missingIngreds.size() == 1) {
                RecpIngredEntity missing = missingIngreds.get(0);
                almostList.add(new AlmostRecpItem(curRecp, missing.getIngNm(), missing.getReqQty(), missing.getUntMeas()));
            }
        }

        return almostList;
    }
}
