package com.mobiato.sfa.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityCustomerLocationBinding;
import com.mobiato.sfa.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerLocationActivity extends BaseActivity implements OnMapReadyCallback {

    public ActivityCustomerLocationBinding binding;
    public Customer mCustomer;
    private GoogleMap mMap;
    LocationManager locationManager;
    LatLng customerLocation, salesmanLocation;
    private String stRDistance = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerLocationBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);

        mCustomer = (Customer) getIntent().getSerializableExtra("custmer");
        setTitle(mCustomer.getCustomerName());

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


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<Marker> arrMarker = new ArrayList<>();

        if (mCustomer.getLatitude() != null && mCustomer.getLongitude() != null && !mCustomer.getLatitude().equalsIgnoreCase("")
                && !mCustomer.getLongitude().equalsIgnoreCase("")) {
            // Add a marker in Sydney, Australia, and move the camera.

            customerLocation = new LatLng(Double.parseDouble(mCustomer.getLatitude()), Double.parseDouble(mCustomer.getLongitude()));

            salesmanLocation = new LatLng(Double.parseDouble(App.Latitude), Double.parseDouble(App.Longitude));

            Location startPoint = new Location("locationA");
            startPoint.setLatitude(Double.parseDouble(App.Latitude));
            startPoint.setLongitude(Double.parseDouble(App.Longitude));

            Location endPoint = new Location("locationB");
            endPoint.setLatitude(Double.parseDouble(mCustomer.getLatitude()));
            endPoint.setLongitude(Double.parseDouble(mCustomer.getLongitude()));

            double distance = startPoint.distanceTo(endPoint);
            double km = distance / 1000;
            stRDistance = String.valueOf(km);
            binding.tvDistance.setText("Distance : " + stRDistance + " km");

            Marker mMarker = mMap.addMarker(new MarkerOptions().position(customerLocation).title(mCustomer.getCustomerName()));
            mMarker.showInfoWindow();
            arrMarker.add(mMarker);

            mMarker = mMap.addMarker(new MarkerOptions().position(salesmanLocation).title("You"));
            mMarker.showInfoWindow();
            arrMarker.add(mMarker);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : arrMarker) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 10; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);

//            mMap.addMarker(new MarkerOptions().position(customerLocation).title(mCustomer.getCustomerName()));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(customerLocation));


        }

    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            App.Latitude = String.valueOf(latitude);
            App.Longitude = String.valueOf(longitude);
            String msg = "New Latitude: " + App.Latitude + "New Longitude: " + App.Longitude;

            salesmanLocation = new LatLng(Double.parseDouble(App.Latitude), Double.parseDouble(App.Longitude));

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

}
