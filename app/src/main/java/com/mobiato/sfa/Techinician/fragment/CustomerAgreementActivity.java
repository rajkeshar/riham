package com.mobiato.sfa.Techinician.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.Adapter.NotificationTechnicianAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.CustomerDetailActivity;
import com.mobiato.sfa.activity.LoadVerifyActivity;
import com.mobiato.sfa.activity.SalesActivity;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormThreeActivity;
import com.mobiato.sfa.databinding.ActivityCustomerAgreementBinding;
import com.mobiato.sfa.databinding.ActivityNotificationBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.merchandising.AddCampaignActivity;
import com.mobiato.sfa.model.ChillerTechnician;
import com.mobiato.sfa.model.Chiller_Model;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerTechnician;
import com.mobiato.sfa.model.Notification;
import com.mobiato.sfa.model.NotificationTechnician;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Part;

public class CustomerAgreementActivity extends BaseActivity {

    private ActivityCustomerAgreementBinding binding;
    private ArrayList<Notification> arrData = new ArrayList<>();
    private CommonAdapter<Notification> mAdapter;
    private DBManager db;
    ArrayList<NotificationTechnician> arrNotification = new ArrayList<>();
    private ArrayList<CustomerTechnician> arrcustomerR = new ArrayList<>();
    ChillerTechnician contact;
    String number = "";

    File file_bh;
    File file_hr;
    File file_sales;
    File file_lc;
    File file_land;

    String sales_id = "";
    String sales_name = "";
    String sales_no = "";
    String fridge_id = "";
    String customer_id = "";
    public String strDigitSign = "";
    public Bitmap bmpSign_hr;
    public Bitmap bmpSign_bh;
    public Bitmap bmpSign_sales;
    public Bitmap bmpSign_lc;
    public Bitmap bmpSign_land;
    String str_signature = "0";
    String behalf_Signature = "upload_image//asset_request/";
    String old_signature = "no";
    private static final int TAKE_PHOTO_REQUEST = 1;
    String mCurrentPhotoPath, mFilePath;
    private File photoFile;
    String image1 = "", image2 = "",image3 = "";
    private boolean isSelected1 = false,isSelected2 = false;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerAgreementBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Agreement");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        db = new DBManager(this);
        arrcustomerR = (ArrayList<CustomerTechnician>) getIntent().getSerializableExtra("customer");
        contact = (ChillerTechnician) getIntent().getSerializableExtra("fridge");
        binding.edtCustomername.setText(arrcustomerR.get(0).getCustomername());
        binding.edtCustomeraddress.setText(arrcustomerR.get(0).getPostal_address() + " , " + arrcustomerR.get(0).getLandmark() + " , " + arrcustomerR.get(0).getLocation());
        binding.edtCustomernameharris.setText(arrcustomerR.get(0).getOwner_name());
        binding.edthil.setText(contact.getAsset_number());
        binding.etModelNumber.setText(contact.getBranding());
        binding.etMachineNumber.setText(contact.getSerial_number());
        binding.etSalesName.setText(arrcustomerR.get(0).getSalesman_name());
        binding.etSalesContactnumber.setText(arrcustomerR.get(0).getSalesman_contact());

        try {
            behalf_Signature = App.BASE_URL + "upload_image//asset_request/" + arrcustomerR.get(0).getSign__customer_file();
            System.out.println("image-->" + behalf_Signature);
            old_signature = arrcustomerR.get(0).getSign__customer_file();
            binding.etBhSignature.setImageURI(Uri.parse(behalf_Signature));
            Glide.with(this)
                    .asBitmap()
                    .load(behalf_Signature).into(binding.etBhSignature);

        } catch (Exception e) {
            e.toString();
        }
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd", Locale.getDefault());
        String formattedDate = df.format(c);
        binding.edtDateof.setText(formattedDate);
        SimpleDateFormat dfm = new SimpleDateFormat("MM-yyyy", Locale.getDefault());
        String formattedDatem = dfm.format(c);
        SimpleDateFormat dfmonth = new SimpleDateFormat("MM", Locale.getDefault());
        String formattedDatemonth = dfmonth.format(c);
        binding.edtdayof.setText(formattedDatemonth);
        SimpleDateFormat dfyr = new SimpleDateFormat("yyyy", Locale.getDefault());
        String formattedDateyr = dfyr.format(c);
        binding.edtyrof.setText(formattedDateyr);


