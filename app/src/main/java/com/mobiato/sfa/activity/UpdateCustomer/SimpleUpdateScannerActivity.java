package com.mobiato.sfa.activity.UpdateCustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.mobiato.sfa.App;
import com.mobiato.sfa.activity.CameraFormActivity;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.utils.Settings;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleUpdateScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private Customer mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mCustomer = (Customer) getIntent().getSerializableExtra("customer");// Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("Barcode", rawResult.getText());
        String currentString = rawResult.getText();

        try {
            String[] separated = currentString.split(";");
            String msg = separated[4];
            Settings.setString(App.SCANSERIALNUMBER, msg);
            onBackPressed();
            finish();
        }catch (Exception e){
            Toast.makeText(SimpleUpdateScannerActivity.this,"QR code/Barcode formate not supported.",Toast.LENGTH_LONG).show();
            Settings.setString(App.SCANSERIALNUMBER, "");
            onBackPressed();
            finish();
        }

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
}
