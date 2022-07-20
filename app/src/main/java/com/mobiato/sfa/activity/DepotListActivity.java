package com.mobiato.sfa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityDepotListBinding;
import com.mobiato.sfa.databinding.RowItemDepotBinding;
import com.mobiato.sfa.databinding.RowItemPromotionMasterBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.merchandising.InventoryCheckActivity;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Depot;
import com.mobiato.sfa.model.PromoOfferData;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class DepotListActivity extends BaseActivity {

    public ActivityDepotListBinding binding;
    private LoadingSpinner progressDialog;
    ArrayList<Depot> arrDepot = new ArrayList<>();
    private CommonAdapter<Depot> mAdapter;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDepotListBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Select Depot");

        progressDialog = new LoadingSpinner(DepotListActivity.this);

        getDepotList();


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedPosition != -1) {
                    UtilApp.confirmationDialog("Are you sure you want to proceed?", DepotListActivity.this, new OnSearchableDialog() {
                        @Override
                        public void onItemSelected(Object o) {
                            String selection = (String) o;
                            if (selection.equalsIgnoreCase("yes")) {

                                String routeId = arrDepot.get(selectedPosition).getRoute_id();
                                String depotId = arrDepot.get(selectedPosition).getId();
                                String agentId = arrDepot.get(selectedPosition).getAgent_id();
                                String depotName = arrDepot.get(selectedPosition).getDepot_name();

                                Settings.setString(App.IS_DATA_SYNCING, "false");
                                Settings.setString(App.ISLOGIN, "true");
                                Settings.setString(App.ROUTEID, routeId);
                                Settings.setString(App.DEPOTID, depotId);
                                Settings.setString(App.AGENTID, agentId);
                                Settings.setString(App.DEPOTNAME, depotName);

                                Intent intent = new Intent(getApplicationContext(), DownloadingDataActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }
                        }
                    });
                } else {
                    Toast.makeText(DepotListActivity.this, "Please select depot first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getDepotList() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getItem(App.ALL_DEPOT);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Depot Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DepotListActivity.this, "Depot Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        arrDepot = new ArrayList<>();
                        arrDepot = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Depot>>() {
                                }.getType());

                    }
                }

                progressDialog.hide();
                setAdapter();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Depot Fail", error.getMessage());
                progressDialog.hide();
                UtilApp.logData(DepotListActivity.this, "Depot Failure: " + error.getMessage());
                arrDepot = new ArrayList<>();
                setAdapter();
            }
        });
    }

    private void setAdapter() {

        mAdapter = new CommonAdapter<Depot>(arrDepot) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemDepotBinding) {
                    Depot mData = arrDepot.get(position);

                    ((RowItemDepotBinding) holder.binding).setItem(mData);

                    if (mData.getIsSelect() == null) {
                        ((RowItemDepotBinding) holder.binding).imgVerified.setVisibility(View.GONE);
                    } else {
                        if (mData.getIsSelect().equals("1")) {
                            ((RowItemDepotBinding) holder.binding).imgVerified.setVisibility(View.VISIBLE);
                        } else {
                            ((RowItemDepotBinding) holder.binding).imgVerified.setVisibility(View.GONE);
                        }
                    }

                    ((RowItemDepotBinding) holder.binding).view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (selectedPosition != -1) {
                                arrDepot.get(selectedPosition).setIsSelect("0");
                                selectedPosition = position;
                                arrDepot.get(position).setIsSelect("1");
                                notifyDataSetChanged();
                            } else {
                                selectedPosition = position;
                                arrDepot.get(position).setIsSelect("1");
                                notifyDataSetChanged();
                            }
                        }
                    });

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_depot;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

}