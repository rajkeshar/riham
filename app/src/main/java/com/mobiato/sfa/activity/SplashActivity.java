package com.mobiato.sfa.activity;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.Fragments.DashboardFragment;
import com.mobiato.sfa.R;
import com.mobiato.sfa.Techinician.fragment.TechnicianDashboardFragment;
import com.mobiato.sfa.databinding.ActivitySplashBinding;
import com.mobiato.sfa.databinding.FingerDialogLayoutBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.merchandising.MerchantDashboardActivity;
import com.mobiato.sfa.utils.CheckPermissionUtils;
import com.mobiato.sfa.utils.FingerprintHandler;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding;

    private static final String TAG = SplashActivity.class.getName();
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3400;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.USE_FINGERPRINT};


    private static final String KEY_NAME = "yourKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setFullScreen();

        //Crashlytics.getInstance().crash();

//        keyguardManager =
//                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//        fingerprintManager =
//                (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        //Settings.setString(App.IS_ATTENDANCE_IN, "1");
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Settings.setString(App.TOKEN, token);
                        // Log and toast
                        Log.d(TAG, "Token: " + token);
                    }
                });

//        //Clear all Preference
//        Settings.clearPreferenceStore();
//
//        //Delete Database
//        deleteDatabase(DBManager.DB_NAME);

        //Settings.setString(App.IS_LOAD_VERIFY, "1");
        //Settings.setString(App.ISLOGIN, "false");
        if (!CheckPermissionUtils.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {

            new Handler().postDelayed(new Runnable() {
                //
                @Override
                public void run() {
                    if (Settings.getString(App.ISLOGIN) != null && Settings.getString(App.ISLOGIN).equals("true")) {
                        Settings.setString(App.IS_DATA_SYNCING, "false");

                        if (Settings.getString(App.ISLOGIN).equals("true")) {
                            if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
                                Intent i = new Intent(SplashActivity.this, TechnicianDashboardFragment.class);
                                startActivity(i);
                            } else if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                                Intent i = new Intent(SplashActivity.this, DashboardFragment.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(SplashActivity.this, MerchantDashboardActivity.class);
                                startActivity(i);
                            }
                        } else {
                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i);
                        }


                    } else {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                    }

                    finish();
                }
            }, 3000);

            //checkScanner();

        }


    }

    public void checkScanner() {

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            UtilApp.displayAlert(SplashActivity.this, "No fingerprint configured. Please register at least one fingerprint in your device's Settings");
            return;
        }

        if (!keyguardManager.isKeyguardSecure()) {
            UtilApp.displayAlert(SplashActivity.this, "Please enable lockscreen security in your device's Settings");
            return;
        } else {
            try {
                generateKey();
            } catch (FingerprintException e) {
                e.printStackTrace();
            }
            if (initCipher()) {

                cryptoObject = new FingerprintManager.CryptoObject(cipher);
                FingerprintHandler helper = new FingerprintHandler(this);
                helper.startAuth(fingerprintManager, cryptoObject);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //show finger Dialog
                        showFingerDialog();
                    }
                }, 1000);


            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //checkScanner();

                    new Handler().postDelayed(new Runnable() {
                        //
                        @Override
                        public void run() {
                            if (Settings.getString(App.ISLOGIN) != null && Settings.getString(App.ISLOGIN).equals("true")) {
                                Settings.setString(App.IS_DATA_SYNCING, "false");

                                Intent i = new Intent(SplashActivity.this, DashboardFragment.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(i);
                            }

                            finish();
                        }
                    }, 3000);
                } else {
                    Log.e("value", "Permission Denied, You cannot use application.");
                }
                break;
        }
    }

    private void generateKey() throws FingerprintException {
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");


            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | IOException
                | java.security.cert.CertificateException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }


    }


    public boolean initCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateEncodingException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }


    private class FingerprintException extends Exception {

        public FingerprintException(Exception e) {
            super(e);
        }
    }

    public void showFingerDialog() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(SplashActivity.this);

        View sheetView = SplashActivity.this.getLayoutInflater().inflate(R.layout.finger_dialog_layout, null);

        final FingerDialogLayoutBinding binding = FingerDialogLayoutBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.show();
    }
}
