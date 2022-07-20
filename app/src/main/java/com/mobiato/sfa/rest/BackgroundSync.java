package com.mobiato.sfa.rest;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mobiato.sfa.App;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.List;
import java.util.Timer;


public class BackgroundSync extends Service {

    Context ctx;
    private static Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        ctx = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String CHANNEL_ID = "com.mobiato.sfa";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Riham", NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("App is running in background").build();
            startForeground(App.COUNT, notification);
            App.COUNT++;

        }

        startService();
    }

    private void startService() {
        Log.e("app", "runstart");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("app", "runcall");
                callService();
            }
        }, 20000);
    }

    private void callService() {
        Log.e("app", "run");
        if (isAppRunning()) {
            startService();
            Log.e("app", "run1");
            if (UtilApp.isNetworkAvailable(ctx)) {
                if (Settings.getString(App.IS_DATA_SYNCING) != null) {
                    if (!Boolean.parseBoolean(Settings.getString(App.IS_DATA_SYNCING))) {
                        Log.e("app", "run2");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            ctx.startForegroundService(new Intent(ctx, SyncData.class));
                        } else {
                            ctx.startService(new Intent(ctx, SyncData.class));
                        }
                    }
                }

            }
        } else {
            Log.e("app", "run3");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopForeground(true);
            }
            stopSelf();

        }
    }

    public boolean isAppRunning() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
