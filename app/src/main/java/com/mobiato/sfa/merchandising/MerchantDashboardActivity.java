package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.databinding.ActivityMerchantDashboardBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.rest.BackgroundSync;

public class MerchantDashboardActivity extends BaseActivity {

    private ActivityMerchantDashboardBinding binding;
    private DBManager db;
    private int totalCustomer = 0, totalVisited = 0, completePer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMerchantDashboardBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setNavigationView();
        setTitle("Dashboard");
        setNotification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(MerchantDashboardActivity.this, BackgroundSync.class));
        } else {
            startService(new Intent(MerchantDashboardActivity.this, BackgroundSync.class));
        }

        //get Customer List
        db = new DBManager(this);
        totalCustomer = db.getTotalCustomerList();
        totalVisited = db.getTotalVisibleCustomer();
        if (totalCustomer > 0)
            completePer = (totalVisited * 100) / totalCustomer;

        binding.customProgress.setProgressValue(0);
        binding.customProgress4.setProgressValue(completePer);
        binding.tvMainProgress.setText(String.valueOf(completePer) + "%");
        binding.txtTotalCustomer.setText(String.valueOf(totalCustomer));
        binding.txtVisitedCustomer.setText(String.valueOf(totalVisited));

    }

}
