package com.mobiato.sfa.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityLoadRequestBinding;
import com.mobiato.sfa.databinding.ActivitySalesmanLoadBinding;
import com.mobiato.sfa.databinding.RowItemLoadRequestBinding;
import com.mobiato.sfa.databinding.RowItemSalesmanLoadBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Order;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;

import java.util.ArrayList;

public class SalesmanLoadActivity extends BaseActivity implements View.OnClickListener {

    private ActivitySalesmanLoadBinding binding;
    private DBManager db;
    private LoadingSpinner progressDialog;
    public ArrayList<Order> arrData = new ArrayList<>();
    private CommonAdapter<Order> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySalesmanLoadBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle(getString(R.string.nav_salesmanLoad));
        setNavigationView();

        db = new DBManager(SalesmanLoadActivity.this);
        progressDialog = new LoadingSpinner(SalesmanLoadActivity.this);

        setData();

        binding.fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent in = new Intent(SalesmanLoadActivity.this, AddSalesmanLoadActivity.class);
                startActivity(in);
                break;
            default:
                break;
        }
    }

    private void setData() {

        arrData = new ArrayList<>();
        arrData = db.getAllSalesmanLoadRequest();
        if (arrData.size() > 0) {
            binding.txtNoContain.setVisibility(View.GONE);
            binding.rvList.setVisibility(View.VISIBLE);
        } else {
            binding.txtNoContain.setVisibility(View.VISIBLE);
            binding.rvList.setVisibility(View.GONE);
        }

        setAdapter();
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Order>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {

                if (holder.binding instanceof RowItemSalesmanLoadBinding) {
                    ((RowItemSalesmanLoadBinding) holder.binding).setItem(arrData.get(position));
                    holder.binding.executePendingBindings();

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(SalesmanLoadActivity.this, OrderDetailActivity.class);
                            in.putExtra("type", "SalesmanLoad");
                            in.putExtra("orderNo", arrData.get(position).orderNo);
                            startActivity(in);
                        }
                    });
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_salesman_load;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        setData();

    }
}