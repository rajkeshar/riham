package com.mobiato.sfa.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.UpdateCustomer.UpdateExistingCustomerActivity;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormActivity;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormTwoActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormActivity;
import com.mobiato.sfa.databinding.ActivityCustomerDetailBinding;
import com.mobiato.sfa.databinding.RowFeedHeaderBinding;
import com.mobiato.sfa.databinding.RowItemFeedProgressBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.merchandising.CustomerOperationActivity;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerType;
import com.mobiato.sfa.model.Feed;
import com.mobiato.sfa.model.Freeze;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.MyLocation;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class CustomerDetailActivity extends BaseActivity implements View.OnClickListener {

    private ActivityCustomerDetailBinding binding;
    private Customer mCustomer;
    ArrayList<Transaction> arrTransaction = new ArrayList<>();
    ArrayList<Feed> arrFeed = new ArrayList<>();
    private CommonAdapter<Feed> mAdapter;
    private DBManager db;
    private double creditLimit = 0, balance = 0;
    private String tag = "";
    private String startTime = "", endTime = "";
    private Location mSalesmanLC, mCustomerLC;
    public String route_id = "0";
    public String sales_id = "0";
    Freeze mPromotion = new Freeze();
    LocationManager locationManager;
    private LoadingSpinner progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerDetailBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        progressDialog = new LoadingSpinner(CustomerDetailActivity.this);
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
                1000,
                10, locationListenerGPS);

        mCustomer = (Customer) getIntent().getSerializableExtra("custmer");
        setTitle(mCustomer.getCustomerName());
        System.out.println("popo-->" + mCustomer.getCustomerId());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        UtilApp.logData(CustomerDetailActivity.this, "Customer Detail OnScreen");
        if (getIntent().getStringExtra("tag") != null) {
            tag = getIntent().getStringExtra("tag");
        } else {
            startTime = UtilApp.getCurrentTimeVisit();
        }
        route_id = Settings.getString(App.ROUTEID);
        sales_id = Settings.getString(App.SALESMANID);
        mCustomerLC = new Location("Customer");
        try {

            if (mCustomer.getLatitude() != null && mCustomer.getLongitude() != null) {
                if (!mCustomer.getLatitude().equalsIgnoreCase("") && !mCustomer.getLongitude().equalsIgnoreCase("")) {
                    mCustomerLC.setLatitude(Double.parseDouble(mCustomer.getLatitude()));
                    mCustomerLC.setLongitude(Double.parseDouble(mCustomer.getLongitude()));
                }
            }

        } catch (Exception e) {
            e.toString();
        }

        binding.fabMenu.setMenuButtonColorNormal(Color.parseColor("#505B96"));
        binding.fabMenu.setMenuButtonColorPressed(Color.parseColor("#505B96"));
        binding.fabMenu.setMenuButtonColorRipple(Color.parseColor("#505B96"));

        db = new DBManager(this);

        try {
            setData();
        } catch (Exception e) {

        }
        setAdapter();

        // insertVisit();

        //set Display Credit
        try {
            if (!mCustomer.getCustType().equalsIgnoreCase("1")) {
                binding.txtCreditDays.setText(mCustomer.getPaymentTerm());
                binding.txtCreditLimit.setText(UtilApp.getNumberFormate(Math.round(Double.parseDouble(mCustomer.getCreditLimit()))));
                creditLimit = Double.parseDouble(mCustomer.getCreditLimit());
                double amtDue = db.getCusAvailableBal(mCustomer.getCustomerId());
                balance = creditLimit - amtDue;
                binding.txtAvailableBal.setText("" + UtilApp.getNumberFormate(balance));
            }
        } catch (Exception w) {

        }
        try {
            if (mCustomer.getIs_fridge_assign().equals("1")) {
                binding.fabChilleradd.setVisibility(View.GONE);
                binding.fabChilleradd.setEnabled(false);
                binding.fabChilleradd.setClickable(false);
                binding.fabChilleradd.setAlpha(0.5f);
            } else {
                binding.fabChilleradd.setVisibility(View.VISIBLE);
                binding.fabChilleradd.setEnabled(true);
                binding.fabChilleradd.setClickable(true);
                binding.fabChilleradd.setAlpha(1.0f);
            }
        } catch (Exception e) {
            binding.fabChilleradd.setVisibility(View.VISIBLE);
            binding.fabChilleradd.setEnabled(true);
            binding.fabChilleradd.setClickable(true);
            binding.fabChilleradd.setAlpha(1.0f);
        }

        if (UtilApp.salesmanRole(Settings.getString(App.ROLE)).equalsIgnoreCase("Depot salesman")) {
            binding.fabChilleradd.setVisibility(View.GONE);
            binding.fabChilleradd.setEnabled(false);
            binding.fabChilleradd.setClickable(false);
            binding.fabChilleradd.setAlpha(0.5f);

            binding.fabChillerTransfer.setVisibility(View.GONE);
            binding.fabChillerTransfer.setEnabled(false);
            binding.fabChillerTransfer.setClickable(false);
            binding.fabChillerTransfer.setAlpha(0.5f);

            binding.fabChiller.setVisibility(View.GONE);
            binding.fabChiller.setEnabled(false);
            binding.fabChiller.setClickable(false);
            binding.fabChiller.setAlpha(0.5f);
        }
       /* binding.fabChilleradd.setVisibility(View.VISIBLE);
        binding.fabChilleradd.setEnabled(true);
        binding.fabChilleradd.setClickable(true);
        binding.fabChilleradd.setAlpha(1.0f);*/
        if (UtilApp.salesmanRole(Settings.getString(App.ROLE)).equalsIgnoreCase("Hariss salesman")) {
            /*binding.fabMerchandising.setVisibility(View.VISIBLE);
            binding.fabMerchandising.setEnabled(true);
            binding.fabMerchandising.setClickable(true);
             binding.fabMerchandising.setAlpha(1.0f);*/
            binding.fabMerchandising.setVisibility(View.GONE);
            binding.fabMerchandising.setEnabled(false);
            binding.fabMerchandising.setClickable(false);
            binding.fabMerchandising.setAlpha(0.5f);
            binding.fabCustomerUpdate.setVisibility(View.GONE);
            binding.fabCustomerUpdate.setEnabled(false);
            binding.fabCustomerUpdate.setClickable(false);
            binding.fabCustomerUpdate.setAlpha(0.5f);
            /*binding.fabCustomerUpdate.setVisibility(View.GONE);
            binding.fabCustomerUpdate.setEnabled(false);
            binding.fabCustomerUpdate.setClickable(false);
            binding.fabCustomerUpdate.setAlpha(0.5f);*/
        } else {
            binding.fabMerchandising.setVisibility(View.GONE);
            binding.fabMerchandising.setEnabled(false);
            binding.fabMerchandising.setClickable(false);
            binding.fabMerchandising.setAlpha(0.5f);
            binding.fabCustomerUpdate.setVisibility(View.GONE);
            binding.fabCustomerUpdate.setEnabled(false);
            binding.fabCustomerUpdate.setClickable(false);
            binding.fabCustomerUpdate.setAlpha(0.5f);
            /*binding.fabCustomerUpdate.setVisibility(View.VISIBLE);
            binding.fabCustomerUpdate.setEnabled(true);
            binding.fabCustomerUpdate.setClickable(true);
            binding.fabCustomerUpdate.setAlpha(1.0f);*/
        }

        binding.fabChillerTransfer.setVisibility(View.GONE);
        binding.fabChillerTransfer.setEnabled(false);
        binding.fabChillerTransfer.setClickable(false);
        binding.fabChillerTransfer.setAlpha(0.5f);
        binding.fabSale.setOnClickListener(this);
        binding.fabOrder.setOnClickListener(this);
        binding.fabCollection.setOnClickListener(this);
        binding.fabReturn.setOnClickListener(this);
        binding.fabExchange.setOnClickListener(this);
        binding.fabDelivery.setOnClickListener(this);
        binding.fabMerchandising.setOnClickListener(this);
        binding.fabChiller.setOnClickListener(this);
        binding.fabCustomerUpdate.setOnClickListener(this);
        binding.fabChillerTransfer.setOnClickListener(this);
        binding.fabChilleradd.setOnClickListener(this);


        insertVisit();

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
            insertVisit();
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


    public void insertVisit() {

        UtilApp.logData(CustomerDetailActivity.this, "Location Fetch:" + App.Latitude + " ," +
                App.Longitude);

        Log.e("Location Fetch:", App.Latitude + " ," +
                App.Longitude);
        mSalesmanLC = new Location("Salesman");
        mSalesmanLC.setLatitude(Double.parseDouble(App.Latitude));
        mSalesmanLC.setLongitude(Double.parseDouble(App.Longitude));

        if (!App.isVisitInsert) {
            App.isVisitInsert = true;
            String mStatus = "";
                              /* mCustomerLC.setLatitude(23.0258002);
                                mCustomerLC.setLongitude(72.5395747);*/

            if (mCustomer.getLatitude() != null) {
                float distance = getRadius(mSalesmanLC, mCustomerLC);
                //System.out.println("distance--> "+distance);
                try {
                    if (distance > Double.parseDouble(mCustomer.getRadius())) {
                        mStatus = "3";
                    } else {
                        mStatus = "1";
                    }
                } catch (Exception e) {
                }
            } else {
                mStatus = "1";
            }

            db.insertVisitedCustomer(mCustomer.getCustomerId(), startTime, "" + mSalesmanLC.getLatitude(), "" + mSalesmanLC.getLongitude(),
                    "" + mSalesmanLC.getLatitude(), "" + mSalesmanLC.getLongitude(), mStatus);
        }
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
                UtilApp.initialiseLocationVariables(CustomerDetailActivity.this, new UtilApp.LocationUpdate() {
                    @Override
                    public void onLocationDetect(boolean isDetect) {
                        if (isDetect) {
                            UtilApp.logData(CustomerDetailActivity.this, "Location Fetch:" + App.Latitude + " ," +
                                    App.Longitude);
                            mSalesmanLC = new Location("Salesman");
                            mSalesmanLC.setLatitude(Double.parseDouble(App.Latitude));
                            mSalesmanLC.setLongitude(Double.parseDouble(App.Longitude));

                            /*mCustomer.setLatitude(String.valueOf(Double.parseDouble(App.Latitude)));
                            mCustomer.setLongitude(String.valueOf(Double.parseDouble(App.Longitude)));*/

                            if (!App.isVisitInsert) {
                                App.isVisitInsert = true;
                                String mStatus = "";
                              /* mCustomerLC.setLatitude(23.0258002);
                                mCustomerLC.setLongitude(72.5395747);*/

                                if (mCustomer.getLatitude() != null) {
                                    float distance = getRadius(mSalesmanLC, mCustomerLC);
                                    //System.out.println("distance--> "+distance);
                                    try {
                                        if (distance > Double.parseDouble(mCustomer.getRadius())) {
                                            mStatus = "3";
                                        } else {
                                            mStatus = "1";
                                        }
                                    } catch (Exception e) {
                                    }
                                } else {
                                    mStatus = "1";
                                }

                                UtilApp.stopLocationUpdates();

                                db.insertVisitedCustomer(mCustomer.getCustomerId(), startTime, "" + mSalesmanLC.getLatitude(), "" + mSalesmanLC.getLongitude(),
                                        "" + mSalesmanLC.getLatitude(), "" + mSalesmanLC.getLongitude(), mStatus);
                            }

                        }
                    }
                });

                UtilApp.startLocationUpdates(CustomerDetailActivity.this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (UtilApp.salesmanRole(Settings.getString(App.ROLE)).equalsIgnoreCase("Hariss salesman")) {
            getMenuInflater().inflate(R.menu.menu_option_cust, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_option, menu);
        }
        return true;
    }

    private void setData() {
        arrTransaction = new ArrayList<>();
        arrFeed = new ArrayList<>();

        arrTransaction = db.getAllTransactionsForCustomer(mCustomer.getCustomerId());

        Collections.sort(arrTransaction, new Comparator<Transaction>() {
            public int compare(Transaction o1, Transaction o2) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                Date mm = null, nn = null;
                try {
                    mm = format.parse(o1.tr_date_time);
                    nn = format.parse(o2.tr_date_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return nn.compareTo(mm);
            }
        });

        for (int i = 0; i < arrTransaction.size(); i++) {
            Feed feed = new Feed();

            if (i == 0) {
                String date = arrTransaction.get(i).tr_date_time.substring(0, 10);
                String time = UtilApp.getStringDate(date);
                feed.type = 1;
                feed.date = time;
                arrFeed.add(feed);

                addFeedData(arrTransaction.get(i));

            } else {
                String date = arrTransaction.get(i).tr_date_time.substring(0, 10);
                String prdate = arrTransaction.get((i - 1)).tr_date_time.substring(0, 10);

                boolean isSame = false;
                if (prdate.equalsIgnoreCase(date)) {
                    isSame = true;
                } else {
                    isSame = false;
                }

                if (isSame) {
                    addFeedData(arrTransaction.get(i));
                } else {
                    String mDate = arrTransaction.get(i).tr_date_time.substring(0, 10);
                    String time = UtilApp.getStringDate(mDate);
                    feed.type = 1;
                    feed.date = time;
                    arrFeed.add(feed);

                    addFeedData(arrTransaction.get(i));
                }

            }
        }

    }

    public void addFeedData(Transaction mTransaction) {
        Feed feed = new Feed();
        feed.type = 2;
        feed.desc = mTransaction.tr_date_time;
        feed.inv_type = mTransaction.tr_type;

        switch (mTransaction.tr_type) {
            case Constant.TRANSACTION_TYPES.TT_CASH_COLLECTION:
                feed.name = "Cash collection created";
                feed.inv_no = "No.: " + mTransaction.tr_collection_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_CREDIT_COLLECTION:
                feed.name = "Credit collection created";
                feed.inv_no = "No.: " + mTransaction.tr_collection_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_PAYMENT_BY_CASH:
                feed.name = "Payment done by cash.";
                feed.inv_no = "Payment id: " + mTransaction.tr_pyament_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_PAYMENT_BY_CHEQUE:
                feed.name = "Payment done by cheque.";
                feed.inv_no = "Payment id: " + mTransaction.tr_pyament_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_STOCK_CAP:
                feed.name = "Stock captured.";
                feed.inv_no = "";
                break;
            case Constant.TRANSACTION_TYPES.TT_SALES_CREATED:
                feed.name = "Sales created.";
                feed.inv_no = "No.: " + mTransaction.tr_invoice_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_OREDER_CREATED:
                feed.name = "Order created.";
                feed.inv_no = "No.: " + mTransaction.tr_order_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_RETURN_CREATED:
                feed.name = "Return created.";
                feed.inv_no = "No.: " + mTransaction.tr_order_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_EXCHANGE_CREATED:
                feed.name = "Exchange created.";
                feed.inv_no = "No.: " + mTransaction.tr_order_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_CUST_CREATED:
                feed.name = "Customer created.";
                feed.inv_no = "No.: " + mTransaction.tr_order_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_UPDATE:
                feed.name = "Customer Updated.";
                feed.inv_no = "No.: " + mTransaction.tr_order_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_ADD_DEPT_CUSTOEMR_UPDATE:
                feed.name = "Customer Updated.";
                feed.inv_no = "No.: " + mTransaction.tr_order_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_CHILER_REQUEST:
                feed.name = "Chiller Request Successfuly.";
                feed.inv_no = "No.: " + mTransaction.tr_order_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_CHILER_TRANSFER:
                feed.name = "Chiller Transfer Successfuly.";
                feed.inv_no = "No.: " + mTransaction.tr_order_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_CHILER_ADD:
                feed.name = "Chiller Added and waiting for approval.";
                feed.inv_no = "No.: " + mTransaction.tr_order_id;
                break;
            case Constant.TRANSACTION_TYPES.TT_CHILER_TRACK:
                feed.name = "Fridge Status update.";
                feed.inv_no = "";
                break;

            default:
        }

        arrFeed.add(feed);
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Feed>(arrFeed) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowFeedHeaderBinding) {
                    ((RowFeedHeaderBinding) holder.binding).tvTitle.setText(arrFeed.get(position).date);
                    holder.binding.executePendingBindings();
                } else if (holder.binding instanceof RowItemFeedProgressBinding) {
                    ((RowItemFeedProgressBinding) holder.binding).txtInvoiceNo.setText(arrFeed.get(position).inv_no);
                    ((RowItemFeedProgressBinding) holder.binding).txtActivity.setText(arrFeed.get(position).name);
                    ((RowItemFeedProgressBinding) holder.binding).txtTime.setText(arrFeed.get(position).desc);

                    switch (arrFeed.get(position).inv_type) {
                        case Constant.TRANSACTION_TYPES.TT_CASH_COLLECTION:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.icon_fan_shop_set);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CREDIT_COLLECTION:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.icon_fan_shop_set);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_SALES_CREATED:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.icon_fan_discount_sel);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_OREDER_CREATED:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.icon_fan_order_sel);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_RETURN_CREATED:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.icon_fan_discount_sel);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_EXCHANGE_CREATED:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.icon_fan_discount_sel);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CHILER_REQUEST:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.fridge_request);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CHILER_TRANSFER:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.fridge_request);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CHILER_ADD:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.fridge_request);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CHILER_TRACK:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.fridge_request);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_FRIDGE_ADD:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.fridge_request);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_UPDATE:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.icon_fan_order_sel);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_ADD_DEPT_CUSTOEMR_UPDATE:
                            ((RowItemFeedProgressBinding) holder.binding).ivIcon.setImageResource(R.drawable.icon_fan_order_sel);
                            break;
                        default:
                            break;
                    }
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                if (arrFeed.get(position).type == 1) {
                    return R.layout.row_feed_header;
                } else {
                    return R.layout.row_item_feed_progress;
                }
            }
        };

        binding.rvOperation.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_more:
                startActivity(new Intent(me, TransactionActivity.class).putExtra("custId", mCustomer.getCustomerId()));
                break;
            case R.id.action_edit:
                if (mCustomer.getCustType().equalsIgnoreCase("7")) {
                    UtilApp.displayAlert(me, "You can not update OTC customer!");
                } else {
                    Settings.setString(App.SCANSERIALNUMBER, "");
                    if (Integer.parseInt(db.getCustomerCategoryCount()) > 0) {
                        startActivity(new Intent(me, UpdateExistingCustomerActivity.class).putExtra("customer", mCustomer));
                        binding.fabMenu.close(false);
                    } else {
                        getCategoryList();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    UtilApp.logData(CustomerDetailActivity.this, "Free Good Response: " + response.body().toString());
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
                startActivity(new Intent(me, UpdateExistingCustomerActivity.class).putExtra("customer", mCustomer));

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Good Fail", error.getMessage());
                progressDialog.hide();
                UtilApp.logData(CustomerDetailActivity.this, "Free Good Failure: " + error.getMessage());
                startActivity(new Intent(me, UpdateExistingCustomerActivity.class).putExtra("customer", mCustomer));

            }
        });
    }

    public void displayAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CustomerDetailActivity.this);
        alertDialogBuilder.setTitle("Alert")
                .setMessage("Is the assigned fridge available?")
                .setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        reason_no();
                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent1 = new Intent(CustomerDetailActivity.this, CameraSerialFormActivity.class);
                        intent1.putExtra("customer", mCustomer);
                        intent1.putExtra("code", "");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        finish();
                        //showDScannerialog();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

   /* public void showDScannerialog() {
        final Dialog dialog = new Dialog(CustomerDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(me, SimpleScannerActivity.class).putExtra("customer", mCustomer));

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }*/

    public void showDScannerialog() {
        final Dialog dialog = new Dialog(CustomerDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        Button dialogButton_manual = (Button) dialog.findViewById(R.id.btn_manual);
        dialogButton_manual.setVisibility(View.VISIBLE);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(me, SimpleScannerActivity.class)
                        .putExtra("customer", mCustomer)
                        .putExtra("Type", "ScanQR"));
            }
        });

        dialogButton_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent1 = new Intent(CustomerDetailActivity.this, CameraSerialFormActivity.class);
                intent1.putExtra("customer", mCustomer);
                intent1.putExtra("code", "");
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public void showDScannerialog1() {
        final Dialog dialog = new Dialog(CustomerDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        Button dialogButton_manual = (Button) dialog.findViewById(R.id.btn_manual);
        dialogButton_manual.setVisibility(View.VISIBLE);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(me, SimpleScannerChillerActivity.class).putExtra("customer", mCustomer));

            }
        });

        dialogButton_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent1 = new Intent(CustomerDetailActivity.this, AddChillerFormActivity.class);
                intent1.putExtra("customer", mCustomer);
                intent1.putExtra("code", "");
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Intent intent1 = new Intent(context, CaptureActivity.class);
                startActivity(intent1);
                // startActivity(new Intent(me, SimpleScannerChillerActivity.class).putExtra("customer", mCustomer));

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public String[] arrDisplayLocation = {"Cooler missing", "Cooler Transfer", "Un authorized Transfer", "Shop Closed", "Another cooler Found", "Others-(Specify reason)"};
    private AlertDialog.Builder builder;

    public void reason_no() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_input_dialog_freeze, null);

        final EditText edt_sale_reason = (EditText) dialogView.findViewById(R.id.edt_sale_reason);
        final TextView et_dispute = (TextView) dialogView.findViewById(R.id.et_dispute);
        Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);

        et_dispute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(CustomerDetailActivity.this);
                builder.setTitle("Choose Reason");
                builder.setItems(arrDisplayLocation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et_dispute.setText(arrDisplayLocation[which]);
                        if (et_dispute.getText().toString().equals("Others-(Specify reason)")) {
                            edt_sale_reason.setVisibility(View.VISIBLE);
                        } else {
                            edt_sale_reason.setVisibility(View.GONE);
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog12 = builder.create();
                dialog12.show();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_dispute.getText().toString().equals("")) {
                    Toast.makeText(me, "Please Select Dispute/ Remark", Toast.LENGTH_SHORT).show();
                } else {
                    if (et_dispute.getText().toString().equals("Others-(Specify reason)")) {
                        if (edt_sale_reason.getText().toString().isEmpty()) {
                            Toast.makeText(me, "Please enter reason", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    dialogBuilder.dismiss();
                    if (!mCustomer.getCustType().equalsIgnoreCase("1")) {
                        binding.fabMenu.close(false);
                        if (creditLimit > 0) {
                            mPromotion = new Freeze();
                            mPromotion.setRoute_id(route_id);
                            mPromotion.setSalesman_id(Settings.getString(App.SALESMANID));
                            mPromotion.setCustomer_id(mCustomer.getCustomerId());
                            mPromotion.setHave_fridge("No");
                            mPromotion.setLatitude(String.valueOf(mCustomer.getLatitude()));
                            mPromotion.setLongitude(String.valueOf(mCustomer.getLongitude()));
                            mPromotion.setComments(edt_sale_reason.getText().toString());
                            mPromotion.setComplaint_type(et_dispute.getText().toString());

                            mPromotion.setImage1("");
                            mPromotion.setImage2("");
                            mPromotion.setImage3("");
                            mPromotion.setImage4("");
                            mPromotion.setSerial_no("");
                            mPromotion.setFridge_scan_img("");

                            String custNum = UtilApp.getChilerTrackingNo();
                            db.insertChillerTrack(custNum, mPromotion);

                            Transaction transaction = new Transaction();
                            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_CHILER_TRACK;
                            transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
                            transaction.tr_customer_num = mCustomer.getCustomerId();
                            transaction.tr_customer_name = mCustomer.getCustomerName();
                            transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
                            transaction.tr_invoice_id = "";
                            transaction.tr_order_id = custNum;
                            transaction.tr_collection_id = "";
                            transaction.tr_pyament_id = "";
                            transaction.tr_is_posted = "No";
                            transaction.tr_printData = "";
                            db.insertTransaction(transaction);

                            SalesActivity.arrFOCItem = new ArrayList<>();
                            startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));


//                            //  callCompititorAPI();
//                            RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.CHILLER_TRACKING);
//                            RequestBody route_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getRoute_id());
//                            RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
//                            RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getCustomerId());
//                            RequestBody have_fridge = RequestBody.create(MediaType.parse("text/plain"), "No");
//                            RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getLatitude());
//                            RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getLongitude());
//                            RequestBody comments = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComments());
//                            RequestBody Complaint_type = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComplaint_type());
//                            RequestBody serial_no = RequestBody.create(MediaType.parse("text/plain"), "");
//
//                            List<MultipartBody.Part> parts = new ArrayList<>();
//
////        // add dynamic amount
//                            if (!mPromotion.getImage1().equalsIgnoreCase("")) {
//                                parts.add(UtilApp.prepareFilePart("image1", mPromotion.getImage1()));
//                            }
//
//                            if (!mPromotion.getImage2().equalsIgnoreCase("")) {
//                                parts.add(UtilApp.prepareFilePart("image2", mPromotion.getImage2()));
//                            }
//
//                            if (!mPromotion.getImage3().equalsIgnoreCase("")) {
//                                parts.add(UtilApp.prepareFilePart("image3", mPromotion.getImage3()));
//                            }
//
//                            if (!mPromotion.getImage4().equalsIgnoreCase("")) {
//                                parts.add(UtilApp.prepareFilePart("image4", mPromotion.getImage4()));
//                            }
//
//                            try {
//                                if (!mPromotion.getFridge_scan_img().equalsIgnoreCase("")) {
//                                    parts.add(UtilApp.prepareFilePart("fridge_scan_img", mPromotion.getFridge_scan_img()));
//                                }
//                            } catch (Exception e) {
//                                e.toString();
//                            }
//
//                            UtilApp.logData(CustomerDetailActivity.this, "Compatitor Feedback Request: " + mPromotion.toString());
//
//                            final Call<JsonObject> labelResponse = ApiClient.getService().postFridgeData(method, route_id, salesman_id,
//                                    customer_id, have_fridge, latitude, longitude, comments, Complaint_type, serial_no, parts);
//
//                            labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
//                                @Override
//                                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                                    Log.e("Freeze Response:", response.toString());
//                                    //  UtilApp.logData(SyncData.this, "Compitior Response: " + response.toString());
//                                    if (response.body() != null) {
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(response.body().toString());
//                                            if (jsonObject.has("STATUS")) {
//                                                if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
//                                                    Log.e("Status", jsonObject.getString("STATUS"));
//                                                    if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
//                                                        App.isCompititorSync = false;
//                                                        Log.e("Compititor Success", jsonObject.getString("STATUS"));
//                                                        SalesActivity.arrFOCItem = new ArrayList<>();
//                                                        startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));
//
//                                                    } else {
//                                                        // Fail to Post
//                                                        App.isCompititorSync = false;
//                                                    }
//                                                }
//                                            }
//                                        } catch (Exception e) {
//                                            App.isCompititorSync = false;
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<JsonObject> call, Throwable t) {
//                                    Log.e("Compititor fail:", t.toString());
//                                    UtilApp.logData(CustomerDetailActivity.this, "Compititoer Fail: " + t.getMessage());
//                                    App.isCompititorSync = false;
//                                }
//                            });
                        } else {
                            UtilApp.displayAlert(CustomerDetailActivity.this, "Customer credit limit exceeds!");
                        }
                    } else {
                   /* SalesActivity.arrFOCItem = new ArrayList<>();
                    startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));*/
                        Freeze mPromotion = new Freeze();
                        mPromotion.setRoute_id(route_id);
                        mPromotion.setSalesman_id(sales_id);
                        mPromotion.setCustomer_id(mCustomer.getCustomerId());
                        mPromotion.setHave_fridge("No");
                        mPromotion.setLatitude(String.valueOf(mCustomer.getLatitude()));
                        mPromotion.setLongitude(String.valueOf(mCustomer.getLongitude()));
                        mPromotion.setComments(edt_sale_reason.getText().toString());
                        mPromotion.setComplaint_type(et_dispute.getText().toString());

                        mPromotion.setSerial_no("");
                        mPromotion.setFridge_scan_img("");
                        mPromotion.setImage1("");
                        mPromotion.setImage2("");
                        mPromotion.setImage3("");
                        mPromotion.setImage4("");

                        String custNum = UtilApp.getChilerTrackingNo();
                        db.insertChillerTrack(custNum, mPromotion);

                        Transaction transaction = new Transaction();
                        transaction.tr_type = Constant.TRANSACTION_TYPES.TT_CHILER_TRACK;
                        transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
                        transaction.tr_customer_num = mCustomer.getCustomerId();
                        transaction.tr_customer_name = mCustomer.getCustomerName();
                        transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
                        transaction.tr_invoice_id = "";
                        transaction.tr_order_id = custNum;
                        transaction.tr_collection_id = "";
                        transaction.tr_pyament_id = "";
                        transaction.tr_is_posted = "No";
                        transaction.tr_printData = "";
                        db.insertTransaction(transaction);

                        SalesActivity.arrFOCItem = new ArrayList<>();
                        startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));


//                        // callCompititorAPI();
//                        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.CHILLER_TRACKING);
//                        RequestBody route_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.ROUTEID));
//                        RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
//                        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getCustomerId());
//                        RequestBody have_fridge = RequestBody.create(MediaType.parse("text/plain"), "No");
//                        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getLatitude());
//                        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getLongitude());
//                        RequestBody comments = RequestBody.create(MediaType.parse("text/plain"), edt_sale_reason.getText().toString());
//                        RequestBody Complaint_type = RequestBody.create(MediaType.parse("text/plain"), et_dispute.getText().toString());
//                        RequestBody serial_no = RequestBody.create(MediaType.parse("text/plain"), "");
//
//                        List<MultipartBody.Part> parts = new ArrayList<>();
//
////        // add dynamic amount
//                        if (!mPromotion.getImage1().equalsIgnoreCase("")) {
//                            parts.add(UtilApp.prepareFilePart("image1", mPromotion.getImage1()));
//                        }
//
//                        if (!mPromotion.getImage2().equalsIgnoreCase("")) {
//                            parts.add(UtilApp.prepareFilePart("image2", mPromotion.getImage2()));
//                        }
//
//                        if (!mPromotion.getImage3().equalsIgnoreCase("")) {
//                            parts.add(UtilApp.prepareFilePart("image3", mPromotion.getImage3()));
//                        }
//
//                        if (!mPromotion.getImage4().equalsIgnoreCase("")) {
//                            parts.add(UtilApp.prepareFilePart("image4", mPromotion.getImage4()));
//                        }
//                        try {
//                            if (!mPromotion.getFridge_scan_img().equalsIgnoreCase("")) {
//                                parts.add(UtilApp.prepareFilePart("fridge_scan_img", mPromotion.getFridge_scan_img()));
//                            }
//                        } catch (Exception e) {
//                            e.toString();
//                        }
//
//                        UtilApp.logData(CustomerDetailActivity.this, "Compatitor Feedback Request: " + mPromotion.toString());
//
//                        final Call<JsonObject> labelResponse = ApiClient.getService().postFridgeData(method, route_id, salesman_id,
//                                customer_id, have_fridge, latitude, longitude, comments, Complaint_type, serial_no, parts);
//
//                        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
//                            @Override
//                            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                                Log.e("Freeze Response:", response.toString());
//                                //  UtilApp.logData(SyncData.this, "Compitior Response: " + response.toString());
//                                if (response.body() != null) {
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                                        if (jsonObject.has("STATUS")) {
//                                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
//                                                Log.e("Status", jsonObject.getString("STATUS"));
//                                                if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
//                                                    App.isCompititorSync = false;
//                                                    Log.e("Compititor Success", jsonObject.getString("STATUS"));
//                                /*    db.updateCampititorPosted(mPromotion.getCompititorId());
//                                    db.updateTransaction(mPromotion.getCompititorId());*/
//                                                    SalesActivity.arrFOCItem = new ArrayList<>();
//                                                    startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));
//                                                } else {
//                                                    // Fail to Post
//                                                    App.isCompititorSync = false;
//                                                }
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        App.isCompititorSync = false;
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<JsonObject> call, Throwable t) {
//                                Log.e("Compititor fail:", t.toString());
//                                UtilApp.logData(CustomerDetailActivity.this, "Compititoer Fail: " + t.getMessage());
//                                App.isCompititorSync = false;
//                            }
//                        });
                    }
                    System.out.println("ppr-->" + mPromotion.getRoute_id());
                }
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fabSale:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Sale Click");
                // System.out.println("freeze-->" + mCustomer.getIs_fridge_assign());

                if (db.checkIfAgentPriceExists()) {

                    String totalCount = db.getRoutePriceCount(Settings.getString(App.ROUTEID));
                    String agentPrice = db.getAgentPriceCount();

                    if (Integer.parseInt(agentPrice) < Integer.parseInt(totalCount)) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CustomerDetailActivity.this);
                        alertDialogBuilder.setTitle("Alert")
                                .setMessage("Pricing data not downloaded. Please Goto Setting Master and Download Agent Pricing!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    } else {
                        if (!mCustomer.getCustType().equalsIgnoreCase("1")) {
                            binding.fabMenu.close(false);
                            if (creditLimit > 0) {
                                //Check Freeze
                                if (mCustomer.getIs_fridge_assign().equals("1")) {
                                    displayAlert();
                                } else {
                                    SalesActivity.arrFOCItem = new ArrayList<>();
                                    startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));
                                }
                            } else {
                                UtilApp.displayAlert(CustomerDetailActivity.this, "Customer credit limit exceeds!");
                            }
                        } else {
                            try {
                                if (mCustomer.getIs_fridge_assign().equals("1")) {
                                    displayAlert();
                                } else {
                                    SalesActivity.arrFOCItem = new ArrayList<>();
                                    startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));
                                }
                            } catch (Exception e) {
                                SalesActivity.arrFOCItem = new ArrayList<>();
                                startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));
                            }
                            binding.fabMenu.close(false);
                        }
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CustomerDetailActivity.this);
                    alertDialogBuilder.setTitle("Alert")
                            .setMessage("Pricing data not downloaded. Please Goto Setting Master and Download Agent Pricing!")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }


                break;
            case R.id.fabChilleradd:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Sale Click");
                startActivity(new Intent(CustomerDetailActivity.this, AddFridgeActivity.class)
                        .putExtra("customer", mCustomer).putExtra("type", "Salesman"));
                binding.fabMenu.close(false);

