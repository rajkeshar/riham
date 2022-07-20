package com.mobiato.sfa.activity;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.Adapter.CustomerAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.UpdateCustomer.UpdateExistingCustomerActivity;
import com.mobiato.sfa.databinding.FragmentJourneyPlanBinding;
import com.mobiato.sfa.databinding.RowRecentCustBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerType;
import com.mobiato.sfa.model.RecentCustomer;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentJourneyPlan extends BaseActivity {

    FragmentJourneyPlanBinding binding;
    boolean isSequence = false;
    boolean isAll = false;

    private ArrayList<Customer> arrData = new ArrayList<>();
    private ArrayList<Customer> arrRecent = new ArrayList<>();
    private ArrayList<Customer> arrOTC = new ArrayList<>();
    private ArrayList<Customer> arrOTC_send = new ArrayList<>();
    private CustomerAdapter mAdapter;
    private CommonAdapter<Customer> mRecentAdapter;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private DBManager db;
    private Location mSalesmanLC, mCustomerL;
    private String custBarcode = "";
    private Customer mCustomer;
    private String mSelectType = "Depot";
    LocationManager locationManager;
    private LoadingSpinner progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentJourneyPlanBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setNavigationView();
        setTitle(getString(R.string.nav_journey));

        progressDialog = new LoadingSpinner(FragmentJourneyPlan.this);

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

        UtilApp.logData(FragmentJourneyPlan.this, "Journey Plan OnScreen");

        db = new DBManager(this);

        checkPermision();

        //get Customer List
        arrData = new ArrayList<>();
        //arrData = db.getCutomerList();
        //arrData = db.getSeqCustomerList(UtilApp.getCurrentDay());
        arrData = db.getCutomerDepotList();

        arrRecent = new ArrayList<>();
        arrRecent = db.getRecentList();

        arrOTC = new ArrayList<>();
        arrOTC_send = new ArrayList<>();

        setMainAdapter();
        setRecentAdapter();

        if (UtilApp.salesmanRole(Settings.getString(App.ROLE)).equalsIgnoreCase("Hariss salesman")) {
            binding.fab.setVisibility(View.VISIBLE);
        }

        if (isSequence) {
            binding.btnSequence.setBackgroundResource(R.drawable.rounded_tab_select);
            binding.btnSequence.setTextColor(Color.parseColor("#FFFFFF"));
            binding.btnAll.setBackgroundResource(R.drawable.rounded_tab_unselect);
            binding.btnAll.setTextColor(Color.parseColor("#606AA9"));
            binding.btnDepot.setBackgroundResource(R.drawable.rounded_tab_unselect);
            binding.btnDepot.setTextColor(Color.parseColor("#606AA9"));
        } else {
            binding.btnAll.setBackgroundResource(R.drawable.rounded_tab_select);
            binding.btnAll.setTextColor(Color.parseColor("#FFFFFF"));
            binding.btnSequence.setBackgroundResource(R.drawable.rounded_tab_unselect);
            binding.btnSequence.setTextColor(Color.parseColor("#606AA9"));
            binding.btnDepot.setBackgroundResource(R.drawable.rounded_tab_unselect);
            binding.btnDepot.setTextColor(Color.parseColor("#606AA9"));
        }

        binding.btnAll.setVisibility(View.GONE);
        binding.btnSequence.setVisibility(View.GONE);
        binding.btnDepot.setVisibility(View.GONE);
        binding.btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectType = "All";
                isSequence = false;
                isAll = true;
                binding.btnAll.setBackgroundResource(R.drawable.rounded_tab_select);
                binding.btnAll.setTextColor(Color.parseColor("#FFFFFF"));
                binding.btnSequence.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnSequence.setTextColor(Color.parseColor("#606AA9"));
                binding.btnDepot.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnDepot.setTextColor(Color.parseColor("#606AA9"));
                binding.recyclerviewMain.setVisibility(View.VISIBLE);
                binding.otcCustomer.setVisibility(View.VISIBLE);
                arrData = new ArrayList<>();
                arrData = db.getCutomerList();


                setMainAdapter();
            }
        });

        // getCustomer_otc();
        getCustomer_otc_final();
        binding.btnDepot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectType = "Depot";
                isSequence = false;
                isAll = false;
                binding.btnAll.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnAll.setTextColor(Color.parseColor("#606AA9"));
                binding.btnSequence.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnSequence.setTextColor(Color.parseColor("#606AA9"));
                binding.btnDepot.setBackgroundResource(R.drawable.rounded_tab_select);
                binding.btnDepot.setTextColor(Color.parseColor("#FFFFFF"));
                arrData = new ArrayList<>();
                arrData = db.getCutomerDepotList();
                setMainAdapter();
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                // getCustomer();
            }
        });

        binding.btnSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectType = "Today";
                isSequence = true;
                isAll = false;
                binding.btnSequence.setBackgroundResource(R.drawable.rounded_tab_select);
                binding.btnSequence.setTextColor(Color.parseColor("#FFFFFF"));
                binding.btnAll.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnAll.setTextColor(Color.parseColor("#606AA9"));
                binding.btnDepot.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnDepot.setTextColor(Color.parseColor("#606AA9"));
                arrData = new ArrayList<>();
                arrData = db.getSeqCustomerList(UtilApp.getCurrentDay());
                binding.recyclerviewMain.setVisibility(View.VISIBLE);
                binding.otcCustomer.setVisibility(View.VISIBLE);
                setMainAdapter();
            }
        });


        binding.otcCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Customer mCust = db.getCustomerDetail(arrOTC_send.get(0).getCustomerId());
                mCust.setRouteId(Settings.getString(App.ROUTEID));
                Intent in = new Intent(FragmentJourneyPlan.this, SalesActivity.class);
                in.putExtra("customer", mCust);
                in.putExtra("customer_type", "OTC");
                RecentCustomer recentCustomer = new RecentCustomer();
                recentCustomer.setCustomer_id(arrOTC_send.get(0).getCustomerId());
                recentCustomer.setCustomer_name(arrOTC_send.get(0).getCustomerName());
                recentCustomer.setDate_time(UtilApp.getCurrentDate());
                db.insertRecentCustomer(recentCustomer);
                startActivity(in);

            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilApp.logData(FragmentJourneyPlan.this, "OnButton Click :- Add Customer");
                if (Integer.parseInt(db.getCustomerCategoryCount()) > 0) {
                    openCustomerActivity();
                } else {
                    getCategoryList();
                }

            }
        });
    }

    //Deport AGENT
    private void getCategoryList() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.CUSTOMER_CATEGORY);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Good Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(FragmentJourneyPlan.this, "Free Good Response: " + response.body().toString());
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
                UtilApp.logData(FragmentJourneyPlan.this, "Free Good Failure: " + error.getMessage());
                openCustomerActivity();
            }
        });
    }

    private void openCustomerActivity() {
        startActivity(new Intent(FragmentJourneyPlan.this, AddNewCustomerActivity.class)
                .putExtra("Type", "Sales")
                .putExtra("CusType", "1"));
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

    private Customer getCustomerDetail(String code) {
        Customer mCustomer = null;

        for (int i = 0; i < arrData.size(); i++) {
            if (arrData.get(i).getCustomerCode().equals(code)) {
                mCustomer = arrData.get(i);
                break;
            }
        }
        return mCustomer;
    }

    private void setMainAdapter() {

        mAdapter = new CustomerAdapter(this, arrData, new CustomerAdapter.ContactsAdapterListener() {
            @Override
            public void onContactSelected(Customer contact) {

               /* mCustomer = contact;
                Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
                in.putExtra("custmer", contact);
                App.isVisitInsert = false;
                //Add Customer in Recent
                RecentCustomer recentCustomer = new RecentCustomer();
                recentCustomer.setCustomer_id(contact.getCustomerId());
                recentCustomer.setCustomer_name(contact.getCustomerName());
                recentCustomer.setDate_time(UtilApp.getCurrentDate());
                db.insertRecentCustomer(recentCustomer);
                startActivity(in);*/

                // scanBarcode();

                UtilApp.shopDialog(FragmentJourneyPlan.this, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        String selection = (String) o;
                        UtilApp.logData(FragmentJourneyPlan.this, "Shop Status :" + selection);
                        if (selection.equalsIgnoreCase("open")) {
                            /*  cusPosition = position;
                            custBarcode = arrData.get(position).getBarcode();
                            mCustomer = arrData.get(position);
                            Intent in = new Intent(FragmentJourneyPlan.this, SimpleScannerActivity.class);
                            startActivityForResult(in, App.REQUEST_BARCODE);
*/
                            mCustomer = contact;
                            Customer mCust = db.getCustomerDetail(mCustomer.getCustomerId());
                            System.out.println("id1--> " + mCust.getCustomerName());
                            RecentCustomer recentCustomer = new RecentCustomer();
                            recentCustomer.setCustomer_id(contact.getCustomerId());
                            recentCustomer.setCustomer_name(contact.getCustomerName());
                            recentCustomer.setDate_time(UtilApp.getCurrentDate());
                            db.insertRecentCustomer(recentCustomer);
                            App.isVisitInsert = false;
                            Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
                            in.putExtra("custmer", contact);
                            //Add Customer in Recent
                            startActivity(in);
                        } else {

                        }
                    }
                });

            }
        });

        binding.recyclerviewMain.setAdapter(mAdapter);
    }

    private void setRecentAdapter() {

        mRecentAdapter = new CommonAdapter<Customer>(arrRecent) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowRecentCustBinding) {
                    ((RowRecentCustBinding) holder.binding).setItem(arrRecent.get(position));
                    ((RowRecentCustBinding) holder.binding).icon.setLetter(arrRecent.get(position).getCustomerName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowRecentCustBinding) holder.binding).icon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    holder.binding.executePendingBindings();

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UtilApp.logData(FragmentJourneyPlan.this, "Click On Recent Customer");

                           /* Customer mCust = db.getCustomerDetail(arrRecent.get(position).getCustomerId());
                            App.isVisitInsert = false;
                            Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
                            in.putExtra("custmer", mCust);
                            startActivity(in);*/

                            UtilApp.shopDialog(FragmentJourneyPlan.this, new OnSearchableDialog() {
                                @Override
                                public void onItemSelected(Object o) {
                                    String selection = (String) o;
                                    UtilApp.logData(FragmentJourneyPlan.this, "Shop Status :" + selection);
                                    if (selection.equalsIgnoreCase("open")) {

                                        /* cusPosition = position;
                            custBarcode = arrData.get(position).getBarcode();
                            mCustomer = arrData.get(position);
                            Intent in = new Intent(FragmentJourneyPlan.this, SimpleScannerActivity.class);
                            startActivityForResult(in, App.REQUEST_BARCODE);
*/
                                        Customer mCust = db.getCustomerDetail(arrRecent.get(position).getCustomerId());
                                        System.out.println("id1--> " + mCust.getCustomerName());
                                        try {
                                            if (mCust.getCustomerType().equals("7")) {
                                                App.isVisitInsert = true;
                                                String startTime = UtilApp.getCurrentTimeVisit();
                                                db.insertVisitedCustomer(mCustomer.getCustomerId(), startTime, "" + mSalesmanLC.getLatitude(), "" + mSalesmanLC.getLongitude(),
                                                        "" + mSalesmanLC.getLatitude(), "" + mSalesmanLC.getLongitude(), "1");
                                                Intent in = new Intent(FragmentJourneyPlan.this, SalesActivity.class);
                                                in.putExtra("customer", mCust);
                                                in.putExtra("customer_type", "OTC");
                                                startActivity(in);
                                            } else {
                                                if (mCust.getCustomerName().isEmpty()) {
                                                    Customer mCust1 = db.getDepotCustomerDetail(arrRecent.get(position).getCustomerId());
                                                    App.isVisitInsert = false;
                                                    Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
                                                    in.putExtra("custmer", mCust1);
                                                    startActivity(in);
                                                } else {
                                                    App.isVisitInsert = false;
                                                    Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
                                                    in.putExtra("custmer", mCust);
                                                    startActivity(in);
                                                }
                                            }

                                        } catch (Exception e) {
                                            Customer mCust1 = db.getDepotCustomerDetail(arrRecent.get(position).getCustomerId());
                                            App.isVisitInsert = false;
                                            Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
                                            in.putExtra("custmer", mCust1);
                                            startActivity(in);
                                        }


                                    } else {

                                    }
                                }
                            });


                        }
                    });
                }

            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_recent_cust;
            }
        };

        binding.recyclerviewRecent.setAdapter(mRecentAdapter);
    }


    private void setOTCAdapter() {

        mRecentAdapter = new CommonAdapter<Customer>(arrRecent) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowRecentCustBinding) {
                    ((RowRecentCustBinding) holder.binding).setItem(arrRecent.get(position));
                    ((RowRecentCustBinding) holder.binding).icon.setLetter(arrRecent.get(position).getCustomerName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowRecentCustBinding) holder.binding).icon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    holder.binding.executePendingBindings();

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UtilApp.logData(FragmentJourneyPlan.this, "Click On Recent Customer");

                           /* Customer mCust = db.getCustomerDetail(arrRecent.get(position).getCustomerId());
                            App.isVisitInsert = false;
                            Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
                            in.putExtra("custmer", mCust);
                            startActivity(in);*/

                            UtilApp.shopDialog(FragmentJourneyPlan.this, new OnSearchableDialog() {
                                @Override
                                public void onItemSelected(Object o) {
                                    String selection = (String) o;
                                    UtilApp.logData(FragmentJourneyPlan.this, "Shop Status :" + selection);
                                    if (selection.equalsIgnoreCase("open")) {

                                        /* cusPosition = position;
                            custBarcode = arrData.get(position).getBarcode();
                            mCustomer = arrData.get(position);
                            Intent in = new Intent(FragmentJourneyPlan.this, SimpleScannerActivity.class);
                            startActivityForResult(in, App.REQUEST_BARCODE);
*/
                                        Customer mCust = db.getCustomerDetail(arrRecent.get(position).getCustomerId());
                                        App.isVisitInsert = false;
                                        Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
                                        in.putExtra("custmer", mCust);
                                        startActivity(in);

                                    } else {

                                    }
                                }
                            });


                        }
                    });
                }

            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_recent_cust;
            }
        };

        binding.recyclerviewRecent.setAdapter(mRecentAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_scan:
                Intent in = new Intent(FragmentJourneyPlan.this, SimpleScannerActivity.class);
                in.putExtra("Type", "ScanCustomer");
                startActivityForResult(in, App.REQUEST_BARCODE);
                break;
            case R.id.action_map:
                ShowMapCustomeActivity.arrData = new ArrayList<>();
                ShowMapCustomeActivity.arrData = arrData;
                Intent inM = new Intent(FragmentJourneyPlan.this, ShowMapCustomeActivity.class);
                startActivity(inM);
                break;
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan_search, menu);
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

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            return false;
        }
        return true;
    }

    public void scanBarcode() {
        String barcode = "323";
        if (barcode.isEmpty()) {
            UtilApp.displayErrorDialog(FragmentJourneyPlan.this, "Customer Barcode does not match!");
        } else {
            if (isJSONValid(barcode)) {
                try {
                    JSONObject jOBj = new JSONObject(barcode);

                    if (jOBj.has("Customer Code")) {
                        Customer mBarCustomer = getCustomerDetail(jOBj.getString("Customer Code"));

                        if (mBarCustomer != null) {
                            UtilApp.shopDialog(FragmentJourneyPlan.this, new OnSearchableDialog() {
                                @Override
                                public void onItemSelected(Object o) {
                                    String selection = (String) o;
                                    UtilApp.logData(FragmentJourneyPlan.this, "Shop Status :" + selection);
                                    if (selection.equalsIgnoreCase("open")) {

                                        mCustomer = mBarCustomer;
                                        Customer mCust = db.getCustomerDetail(mCustomer.getCustomerId());
                                        System.out.println("id1--> " + mCust.getCustomerName());
                                        RecentCustomer recentCustomer = new RecentCustomer();
                                        recentCustomer.setCustomer_id(mBarCustomer.getCustomerId());
                                        recentCustomer.setCustomer_name(mBarCustomer.getCustomerName());
                                        recentCustomer.setDate_time(UtilApp.getCurrentDate());
                                        db.insertRecentCustomer(recentCustomer);
                                        Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
                                        in.putExtra("custmer", mBarCustomer);
                                        App.isVisitInsert = false;
                                        //Add Customer in Recent
                                        startActivity(in);
                                    }
                                }
                            });
                        } else {
                            UtilApp.displayErrorDialog(FragmentJourneyPlan.this, "Customer does not match!");
                        }
                    } else {
                        UtilApp.displayErrorDialog(FragmentJourneyPlan.this, "Customer Barcode does not match!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                UtilApp.displayErrorDialog(FragmentJourneyPlan.this, "Customer Barcode does not match!");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case App.REQUEST_BARCODE:
                if (resultCode == Activity.RESULT_OK) {
                    UtilApp.logData(FragmentJourneyPlan.this, "Barcode Scan");

                    String barcode = data.getStringExtra("code");
                    if (barcode.isEmpty()) {
                        UtilApp.displayErrorDialog(FragmentJourneyPlan.this, "Customer Barcode does not match!");
                    } else {
                        if (isJSONValid(barcode)) {
                            try {
                                JSONObject jOBj = new JSONObject(barcode);

                                if (jOBj.has("Customer Code")) {
                                    Customer mBarCustomer = getCustomerDetail(jOBj.getString("Customer Code"));

                                    if (mBarCustomer != null) {
                                        UtilApp.shopDialog(FragmentJourneyPlan.this, new OnSearchableDialog() {
                                            @Override
                                            public void onItemSelected(Object o) {
                                                String selection = (String) o;
                                                UtilApp.logData(FragmentJourneyPlan.this, "Shop Status :" + selection);
                                                if (selection.equalsIgnoreCase("open")) {

                                                    mCustomer = mBarCustomer;
                                                    Customer mCust = db.getCustomerDetail(mCustomer.getCustomerId());
                                                    System.out.println("id1--> " + mCust.getCustomerName());
                                                    RecentCustomer recentCustomer = new RecentCustomer();
                                                    recentCustomer.setCustomer_id(mBarCustomer.getCustomerId());
                                                    recentCustomer.setCustomer_name(mBarCustomer.getCustomerName());
                                                    recentCustomer.setDate_time(UtilApp.getCurrentDate());
                                                    db.insertRecentCustomer(recentCustomer);
                                                    Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
                                                    in.putExtra("custmer", mBarCustomer);
                                                    App.isVisitInsert = false;
                                                    //Add Customer in Recent
                                                    startActivity(in);
                                                }
                                            }
                                        });
                                    } else {
                                        UtilApp.displayErrorDialog(FragmentJourneyPlan.this, "Customer does not match!");
                                    }
                                } else {
                                    UtilApp.displayErrorDialog(FragmentJourneyPlan.this, "Customer Barcode does not match!");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            UtilApp.displayErrorDialog(FragmentJourneyPlan.this, "Customer Barcode does not match!");
                        }
                    }
                }
        }
    }

    public void openActivity() {
        UtilApp.logData(FragmentJourneyPlan.this, "Call OpenActivity");
        Intent in = new Intent(FragmentJourneyPlan.this, CustomerDetailActivity.class);
        in.putExtra("custmer", mCustomer);
        App.isVisitInsert = false;
        //Add Customer in Recent
        RecentCustomer recentCustomer = new RecentCustomer();
        recentCustomer.setCustomer_id(mCustomer.getCustomerId());
        recentCustomer.setCustomer_name(mCustomer.getCustomerName());
        recentCustomer.setDate_time(UtilApp.getCurrentDate());
        db.insertRecentCustomer(recentCustomer);

        startActivity(in);
    }

    @Override
    protected void onResume() {
        super.onResume();

        db = new DBManager(this);

        if (mSelectType.equals("Depot")) {
            isSequence = false;
            isAll = false;
            arrData = new ArrayList<>();
            arrData = db.getCutomerDepotList();
            setMainAdapter();
        } else if (mSelectType.equals("All")) {
            isSequence = false;
            isAll = true;
            arrData = new ArrayList<>();
            arrData = db.getCutomerList();
            setMainAdapter();
        } else {
            isSequence = true;
            isAll = false;
            //get Customer List
            arrData = new ArrayList<>();
            arrData = db.getSeqCustomerList(UtilApp.getCurrentDay());

            setMainAdapter();
        }


        arrRecent = new ArrayList<>();
        arrRecent = db.getRecentList();

        setRecentAdapter();
    }

    private void getCustomer() {

        String dept = Settings.getString(App.DEPOTID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomer_depot(App.CUSTOMER_DEPORT, dept, UtilApp.salesmanType(Settings.getString(App.ROLE)));

        //final Call<JsonObject> labelResponse = ApiClient.getService().getCustomer(App.CUSTOMER, Settings.getString(App.ROUTEID), UtilApp.salesmanType(Settings.getString(App.ROLE)));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Response", response.toString());
                binding.swipeRefreshLayout.setRefreshing(false);
                if (response.body() != null) {
                    UtilApp.logData(FragmentJourneyPlan.this, "Customer Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Customer> arrCustomer = new ArrayList<>();
                        arrCustomer = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Customer>>() {
                                }.getType());

                        String mtotalCOunt = response.body().get("total_cust").getAsString();
                        db.deleteTable(db.TABLE_DEPOT_CUSTOMER_COUNT);
                        db.insertDepotCustomerCount(mtotalCOunt, dept);
                        db.deleteTable(db.TABLE_DEPOT_CUSTOMER);
                        db.insertDept(arrCustomer);
                        // db.insertCustomer(arrCustomer);

                        //get Customer List
                        arrData = new ArrayList<>();
                        arrData = db.getCutomerDepotList();

                        setMainAdapter();
                    }
                } else {
                    UtilApp.logData(FragmentJourneyPlan.this, "Customer Response: Blank");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                UtilApp.logData(FragmentJourneyPlan.this, "Customer Failure: " + error.getMessage());
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
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
//                UtilApp.initialiseLocationVariables(FragmentJourneyPlan.this, new UtilApp.LocationUpdate() {
//                    @Override
//                    public void onLocationDetect(boolean isDetect) {
//                        if (isDetect) {
//                            UtilApp.logData(FragmentJourneyPlan.this, "Location Fetch:" + App.Latitude + " ," +
//                                    App.Longitude);
//                            mSalesmanLC = new Location("Salesman");
//                            mSalesmanLC.setLatitude(Double.parseDouble(App.Latitude));
//                            mSalesmanLC.setLongitude(Double.parseDouble(App.Longitude));
//                        }
//                    }
//                });
//
//                UtilApp.startLocationUpdates(FragmentJourneyPlan.this);
//            }
        }
    }

    private String routeId = "";

    private void getCustomer_otc_final() {
        arrOTC = db.getCutomerList();
        /*System.out.println("array-->"+arrOTC.size());
        System.out.println("array-->"+db.getCutomerList().get(0).getCustomerName());*/
        for (int i = 0; i < arrOTC.size(); i++) {
            // System.out.println("array-->"+arrOTC.get(i).getCustomerType());
            if (arrOTC.get(i).getCustomerType().equals("7")) {
                //  System.out.println("array1-->"+arrOTC.get(i).getCustomerName());
                binding.imageView2.setLetter(arrOTC.get(i).getCustomerName());
                mMaterialColors = getResources().getIntArray(R.array.colors);
                binding.imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);
                binding.imgColor.setBackgroundColor(getResources().getColor(R.color.progress1));
                binding.tvId.setText(arrOTC.get(i).getCustomerCode());
                binding.tvName.setText(arrOTC.get(i).getCustomerName());
                binding.tvPhone.setText(arrOTC.get(i).getCustPhone());
                // App.isVisitInsert = false;
                arrOTC_send.add(arrOTC.get(i));
            }
        }
    }

    private void getCustomer_otc() {
        routeId = Settings.getString(App.ROUTEID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomer_otc(App.CUSTOMER_OTC, routeId, UtilApp.salesmanType(Settings.getString(App.ROLE)));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(FragmentJourneyPlan.this, "Customer Response1: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Customer> arrCustomer = new ArrayList<>();
                        arrCustomer = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Customer>>() {
                                }.getType());
                        //db.deleteTable(DBManager.TABLE_CUSTOMER);
                        // db.insertCustomerOtc(arrCustomer);
                        Customer mCust = new Customer();
                        mCust.setCustomerId(arrCustomer.get(0).getCustomerId());
                        /* Customer mCust = db.getCustomerDetail(arrData.get(0).getCustomerId());*/
                        binding.imageView2.setLetter(arrCustomer.get(0).getCustomerName());
                        mMaterialColors = getResources().getIntArray(R.array.colors);
                        binding.imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);
                        binding.imgColor.setBackgroundColor(getResources().getColor(R.color.progress1));
                        binding.tvId.setText(arrCustomer.get(0).getCustomerCode());
                        binding.tvName.setText(arrCustomer.get(0).getCustomerName());
                        binding.tvPhone.setText(arrCustomer.get(0).getCustPhone());
                        App.isVisitInsert = false;

                        arrOTC = arrCustomer;
                    }
                } else {
                    UtilApp.logData(FragmentJourneyPlan.this, "Customer Response: Blank");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                UtilApp.logData(FragmentJourneyPlan.this, "Customer Failure: " + error.getMessage());

            }
        });
    }

}
