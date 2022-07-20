package com.mobiato.sfa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivitySettingBinding;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    public ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Settings");
        setNavigationView();


        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {

            binding.viewTick.setVisibility(View.VISIBLE);
            binding.btnTiccket.setVisibility(View.VISIBLE);

            binding.viewChange.setVisibility(View.GONE);
            binding.btnChangePWD.setVisibility(View.GONE);
            binding.viewMat.setVisibility(View.GONE);
            binding.btnMaterialMater.setVisibility(View.GONE);
            binding.viewCus.setVisibility(View.GONE);
            binding.btnCusMater.setVisibility(View.GONE);
            binding.viewDis.setVisibility(View.GONE);
            binding.btnDiscount.setVisibility(View.GONE);
            binding.viewPromo.setVisibility(View.GONE);
            binding.btnPromotion.setVisibility(View.GONE);
            binding.viewCat.setVisibility(View.GONE);
            binding.btnCategory.setVisibility(View.GONE);
            binding.viewSubCa.setVisibility(View.GONE);
            binding.btnSubCategory.setVisibility(View.GONE);
            binding.viewChannel.setVisibility(View.GONE);
            binding.btnChannel.setVisibility(View.GONE);
            binding.viewAgent.setVisibility(View.GONE);
            binding.btnAgentPrice.setVisibility(View.GONE);

        }else  if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Merchandising")) {

            binding.viewDis.setVisibility(View.GONE);
            binding.btnDiscount.setVisibility(View.GONE);
            binding.viewPromo.setVisibility(View.GONE);
            binding.btnPromotion.setVisibility(View.GONE);
            binding.viewCat.setVisibility(View.GONE);
            binding.btnCategory.setVisibility(View.GONE);
            binding.viewSubCa.setVisibility(View.GONE);
            binding.btnSubCategory.setVisibility(View.GONE);
            binding.viewChannel.setVisibility(View.GONE);
            binding.btnChannel.setVisibility(View.GONE);
            binding.txtPrice.setText("Pricing Master");

        } else {
            binding.viewTick.setVisibility(View.GONE);
            binding.btnTiccket.setVisibility(View.GONE);
        }


        binding.btnChangePWD.setOnClickListener(this);
        binding.btnCusMater.setOnClickListener(this);
        binding.btnDiscount.setOnClickListener(this);
        binding.btnMaterialMater.setOnClickListener(this);
        binding.btnAgentPrice.setOnClickListener(this);
        binding.btnPromotion.setOnClickListener(this);
        binding.btnCategory.setOnClickListener(this);
        binding.btnChannel.setOnClickListener(this);
        binding.btnSubCategory.setOnClickListener(this);
        binding.btnTiccket.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChangePWD:
                startActivity(new Intent(SettingActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.btnCusMater:
                startActivity(new Intent(SettingActivity.this, MasterDataActivity.class)
                        .putExtra("Type", "Customer"));
                break;
            case R.id.btnMaterialMater:
                startActivity(new Intent(SettingActivity.this, MasterDataActivity.class)
                        .putExtra("Type", "Material"));
                break;
            case R.id.btnDiscount:
                startActivity(new Intent(SettingActivity.this, DiscountMasterActivity.class)
                        .putExtra("Type", "Discount"));
                break;
            case R.id.btnAgentPrice:
                startActivity(new Intent(SettingActivity.this, MasterDataActivity.class)
                        .putExtra("Type", "AgentPrice"));
                break;
            case R.id.btnPromotion:
                startActivity(new Intent(SettingActivity.this, MasterDataActivity.class)
                        .putExtra("Type", "Promotion"));
                break;
            case R.id.btnCategory:
                startActivity(new Intent(SettingActivity.this, MasterDataActivity.class)
                        .putExtra("Type", "Category"));
                break;
            case R.id.btnChannel:
                startActivity(new Intent(SettingActivity.this, MasterDataActivity.class)
                        .putExtra("Type", "Channel"));
                break;
            case R.id.btnSubCategory:
                startActivity(new Intent(SettingActivity.this, MasterDataActivity.class)
                        .putExtra("Type", "SubCategory"));
                break;
            case R.id.btnTiccket:
                startActivity(new Intent(SettingActivity.this, MasterDataActivity.class)
                        .putExtra("Type", "Ticket"));
                break;
        }
    }
}
