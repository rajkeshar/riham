package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.OrderDetailActivity;
import com.mobiato.sfa.databinding.ActivityReturnMerchandiserBinding;
import com.mobiato.sfa.databinding.RowItemReturnBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.ReturnOrder;

import java.util.ArrayList;

public class ReturnMerchandiserActivity extends BaseActivity implements View.OnClickListener {

    private ActivityReturnMerchandiserBinding binding;
    public ArrayList<ReturnOrder> arrData = new ArrayList<>();
    private CommonAdapter<ReturnOrder> mAdapter;
    private Customer mCustomer;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReturnMerchandiserBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Return");

        db = new DBManager(ReturnMerchandiserActivity.this);

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
                            Intent in = new Intent(ReturnMerchandiserActivity.this, ReturnDetailActivity.class);
                            in.putExtra("type", "Return");
                            in.putExtra("orderNo", arrData.get(position).orderNo);
                            startActivity(in);
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
    protected void onResume() {
        super.onResume();

        db = new DBManager(ReturnMerchandiserActivity.this);

        arrData = new ArrayList<>();
        arrData = db.getAllReturnMR(mCustomer.getCustomerId());

        setAdapter();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabGood:
                Intent in = new Intent(ReturnMerchandiserActivity.this, ReturnRequestActivity.class);
                in.putExtra("Type", "Return");
                in.putExtra("ReturnType", "Good");
                in.putExtra("cust_code", mCustomer.getCustomerId());
                startActivity(in);
                binding.fabMenu.close(false);
                break;
            case R.id.fabBad:
                Intent inB = new Intent(ReturnMerchandiserActivity.this, ReturnRequestActivity.class);
                inB.putExtra("Type", "Return");
                inB.putExtra("ReturnType", "Bad");
                inB.putExtra("cust_code", mCustomer.getCustomerId());
                startActivity(inB);
                binding.fabMenu.close(false);
                break;
            default:
                break;
        }
    }
}
