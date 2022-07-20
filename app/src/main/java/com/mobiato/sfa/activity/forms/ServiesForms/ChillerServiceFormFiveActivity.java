package com.mobiato.sfa.activity.forms.ServiesForms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.DatePicker;

import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormTwoActivity;
import com.mobiato.sfa.databinding.ActivityChillerServiceFormFiveBinding;
import com.mobiato.sfa.databinding.ActivityChillerServiceFormFourBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Salesman;

import java.util.ArrayList;
import java.util.Calendar;

public class ChillerServiceFormFiveActivity extends BaseActivity implements View.OnClickListener {

    public ActivityChillerServiceFormFiveBinding binding; //Change this

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
        binding = ActivityChillerServiceFormFiveBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Chiller Services Form");
        // setFullScreenBack();

        //mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
        binding.btnTransferNext.setOnClickListener(this);
        binding.btnTransferPre.setOnClickListener(this);
        binding.etTechDate.setOnClickListener(this);
        baseBinding.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChillerServiceFormFiveActivity.this, ChillerServiceFormFourActivity.class));
                finish();
            }
        });

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
            case R.id.et_tech_date:
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.btnTransferPre:
                startActivity(new Intent(ChillerServiceFormFiveActivity.this, ChillerServiceFormFourActivity.class));
                finish();
                break;
            case R.id.btnTransferNext:
                //  if (checkNullValues()) {
                /*   // Toast.makeText(ChillerTransferFormActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
                    //addCustomer();
                    startActivity(new Intent(ChillerTransferFormActivity.this, ChillerTransferFormTwoActivity.class));

                }*/

                break;
            default:
                break;
        }
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
        binding.etTechDate.setText(//this is the edit text where you want to show the selected date
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(""));

    }

}
