package com.mobiato.sfa.merchandising;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.OrderActivity;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormActivity;
import com.mobiato.sfa.databinding.ActivityCustomerOperationBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.utils.UtilApp;

public class CustomerOperationActivity extends BaseActivity implements View.OnClickListener {

    private ActivityCustomerOperationBinding binding;
    public static String id;
    private DBManager db;
    private Customer mCustomer;
    private String startTime = "", endTime = "", strType = "";
    private Location mSalesmanLC, mCustomerLC;
    private boolean isMerchant = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerOperationBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        id = getIntent().getStringExtra("id");
        strType = getIntent().getStringExtra("type");

        db = new DBManager(this);
        mCustomer = db.getCustomerDetail(id);
        setTitle(mCustomer.getCustomerName());
        App.flag = "true";

        if (strType.equalsIgnoreCase("Merchandising")) {
            isMerchant = true;
            db.UpdateCustomerVisiblity(mCustomer.getCustomerId());
        } else {
            isMerchant = false;
        }
        setMenu();

        mCustomerLC = new Location("Customer");
        if (!mCustomer.getLatitude().equalsIgnoreCase("") && !mCustomer.getLongitude().equalsIgnoreCase("")) {
            mCustomerLC.setLatitude(Double.parseDouble(mCustomer.getLatitude()));
            mCustomerLC.setLongitude(Double.parseDouble(mCustomer.getLongitude()));
        }

        //Check Permission
        checkPermision();

        binding.btnDistributionCheck.setOnClickListener(this);
        binding.btnInventoryCheck.setOnClickListener(this);
        binding.btnCompaignPicture.setOnClickListener(this);
        binding.btnAssetTracking.setOnClickListener(this);
        binding.btnOrder.setOnClickListener(this);
        binding.btnReturn.setOnClickListener(this);
        binding.ComplaintFeedback.setOnClickListener(this);
        binding.btnSurevey.setOnClickListener(this);
        binding.btnPlanogram.setOnClickListener(this);
        binding.btnCFR.setOnClickListener(this);
    }

    private void setMenu() {
        if (!isMerchant) {
            binding.btnDistributionCheck.setEnabled(false);
            binding.btnDistributionCheck.setClickable(false);
            binding.btnDistributionCheck.setAlpha(0.5f);

            binding.btnInventoryCheck.setEnabled(false);
            binding.btnInventoryCheck.setClickable(false);
            binding.btnInventoryCheck.setAlpha(0.5f);

            binding.btnOrder.setEnabled(false);
            binding.btnOrder.setClickable(false);
            binding.btnOrder.setAlpha(0.5f);

            binding.btnReturn.setEnabled(false);
            binding.btnReturn.setClickable(false);
            binding.btnReturn.setAlpha(0.5f);

            binding.btnCompaignPicture.setEnabled(false);
            binding.btnCompaignPicture.setClickable(false);
            binding.btnCompaignPicture.setAlpha(0.5f);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_operation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_store:
                Intent intent = new Intent(CustomerOperationActivity.this, CustomerProfileActivity.class);
                intent.putExtra("id", mCustomer.getCustomerId());
                startActivity(intent);
                return true;
            case android.R.id.home:
                // todo: goto back activity from here
                if (isMerchant) {
                    if (App.isVisitInsert) {
                        endTime = UtilApp.getCurrentTimeVisit();
                        db.updateCustomerVisit(endTime, mCustomer.getCustomerId());
                    }
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDistributionCheck:
                startActivity(new Intent(CustomerOperationActivity.this, StoreCheckBrandActivity.class)
                        .putExtra("CustomerId", mCustomer.getCustomerId()));
                break;
            case R.id.ComplaintFeedback:
                startActivity(new Intent(CustomerOperationActivity.this, ComplaintListActivity.class)
                        .putExtra("CustomerId", mCustomer.getCustomerId()));
                break;
            case R.id.btnInventoryCheck:
                startActivity(new Intent(CustomerOperationActivity.this, InventoryCheckActivity.class)
                        .putExtra("id", mCustomer.getCustomerId()));
                break;
            case R.id.btnCompaignPicture:
                startActivity(new Intent(CustomerOperationActivity.this, CampaignListActivity.class)
                        .putExtra("CustomerId", mCustomer.getCustomerId()));
                break;
            case R.id.btnOrder:
                startActivity(new Intent(CustomerOperationActivity.this, OrderActivity.class)
                        .putExtra("customer", mCustomer).putExtra("Type", "Merchandiser"));
                break;
            case R.id.btnReturn:
                startActivity(new Intent(CustomerOperationActivity.this, ReturnMerchandiserActivity.class)
                        .putExtra("customer", mCustomer));
                break;
            case R.id.btnPlanogram:
                startActivity(new Intent(CustomerOperationActivity.this, PlanogramListActivity.class)
                        .putExtra("id", mCustomer.getCustomerId()));
                break;
            case R.id.btnSurevey:
                startActivity(new Intent(CustomerOperationActivity.this, StockCheck_Questions.class)
                        .putExtra("title", "Consumer Surveys")
                        .putExtra("customerId", mCustomer.getCustomerId()));
                break;
            case R.id.btnAssetTracking:
                startActivity(new Intent(CustomerOperationActivity.this, AssetsListActivity.class)
                        .putExtra("CustomerId", mCustomer.getCustomerId()));
                break;
            case R.id.btnCFR:
                startActivity(new Intent(CustomerOperationActivity.this, ChillerRequestFormActivity.class)
                        .putExtra("customer", mCustomer));
                break;
            default:
                break;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                checkPermision();
                break;
        }
    }

    private float getRadius(Location currentLocation, Location depotLocation) {

        float distance = currentLocation.distanceTo(depotLocation);

        return distance;
    }

    @Override
    protected void onResume() {
        super.onResume();

        UtilApp.logData(CustomerOperationActivity.this, "On Resume Customer Detail");
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
                UtilApp.initialiseLocationVariables(CustomerOperationActivity.this, new UtilApp.LocationUpdate() {
                    @Override
                    public void onLocationDetect(boolean isDetect) {
                        if (isDetect) {


                            UtilApp.logData(CustomerOperationActivity.this, "Location Fetch:" + App.Latitude + " ," +
                                    App.Longitude);
                            mSalesmanLC = new Location("Salesman");
                            mSalesmanLC.setLatitude(Double.parseDouble(App.Latitude));
                            mSalesmanLC.setLongitude(Double.parseDouble(App.Longitude));

                            if (isMerchant) {
                                if (!App.isVisitInsert) {

                                    UtilApp.stopLocationUpdates();

                                    App.isVisitInsert = true;
                                    startTime = UtilApp.getCurrentTimeVisit();
                                    String mStatus = "";
                                    float distance = getRadius(mSalesmanLC, mCustomerLC);
                                    if (distance > Double.parseDouble(mCustomer.getRadius())) {
                                        mStatus = "3";
                                    } else {
                                        mStatus = "1";
                                    }

                                    db.insertVisitedCustomer(mCustomer.getCustomerId(), startTime, "" + mCustomerLC.getLatitude(), "" + mCustomerLC.getLongitude(),
                                            "" + mSalesmanLC.getLatitude(), "" + mSalesmanLC.getLongitude(), mStatus);
                                }
                            }

                        }
                    }
                });

                UtilApp.startLocationUpdates(CustomerOperationActivity.this);
            }
        }
    }
}
