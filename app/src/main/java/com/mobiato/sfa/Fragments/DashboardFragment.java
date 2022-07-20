package com.mobiato.sfa.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.CustomerDetailActivity;
import com.mobiato.sfa.databinding.FragmentDashboardBinding;
import com.mobiato.sfa.rest.BackgroundSync;
import com.mobiato.sfa.utils.UtilApp;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.target.ViewTarget;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends BaseActivity {

    FragmentDashboardBinding binding;
    private MaterialShowcaseSequence sequence;
    LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentDashboardBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        setNotification();

        UtilApp.logData(DashboardFragment.this, "Dashboard OnScreen");

        sequence = new MaterialShowcaseSequence(DashboardFragment.this);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        sequence.setConfig(config);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, locationListenerGPS);

        //show Showcase View
        //showTutorial();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(DashboardFragment.this, BackgroundSync.class));
        } else {
            startService(new Intent(DashboardFragment.this, BackgroundSync.class));
        }

        setNavigationView();
        setTitle(getString(R.string.nav_home));
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

        //checkPermision();
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            App.Latitude = String.valueOf(latitude);
            App.Longitude = String.valueOf(longitude);
            String msg = "New Latitude: " + App.Latitude + "New Longitude: " + App.Longitude;
            //Toast.makeText(CustomerDetailActivity.this, msg, Toast.LENGTH_LONG).show();
            // insertVisit();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    FragmentOne fragmentAnimation1 = new FragmentOne();
                    return fragmentAnimation1;

                case 1:
                    FragmentTwo fragmentAnimation2 = new FragmentTwo();
                    return fragmentAnimation2;

                case 2:
                    FragmentThree fragmentAnimation3 = new FragmentThree();
                    return fragmentAnimation3;

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

    public void checkPermision() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);

        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
        } else {
            if (!UtilApp.canGetLocation) {
                // System.out.println("print1-->");
                UtilApp.initialiseLocationVariables(DashboardFragment.this, new UtilApp.LocationUpdate() {
                    @Override
                    public void onLocationDetect(boolean isDetect) {
                        if (isDetect) {

                            UtilApp.logData(DashboardFragment.this, "Location Fetch:" + App.Latitude + " ," +
                                    App.Longitude);

                        }
                    }
                });

                UtilApp.startLocationUpdates(DashboardFragment.this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNotification();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                //checkPermision();
                break;
        }
    }


}
