package com.smartpantry.manager.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartpantry.manager.R;

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
            int itmId = item.getItemId();

            if (itmId == R.id.nav_stock) {
                // will attach pantry stock fragment here later
                return true;
            } else if (itmId == R.id.nav_recp) {
                // will attach recipe suggestions fragment here later
                return true;
            } else if (itmId == R.id.nav_pref) {
                // will attach settings fragment here later
                return true;
            }
            return false;
        });
    }
}
