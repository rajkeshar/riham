package com.mobiato.sfa.Techinician.fragment.Customers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.DownloadingDataActivity;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.activity.UpdateCustomer.SimpleUpdateScannerActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormThreeActivity;
import com.mobiato.sfa.databinding.ActivityChillerTransferFormBinding;
import com.mobiato.sfa.databinding.ActivityCustomerTransferFormBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerTechnician;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;

public class CustomerHistoryFormActivity extends BaseActivity implements View.OnClickListener {

    public ActivityCustomerTransferFormBinding binding;
    public String[] arrOutletType = {"Retail", "Whsale", "Eatery", "Bar", "Supermarket", "Other"};
    public String[] arrCoolerType = {"CC", "PC", "HI", "Own", "Others"};
    public String[] arrCoolerSize = {"80", "100", "150", "200", "250", "330", "350", "490"};
    private String strFridge = "", custNum = "", strType = "", strCustTypeId = "", strCustCategoryId = "", strCType = "";
    private Salesman mSalesman;
    private DBManager db;
    private ArrayList<String> arrFridge = new ArrayList<>();
    private AlertDialog.Builder builder;
    private String[] arrCusType, arrCusTypeId, arrCusCategory, arrCusCatId;
    private CustomerTechnician mCustomer;
    RadioButton radioButton;

