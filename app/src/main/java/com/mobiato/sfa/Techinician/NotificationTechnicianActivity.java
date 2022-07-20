package com.mobiato.sfa.Techinician;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.Adapter.CustomerTechnicianAdapter;
import com.mobiato.sfa.Adapter.NotificationTechnicianAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.Techinician.fragment.TechnicianCustomerListFragment;
import com.mobiato.sfa.activity.DeliveryActivity;
import com.mobiato.sfa.activity.DownloadingDataActivity;
import com.mobiato.sfa.activity.NotificationDetailActivity;
import com.mobiato.sfa.activity.ReturnActivity;
import com.mobiato.sfa.databinding.ActivityNotificationBinding;
import com.mobiato.sfa.databinding.RowItemNotificationBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerTechnician;
import com.mobiato.sfa.model.Notification;
import com.mobiato.sfa.model.NotificationTechnician;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class NotificationTechnicianActivity extends BaseActivity {

    private ActivityNotificationBinding binding;
    private ArrayList<Notification> arrData = new ArrayList<>();
    private CommonAdapter<Notification> mAdapter;
    private DBManager db;
    ArrayList<NotificationTechnician> arrNotification=new ArrayList<>();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Notifications");

        App.countNoti = 0;
        db = new DBManager(NotificationTechnicianActivity.this);
        arrData = new ArrayList<>();
        arrData = db.getAllNotification();

        binding.fabNotification.setVisibility(View.VISIBLE);
        binding.fabNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCustomer_technician();
            }
        });

        //setData();
        getCustomer_technician();

    }
    private void getCustomer_technician() {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        //  progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        // progress.setProgress(0);
        progress.show();
        String custId = Settings.getString(App.SALESMANID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getNotificationTechnician(App.CHILLER_NOTIFICATION_TECHNICIAN, custId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Response technician", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(NotificationTechnicianActivity.this, "Customer Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<NotificationTechnician> arrCustomer = new ArrayList<>();
                        arrCustomer.clear();
                        arrCustomer = new Gson().fromJson(response.body().get("Result").getAsJsonArray().toString(),
                                new TypeToken<List<NotificationTechnician>>() {
                                }.getType());
                        //db.deleteTable(DBManager.TABLE_CUSTOMER);
                       // db.insertCustomerTachnician(arrCustomer);
                        arrNotification=arrCustomer;
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        // write all the data entered by the user in SharedPreference and apply
                        myEdit.putString("Notificationcount",String.valueOf(arrCustomer.size()));
                        myEdit.apply();
                       // System.out.println("No-->"+arrNotification.get(0).getSchudule_date());
                        setMainAdapter();
                    }
                } else {
                    UtilApp.logData(NotificationTechnicianActivity.this, "Customer Response: Blank");
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                progress.dismiss();
                UtilApp.logData(NotificationTechnicianActivity.this, "Customer Failure: " + error.getMessage());
            }
        });
    }
    private NotificationTechnicianAdapter mAdapter1;
    private void setMainAdapter() {

        mAdapter1 = new NotificationTechnicianAdapter(this, arrNotification, new NotificationTechnicianAdapter.ContactsAdapterListener() {
            @Override
            public void onContactSelected(NotificationTechnician contact) {

            }
        });

        binding.rvList.setAdapter(mAdapter1);
    }
}
