package com.smartpantry.manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.R;
import com.smartpantry.manager.activity.RecpSpecAct;
import com.smartpantry.manager.adapter.RecpCardAdapt;
import com.smartpantry.manager.database.PantryRoomDb;
import com.smartpantry.manager.logic.RecpMatchLogic;
import com.smartpantry.manager.model.RecpEntity;
import com.smartpantry.manager.model.RecpIngredEntity;
import com.smartpantry.manager.model.StockEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

// fragment displaying recipe suggestions based on current pantry stock
public class MatchRecpFrag extends Fragment implements RecpCardAdapt.OnRecpCardClickListener {

    private RecyclerView recMatchedLst;
    private TextView txtZeroMatchWarn;
    private TextView txtRecpCounter;
    private RecpCardAdapt recpAdapt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragVw = inflater.inflate(R.layout.frag_match_recp, container, false);

        recMatchedLst = fragVw.findViewById(R.id.rec_matched_lst);
        txtZeroMatchWarn = fragVw.findViewById(R.id.txt_zero_match_warn);
        txtRecpCounter = fragVw.findViewById(R.id.txt_recp_counter);

        recMatchedLst.setLayoutManager(new LinearLayoutManager(getContext()));
        recpAdapt = new RecpCardAdapt(new ArrayList<>(), this);
        recMatchedLst.setAdapter(recpAdapt);

        return fragVw;
    }

    @Override
    public void onResume() {
        super.onResume();
        executeRecipeMatching();
    }

    // handles recipe matching by querying stock and recipes from database
    private void executeRecipeMatching() {
        Executors.newSingleThreadExecutor().execute(() -> {
            PantryRoomDb appDb = PantryRoomDb.getDbInst(getContext());
            List<StockEntity> stkItms = appDb.stockDataAcc().getAllStock();
            List<RecpEntity> allRecps = appDb.recpDataAcc().getAllRecp();
            List<RecpIngredEntity> allIngreds = appDb.recpIngredDataAcc().getAllIngred();

            // handles grouping recipe ingredients by recipe id
            Map<Integer, List<RecpIngredEntity>> ingByRecpMap = new HashMap<>();
            for (RecpIngredEntity curIng : allIngreds) {
                if (!ingByRecpMap.containsKey(curIng.getRecpIdRef())) {
                    ingByRecpMap.put(curIng.getRecpIdRef(), new ArrayList<>());
                }
                ingByRecpMap.get(curIng.getRecpIdRef()).add(curIng);
            }

            // runs the strict matching algorithm against current stock
            List<RecpEntity> matchedRecps = RecpMatchLogic.findMatchingRecp(stkItms, allRecps, ingByRecpMap);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    recpAdapt.refreshRecpData(matchedRecps);
                    int count = matchedRecps.size();

                    // handles counter message and empty state based on number of matches
                    if (count == 0) {
                        txtZeroMatchWarn.setVisibility(View.VISIBLE);
                        recMatchedLst.setVisibility(View.GONE);
                        txtRecpCounter.setText("You can't make any recipes yet");
                    } else {
                        txtZeroMatchWarn.setVisibility(View.GONE);
                        recMatchedLst.setVisibility(View.VISIBLE);

                        if (count == 1) {
                            txtRecpCounter.setText("Yay, you can make 1 recipe!");
                        } else if (count <= 4) {
                            txtRecpCounter.setText("Wow, you can make " + count + " recipes!");
                        } else {
                            txtRecpCounter.setText("Woah, you can make " + count + " recipes!");
                        }
                    }
                });
            }
        });
    }

    // tap the recipe card to open detailed recipe view
    @Override
    public void onRecpSelected(RecpEntity recp) {
        Intent viewSpecInt = new Intent(getContext(), RecpSpecAct.class);
        viewSpecInt.putExtra(RecpSpecAct.KEY_SPEC_RECP_ID, recp.getRecpId());
        startActivity(viewSpecInt);
    }
}
