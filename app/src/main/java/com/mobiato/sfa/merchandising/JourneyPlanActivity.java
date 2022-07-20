package com.mobiato.sfa.merchandising;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.MerchanCustomerAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.AddNewCustomerActivity;
import com.mobiato.sfa.activity.CustomerDetailActivity;
import com.mobiato.sfa.activity.DownloadingDataActivity;
import com.mobiato.sfa.activity.UpdateCustomer.UpdateExistingCustomerActivity;
import com.mobiato.sfa.databinding.ActivityJourneyPlanBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerType;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class JourneyPlanActivity extends BaseActivity implements OnMapReadyCallback {

    public ActivityJourneyPlanBinding binding;

    private DBManager db;
    private ArrayList<Customer> arrData = new ArrayList<>();
    private MerchanCustomerAdapter mAdapter;
    private GoogleMap mMap;
    boolean IsClick = false;
    private static final int ZOOM_LEVEL = 15;
    private static final int TILT_LEVEL = 0;
    private static final int BEARING_LEVEL = 0;
    private String custBarcode = "";
    private Customer mCustomer;
    private int cusPosition = 0;
    private Location mSalesmanLC, mCustomerL;
    private LoadingSpinner progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJourneyPlanBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setNavigationView();
        setTitle("Journey Plan");

        progressDialog = new LoadingSpinner(JourneyPlanActivity.this);
        db = new DBManager(JourneyPlanActivity.this);

        checkPermision();

        //get Customer List
        arrData = new ArrayList<>();
        arrData = db.getCutomerList();
        setMainAdapter();

        binding.txtCountCustomer.setText("Showing Total " + arrData.size() + " Customers");

        binding.layoutMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsClick) {
                    binding.imgMap.setImageResource(R.drawable.place_dark);
                    binding.layoutFramemap.setVisibility(View.GONE);
                    binding.rvJourneyList.setVisibility(View.VISIBLE);
                    IsClick = false;
                } else {
                    binding.imgMap.setImageResource(R.drawable.ic_list_black);
                    binding.layoutFramemap.setVisibility(View.VISIBLE);
                    binding.rvJourneyList.setVisibility(View.GONE);

                    IsClick = true;
                }
            }
        });

        binding.btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(db.getCustomerCategoryCount()) > 0) {
                    openCustomerActivity();
                } else {
                    getCategoryList();
                }

            }
        });

    }

    private void openCustomerActivity() {
        startActivity(new Intent(JourneyPlanActivity.this, AddNewCustomerActivity.class)
                .putExtra("Type", "Merchandiser"));
    }

    private void setMainAdapter() {

        mAdapter = new MerchanCustomerAdapter(JourneyPlanActivity.this, arrData, new MerchanCustomerAdapter.ContactsAdapterListener() {
            @Override
            public void onContactSelected(Customer contact) {

                mCustomer = contact;
                App.strCustomerId = mCustomer.getCustomerId();
                Intent intent = new Intent(JourneyPlanActivity.this, CustomerOperationActivity.class);
                intent.putExtra("id", mCustomer.getCustomerId());
                intent.putExtra("type", "Merchandising");
                startActivity(intent);
            }
        });

        binding.rvJourneyList.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setHint("Search");
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));

        ImageView searchMagIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchMagIcon.setImageResource(R.drawable.ic_action_action_search);

        ImageView searchMagCloseIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchMagCloseIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (arrData.size() > 0) {
            for (int i = 0; i < arrData.size(); i++) {

                Customer mCustomer = arrData.get(i);
                if (!mCustomer.getLatitude().equalsIgnoreCase("") && !mCustomer.getLongitude().equalsIgnoreCase("")) {

                    LatLng position = new LatLng(Double.parseDouble(mCustomer.getLatitude()), Double.parseDouble(mCustomer.getLongitude()));
                    createMarker(Double.parseDouble(mCustomer.getLatitude()), Double.parseDouble(mCustomer.getLongitude()), mCustomer.getCustomerName(),
                            mCustomer.getAddress());

                    if (i == 0) {
                        CameraPosition camPos = new CameraPosition(position, ZOOM_LEVEL, TILT_LEVEL, BEARING_LEVEL);
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos));
                    }
                }

            }
        }
    }

    protected Marker createMarker(double latitude, double longitude, String title, String snippet) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case App.REQUEST_BARCODE:
                if (resultCode == 2) {
                    UtilApp.logData(JourneyPlanActivity.this, "Barcode Scan");
                    if (data.getStringExtra("code").equalsIgnoreCase(custBarcode)) {
                        if (!mCustomer.getLatitude().equalsIgnoreCase("")) {
                            mCustomerL = new Location("Customer");
                            mCustomerL.setLatitude(Double.parseDouble(mCustomer.getLatitude()));
                            mCustomerL.setLongitude(Double.parseDouble(mCustomer.getLongitude()));
                        }
                        if (mSalesmanLC != null && mCustomerL != null) {
                            float distance = UtilApp.getRadius(mSalesmanLC, mCustomerL);

                            if (!mCustomer.getRadius().equalsIgnoreCase("")) {
                                if (distance > Double.parseDouble(mCustomer.getRadius())) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JourneyPlanActivity.this);
                                    alertDialogBuilder.setTitle("Alert")
                                            .setMessage("You are not in customer location!")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    openActivity(cusPosition);
                                                }
                                            });
                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    // show it
                                    alertDialog.show();
                                } else {
                                    openActivity(cusPosition);
                                }
                            }
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JourneyPlanActivity.this);
                            alertDialogBuilder.setTitle("Alert")
                                    .setMessage("Salesman & Customer Location not found!")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            openActivity(cusPosition);
                                        }
                                    });
                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            // show it
                            alertDialog.show();
                        }
                    } else {
                        UtilApp.displayErrorDialog(JourneyPlanActivity.this, "Customer Barcode does not match!");
                    }
                }
        }
    }

    public void openActivity(int position) {
        UtilApp.logData(JourneyPlanActivity.this, "Call OpenActivity");
        App.strCustomerId = arrData.get(position).getCustomerId();
        Intent intent = new Intent(JourneyPlanActivity.this, CustomerOperationActivity.class);
        intent.putExtra("id", arrData.get(position).getCustomerId());
        intent.putExtra("type", "Merchandising");
        startActivity(intent);
    }

    public void checkPermision() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);

        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
        } else {

//            if (!UtilApp.canGetLocation) {
//                UtilApp.initialiseLocationVariables(JourneyPlanActivity.this, new UtilApp.LocationUpdate() {
//                    @Override
//                    public void onLocationDetect(boolean isDetect) {
//                        if (isDetect) {
//                            UtilApp.logData(JourneyPlanActivity.this, "Location Fetch:" + App.Latitude + " ," +
//                                    App.Longitude);
//                            mSalesmanLC = new Location("Salesman");
//                            mSalesmanLC.setLatitude(Double.parseDouble(App.Latitude));
//                            mSalesmanLC.setLongitude(Double.parseDouble(App.Longitude));
//                        }
//                    }
//                });
//
//                UtilApp.startLocationUpdates(JourneyPlanActivity.this);
//            }
        }
    }

    private void getCategoryList() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.CUSTOMER_CATEGORY);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Good Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(JourneyPlanActivity.this, "Free Good Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<CustomerType> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<CustomerType>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_CUSTOMER_CATEGORY);
                        db.insertCustomerCategory(arrItems);
                    }
                }

                progressDialog.hide();
                openCustomerActivity();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Good Fail", error.getMessage());
                progressDialog.hide();
                UtilApp.logData(JourneyPlanActivity.this, "Free Good Failure: " + error.getMessage());
                openCustomerActivity();

            }
        });
    }

}
