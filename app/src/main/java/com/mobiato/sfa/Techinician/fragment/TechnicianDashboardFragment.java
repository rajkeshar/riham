package com.mobiato.sfa.Techinician.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.Fragments.FragmentOne;
import com.mobiato.sfa.Fragments.FragmentThree;
import com.mobiato.sfa.Fragments.FragmentTwo;
import com.mobiato.sfa.R;
import com.mobiato.sfa.Techinician.NotificationTechnicianActivity;
import com.mobiato.sfa.databinding.FragmentDashboardBinding;
import com.mobiato.sfa.model.NotificationTechnician;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.rest.BackgroundSync;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.target.ViewTarget;

/**
 * A simple {@link Fragment} subclass.
 */
public class TechnicianDashboardFragment extends BaseActivity {

    FragmentDashboardBinding binding;
    private MaterialShowcaseSequence sequence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentDashboardBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setNotification_technician();

        UtilApp.logData(TechnicianDashboardFragment.this, "Dashboard OnScreen");

        sequence = new MaterialShowcaseSequence(TechnicianDashboardFragment.this);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        sequence.setConfig(config);

        //show Showcase View
        //showTutorial();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(TechnicianDashboardFragment.this, BackgroundSync.class));
        } else {
            startService(new Intent(TechnicianDashboardFragment.this, BackgroundSync.class));
        }

        setNavigationView();
        setTitle(getString(R.string.nav_home));
        binding.tabDots.setVisibility(View.GONE);
        binding.tabDots.setupWithViewPager(binding.pager, true);

        binding.pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onPageSelected(int position) {
                // Check if this is the page you want.
            }
        });
       // getCustomer_technician();

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    TechnicianFragmentThree fragmentAnimation1 = new TechnicianFragmentThree();
                    return fragmentAnimation1;

               /* case 1:
                    FragmentTwo fragmentAnimation2 = new FragmentTwo();
                    return fragmentAnimation2;

                case 2:
                    FragmentThree fragmentAnimation3 = new FragmentThree();
                    return fragmentAnimation3;*/

            }
            return null;
        }

    }

    private void showTutorial() {

        ViewTarget target = new ViewTarget(baseBinding.mToolbar.findViewById(android.R.id.home));

        sequence.addSequenceItem(target.getView(),
                "Click here to explore more options!", "NEXT");

        sequence.addSequenceItem(binding.tabDots,
                "Swipe left to get more idea of sales!", "GOT IT");

        sequence.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setNotification_technician();
    }


}
