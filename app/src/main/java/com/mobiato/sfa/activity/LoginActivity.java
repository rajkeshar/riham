package com.mobiato.sfa.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.BuildConfig;
import com.mobiato.sfa.Fragments.DashboardFragment;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityLoginBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Pricing;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private LoadingSpinner progressDialog;
    private String username = "", password = "", latitude = "", longitude = "";
    private DBManager db;

    private FusedLocationProviderClient fusedLocationClient;
    private Location mSalesmanLC, mDepotLC;
    private Salesman mSalesman;

    private MaterialShowcaseSequence sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setFullScreen();

        if (Settings.getString(App.TOKEN) == null) {
            getToken();
        } else if (Settings.getString(App.TOKEN).equalsIgnoreCase("")) {
            getToken();
        }

        sequence = new MaterialShowcaseSequence(LoginActivity.this);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        sequence.setConfig(config);

        sequence.addSequenceItem(binding.btnLogin,
                "Enter Username & Password. \n\nThen click on Login button", "GOT IT");

        sequence.start();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        UtilApp.logData(LoginActivity.this, "On Login Screen");

        progressDialog = new LoadingSpinner(LoginActivity.this);
        db = new DBManager(LoginActivity.this);

//        setAppInfo();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mSalesmanLC = location;
                            latitude = String.valueOf(mSalesmanLC.getLatitude());
                            longitude = String.valueOf(mSalesmanLC.getLongitude());
                        }
                    }
                });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginView();
            }
        });


       /* binding.username.setText("SM00147");
        binding.password.setText("SM00147");

        loginView();*/

    }

    public void getToken() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase :", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Settings.setString(App.TOKEN, token);
                        System.out.println("TOKEN--> " + token);
                        // Log and toast
                        Log.d("Firebase :", "Token: " + token);
                    }
                });
    }

    public void setAppInfo() {

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("App Ver: ");
        sb.append(pInfo.versionName);
        sb.append("\t \t");
        sb.append("Build: ");
        sb.append(App.ENVIRONMENT);

        binding.tvAppinfo.setText(sb.toString());
    }

    private void loginView() {
        username = binding.username.getText().toString();
        password = binding.password.getText().toString();

        UtilApp.logData(LoginActivity.this, "Login Credentials for user:" + username + "/" + password);

        if (username.isEmpty()) {
            binding.username.requestFocus();
            binding.username.setError(getResources().getString(R.string.enter_username));
        } else if (password.isEmpty()) {
            binding.password.requestFocus();
            binding.password.setError(getResources().getString(R.string.enter_password));
        } else {
            progressDialog.show();
            if (UtilApp.isNetworkAvailable(this)) {
                UtilApp.logData(LoginActivity.this, "Network Available. Logging in user");
                callLoginAPI();
            } else {
                if (db.checkSalesmanExits(username, password)) {
                    progressDialog.hide();
                    Settings.setString(App.SALESMANID, mSalesman.getSalesmanId());
                    Settings.setString(App.IS_DATA_SYNCING, "false");
                    Settings.setString(App.ISLOGIN, "true");
                    Settings.setString(App.ROUTEID, mSalesman.getRoute());
                    Settings.setString(App.SALESMANNAME, mSalesman.getSalesmanName());
                    Settings.setString(App.SALESMANINO, mSalesman.getSalesmanCode());
                    Settings.setString(App.SALESMANID, mSalesman.getSalesmanId());
                    Settings.setString(App.SALESMANCONTACT, mSalesman.getContactNo());
                    Settings.setString(App.DEPOTID, mSalesman.getDepot());
                    Intent intent = new Intent(getApplicationContext(), DashboardFragment.class);
                    startActivityForResult(intent, 0);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    progressDialog.hide();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertDialogBuilder.setTitle(R.string.internet_available_title)
                            .setMessage(R.string.internet_available_msg)
                            .setCancelable(false)
                            .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.hide();
                                    }
                                    dialog.dismiss();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            }

        }
    }

    private void callLoginAPI() {

        String time = UtilApp.getCurrent12Time();

        String versionName = BuildConfig.VERSION_NAME;

        final Call<JsonObject> labelResponse = ApiClient.getService().getLogin(App.LOGIN, username, password, Settings.getString(App.TOKEN), time, latitude, longitude, versionName);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                progressDialog.hide();
                Log.e("Login Response", response.toString());
                System.out.println("Link-->" + response.raw().request().url());
                if (response.body() != null) {
                    try {
                        UtilApp.logData(LoginActivity.this, "Login Response : " + response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {

                                String loggId = "";
                                if (jsonObject.has("Logged_id")) {
                                    loggId = jsonObject.getString("Logged_id");
                                    Settings.setString(App.LOGGID, loggId);
                                }

                                mSalesman = new Gson().fromJson(jsonObject.getJSONObject("RESULT").toString(),
                                        new TypeToken<Salesman>() {
                                        }.getType());

                                if (!mSalesman.getDepotLatitude().equalsIgnoreCase("")) {
                                    mDepotLC = new Location("Depot");
                                    mDepotLC.setLatitude(Double.parseDouble(mSalesman.getDepotLatitude()));
                                    mDepotLC.setLongitude(Double.parseDouble(mSalesman.getDepotLongitude()));
                                }

                                //Insert Salesman Data
                                db.insertSalesman(mSalesman);

                                System.out.println("R--> " + mSalesman.getDepot());
                                Settings.setString(App.SALESMANID, mSalesman.getSalesmanId());
                                Settings.setString(App.SALESMANNAME, mSalesman.getSalesmanName());
                                Settings.setString(App.SALESMANINO, mSalesman.getSalesmanCode());

                                Settings.setString(App.ROUTEID, mSalesman.getRoute());
                                Settings.setString(App.DEPOTID, mSalesman.getDepot());
                                Settings.setString(App.AGENTID, mSalesman.getAgent());
                                Settings.setString(App.ROLE, mSalesman.getRole());
                                if (db.isSyncCompleteTransaction()) {
                                    Settings.setString(App.INVOICE_LAST, mSalesman.getInvLast());
                                }
                                Settings.setString(App.SALESMANCONTACT, mSalesman.getContactNo());

                                Settings.setString(App.ORDER_LAST, mSalesman.getOrderLast());
                                Settings.setString(App.COLLECTION_LAST, mSalesman.getCollectionLast());
                                Settings.setString(App.RETURN_LAST, mSalesman.getReturnLast());
                                Settings.setString(App.LOAD_LAST, mSalesman.getLoadLast());
                                Settings.setString(App.UNLOAD_LAST, mSalesman.getUnLoadLast());
                                Settings.setString(App.CUSTOMER_LAST, mSalesman.getCustomerLast());
                                Settings.setString(App.EXCHANGE_LAST, mSalesman.getExchangeLast());
                                Settings.setString(App.IS_LOAD_VERIFY, "0");

                                if (mSalesman.getGet_attendance() != null) {
                                    if (mSalesman.getGet_attendance().equals("1")) {
                                        Settings.setString(App.IS_ATTENDANCE_IN, "1");
                                    } else {
                                        Settings.setString(App.IS_ATTENDANCE_IN, "0");
                                    }
                                } else {
                                    Settings.setString(App.IS_ATTENDANCE_IN, "0");
                                }
                                Settings.setString(App.IS_ATTENDANCE_OUT, "0");
                                Settings.setString(App.IS_ENDDAY, "0");
                                Settings.setString(App.IS_SALE_POSTED, "0");
                                Settings.setString(App.IS_BADCAPTURE, "0");
                                Settings.setString(App.PROJECT_ID, mSalesman.getProjectid());
                                Settings.setString(App.DEPOTNAME, mSalesman.getDepotName());
                                Settings.setString(App.DEPOTTIN, mSalesman.getDepotTIN());
                                Settings.setString(App.DEPOTVILLAGE, mSalesman.getDepotVillage());
                                Settings.setString(App.DEPOTCITY, mSalesman.getDepotCity());
                                Settings.setString(App.DEPOTSTREET, mSalesman.getDepotStreet());
                                Settings.setString(App.DEPOTPHONE, mSalesman.getDepotPhone());
                                Settings.setString(App.SALE_TARGET, mSalesman.getSalesTarget());
                                Settings.setString(App.ACHIVE_TARGET, mSalesman.getAchiveTarget());
                                Settings.setSalesmanData(App.SALESMAN, mSalesman);
                                Settings.setString(App.ASSOCIATED_ROUTEID, mSalesman.getAssignRoute());

                                Settings.setString(App.PM_ServiceLast, "1");
                                Settings.setString(App.BD_ServiceLast, "1");
                                Settings.setString(App.BD_Done_ServiceLast, "1");
                                Settings.setString(App.BD_Pending_ServiceLast, "1");
                                Settings.setString(App.CA_ServiceLast, "1");
                                Settings.setString(App.SA_ServiceLast, "1");

                                if (mSalesman.getIsPriceUpdate() != null) {
                                    if (mSalesman.getIsPriceUpdate().equals("1")) {
                                        Log.e("FlagPrice", "1");
                                        db.deleteTable(db.TABLE_AGENT_PRICING);
                                        updatePriceFlag(mSalesman.getSalesmanId());
                                    }
                                }

//                                if (mSalesman.getRole().equals("6") || mSalesman.getRole().equals("7") ||
//                                        mSalesman.getRole().equals("8") || mSalesman.getRole().equals("9") ||
//                                        mSalesman.getRole().equals("10")) {
//                                    Intent intent = new Intent(getApplicationContext(), DepotListActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                                } else {

                                Settings.setString(App.IS_DATA_SYNCING, "false");
                                Settings.setString(App.ISLOGIN, "true");

                                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {

                                    Settings.setString(App.CUSTOMERFILE, mSalesman.getCustomer_file());
                                    Settings.setString(App.ITEMFILE, mSalesman.getItem_file());
                                    Settings.setString(App.PRICEFILE, mSalesman.getPrice_file());

                                    if (Settings.getString(App.ROLE).equals("6")) {
                                        Settings.setString(App.PROJECT_NAME, mSalesman.getProjectname());
                                    } else {
                                        Settings.setString(App.PROJECT_NAME, "");
                                    }
                                    new RetrieveFeedTask().execute(mSalesman.getCustomer_file());
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), DownloadingDataActivity.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                }
                                //   }

//                                Intent in = new Intent(LoginActivity.this, SimpleScannerActivity.class);
//                                startActivityForResult(in, App.REQUEST_BARCODE);

                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.hide();
                                }

                                UtilApp.displayErrorDialog(LoginActivity.this, jsonObject.getString("MESSAGE"));

                            }

                            // new RetrieveFeedTask().execute();
                        }
                    } catch (Exception e) {
                        Log.e("Msg", e.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                progressDialog.hide();
                Log.e("Login Fail", error.getMessage());
                UtilApp.logData(LoginActivity.this, "Login Response Fail: API FAIL");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case App.REQUEST_BARCODE:
                if (resultCode == 2) {
                    if (data.getStringExtra("code").equalsIgnoreCase("123456")) {
                        //if (data.getStringExtra("code").equalsIgnoreCase(mSalesman.getDepotBarcode())) {

                        if (mSalesmanLC != null && mDepotLC != null) {
                            float distance = getRadius(mSalesmanLC, mDepotLC);

                            if (!mSalesman.getThreshold_ORG().equalsIgnoreCase("")) {
                                if (distance > Double.parseDouble(mSalesman.getThreshold_ORG())) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                                    alertDialogBuilder.setTitle("Alert")
                                            .setMessage("You are not in depot location!")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    openActivity();
                                                }
                                            });
                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    // show it
                                    alertDialog.show();
                                } else {
                                    openActivity();
                                }
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                                alertDialogBuilder.setTitle("Alert")
                                        .setMessage("Depot Thresheold not define!")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                openActivity();
                                            }
                                        });
                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                // show it
                                alertDialog.show();
                            }

                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                            alertDialogBuilder.setTitle("Alert")
                                    .setMessage("Salesman & Depot Location not found!")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            openActivity();
                                        }
                                    });
                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            // show it
                            alertDialog.show();
                        }

                    } else {
                        UtilApp.displayErrorDialog(LoginActivity.this, "Depot Barcode does not match!");
                    }

                }
                break;
            default:
                break;
        }
    }

    public void openActivity() {
        //Insert Salesman Data
        db.insertSalesman(mSalesman);
        Settings.setString(App.SALESMANID, mSalesman.getSalesmanId());
        Settings.setString(App.SALESMANNAME, mSalesman.getSalesmanName());
        Settings.setString(App.SALESMANINO, mSalesman.getSalesmanCode());
        Settings.setString(App.IS_DATA_SYNCING, "false");
        Settings.setString(App.ISLOGIN, "true");
        Settings.setString(App.ROUTEID, mSalesman.getRoute());
        Settings.setString(App.DEPOTID, mSalesman.getDepot());
        Settings.setString(App.AGENTID, mSalesman.getAgent());
        Settings.setString(App.ROLE, mSalesman.getRole());
        Settings.setString(App.INVOICE_LAST, mSalesman.getInvLast());
        Settings.setString(App.ORDER_LAST, mSalesman.getOrderLast());
        Settings.setString(App.COLLECTION_LAST, mSalesman.getCollectionLast());
        Settings.setString(App.RETURN_LAST, mSalesman.getReturnLast());
        Settings.setString(App.LOAD_LAST, mSalesman.getLoadLast());
        Settings.setString(App.UNLOAD_LAST, mSalesman.getUnLoadLast());
        Settings.setString(App.CUSTOMER_LAST, mSalesman.getCustomerLast());
        Settings.setString(App.EXCHANGE_LAST, mSalesman.getExchangeLast());
        Settings.setString(App.IS_LOAD_VERIFY, "0");
        Settings.setString(App.IS_ENDDAY, "0");
        Settings.setString(App.IS_SALE_POSTED, "0");
        Settings.setString(App.IS_BADCAPTURE, "0");
        Settings.setString(App.DEPOTNAME, mSalesman.getDepotName());
        Settings.setString(App.DEPOTTIN, mSalesman.getDepotTIN());
        Settings.setString(App.DEPOTVILLAGE, mSalesman.getDepotVillage());
        Settings.setString(App.DEPOTCITY, mSalesman.getDepotCity());
        Settings.setString(App.DEPOTSTREET, mSalesman.getDepotStreet());
        Settings.setString(App.DEPOTPHONE, mSalesman.getDepotPhone());
        Settings.setString(App.SALE_TARGET, mSalesman.getSalesTarget());
        Settings.setString(App.ACHIVE_TARGET, mSalesman.getAchiveTarget());
        Settings.setSalesmanData(App.SALESMAN, mSalesman);
        Settings.setString(App.ASSOCIATED_ROUTEID, mSalesman.getAssignRoute());

        Intent intent = new Intent(getApplicationContext(), DownloadingDataActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    private float getRadius(Location currentLocation, Location depotLocation) {

        float distance = currentLocation.distanceTo(depotLocation);

        return distance;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    mSalesmanLC = location;
                                }
                            }
                        });
                break;
        }
    }

    public void updatePriceFlag(String salesmanId) {

        final Call<JsonObject> labelResponse = ApiClient.getService().getUpdatePrice(App.UPDATE_PRICE, salesmanId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                // progressDialog.hide();
                UtilApp.logData(LoginActivity.this, "PriceUpdate Response Success");
                Log.e("PriceUpdate Response", response.toString());
                System.out.println("Link-->" + response.raw().request().url());
                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                // progressDialog.hide();
                Log.e("PriceUpdate Fail", error.getMessage());
                UtilApp.logData(LoginActivity.this, "PriceUpdate Response Fail: API FAIL");
            }
        });
    }

    private File downloadFile(String fileUrl, String fileName) {

        File mFIle = null;
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/Riham/master");
            if (dir.exists() == false) {
                dir.mkdirs();
            }


            String mUrl = App.BASE_URL + "API/" + fileUrl;
            URL url = new URL(mUrl);
            File file = new File(dir, fileName);

            mFIle = file;

            long startTime = System.currentTimeMillis();
            Log.d("DownloadManager", "download url:" + url);
            Log.d("DownloadManager", "download file name:" + fileName);

            URLConnection uconn = url.openConnection();
//            uconn.setReadTimeout(TIMEOUT_CONNECTION);
//            uconn.setConnectTimeout(TIMEOUT_SOCKET);

            InputStream is = uconn.getInputStream();
            BufferedInputStream bufferinstream = new BufferedInputStream(is);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            //We create an array of bytes
            byte[] data = new byte[50];
            int current = 0;

            while ((current = bufferinstream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, current);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer.toByteArray());
            fos.flush();
            fos.close();
            Log.d("DownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + "sec");
            int dotindex = fileName.lastIndexOf('.');
            if (dotindex >= 0) {
                fileName = fileName.substring(0, dotindex);
            }

        } catch (IOException e) {
            Log.d("DownloadManager", "Error:" + e);
        }

        return mFIle;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        protected String doInBackground(String... urls) {
            try {

                File mFIle = downloadFile(mSalesman.getCustomer_file(), "customer.txt");

                String custData = readfile(mFIle);
                try {
                    JSONArray obj = new JSONArray(custData);
                    Log.d("Customer Data", obj.toString());

                    ArrayList<Customer> arrCustomer = new ArrayList<>();
                    arrCustomer = new Gson().fromJson(obj.toString(),
                            new TypeToken<List<Customer>>() {
                            }.getType());
                    db.deleteTable(DBManager.TABLE_DEPOT_CUSTOMER);
                    String mtotalCOunt = String.valueOf(arrCustomer.size());
                    db.deleteTable(db.TABLE_DEPOT_CUSTOMER_COUNT);
                    String dept = Settings.getString(App.DEPOTID);
                    db.insertDepotCustomerCount(mtotalCOunt, dept);
                    db.insertDept(arrCustomer);

                } catch (Throwable t) {
                }

                if (Integer.parseInt(db.getMaterialCount()) == 0) {

                    File mIFIle = downloadFile(mSalesman.getItem_file(), "item.txt");
                    String itemData = readfile(mIFIle);

                    try {
                        JSONArray obj = new JSONArray(itemData);
                        Log.d("Item Data", obj.toString());

                        ArrayList<Item> arrItem = new ArrayList<>();
                        arrItem = new Gson().fromJson(obj.toString(),
                                new TypeToken<List<Item>>() {
                                }.getType());
                        Log.e("Item Count:", String.valueOf(arrItem.size()));
                        db.deleteTable(DBManager.TABLE_ITEM);
                        db.insertItem(arrItem);
                    } catch (Throwable t) {
                    }
                }

                File mPRFIle = downloadFile(mSalesman.getPrice_file(), "pricing.txt");
                String priceData = readfile(mPRFIle);
                try {
                    JSONArray obj = new JSONArray(priceData);
                    Log.d("Price Data", obj.toString());

                    String routeId = Settings.getString(App.ROUTEID);

                    ArrayList<Pricing> arrPricing = new ArrayList<>();
                    arrPricing = new Gson().fromJson(obj.toString(),
                            new TypeToken<List<Pricing>>() {
                            }.getType());
                    db.deleteTable(db.TABLE_AGENT_PRICE_COUNT);
                    db.insertAgentPricingCount(arrPricing, routeId);
                    db.deleteTable(db.TABLE_AGENT_PRICING);
                    db.insertAgentPricingItems(arrPricing, routeId);

                } catch (Throwable t) {

                }

            } catch (Exception e) {
                this.exception = e;
                return null;
            } finally {
                return "";
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            progressDialog.hide();

            Intent intent = new Intent(getApplicationContext(), DownloadingDataActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        }
    }

    public String readfile(File file) {
        // File file = new File(Environment.getExternalStorageDirectory(), "mytextfile.txt");
        StringBuilder builder = new StringBuilder();
        Log.e("main", "read start");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            br.close();
        } catch (Exception e) {
            Log.e("main", " error is " + e.toString());
        }
        Log.e("main", " read text is " + builder.toString());
        return builder.toString();
    }
}
