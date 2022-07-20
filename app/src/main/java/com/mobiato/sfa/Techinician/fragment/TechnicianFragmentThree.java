package com.mobiato.sfa.Techinician.fragment;


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
import com.mobiato.sfa.databinding.TechnicianFragmentThreeBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.ChillerTechnician;
import com.mobiato.sfa.model.CustomerTechnician;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TechnicianFragmentThree extends Fragment {

    //FragmentFragmentThreeBinding binding;
    TechnicianFragmentThreeBinding binding;
    private DBManager db;

    int total_cust = 0;
    int total_chiller = 0;
    int total_chiller_install = 0;
    int total_chiller_padding = 0;

    private ArrayList<CustomerTechnician> arrCustomer = new ArrayList<>();
    private ArrayList<ChillerTechnician> arrtotalChiller = new ArrayList<>();
    private ArrayList<ChillerTechnician> arrtotal_install_chiller = new ArrayList<>();

    public TechnicianFragmentThree() {
        // Required empty public constructor
    }

    public static TechnicianFragmentThree createInstance() {
        TechnicianFragmentThree partThreeFragment = new TechnicianFragmentThree();
        return partThreeFragment;
    }

    static FragmentManager fragmentManager;

    public static Fragment newInstance() {
        TechnicianFragmentThree fragment = new TechnicianFragmentThree();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        arrCustomer = new ArrayList<>();
        arrCustomer.clear();
        arrCustomer = db.getCutomerTechnicianList();
        total_cust = arrCustomer.size();
        binding.txtTotalCustomers.setText(String.valueOf(total_cust));
        arrtotalChiller = new ArrayList<>();
        arrtotalChiller.clear();
        arrtotalChiller = db.getChillerTechnicianList();
        total_chiller = arrtotalChiller.size();
        binding.txtTotalFridges.setText(String.valueOf(total_chiller));
        //Install
        arrtotal_install_chiller = new ArrayList<>();
        arrtotal_install_chiller.clear();
        arrtotal_install_chiller = db.getChillerTechnicianListcheck();
        total_chiller_install = arrtotal_install_chiller.size();
        binding.txtTotalChillers.setText(String.valueOf(total_chiller_install));

        total_chiller_padding = total_chiller - total_chiller_install;
        binding.txtTotalPendingChillers.setText(String.valueOf(total_chiller_padding));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.technician_fragment_three, container, false);
        View view = binding.getRoot();

        db = new DBManager(getActivity());
        arrCustomer = new ArrayList<>();
        arrCustomer.clear();
        arrCustomer = db.getCutomerTechnicianList();
        total_cust = arrCustomer.size();
        binding.txtTotalCustomers.setText(String.valueOf(total_cust));
        arrtotalChiller = new ArrayList<>();
        arrtotalChiller.clear();
        arrtotalChiller = db.getChillerTechnicianList();
        total_chiller = arrtotalChiller.size();
        binding.txtTotalFridges.setText(String.valueOf(total_chiller));
        //Install
        arrtotal_install_chiller = new ArrayList<>();
        arrtotal_install_chiller.clear();
        arrtotal_install_chiller = db.getChillerTechnicianListcheck();
        total_chiller_install = arrtotal_install_chiller.size();
        binding.txtTotalChillers.setText(String.valueOf(total_chiller_install));

        total_chiller_padding = total_chiller - total_chiller_install;
        binding.txtTotalPendingChillers.setText(String.valueOf(total_chiller_padding));

        int PMDONe = Integer.parseInt(Settings.getString(App.PM_ServiceLast)) - 1;
        int BDDONe = Integer.parseInt(Settings.getString(App.BD_Done_ServiceLast)) - 1;
        int BDPendingDONe = Integer.parseInt(Settings.getString(App.BD_Pending_ServiceLast)) - 1;
        int SADONe = Integer.parseInt(Settings.getString(App.SA_ServiceLast)) - 1;
        int CADONe = Integer.parseInt(Settings.getString(App.CA_ServiceLast)) - 1;

        binding.txtPMDone.setText(String.valueOf(PMDONe));
        binding.txtBDDone.setText(String.valueOf(BDDONe));
        binding.txtBDPending.setText(String.valueOf(BDPendingDONe));
        binding.txtInspectionDone.setText(String.valueOf(SADONe));
        binding.txtAuditDone.setText(String.valueOf(CADONe));
       /* double totalSale = db.getTotalAmtSale();
        double totalColl = db.getTotalCollection();
        try {
            double totalexchange = Double.valueOf(Settings.getString(App.EXCHANGE_AMONUNT));
            binding.txtTotalExchange.setText(""+UtilApp.withSuffix(Math.round(totalexchange)));
        }catch (Exception e){

        }*/
//
        /*if (App.countNoti > 0) {
            binding.txtTotalPendingChillers.setText(App.countNoti);
        }*/

    /*    binding.txtTotalSale.setText("" + UtilApp.withSuffix(Math.round(totalSale)));
        binding.txtTotalCollection.setText("" + UtilApp.withSuffix(Math.round(totalColl)));
        binding.txtLoadQty.setText("" + totalLoadQty);
        binding.txtPlannedVisit.setText("" + planCust);*/

        return view;
    }

}
