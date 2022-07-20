package com.mobiato.sfa.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityShowMapCustomeBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.RecentCustomer;
import com.mobiato.sfa.utils.UtilApp;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowMapCustomeActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public ActivityShowMapCustomeBinding binding;
    private GoogleMap mMap;
    public static ArrayList<Customer> arrData = new ArrayList<>();
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowMapCustomeBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Journey Plan");

        db = new DBManager(this);

        //arrData = new ArrayList<>();

        // arrData = (ArrayList<Customer>) getIntent().getSerializableExtra("CustomerList");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

        ArrayList<Marker> arrMarker = new ArrayList<>();
        for (int i = 0; i < arrData.size(); i++) {

            if (arrData.get(i).getLatitude() != null && arrData.get(i).getLongitude() != null) {
                if (!arrData.get(i).getLatitude().isEmpty() && !arrData.get(i).getLongitude().isEmpty()) {
                    LatLng sydney = new LatLng(Double.parseDouble(arrData.get(i).getLatitude()), Double.parseDouble(arrData.get(i).getLongitude()));

                    Marker mMarker = mMap.addMarker(new MarkerOptions().position(sydney).title(arrData.get(i).getCustomerName()));
                    mMarker.setTag(arrData.get(i).getCustomerId());
                    mMarker.showInfoWindow();
                    arrMarker.add(mMarker);
                    // below line is use to add marker to each location of our array list.
                    // mMap.addMarker(new MarkerOptions().position(sydney).title(arrData.get(i).getCustomerName()));
                }
            }
            // below lin is use to zoom our camera on map.
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
//
//            // below line is use to move our camera to the specific location.
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : arrMarker) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 10; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        String position = (String) (marker.getTag());
        //openCustomer(position);
        showPopupInfo(position);
        return false;
    }

    public void openCustomer(String customerId) {

        Customer mCustomer = getCUstomer(customerId);
        UtilApp.shopDialog(ShowMapCustomeActivity.this, new OnSearchableDialog() {
            @Override
            public void onItemSelected(Object o) {
                String selection = (String) o;
                UtilApp.logData(ShowMapCustomeActivity.this, "Shop Status :" + selection);
                if (selection.equalsIgnoreCase("open")) {
                            /*  cusPosition = position;
                            custBarcode = arrData.get(position).getBarcode();
                            mCustomer = arrData.get(position);
                            Intent in = new Intent(FragmentJourneyPlan.this, SimpleScannerActivity.class);
                            startActivityForResult(in, App.REQUEST_BARCODE);
*/

                    Customer mCust = db.getCustomerDetail(mCustomer.getCustomerId());
                    System.out.println("id1--> " + mCust.getCustomerName());
                    RecentCustomer recentCustomer = new RecentCustomer();
                    recentCustomer.setCustomer_id(mCustomer.getCustomerId());
                    recentCustomer.setCustomer_name(mCustomer.getCustomerName());
                    recentCustomer.setDate_time(UtilApp.getCurrentDate());
                    db.insertRecentCustomer(recentCustomer);
                    App.isVisitInsert = false;
                    Intent in = new Intent(ShowMapCustomeActivity.this, CustomerDetailActivity.class);
                    in.putExtra("custmer", mCustomer);
                    //Add Customer in Recent
                    startActivity(in);
                }
            }
        });
    }

    public Customer getCUstomer(String customerId) {
        Customer mCustomer = null;

        for (int i = 0; i < arrData.size(); i++) {
            if (arrData.get(i).getCustomerId().equals(customerId)) {
                mCustomer = arrData.get(i);
                break;
            }
        }

        return mCustomer;
    }

    public void showPopupInfo(String customerId) {

        Customer mCustomer = getCUstomer(customerId);

        final Dialog alertDialog = new Dialog(ShowMapCustomeActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_customer_info);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvCode = (TextView) alertDialog.findViewById(R.id.tvCode);
        TextView tvOutLLet = (TextView) alertDialog.findViewById(R.id.tvOutlet);
        TextView tvOwner = (TextView) alertDialog.findViewById(R.id.tvOwner);
        TextView tvPhone = (TextView) alertDialog.findViewById(R.id.tvContact);

        tvCode.setText("Customer Code: " + mCustomer.getCustomerCode());
        tvOutLLet.setText("Outlet Name: " + mCustomer.getCustomerName());
        tvOwner.setVisibility(View.GONE);
        tvOwner.setText("Owner Name: " + mCustomer.getCustomerName2());
        tvPhone.setText("Contact No: " + mCustomer.getCustPhone());

        alertDialog.findViewById(R.id.txtProceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                alertDialog.dismiss();

                openCustomer(customerId);

            }
        });

        alertDialog.findViewById(R.id.txtBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}