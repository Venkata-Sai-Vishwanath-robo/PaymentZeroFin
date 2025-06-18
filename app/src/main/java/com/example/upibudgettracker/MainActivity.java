package com.example.upibudgettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    EditText targetInput;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, findViewById(R.id.contentContainer), true);

        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        window.setStatusBarColor(Color.TRANSPARENT);


        targetInput = findViewById(R.id.monthlyTarget);
        prefs = getSharedPreferences("BudgetPrefs", MODE_PRIVATE);
        float savedTarget = prefs.getFloat("monthlyTarget", 0);
        targetInput.setText(String.valueOf(savedTarget));

        checkOverlayPermission();
        checkUsageStatsPermission();
        checkSMSPermission();
        checkBatteryPermission();

        startService(new Intent(this, UPIAppMonitorService.class));

    }

    public void saveTarget(View view) {
        String targetStr = targetInput.getText().toString().trim();
        if (!targetStr.isEmpty()) {
            prefs.edit().putFloat("monthlyTarget", Float.parseFloat(targetStr)).apply();
            Toast.makeText(this, "Budget target saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())));
        }
    }

    private void checkBatteryPermission() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        String packageName = getPackageName();

        if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
            @SuppressLint("BatteryLife") Intent i = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            i.setData(Uri.parse("package:" + packageName));
            startActivity(i);
        }
    }

    private void checkUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode != AppOpsManager.MODE_ALLOWED) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    private void checkSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    100);
        }
    }

    @Override
    protected int getNavBarItemId() {
        return R.id.nav_ghar;
    }
}