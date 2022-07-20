package com.mobiato.sfa.activity.forms.ServiesForms;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormTwoActivity;
import com.mobiato.sfa.databinding.ActivityChillerServiceFormFourBinding;
import com.mobiato.sfa.databinding.ActivityChillerServiceFormThreeBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Salesman;

import java.util.ArrayList;

public class ChillerServiceFormFourActivity extends BaseActivity implements View.OnClickListener {


    public ActivityChillerServiceFormFourBinding binding; //Change this

    public String[] arrOutletType = {"Retail", "Whsale", "Eatery", "Bar", "Supermarket", "Other"};
    private String strFridge = "", custNum = "", strType = "", strCustTypeId = "", strCustCategoryId = "", strCType = "";
    private Salesman mSalesman;
    private DBManager db;
    private ArrayList<String> arrFridge = new ArrayList<>();
    private AlertDialog.Builder builder;
    private String[] arrCusType, arrCusTypeId, arrCusCategory, arrCusCatId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChillerServiceFormFourBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Chiller Services Form");
        baseBinding.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChillerServiceFormFourActivity.this, ChillerServiceFormThreeActivity.class));
                finish();
            }
        });
        //mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
        binding.btnTransferNext.setOnClickListener(this);
        binding.btnTransferPre.setOnClickListener(this);


    }

    public SpannableStringBuilder getBuilder(String actualText) {
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(actualText);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }


    private boolean checkNullValues() {
        boolean returnvalue = false;
        try {
           /* if (binding.etOutletName.getText().toString().equals("")) {
                Toast.makeText(ChillerServiceFormThreeActivity.this, "Please enter present outlet name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().matches("")) {
                Toast.makeText(ChillerServiceFormThreeActivity.this, "Please enter contact number", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().length() < 10) {
                Toast.makeText(ChillerServiceFormThreeActivity.this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            } else if (binding.etLocation.getText().toString().matches("")) {
                Toast.makeText(ChillerServiceFormThreeActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
            } else if (binding.etLandmark.getText().toString().matches("")) {
                Toast.makeText(ChillerServiceFormThreeActivity.this, "Please enter landmark", Toast.LENGTH_SHORT).show();
            }
            else {
                returnvalue = true;
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnvalue;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTransferPre:
                //  if (checkNullValues()) {
                /*   // Toast.makeText(ChillerTransferFormActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
                    //addCustomer();
                    startActivity(new Intent(ChillerTransferFormActivity.this, ChillerTransferFormTwoActivity.class));

                }*/
                startActivity(new Intent(ChillerServiceFormFourActivity.this, ChillerServiceFormThreeActivity.class));
                finish();
                break;
            case R.id.btnTransferNext:
                //  if (checkNullValues()) {
                /*   // Toast.makeText(ChillerTransferFormActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
                    //addCustomer();
                    startActivity(new Intent(ChillerTransferFormActivity.this, ChillerTransferFormTwoActivity.class));

                }*/
                startActivity(new Intent(ChillerServiceFormFourActivity.this, ChillerServiceFormFiveActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

}
