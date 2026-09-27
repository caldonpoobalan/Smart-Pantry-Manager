package com.smartpantry.manager.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartpantry.manager.R;
import com.smartpantry.manager.fragment.StockVwFrag;

// main navigation host activity
public class HubHomeAct extends AppCompatActivity {

    // bottom navigation bar
    private BottomNavigationView btmNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_hub_home);

        btmNavBar = findViewById(R.id.btm_nav_bar);

        // handle bottom navigation tab selection
        btmNavBar.setOnItemSelectedListener(item -> {
            Fragment targetFrag = null;
            int itmId = item.getItemId();

            if (itmId == R.id.nav_stock) {
                targetFrag = new StockVwFrag();
            } else if (itmId == R.id.nav_recp) {
                // will attach recipe suggestions fragment later
            } else if (itmId == R.id.nav_pref) {
                // will attach settings fragment later
            }

            if (targetFrag != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frm_host_slot, targetFrag)
                        .commit();
                return true;
            }
            return false;
        });

        // show pantry stock fragment by default on first launch
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frm_host_slot, new StockVwFrag())
                    .commit();
            btmNavBar.setSelectedItemId(R.id.nav_stock);
        }
    }
}
