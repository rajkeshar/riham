package com.mobiato.sfa.activity.forms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.MasterDataActivity;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormActivity;
import com.mobiato.sfa.activity.forms.ServiesForms.ChillerServiceFormActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormActivity;
import com.mobiato.sfa.databinding.ActivityFormsBinding;

public class FormsActivity extends BaseActivity implements View.OnClickListener {

    public ActivityFormsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormsBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Forms");
        setNavigationView();


        binding.btnTransfer.setOnClickListener(this);
        binding.btnService.setOnClickListener(this);
        binding.btnRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTransfer:
                startActivity(new Intent(FormsActivity.this, ChillerTransferFormActivity.class));
                break;
            case R.id.btnService:
                startActivity(new Intent(FormsActivity.this, ChillerServiceFormActivity.class));
                break;
            case R.id.btnRequest:
                startActivity(new Intent(FormsActivity.this, ChillerRequestFormActivity.class));
                break;
        }
    }
}
