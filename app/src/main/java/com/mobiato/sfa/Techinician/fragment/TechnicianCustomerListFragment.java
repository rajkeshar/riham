package com.mobiato.sfa.Techinician.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mobiato.sfa.Adapter.CustomerAdapter;
import com.mobiato.sfa.Adapter.CustomerTechnicianAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.Techinician.fragment.Customers.CustomerHistoryFormActivity;
import com.mobiato.sfa.activity.CustomerDetailActivity;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.databinding.FragmentTechnicianChillerBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerTechnician;
import com.mobiato.sfa.model.RecentCustomer;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;

/**
 * A simple {@link Fragment} subclass.
 */
public class TechnicianCustomerListFragment extends BaseActivity {

    FragmentTechnicianChillerBinding binding;
    private MaterialShowcaseSequence sequence;
    private DBManager db;
    private ArrayList<CustomerTechnician> arrRecent = new ArrayList<>();
    private CustomerTechnicianAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTechnicianChillerBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        //setNotification();

        db = new DBManager(this);
        arrRecent = new ArrayList<>();
        arrRecent = db.getCutomerTechnicianList();

        setMainAdapter();
        setNavigationView();
        setTitle(getString(R.string.nav_customer));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setNotification();
    }


    private void setMainAdapter() {

        mAdapter = new CustomerTechnicianAdapter(this, arrRecent, new CustomerTechnicianAdapter.ContactsAdapterListener() {

            @Override
            public void onContactSelected(CustomerTechnician contact) {
                Intent in = new Intent(TechnicianCustomerListFragment.this, CustomerHistoryFormActivity.class);
                in.putExtra("customer", contact);
                startActivity(in);
            }
        });

        binding.rvChillerList.setAdapter(mAdapter);
    }
}
