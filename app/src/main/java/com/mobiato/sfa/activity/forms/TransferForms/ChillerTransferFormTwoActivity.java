package com.mobiato.sfa.activity.forms.TransferForms;

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
import com.mobiato.sfa.activity.AddNewCustomerActivity;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.databinding.ActivityChillerTransferFormBinding;
import com.mobiato.sfa.databinding.ActivityChillerTransferFormTwoBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.Calendar;

public class ChillerTransferFormTwoActivity extends BaseActivity implements View.OnClickListener {

    public ActivityChillerTransferFormTwoBinding binding;
    public String[] arrOutletType = {"Retail", "Whsale", "Eatery", "Bar", "Supermarket", "Other"};
    public String[] arrCoolerType = {"CC", "PC", "HI", "Own", "Others"};
    public String[] arrDisplayLocation = {"Inside", "Outside"};
    public String[] arrPlacementRequest = {"Agent", "Sales Executive", "Area Manager"};
    private String strFridge = "", custNum = "", strType = "", strCustTypeId = "", strCustCategoryId = "", strCType = "";
    private Salesman mSalesman;
    private DBManager db;
    private ArrayList<String> arrFridge = new ArrayList<>();
    private AlertDialog.Builder builder;
    private String[] arrCusType, arrCusTypeId, arrCusCategory, arrCusCatId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChillerTransferFormTwoBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Chiller Transfer Form");

        baseBinding.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChillerTransferFormTwoActivity.this, ChillerTransferFormActivity.class));
                finish();
            }
        });

        //mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
        binding.btnTransferNext.setOnClickListener(this);
        binding.etOutletType.setOnClickListener(this);
        binding.etCooler.setOnClickListener(this);
        binding.etDisplayLocation.setOnClickListener(this);
        binding.etPlacementRequest.setOnClickListener(this);
        binding.btnTransferPre.setOnClickListener(this);
        binding.etAgentDate.setOnClickListener(this);
        binding.etAgentDate.setInputType(0);
        binding.etAgentDate.setFocusable(false);

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
            if (binding.etNewOutletName.getText().toString().equals("")) {
                Toast.makeText(ChillerTransferFormTwoActivity.this, "Please enter present outlet name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerNewName.getText().toString().matches("")) {
                Toast.makeText(ChillerTransferFormTwoActivity.this, "Please enter customer name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().matches("")) {
                Toast.makeText(ChillerTransferFormTwoActivity.this, "Please enter contact number", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().length() < 10) {
                Toast.makeText(ChillerTransferFormTwoActivity.this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            } else if (binding.etNewLandmark.getText().toString().matches("")) {
                Toast.makeText(ChillerTransferFormTwoActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
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
            case R.id.et_agent_date:
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.btnTransferPre:
                startActivity(new Intent(ChillerTransferFormTwoActivity.this, ChillerTransferFormActivity.class));
                finish();
                break;
            case R.id.btnTransferNext:
                /*if (checkNullValues()) {
                    // Toast.makeText(ChillerTransferFormActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
                    //addCustomer();
                }*/
                startActivity(new Intent(ChillerTransferFormTwoActivity.this, ChillerTransferFormThreeActivity.class));
                finish();
                break;
            case R.id.et_outletType:
                // setup the alert builder
                builder = new AlertDialog.Builder(ChillerTransferFormTwoActivity.this);
                builder.setTitle("Choose an Outlet");
                builder.setItems(arrOutletType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etOutletType.setText(arrOutletType[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.et_cooler:
                // setup the alert builder
                builder = new AlertDialog.Builder(ChillerTransferFormTwoActivity.this);
                builder.setTitle("Choose an cooler type");
                builder.setItems(arrCoolerType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etCooler.setText(arrCoolerType[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog1 = builder.create();
                dialog1.show();
                break;
            case R.id.et_display_location:
                // setup the alert builder
                builder = new AlertDialog.Builder(ChillerTransferFormTwoActivity.this);
                builder.setTitle("Choose display location");
                builder.setItems(arrDisplayLocation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etDisplayLocation.setText(arrDisplayLocation[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog12 = builder.create();
                dialog12.show();
                break;
            case R.id.et_placement_request:
                // setup the alert builder
                builder = new AlertDialog.Builder(ChillerTransferFormTwoActivity.this);
                builder.setTitle("New placement requested by");
                builder.setItems(arrPlacementRequest, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etPlacementRequest.setText(arrPlacementRequest[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog14 = builder.create();
                dialog14.show();
                break;
            default:
                break;
        }
    }

    private void addCustomer() {
        Customer mCustomer = new Customer();
        mCustomer.setCustomerId(custNum);
        mCustomer.setCustomerCode(custNum);
        mCustomer.setCustomerName(binding.etNewOutletName.getText().toString());
        mCustomer.setCustomerName2("");
        mCustomer.setAddress(binding.etCustomerNewName.getText().toString());
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
        startActivity(new Intent(ChillerTransferFormTwoActivity.this, FragmentJourneyPlan.class));
        //Toast.makeText(getApplicationContext(), "Customer added successfully!", Toast.LENGTH_SHORT).show();
        finish();

    }

    //Date
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;

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


    //update month day year
    private void updateDisplay() {
        binding.etAgentDate.setText(//this is the edit text where you want to show the selected date
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(""));

    }

}
