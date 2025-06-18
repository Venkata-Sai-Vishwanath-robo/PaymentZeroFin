package com.example.upibudgettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.Manifest;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity {
    TextView statusSms, statusOverlay, statusUsage, statusBattery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settings, findViewById(R.id.contentContainer), true);

        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        window.setStatusBarColor(Color.TRANSPARENT);

        statusSms = findViewById(R.id.status_sms);
        statusOverlay = findViewById(R.id.status_overlay);
        statusUsage = findViewById(R.id.status_usage);
        statusBattery = findViewById(R.id.status_battery);


        findViewById(R.id.button2).setOnClickListener(v -> {
            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.setData(Uri.parse("package:" + getPackageName()));
            startActivity(i);
        });

        findViewById(R.id.button3).setOnClickListener(v -> {
            Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(i);
        });

        findViewById(R.id.button4).setOnClickListener(v -> {
            Intent i = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(i);
        });

        findViewById(R.id.button5).setOnClickListener(v -> {
            Intent i = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            startActivity(i);
        });

    }

    @Override
    protected int getNavBarItemId() {
        return R.id.nav_home;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    private void updateStatus() {
        // SMS
        boolean smsGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
        statusSms.setText("SMS: " + (smsGranted ? "Granted ✅" : "Not granted ❌"));

        // Overlay
        boolean overlayGranted = Settings.canDrawOverlays(this);
        statusOverlay.setText("Overlay: " + (overlayGranted ? "Granted ✅" : "Not granted ❌"));

        // Usage
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        boolean usageGranted = (mode == AppOpsManager.MODE_ALLOWED);
        statusUsage.setText("Usage Access: " + (usageGranted ? "Granted ✅" : "Not granted ❌"));

        // Battery Optimization
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean batteryIgnored = pm.isIgnoringBatteryOptimizations(getPackageName());
        statusBattery.setText("Battery Optimization: " +
                (batteryIgnored ? "Not Optimized ✅" : "Restricted ❌"));
    }
}