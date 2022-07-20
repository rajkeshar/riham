package com.mobiato.sfa.activity;

import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityOrderDetailBinding;
import com.mobiato.sfa.databinding.RowItemSaleBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.Random;

public class OrderDetailActivity extends BaseActivity {

    private ActivityOrderDetailBinding binding;
    private ArrayList<Item> arrData = new ArrayList<>();
    private String type = "", orderNo = "";
    private DBManager db;
    private CommonAdapter<Item> mAdapter;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private double totalAMt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        db = new DBManager(OrderDetailActivity.this);

        arrData = new ArrayList<>();
        type = getIntent().getStringExtra("type");
        orderNo = getIntent().getStringExtra("orderNo");

        if (type.equalsIgnoreCase("Order")) {
            setTitle("Order Detail");
            arrData = db.getOrderItems(orderNo);
            totalAMt = db.getOrderTotalAmt(orderNo);
        } else if (type.equalsIgnoreCase("Load")) {
            setTitle("Load Detail");
            arrData = db.getLoadRequestItems(orderNo);
            totalAMt = db.getLoadTotalAmt(orderNo);
        } else if (type.equalsIgnoreCase("SalesmanLoad")) {
            setTitle("SalesmanLoad Detail");
            arrData = db.getSalesmanLoadRequestItems(orderNo);
            // totalAMt = db.getLoadTotalAmt(orderNo);
            binding.rlCheckout.setVisibility(View.GONE);
        } else {
            setTitle("Order Detail");
            arrData = db.getReturnItems(orderNo);
            totalAMt = db.getReturnTotalAmt(orderNo);
        }

        binding.txtTot.setText("Total: " + UtilApp.getNumberFormate(totalAMt) + " UGX");
        setAdapter();

    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Item>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemSaleBinding) {
                    Item mItem = arrData.get(position);
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

                    if (mItem.getSaleAltPrice() != null) {
                        ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
                        ((RowItemSaleBinding) holder.binding).tvAlterPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleAltPrice())) + " UGX");
                        if (mItem.getSaleBasePrice() != null) {
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText(UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleBasePrice())) + " UGX");
                        } else {
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.GONE);
                        }
                    } else {
                        if (mItem.getSaleBasePrice() != null) {
                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvAlterPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleBasePrice())) + " UGX");
                        } else {
                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.GONE);
                        }

                    }

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_sale;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }
}
