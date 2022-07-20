package com.mobiato.sfa.activity;

import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityAllItemsBinding;
import com.mobiato.sfa.databinding.RowItemUnloadBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Item;

import java.util.ArrayList;
import java.util.Random;

public class AllItemsActivity extends BaseActivity {

    private ActivityAllItemsBinding binding;
    private String type = "";
    private ArrayList<Item> arrItem = new ArrayList<>();
    private CommonAdapter<Item> mAdapter;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private DBManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllItemsBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);

        db = new DBManager(AllItemsActivity.this);

        type = getIntent().getStringExtra("type");
        setTitle(type);

        String reasonCode = getReasonCode(type);
        arrItem = new ArrayList<>();
        arrItem = db.getVarianceItem(reasonCode);
        setAdapter();
    }

    private void setAdapter() {

        mAdapter = new CommonAdapter<Item>(arrItem) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemUnloadBinding) {
                    Item mItem = arrItem.get(position);
                    ((RowItemUnloadBinding) holder.binding).setItem(mItem);

                    if (mItem.getAltrUOM() != null) {
                        ((RowItemUnloadBinding) holder.binding).tvAlterUOM.setText(mItem.getAlterUOMName() + ": " + mItem.getAlterUOMQty());
                        if (mItem.getBaseUOM() != null) {
                            ((RowItemUnloadBinding) holder.binding).dividerUOM.setVisibility(View.VISIBLE);
                            ((RowItemUnloadBinding) holder.binding).tvBaseUOM.setVisibility(View.VISIBLE);
                            ((RowItemUnloadBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
                        } else {
                            ((RowItemUnloadBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                            ((RowItemUnloadBinding) holder.binding).tvBaseUOM.setVisibility(View.GONE);
                        }
                    } else {
                        ((RowItemUnloadBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                        ((RowItemUnloadBinding) holder.binding).tvAlterUOM.setVisibility(View.GONE);
                        ((RowItemUnloadBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
                    }

                    ((RowItemUnloadBinding) holder.binding).imageView2.setLetter(mItem.getItemName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowItemUnloadBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_unload;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    private String getReasonCode(String name) {
        String code = "0";

        switch (name) {
            case "ROT":
                code = "1";
                break;
            case "Truck Damage":
                code = "2";
                break;
            case "Van Expiry":
                code = "3";
                break;
            case "Theft/Variance":
                code = "4";
                break;
            case "Bad Return Variance":
                code = "5";
                break;
        }
        return code;

    }
}
