package com.mobiato.sfa.rest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobiato.sfa.App;
import com.mobiato.sfa.utils.NetworkUtil;


public class BackgroundJobLower extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if (!status.equals(App.NO_CONNECTION)) {
            context.startService(new Intent(context, SyncData.class));
        }
    }
}
