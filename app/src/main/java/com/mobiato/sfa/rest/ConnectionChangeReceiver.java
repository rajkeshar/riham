package com.mobiato.sfa.rest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.mobiato.sfa.utils.NetworkUtil;
import com.mobiato.sfa.utils.UtilApp;


public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        Toast.makeText(context,status,Toast.LENGTH_LONG).show();
        if(!status.equals(UtilApp.NO_CONNECTION)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, SyncData.class));
            } else {
                context.startService(new Intent(context, SyncData.class));
            }
        }
    }
}
