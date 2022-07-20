package com.mobiato.sfa.activity;

import android.os.Bundle;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityCollectionBinding;
import com.mobiato.sfa.databinding.RowItemCollectionBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.CollectionData;
import com.mobiato.sfa.model.Customer;

import java.util.ArrayList;

public class CollectionActivity extends BaseActivity {

    private ActivityCollectionBinding binding;
    private ArrayList<CollectionData> arrData = new ArrayList<>();
    private CommonAdapter<CollectionData> mAdapter;
    private DBManager db;
    private Customer mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Collections");

        db = new DBManager(CollectionActivity.this);

        if (getIntent() != null) {
            mCustomer = (Customer) getIntent().getSerializableExtra("customer");
        }

        setData();
        setAdapter();

    }

    private void setData() {
        arrData = new ArrayList<>();
        arrData = db.getCustomerCollection(mCustomer.getCustomerId());
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<CollectionData>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemCollectionBinding) {
                    ((RowItemCollectionBinding) holder.binding).setItem(arrData.get(position));
                    ((RowItemCollectionBinding) holder.binding).tvAmtDue.setText("Amount Due" + " : 0");
                    holder.binding.executePendingBindings();

//                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (arrData.get(position).getColl_is_payable().equalsIgnoreCase("1")) {
//                                Intent in = new Intent(CollectionActivity.this, PaymentDetailActivity.class);
//                                in.putExtra("customer", mCustomer);
//                                in.putExtra("collNum", arrData.get(position).getColl_Id());
//                                in.putExtra("invDate", arrData.get(position).getColl_invoiceDate());
//                                in.putExtra("dueDate", arrData.get(position).getColl_dueDate());
//                                in.putExtra("amount", arrData.get(position).getColl_dueAmount());
//                                in.putExtra("invNo", arrData.get(position).getColl_invoiceNo());
//                                in.putExtra("amtClear", arrData.get(position).getColl_paidAmount());
//                                in.putExtra("invAmt", arrData.get(position).getColl_amount());
//                                startActivity(in);
//                            } else {
//                                Toast.makeText(CollectionActivity.this, "Payment is already done", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_collection;
            }
        };

        binding.rvCollection.setAdapter(mAdapter);
    }
}
