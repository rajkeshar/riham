package com.mobiato.sfa.activity.forms.ServiesForms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormTwoActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormTwoActivity;
import com.mobiato.sfa.databinding.ActivityChillerRequestFormBinding;
import com.mobiato.sfa.databinding.ActivityChillerRequestFormFourBinding;
import com.mobiato.sfa.databinding.ActivityChillerServiceFormBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.Calendar;

public class ChillerServiceFormActivity extends BaseActivity implements View.OnClickListener {


    public ActivityChillerServiceFormBinding binding; //Change this

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
        binding = ActivityChillerServiceFormBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Chiller Services Form");

        //mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
        binding.btnTransferNext.setOnClickListener(this);
        binding.edtEffectiveDate.setOnClickListener(this);
        binding.edtRefDate.setOnClickListener(this);

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
            if (binding.edtRefDate.getText().toString().equals("")) {
                Toast.makeText(ChillerServiceFormActivity.this, "Please enter present outlet name", Toast.LENGTH_SHORT).show();
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
            case R.id.edtEffectiveDate:
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.edtRefDate:
                showDialog(DATE_DIALOG_ID1);
                break;
            case R.id.btnTransferNext:
                //  if (checkNullValues()) {
                /*   // Toast.makeText(ChillerTransferFormActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
                    //addCustomer();
                    startActivity(new Intent(ChillerTransferFormActivity.this, ChillerTransferFormTwoActivity.class));

                }*/
                startActivity(new Intent(ChillerServiceFormActivity.this, ChillerServiceFormTwoActivity.class));
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
        // mCustomer.setCustomerName(binding.etOutletName.getText().toString());
        mCustomer.setCustomerName2("");
        // mCustomer.setAddress(binding.etCustomerName.getText().toString());
        mCustomer.setCustDivison("");
        mCustomer.setCustChannel("");
        mCustomer.setCustOrg("");
        mCustomer.setCustEmail("");
        // mCustomer.setCustPhone(binding.etCustomerTelephone.getText().toString());
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
        startActivity(new Intent(ChillerServiceFormActivity.this, FragmentJourneyPlan.class));
        //Toast.makeText(getApplicationContext(), "Customer added successfully!", Toast.LENGTH_SHORT).show();
        finish();

    }

    //Date
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;

    //return date picker dialog
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
            case DATE_DIALOG_ID1:
                Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(this, mDateSetListener1, mYear, mMonth, mDay);
        }
        return null;
    }

    // the call back received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    // the call back received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener1 =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay1();
                }
            };


    //update month day year
    private void updateDisplay() {
        binding.edtEffectiveDate.setText(//this is the edit text where you want to show the selected date
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(""));
    }

    private void updateDisplay1() {
        binding.edtRefDate.setText(//this is the edit text where you want to show the selected date
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(""));

    }

}
