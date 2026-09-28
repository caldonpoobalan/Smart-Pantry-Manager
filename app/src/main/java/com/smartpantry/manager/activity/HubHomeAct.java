package com.smartpantry.manager.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartpantry.manager.R;
import com.smartpantry.manager.database.PantryRoomDb;
import com.smartpantry.manager.fragment.MatchRecpFrag;
import com.smartpantry.manager.fragment.PrefCfgFrag;
import com.smartpantry.manager.fragment.StockVwFrag;
import com.smartpantry.manager.model.StockEntity;

import java.util.List;
import java.util.concurrent.Executors;

// main navigation host activity
public class HubHomeAct extends AppCompatActivity {

    // bottom navigation bar
    private BottomNavigationView btmNavBar;

    // top navigation toolbar
    private androidx.appcompat.widget.Toolbar barHubTopNav;

    // 5 days warning limit in milliseconds
    private static final long EXP_WARN_LIMIT_MS = 5L * 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // applies user dark theme preference on activity creation
        SharedPreferences spSharedPrefs = getSharedPreferences(PrefCfgFrag.PREF_STORAGE_TAG, MODE_PRIVATE);
        boolean isDarkTheme = spSharedPrefs.getBoolean(PrefCfgFrag.KEY_DARK_THEME, false);
        AppCompatDelegate.setDefaultNightMode(isDarkTheme ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_hub_home);

        // handles top toolbar setup
        barHubTopNav = findViewById(R.id.bar_hub_top_nav);
        setSupportActionBar(barHubTopNav);

        btmNavBar = findViewById(R.id.btm_nav_bar);

        // handles bottom navigation tab selection
        btmNavBar.setOnItemSelectedListener(item -> {
            Fragment targetFrag = null;
            int itmId = item.getItemId();

            if (itmId == R.id.nav_stock) {
                targetFrag = new StockVwFrag();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.title_pantry);
                }
            } else if (itmId == R.id.nav_recp) {
                targetFrag = new MatchRecpFrag();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.title_recipes);
                }
            } else if (itmId == R.id.nav_pref) {
                targetFrag = new PrefCfgFrag();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.title_settings);
                }
            }

            if (targetFrag != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frm_host_slot, targetFrag)
                        .commit();
                return true;
            }
            return false;
        });

        // handles setting default view to pantry stock on first launch
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frm_host_slot, new StockVwFrag())
                    .commit();
            btmNavBar.setSelectedItemId(R.id.nav_stock);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.title_pantry);
            }

            // checks if expiry alerts are enabled and displays reminder dialog
            boolean alertsEnabled = spSharedPrefs.getBoolean(PrefCfgFrag.KEY_ALERT_TOGGLE, true);
            if (alertsEnabled) {
                checkExpiringItemsAlert();
            }
        }
    }

    // checks for expired or expiring ingredients in the background and shows reminder
    private void checkExpiringItemsAlert() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<StockEntity> stockList = PantryRoomDb.getDbInst(this).stockDataAcc().getAllStock();
            if (stockList == null || stockList.isEmpty()) {
                return;
            }

            long nowMs = System.currentTimeMillis();
            int expiredCount = 0;
            int expiringSoonCount = 0;

            for (StockEntity item : stockList) {
                if (item.getExpDateMs() != null) {
                    long diffMs = item.getExpDateMs() - nowMs;
                    if (diffMs < 0) {
                        expiredCount++;
                    } else if (diffMs <= EXP_WARN_LIMIT_MS) {
                        expiringSoonCount++;
                    }
                }
            }

            if (expiredCount > 0 || expiringSoonCount > 0) {
                int finalExpiredCount = expiredCount;
                int finalExpiringSoonCount = expiringSoonCount;

                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        showExpiryPopup(finalExpiringSoonCount, finalExpiredCount);
                    }
                });
            }
        });
    }

    // displays expiry reminder dialog popup
    private void showExpiryPopup(int expiringCount, int expiredCount) {
        StringBuilder msgBuilder = new StringBuilder();

        if (expiringCount > 0) {
            msgBuilder.append(getString(R.string.expiry_alert_expiring));
        }

        if (expiredCount > 0) {
            if (msgBuilder.length() > 0) {
                msgBuilder.append("\n\n");
            }
            msgBuilder.append(String.format(getString(R.string.expiry_alert_expired), expiredCount));
        }

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.img_expiry_reminder)
                .setTitle(R.string.expiry_alert_title)
                .setMessage(msgBuilder.toString())
                .setPositiveButton(R.string.dialog_ok, null)
                .show();
    }
}
