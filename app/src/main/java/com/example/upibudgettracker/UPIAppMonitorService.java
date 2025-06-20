package com.example.upibudgettracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

public class UPIAppMonitorService extends Service {
    
    //TO DO – Add banking app’s package names.
    private final String[] upiApps = {
            "com.google.android.apps.nbu.paisa.user",
            "net.one97.paytm",
            "com.phonepe.app",
            "indwin.c3.shareapp",
            "com.naviapp",
            "com.popclub.android",
            "money.super.payments",
            "com.fampay.in",
            "com.dreamplug.androidapp",
            "com.freecharge.android"
    };

    private Handler handler = new Handler();
    private Runnable monitor = new Runnable() {
        @Override
        public void run() {
            UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            long now = System.currentTimeMillis();
            UsageEvents events = usm.queryEvents(now - 5000, now);
            UsageEvents.Event e = new UsageEvents.Event();

            while (events.hasNextEvent()) {
                events.getNextEvent(e);
                for (String pkg : upiApps) {
                    if (e.getPackageName().equals(pkg) && e.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        Intent i = new Intent(getApplicationContext(), OverlayViewService.class);
                        startService(i);
                        break;
                    }
                }
            }
            handler.postDelayed(this, 5000);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    "upi_monitor", "UPI Monitor", NotificationManager.IMPORTANCE_LOW);
        }
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel);
        }

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, "upi_monitor")
                    .setContentTitle("UPI Budget Tracker")
                    .setContentText("Monitoring UPI app usage")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();
        }

        startForeground(1, notification);

        handler.post(monitor);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

