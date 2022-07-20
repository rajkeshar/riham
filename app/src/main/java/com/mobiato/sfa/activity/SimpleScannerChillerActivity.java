package com.mobiato.sfa.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.mobiato.sfa.model.Customer;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerChillerActivity extends Activity implements ZXingScannerView.ResultHandler {
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

        if(currentString.contains(";")) {
            try {
                String[] separated = currentString.split(";");
                String msg = separated[4];
                Intent intent1 = new Intent(SimpleScannerChillerActivity.this, AddChillerFormActivity.class);
                intent1.putExtra("customer", mCustomer);
                intent1.putExtra("code", msg);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
            }catch (Exception e){
                try{
                    String[] separated1 = currentString.split(";");
                    String msg = separated1[1];
                    Intent intent1 = new Intent(SimpleScannerChillerActivity.this, AddChillerFormActivity.class);
                    intent1.putExtra("customer", mCustomer);
                    intent1.putExtra("code", msg);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finish();
                }catch (Exception e1) {
                    Intent intent1 = new Intent(SimpleScannerChillerActivity.this, AddChillerFormActivity.class);
                    intent1.putExtra("customer", mCustomer);
                    intent1.putExtra("code", rawResult.getText().toString());
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finish();
                }
            }
        }else{
           // Toast.makeText(this,rawResult.getText().toString(),Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(SimpleScannerChillerActivity.this, AddChillerFormActivity.class);
            intent1.putExtra("customer", mCustomer);
            intent1.putExtra("code", currentString);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
            finish();
        }


        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
}
