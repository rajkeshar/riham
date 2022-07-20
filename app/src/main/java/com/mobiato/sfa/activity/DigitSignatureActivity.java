package com.mobiato.sfa.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.mobiato.sfa.views.SignatureMainLayout;

public class DigitSignatureActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new SignatureMainLayout(this, DigitSignatureActivity.this));
    }
}
