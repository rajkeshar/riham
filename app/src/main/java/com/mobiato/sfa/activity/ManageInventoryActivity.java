package com.mobiato.sfa.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.mobiato.sfa.Adapter.PagerAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.Fragments.LoadFragment;
import com.mobiato.sfa.Fragments.UnloadFragment;
import com.mobiato.sfa.Fragments.VanStockFragment;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityManageInventoryBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

public class ManageInventoryActivity extends BaseActivity {

    public ActivityManageInventoryBinding binding;
    private PagerAdapter pagerAdapter;
    private int currentPage = 0;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageInventoryBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setNavigationView();
        setTitle(getString(R.string.nav_inventory));

        db = new DBManager(this);
        setupViewPager();
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 2) {
                    if (!db.checkIfLoadExists()) {
                        currentPage = i;
                    } else {
                        UtilApp.displayAlert(me, "Please accept pending load before doing unload.");
                        binding.viewPager.setCurrentItem(currentPage);
                    }
                } else {
                    if (Settings.getString(App.IS_LOAD_VERIFY).equalsIgnoreCase("1"))
                        currentPage = i;
                    else
                        binding.viewPager.setCurrentItem(currentPage);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void setupViewPager() {

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(LoadFragment.newInstance(), getString(R.string.tab_load));
        pagerAdapter.addFragment(VanStockFragment.newInstance(), getString(R.string.tab_van_stock));
        pagerAdapter.addFragment(UnloadFragment.newInstance(), getString(R.string.tab_unload));

        binding.viewPager.setAdapter(pagerAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pagerAdapter != null) {
            if (currentPage == 2) {
                setNavigationView();
                ((UnloadFragment) pagerAdapter.getItem(currentPage)).disableOption();
            } else if (currentPage == 0) {
                ((LoadFragment) pagerAdapter.getItem(currentPage)).refreshLoad();
            }
        }
    }
}
