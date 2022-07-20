package com.mobiato.sfa.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.UpdateCustomer.UpdateExistingCustomerActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormThreeActivity;
import com.mobiato.sfa.databinding.ActivityCameraBinding;
import com.mobiato.sfa.databinding.ActivityChilleraddBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.AddChiller;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Freeze;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddChillerFormActivity extends BaseActivity implements View.OnClickListener {

    public ActivityChilleraddBinding binding;

    private Customer mCustomer;

    AddChiller mPromotion = new AddChiller();
    String str_fridge_code = "";
    String depot_id = "";
    String custNum = "";
    private DBManager db;

    private File photoFile_chiller;
    private static final int TAKE_PHOTO_CHILLER_REQUEST = 100;
    private String mCurrentPhotoPath, mFilePath;
    private ArrayList<String> arrImage_chiller = new ArrayList<>();
    private ArrayList<String> arrImagePath_chiller = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChilleraddBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Add Chiller");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mCustomer = (Customer) getIntent().getSerializableExtra("customer");
        str_fridge_code = getIntent().getStringExtra("code");

        binding.btnSubmit.setOnClickListener(this);
        depot_id = Settings.getString(App.DEPOTID);

        custNum = UtilApp.getChillerNo();
        db = new DBManager(this);
        binding.edtSaleSerial.setText(str_fridge_code);
        binding.btnAddChiller.setOnClickListener(this);


    }

    private void dispatchTakeChillerPictureIntent() {

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_chiller = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_chiller));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_CHILLER_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mFilePath = "Riham_" + timeStamp + ".png";
        File storageDir = Environment.getExternalStoragePublicDirectory("Riham");
        if (!storageDir.exists()) {

            if (!storageDir.mkdirs()) {
                return null;
            }
        }

        File image = new File(storageDir.getPath() + File.separator +
                mFilePath);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddChiller:
                if (arrImage_chiller.size() < 1) {
                    dispatchTakeChillerPictureIntent();
                }
                break;
            case R.id.btn_submit:
                if (binding.edtSaleSerial.getText().toString().equals("")) {
                    UtilApp.displayAlert(me, "Please enter serial number.");
                } else if (arrImagePath_chiller.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload Chiller Image.");
                } else {

                    db.insertChiller(custNum, mCustomer.getCustomerId(), binding.edtSaleSerial.getText().toString(), arrImagePath_chiller.get(0));

                    //INSERT TRANSACTION
                    Transaction transaction = new Transaction();
                    transaction.tr_type = Constant.TRANSACTION_TYPES.TT_CHILER_ADD;
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

                    SalesActivity.arrFOCItem = new ArrayList<>();
                    /* startActivity(new Intent(me, CustomerDetailActivity.class).putExtra("customer", mCustomer));*/
                    startActivity(new Intent(me, CustomerDetailActivity.class).putExtra("custmer", mCustomer));
                    finish();


//                    ProgressDialog progress = new ProgressDialog(this);
//                    progress.setMessage("Please wait...");
//                    progress.setIndeterminate(true);
//                    progress.show();
//
//                    mPromotion = new AddChiller();
//                    mPromotion.setDepot_id(Settings.getString(App.DEPOTID));
//                    mPromotion.setSalesman_id(Settings.getString(App.SALESMANID));
//                    mPromotion.setCustomer_id(mCustomer.getCustomerId());
//                    mPromotion.setAsset_no(binding.edtSaleSerial.getText().toString());
//                    try {
//                        mPromotion.setFridge_scan_img(arrImagePath_chiller.get(0));
//                    } catch (Exception e) {
//                    }
//                    //callCompititorAPI();
//                    System.out.println("routId-->" + mPromotion.getDepot_id());
//                    // db.insertChillerAdd(mPromotion);
//                    RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.ADD_CHILLER);
//                    RequestBody depot_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getDepot_id());
//                    RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getSalesman_id());
//                    RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getCustomer_id());
//                    RequestBody asset_no = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getAsset_no());
//                    List<MultipartBody.Part> parts = new ArrayList<>();
//
//                    try {
//                        if (!mPromotion.getFridge_scan_img().equalsIgnoreCase("")) {
//                            parts.add(UtilApp.prepareFilePart("fridge_scan_img", mPromotion.getFridge_scan_img()));
//                        }
//                    } catch (Exception e) {
//                        e.toString();
//                    }
//
//                    UtilApp.logData(AddChillerFormActivity.this, "Compatitor Feedback Request: " + mPromotion.toString());
//
//                    final Call<JsonObject> labelResponse = ApiClient.getService().postAddFridgeData(method, depot_id, salesman_id,
//                            customer_id, asset_no, parts);
//
//                    labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
//                        @Override
//                        public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                            Log.e("Freeze Response:", response.toString());
//                            //  UtilApp.logData(SyncData.this, "Compitior Response: " + response.toString());
//                            if (response.body() != null) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response.body().toString());
//                                    if (jsonObject.has("STATUS")) {
//                                        if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
//                                            Log.e("Status", jsonObject.getString("STATUS"));
//                                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
//                                                App.isCompititorSync = false;
//                                                Log.e("Compititor Success", jsonObject.getString("STATUS"));
//                                                Settings.setString(App.CUSTOMER_LAST, custNum);
//                                                //INSERT TRANSACTION
//                                                Transaction transaction = new Transaction();
//                                                transaction.tr_type = Constant.TRANSACTION_TYPES.TT_CHILER_ADD;
//                                                transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
//                                                transaction.tr_customer_num = mCustomer.getCustomerId();
//                                                transaction.tr_customer_name = mCustomer.getCustomerName();
//                                                transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
//                                                transaction.tr_invoice_id = "";
//                                                transaction.tr_order_id = custNum;
//                                                transaction.tr_collection_id = "";
//                                                transaction.tr_pyament_id = "";
//                                                transaction.tr_is_posted = "Yes";
//                                                transaction.tr_printData = "";
//                                                db.insertTransaction(transaction);
//                                                progress.hide();
//                                                SalesActivity.arrFOCItem = new ArrayList<>();
//                                                /* startActivity(new Intent(me, CustomerDetailActivity.class).putExtra("customer", mCustomer));*/
//                                                startActivity(new Intent(me, CustomerDetailActivity.class).putExtra("custmer", mCustomer));
//                                                finish();
//                                            } else {
//                                                // Fail to Post
//                                                App.isCompititorSync = false;
//                                                progress.hide();
//                                            }
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    App.isCompititorSync = false;
//                                    progress.hide();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                            Log.e("Compititor fail:", t.toString());
//                            UtilApp.logData(AddChillerFormActivity.this, "Compititoer Fail: " + t.getMessage());
//                            App.isCompititorSync = false;
//                            progress.hide();
//                        }
//                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CHILLER_REQUEST && resultCode == RESULT_OK) {
            String filePat_chiller = UtilApp.compressImage(AddChillerFormActivity.this, mCurrentPhotoPath);
            binding.layoutImagechiller.setVisibility(View.GONE);
            binding.layoutPagerChiller.setVisibility(View.VISIBLE);
            binding.btnAddChiller.setVisibility(View.VISIBLE);
            arrImage_chiller.add(filePat_chiller);
            arrImagePath_chiller.add(filePat_chiller.substring(filePat_chiller.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_chiller);
            binding.viewPagerChiller.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsChiller.setViewPager(binding.viewPagerChiller);
        }

    }

}
