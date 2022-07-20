package com.mobiato.sfa.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.Result;
import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Freeze;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private Customer mCustomer;
    public String route_id = "0";
    public String sales_id = "0";
    private DBManager db;
    public String mType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        mType = getIntent().getStringExtra("Type");
        if (mType.equals("ScanQR")) {
            mCustomer = (Customer) getIntent().getSerializableExtra("customer");// Set the scanner view as the content view
            route_id = Settings.getString(App.ROUTEID);
            sales_id = Settings.getString(App.SALESMANID);
        }
        db = new DBManager(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("Barcode", rawResult.getText());
        String currentString = rawResult.getText();

        if (mType.equals("ScanQR")) {
            if (currentString.contains(";")) {
                String[] separated = currentString.split(";");
                try {
                    String msg = separated[4];
                    if (!msg.equals(mCustomer.getFridger_code())) {
                        reason_no();
                    } else {
                        Intent intent1 = new Intent(SimpleScannerActivity.this, CameraFormActivity.class);
                        intent1.putExtra("customer", mCustomer);
                        intent1.putExtra("code", msg);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //Intent intent1 = new Intent(context, CaptureActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                } catch (Exception e) {
                    try {
                        String[] separated1 = currentString.split(";");
                        if (!separated1.equals(mCustomer.getFridger_code())) {
                            reason_no();
                        } else {
                            try {
                                String msg = separated1[1];
                                Intent intent1 = new Intent(SimpleScannerActivity.this, CameraFormActivity.class);
                                intent1.putExtra("customer", mCustomer);
                                intent1.putExtra("code", msg);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);
                                finish();
                            } catch (Exception e1) {
                                if (!rawResult.getText().toString().equals(mCustomer.getFridger_code())) {
                                    reason_no();
                                } else {
                                    Intent intent1 = new Intent(SimpleScannerActivity.this, CameraFormActivity.class);
                                    intent1.putExtra("customer", mCustomer);
                                    intent1.putExtra("code", rawResult.getText().toString());
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent1);
                                    finish();
                                }
                            }
                        }
                    } catch (Exception e1) {
                        if (!rawResult.getText().toString().equals(mCustomer.getFridger_code())) {
                            reason_no();
                        } else {
                            Intent intent1 = new Intent(SimpleScannerActivity.this, CameraFormActivity.class);
                            intent1.putExtra("customer", mCustomer);
                            intent1.putExtra("code", "");
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent1);
                            finish();
                        }
                    }

                }
            } else {
                Intent intent1 = new Intent(SimpleScannerActivity.this, CameraFormActivity.class);
                intent1.putExtra("customer", mCustomer);
                intent1.putExtra("code", currentString);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Intent intent1 = new Intent(context, CaptureActivity.class);
                startActivity(intent1);
                finish();
            }
        } else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("code", currentString);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }


        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

    public String[] arrDisplayLocation = {"Mis match", "Un authorised movement/Transfer", "Mis use/Low Purity/cooler Abuse", "No stock/Low Stock.", "Customer Not Supportive", "Request to Print Qr Code for the Fridge", "Others"};
    public String[] arrDisplayLocationNo = {"Another Cooler found", "Un authorised cooler movement found", "Chiller Transferred to another outlet", "Cooler Available Barcode Sticker Damaged", "Others(Specify the reason)"};

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
                builder = new AlertDialog.Builder(SimpleScannerActivity.this);
                builder.setTitle("Choose Reason");
                builder.setItems(arrDisplayLocationNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et_dispute.setText(arrDisplayLocationNo[which]);
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
                if (!mCustomer.getCustType().equalsIgnoreCase("1")) {
                      /*  SalesActivity.arrFOCItem = new ArrayList<>();
                        startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));*/
                    Freeze mPromotion = new Freeze();
                    mPromotion.setRoute_id(route_id);
                    mPromotion.setSalesman_id(Settings.getString(App.SALESMANID));
                    mPromotion.setCustomer_id(mCustomer.getCustomerId());
                    mPromotion.setHave_fridge("No");
                    mPromotion.setLatitude(String.valueOf(mCustomer.getLatitude()));
                    mPromotion.setLongitude(String.valueOf(mCustomer.getLongitude()));
                    mPromotion.setComments(edt_sale_reason.getText().toString());
                    mPromotion.setComplaint_type(et_dispute.getText().toString());
                    mPromotion.setSerial_no(et_dispute.getText().toString());

                    mPromotion.setImage1("");
                    mPromotion.setImage2("");
                    mPromotion.setImage3("");
                    mPromotion.setImage4("");
                    mPromotion.setFridge_scan_img("");

                    String custNum = UtilApp.getChilerTrackingNo();
                    db.insertChillerTrack(custNum, mPromotion);

                    Transaction transaction = new Transaction();
                    transaction.tr_type = Constant.TRANSACTION_TYPES.TT_CHILER_TRACK;
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
                    startActivity(new Intent(SimpleScannerActivity.this, SalesActivity.class).putExtra("customer", mCustomer));


//                    //  callCompititorAPI();
//                    RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.CHILLER_TRACKING);
//                    RequestBody route_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getRoute_id());
//                    RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
//                    RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getCustomerId());
//                    RequestBody have_fridge = RequestBody.create(MediaType.parse("text/plain"), "No");
//                    RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getLatitude());
//                    RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getLongitude());
//                    RequestBody comments = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComments());
//                    RequestBody Complaint_type = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComplaint_type());
//                    RequestBody serial_no = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComplaint_type());
//
//                    List<MultipartBody.Part> parts = new ArrayList<>();
//
////        // add dynamic amount
//                    if (!mPromotion.getImage1().equalsIgnoreCase("")) {
//                        parts.add(UtilApp.prepareFilePart("image1", mPromotion.getImage1()));
//                    }
//
//                    if (!mPromotion.getImage2().equalsIgnoreCase("")) {
//                        parts.add(UtilApp.prepareFilePart("image2", mPromotion.getImage2()));
//                    }
//
//                    if (!mPromotion.getImage3().equalsIgnoreCase("")) {
//                        parts.add(UtilApp.prepareFilePart("image3", mPromotion.getImage3()));
//                    }
//
//                    if (!mPromotion.getImage4().equalsIgnoreCase("")) {
//                        parts.add(UtilApp.prepareFilePart("image4", mPromotion.getImage4()));
//                    }
//                    try {
//                        if (!mPromotion.getFridge_scan_img().equalsIgnoreCase("")) {
//                            parts.add(UtilApp.prepareFilePart("fridge_scan_img", mPromotion.getFridge_scan_img()));
//                        }
//                    } catch (Exception e) {
//                        e.toString();
//                    }
//
//                    UtilApp.logData(SimpleScannerActivity.this, "Compatitor Feedback Request: " + mPromotion.toString());
//
//                    final Call<JsonObject> labelResponse = ApiClient.getService().postFridgeData(method, route_id, salesman_id,
//                            customer_id, have_fridge, latitude, longitude, comments, Complaint_type, serial_no, parts);
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
//                                /*    db.updateCampititorPosted(mPromotion.getCompititorId());
//                                    db.updateTransaction(mPromotion.getCompititorId());*/
//                                                SalesActivity.arrFOCItem = new ArrayList<>();
//                                                startActivity(new Intent(SimpleScannerActivity.this, SalesActivity.class).putExtra("customer", mCustomer));
//
//                                            } else {
//                                                // Fail to Post
//                                                App.isCompititorSync = false;
//                                            }
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    App.isCompititorSync = false;
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                            Log.e("Compititor fail:", t.toString());
//                            UtilApp.logData(SimpleScannerActivity.this, "Compititoer Fail: " + t.getMessage());
//                            App.isCompititorSync = false;
//                        }
//                    });
                } else {
                   /* SalesActivity.arrFOCItem = new ArrayList<>();
                    startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));*/
                    Freeze mPromotion = new Freeze();
                    mPromotion.setRoute_id(route_id);
                    mPromotion.setSalesman_id(sales_id);
                    mPromotion.setCustomer_id(mCustomer.getCustomerId());
                    mPromotion.setHave_fridge("No");
                    mPromotion.setLatitude(String.valueOf(mCustomer.getLatitude()));
                    mPromotion.setLongitude(String.valueOf(mCustomer.getLongitude()));
                    mPromotion.setComments(edt_sale_reason.getText().toString());
                    mPromotion.setComplaint_type(et_dispute.getText().toString());
                    mPromotion.setSerial_no("");


                    mPromotion.setImage1("");
                    mPromotion.setImage2("");
                    mPromotion.setImage3("");
                    mPromotion.setImage4("");

                    String custNum = UtilApp.getChilerTrackingNo();
                    db.insertChillerTrack(custNum, mPromotion);

                    Transaction transaction = new Transaction();
                    transaction.tr_type = Constant.TRANSACTION_TYPES.TT_CHILER_TRACK;
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
                    startActivity(new Intent(SimpleScannerActivity.this, SalesActivity.class).putExtra("customer", mCustomer));


                    // callCompititorAPI();
//                    RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.CHILLER_TRACKING);
//                    RequestBody route_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.ROUTEID));
//                    RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
//                    RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getCustomerId());
//                    RequestBody have_fridge = RequestBody.create(MediaType.parse("text/plain"), "No");
//                    RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getLatitude());
//                    RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getLongitude());
//                    RequestBody comments = RequestBody.create(MediaType.parse("text/plain"), edt_sale_reason.getText().toString());
//                    RequestBody Complaint_type = RequestBody.create(MediaType.parse("text/plain"), et_dispute.getText().toString());
//                    RequestBody serial_no = RequestBody.create(MediaType.parse("text/plain"), "");
//
//                    List<MultipartBody.Part> parts = new ArrayList<>();
//
////        // add dynamic amount
//                    if (!mPromotion.getImage1().equalsIgnoreCase("")) {
//                        parts.add(UtilApp.prepareFilePart("image1", mPromotion.getImage1()));
//                    }
//
//                    if (!mPromotion.getImage2().equalsIgnoreCase("")) {
//                        parts.add(UtilApp.prepareFilePart("image2", mPromotion.getImage2()));
//                    }
//
//                    if (!mPromotion.getImage3().equalsIgnoreCase("")) {
//                        parts.add(UtilApp.prepareFilePart("image3", mPromotion.getImage3()));
//                    }
//
//                    if (!mPromotion.getImage4().equalsIgnoreCase("")) {
//                        parts.add(UtilApp.prepareFilePart("image4", mPromotion.getImage4()));
//                    }
//
//                    try {
//                        if (!mPromotion.getFridge_scan_img().equalsIgnoreCase("")) {
//                            parts.add(UtilApp.prepareFilePart("fridge_scan_img", mPromotion.getFridge_scan_img()));
//                        }
//                    } catch (Exception e) {
//                        e.toString();
//                    }
//
//                    UtilApp.logData(SimpleScannerActivity.this, "Compatitor Feedback Request: " + mPromotion.toString());
//
//                    final Call<JsonObject> labelResponse = ApiClient.getService().postFridgeData(method, route_id, salesman_id,
//                            customer_id, have_fridge, latitude, longitude, comments, Complaint_type, serial_no, parts);
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
//                                /*    db.updateCampititorPosted(mPromotion.getCompititorId());
//                                    db.updateTransaction(mPromotion.getCompititorId());*/
//                                                SalesActivity.arrFOCItem = new ArrayList<>();
//                                                startActivity(new Intent(SimpleScannerActivity.this, SalesActivity.class).putExtra("customer", mCustomer));
//                                            } else {
//                                                // Fail to Post
//                                                App.isCompititorSync = false;
//                                            }
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    App.isCompititorSync = false;
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                            Log.e("Compititor fail:", t.toString());
//                            UtilApp.logData(SimpleScannerActivity.this, "Compititoer Fail: " + t.getMessage());
//                            App.isCompititorSync = false;
//                        }
//                    });
                }
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}
