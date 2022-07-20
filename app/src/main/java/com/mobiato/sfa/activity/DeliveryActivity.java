package com.mobiato.sfa.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityDeliveryBinding;
import com.mobiato.sfa.databinding.RowItemDeliveryBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Delivery;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryActivity extends BaseActivity {

    private ActivityDeliveryBinding binding;
    private Customer mCustomer;
    private ArrayList<Delivery> arrDelivery = new ArrayList<>();
    private CommonAdapter<Delivery> mAdapter;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Delivery");

        if (getIntent() != null) {
            mCustomer = (Customer) getIntent().getSerializableExtra("customer");
        }

        db = new DBManager(DeliveryActivity.this);
        arrDelivery = db.getAllDelivery(mCustomer.getCustomerId());

        setAdapter();

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDelivery();
            }
        });

    }

    private void getDelivery() {
        final Call<JsonObject> labelResponse = ApiClient.getService().getDeliveryList(App.GET_DELIVERY, Settings.getString(App.SALESMANID));

        labelResponse.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("Delivery Response", response.body().toString());
                binding.swipeRefreshLayout.setRefreshing(false);
                UtilApp.logData(DeliveryActivity.this, "Delivery Response" + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Delivery> arrDelivery = new ArrayList<>();
                        arrDelivery = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Delivery>>() {
                                }.getType());
                        db.insertDeliveryItems(arrDelivery);

                        //Set Delivery Data
                        setDeliveryData();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Delivery Fail", error.getMessage());
                UtilApp.logData(DeliveryActivity.this, "Delivery Fails");
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    private void setDeliveryData() {
        arrDelivery = new ArrayList<>();
        arrDelivery = db.getAllDelivery(mCustomer.getCustomerId());
        setAdapter();
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Delivery>(arrDelivery) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemDeliveryBinding) {
                    Delivery mItem = arrDelivery.get(position);
                    ((RowItemDeliveryBinding) holder.binding).setItem(mItem);

                    if (mItem.getIsDelete().equalsIgnoreCase("1")) {
                        ((RowItemDeliveryBinding) holder.binding).imgVerified.setVisibility(View.VISIBLE);
                        ((RowItemDeliveryBinding) holder.binding).imgVerified.setImageResource(R.drawable.ic_icon_block_sel);
                    } else if (mItem.getIsDelete().equalsIgnoreCase("2")) {
                        ((RowItemDeliveryBinding) holder.binding).imgVerified.setVisibility(View.VISIBLE);
                        ((RowItemDeliveryBinding) holder.binding).imgVerified.setImageResource(R.drawable.ic_icon_verified_sel);
                    } else {
                        ((RowItemDeliveryBinding) holder.binding).imgVerified.setVisibility(View.GONE);
                    }

                    holder.binding.executePendingBindings();


                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mItem.getIsDelete().equalsIgnoreCase("0")) {
                                startActivity(new Intent(DeliveryActivity.this, DeliveryConfirmActivity.class)
                                        .putExtra("orderNo", mItem.getOrderNo())
                                        .putExtra("orderId", mItem.getOrderId())
                                        .putExtra("customer", mCustomer));
                            }
                        }
                    });

                    holder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeliveryActivity.this);
                            alertDialogBuilder.setTitle("Delete")
                                    .setMessage("Are you sure you want to delete this delivery?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            //Update delivery Delete
                                            db.deliveryDeleteStatus(mItem.getOrderId());
                                            arrDelivery.get(position).setIsDelete("1");
                                            notifyDataSetChanged();

                                            dialog.dismiss();

                                            if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                                                UtilApp.createBackgroundJob(getApplicationContext());
                                            }

                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            // show it
                            alertDialog.show();
                            return false;
                        }
                    });
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_delivery;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

}
