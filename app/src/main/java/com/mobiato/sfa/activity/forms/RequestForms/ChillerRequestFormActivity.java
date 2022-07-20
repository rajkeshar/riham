package com.mobiato.sfa.activity.forms.RequestForms;

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
import com.mobiato.sfa.activity.CustomerDetailActivity;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormTwoActivity;
import com.mobiato.sfa.databinding.ActivityChillerRequestFormBinding;
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

public class ChillerRequestFormActivity extends BaseActivity implements View.OnClickListener {

    public ActivityChillerRequestFormBinding binding;
    public String[] arrOutletType = {"Retail", "Wholesale", "Eatery", "Bar", "Supermarket", "Other"};
    public String[] arrCoolerType = {"CC", "PC", "HI", "Own", "Others"};
    //public String[] arrCoolerSize = {"80", "100", "150", "200", "250", "330", "350", "490"};

   /* public String[] arrCoolerSize = {"CH200X", "CHC200", "CHC400X", "W200", "w 200X", "SAC120", "SPE0253", "SPE0403", "SRC60", "SRC1000", "SRC1000D", "SRC1100",
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
    RadioButton radioBLocation;

    public String route_id = "0";
    public String sales_id = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChillerRequestFormBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Chiller Request Form");
        mCustomer = (Customer) getIntent().getSerializableExtra("customer");

        // mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
        //binding.edtDocId.setText(mSalesman.getAgent());
        route_id = Settings.getString(App.ROUTEID);
        sales_id = Settings.getString(App.SALESMANID);
        binding.btnTransferNext.setOnClickListener(this);
        binding.etOutletType.setOnClickListener(this);
        binding.edtEffectiveDate.setOnClickListener(this);
        // binding.etCooler.setOnClickListener(this);
        binding.etCoolerSize.setOnClickListener(this);

        String sales_no = Settings.getString(App.SALESMANCONTACT);
        binding.etCustomerName.setText(mCustomer.getCustomerName());
        binding.etCustomerTelephone.setText(mCustomer.getCustPhone());
        binding.etPostalAddress.setText(mCustomer.getAddress());
        binding.etLandmark.setText(mCustomer.getAddress2());
        // binding.etLocation.setText(mCustomer.getLatitude() + "," + mCustomer.getLongitude());


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
            if (binding.etCustomerName.getText().toString().matches("")) {
                Toast.makeText(ChillerRequestFormActivity.this, "Please enter outlet name", Toast.LENGTH_SHORT).show();
            } else if (binding.etOutletName.getText().toString().equals("")) {
                Toast.makeText(ChillerRequestFormActivity.this, "Please enter present outlet name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().matches("")) {
                Toast.makeText(ChillerRequestFormActivity.this, "Please enter contact number", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().length() < 10) {
                Toast.makeText(ChillerRequestFormActivity.this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            } else if (binding.etLocation.getText().toString().matches("")) {
                Toast.makeText(ChillerRequestFormActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
            } else if (binding.etLandmark.getText().toString().matches("")) {
                Toast.makeText(ChillerRequestFormActivity.this, "Please enter landmark", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(this,str_existing,Toast.LENGTH_SHORT).show();
                if (binding.etCustomerName.getText().toString().equals("")) {
                    Toast.makeText(me, "Please enter customer name", Toast.LENGTH_SHORT).show();
                } else if (binding.etOwnerName.getText().toString().equals("")) {
                    Toast.makeText(me, "Please enter owner name", Toast.LENGTH_SHORT).show();
                } else if (binding.etCustomerTelephone.getText().toString().equals("")) {
                    Toast.makeText(me, "Please enter contact number", Toast.LENGTH_SHORT).show();
                } else if (binding.etCustomerTelephone.getText().toString().length() < 10) {
                    Toast.makeText(ChillerRequestFormActivity.this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
                } else if (binding.etPostalAddress.getText().toString().equals("")) {
                    Toast.makeText(me, "Please enter postal address", Toast.LENGTH_SHORT).show();
                } else if (binding.etLocation.getText().toString().matches("")) {
                    Toast.makeText(me, "Please enter location", Toast.LENGTH_SHORT).show();
                } else if (binding.etOutletType.getText().toString().matches("")) {
                    Toast.makeText(me, "Please enter outlet type", Toast.LENGTH_SHORT).show();
                } else if (binding.etOutletType.getText().toString().equals("Other") && binding.etOutletOtherType.getText().toString().matches("")) {
                    Toast.makeText(me, "Please specify other type of outlet", Toast.LENGTH_SHORT).show();
                } else if (binding.etCoolerSize.getText().toString().equals("")) {
                    Toast.makeText(this, "Please enter chiller size", Toast.LENGTH_SHORT).show();
                } else if (binding.etLandmark.getText().toString().equals("")) {
                    Toast.makeText(this, "Please enter landmark", Toast.LENGTH_SHORT).show();
                } else if (binding.etCurrentSale.getText().toString().equals("")) {
                    Toast.makeText(this, "Please enter current sale", Toast.LENGTH_SHORT).show();
                } else if (binding.etExpectedSale.getText().toString().equals("")) {
                    Toast.makeText(this, "Please enter expected sale", Toast.LENGTH_SHORT).show();
                } else if (binding.etShare.getText().toString().equals("")) {
                    Toast.makeText(this, "Please enter Stock share with competitor in %", Toast.LENGTH_SHORT).show();
                } else {
                    int selectedID = binding.radioGender.getCheckedRadioButtonId();
                    radioButton = findViewById(selectedID);
                    String id = radioButton.getText().toString();
                    if (radioButton.getText().equals("")) {
                        id = "NA";
                    }
                    int selectedIDLocation = binding.radiolocation.getCheckedRadioButtonId();
                    radioBLocation = findViewById(selectedIDLocation);
                    String id1 = radioBLocation.getText().toString();
                    if (radioBLocation.getText().equals("")) {
                        id1 = "Inside";
                    }
                    Intent intent = new Intent(ChillerRequestFormActivity.this, ChillerRequestFormThreeActivity.class);
                    intent.putExtra("customer", mCustomer);
                    intent.putExtra("ownername", binding.etOwnerName.getText().toString());
                    intent.putExtra("outlettype", binding.etOutletType.getText().toString());
                    intent.putExtra("existing_cooler", str_existing);
                    intent.putExtra("stock", binding.etShare.getText().toString());
                    intent.putExtra("address", binding.etPostalAddress.getText().toString());
                    intent.putExtra("specify_if_other_type", binding.etOutletOtherType.getText().toString());
                    intent.putExtra("currentsale", binding.etCurrentSale.getText().toString());
                    intent.putExtra("expectedsale", binding.etExpectedSale.getText().toString());
                    intent.putExtra("chillersize", binding.etCoolerSize.getText().toString());
                    intent.putExtra("location", binding.etLocation.getText().toString());
                    intent.putExtra("landmark", binding.etLandmark.getText().toString());
                    intent.putExtra("number", binding.etCustomerTelephone.getText().toString());
                    intent.putExtra("grill", id);
                    intent.putExtra("display_location", id1);
                    startActivity(intent);
                    // finish();
                    System.out.println("p->" + binding.etOutletType.getText().toString());
                }
                break;
            case R.id.et_outletType:
                // setup the alert builder
                builder = new AlertDialog.Builder(ChillerRequestFormActivity.this);
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
            case R.id.et_cooler_size:
                // setup the alert builder
                builder = new AlertDialog.Builder(ChillerRequestFormActivity.this);
                builder.setTitle("Choose an Cooler Size");
                builder.setItems(arrCoolerSize, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etCoolerSize.setText(arrCoolerSize[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog12 = builder.create();
                dialog12.show();
                break;
            case R.id.edtEffectiveDate:
                showDialog(DATE_DIALOG_ID);
                break;
           /* case R.id.et_cooler:
                // setup the alert builder
                builder = new AlertDialog.Builder(ChillerRequestFormActivity.this);
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
        startActivity(new Intent(ChillerRequestFormActivity.this, FragmentJourneyPlan.class));
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
}