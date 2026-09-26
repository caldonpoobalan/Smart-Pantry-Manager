package com.smartpantry.manager.logic;

import com.smartpantry.manager.model.RecpEntity;
import com.smartpantry.manager.model.RecpIngredEntity;
import com.smartpantry.manager.model.StockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// checks recipes against available pantry stock
public class RecpMatchLogic {

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
                boolean reqMet = false;

                // look through pantry stock items
                for (StockEntity stkItm : stkLst) {
                    if (IngredParseLogic.isMatch(stkItm.getItmNm(), reqIng.getIngNm())) {
                        String reqUnt = reqIng.getUntMeas() != null ? reqIng.getUntMeas().trim().toLowerCase() : "";
                        String stkUnt = stkItm.getUntLbl() != null ? stkItm.getUntLbl().trim().toLowerCase() : "";

                        // if units match then check quantity
                        if (reqUnt.equals(stkUnt)) {
                            if (stkItm.getQtyVal() >= reqIng.getReqQty()) {
                                reqMet = true;
                                break;
                            }
                        } else {
                            // match if unit format differs
                            reqMet = true;
                            break;
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
}
