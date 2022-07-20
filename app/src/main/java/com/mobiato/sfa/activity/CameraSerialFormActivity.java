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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityCameraBinding;
import com.mobiato.sfa.localdb.DBManager;
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

public class CameraSerialFormActivity extends BaseActivity implements View.OnClickListener {

    public ActivityCameraBinding binding;
    private File photoFile;
    private static final int TAKE_PHOTO_REQUEST = 1;
    private String mCurrentPhotoPath, mFilePath;
    private ArrayList<String> arrPagerImage = new ArrayList<>();
    private ArrayList<String> arrImage = new ArrayList<>();
    private ArrayList<String> arrImagePath = new ArrayList<>();
    private Customer mCustomer;

    private Location mCustomerLC;
    Freeze mPromotion = new Freeze();
    String str_fridge_code = "";

    public String[] arrDisplayLocation = {"Another Cooler found", "Un authorised cooler movement found", "Chiller Transferred to another outlet", "Cooler Available Barcode Sticker Damaged", "Others(Specify the reason)"};
    AlertDialog.Builder builder1;

    private File photoFile_chiller;
    private static final int TAKE_PHOTO_CHILLER_REQUEST = 100;
    private ArrayList<String> arrImage_chiller = new ArrayList<>();
    private ArrayList<String> arrImagePath_chiller = new ArrayList<>();
    String custNum = "";
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Take Picture");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mCustomer = (Customer) getIntent().getSerializableExtra("customer");
        str_fridge_code = getIntent().getStringExtra("code");

        db = new DBManager(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.btnAddCompagin.setOnClickListener(this);
        binding.etdisputelocation.setOnClickListener(this);
        binding.btnAddChiller.setOnClickListener(this);

        binding.rlCheckout211.setVisibility(View.GONE);
        binding.layImageHide.setVisibility(View.GONE);
        /*if(!str_fridge_code.equals(mCustomer.getFridger_code())){
            binding.etdisputelocation.setText("Mis match");
        }*//*else if(str_fridge_code.equals("")){
            binding.etdisputelocation.setText("Mis match");
          //  Toast.makeText(this, "check-> "+mCustomer.getFridger_code(), Toast.LENGTH_SHORT).show();
        }*/


    }

    private void dispatchTakePictureIntent() {

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            case R.id.btnAddCompagin:
                if (arrImage.size() < 4) {
                    dispatchTakePictureIntent();
                }
                //  dispatchTakePictureIntent();
                break;
            case R.id.etdisputelocation:
                builder1 = new AlertDialog.Builder(CameraSerialFormActivity.this);
                builder1.setTitle("Choose Reason");
                builder1.setItems(arrDisplayLocation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etdisputelocation.setText(arrDisplayLocation[which]);
                        if (binding.etdisputelocation.getText().toString().equals("Others(Specify the reason)")) {
                            binding.edtSaleReason.setVisibility(View.VISIBLE);
                        } else {
                            binding.edtSaleReason.setVisibility(View.GONE);
                        }
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog12 = builder1.create();
                dialog12.show();

                break;
            case R.id.btn_submit:
                if (binding.edtSaleSerial.getText().toString().equals("")) {
                    UtilApp.displayAlert(me, "Please enter serial number.");
                } else if (arrImagePath_chiller.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload fridge image.");
                } else if (!binding.edtSaleSerial.getText().toString().equals(mCustomer.getFridger_code())) {
                    reason_no();
                } else {
                    Intent intent1 = new Intent(me, CameraFormActivity.class);
                    intent1.putExtra("customer", mCustomer);
                    intent1.putExtra("code", binding.edtSaleSerial.getText().toString());
                    intent1.putExtra("chiller_image", arrImagePath_chiller.get(0));
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //Intent intent1 = new Intent(context, CaptureActivity.class);
                    startActivity(intent1);
                    finish();
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
            String filePat_chiller = UtilApp.compressImage(CameraSerialFormActivity.this, mCurrentPhotoPath);
            binding.layoutImagechiller.setVisibility(View.GONE);
            binding.layoutPagerChiller.setVisibility(View.VISIBLE);
            binding.btnAddChiller.setVisibility(View.VISIBLE);
            arrImage_chiller.add(filePat_chiller);
            arrImagePath_chiller.add(filePat_chiller.substring(filePat_chiller.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_chiller);
            binding.viewPagerChiller.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsChiller.setViewPager(binding.viewPagerChiller);
        } else if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(CameraSerialFormActivity.this, mCurrentPhotoPath);
            binding.layoutImage.setVisibility(View.GONE);
            binding.layoutPager.setVisibility(View.VISIBLE);
            binding.btnAddCompagin.setVisibility(View.VISIBLE);
            arrImage.add(filePat);
            arrImagePath.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage);
            binding.viewPager.setAdapter(viewPagerAdapter);
            binding.dotsIndicators.setViewPager(binding.viewPager);
            /*if(arrImage.size()<4){
                binding.btnAddCompagin.setVisibility(View.GONE);
            }*/
        }

    }