    public String route_id = "0";
    public String sales_id = "0";
    String str_fridge_code="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerTransferFormBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Customer Detail");
        mCustomer = (CustomerTechnician) getIntent().getSerializableExtra("customer");

        // mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
        //binding.edtDocId.setText(mSalesman.getAgent());

        route_id = Settings.getString(App.ROUTEID);
        sales_id = Settings.getString(App.SALESMANID);
        binding.btnTransferNext.setOnClickListener(this);
      /*  binding.etOutletType.setOnClickListener(this);
        binding.edtEffectiveDate.setOnClickListener(this);
        binding.etCooler.setOnClickListener(this);*/


        getCustomer_technician(mCustomer.getCustomer_id());


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
                Toast.makeText(CustomerHistoryFormActivity.this, "Please enter present outlet name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerName.getText().toString().matches("")) {
                Toast.makeText(CustomerHistoryFormActivity.this, "Please enter customer name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().matches("")) {
                Toast.makeText(CustomerHistoryFormActivity.this, "Please enter contact number", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().length() < 10) {
                Toast.makeText(CustomerHistoryFormActivity.this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            } else if (binding.etLocation.getText().toString().matches("")) {
                Toast.makeText(CustomerHistoryFormActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
            } else if (binding.etLandmark.getText().toString().matches("")) {
                Toast.makeText(CustomerHistoryFormActivity.this, "Please enter landmark", Toast.LENGTH_SHORT).show();
            } else if (binding.etAssetNo.getText().toString().matches("")) {
                Toast.makeText(CustomerHistoryFormActivity.this, "Please enter asset number", Toast.LENGTH_SHORT).show();
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
            case R.id.btnTransferNext:

                Intent intent = new Intent(CustomerHistoryFormActivity.this, ChillerTransferFormThreeActivity.class);
                intent.putExtra("customer", mCustomer);
                intent.putExtra("outlettype", binding.etOutletType.getText().toString());
                intent.putExtra("existing_cooler", binding.etCooler.getText().toString());
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
                System.out.println("p->"+binding.etShare.getText().toString());
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
                break;
            case R.id.et_outletType:
                // setup the alert builder
                builder = new AlertDialog.Builder(CustomerHistoryFormActivity.this);
                builder.setTitle("Choose an Outlet");
                builder.setItems(arrOutletType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etOutletType.setText(arrOutletType[which]);
                        if(arrOutletType[which].equals("Other")){
                            binding.laySpecific.setVisibility(View.VISIBLE);
                        }else {
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
            case R.id.et_cooler:
                // setup the alert builder
                builder = new AlertDialog.Builder(CustomerHistoryFormActivity.this);
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
        startActivity(new Intent(CustomerHistoryFormActivity.this, FragmentJourneyPlan.class));
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
    public void onResume(){
        super.onResume();
        // put your code here...
    }
    private ArrayList<String> arrImage = new ArrayList<>();
    String img_path=App.BASE_URL+"upload_image/asset_request/";
    private void getCustomer_technician(String customer_id) {
        ProgressDialog progress=new ProgressDialog(this);
        progress.setMessage("Please wait...");
        //  progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        // progress.setProgress(0);
        progress.show();

        String custId = Settings.getString(App.SALESMANID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomerTechnician(App.CUSTOMER_TECHNICIAN, custId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Response technician", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(CustomerHistoryFormActivity.this, "Customer Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<CustomerTechnician> arrCustomer = new ArrayList<>();
                        arrCustomer = new Gson().fromJson(response.body().get("Result").getAsJsonArray().toString(),
                                new TypeToken<List<CustomerTechnician>>() {
                                }.getType());
                        //db.deleteTable(DBManager.TABLE_CUSTOMER);
                        for (int i=0;i<arrCustomer.size();i++){
                            if(arrCustomer.get(i).getCustomer_id().equals(customer_id)){
                                System.out.println("p00-->"+arrCustomer.get(i).getLocation());
                                if(arrCustomer.get(i).getNational_id().equals("yes")||arrCustomer.get(i).getNational_id().equals("Yes")){
                                    binding.layNationalImg.setVisibility(View.VISIBLE);
                                    binding.raYesId.setChecked(true);
                                    Glide.with(CustomerHistoryFormActivity.this)
                                            .load(img_path+arrCustomer.get(i).getNational_id_file())
                                            .thumbnail(0.1f)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .into(binding.nationImg);
                                }else {
                                    binding.raNoId.setChecked(true);
                                    binding.layNationalImg.setVisibility(View.GONE);
                                }
                                binding.etCustomerName.setText(arrCustomer.get(i).getCustomername());
                                binding.etCustomerTelephone.setText(arrCustomer.get(i).getContact_number());
                                binding.etAddress.setText(arrCustomer.get(i).getPostal_address());
                                binding.etLocation.setText(arrCustomer.get(i).getLocation());
                                binding.etLandmark.setText(arrCustomer.get(i).getLandmark());
                                binding.etOutletType.setText(arrCustomer.get(i).getOutlet_type());
                                if(arrCustomer.get(i).getOutlet_type().equals("Other")){
                                    binding.laySpecific.setVisibility(View.VISIBLE);
                                    binding.etOutletOtherType.setText(arrCustomer.get(i).getSpecify_if_other_type());
                                }
                                binding.etCooler.setText(arrCustomer.get(i).getExisting_coolers());
                                binding.etShare.setText(arrCustomer.get(i).getStock_share_with_competitor());
                                binding.etCurrentSale.setText(arrCustomer.get(i).getOutlet_weekly_sale_volume());
                                binding.etExpectedSale.setText(arrCustomer.get(i).getOutlet_weekly_sales());
                                binding.etAssetNo.setText(arrCustomer.get(i).getAsset_number());
                                binding.etSerialNo.setText(arrCustomer.get(i).getMachine_number());
                                binding.etMachineType.setText(arrCustomer.get(i).getBrand());
                                binding.etHIL.setText(arrCustomer.get(i).getHil());
                                binding.etChillerSizeRequested.setText(arrCustomer.get(i).getChiller_size_requested());

                                //image



                                //PAssport
                                if(arrCustomer.get(i).getPassword_photo().equals("yes")||arrCustomer.get(i).getPassword_photo().equals("Yes")){
                                    binding.layPassportImg.setVisibility(View.VISIBLE);
                                    binding.raYesPassport.setChecked(true);
                                    Glide.with(CustomerHistoryFormActivity.this)
                                            .load(img_path+arrCustomer.get(i).getPassword_photo_file())
                                            .thumbnail(0.1f)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .into(binding.passportImg);
                                }else {
                                    binding.raNoPassport.setChecked(true);
                                    binding.layPassportImg.setVisibility(View.GONE);
                                }

                                if(arrCustomer.get(i).getOutlet_address_proof().equals("yes")||arrCustomer.get(i).getOutlet_address_proof().equals("Yes")){
                                    binding.layProofImg.setVisibility(View.VISIBLE);
                                    binding.raYesProof.setChecked(true);
                                    Glide.with(getBaseContext())
                                            .load(img_path+arrCustomer.get(i).getOutlet_address_proof_file())
                                            .thumbnail(0.1f)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .into(binding.outletaddressimg);
                                }else {
                                    binding.raNoProof.setChecked(true);
                                    binding.layProofImg.setVisibility(View.GONE);
                                }

                                if(arrCustomer.get(i).getOutlet_stamp().equals("yes")||arrCustomer.get(i).getOutlet_stamp().equals("Yes")){
                                    binding.layoutImage.setVisibility(View.VISIBLE);
                                    binding.raYesStemp.setChecked(true);
                                    Glide.with(getBaseContext())
                                            .load(img_path+arrCustomer.get(i).getOutlet_stamp_file())
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .thumbnail(0.1f)
                                            .into(binding.outletstempimg);
                                }else {
                                    binding.raNoStemp.setChecked(true);
                                    binding.layoutImage.setVisibility(View.GONE);
                                }

                                if(arrCustomer.get(i).getLc_letter().equals("yes")||arrCustomer.get(i).getLc_letter().equals("Yes")){
                                    binding.layoutImagelc.setVisibility(View.VISIBLE);
                                    binding.raYesLc.setChecked(true);
                                    Glide.with(getBaseContext())
                                            .load(img_path+arrCustomer.get(i).getLc_letter())
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .thumbnail(0.1f)
                                            .into(binding.lcimage);
                                }else {
                                    binding.raNoLc.setChecked(true);
                                    binding.layoutImagelc.setVisibility(View.GONE);
                                }

                                if(arrCustomer.get(i).getTrading_licence().equals("yes")||arrCustomer.get(i).getTrading_licence().equals("Yes")){
                                    binding.layLicenceImg.setVisibility(View.VISIBLE);
                                    binding.raYesTreding.setChecked(true);
                                    Glide.with(getBaseContext())
                                            .load(img_path+arrCustomer.get(i).getTrading_licence_file())
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .thumbnail(0.1f)
                                            .into(binding.tradingLc);
                                }else {
                                    binding.raNoTreding.setChecked(true);
                                    binding.layLicenceImg.setVisibility(View.GONE);
                                }

                            }
                        }

                    }
                } else {
                    UtilApp.logData(CustomerHistoryFormActivity.this, "Customer Response: Blank");
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                progress.dismiss();
                UtilApp.logData(CustomerHistoryFormActivity.this, "Customer Failure: " + error.getMessage());
            }
        });
    }
}
