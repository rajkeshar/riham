package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityAssetsListBinding;
import com.mobiato.sfa.databinding.RowAssetsListBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.ASSETS_MODEL;

import java.util.ArrayList;

public class AssetsListActivity extends BaseActivity {
    private ActivityAssetsListBinding binding;
    private DBManager db;
    private ArrayList<ASSETS_MODEL> arrData = new ArrayList<>();
    private CommonAdapter<ASSETS_MODEL> mAdapter;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssetsListBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        setTitle("Assets");
        customerId = getIntent().getStringExtra("CustomerId");

        db = new DBManager(this);
        arrData = new ArrayList<>();
        arrData = db.getAssetsList(customerId);

        setMainAdapter();

    }

    private void setMainAdapter() {

        mAdapter = new CommonAdapter<ASSETS_MODEL>(arrData) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowAssetsListBinding) {
                    ((RowAssetsListBinding) holder.binding).setItem(arrData.get(position));
                    holder.binding.executePendingBindings();

                    Glide.with(AssetsListActivity.this).load(arrData.get(position).getAssetsImage()).into(((RowAssetsListBinding) holder.binding).profileImage);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AssetsListActivity.this, AssetsDetailActivity.class);
                            intent.putExtra("assetsId", arrData.get(position).getAssetsId());
                            intent.putExtra("customerId", customerId);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_assets_list;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }
}
