package com.mobiato.sfa.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityDiscountMasterBinding;
import com.mobiato.sfa.databinding.RowDiscountHeaderBinding;
import com.mobiato.sfa.databinding.RowItemCatDiscountBinding;
import com.mobiato.sfa.databinding.RowItemDiscountBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Discount;
import com.mobiato.sfa.model.DiscountData;
import com.mobiato.sfa.model.DiscountHeader;
import com.mobiato.sfa.model.DiscountMaster;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;

public class DiscountMasterActivity extends BaseActivity implements View.OnClickListener {

    private ActivityDiscountMasterBinding binding;
    public String[] arrType = {"Item Discount", "Route Category Discount",
            "Customer Item Discount"};
    public String type = "";
    private ArrayList<DiscountMaster> arrDiscount = new ArrayList<>();
    private CommonAdapter<DiscountMaster> mAdapter;
    private ArrayList<DiscountHeader> arrData = new ArrayList<>();
    private CommonAdapter<DiscountHeader> mDisAdapter;
    private DBManager db;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private LoadingSpinner progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiscountMasterBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Discount Master");

        db = new DBManager(DiscountMasterActivity.this);
        progressDialog = new LoadingSpinner(DiscountMasterActivity.this);

        binding.lytDepot.setOnClickListener(this);
        binding.fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lytDepot:
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(DiscountMasterActivity.this);
                builder.setTitle("Select Discount");
                builder.setItems(arrType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etType.setText(arrType[which]);
                        if (which == 0) {
                            type = "item";
                        } else if (which == 1) {
                            type = "category";
                        } else if (which == 2) {
                            type = "customer";
                        }

                        getDiscountData(type);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.fab:
                getItemDiscount();
                break;
        }
    }

    private void getDiscountData(String type) {

        if (type.equalsIgnoreCase("item")) {
            arrData = new ArrayList<>();
            arrData = db.getDiscountHeader("3");
            setAdapter();
        } else if (type.equalsIgnoreCase("category")) {
            arrData = new ArrayList<>();
            arrData = db.getDiscountHeader("2");
            setAdapter();
        } else if (type.equalsIgnoreCase("customer")) {
            arrData = new ArrayList<>();
            arrData = db.getDiscountHeader("1");
            setAdapter();
        }
    }

    private void setAdapter() {
        mDisAdapter = new CommonAdapter<DiscountHeader>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowDiscountHeaderBinding) {

                    DiscountHeader mData = arrData.get(position);

                    ((RowDiscountHeaderBinding) holder.binding).tvCategory.setText("Discount Name: " + mData.getName());
                    ((RowDiscountHeaderBinding) holder.binding).tvQty.setText("Priority: " + mData.getPriority());
                    ((RowDiscountHeaderBinding) holder.binding).tvQty.setVisibility(View.GONE);
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_discount_header;
            }
        };

        binding.rvList.setAdapter(mDisAdapter);
    }

    private void getItemDiscount() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.ROUTE_ITEM_DISCOUNT);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Item Discount Response", response.toString());

                if (response.body() != null) {
                    UtilApp.logData(DiscountMasterActivity.this, "Item Discount Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<DiscountData> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<DiscountData>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_DISCOUNT_MAIN_HEADER);
                        db.deleteTable(db.TABLE_DISCOUNT_SITEM);
                        db.deleteTable(db.TABLE_DISCOUNT_SLAB);
                        db.insertItemDiscount(arrItems);
                    }
                } else {
                    UtilApp.logData(DiscountMasterActivity.this, "Item Discount Response: Blank");
                }

                getCustomerDiscount();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Item Discount Fail", error.getMessage());
                progressDialog.hide();
                UtilApp.logData(DiscountMasterActivity.this, "Item Discount Fail: " + error.getMessage());
            }
        });
    }

    private void getCustomerDiscount() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomerDiscountList(App.CUSTOMER_ITEM_DISCOUNT, Settings.getString(App.ROUTEID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Discount Response", response.body().toString());
                UtilApp.logData(DiscountMasterActivity.this, "Customer Discount Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<DiscountData> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<DiscountData>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_DISCOUNT_CUSTOMER_HEADER);
                        db.insertCustomerItemDiscount(arrItems);
                    }
                }

                getCategoryDiscount();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Discount Fail", error.getMessage());
                progressDialog.hide();
                UtilApp.logData(DiscountMasterActivity.this, "Customer Discount Fail: " + error.getMessage());
            }
        });
    }

    private void getCategoryDiscount() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomerDiscountList(App.ROUTE_CATEGORY_DISCOUNT, Settings.getString(App.ROUTEID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Discount Response", response.body().toString());
                UtilApp.logData(DiscountMasterActivity.this, "Customer Discount Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<DiscountData> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<DiscountData>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_DISCOUNT_CUSTOMER_EXCLUDE);
                        db.deleteTable(db.TABLE_DISCOUNT_MAIN_CATEGORY);
                        db.insertRouteCategoryDiscount(arrItems);
                    }
                }

                progressDialog.hide();
                getDiscountData(type);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Discount Fail", error.getMessage());
                progressDialog.hide();
                UtilApp.logData(DiscountMasterActivity.this, "Customer Discount Fail: " + error.getMessage());
            }
        });
    }
}
