package com.mobiato.sfa.merchandising;

import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityDeliveryDetailBinding;
import com.mobiato.sfa.databinding.RowItemSaleBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.Random;

public class DeliveryDetailActivity extends BaseActivity {

    private ActivityDeliveryDetailBinding binding;
    private String mCustomer, deliveryId;
    private ArrayList<Item> arrData = new ArrayList<>();
    private DBManager db;
    private CommonAdapter<Item> mAdapter;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private double totalAMt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryDetailBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        if (getIntent() != null) {
            mCustomer = getIntent().getStringExtra("customer");
            deliveryId = getIntent().getStringExtra("orderNo");
        }

        setTitle(mCustomer);

        db = new DBManager(DeliveryDetailActivity.this);

        arrData = new ArrayList<>();
        arrData = db.getDeliveryAcceptItems(deliveryId);
        setAdapter();

        binding.rlCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
                        ((RowItemSaleBinding) holder.binding).tvAlterUOM.setText(mItem.getAlterUOMName() + ": " + mItem.getSaleAltQty());
                        if (mItem.getBaseUOM() != null) {
                            ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getSaleBaseQty());
                        } else {
                            ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setVisibility(View.GONE);
                        }
                    } else {
                        ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                        ((RowItemSaleBinding) holder.binding).tvAlterUOM.setVisibility(View.GONE);
                        ((RowItemSaleBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getSaleBaseQty());
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
