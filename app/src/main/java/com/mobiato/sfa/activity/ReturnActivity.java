package com.mobiato.sfa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityReturnBinding;
import com.mobiato.sfa.databinding.RowItemReturnBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.ReturnOrder;

import java.util.ArrayList;

public class ReturnActivity extends BaseActivity implements View.OnClickListener {

    public ActivityReturnBinding binding;
    public ArrayList<ReturnOrder> arrData = new ArrayList<>();
    private CommonAdapter<ReturnOrder> mAdapter;
    private Customer mCustomer;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReturnBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Returns");

        db = new DBManager(ReturnActivity.this);

        if (getIntent() != null) {
            mCustomer = (Customer) getIntent().getSerializableExtra("customer");
        }

        binding.fabMenu.setMenuButtonColorNormal(Color.parseColor("#505B96"));
        binding.fabMenu.setMenuButtonColorPressed(Color.parseColor("#505B96"));
        binding.fabMenu.setMenuButtonColorRipple(Color.parseColor("#505B96"));

        binding.fabGood.setOnClickListener(this);
        binding.fabBad.setOnClickListener(this);

    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<ReturnOrder>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {

                if (holder.binding instanceof RowItemReturnBinding) {
                    ((RowItemReturnBinding) holder.binding).setItem(arrData.get(position));
                    if (arrData.get(position).returnType.equalsIgnoreCase("Bad")) {
                        ((RowItemReturnBinding) holder.binding).tvOrderNo.setText("BR No: " + arrData.get(position).orderNo);
                    } else {
                        ((RowItemReturnBinding) holder.binding).tvOrderNo.setText("GR No: " + arrData.get(position).orderNo);
                    }
                    holder.binding.executePendingBindings();

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrData.get(position).getIsReturnRequest().equalsIgnoreCase("1")) {
                                if (arrData.get(position).getIsPosted().equalsIgnoreCase("N")) {
                                    Intent in = new Intent(ReturnActivity.this, ReturnUpdateActivity.class);
                                    in.putExtra("ReturnType", arrData.get(position).getReturnType());
                                    in.putExtra("cust_code", mCustomer.getCustomerId());
                                    in.putExtra("orderNo", arrData.get(position).orderNo);
                                    startActivity(in);
                                } else {
                                    Intent in = new Intent(ReturnActivity.this, OrderDetailActivity.class);
                                    in.putExtra("type", "Return");
                                    in.putExtra("orderNo", arrData.get(position).orderNo);
                                    startActivity(in);
                                }
                            } else {
                                Intent in = new Intent(ReturnActivity.this, OrderDetailActivity.class);
                                in.putExtra("type", "Return");
                                in.putExtra("orderNo", arrData.get(position).orderNo);
                                startActivity(in);
                            }

                        }
                    });
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_return;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabGood:
                OrderRequestActivity.arrFOCItem = new ArrayList<>();
                Intent in = new Intent(ReturnActivity.this, OrderRequestActivity.class);
                in.putExtra("Type", "Return");
                in.putExtra("ReturnType", "Good");
                in.putExtra("cust_code", mCustomer.getCustomerId());
                startActivity(in);
                binding.fabMenu.close(false);
                break;
            case R.id.fabBad:
                OrderRequestActivity.arrFOCItem = new ArrayList<>();
                Intent inB = new Intent(ReturnActivity.this, OrderRequestActivity.class);
                inB.putExtra("Type", "Return");
                inB.putExtra("ReturnType", "Bad");
                inB.putExtra("cust_code", mCustomer.getCustomerId());
                startActivity(inB);
                binding.fabMenu.close(false);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        db = new DBManager(ReturnActivity.this);

        arrData = new ArrayList<>();
        arrData = db.getAllReturn(mCustomer.getCustomerId());

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