//                showDScannerialog1();
//                binding.fabMenu.close(false);
                break;
//            case R.id.fabCustomerUpdate:
//                if (mCustomer.getIs_fridge_assign().equals("1")) {
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CustomerDetailActivity.this);
//                    alertDialogBuilder.setTitle("Alert")
//                            .setMessage("Is the assigned fridge available?")
//                            .setCancelable(false)
//                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    startActivity(new Intent(me, UpdateExistingCustomerActivity.class).putExtra("customer", mCustomer).putExtra("available","No"));
//                                }
//                            })
//                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // dialog.dismiss();
//                                    startActivity(new Intent(me, SimpleScannerActivity.class).putExtra("customer", mCustomer).putExtra("available","No"));
//                                }
//                            });
//                    // create alert dialog
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    // show it
//                    alertDialog.show();
//                }else {
//                    startActivity(new Intent(me, UpdateExistingCustomerActivity.class).putExtra("customer", mCustomer));
//                }
//                break;
            case R.id.fabCustomerUpdate:
                Settings.setString(App.SCANSERIALNUMBER, "");
                startActivity(new Intent(me, UpdateExistingCustomerActivity.class).putExtra("customer", mCustomer));
                binding.fabMenu.close(false);
                break;
          /*  case R.id.fabSale:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Sale Click");
                System.out.println("freeze-->" + mCustomer.getIs_fridge_assign());
                if (!mCustomer.getCustType().equalsIgnoreCase("1")) {
                    binding.fabMenu.close(false);
                    if (creditLimit > 0) {
                        //Check Freeze
                        SalesActivity.arrFOCItem = new ArrayList<>();
                        startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));
                    } else {
                        UtilApp.displayAlert(CustomerDetailActivity.this, "Customer credit limit exceeds!");
                    }
                } else {
                    SalesActivity.arrFOCItem = new ArrayList<>();
                    startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));
                    binding.fabMenu.close(false);
                }

                break;*/
            case R.id.fabOrder:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Order Click");
                startActivity(new Intent(me, OrderActivity.class).putExtra("customer", mCustomer)
                        .putExtra("Type", "Salesman"));
                binding.fabMenu.close(false);
                break;
            case R.id.fabCollection:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Collection Click");
                if (!mCustomer.getCustType().equalsIgnoreCase("1")) {
                    startActivity(new Intent(me, MultiCollectionActivity.class).putExtra("customer", mCustomer));
                } else {
                    startActivity(new Intent(me, CollectionActivity.class).putExtra("customer", mCustomer));
                }

                binding.fabMenu.close(false);
                break;
            case R.id.fabReturn:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Return Click");
                startActivity(new Intent(me, ReturnActivity.class).putExtra("customer", mCustomer));
                binding.fabMenu.close(false);
                break;
            case R.id.fabExchange:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Exchange Click");
                startActivity(new Intent(me, ExchangeActivity.class).putExtra("customer", mCustomer));
                binding.fabMenu.close(false);
                break;
            case R.id.fabDelivery:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Delivery Click");
                startActivity(new Intent(me, DeliveryActivity.class).putExtra("customer", mCustomer));
                binding.fabMenu.close(false);
                break;
            case R.id.fabMerchandising:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Merchandising Click");
                startActivity(new Intent(CustomerDetailActivity.this, CustomerOperationActivity.class)
                        .putExtra("id", mCustomer.getCustomerId()).putExtra("type", "Salesman"));
                binding.fabMenu.close(false);
                break;
            case R.id.fabChiller:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Merchandising Click");
                startActivity(new Intent(CustomerDetailActivity.this, ChillerRequestFormActivity.class)
                        .putExtra("customer", mCustomer).putExtra("type", "Salesman"));
                binding.fabMenu.close(false);
                break;
            case R.id.fabChillerTransfer:
                UtilApp.logData(CustomerDetailActivity.this, "On Button Merchandising Click");
                startActivity(new Intent(CustomerDetailActivity.this, ChillerTransferFormActivity.class)
                        .putExtra("customer", mCustomer).putExtra("type", "Salesman"));
                binding.fabMenu.close(false);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        UtilApp.logData(CustomerDetailActivity.this, "On Resume Customer Detail");

        //set Display Credit
        try {
            if (mCustomer != null) {
                if (!mCustomer.getCustType().equalsIgnoreCase("1")) {
                    binding.txtCreditDays.setText(mCustomer.getPaymentTerm());
                    binding.txtCreditLimit.setText(UtilApp.getNumberFormate(Double.parseDouble(mCustomer.getCreditLimit())));
                    creditLimit = Double.parseDouble(mCustomer.getCreditLimit());
                    double amtDue = db.getCusAvailableBal(mCustomer.getCustomerId());
                    balance = creditLimit - amtDue;
                    binding.txtAvailableBal.setText("" + UtilApp.getNumberFormate(balance));
                }

                setData();
                setAdapter();
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onBackPressed() {
        if (tag.equalsIgnoreCase("old")) {
            if (App.isVisitInsert) {
                App.isVisitInsert = false;
                endTime = UtilApp.getCurrentTimeVisit();
                db.updateCustomerVisit(endTime, mCustomer.getCustomerId());
                db.UpdateCustomerVisiblity(mCustomer.getCustomerId());
            } else {
                insertVisit();
                App.isVisitInsert = false;
                endTime = UtilApp.getCurrentTimeVisit();
                db.updateCustomerVisit(endTime, mCustomer.getCustomerId());
                db.UpdateCustomerVisiblity(mCustomer.getCustomerId());
            }
            Intent intent = new Intent(CustomerDetailActivity.this, FragmentJourneyPlan.class);
            startActivity(intent);
            finish();
        } else {
            if (App.isVisitInsert) {
                App.isVisitInsert = false;
                endTime = UtilApp.getCurrentTimeVisit();
                db.updateCustomerVisit(endTime, mCustomer.getCustomerId());
                db.UpdateCustomerVisiblity(mCustomer.getCustomerId());
            } else {
                insertVisit();
                App.isVisitInsert = false;
                endTime = UtilApp.getCurrentTimeVisit();
                db.updateCustomerVisit(endTime, mCustomer.getCustomerId());
                db.UpdateCustomerVisiblity(mCustomer.getCustomerId());
            }
            super.onBackPressed();
        }
    }


    private float getRadius(Location currentLocation, Location depotLocation) {

        float distance = (currentLocation.distanceTo(depotLocation)) / 1000;

        return distance;
    }

    int meterInDec = 0;

    public double CalculationByDistance(Location StartP, Location EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.getLatitude();
        double lat2 = EndP.getLatitude();
        double lon1 = StartP.getLongitude();
        double lon2 = EndP.getLongitude();
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    //Call Compititoer API
    public void callCompititorAPI() {


        // mPromotion = new Freeze();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.CHILLER_TRACKING);
        RequestBody route_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getRoute_id());
        RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getCustomerId());
        RequestBody have_fridge = RequestBody.create(MediaType.parse("text/plain"), "No");
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getLatitude());
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), mCustomer.getLongitude());
        RequestBody comments = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComments());

        List<MultipartBody.Part> parts = new ArrayList<>();

