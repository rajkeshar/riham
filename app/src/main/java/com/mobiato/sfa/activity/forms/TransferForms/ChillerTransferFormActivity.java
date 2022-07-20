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
import android.widget.RadioButton;
import android.widget.Toast;

import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.activity.UpdateCustomer.SimpleUpdateScannerActivity;
import com.mobiato.sfa.activity.forms.FormsActivity;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormActivity;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormThreeActivity;
import com.mobiato.sfa.databinding.ActivityChillerTransferFormBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.Calendar;

public class ChillerTransferFormActivity extends BaseActivity implements View.OnClickListener {

    public ActivityChillerTransferFormBinding binding;
    public String[] arrOutletType = {"Retail", "Wholesale", "Eatery", "Bar", "Supermarket", "Other"};
    public String[] arrCoolerType = {"CC", "PC", "HI", "Own", "Others"};
    /*   public String[] arrCoolerSize = {"80", "100", "150", "200", "250", "330", "350", "490"};*/
    /*public String[] arrCoolerSize = {"CH200X", "CHC200", "CHC400X", "W200", "w 200X", "SAC120", "SPE0253", "SPE0403", "SRC60", "SRC1000", "SRC1000D", "SRC1100",
            "SRC1100-XGLI", "SRC120", "SRC200", "SRC220", "SRC350", "SRC350-XGLI", "SRC450", "SRC550", "SRC550-XGLI", "SRC650", "SRC850", "SRC850X", "Venus", "eKoCool - 35", "EKoCool - JUMBO - X",
            "FR170", "FR240", "Steca 200"};*/
    public String[] arrCoolerSize = {"SRC1000SD-GL", "SRC1100-XGLI", "SRC350-XGLI", "SRC550-XGLI", "SRC60", "FR170", "FR240"};
    private String strFridge = "", custNum = "", strType = "", strCustTypeId = "", strCustCategoryId = "", strCType = "";
    private Salesman mSalesman;
    private DBManager db;
    private ArrayList<String> arrFridge = new ArrayList<>();
    private AlertDialog.Builder builder;
    private String[] arrCusType, arrCusTypeId, arrCusCategory, arrCusCatId;
    private Customer mCustomer;
    RadioButton radioButton;

    public String route_id = "0";
    public String sales_id = "0";
    String str_fridge_code = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChillerTransferFormBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Chiller Transfer Form");
        mCustomer = (Customer) getIntent().getSerializableExtra("customer");

        // mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
        //binding.edtDocId.setText(mSalesman.getAgent());
        try {
            str_fridge_code = Settings.getString(App.SCANSERIALNUMBER);

        } catch (Exception e) {
        }
        route_id = Settings.getString(App.ROUTEID);
        sales_id = Settings.getString(App.SALESMANID);
        binding.btnTransferNext.setOnClickListener(this);
        binding.etOutletType.setOnClickListener(this);
        binding.edtEffectiveDate.setOnClickListener(this);
        // binding.etCooler.setOnClickListener(this);

        String sales_no = Settings.getString(App.SALESMANCONTACT);
        binding.etCustomerName.setText(mCustomer.getCustomerName());
        binding.etCustomerTelephone.setText(mCustomer.getCustPhone());
        binding.etPostalAddress.setText(mCustomer.getAddress());
        binding.etLandmark.setText(mCustomer.getAddress2());

