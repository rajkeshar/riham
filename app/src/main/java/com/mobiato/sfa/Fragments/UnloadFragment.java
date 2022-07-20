package com.mobiato.sfa.Fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.AllItemsActivity;
import com.mobiato.sfa.activity.BadReturnActivity;
import com.mobiato.sfa.activity.FreshUnloadActivity;
import com.mobiato.sfa.databinding.FragmentUnloadBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnloadFragment extends Fragment {

    public FragmentUnloadBinding binding;
    public DBManager db;

    // TODO: Rename and change types and number of parameters
    public static UnloadFragment newInstance() {
        UnloadFragment fragment = new UnloadFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_unload, container, false);
        View view = binding.getRoot();

        db = new DBManager(getActivity());

        //Enable/Disable Items
        disableOption();

        binding.lytFreshUnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!db.checkIfLoadExists()) {
                    if (db.isSyncCompleteTransaction()) {
                        startActivity(new Intent(getActivity(), FreshUnloadActivity.class));
                    } else {
                        // logOut();
                        UtilApp.displayAlert(getActivity(), "Please sync transaction first!");
                    }
                } else {
                    UtilApp.displayAlert(getActivity(), "Please accept pending load before doing unload.");
                }


                /*if (db.isSyncCompleteTransaction()) {
                    startActivity(new Intent(getActivity(), FreshUnloadActivity.class));
                } else {
                    UtilApp.displayAlert(getActivity(), "Please sync all the transaction first!");
                }*/

            }
        });

        /*binding.lytROT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AllItemsActivity.class);
                in.putExtra("type", "ROT");
                startActivity(in);
            }
        });*/

        binding.lytEndInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AllItemsActivity.class);
                in.putExtra("type", "End Inventory");
                startActivity(in);
            }
        });

        binding.lytTruckDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AllItemsActivity.class);
                in.putExtra("type", "Truck Damage");
                startActivity(in);
            }
        });

        binding.lytVariance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AllItemsActivity.class);
                in.putExtra("type", "Theft/Variance");
                startActivity(in);
            }
        });

        binding.lytExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AllItemsActivity.class);
                in.putExtra("type", "Van Expiry");
                startActivity(in);
            }
        });

        binding.lytBadReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BadReturnActivity.class));
            }
        });

       /* binding.lytBadVariance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AllItemsActivity.class);
                in.putExtra("type", "Bad Return Variance");
                startActivity(in);
            }
        });*/
        return view;
    }

    public void disableOption() {

        if (binding != null) {

           /* if (Settings.getString(App.IS_BADCAPTURE).equalsIgnoreCase("1")) {
                binding.lytBadVariance.setClickable(true);
                binding.lytBadVariance.setEnabled(true);
                binding.lytBadVariance.setAlpha(1.0f);
            } else {
                binding.lytBadVariance.setClickable(false);
                binding.lytBadVariance.setEnabled(false);
                binding.lytBadVariance.setAlpha(0.5f);
            }*/

            if (Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("1")) {
//            binding.lytROT.setClickable(true);
//            binding.lytROT.setEnabled(true);
//            binding.lytROT.setAlpha(1.0f);

                binding.lytEndInventory.setClickable(true);
                binding.lytEndInventory.setEnabled(true);
                binding.lytEndInventory.setAlpha(1.0f);

                binding.lytTruckDamage.setClickable(true);
                binding.lytTruckDamage.setEnabled(true);
                binding.lytTruckDamage.setAlpha(1.0f);

                binding.lytVariance.setClickable(true);
                binding.lytVariance.setEnabled(true);
                binding.lytVariance.setAlpha(1.0f);

                binding.lytExpiry.setClickable(true);
                binding.lytExpiry.setEnabled(true);
                binding.lytExpiry.setAlpha(1.0f);

                binding.lytFreshUnload.setClickable(false);
                binding.lytFreshUnload.setEnabled(false);
                binding.lytFreshUnload.setAlpha(0.5f);
            } else {
//            binding.lytROT.setClickable(false);
//            binding.lytROT.setEnabled(false);
//            binding.lytROT.setAlpha(0.5f);

                binding.lytEndInventory.setClickable(false);
                binding.lytEndInventory.setEnabled(false);
                binding.lytEndInventory.setAlpha(0.5f);

                binding.lytTruckDamage.setClickable(false);
                binding.lytTruckDamage.setEnabled(false);
                binding.lytTruckDamage.setAlpha(0.5f);

                binding.lytVariance.setClickable(false);
                binding.lytVariance.setEnabled(false);
                binding.lytVariance.setAlpha(0.5f);

                binding.lytExpiry.setClickable(false);
                binding.lytExpiry.setEnabled(false);
                binding.lytExpiry.setAlpha(0.5f);
            }

        }
    }

}