//        // add dynamic amount
        if (!mPromotion.getImage1().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image1", mPromotion.getImage1()));
        }

        if (!mPromotion.getImage2().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image2", mPromotion.getImage2()));
        }

        if (!mPromotion.getImage3().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image3", mPromotion.getImage3()));
        }

        if (!mPromotion.getImage4().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image4", mPromotion.getImage4()));
        }

        UtilApp.logData(CustomerDetailActivity.this, "Compatitor Feedback Request: " + mPromotion.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postCompatitorData(method, route_id, salesman_id,
                customer_id, have_fridge, latitude, longitude, comments, parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Freeze Response:", response.toString());
                //  UtilApp.logData(SyncData.this, "Compitior Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                Log.e("Status", jsonObject.getString("STATUS"));
                                if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                    App.isCompititorSync = false;
                                    Log.e("Compititor Success", jsonObject.getString("STATUS"));
                                /*    db.updateCampititorPosted(mPromotion.getCompititorId());
                                    db.updateTransaction(mPromotion.getCompititorId());*/
                                    SalesActivity.arrFOCItem = new ArrayList<>();
                                    startActivity(new Intent(me, SalesActivity.class).putExtra("customer", mCustomer));
                                } else {
                                    // Fail to Post
                                    App.isCompititorSync = false;
                                }
                            }
                        }
                    } catch (Exception e) {
                        App.isCompititorSync = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Compititor fail:", t.toString());
                UtilApp.logData(CustomerDetailActivity.this, "Compititoer Fail: " + t.getMessage());
                App.isCompititorSync = false;
            }
        });
    }

}
