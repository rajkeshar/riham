package com.mobiato.sfa.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.FragmentFragmentThreeBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentThree extends Fragment {

    FragmentFragmentThreeBinding binding;
    private DBManager db;

    public FragmentThree() {
        // Required empty public constructor
    }

    public static FragmentThree createInstance() {
        FragmentThree partThreeFragment = new FragmentThree();
        return partThreeFragment;
    }

    static FragmentManager fragmentManager;

    public static Fragment newInstance() {
        FragmentThree fragment = new FragmentThree();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_fragment_three, container, false);
        View view = binding.getRoot();

        db = new DBManager(getActivity());
        double totalSale = db.getTotalAmtSale();
        double totalColl = db.getTotalCollection();
        try {
            double totalexchange = Double.valueOf(Settings.getString(App.EXCHANGE_AMONUNT));
            binding.txtTotalExchange.setText(""+UtilApp.withSuffix(Math.round(totalexchange)));
        }catch (Exception e){

        }


        int totalLoadQty = 0;
        if (Settings.getString(App.IS_LOAD_VERIFY).equalsIgnoreCase("1")) {
            totalLoadQty = db.getTotalLoadQty();
        }

        int planCust = db.getSeqCustomerCount(UtilApp.getCurrentDay());
        int visitCount = db.getVisitedCustomer();
        //int totalCust = db.getCutomerCount();
        int unPlanCust = visitCount - planCust;
        int totalInv = db.getTotalInvoice();

        double dropPerCust = 0, dropPerInv = 0;
        if (totalSale > 0) {
            dropPerCust = (totalSale / visitCount);
            dropPerInv = (totalSale / totalInv);
        }

        binding.txtTotalSale.setText("" + UtilApp.withSuffix(Math.round(totalSale)));
        binding.txtTotalCollection.setText("" + UtilApp.withSuffix(Math.round(totalColl)));
        binding.txtLoadQty.setText("" + totalLoadQty);
        binding.txtPlannedVisit.setText("" + planCust);
        binding.txtUnplannedVisitis.setText("" + unPlanCust);
        binding.txtVisitedCustomer.setText("" + visitCount);
        binding.txtDropSizeCustomer.setText("" + Math.round(dropPerCust));
        binding.txtDropSizeInvoice.setText("" + Math.round(dropPerInv));

        return view;
    }

}
