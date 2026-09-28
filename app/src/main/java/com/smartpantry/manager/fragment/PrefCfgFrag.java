package com.smartpantry.manager.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.smartpantry.manager.R;
import com.smartpantry.manager.database.PantryRoomDb;

import java.util.concurrent.Executors;

// fragment handling user preferences and database settings
public class PrefCfgFrag extends Fragment {

    private SwitchCompat swtExpAlert;
    private Spinner spnUnitPref;
    private SwitchCompat swtDarkTheme;
    private MaterialButton btnResetPantry;
    private TextView txtAppInfo;

    public static final String PREF_STORAGE_TAG = "smart_pantry_prefs";
    public static final String KEY_ALERT_TOGGLE = "expiry_alerts";
    public static final String KEY_UNIT_SYS = "unit_pref";
    public static final String KEY_DARK_THEME = "dark_theme";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View cfgVw = inflater.inflate(R.layout.frag_pref_cfg, container, false);

        swtExpAlert = cfgVw.findViewById(R.id.swt_exp_alert);
        spnUnitPref = cfgVw.findViewById(R.id.spn_unit_pref);
        swtDarkTheme = cfgVw.findViewById(R.id.swt_dark_theme);
        btnResetPantry = cfgVw.findViewById(R.id.btn_reset_pantry);
        txtAppInfo = cfgVw.findViewById(R.id.txt_app_info);

        SharedPreferences spSharedPrefs = requireActivity().getSharedPreferences(PREF_STORAGE_TAG, Context.MODE_PRIVATE);

        // handles setting initial state for expiry alert toggle
        swtExpAlert.setChecked(spSharedPrefs.getBoolean(KEY_ALERT_TOGGLE, true));
        swtExpAlert.setOnCheckedChangeListener((btn, isChecked) -> {
            spSharedPrefs.edit().putBoolean(KEY_ALERT_TOGGLE, isChecked).apply();
        });

        // handles setting up measurement unit options
        String[] unitDisplayList = {"Metric (SA)", "Imperial (US)"};
        ArrayAdapter<String> adaptUnit = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, unitDisplayList);
        adaptUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUnitPref.setAdapter(adaptUnit);

        String savedUnit = spSharedPrefs.getString(KEY_UNIT_SYS, "Metric");
        if ("Imperial".equalsIgnoreCase(savedUnit)) {
            spnUnitPref.setSelection(1);
        } else {
            spnUnitPref.setSelection(0);
        }

        spnUnitPref.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosenSystem = position == 1 ? "Imperial" : "Metric";
                spSharedPrefs.edit().putString(KEY_UNIT_SYS, chosenSystem).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // handles dark theme toggle and applies night mode
        swtDarkTheme.setChecked(spSharedPrefs.getBoolean(KEY_DARK_THEME, false));
        swtDarkTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spSharedPrefs.edit().putBoolean(KEY_DARK_THEME, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        // handles reset pantry button click with confirmation dialog
        btnResetPantry.setOnClickListener(v -> showResetConfirmationDialog());

        // displays app version and student mission statement
        txtAppInfo.setText(getString(R.string.about_app_name) + "\n\n" + getString(R.string.about_app_desc));

        return cfgVw;
    }

    // displays confirmation dialog before wiping pantry stock
    private void showResetConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.img_reset_pantry)
                .setTitle(R.string.reset_pantry_title)
                .setMessage(R.string.reset_pantry_msg)
                .setPositiveButton(R.string.reset_pantry, (dialog, which) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        PantryRoomDb.getDbInst(requireContext()).stockDataAcc().clearAllStock();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), R.string.reset_pantry_cleared, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
