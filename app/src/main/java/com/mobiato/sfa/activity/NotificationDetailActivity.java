package com.mobiato.sfa.activity;

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
import com.mobiato.sfa.databinding.ActivityNotificationDetailBinding;
import com.mobiato.sfa.databinding.RowItemSaleBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.OrderData;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;

public class NotificationDetailActivity extends BaseActivity {

    private ActivityNotificationDetailBinding binding;
    private String strOrderNo = "";
    private LoadingSpinner mDialog;
    private ArrayList<OrderData> arrOrder = new ArrayList<>();
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationDetailBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Order Detail");
        me = this;

        db = new DBManager(NotificationDetailActivity.this);

        strOrderNo = getIntent().getStringExtra("orderNo");
        mDialog = new LoadingSpinner(NotificationDetailActivity.this);

        if (UtilApp.isNetworkAvailable(this)) {
            getOrderDetailAPI();
        } else {
            UtilApp.displayAlert(me, "Network not available. Plz check your mobile data.");
        }

    }

    private void getOrderDetailAPI() {
        mDialog.show();
        final Call<JsonObject> labelResponse = ApiClient.getService().getOrderDetail(App.NOTIFICATION_DETAIL, strOrderNo);
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Order Response", response.body().toString());
                UtilApp.logData(NotificationDetailActivity.this, "Order Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        arrOrder = new ArrayList<>();
                        arrOrder = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<OrderData>>() {
                                }.getType());

                        setData();
                    }
                }

                mDialog.hide();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Order Fail", error.getMessage());
                UtilApp.logData(NotificationDetailActivity.this, "Order Fail: " + error.getMessage());
                mDialog.hide();
            }
        });

    }

    private void setData() {

        binding.txtTot.setText("Total: " + UtilApp.getNumberFormate(Double.parseDouble(arrOrder.get(0).getTotalAmt())) + " UGX");

        ArrayList<Item> arrItem = new ArrayList<>();
        for (int i = 0; i < arrOrder.get(0).getDeliveryItems().size(); i++) {
            Item mItem = arrOrder.get(0).getDeliveryItems().get(i);

            mItem.setItemId(arrOrder.get(0).getDeliveryItems().get(i).getItemId());
            mItem.setAgentExcise(arrOrder.get(0).getDeliveryItems().get(i).getAgentExcise());
            mItem.setItemName(db.getItemName(arrOrder.get(0).getDeliveryItems().get(i).getItemId()));
            mItem.setItemCode(db.getItemCode(arrOrder.get(0).getDeliveryItems().get(i).getItemId()));

            if (db.checkIsBaseUOM(arrOrder.get(0).getDeliveryItems().get(i).getUom(), arrOrder.get(0).getDeliveryItems().get(i).getItemId())) {
                mItem.setBaseUOM(arrOrder.get(0).getDeliveryItems().get(i).getUom());
                mItem.setBaseUOMName(db.getUOM(arrOrder.get(0).getDeliveryItems().get(i).getUom()));
                mItem.setBaseUOMQty(arrOrder.get(0).getDeliveryItems().get(i).getQty());
                mItem.setSaleBaseQty(arrOrder.get(0).getDeliveryItems().get(i).getQty());
                mItem.setBaseUOMPrice(arrOrder.get(0).getDeliveryItems().get(i).getTotalAmt());
                mItem.setSaleBasePrice(arrOrder.get(0).getDeliveryItems().get(i).getTotalAmt());
                mItem.setAltrUOM(db.getItemUOM(arrOrder.get(0).getDeliveryItems().get(i).getItemId(), "Alter"));
                mItem.setAlterUOMName(db.getUOM(db.getItemUOM(arrOrder.get(0).getDeliveryItems().get(i).getItemId(), "Alter")));
                mItem.setAlterUOMQty("0");
                mItem.setAlterUOMPrice("0");
                mItem.setSaleAltQty("0");
                mItem.setSaleAltPrice("0");
            } else if (db.checkIsAlterUOM(arrOrder.get(0).getDeliveryItems().get(i).getUom(), arrOrder.get(0).getDeliveryItems().get(i).getItemId())) {
                mItem.setBaseUOM(db.getItemUOM(arrOrder.get(0).getDeliveryItems().get(i).getItemId(), "Base"));
                mItem.setBaseUOMQty("0");
                mItem.setBaseUOMPrice("0");
                mItem.setSaleBaseQty("0");
                mItem.setSaleBasePrice("0");
                mItem.setBaseUOMName(db.getUOM(db.getItemUOM(arrOrder.get(0).getDeliveryItems().get(i).getItemId(), "Base")));
                mItem.setAlterUOMName(db.getUOM(arrOrder.get(0).getDeliveryItems().get(i).getUom()));
                mItem.setAltrUOM(db.getItemUOM(arrOrder.get(0).getDeliveryItems().get(i).getItemId(), "Alter"));
                mItem.setAlterUOMQty(arrOrder.get(0).getDeliveryItems().get(i).getQty());
                mItem.setAlterUOMPrice(arrOrder.get(0).getDeliveryItems().get(i).getTotalAmt());
                mItem.setSaleAltQty(arrOrder.get(0).getDeliveryItems().get(i).getQty());
                mItem.setSaleAltPrice(arrOrder.get(0).getDeliveryItems().get(i).getTotalAmt());
            }

            mItem.setPreVatAmt(arrOrder.get(0).getDeliveryItems().get(i).getPreVatAmt());
            mItem.setExciseAmt(arrOrder.get(0).getDeliveryItems().get(i).getExciseAmt());
            mItem.setNetAmt(arrOrder.get(0).getDeliveryItems().get(i).getNetAmt());
            mItem.setVatAmt(arrOrder.get(0).getDeliveryItems().get(i).getVatAmt());

            if (arrItem.size() > 0) {

                boolean isContain = false;
                int position = 0;
                for (int j = 0; j < arrItem.size(); j++) {
                    if (arrItem.get(j).getItemId().equalsIgnoreCase(arrOrder.get(0).getDeliveryItems().get(i).getItemId())) {
                        position = j;
                        isContain = true;
                        break;
                    }
                }

                if (isContain) {
                    Item containItem = arrItem.get(position);
                    if (db.checkIsBaseUOM(arrOrder.get(0).getDeliveryItems().get(i).getUom(), arrOrder.get(0).getDeliveryItems().get(i).getItemId())) {
                        containItem.setSaleBaseQty(mItem.getBaseUOMQty());
                        containItem.setSaleBasePrice(mItem.getBaseUOMPrice());
                        containItem.setBaseUOMQty(mItem.getBaseUOMQty());
                        containItem.setBaseUOMPrice(mItem.getBaseUOMPrice());
                        containItem.setBaseUOM(mItem.getBaseUOM());
                        containItem.setBaseUOMName(mItem.getBaseUOMName());
                    } else {
                        containItem.setSaleAltQty(mItem.getSaleAltQty());
                        containItem.setSaleAltPrice(mItem.getSaleAltPrice());
                        containItem.setAlterUOMQty(mItem.getSaleAltQty());
                        containItem.setAlterUOMPrice(mItem.getSaleAltPrice());
                        containItem.setAltrUOM(mItem.getAltrUOM());
                        containItem.setAlterUOMName(mItem.getAlterUOMName());
                    }
                    arrItem.set(position, containItem);
                } else {
                    arrItem.add(mItem);
                }
            } else {
                arrItem.add(mItem);
            }

        }


        CommonAdapter<Item> adapter = new CommonAdapter<Item>(arrItem) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemSaleBinding) {
                    Item mItem = arrItem.get(position);
                    ((RowItemSaleBinding) holder.binding).setItem(mItem);

                    ((RowItemSaleBinding) holder.binding).imageView2.setLetter(mItem.getItemName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowItemSaleBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    if (mItem.getAltrUOM() != null) {
                        ((RowItemSaleBinding) holder.binding).tvAlterUOM.setText(mItem.getAlterUOMName() + ": " + mItem.getAlterUOMQty());
                        if (mItem.getBaseUOM() != null) {
                            ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
                        } else {
                            ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setVisibility(View.GONE);
                        }
                    } else {
                        ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                        ((RowItemSaleBinding) holder.binding).tvAlterUOM.setVisibility(View.GONE);
                        ((RowItemSaleBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
                    }

                    if (mItem.getAlterUOMPrice() != null) {
                        ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
                        ((RowItemSaleBinding) holder.binding).tvAlterPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getAlterUOMPrice())) + " UGX");
                        if (mItem.getBaseUOMPrice() != null) {
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText(UtilApp.getNumberFormate(Double.parseDouble(mItem.getBaseUOMPrice())) + " UGX");
                        } else {
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.GONE);
                        }
                    } else {
                        if (mItem.getBaseUOMPrice() != null) {
                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvAlterPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getBaseUOMPrice())) + " UGX");
                        } else {
                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.GONE);
                        }

                    }
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_sale;
            }
        };

        binding.rvList.setAdapter(adapter);
    }

}
