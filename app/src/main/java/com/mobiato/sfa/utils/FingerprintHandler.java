package com.mobiato.sfa.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.mobiato.sfa.App;
import com.mobiato.sfa.Fragments.DashboardFragment;
import com.mobiato.sfa.activity.LoginActivity;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context context;


    public FingerprintHandler(Context mContext) {
        context = mContext;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId,
                                      CharSequence errString) {

       // UtilApp.displayAlert(context, "Authentication error\n" + errString);
    }

    @Override
    public void onAuthenticationFailed() {
        UtilApp.displayAlert(context, "Authentication failed");
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId,
                                     CharSequence helpString) {
      //  UtilApp.displayAlert(context, "Authentication help\n" + helpString);

    }


    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {

        new Handler().postDelayed(new Runnable() {
            //
            @Override
            public void run() {
                if (Settings.getString(App.ISLOGIN) != null && Settings.getString(App.ISLOGIN).equals("true")) {
                    Settings.setString(App.IS_DATA_SYNCING, "false");

                    Intent i = new Intent(context, DashboardFragment.class);
                    context.startActivity(i);
                } else {
                    Intent i = new Intent(context, LoginActivity.class);
                    context.startActivity(i);
                }
            }
        }, 3000);

    }


}