        SimpleDateFormat dfr = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDater = dfr.format(c);
        binding.etHrDate.setText(formattedDater);
        binding.etBhDate.setText(formattedDater);
        binding.etSalesDate.setText(formattedDater);
        binding.etLcDate.setText(formattedDater);
        binding.etLandDate.setText(formattedDater);

        sales_id = Settings.getString(App.SALESMANID);
        sales_name = Settings.getString(App.SALESMANNAME);
        sales_no = Settings.getString(App.SALESMANCONTACT);
        binding.etHrName.setText(sales_name);
        binding.etHrContactnumber.setText(sales_no);

        binding.etBhName.setText(arrcustomerR.get(0).getOwner_name());
        binding.etBhContactnumber.setText(arrcustomerR.get(0).getContact_number());

        fridge_id = contact.getFridge_id();
        customer_id = arrcustomerR.get(0).getCustomer_id();
        binding.etHrSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_signature = "0";
                showCustomerSignCapture();
            }
        });

        /*binding.etBhSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_signature = "1";
                showCustomerSignCapture();
            }
        });*/

        binding.etSalesSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_signature = "2";
                showCustomerSignCapture();
            }
        });

        binding.etLcSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_signature = "3";
                showCustomerSignCapture();
            }
        });

        binding.etLandSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_signature = "4";
                showCustomerSignCapture();
            }
        });

        binding.layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelected1 = true;
                isSelected2 = false;
                dispatchTakePictureIntent();
            }
        });

        binding.layoutImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelected1 = false;
                isSelected2 = false;
                dispatchTakePictureIntent();
            }
        });

        binding.layoutImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelected1 = false;
                isSelected2 = true;
                dispatchTakePictureIntent();
            }
        });


        binding.btnTransferNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bmpSign_hr == null) {
                    UtilApp.displayAlert(CustomerAgreementActivity.this, "Please take a signature of Technician.");
                    //Toast.makeText(getApplicationContext(),"Please make Salesman Signature.",Toast.LENGTH_LONG).show();
                } else if (bmpSign_sales == null) {
                    UtilApp.displayAlert(CustomerAgreementActivity.this, "Please take a signature of Sales Executive..");
                } else if (image1.isEmpty()) {
                    UtilApp.displayAlert(CustomerAgreementActivity.this, "Please capture Asset number image");
                } else if (image2.isEmpty()) {
                    UtilApp.displayAlert(CustomerAgreementActivity.this, "Please capture Fridge positioning image");
                }else if (image3.isEmpty()) {
                    UtilApp.displayAlert(CustomerAgreementActivity.this, "Please capture outlet with Fridge image");
                } /*else if (file_bh == null && behalf_Signature.equals("upload_image//asset_request/")) {
                    UtilApp.displayAlert(CustomerAgreementActivity.this, "Please take a signature of Receiver's");
                }*/ else {
                    ProgressDialog progress = new ProgressDialog(CustomerAgreementActivity.this);
                    progress.setMessage("Please wait...");
                    //  progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progress.setIndeterminate(true);
                    // progress.setProgress(0);
                    progress.show();
//                    try {
//                        if (number.startsWith("0")) {
//                            number = binding.etSalesContactnumber.getText().toString();
//                        } else {
//                            number = "0" + binding.etSalesContactnumber.getText().toString();
//                        }
//                    } catch (Exception e) {
//                        number = binding.etSalesContactnumber.getText().toString();
//                    }
                    number = binding.etSalesContactnumber.getText().toString();
                    //callCompititorAPI();
                    RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.AGREEMENT_POST);
                    RequestBody behaf_reciver_old_signature = RequestBody.create(MediaType.parse("text/plain"), old_signature);
                    RequestBody ms = RequestBody.create(MediaType.parse("text/plain"), binding.edtCustomername.getText().toString());
                    RequestBody ms_of = RequestBody.create(MediaType.parse("text/plain"), binding.edtCustomernameharris.getText().toString());
                    RequestBody address = RequestBody.create(MediaType.parse("text/plain"), binding.edtCustomeraddress.getText().toString());
                    RequestBody asset_number = RequestBody.create(MediaType.parse("text/plain"), contact.getAsset_number());
                    RequestBody serial_number = RequestBody.create(MediaType.parse("text/plain"), contact.getSerial_number());
                    RequestBody model_branding = RequestBody.create(MediaType.parse("text/plain"), contact.getBranding());
                    RequestBody behaf_hariss_name_contact = RequestBody.create(MediaType.parse("text/plain"), binding.etHrName.getText().toString() + " " + binding.etHrContactnumber.getText().toString());
                    RequestBody behaf_hariss_date = RequestBody.create(MediaType.parse("text/plain"), binding.etHrDate.getText().toString());
                    RequestBody behaf_reciver_name_contact = RequestBody.create(MediaType.parse("text/plain"), binding.etBhName.getText().toString() + " " + binding.etBhContactnumber.getText().toString());
                    RequestBody behaf_reciver_date = RequestBody.create(MediaType.parse("text/plain"), binding.etBhDate.getText().toString());
                    RequestBody presence_sales_name = RequestBody.create(MediaType.parse("text/plain"), binding.etSalesName.getText().toString());
                    RequestBody presence_sales_contact = RequestBody.create(MediaType.parse("text/plain"), number);
                    RequestBody presence_lc_name = RequestBody.create(MediaType.parse("text/plain"), binding.etLcName.getText().toString());
                    RequestBody presence_lc_contact = RequestBody.create(MediaType.parse("text/plain"), binding.etLcContactnumber.getText().toString());
                    RequestBody presence_landloard_name = RequestBody.create(MediaType.parse("text/plain"), binding.etLandName.getText().toString());

                    RequestBody presence_landloard_contact = RequestBody.create(MediaType.parse("text/plain"), binding.etLandContactnumber.getText().toString());
                    RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), sales_id);
                    RequestBody ir_id = RequestBody.create(MediaType.parse("text/plain"), contact.getIr_id());

                    RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), arrcustomerR.get(0).getCustomer_id());
                    RequestBody fridge_id = RequestBody.create(MediaType.parse("text/plain"), contact.getFridge_id());
                    RequestBody crf_id = RequestBody.create(MediaType.parse("text/plain"), arrcustomerR.get(0).getCrf_id());

                    List<MultipartBody.Part> parts = new ArrayList<>();
                    MultipartBody.Part body = null;
                    MultipartBody.Part body_hr = null;
                    MultipartBody.Part body_saels = null;
                    MultipartBody.Part body_lc = null;
                    MultipartBody.Part body_land = null;
                    MultipartBody.Part body_image1 = null;
                    MultipartBody.Part body_image2 = null;
                    MultipartBody.Part body_image3 = null;
                    // add dynamic amount
                    try {
                        RequestBody reqFile_hr = RequestBody.create(MediaType.parse("image/*"), file_hr);
                        body_hr = MultipartBody.Part.createFormData("behaf_hariss_sign", file_hr.getName(), reqFile_hr);
                    } catch (Exception e) {

                    }

                    try {
                        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_bh);
                        body = MultipartBody.Part.createFormData("behaf_reciver_sign", file_bh.getName(), reqFile);
                    } catch (Exception e) {

                    }
                    try {
                        RequestBody reqFile_sales = RequestBody.create(MediaType.parse("image/*"), file_sales);
                        body_saels = MultipartBody.Part.createFormData("presence_sign", file_sales.getName(), reqFile_sales);
                    } catch (Exception e) {

                    }

                    try {
                        RequestBody reqFile_lc = RequestBody.create(MediaType.parse("image/*"), file_lc);
                        body_lc = MultipartBody.Part.createFormData("presence_lc_sign", file_lc.getName(), reqFile_lc);
                    } catch (Exception e) {

                    }

                    try {
                        RequestBody reqFile_land = RequestBody.create(MediaType.parse("image/*"), file_land);
                        body_land = MultipartBody.Part.createFormData("presence_landloard_sign", file_land.getName(), reqFile_land);
                    } catch (Exception e) {

                    }

                    try {
                        RequestBody reqFile_hr = RequestBody.create(MediaType.parse("image/*"), file_hr);
                        body_hr = MultipartBody.Part.createFormData("behaf_hariss_sign", file_hr.getName(), reqFile_hr);
                    } catch (Exception e) {

                    }

                    String image1Name = image1.substring(image1.lastIndexOf("/") + 1);
                    body_image1 = UtilApp.prepareFilePart("installed_img1", image1Name);

                    String image2Name = image2.substring(image2.lastIndexOf("/") + 1);
                    body_image2 = UtilApp.prepareFilePart("installed_img2", image2Name);

                    String image3Name = image3.substring(image3.lastIndexOf("/") + 1);
                    body_image3 = UtilApp.prepareFilePart("installed_img3", image3Name);
                    // UtilApp.logData(CustomerAgreementActivity.this, "Compatitor Feedback Request: " + mPromotion.toString());

                    final Call<JsonObject> labelResponse = ApiClient.getService().postAgreementData(method, behaf_reciver_old_signature, ms, ms_of,
                            address, asset_number, serial_number, model_branding, behaf_hariss_name_contact,
                            behaf_hariss_date, behaf_reciver_name_contact, behaf_reciver_date, presence_sales_name,
                            presence_sales_contact, presence_lc_name, presence_lc_contact, presence_landloard_name,
                            presence_landloard_contact, salesman_id, customer_id, fridge_id, ir_id, crf_id,
                            body_hr, body, body_saels, body_lc, body_land, body_image1, body_image2,body_image3);
                    labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                            Log.e("Freeze Response:", response.toString());
                            //  UtilApp.logData(SyncData.this, "Compitior Response: " + response.toString());
                            if (response.body() != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                    if (jsonObject.has("STATUS")) {
                                        if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                            Log.e("Status", jsonObject.getString("STATUS"));
                                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                                Log.e("Compititor Success", jsonObject.getString("STATUS"));
                                                System.out.println("cust-->" + contact.getIr_id());
                                                String agreement_id = jsonObject.getString("AgreementID");
                                                db.deleteChillerCustomer(String.valueOf(arrcustomerR.get(0).getCrf_id()));
                                                db.insertChillerTachniciancheck(contact.getIr_id(), contact.getFridge_id(), agreement_id);
                                                progress.dismiss();
                                                startActivity(new Intent(me, TechnicianChillerListFragment.class));
                                                finish();
                                            } else {
                                                // Fail to Post
                                                System.out.println("Fail");
                                                progress.dismiss();
                                                App.isCompititorSync = false;
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Exception--> " + e.toString());
                                    App.isCompititorSync = false;
                                    progress.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("Compititor fail:", t.toString());
                            progress.dismiss();
                            UtilApp.logData(CustomerAgreementActivity.this, "Compititoer Fail: " + t.getMessage());
                            App.isCompititorSync = false;
                        }
                    });
                }
            }
        });


    }

    private void showCustomerSignCapture() {
        final Dialog dialog = new Dialog(CustomerAgreementActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cust_sign);

        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        int width;
        width = manager.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
        Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        final SignaturePad viewSign = dialog.findViewById(R.id.signature);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final Handler handler = new Handler();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (!viewSign.isEmpty()) {
                    if (str_signature == "0") {
                        bmpSign_hr = viewSign.getSignatureBitmap();
                        binding.etHrSignature.setImageBitmap(bmpSign_hr);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                saveImage(bmpSign_hr);
                            }
                        }, 1000);

                    } else if (str_signature == "1") {
                        bmpSign_bh = viewSign.getSignatureBitmap();
                        binding.etBhSignature.setImageBitmap(bmpSign_bh);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                saveImage(bmpSign_bh);
                            }
                        }, 1000);
                    } else if (str_signature == "2") {
                        bmpSign_sales = viewSign.getSignatureBitmap();
                        binding.etSalesSignature.setImageBitmap(bmpSign_sales);
                        saveImage(bmpSign_sales);
                    } else if (str_signature == "3") {
                        bmpSign_lc = viewSign.getSignatureBitmap();
                        binding.etLcSignature.setImageBitmap(bmpSign_lc);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                saveImage(bmpSign_lc);
                            }
                        }, 1000);

                    } else if (str_signature == "4") {
                        bmpSign_land = viewSign.getSignatureBitmap();
                        binding.etLandSignature.setImageBitmap(bmpSign_land);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                saveImage(bmpSign_land);
                            }
                        }, 1000);
                    }
                    //saveImage(bmpSign_bh);
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CustomerAgreementActivity.this);
                    alertDialogBuilder
                            .setMessage("Please add signature before proceed!")
                            .setCancelable(false)
                            .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            }
        });

    }

    String str_save_image = "";
    String str_save_bh = "";
    String str_save_sales = "";
    String str_save_lc = "";
    String str_save_land = "";
    String fname_hr = "";

    /**
     * save the signature to an sd card directory
     *
     * @param signature bitmap
     */
    final void saveImage(Bitmap signature) {

        String root = Environment.getExternalStorageDirectory().toString();

        // the directory where the signature will be saved
        File myDir = new File(root + "/saved_signature");

        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // set the file name of your choice
        if (str_signature == "0") {
            //create a file to write bitmap data
            String fname_hr = "sa_signature_hr.png";
            file_hr = new File(getApplicationContext().getCacheDir(), fname_hr);
            try {
                file_hr.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

//Convert bitmap to byte array
            Bitmap bitmap = signature;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file_hr);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (str_signature == "1") {
            //create a file to write bitmap data
            String fname_bh = "sa_signature_bh.png";
            file_bh = new File(getApplicationContext().getCacheDir(), fname_bh);
            try {
                file_bh.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

//Convert bitmap to byte array
            Bitmap bitmap = signature;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file_bh);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (str_signature == "2") {
            String fname_sales = "sa_signature_sales.png";
            file_sales = new File(getApplicationContext().getCacheDir(), fname_sales);
            try {
                file_sales.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

//Convert bitmap to byte array
            Bitmap bitmap = signature;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file_sales);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (str_signature == "3") {
            String fname_lc = "sa_signature_lc.png";
            file_lc = new File(getApplicationContext().getCacheDir(), fname_lc);
            try {
                file_lc.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

//Convert bitmap to byte array
            Bitmap bitmap = signature;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file_lc);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (str_signature == "4") {
            String fname_land = "sa_signature_land.png";
            file_land = new File(getApplicationContext().getCacheDir(), fname_land);
            try {
                file_land.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

//Convert bitmap to byte array
            Bitmap bitmap = signature;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file_land);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
            Log.e("Patah", photoFile.toString());
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        mFilePath = "Riham_" + timeStamp + ".png";
        File storageDir = Environment.getExternalStoragePublicDirectory("Riham");
        if (!storageDir.exists()) {

            if (!storageDir.mkdirs()) {
                return null;
            }
        }

        File image = new File(storageDir.getPath() + File.separator +
                mFilePath);

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            if (isSelected1) {
                String filePat = UtilApp.compressImage(CustomerAgreementActivity.this, mCurrentPhotoPath);
                binding.layoutImage.setVisibility(View.GONE);
                binding.ivImage1.setVisibility(View.VISIBLE);
                image1 = filePat;
                // image1 = filePat.substring(filePat.lastIndexOf("/") + 1);
                binding.ivImage1.setImageURI(Uri.parse(image1));
            }else  if (isSelected2) {
                String filePat = UtilApp.compressImage(CustomerAgreementActivity.this, mCurrentPhotoPath);
                binding.layoutImage2.setVisibility(View.GONE);
                binding.ivImage3.setVisibility(View.VISIBLE);
                image3 = filePat;
                // image1 = filePat.substring(filePat.lastIndexOf("/") + 1);
                binding.ivImage3.setImageURI(Uri.parse(image3));
            } else {
                String filePat = UtilApp.compressImage(CustomerAgreementActivity.this, mCurrentPhotoPath);
                binding.layoutImage1.setVisibility(View.GONE);
                binding.ivImage2.setVisibility(View.VISIBLE);
                image2 = filePat;
                // image2 = filePat.substring(filePat.lastIndexOf("/") + 1);
                binding.ivImage2.setImageURI(Uri.parse(image2));
            }

//            arrImage.add(filePat);
//            arrImagePath.add(filePat.substring(filePat.lastIndexOf("/") + 1));
        }

    }

}
