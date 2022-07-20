package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityCustomerProfileBinding;
import com.mobiato.sfa.databinding.RowCustomerProfileBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Distribution;

import java.util.ArrayList;

public class CustomerProfileActivity extends BaseActivity implements OnMapReadyCallback {

    private ActivityCustomerProfileBinding binding;
    private GoogleMap mMap;
    private DBManager db;
    private Customer customer;
    private Intent intent;
    private ArrayList<Distribution> arrToolsData = new ArrayList<>();
    private CommonAdapter<Distribution> mAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerProfileBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Customer Profile");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent = getIntent();
        String id = intent.getStringExtra("id");
        db = new DBManager(this);
        customer = db.getCustomerDetail(id);
        arrToolsData = db.getCustomerToolsList(customer.getCustomerId());
        setMainAdapter1();

        binding.txtCustomerName.setText(customer.getCustomerName());
        binding.txtStoreName.setText("");
        binding.txtCustomerContact.setText(customer.getCustPhone());
        binding.txtCustomerCode.setText(customer.getCustomerCode());
        binding.txtVisitDate.setText("");
        binding.txtSelfChannel.setText("");

        if (customer.getCustType().equals("1")) {
            binding.txtChainName.setText("Cash Customer");

        } else if (customer.getCustType().equals("2")) {
            binding.txtChainName.setText("Credit Customer");

        } else if (customer.getCustType().equals("3")) {
            binding.txtChainName.setText("TC Customer");

        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        binding.rvprofileList.setLayoutManager(gridLayoutManager);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        if (!customer.getLatitude().equalsIgnoreCase("") && !customer.getLongitude().equalsIgnoreCase("")) {
            LatLng TutorialsPoint = new LatLng(Double.parseDouble(customer.getLatitude()), Double.parseDouble(customer.getLongitude()));
            mMap.addMarker(new
                    MarkerOptions().position(TutorialsPoint).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
        }
    }

    private void setMainAdapter1() {

        mAdapter1 = new CommonAdapter<Distribution>(arrToolsData) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowCustomerProfileBinding) {
                    ((RowCustomerProfileBinding) holder.binding).txtDisplayName.setText(arrToolsData.get(position).getDistribution_Tool_Name());
                    holder.binding.executePendingBindings();
                }
            }
            @Override
            public int getItemViewType(int position) {
                return R.layout.row_customer_profile;
            }
        };

        binding.rvprofileList.setAdapter(mAdapter1);
    }
}
