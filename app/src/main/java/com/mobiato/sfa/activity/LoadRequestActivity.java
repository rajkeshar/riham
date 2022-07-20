package com.mobiato.sfa.activity;

import android.content.Intent;
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
import com.mobiato.sfa.databinding.ActivityLoadRequestBinding;
import com.mobiato.sfa.databinding.RowItemLoadRequestBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.DepotData;
import com.mobiato.sfa.model.Order;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class LoadRequestActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoadRequestBinding binding;
    public ArrayList<Order> arrData = new ArrayList<>();
    private CommonAdapter<Order> mAdapter;
    private DBManager db;
    private LoadingSpinner progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadRequestBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle(getString(R.string.nav_loadRequest));
        setNavigationView();

        db = new DBManager(LoadRequestActivity.this);
        progressDialog = new LoadingSpinner(LoadRequestActivity.this);

        arrData = new ArrayList<>();
        arrData = db.getAllLoadRequest(Settings.getString(App.SALESMANID));

        if (arrData.size() > 0) {
            binding.txtNoContain.setVisibility(View.GONE);
            binding.rvList.setVisibility(View.VISIBLE);
        } else {
            binding.txtNoContain.setVisibility(View.VISIBLE);
            binding.rvList.setVisibility(View.GONE);
        }

        setAdapter();

        binding.fab.setOnClickListener(this);
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Order>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {

                if (holder.binding instanceof RowItemLoadRequestBinding) {
                    ((RowItemLoadRequestBinding) holder.binding).setItem(arrData.get(position));
                    holder.binding.executePendingBindings();

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(LoadRequestActivity.this, OrderDetailActivity.class);
                            in.putExtra("type", "Load");
                            in.putExtra("orderNo", arrData.get(position).orderNo);
                            startActivity(in);
                        }
                    });
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_load_request;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (Integer.parseInt(db.getAgentCount()) > 0) {
                    openActivity();
                } else {
                    getAgentList();
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        db = new DBManager(LoadRequestActivity.this);
        arrData = new ArrayList<>();
        arrData = db.getAllLoadRequest(Settings.getString(App.SALESMANID));

        if (arrData.size() > 0) {
            binding.txtNoContain.setVisibility(View.GONE);
            binding.rvList.setVisibility(View.VISIBLE);
        } else {
            binding.txtNoContain.setVisibility(View.VISIBLE);
            binding.rvList.setVisibility(View.GONE);
        }
        setAdapter();
    }

    //Deport AGENT
    private void getAgentList() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getDeliveryList(App.GET_AGENT_LIST, Settings.getString(App.SALESMANID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Agent List Response", response.toString());

                if (response.body() != null) {
                    UtilApp.logData(LoadRequestActivity.this, "Agent List Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<DepotData> arrData = new ArrayList<>();
                        arrData = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<DepotData>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_DEPOT);
                        db.insertDepot(arrData);
                    }
                } else {
                    UtilApp.logData(LoadRequestActivity.this, "Agent List Response: Blank");
                }

                progressDialog.hide();
                openActivity();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Agent List Fail", error.getMessage());
                progressDialog.hide();
                openActivity();
                UtilApp.logData(LoadRequestActivity.this, "Agent List Fail: " + error.getMessage());
            }
        });

    }

    private void openActivity() {
        Intent in = new Intent(LoadRequestActivity.this, OrderRequestActivity.class);
        in.putExtra("Type", "Load");
        in.putExtra("cust_code", "");
        startActivity(in);
    }

}
