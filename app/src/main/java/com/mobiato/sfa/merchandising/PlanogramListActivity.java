package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityPlanogramListBinding;
import com.mobiato.sfa.databinding.RowPlanogramBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.PlanogramList;

import java.util.ArrayList;

public class PlanogramListActivity extends BaseActivity {

    private ActivityPlanogramListBinding binding;
    private DBManager db;
    private ArrayList<PlanogramList> arrData = new ArrayList<>();
    private CommonAdapter<PlanogramList> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlanogramListBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Planogram List");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        String ID = getIntent().getStringExtra("id");
        db = new DBManager(this);
        arrData = new ArrayList<>();
        arrData = db.getPlanogramList(ID);
        setMainAdapter();
    }

    private void setMainAdapter() {

        mAdapter = new CommonAdapter<PlanogramList>(arrData) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowPlanogramBinding) {
                    ((RowPlanogramBinding) holder.binding).setItem(arrData.get(position));
                    holder.binding.executePendingBindings();
                    ((RowPlanogramBinding) holder.binding).txtTitle.setText(arrData.get(position).getPlanogramName());
                    ((RowPlanogramBinding) holder.binding).txtBrand.setText(arrData.get(position).getDistribution_Tool_Name());

                    Glide.with(PlanogramListActivity.this).load(arrData.get(position).getImage1()).into(((RowPlanogramBinding) holder.binding).profileImage);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(PlanogramListActivity.this, AddPlanogramActivity.class);
                            intent.putExtra("id", arrData.get(position).getDistribution_tool_id());
                            startActivity(intent);
                        }
                    });

                }

            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_planogram;
            }
        };

        binding.rvPlanogramList.setAdapter(mAdapter);
    }
}
