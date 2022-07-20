package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityDeliveryListBinding;
import com.mobiato.sfa.databinding.RowItemCustDeliveryBinding;
import com.mobiato.sfa.databinding.RowItemDeliveryBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Delivery;

import java.util.ArrayList;

public class DeliveryListActivity extends BaseActivity {

    private ActivityDeliveryListBinding binding;
    private ArrayList<Delivery> arrDelivery = new ArrayList<>();
    private CommonAdapter<Delivery> mAdapter;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryListBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setNavigationView();

        setTitle("Delivery List");
        db = new DBManager(DeliveryListActivity.this);
        arrDelivery = db.getAllDeliveryList();

        setAdapter();

    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Delivery>(arrDelivery) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemCustDeliveryBinding) {
                    Delivery mItem = arrDelivery.get(position);
                    ((RowItemCustDeliveryBinding) holder.binding).setItem(mItem);

                    Customer mCustomer = db.getCustomerDetail(arrDelivery.get(position).getCustomerId());
                    ((RowItemCustDeliveryBinding) holder.binding).tvDeliveryDate.setText("Customer: " + mCustomer.getCustomerName());

                    if (mItem.getIsDelete().equalsIgnoreCase("1")) {
                        ((RowItemCustDeliveryBinding) holder.binding).imgVerified.setVisibility(View.VISIBLE);
                        ((RowItemCustDeliveryBinding) holder.binding).imgVerified.setImageResource(R.drawable.ic_icon_block_sel);
                    } else if (mItem.getIsDelete().equalsIgnoreCase("2")) {
                        ((RowItemCustDeliveryBinding) holder.binding).imgVerified.setVisibility(View.VISIBLE);
                        ((RowItemCustDeliveryBinding) holder.binding).imgVerified.setImageResource(R.drawable.ic_icon_verified_sel);
                    } else {
                        ((RowItemCustDeliveryBinding) holder.binding).imgVerified.setVisibility(View.GONE);
                    }

                    holder.binding.executePendingBindings();


                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mItem.getIsDelete().equalsIgnoreCase("0")) {
                                startActivity(new Intent(DeliveryListActivity.this, DeliveryDetailActivity.class)
                                        .putExtra("orderNo", mItem.getOrderId())
                                        .putExtra("customer", mCustomer.getCustomerName()));
                            }
                        }
                    });

                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_cust_delivery;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

}
