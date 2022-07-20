package com.mobiato.sfa.Techinician;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityCustomerLocationBinding;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerTechnician;

public class CustomerTechnicianLocationActivity extends BaseActivity implements OnMapReadyCallback {

    public ActivityCustomerLocationBinding binding;
    public CustomerTechnician mCustomer;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerLocationBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);

        mCustomer = (CustomerTechnician) getIntent().getSerializableExtra("custmer");
        setTitle(mCustomer.getCustomername());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mCustomer.getLocation() != null) {
            // Add a marker in Sydney, Australia, and move the camera.
            String split[]=mCustomer.getLocation().split(",");
            System.out.println("check-->"+split[0]);
            LatLng sydney = new LatLng(Double.parseDouble(split[0]),Double.parseDouble(split[1]));
            mMap.addMarker(new MarkerOptions().position(sydney).title(mCustomer.getCustomername()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

    }
}