        binding.etSerialNo.setText(str_fridge_code);
        binding.etScanQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(me, SimpleUpdateScannerActivity.class).putExtra("customer", mCustomer).putExtra("available", "No"));
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
            if (binding.etCustomerName.getText().toString().equals("")) {
                Toast.makeText(me, "Please enter customer name", Toast.LENGTH_SHORT).show();
            } else if (binding.etOwnerName.getText().toString().equals("")) {
                Toast.makeText(me, "Please enter owner name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().equals("")) {
                Toast.makeText(me, "Please enter contact number", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().length() < 10) {
                Toast.makeText(me, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            } else if (binding.etLocation.getText().toString().matches("")) {
                Toast.makeText(ChillerTransferFormActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
            } else if (binding.etOutletType.getText().toString().matches("")) {
                Toast.makeText(ChillerTransferFormActivity.this, "Please enter outlet type", Toast.LENGTH_SHORT).show();
            } else if (binding.etOutletType.getText().toString().equals("Other") && binding.etOutletOtherType.getText().toString().matches("")) {
                Toast.makeText(ChillerTransferFormActivity.this, "Please specify other type of outlet", Toast.LENGTH_SHORT).show();
            } else if (binding.etSerialNo.getText().toString().equals("")) {
                UtilApp.displayAlert(me, "Please enter serial number.");
            } else {
                returnvalue = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnvalue;
    }

    String str_existing = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTransferNext:
                if (checkNullValues()) {
                    if (binding.chkCc.isChecked()) {
                        if (str_existing.equalsIgnoreCase("")) {
                            str_existing = "CC";
                        } else {
                            str_existing = str_existing + "," + "CC";
                        }
                    }

                    if (binding.chkPc.isChecked()) {
                        if (str_existing.equalsIgnoreCase("")) {
                            str_existing = "PC";
                        } else {
                            str_existing = str_existing + "," + "PC";
                        }
                    }

                    if (binding.chkHi.isChecked()) {
                        if (str_existing.equalsIgnoreCase("")) {
                            str_existing = "HI";
                        } else {
                            str_existing = str_existing + "," + "HI";
                        }
                    }

                    if (binding.chkOwn.isChecked()) {
                        if (str_existing.equalsIgnoreCase("")) {
                            str_existing = "Own";
                        } else {
                            str_existing = str_existing + "," + "Own";
                        }
                    }

                    if (binding.chkOthers.isChecked()) {
                        if (str_existing.equalsIgnoreCase("")) {
                            str_existing = "Others";
                        } else {
                            str_existing = str_existing + "," + "Others";
                        }
                    }

                    Intent intent = new Intent(ChillerTransferFormActivity.this, ChillerTransferFormThreeActivity.class);
                    intent.putExtra("customer", mCustomer);
                    intent.putExtra("ownername", binding.etOwnerName.getText().toString());
                    intent.putExtra("outlettype", binding.etOutletType.getText().toString());
                    intent.putExtra("existing_cooler", str_existing);
                    intent.putExtra("stock", binding.etShare.getText().toString());
                    intent.putExtra("specify_if_other_type", binding.etOutletOtherType.getText().toString());
                    intent.putExtra("currentsale", binding.etCurrentSale.getText().toString());
                    intent.putExtra("expectedsale", binding.etExpectedSale.getText().toString());
                    intent.putExtra("location", binding.etLocation.getText().toString());
                    intent.putExtra("landmark", binding.etLandmark.getText().toString());
                    intent.putExtra("number", binding.etCustomerTelephone.getText().toString());

                    intent.putExtra("asset_number", binding.etAssetNo.getText().toString());
                    intent.putExtra("serial_number", binding.etSerialNo.getText().toString());
                    intent.putExtra("type_of_the_machine", binding.etMachineType.getText().toString());
                    intent.putExtra("reason_for_withdrawal", binding.etReasonWindwal.getText().toString());
                    intent.putExtra("history_of_the_outlet", binding.etHistory.getText().toString());
                    startActivity(intent);
                    System.out.println("p->" + binding.etShare.getText().toString());
               /* startActivity(new Intent(ChillerRequestFormActivity.this, ChillerRequestFormThreeActivity.class)
                        .putExtra("customer", mCustomer)
                        .putExtra("outlettype", binding.etOutletType.getText().toString())
                        .putExtra("existing_cooler", binding.etCooler.getText().toString())
                        .putExtra("stock", binding.etShare.getText().toString())
                        .putExtra("currentsale", binding.etCurrentSale.getText().toString())
                        .putExtra("expectedsale", binding.etExpectedSale.getText().toString())
                        .putExtra("chillersize", binding.etCoolerSize.getText().toString())
                        .putExtra("grill", radioButton.getText()));
                 System.out.println("p->"+binding.etOutletType.getText().toString());*/
                    //Toast.makeText(ChillerRequestFormActivity.this, radioButton.getText(),Toast.LENGTH_SHORT).show();
              /*  startActivity(new Intent(ChillerRequestFormActivity.this, ChillerRequestFormThreeActivity.class));
                finish();*/
                }
                break;
            case R.id.et_outletType:
                // setup the alert builder
                builder = new AlertDialog.Builder(ChillerTransferFormActivity.this);
                builder.setTitle("Choose an Outlet");
                builder.setItems(arrOutletType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etOutletType.setText(arrOutletType[which]);
                        if (arrOutletType[which].equals("Other")) {
                            binding.laySpecific.setVisibility(View.VISIBLE);
                        } else {
                            binding.laySpecific.setVisibility(View.GONE);
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.edtEffectiveDate:
                showDialog(DATE_DIALOG_ID);
                break;
            /*case R.id.et_cooler:
                // setup the alert builder
                builder = new AlertDialog.Builder(ChillerTransferFormActivity.this);
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
                break;*/
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
        mCustomer.setAddress(binding.etCustomerName.getText().toString());
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
        startActivity(new Intent(ChillerTransferFormActivity.this, FragmentJourneyPlan.class));
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
        binding.edtEffectiveDate.setText(//this is the edit text where you want to show the selected date
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(""));


        //.append(mMonth + 1).append("-")
        //.append(mDay).append("-")
        //.append(mYear).append(" "));
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        str_fridge_code = Settings.getString(App.SCANSERIALNUMBER);
        binding.etSerialNo.setText(str_fridge_code);

    }
}
