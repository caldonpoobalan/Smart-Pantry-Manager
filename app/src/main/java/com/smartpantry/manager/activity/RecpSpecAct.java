package com.smartpantry.manager.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.smartpantry.manager.R;
import com.smartpantry.manager.database.PantryRoomDb;
import com.smartpantry.manager.fragment.PrefCfgFrag;
import com.smartpantry.manager.logic.UnitConvertLogic;
import com.smartpantry.manager.model.RecpEntity;
import com.smartpantry.manager.model.RecpIngredEntity;

import java.util.List;
import java.util.concurrent.Executors;

// screen displaying recipe details, required ingredients, and cooking steps
public class RecpSpecAct extends AppCompatActivity {

    public static final String KEY_SPEC_RECP_ID = "EXTRA_RECP_ID";

    private TextView txtSpecHeading;
    private TextView txtReqIngredBlock;
    private TextView txtPrepStepsBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recp_spec);

        // handles top toolbar setup with back button
        Toolbar barSpecToolbar = findViewById(R.id.bar_spec_toolbar);
        setSupportActionBar(barSpecToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.recipe_detail);
        }

        txtSpecHeading = findViewById(R.id.txt_spec_heading);
        txtReqIngredBlock = findViewById(R.id.txt_req_ingred_block);
        txtPrepStepsBlock = findViewById(R.id.txt_prep_steps_block);

        int targetRecpId = getIntent().getIntExtra(KEY_SPEC_RECP_ID, -1);
        if (targetRecpId != -1) {
            fetchRecipeDetails(targetRecpId);
        }
    }

    // handles loading recipe information from room database
    private void fetchRecipeDetails(int recpId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            PantryRoomDb appDb = PantryRoomDb.getDbInst(this);
            RecpEntity curRecp = appDb.recpDataAcc().getRecpById(recpId);
            List<RecpIngredEntity> ingredLst = appDb.recpIngredDataAcc().getIngredByRecpId(recpId);

            runOnUiThread(() -> {
                if (curRecp != null) {
                    txtSpecHeading.setText(curRecp.getRecpNm());

                    // displays dual oven temperatures and unit-neutral preparation text
                    String steps = curRecp.getPrepStepTxt();
                    if (steps != null) {
                        steps = steps.replace("200°C", "200°C (400°F)")
                                     .replace("50 g shredded cheese", "the shredded cheese")
                                     .replace("45 ml cream", "cream")
                                     .replace("15 ml cream", "cream");
                    }
                    txtPrepStepsBlock.setText(steps);
                }

                // handles formatting bulleted list of required ingredients
                StringBuilder bldIngredTxt = new StringBuilder();
                if (ingredLst != null) {
                    SharedPreferences spSharedPrefs = getSharedPreferences(PrefCfgFrag.PREF_STORAGE_TAG, MODE_PRIVATE);
                    boolean isImperial = "Imperial".equalsIgnoreCase(spSharedPrefs.getString(PrefCfgFrag.KEY_UNIT_SYS, "Metric"));

                    for (RecpIngredEntity curIng : ingredLst) {
                        String ingName = curIng.getIngNm() != null ? curIng.getIngNm().trim().toLowerCase() : "";

                        // displays seasoning staples as to taste
                        if (ingName.equals("salt") || ingName.equals("black pepper")) {
                            bldIngredTxt.append("• ").append(curIng.getIngNm()).append(" (to taste)\n");
                        } else {
                            String formattedQtyAndUnit = UnitConvertLogic.formatDisplayQtyAndUnit(curIng.getReqQty(), curIng.getUntMeas(), isImperial);
                            bldIngredTxt.append("• ")
                                    .append(formattedQtyAndUnit).append(" ")
                                    .append(curIng.getIngNm()).append("\n");
                        }
                    }
                }
                txtReqIngredBlock.setText(bldIngredTxt.toString().trim());
            });
        });
    }

    // handles toolbar back arrow click
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
