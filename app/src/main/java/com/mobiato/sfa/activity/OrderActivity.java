package com.mobiato.sfa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityOrderBinding;
import com.mobiato.sfa.databinding.RowItemOrederBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Order;

import java.util.ArrayList;

public class OrderActivity extends BaseActivity {

    public ActivityOrderBinding binding;
    public ArrayList<Order> arrData = new ArrayList<>();
    private CommonAdapter<Order> mAdapter;
    private Customer mCustomer;
    private DBManager db;
    private String strType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Orders");


        if (getIntent() != null) {
            mCustomer = (Customer) getIntent().getSerializableExtra("customer");
            strType = getIntent().getStringExtra("Type");
        }


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderRequestActivity.strDigitSign = "";
                OrderRequestActivity.arrFOCItem = new ArrayList<>();
                Intent in = new Intent(OrderActivity.this, OrderRequestActivity.class);
                in.putExtra("Type", "Order");
                in.putExtra("AppType", strType);
                in.putExtra("cust_code", mCustomer.getCustomerId());
                startActivity(in);
            }
        });
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Order>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {

                if (holder.binding instanceof RowItemOrederBinding) {
                    ((RowItemOrederBinding) holder.binding).setItem(arrData.get(position));
                    holder.binding.executePendingBindings();

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(OrderActivity.this, OrderDetailActivity.class);
                            in.putExtra("type", "Order");
                            in.putExtra("orderNo", arrData.get(position).orderNo);
                            startActivity(in);
                        }
                    });
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_oreder;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        db = new DBManager(OrderActivity.this);
        arrData = new ArrayList<>();
        arrData = db.getAllOrder(mCustomer.getCustomerId());

        if (arrData.size() > 0) {
            binding.rvList.setVisibility(View.VISIBLE);
            binding.txtNoContain.setVisibility(View.GONE);
            setAdapter();
        } else {
            binding.rvList.setVisibility(View.GONE);
            binding.txtNoContain.setVisibility(View.VISIBLE);
        }

    }
}
