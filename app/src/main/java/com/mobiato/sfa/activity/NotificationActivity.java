package com.mobiato.sfa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityNotificationBinding;
import com.mobiato.sfa.databinding.RowItemNotificationBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Notification;

import java.util.ArrayList;

public class NotificationActivity extends BaseActivity {

    private ActivityNotificationBinding binding;
    private ArrayList<Notification> arrData = new ArrayList<>();
    private CommonAdapter<Notification> mAdapter;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Notifications");

        App.countNoti = 0;
        db = new DBManager(NotificationActivity.this);
        arrData = new ArrayList<>();
        arrData = db.getAllNotification();

        //setData();
        setAdapter();

    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Notification>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {

                if (holder.binding instanceof RowItemNotificationBinding) {
                    ((RowItemNotificationBinding) holder.binding).setItem(arrData.get(position));

                    if (arrData.get(position).getType().equalsIgnoreCase("return") ||
                            arrData.get(position).getType().equalsIgnoreCase("delivery")) {
                        ((RowItemNotificationBinding) holder.binding).txtMessage.setText(arrData.get(position).getTitle());
                    } else {
                        if (arrData.get(position).getMessage().equalsIgnoreCase("")) {
                            ((RowItemNotificationBinding) holder.binding).txtMessage.setText(arrData.get(position).getTitle());
                        } else {
                            ((RowItemNotificationBinding) holder.binding).txtMessage.setText(arrData.get(position).getTitle() + "\n Reason: " +
                                    arrData.get(position).getMessage());
                        }
                    }

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrData.get(position).getType().equalsIgnoreCase("return")) {
                                Customer mCustomer = db.getCustomerDetail(arrData.get(position).getId());
                                startActivity(new Intent(me, ReturnActivity.class)
                                        .putExtra("customer", mCustomer));
                            } else if (arrData.get(position).getType().equalsIgnoreCase("delivery")) {
                                Customer mCustomer = db.getCustomerDetail(arrData.get(position).getId());
                                startActivity(new Intent(me, DeliveryActivity.class)
                                        .putExtra("customer", mCustomer));
                            } else {
                                startActivity(new Intent(me, NotificationDetailActivity.class)
                                        .putExtra("orderNo", arrData.get(position).getOrderId()));
                            }
                        }
                    });

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_notification;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }
}
