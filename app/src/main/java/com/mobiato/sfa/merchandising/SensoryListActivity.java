package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivitySensoryListBinding;
import com.mobiato.sfa.databinding.RowSensoryCustomerBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Stock_Questions;

import java.util.ArrayList;

public class SensoryListActivity extends BaseActivity {

    private ActivitySensoryListBinding binding;
    ArrayList<Stock_Questions> arrAnswer = new ArrayList<>();
    private DBManager db;
    private CommonAdapter<Stock_Questions> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySensoryListBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setNavigationView();

        setTitle("Sensory Evaluation");
        db = new DBManager(this);
        arrAnswer = new ArrayList<>();
        arrAnswer = db.getAllSensoryCustomerList();
        setMainAdapter();

        if (arrAnswer.size() > 0) {
            binding.rvSensorList.setVisibility(View.VISIBLE);
            binding.txtNoContain.setVisibility(View.GONE);
        }

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(me, StockCheck_Questions.class);
                intent.putExtra("title", "Sensory Evaluation");
                startActivity(intent);
            }
        });
    }

    private void setMainAdapter() {

        mAdapter = new CommonAdapter<Stock_Questions>(arrAnswer) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowSensoryCustomerBinding) {
                    ((RowSensoryCustomerBinding) holder.binding).setItem(arrAnswer.get(position));
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_sensory_customer;
            }
        };

        binding.rvSensorList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        db = new DBManager(this);
        arrAnswer = new ArrayList<>();
        arrAnswer = db.getAllSensoryCustomerList();
        setMainAdapter();

        if (arrAnswer.size() > 0) {
            binding.rvSensorList.setVisibility(View.VISIBLE);
            binding.txtNoContain.setVisibility(View.GONE);
        }
    }
}
