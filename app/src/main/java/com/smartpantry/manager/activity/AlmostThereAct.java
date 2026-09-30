package com.smartpantry.manager.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.R;
import com.smartpantry.manager.adapter.AlmostRecpAdapt;
import com.smartpantry.manager.database.PantryRoomDb;
import com.smartpantry.manager.database.RecpSeedData;
import com.smartpantry.manager.logic.RecpMatchLogic;
import com.smartpantry.manager.model.AlmostRecpItem;
import com.smartpantry.manager.model.RecpEntity;
import com.smartpantry.manager.model.RecpIngredEntity;
import com.smartpantry.manager.model.StockEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

// screen displaying recipes that are missing only one ingredient
public class AlmostThereAct extends AppCompatActivity {

    private RecyclerView recAlmostLst;
    private View layAlmostEmptyNotice;
    private AlmostRecpAdapt almostAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_almost_there);

        // handles top toolbar setup with back arrow navigation
        Toolbar barAlmostToolbar = findViewById(R.id.bar_almost_toolbar);
        setSupportActionBar(barAlmostToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.almost_there_title);
        }

        recAlmostLst = findViewById(R.id.rec_almost_lst);
        layAlmostEmptyNotice = findViewById(R.id.lay_almost_empty_notice);

        recAlmostLst.setLayoutManager(new LinearLayoutManager(this));
        almostAdapt = new AlmostRecpAdapt(new ArrayList<>());
        recAlmostLst.setAdapter(almostAdapt);

        loadAlmostThereRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlmostThereRecipes();
    }

    // handles loading recipes missing exactly one ingredient in background
    private void loadAlmostThereRecipes() {
        Executors.newSingleThreadExecutor().execute(() -> {
            PantryRoomDb appDb = PantryRoomDb.getDbInst(this);
            // ensures full recipe seed catalog is populated
            RecpSeedData.ensureSeedData(appDb);
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

            // runs the almost-there matching algorithm
            List<AlmostRecpItem> results = RecpMatchLogic.findAlmostMatchingRecp(stkItms, allRecps, ingByRecpMap);

            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    almostAdapt.refreshAlmostData(results);
                    if (results.isEmpty()) {
                        layAlmostEmptyNotice.setVisibility(View.VISIBLE);
                        recAlmostLst.setVisibility(View.GONE);
                    } else {
                        layAlmostEmptyNotice.setVisibility(View.GONE);
                        recAlmostLst.setVisibility(View.VISIBLE);
                    }
                }
            });
        });
    }

    // handles back navigation button press in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
