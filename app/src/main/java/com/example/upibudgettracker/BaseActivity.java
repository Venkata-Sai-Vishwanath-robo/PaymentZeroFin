package com.example.upibudgettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;

    // Each activity will return its nav item ID
    protected abstract int getNavBarItemId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base); // contains BottomNavigationView

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Highlight current item
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(getNavBarItemId());
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == getNavBarItemId()) return true; // already active

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_qr) {
                startActivity(new Intent(this, InfoActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_ghar) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });
    }
}