    //Call Compititoer API
    private void callCompititorAPI() {
//        ProgressDialog progress = new ProgressDialog(this);
//        progress.setMessage("Please wait...");
//        progress.setIndeterminate(true);
//        progress.show();

        mPromotion = new Freeze();
        mPromotion.setRoute_id(Settings.getString(App.ROUTEID));
        mPromotion.setSalesman_id(Settings.getString(App.SALESMANID));
        mPromotion.setCustomer_id(mCustomer.getCustomerId());
        mPromotion.setHave_fridge("yes");
        mPromotion.setLatitude(String.valueOf(mCustomer.getLatitude()));
        mPromotion.setLongitude(String.valueOf(mCustomer.getLongitude()));
        mPromotion.setComments(binding.edtSaleReason.getText().toString());
        mPromotion.setComplaint_type(binding.etdisputelocation.getText().toString());
        mPromotion.setSerial_no(binding.edtSaleSerial.getText().toString());

        try {
            mPromotion.setFridge_scan_img(arrImagePath_chiller.get(0));
        } catch (Exception e) {
        }

        if (arrImagePath.size() == 1) {
            mPromotion.setImage1(arrImagePath.get(0));
            mPromotion.setImage2("");
            mPromotion.setImage3("");
            mPromotion.setImage4("");
        } else if (arrImagePath.size() == 2) {
            mPromotion.setImage1(arrImagePath.get(0));
            mPromotion.setImage2(arrImagePath.get(1));
            mPromotion.setImage3("");
            mPromotion.setImage4("");
        } else if (arrImagePath.size() == 3) {
            mPromotion.setImage1(arrImagePath.get(0));
            mPromotion.setImage2(arrImagePath.get(1));
            mPromotion.setImage3(arrImagePath.get(2));
            mPromotion.setImage4("");
        } else if (arrImagePath.size() == 4) {
            mPromotion.setImage1(arrImagePath.get(0));
            mPromotion.setImage2(arrImagePath.get(1));
            mPromotion.setImage3(arrImagePath.get(2));
            mPromotion.setImage4(arrImagePath.get(3));
        }

        custNum = UtilApp.getChillerNo();
        db.insertFreedge(custNum, mPromotion);

        //INSERT TRANSACTION
        Transaction transaction = new Transaction();
        transaction.tr_type = Constant.TRANSACTION_TYPES.TT_FRIDGE_ADD;
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


        //startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));
        Intent intent = new Intent(me, SalesActivity.class);
        intent.putExtra("customer", mCustomer);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


//        //callCompititorAPI();
//        System.out.println("routId-->" + mPromotion.getSalesman_id());
//        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.CHILLER_TRACKING);
//        RequestBody route_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getRoute_id());
//        RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getSalesman_id());
//        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getCustomer_id());
//        RequestBody have_fridge = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getHave_fridge());
//        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLatitude());
//        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLongitude());
//        RequestBody comments = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComments());
//        RequestBody Complaint_type = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComplaint_type());
//        RequestBody serial_no = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getSerial_no());
//
//        List<MultipartBody.Part> parts = new ArrayList<>();
//
////        // add dynamic amount
//        try {
//            if (!mPromotion.getImage1().equalsIgnoreCase("")) {
//                parts.add(UtilApp.prepareFilePart("image1", mPromotion.getImage1()));
//            }
//
//            if (!mPromotion.getImage2().equalsIgnoreCase("")) {
//                parts.add(UtilApp.prepareFilePart("image2", mPromotion.getImage2()));
//            }
//
//            if (!mPromotion.getImage3().equalsIgnoreCase("")) {
//                parts.add(UtilApp.prepareFilePart("image3", mPromotion.getImage3()));
//            }
//
//            if (!mPromotion.getImage4().equalsIgnoreCase("")) {
//                parts.add(UtilApp.prepareFilePart("image4", mPromotion.getImage4()));
//            }
//        } catch (Exception e) {
//            e.toString();
//        }
//        try {
//            if (!mPromotion.getFridge_scan_img().equalsIgnoreCase("")) {
//                parts.add(UtilApp.prepareFilePart("fridge_scan_img", mPromotion.getFridge_scan_img()));
//            }
//        } catch (Exception e) {
//            e.toString();
//        }
//
//        UtilApp.logData(CameraSerialFormActivity.this, "Compatitor Feedback Request: " + mPromotion.toString());
//
//        final Call<JsonObject> labelResponse = ApiClient.getService().postFridgeData(method, route_id, salesman_id,
//                customer_id, have_fridge, latitude, longitude, comments, Complaint_type, serial_no, parts);
//
//        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                Log.e("Freeze Response:", response.toString());
//                //  UtilApp.logData(SyncData.this, "Compitior Response: " + response.toString());
//                if (response.body() != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                        if (jsonObject.has("STATUS")) {
//                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
//                                Log.e("Status", jsonObject.getString("STATUS"));
//                                if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
//                                    App.isCompititorSync = false;
//                                    Log.e("Compititor Success", jsonObject.getString("STATUS"));
//
//                                    SalesActivity.arrFOCItem = new ArrayList<>();
//                                    progress.hide();
//                                    //startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));
//                                    Intent intent = new Intent(me, SalesActivity.class);
//                                    intent.putExtra("customer", mCustomer);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    // Fail to Post
//                                    App.isCompititorSync = false;
//                                    progress.hide();
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        App.isCompititorSync = false;
//                        progress.hide();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.e("Compititor fail:", t.toString());
//                UtilApp.logData(CameraSerialFormActivity.this, "Compititoer Fail: " + t.getMessage());
//                App.isCompititorSync = false;
//            }
//        });
    }

    private AlertDialog.Builder builder;

    public void reason_no() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_input_dialog_freeze, null);

        final TextView txt_name = (TextView) dialogView.findViewById(R.id.txt_name);
        final EditText edt_sale_reason = (EditText) dialogView.findViewById(R.id.edt_sale_reason);
        final TextView et_dispute = (TextView) dialogView.findViewById(R.id.et_dispute);
        Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        txt_name.setText("Barcode/QR Code did not match");

        et_dispute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(CameraSerialFormActivity.this);
                builder.setTitle("Choose Reason");
                builder.setItems(arrDisplayLocation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et_dispute.setText(arrDisplayLocation[which]);
                        if (et_dispute.getText().toString().equals("Others(Specify the reason)")) {
                            edt_sale_reason.setVisibility(View.VISIBLE);
                        } else {
                            edt_sale_reason.setVisibility(View.GONE);
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog12 = builder.create();
                dialog12.show();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                if (et_dispute.getText().toString().equals("Others(Specify the reason)")) {
                    if (edt_sale_reason.getText().toString().isEmpty()) {
                        Toast.makeText(me, "Please enter reason", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                callCompititorAPI();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

}
