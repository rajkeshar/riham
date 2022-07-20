package com.mobiato.sfa.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.FragmentOneBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOne extends Fragment {

    private Boolean isStarted = false;
    private Boolean isVisible = false;
    private DBManager db;

    public FragmentOne() {
        // Required empty public constructor
    }

    static FragmentManager fragmentManager;


    public static Fragment newInstance(FragmentManager fragmentManager1) {
        FragmentOne fragment = new FragmentOne();
        fragmentManager = fragmentManager1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    private FragmentOneBinding binding;
    private int saleTraget = 0, prevSale = 0, selectedCat = 2;
    private Salesman mSalesman;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_one, container, false);
        View view = binding.getRoot();

        db = new DBManager(getActivity());
        mSalesman = Settings.getSalesmanData(App.SALESMAN);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("CSD"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Juice"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Water"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Biscuit"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Hamper"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Confectionary"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    selectedCat = 2;
                    jumpTime =0;
                    if (mSalesman.getCSDTarget() != null && !mSalesman.getCSDTarget().equalsIgnoreCase(""))
                        saleTraget = Integer.parseInt(mSalesman.getCSDTarget());
                    if (mSalesman.getAchiveCSDTarget() != null && !mSalesman.getAchiveCSDTarget().equalsIgnoreCase(""))
                        prevSale = Integer.parseInt(mSalesman.getAchiveCSDTarget());
                } else if (tab.getPosition() == 1) {
                    selectedCat = 4;
                    jumpTime =0;
                    if (mSalesman.getJuiceTarget() != null && !mSalesman.getJuiceTarget().equalsIgnoreCase(""))
                        saleTraget = Integer.parseInt(mSalesman.getJuiceTarget());
                    if (mSalesman.getAchiveJuiceTarget() != null && !mSalesman.getAchiveJuiceTarget().equalsIgnoreCase(""))
                        prevSale = Integer.parseInt(mSalesman.getAchiveJuiceTarget());
                } else if (tab.getPosition() == 2) {
                    selectedCat = 3;
                    jumpTime =0;
                    if (mSalesman.getWaterTarget() != null && !mSalesman.getWaterTarget().equalsIgnoreCase(""))
                        saleTraget = Integer.parseInt(mSalesman.getWaterTarget());
                    if (mSalesman.getAchiveWaterTarget() != null && !mSalesman.getAchiveWaterTarget().equalsIgnoreCase(""))
                        prevSale = Integer.parseInt(mSalesman.getAchiveWaterTarget());
                }else if (tab.getPosition() == 3) {
                    selectedCat = 1;
                    jumpTime =0;
                    if (mSalesman.getBiscutsTarget() != null && !mSalesman.getBiscutsTarget().equalsIgnoreCase(""))
                        saleTraget = Integer.parseInt(mSalesman.getBiscutsTarget());
                    if (mSalesman.getAchiveBiscutsTarget() != null && !mSalesman.getAchiveBiscutsTarget().equalsIgnoreCase(""))
                        prevSale = Integer.parseInt(mSalesman.getAchiveBiscutsTarget());
                } else if (tab.getPosition() == 4) {
                    selectedCat = 5;
                    jumpTime =0;
                    if (mSalesman.getHamperTarget() != null && !mSalesman.getHamperTarget().equalsIgnoreCase(""))
                        saleTraget = Integer.parseInt(mSalesman.getHamperTarget());
                    if (mSalesman.getAchiveHamperTarget() != null && !mSalesman.getAchiveHamperTarget().equalsIgnoreCase(""))
                        prevSale = Integer.parseInt(mSalesman.getAchiveHamperTarget());

                }else{
                    selectedCat = 6;
                    jumpTime =0;
                    if (mSalesman.getAchiveConfectionaryTarget() != null && !mSalesman.getAchiveConfectionaryTarget().equalsIgnoreCase(""))
                        saleTraget = Integer.parseInt(mSalesman.getAchiveConfectionaryTarget());
                    if (mSalesman.getAchiveConfectionaryTarget() != null && !mSalesman.getAchiveConfectionaryTarget().equalsIgnoreCase(""))
                        prevSale = Integer.parseInt(mSalesman.getAchiveConfectionaryTarget());
                }
                if (binding.swcDtdMtd.isChecked()) {
                    setMtd(selectedCat);
                } else {
                    setDtd(selectedCat);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (mSalesman.getCSDTarget() != null && !mSalesman.getCSDTarget().equalsIgnoreCase(""))
            saleTraget = Integer.parseInt(mSalesman.getCSDTarget());
        if (mSalesman.getAchiveCSDTarget() != null && !mSalesman.getAchiveCSDTarget().equalsIgnoreCase(""))
            prevSale = Integer.parseInt(mSalesman.getAchiveCSDTarget());

        binding.swcDtdMtd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    if (saleTraget > 0)
                        binding.txtTotalSales.setText("of " + Math.round((saleTraget / 30)));
                    setDtd(selectedCat);
                } else {
                    binding.txtTotalSales.setText("of " + Math.round(saleTraget));
                    setMtd(selectedCat);
                }
            }
        });

        if (saleTraget > 0)
            binding.txtTotalSales.setText("of " + Math.round((saleTraget / 30)));
        else
            binding.txtTotalSales.setText("of 0");
        setDtd(selectedCat);

        return view;
    }

    public void setDtd(int categoryId) {

        if (saleTraget > 0)
            binding.txtTotalSales.setText("of " + Math.round((saleTraget / 30)));
        else
            binding.txtTotalSales.setText("of 0");

        int todaySale = db.getTotalCategoryCaseSale(String.valueOf(categoryId));
        binding.tvMainSell.setText("" + Math.round(todaySale));

        int totalProgressSales = UtilApp.getSalePercent((saleTraget / 30), todaySale);
       /* if (totalProgressSales == 0) {
            binding.progressMain.setProgressValue2(0);
        }

        if (totalProgressSales >= 70)
            binding.progressMain.setProgressValue2Color(getResources().getColor(R.color.progress2));
        else
            binding.progressMain.setProgressValue2Color(getResources().getColor(R.color.creditCustomer));
*/
        if(totalProgressSales!=0) {
            if (totalProgressSales >= 70)
                binding.progressMain.setProgressValue2Color(getResources().getColor(R.color.progress2));
            else
                binding.progressMain.setProgressValue2Color(getResources().getColor(R.color.creditCustomer));
        }else {
            binding.progressMain.setProgressValue2(0);
        }

        try {
            if(totalProgressSales!=0) {
                binding.progressMain.setProgressValue2(100);
                int finalTotalProgressSales = totalProgressSales;
                final Thread t = new Thread() {
                    @Override
                    public void run() {
                        jumpTime = 0;

                        try {
                            while (jumpTime < 100) {
                                try {
                                    sleep(50);
                                    jumpTime += 10;
                                  //  binding.progressMain.setProgressValue2(jumpTime);

                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    int jumpTime = 0;
    public void setMtd(int categoryId) {

        binding.txtTotalSales.setText("of " + Math.round((saleTraget)));
        int todaySale = db.getTotalCategoryCaseSale(String.valueOf(categoryId));
        int totalSale = prevSale + todaySale;

        binding.tvMainSell.setText("" + Math.round(totalSale));

        int totalProgressSales = UtilApp.getSalePercent(saleTraget, totalSale);
        System.out.println("PP-->"+totalProgressSales);

        if(totalProgressSales!=0) {
            if (totalProgressSales >= 70)
                binding.progressMain.setProgressValue2Color(getResources().getColor(R.color.progress2));
            else
                binding.progressMain.setProgressValue2Color(getResources().getColor(R.color.creditCustomer));
        }else {
            binding.progressMain.setProgressValue2(0);
        }


        try {
            if(totalProgressSales!=0) {
                binding.progressMain.setProgressValue2(100);
                int finalTotalProgressSales = totalProgressSales;
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        jumpTime = 0;

                        try {
                            while (jumpTime < 100) {
                                try {
                                    sleep(50);
                                    jumpTime += 2;
                                   // binding.progressMain.setProgressValue2(jumpTime);

                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
    }


    @Override
    public void onStart() {
        super.onStart();

        isStarted = true;
        if (isVisible && isStarted) {
            viewInit();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isStarted && isVisible) {
            viewInit();
        }
    }

    private void viewInit() {
        setDtd(selectedCat);
    }
}
