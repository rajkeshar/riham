package com.mobiato.sfa.rest;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BackgroundJob extends JobService {
    private static final String TAG = "BackgroundJob";
    Context context;

    public BackgroundJob(Context context) {
        this.context = context;
    }

    public BackgroundJob() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(new Intent(getApplicationContext(), SyncData.class));
        } else {
            startService(new Intent(getApplicationContext(), SyncData.class));
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("Stop Job", "Stop Job");
        return true;
    }
}
