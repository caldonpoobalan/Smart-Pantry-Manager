package com.smartpantry.manager.activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.smartpantry.manager.R;
import com.smartpantry.manager.database.PantryRoomDb;
import com.smartpantry.manager.fragment.PrefCfgFrag;
import com.smartpantry.manager.logic.UnitConvertLogic;
import com.smartpantry.manager.model.StockEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

// screen for adding a new ingredient or editing an existing stock item
public class IngredFormAct extends AppCompatActivity {

    public static final String KEY_ROW_ID = "EXTRA_ROW_ID";

    private EditText edtIngNm;
    private EditText edtQtyVal;
    private Spinner spnUnitOpt;
    private Button btnPickExp;
    private TextView txtExpDisp;
    private Button btnSaveRec;

    private Long expDateMs = null;
    private int targetRowId = -1;

    // standard physical units for pantry inventory
    private String[] unitOptArr = {"pcs", "g", "kg", "ml", "L"};
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ingred_form);

        // handles top toolbar setup with back button
        Toolbar barTopNav = findViewById(R.id.bar_top_nav);
        setSupportActionBar(barTopNav);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtIngNm = findViewById(R.id.edt_ing_name);
        edtQtyVal = findViewById(R.id.edt_qty_val);
        spnUnitOpt = findViewById(R.id.spn_unit_opt);
        btnPickExp = findViewById(R.id.btn_pick_exp);
        txtExpDisp = findViewById(R.id.txt_exp_disp);
        btnSaveRec = findViewById(R.id.btn_save_record);

        // sets available measurement units based on user preference
        SharedPreferences spSharedPrefs = getSharedPreferences(PrefCfgFrag.PREF_STORAGE_TAG, MODE_PRIVATE);
        boolean isImperial = "Imperial".equalsIgnoreCase(spSharedPrefs.getString(PrefCfgFrag.KEY_UNIT_SYS, "Metric"));
        if (isImperial) {
            unitOptArr = new String[]{"pcs", "oz", "lb", "fl oz", "gal"};
        } else {
            unitOptArr = new String[]{"pcs", "g", "kg", "ml", "L"};
        }

        // handles unit options spinner setup
        ArrayAdapter<String> adaptSpn = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitOptArr);
        adaptSpn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUnitOpt.setAdapter(adaptSpn);

        TextView txtTipsTitle = findViewById(R.id.txt_form_tips_title);
        TextView txtTipsBody = findViewById(R.id.txt_form_tips_body);

        // checks if this is an edit or add operation
        targetRowId = getIntent().getIntExtra(KEY_ROW_ID, -1);
        if (targetRowId > 0) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.edit_ingredient);
            }
            if (txtTipsTitle != null) {
                txtTipsTitle.setText(R.string.edit_ingredient_tips_title);
            }
            if (txtTipsBody != null) {
                txtTipsBody.setText(R.string.edit_ingredient_tips_body);
            }
            loadExistingStock();
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.add_ingredient);
            }
            if (txtTipsTitle != null) {
                txtTipsTitle.setText(R.string.add_ingredient_tips_title);
            }
            if (txtTipsBody != null) {
                txtTipsBody.setText(R.string.add_ingredient_tips_body);
            }
        }

        // handles button clicks for expiry picker and saving
        btnPickExp.setOnClickListener(v -> showDatePickerDlg());
        btnSaveRec.setOnClickListener(v -> commitStockItem());
    }

    // handles opening android calendar dialog to select expiry date
    private void showDatePickerDlg() {
        Calendar calInst = Calendar.getInstance();
        if (expDateMs != null) {
            calInst.setTimeInMillis(expDateMs);
        }

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selCal = Calendar.getInstance();
            selCal.set(year, month, dayOfMonth);
            expDateMs = selCal.getTimeInMillis();
            refreshExpDisplay();
        }, calInst.get(Calendar.YEAR), calInst.get(Calendar.MONTH), calInst.get(Calendar.DAY_OF_MONTH)).show();
    }

    // handles refreshing expiry date display label
    private void refreshExpDisplay() {
        if (expDateMs != null) {
            txtExpDisp.setText(dateFmt.format(new Date(expDateMs)));
        } else {
            txtExpDisp.setText(R.string.no_expiry);
        }
    }

    // handles loading existing item details from room database
    private void loadExistingStock() {
        Executors.newSingleThreadExecutor().execute(() -> {
            StockEntity curStock = PantryRoomDb.getDbInst(this).stockDataAcc().getStockById(targetRowId);
            if (curStock != null) {
                runOnUiThread(() -> {
                    edtIngNm.setText(curStock.getItmNm());
                    expDateMs = curStock.getExpDateMs();
                    refreshExpDisplay();

                    SharedPreferences spPrefs = getSharedPreferences(PrefCfgFrag.PREF_STORAGE_TAG, MODE_PRIVATE);
                    boolean isImperial = "Imperial".equalsIgnoreCase(spPrefs.getString(PrefCfgFrag.KEY_UNIT_SYS, "Metric"));

                    double displayQty = curStock.getQtyVal();
                    String displayUnit = curStock.getUntLbl() != null ? curStock.getUntLbl() : "pcs";

                    // converts existing units if user switched preference system
                    String norm = displayUnit.trim().toLowerCase();
                    if (isImperial) {
                        if (norm.equals("g") || norm.equals("kg")) {
                            double baseGrams = UnitConvertLogic.toBaseUnit(displayQty, norm);
                            if (baseGrams >= 453.592) {
                                displayQty = Math.round((baseGrams / 453.592) * 10.0) / 10.0;
                                displayUnit = "lb";
                            } else {
                                displayQty = Math.round((baseGrams / 28.3495) * 10.0) / 10.0;
                                displayUnit = "oz";
                            }
                        } else if (norm.equals("ml") || norm.equals("l")) {
                            double baseMl = UnitConvertLogic.toBaseUnit(displayQty, norm);
                            if (baseMl >= 3785.41) {
                                displayQty = Math.round((baseMl / 3785.41) * 10.0) / 10.0;
                                displayUnit = "gal";
                            } else {
                                displayQty = Math.round((baseMl / 29.5735) * 10.0) / 10.0;
                                displayUnit = "fl oz";
                            }
                        }
                    } else {
                        if (norm.equals("oz") || norm.equals("lb")) {
                            double baseGrams = UnitConvertLogic.toBaseUnit(displayQty, norm);
                            if (baseGrams >= 1000.0) {
                                displayQty = Math.round((baseGrams / 1000.0) * 10.0) / 10.0;
                                displayUnit = "kg";
                            } else {
                                displayQty = Math.round(baseGrams * 10.0) / 10.0;
                                displayUnit = "g";
                            }
                        } else if (norm.equals("fl oz") || norm.equals("gal")) {
                            double baseMl = UnitConvertLogic.toBaseUnit(displayQty, norm);
                            if (baseMl >= 1000.0) {
                                displayQty = Math.round((baseMl / 1000.0) * 10.0) / 10.0;
                                displayUnit = "L";
                            } else {
                                displayQty = Math.round(baseMl * 10.0) / 10.0;
                                displayUnit = "ml";
                            }
                        }
                    }

                    edtQtyVal.setText(UnitConvertLogic.formatQty(displayQty));

                    for (int i = 0; i < unitOptArr.length; i++) {
                        if (unitOptArr[i].equalsIgnoreCase(displayUnit)) {
                            spnUnitOpt.setSelection(i);
                            break;
                        }
                    }
                });
            }
        });
    }

    // validates input fields and handles saving to room database
    private void commitStockItem() {
        String rawNm = edtIngNm.getText().toString().trim();
        String rawQty = edtQtyVal.getText().toString().trim();

        // validates ingredient name is not blank
        if (TextUtils.isEmpty(rawNm)) {
            edtIngNm.setError(getString(R.string.validation_name_required));
            edtIngNm.requestFocus();
            return;
        }

        // validates quantity is entered
        if (TextUtils.isEmpty(rawQty)) {
            edtQtyVal.setError(getString(R.string.validation_quantity_required));
            edtQtyVal.requestFocus();
            return;
        }

        double parsedQty;
        try {
            parsedQty = Double.parseDouble(rawQty);
            // validates quantity is positive
            if (parsedQty <= 0) {
                edtQtyVal.setError(getString(R.string.validation_quantity_required));
                edtQtyVal.requestFocus();
                return;
            }
        } catch (NumberFormatException exc) {
            edtQtyVal.setError("Invalid number");
            edtQtyVal.requestFocus();
            return;
        }

        String selUnit = spnUnitOpt.getSelectedItem().toString();

        // handles saving or updating stock in room database on background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            if (targetRowId > 0) {
                StockEntity existingStk = PantryRoomDb.getDbInst(this).stockDataAcc().getStockById(targetRowId);
                if (existingStk != null) {
                    existingStk.setItmNm(rawNm);
                    existingStk.setQtyVal(parsedQty);
                    existingStk.setUntLbl(selUnit);
                    existingStk.setExpDateMs(expDateMs);
                    PantryRoomDb.getDbInst(this).stockDataAcc().updateStock(existingStk);
                }
            } else {
                StockEntity newStk = new StockEntity(rawNm, parsedQty, selUnit, expDateMs);
                PantryRoomDb.getDbInst(this).stockDataAcc().insertStock(newStk);
            }

            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
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
