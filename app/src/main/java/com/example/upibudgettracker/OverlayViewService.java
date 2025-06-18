package com.example.upibudgettracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.*;
import android.widget.TextView;
import com.example.upibudgettracker.SMSParser;

public class OverlayViewService extends Service {
    private WindowManager windowManager;
    private View overlayView;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        float spent = SMSParser.getMonthlySpend(this);
        float target = getSharedPreferences("BudgetPrefs", MODE_PRIVATE)
                .getFloat("monthlyTarget", 0);

        showOverlay("Spent: ₹" + spent + " / ₹" + target);
        return START_NOT_STICKY;
    }

    private void showOverlay(String text) {
        if (overlayView != null) {
            windowManager.removeView(overlayView);
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_view, null);
        TextView overlayText = overlayView.findViewById(R.id.overlayText);
        overlayText.setText(text);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        windowManager.addView(overlayView, params);

        new Handler().postDelayed(() -> {
            try {
                if (overlayView != null) {
                    windowManager.removeView(overlayView);
                    overlayView = null;
                }
            } catch (Exception ignored) {}
        }, 4000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}