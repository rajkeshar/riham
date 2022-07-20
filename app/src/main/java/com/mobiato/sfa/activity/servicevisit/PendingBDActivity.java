package com.mobiato.sfa.activity.servicevisit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.MasterDataActivity;
import com.mobiato.sfa.databinding.ActivityPendingBdactivityBinding;
import com.mobiato.sfa.databinding.ActivitySettingBinding;
import com.mobiato.sfa.databinding.RowTicketItemBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.NatureMaster;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class PendingBDActivity extends BaseActivity implements View.OnClickListener {

    public ActivityPendingBdactivityBinding binding;
    private ArrayList<NatureMaster> arrNatature = new ArrayList<>();
    private DBManager db;
    private CommonAdapter<NatureMaster> mNatureAdapter;
    private LoadingSpinner progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPendingBdactivityBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Pending BD Tickets");
        setNavigationView();

        db = new DBManager(this);
        progressDialog = new LoadingSpinner(PendingBDActivity.this);

        arrNatature = new ArrayList<>();
        arrNatature = db.getNatureData();
        setNatureAdapter();

        binding.fab.setOnClickListener(this);

    }

    private void setNatureAdapter() {

        mNatureAdapter = new CommonAdapter<NatureMaster>(arrNatature) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowTicketItemBinding) {
                    NatureMaster mData = arrNatature.get(position);
                    ((RowTicketItemBinding) holder.binding).setItem(mData);

                    String reStatus = db.getResolutionStatus(mData.getTicket_no());

                    if (!reStatus.equals("")) {
                        ((RowTicketItemBinding) holder.binding).tvResolution.setVisibility(View.VISIBLE);
                        ((RowTicketItemBinding) holder.binding).tvResolution.setText("Resolution Status: " + reStatus);
                    } else {
                        ((RowTicketItemBinding) holder.binding).tvResolution.setVisibility(View.GONE);
                    }

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_ticket_item;
            }
        };

        binding.rvList.setAdapter(mNatureAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getNature();
                break;
        }
    }

    private void getNature() {

        progressDialog.show();

        String custId = Settings.getString(App.SALESMANID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getNatureOfCallList(App.NATURE_OF_CAL_MASTER, custId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Good Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(PendingBDActivity.this, "NatureMaster Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<NatureMaster> arrFridge = new ArrayList<>();
                        if (response.body().get("RESULT").getAsJsonArray() != null) {
                            arrFridge = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                    new TypeToken<List<NatureMaster>>() {
                                    }.getType());
                            db.insertNatureMaster(arrFridge);
                        }
                    }
                } else {
                    UtilApp.logData(PendingBDActivity.this, "NatureMaster Response: Blank");
                }

                progressDialog.hide();

                arrNatature = new ArrayList<>();
                arrNatature = db.getAllNatureData();
                setNatureAdapter();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Good Fail", error.getMessage());
                progressDialog.hide();
                UtilApp.logData(PendingBDActivity.this, "Free Good Failure: " + error.getMessage());
            }
        });
    }

}