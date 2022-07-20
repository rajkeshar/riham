package com.mobiato.sfa.activity.forms.ServiesForms;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormThreeActivity;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormTwoActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormTwoActivity;
import com.mobiato.sfa.databinding.ActivityChillerServiceFormBinding;
import com.mobiato.sfa.databinding.ActivityChillerServiceFormTwoBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;

public class ChillerServiceFormTwoActivity extends BaseActivity implements View.OnClickListener {


    public ActivityChillerServiceFormTwoBinding binding; //Change this

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
        binding = ActivityChillerServiceFormTwoBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Chiller Services Form");

        baseBinding.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChillerServiceFormTwoActivity.this, ChillerServiceFormActivity.class));
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
            if (binding.etOutletName.getText().toString().equals("")) {
                Toast.makeText(ChillerServiceFormTwoActivity.this, "Please enter present outlet name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().matches("")) {
                Toast.makeText(ChillerServiceFormTwoActivity.this, "Please enter contact number", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().length() < 10) {
                Toast.makeText(ChillerServiceFormTwoActivity.this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            } else if (binding.etLocation.getText().toString().matches("")) {
                Toast.makeText(ChillerServiceFormTwoActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
            } else if (binding.etLandmark.getText().toString().matches("")) {
                Toast.makeText(ChillerServiceFormTwoActivity.this, "Please enter landmark", Toast.LENGTH_SHORT).show();
            } else {
                returnvalue = true;
            }

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
                startActivity(new Intent(ChillerServiceFormTwoActivity.this, ChillerServiceFormActivity.class));
                finish();
                break;
            case R.id.btnTransferNext:
                //  if (checkNullValues()) {
                /*   // Toast.makeText(ChillerTransferFormActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
                    //addCustomer();
                    startActivity(new Intent(ChillerTransferFormActivity.this, ChillerTransferFormTwoActivity.class));

                }*/
                startActivity(new Intent(ChillerServiceFormTwoActivity.this, ChillerServiceFormThreeActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    private void addCustomer() {
        Customer mCustomer = new Customer();
        mCustomer.setCustomerId(custNum);
        mCustomer.setCustomerCode(custNum);
        mCustomer.setCustomerName(binding.etOutletName.getText().toString());
        mCustomer.setCustomerName2("");
        //  mCustomer.setAddress(binding.etCustomerName.getText().toString());
        mCustomer.setCustDivison("");
        mCustomer.setCustChannel("");
        mCustomer.setCustOrg("");
        mCustomer.setCustEmail("");
        mCustomer.setCustPhone(binding.etCustomerTelephone.getText().toString());
        mCustomer.setFridge(strFridge);

        mCustomer.setRadius("20");
        mCustomer.setCustRegion(mSalesman.getRegion());
        mCustomer.setCustSubRegion(mSalesman.getSubRegion());
        mCustomer.setCustType("1");
        mCustomer.setPaymentTerm("0");
        mCustomer.setBarcode("");
        mCustomer.setBalance("0");
        mCustomer.setCreditLimit("0");
        mCustomer.setRouteId(Settings.getString(App.ROUTEID));
        mCustomer.setCustomerType(strCustTypeId);
        mCustomer.setCustomerCategory(strCustCategoryId);

        db.addCustomer(mCustomer);

        //store Last Invoice Number
        Settings.setString(App.CUSTOMER_LAST, custNum);

//        //Add Customer in Recent
//        RecentCustomer recentCustomer = new RecentCustomer();
//        recentCustomer.setCustomer_id(mCustomer.getCustomerId());
//        recentCustomer.setCustomer_name(mCustomer.getCustomerName());
//        recentCustomer.setDate_time(UtilApp.getCurrentDate());
//        db.insertRecentCustomer(recentCustomer);

        //INSERT TRANSACTION
        Transaction transaction = new Transaction();
        transaction.tr_type = Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_CREATED;
        transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
        transaction.tr_customer_num = mCustomer.getCustomerId();
        transaction.tr_customer_name = mCustomer.getCustomerName();
        transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
        transaction.tr_invoice_id = "";
        transaction.tr_order_id = custNum;
        transaction.tr_collection_id = "";
        transaction.tr_pyament_id = "";
        transaction.tr_is_posted = "No";
        transaction.tr_printData = "";
        db.insertTransaction(transaction);

        if (UtilApp.isNetworkAvailable(getApplicationContext())) {
            UtilApp.createBackgroundJob(getApplicationContext());
        }

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        startActivity(new Intent(ChillerServiceFormTwoActivity.this, FragmentJourneyPlan.class));
        //Toast.makeText(getApplicationContext(), "Customer added successfully!", Toast.LENGTH_SHORT).show();
        finish();

    }

}
