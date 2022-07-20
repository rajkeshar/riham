package com.mobiato.sfa.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.Adapter.ItemAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivitySalesBinding;
import com.mobiato.sfa.databinding.DialogReceiptSummaryBinding;
import com.mobiato.sfa.databinding.OrderCommentDialogBinding;
import com.mobiato.sfa.databinding.OrderPurchaseDialogBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Discount;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.PromoOfferData;
import com.mobiato.sfa.model.SalesInvoice;
import com.mobiato.sfa.model.SalesSummary;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.rest.SyncData;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.DecimalUtils;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;

public class SalesActivity extends BaseActivity implements View.OnClickListener {

    private ActivitySalesBinding binding;
    public ArrayList<Item> arrItem = new ArrayList<>();
    public ArrayList<Item> arrCSDItem = new ArrayList<>();
    public ArrayList<Item> arrJuiceItem = new ArrayList<>();
    public ArrayList<Item> arrWaterItem = new ArrayList<>();
    public ArrayList<Item> arrBisItem = new ArrayList<>();
    public ArrayList<Item> arrHamperItem = new ArrayList<>();
    public ArrayList<Item> arrConfectionaryItem = new ArrayList<>();

    public ArrayList<Item> arrSales = new ArrayList<>();
    public boolean isCustomerPromo = false;
    public boolean isCustomerPromoExclude = false;
    public ArrayList<PromoOfferData> arrPromo = new ArrayList<>();
    public ArrayList<PromoOfferData> arrApplyPromo = new ArrayList<>();

    public static ArrayList<Item> arrFOCItem = new ArrayList<>();
    private CommonAdapter<Item> mAdapter;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private DBManager db;
    private int selectedCat = 0, itemPosition = 0, totalAltQty = 0, totalBaseQty = 0, selectCatId = 2;
    private Customer mCustomer;
    private String invNum, CollNum;
    private JSONArray jsonArray;
    private boolean isLimitAvailable = false, isCatDisApply = false;
    private double balance = 0;
    private ItemAdapter mItemAdapter;
    private SearchView searchView;
    private Discount mDiscount;
    private String mDiscountId = "";
    private String mPromotionId = "";
    private String mPurchaseName = "", mPurchaseNo = "";

    String cust_type = "0";

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySalesBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);

        db = new DBManager(this);

        registerReceiver(broadcastReceiver, new IntentFilter(InputDialogActivity.BROADCAST_ACTION));

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

        mCustomer = (Customer) getIntent().getSerializableExtra("customer");
        try {
            if (getIntent().getStringExtra("customer_type") != null) {
                cust_type = getIntent().getStringExtra("customer_type");
            } else {
                cust_type = "";
            }
        } catch (Exception e) {
            e.toString();
        }

        if (cust_type.equals("OTC")) {
            isCustomerPromo = false;
        } else {
            isCustomerPromo = db.isCustomerSpecificPromo(mCustomer.getCustomerId());
            if (isCustomerPromo) {
                arrPromo = db.getAllCustomerPromotion();
            } else {
                isCustomerPromoExclude = db.isCustomerRoutePromoExclude(mCustomer.getCustomerId());
                if (!isCustomerPromoExclude) {
                    arrPromo = db.getAllPromotion();
                }
            }
        }

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("CSD"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Juice"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Water"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Biscuit"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Hamper"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Confectionary"));

        UtilApp.logData(SalesActivity.this, "Sales OnScreen");

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    selectCatId = 2;
                    setCategoryItem(arrCSDItem, "CSD");
                } else if (tab.getPosition() == 1) {
                    selectCatId = 4;
                    setCategoryItem(arrJuiceItem, "Juice");
                } else if (tab.getPosition() == 2) {
                    selectCatId = 3;
                    setCategoryItem(arrWaterItem, "Water");
                } else if (tab.getPosition() == 3) {
                    selectCatId = 1;
                    setCategoryItem(arrBisItem, "Biscuit");
                } else if (tab.getPosition() == 4) {
                    selectCatId = 5;
                    setCategoryItem(arrHamperItem, "Hamper");
                } else if (tab.getPosition() == 5) {
                    selectCatId = 6;
                    setCategoryItem(arrConfectionaryItem, "Confectionary");
                }

                selectedCat = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        arrSales = new ArrayList<>();
        setItem();
        setCategoryItem(arrCSDItem, "CSD");

        /*System.out.println("check-->" + mCustomer.getCustomerType());*/

        if (!mCustomer.getCustType().equalsIgnoreCase("1")) {
            double creditLimit = Double.parseDouble(mCustomer.getCreditLimit());
            double amtDue = db.getCusAvailableBal(mCustomer.getCustomerId());
            balance = creditLimit - amtDue;
            isLimitAvailable = true;
            if (mCustomer.getCustType().equalsIgnoreCase("2")) {
                setTitle("TC SALE");
            } else if (mCustomer.getCustomerType().equalsIgnoreCase("7")) {
                isLimitAvailable = false;
                setTitle("CASH SALE");
            } else {
                setTitle("CREDIT SALE");
            }
        } else {
            setTitle("CASH SALE");
        }

        binding.rlCheckout.setOnClickListener(this);
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

    private void setCategoryItem(ArrayList<Item> arrData, String catName) {
        arrItem = new ArrayList<>();
        arrItem = arrData;

        if (arrItem.size() > 0) {
            binding.lytNoData.setVisibility(View.GONE);
            binding.rvItemList.setVisibility(View.VISIBLE);
            setAdapter();
        } else {
            binding.lytNoData.setVisibility(View.VISIBLE);
            binding.rvItemList.setVisibility(View.GONE);
            binding.tvNoText.setText(catName + " items not available for Sale!");
        }

    }

    private void setItem() {
        arrCSDItem = new ArrayList<>();
        arrJuiceItem = new ArrayList<>();
        arrWaterItem = new ArrayList<>();
        arrBisItem = new ArrayList<>();
        arrHamperItem = new ArrayList<>();
        arrConfectionaryItem = new ArrayList<>();

        arrCSDItem = db.getVanStockItemList("2");
        arrJuiceItem = db.getVanStockItemList("4");
        arrWaterItem = db.getVanStockItemList("3");
        arrBisItem = db.getVanStockItemList("1");
        arrHamperItem = db.getVanStockItemList("5");
        arrConfectionaryItem = db.getVanStockItemList("6");
    }

    private void setAdapter() {

        Log.e("OTC Route", mCustomer.getRouteId());
        mItemAdapter = new ItemAdapter(SalesActivity.this, arrItem, "", mCustomer.getCustomerId(), mCustomer.getRouteId(), new ItemAdapter.ItemsAdapterListener() {
            @Override
            public void onItemSelected(Item contact, int position) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                    searchView.clearFocus();
                    UtilApp.hideSoftKeyboard(SalesActivity.this);
                }

                itemPosition = getItemIndex(contact.getItemId());
                UtilApp.logData(SalesActivity.this, "On Item Click " + position);
                Intent i = new Intent(SalesActivity.this, InputDialogActivity.class);
                i.putExtra("item", contact);
                i.putExtra("custId", mCustomer.getCustomerId());
                i.putExtra("routeId", mCustomer.getRouteId());
                i.putExtra("customer_type", cust_type);
                i.putExtra("type", "Sale");
                startActivity(i);

            }
        });

        binding.rvItemList.setAdapter(mItemAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
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
                mItemAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            Item item = (Item) intent.getSerializableExtra("item");

            if (arrSales.size() == 0) {
                item.setDiscountId("");
                arrSales.add(item);
            } else {
                item.setDiscountId("");
                boolean flag = false;
                for (int i = 0; i < arrSales.size(); i++) {
                    if (arrSales.get(i).getItemId().equalsIgnoreCase(item.getItemId())) {
                        flag = true;
                        arrSales.set(i, item);
                        break;
                    }
                }

                if (!flag)
                    arrSales.add(item);
            }

            //check Category Discount
//            if (item.getIsFOCItem() != null) {
//                if (item.getIsFOCItem().equals("yes")) {
//                    //Update Invoice Price
//                    updateItem(item);
//                    fillButtonPrice();
//                } else {
//                    updateItem(item);
//                    fillButtonPrice();
//                }
//            } else {

            boolean isCusDiscount = db.isCustomerDiscount(mCustomer.getCustomerId());

            if (isCusDiscount) {
                String discountId = db.getCustomerDiscountId(mCustomer.getCustomerId());
                boolean isCusItDiscount = db.isDiscountItemExist(item.getItemId(), discountId);
                if (isCusItDiscount) {
                    Discount mDiscount = db.getDiscountDetail(discountId);
                    if (mDiscount != null) {
                        applyItemDiscount(item, mDiscount);
                    } else {
                        checkCategoryDiscount(item);
                    }
                } else {
                    checkCategoryDiscount(item);
                }
            } else {
                checkCategoryDiscount(item);
            }


            //  }
        }

    };

    public void checkCategoryDiscount(Item item) {
        boolean isCatDis = db.isCategoryDiscount();
        if (isCatDis) {
            String discountId = db.getCategoryDiscountId();
            boolean isExclude = db.isExcludeCustomerDiscount(mCustomer.getCustomerId(), discountId);
            if (isExclude) {
                checkItemDiscount(item);
            } else {
                Discount mDiscount = db.getDiscountDetail(discountId);
                if (mDiscount != null) {
                    applyCategoryDiscount(item, mDiscount);
                } else {
                    checkItemDiscount(item);
                }
            }
        } else {
            checkItemDiscount(item);
        }
    }

    public void checkItemDiscount(Item item) {

        boolean isItemDiscount = db.isItemDiscount(item.getItemId());
        if (isItemDiscount) {

            String discountId = db.getItemDiscountId(item.getItemId());

            Discount mDiscount = db.getDiscountDetail(discountId);
            if (mDiscount != null) {
                applyItemDiscount(item, mDiscount);
            } else {
                updateItem(item);
                fillButtonPrice();
            }
        } else {
            updateItem(item);
            fillButtonPrice();
        }
    }

    public void applyItemDiscount(Item item, Discount mDiscount) {

        boolean isApply = false;
        String mApplyValue = "";
        if (mDiscount.getDiscountType().equals("1")) {

            double itemAlterPrice = Double.parseDouble(item.getAlterUOMPrice());

            double alterSellPrice = 0;
            alterSellPrice = Integer.parseInt(item.getSaleAltQty()) * itemAlterPrice;

            boolean isDApply = db.isSlabDiscountValue(alterSellPrice, mDiscount.getDiscountId());
            if (isDApply) {
                isApply = true;
                mApplyValue = db.getSlabDiscountValue(alterSellPrice, mDiscount.getDiscountId());
            }
        } else {

            int alterSellQty = 0;
            alterSellQty = Integer.parseInt(item.getSaleAltQty());
            boolean isDApply = db.isSlabDiscountQty(alterSellQty, mDiscount.getDiscountId());
            if (isDApply) {
                isApply = true;
                mApplyValue = db.getSlabDiscountQty(alterSellQty, mDiscount.getDiscountId());
            }
        }

        if (isApply) {
            if (mDiscount.getType().equals("1")) {

                double itemAlterPrice = Double.parseDouble(item.getAlterUOMPrice());

                double alterSellPrice = 0;
                alterSellPrice = Integer.parseInt(item.getSaleAltQty()) * itemAlterPrice;

                double value = 0;
                value = ((alterSellPrice * Double.parseDouble(mApplyValue)) / 100);

                double discountAl = Double.parseDouble(mApplyValue);
                double discountAlAmt = value;

                double finalPrice = alterSellPrice + Double.parseDouble(item.getSaleBasePrice()) - discountAlAmt;
                double sellPrice = alterSellPrice - discountAlAmt;

                //set Data for Discount
                item.setPrice("" + finalPrice);
                item.setDiscountAlAmt("" + DecimalUtils.round(discountAlAmt, 2));
                item.setDiscountAlPer("" + discountAl);
                item.setAlterUOMPrice("" + (itemAlterPrice));
                item.setSaleAltPrice("" + sellPrice);
                item.setDiscountId(mDiscount.getDiscountId());

            } else {

                double itemAlterPrice = Double.parseDouble(item.getAlterUOMPrice());

                double alterSellPrice = 0;
                alterSellPrice = Integer.parseInt(item.getSaleAltQty()) * itemAlterPrice;

                double discountAl = 0;
                double discountAlAmt = Double.parseDouble(mApplyValue);

                double finalPrice = alterSellPrice + Double.parseDouble(item.getSaleBasePrice()) - discountAlAmt;
                double sellPrice = alterSellPrice - discountAlAmt;

                //set Data for Discount
                item.setPrice("" + finalPrice);
                item.setDiscountAlAmt("" + DecimalUtils.round(discountAlAmt, 2));
                item.setDiscountAlPer("" + discountAl);
                item.setAlterUOMPrice("" + (itemAlterPrice));
                item.setSaleAltPrice("" + sellPrice);
                item.setDiscountId(mDiscount.getDiscountId());
            }

        }

        for (int i = 0; i < arrSales.size(); i++) {
            if (arrSales.get(i).getItemId().equalsIgnoreCase(item.getItemId())) {
                arrSales.set(i, item);
                break;
            }
        }

        //update Item after discount
        updateItem(item);
        fillButtonPrice();
    }

    public void applyCategoryDiscount(Item item, Discount mDiscount) {

        ArrayList<String> arrCategoryId = new ArrayList<>();

        //Get Discount Category
        arrCategoryId = db.getDiscountCategory(mDiscount.getDiscountId());

        int categoryQTy = 0;
        double categoryValue = 0;

        for (int i = 0; i < arrSales.size(); i++) {
            if (arrCategoryId.contains(arrSales.get(i).getCategory())) {
                if (arrSales.get(i).getIsFOCItem() != null) {
                    if (!arrSales.get(i).getIsFOCItem().equals("yes")) {
                        if (arrSales.get(i).getIsFreeItem().equals("0")) {
                            if (mDiscount.getDiscountType().equals("1")) {
                                double itemAlterPrice = Double.parseDouble(arrSales.get(i).getAlterUOMPrice());
                                double alterSellPrice = 0;
                                alterSellPrice = Integer.parseInt(arrSales.get(i).getSaleAltQty()) * itemAlterPrice;
                                categoryValue += alterSellPrice;
                            } else {
                                categoryQTy += Integer.parseInt(arrSales.get(i).getSaleAltQty());
                            }
                        }
                    }
                } else {
                    if (mDiscount.getDiscountType().equals("1")) {
                        double itemAlterPrice = Double.parseDouble(arrSales.get(i).getAlterUOMPrice());
                        double alterSellPrice = 0;
                        alterSellPrice = Integer.parseInt(arrSales.get(i).getSaleAltQty()) * itemAlterPrice;
                        categoryValue += alterSellPrice;
                    } else {
                        categoryQTy += Integer.parseInt(arrSales.get(i).getSaleAltQty());
                    }
                }
            }
        }

        if (mDiscount != null) {

            boolean isDApply = false;

            if (mDiscount.getDiscountType().equals("1")) {
                isDApply = db.isSlabDiscountValue(categoryValue, mDiscount.getDiscountId());
            } else {
                isDApply = db.isSlabDiscountQty(categoryQTy, mDiscount.getDiscountId());
            }

            if (isDApply) {
                String mApplyValue = "";

                if (mDiscount.getDiscountType().equals("1")) {
                    mApplyValue = db.getSlabDiscountValue(categoryValue, mDiscount.getDiscountId());
                } else {
                    mApplyValue = db.getSlabDiscountQty(categoryQTy, mDiscount.getDiscountId());
                }
                for (int j = 0; j < arrSales.size(); j++) {

                    if (!arrSales.get(j).getIsFOCItem().equals("yes")) {

                        if (arrCategoryId.contains(arrSales.get(j).getCategory())) {
                            double itemAlterPrice = Double.parseDouble(arrSales.get(j).getAlterUOMPrice());

                            double value = 0;
                            if (mDiscount.getType().equalsIgnoreCase("1")) {
                                value = ((itemAlterPrice * Double.parseDouble(mApplyValue)) / 100);
                            } else {
                                value = Double.parseDouble(mApplyValue);
                            }

                            itemAlterPrice = itemAlterPrice - value;
                            double discountAl = Double.parseDouble(mApplyValue);
                            double discountAlAmt = value;
                            double alterSellPrice = 0;
                            alterSellPrice = Integer.parseInt(arrSales.get(j).getSaleAltQty()) * itemAlterPrice;
                            double finalPrice = alterSellPrice + Double.parseDouble(arrSales.get(j).getSaleBasePrice());

                            //set Data for Discount
                            arrSales.get(j).setPrice("" + finalPrice);
                            arrSales.get(j).setDiscountAlAmt("" + DecimalUtils.round(discountAlAmt, 2));
                            arrSales.get(j).setDiscountAlPer("" + discountAl);
                            arrSales.get(j).setAlterUOMPrice("" + (itemAlterPrice + discountAlAmt));
                            arrSales.get(j).setSaleAltPrice("" + alterSellPrice);
                            arrSales.get(j).setDiscountId(mDiscount.getDiscountId());

                            //update Item after discount
                            updateItem(arrSales.get(j));

                        }
                    }
                }

                fillButtonPrice();

            } else {
                checkItemDiscount(item);
            }
        } else {
            checkItemDiscount(item);
        }
    }

    private int getItemIndex(String itemId) {
        int itemPosition = 0;

        switch (selectedCat) {
            case 0:
                for (int i = 0; i < arrCSDItem.size(); i++) {
                    if (arrCSDItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < arrJuiceItem.size(); i++) {
                    if (arrJuiceItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < arrWaterItem.size(); i++) {
                    if (arrWaterItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < arrBisItem.size(); i++) {
                    if (arrBisItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 4:
                for (int i = 0; i < arrHamperItem.size(); i++) {
                    if (arrHamperItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 5:
                for (int i = 0; i < arrConfectionaryItem.size(); i++) {
                    if (arrConfectionaryItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
        }
        return itemPosition;
    }

    private void updateItem(Item mItem) {
        switch (selectedCat) {
            case 0:
                arrCSDItem.set(itemPosition, mItem);
                setCategoryItem(arrCSDItem, "CSD");
                break;
            case 1:
                arrJuiceItem.set(itemPosition, mItem);
                setCategoryItem(arrJuiceItem, "Juice");
                break;
            case 2:
                arrWaterItem.set(itemPosition, mItem);
                setCategoryItem(arrWaterItem, "Water");
                break;
            case 3:
                arrBisItem.set(itemPosition, mItem);
                setCategoryItem(arrBisItem, "Biscuit");
                break;
            case 4:
                arrHamperItem.set(itemPosition, mItem);
                setCategoryItem(arrHamperItem, "Hamper");
                break;
            case 5:
                arrConfectionaryItem.set(itemPosition, mItem);
                setCategoryItem(arrConfectionaryItem, "Confectionary");
                break;


        }
    }

    private void fillButtonPrice() {
        double btnTotVal = 0;
        for (int i = 0; i < arrSales.size(); i++) {

            double totAmt = Double.parseDouble(arrSales.get(i).getPrice());

            btnTotVal += totAmt;
        }

        grandTot = btnTotVal;
        binding.txtTot.setText(UtilApp.getNumberFormate(btnTotVal) + " UGX");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_checkout:
                if (isLimitAvailable) {
                    if (grandTot > balance) {
                        UtilApp.displayAlert(SalesActivity.this, "Balance is low!");
                    } else {
                        checkOut();
                    }
                } else {
                    checkOut();
                }

                break;
            default:
                break;
        }
    }

    public void checkOut() {

        if (grandTot > 0) {
            grossAmt = 0;
            vatAmt = 0;
            preVatAmt = 0;
            exciseAmt = 0;
            discountAmt = 0;
            netAmt1 = 0;
            totalQty = 0;
            totalAltQty = 0;
            totalBaseQty = 0;

            for (int i = 0; i < arrSales.size(); i++) {
                if (arrSales.get(i).getDiscountId() != null) {
                    if (!arrSales.get(i).getDiscountId().isEmpty()) {
                        isCatDisApply = true;
                        break;
                    }
                }
            }

            arrFOCItem.clear();
            arrFOCItem = new ArrayList<>();

            if (isCatDisApply) {
                makeDilog();
            } else {
                if (isPromoApplay()) {
                    showApplyPromoDialog(0);
                } else {
                    makeDilog();
                }
            }
        } else {
            UtilApp.displayAlert(SalesActivity.this, "Please select at-least one item");
        }
    }

    private void showApplyPromoDialog(int position) {
        int mPromoApplySize = position;

        if (mPromoApplySize == (arrApplyPromo.size())) {
            makeDilog();
        } else {
            if (arrApplyPromo.get(position).isApply == "0") {
                singleChoicePromoDialog(position, arrApplyPromo.get(position).promotionName,
                        arrApplyPromo.get(position).offerQty,
                        arrApplyPromo.get(position).offerUom,
                        arrApplyPromo.get(position).offerItem);
            } else {
                int mNext = position + 1;
                showApplyPromoDialog(mNext);
            }
        }
    }

    private void singleChoicePromoDialog(int mPosition, String planName, String offerQty, String offerUOM, ArrayList<Item> offerItem) {

        final int[] checkedItem = {-1};

        // AlertDialog builder instance to build the alert dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);

        // title of the alert dialog
        alertDialog.setTitle("Select Promotional Item");

        // list of the items to be displayed to
        // the user in the form of list
        // so that user can select the item from
        final String[] listItems = new String[offerItem.size()];
        for (int i = 0; i < offerItem.size(); i++) {
            listItems[i] = offerItem.get(i).getItemName();
        }

        // the function setSingleChoiceItems is the function which builds
        // the alert dialog with the single item selection
        alertDialog.setSingleChoiceItems(listItems, checkedItem[0], new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // update the selected item which is selected by the user
                // so that it should be selected when user opens the dialog next time
                // and pass the instance to setSingleChoiceItems method
                checkedItem[0] = which;

                String itemId = offerItem.get(which).getItemId();

                int mTotalQty = 0;
                for (int j = 0; j < arrSales.size(); j++) {
                    if (arrSales.get(j).getItemId().equals(itemId)) {
                        mTotalQty += Integer.parseInt(arrSales.get(j).getSaleAltQty());
                        break;
                    }
                }

                int finalQty = mTotalQty + Integer.parseInt(offerQty);
                boolean isAvailble = db.isItemAvailable(itemId, "0", String.valueOf(finalQty));
                if (!isAvailble) {
                    // Toast.makeText(SalesActivity.this, "Not enough stock in vanstock! You can not give this item as free goods!", Toast.LENGTH_SHORT).show();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SalesActivity.this);
                    alertDialogBuilder.setTitle("Error")
                            .setMessage("Not enough stock in vanstock! You can not give this item as free goods!")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    dialog1.dismiss();
                                }
                            });
                    // create alert dialog
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                } else {

                    // when selected an item the dialog should be closed with the dismiss method
                    dialog.dismiss();

                    Item itemFree = new Item();
                    itemFree.setItemId(itemId);
                    itemFree.setItemCode(db.getItemCode(itemId));
                    itemFree.setItemName(db.getItemName(itemId));
                    itemFree.setSaleQty(offerQty);
                    itemFree.setBaseUOM(db.getItemUOM(itemId, "Base"));
                    itemFree.setBaseUOMName(db.getUOM(db.getItemUOM(itemId, "Base")));
                    itemFree.setAltrUOM(db.getItemUOM(itemId, "Alter"));
                    itemFree.setAlterUOMName(db.getUOM(db.getItemUOM(itemId, "Alter")));
                    if (db.checkIsAlterUOM(offerUOM, itemId)) {
                        itemFree.setSaleAltQty(offerQty);
                        itemFree.setSaleBaseQty("0");
                    } else {
                        itemFree.setSaleAltQty("0");
                        itemFree.setSaleBaseQty(offerQty);
                    }
                    itemFree.setSaleAltPrice("0");
                    itemFree.setSaleBasePrice("0");
                    itemFree.setQty(offerQty);
                    itemFree.setCategory(db.getItemCategory(itemId));
                    itemFree.setPrice("0");
                    itemFree.setPreVatAmt("0");
                    itemFree.setVatAmt("0");
                    itemFree.setExciseAmt("0");
                    itemFree.setAgentExcise("0");
                    itemFree.setDirectsellexcise("0");
                    itemFree.setNetAmt("0");
                    itemFree.setIsFreeItem("1");
                    itemFree.setDiscountAmt("0");
                    itemFree.setDiscount("0");
                    itemFree.setDiscountAlAmt("0");
                    itemFree.setDiscountAlPer("0");
                    itemFree.setInventoryId(itemId);
                    itemFree.setPerentItemId(arrApplyPromo.get(mPosition).promotionId);
                    arrFOCItem.add(itemFree);

                    arrApplyPromo.get(mPosition).isApply = "1";
                    int mNext = mPosition + 1;
                    showApplyPromoDialog(mNext);
                }
            }
        });

        // set the negative button if the user
        // is not interested to select or change
        // already selected item
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // create and build the AlertDialog instance
        // with the AlertDialog builder instance
        AlertDialog customAlertDialog = alertDialog.create();

        // show the alert dialog when the button is clicked
        customAlertDialog.show();

    }

    private boolean isPromoApplay() {
        boolean isPromo = false;

        arrApplyPromo = new ArrayList<>();

        for (int i = 0; i < arrPromo.size(); i++) {

            int mTotalQty = 0;
            for (int j = 0; j < arrSales.size(); j++) {
                for (int k = 0; k < arrPromo.get(i).orderItem.size(); k++) {
                    Item mOrItem = arrPromo.get(i).orderItem.get(k);
                    if (mOrItem.getItemId().equals(arrSales.get(j).getItemId())) {

                        String assignUOm = arrPromo.get(i).assignUom;

                        if (db.checkIsAlterUOM(assignUOm, mOrItem.getItemId())) {
                            if (!arrSales.get(j).getSaleAltQty().isEmpty())
                                mTotalQty += Integer.parseInt(arrSales.get(j).getSaleAltQty());
                        } else {
                            if (!arrSales.get(j).getSaleBaseQty().isEmpty())
                                mTotalQty += Integer.parseInt(arrSales.get(j).getSaleBaseQty());
                        }

                        break;
                    }
                }
            }

            boolean isPromoApply = false;
            if (isCustomerPromo) {
                isPromoApply = db.isSlabCustomerPromo(String.valueOf(mTotalQty), arrPromo.get(i).promotionId);
            } else {
                isPromoApply = db.isSlabPromo(String.valueOf(mTotalQty), arrPromo.get(i).promotionId);
            }

            if (isPromoApply) {
                String offerQTy = "0";
                if (isCustomerPromo) {
                    offerQTy = db.getSlabCustomerPromoOfferQty(String.valueOf(mTotalQty), arrPromo.get(i).promotionId);
                } else {
                    offerQTy = db.getSlabPromoOfferQty(String.valueOf(mTotalQty), arrPromo.get(i).promotionId);
                }
                PromoOfferData mPromoOFf = arrPromo.get(i);
                mPromoOFf.offerQty = offerQTy;
                mPromoOFf.isApply = "0";
                arrApplyPromo.add(mPromoOFf);
            }
        }

        Log.e("Apply size", String.valueOf(arrApplyPromo.size()));
        if (arrApplyPromo.size() > 0) {
            isPromo = true;
        }
        return isPromo;
    }

    double grossAmt = 0;
    double vatAmt = 0;
    double preVatAmt = 0;
    double exciseAmt = 0;
    double netAmt1 = 0;
    double grandTot = 0;
    double discountAmt = 0;
    int totalQty = 0;


    private void makeDilog() {

        mPromotionId = "";
        LayoutInflater factory = LayoutInflater.from(this);
        DialogReceiptSummaryBinding binding = DataBindingUtil.inflate(factory, R.layout.dialog_receipt_summary, null, false);

        final View deleteDialogView = binding.getRoot();

        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);


        for (int i = 0; i < arrSales.size(); i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(3f);
            // Set new table row layout parameters.
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 0);
            tableRow.setLayoutParams(layoutParams);

            TextView itemTV = new TextView(this);
            TextView qtyTV = new TextView(this);
            TextView priceTV = new TextView(this);

            priceTV.setTypeface(null, Typeface.BOLD);

            TableRow.LayoutParams paramItem = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT
            );

            TableRow.LayoutParams paramQty = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT
            );

            TableRow.LayoutParams paramPrice = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT
            );


            paramItem.weight = 1.7f;
            itemTV.setLayoutParams(paramItem);

            paramQty.weight = 0.5f;
            qtyTV.setLayoutParams(paramQty);

            paramPrice.weight = 0.8f;
            priceTV.setLayoutParams(paramPrice);


            itemTV.setText(arrSales.get(i).getItemName());
            if (Integer.parseInt(arrSales.get(i).getSaleAltQty()) > 0) {
                double totPrice = Double.parseDouble(arrSales.get(i).getSaleAltQty()) * Double.parseDouble(arrSales.get(i).getAlterUOMPrice());
                priceTV.setText(UtilApp.getNumberFormate(totPrice));
            } else {
                double totPrice = Double.parseDouble(arrSales.get(i).getSaleBaseQty()) * Double.parseDouble(arrSales.get(i).getBaseUOMPrice());
                priceTV.setText(UtilApp.getNumberFormate(totPrice));
            }

            if (Integer.parseInt(arrSales.get(i).getSaleAltQty()) > 0 && Integer.parseInt(arrSales.get(i).getSaleBaseQty()) > 0) {
                qtyTV.setText(arrSales.get(i).getSaleAltQty() + "/" + arrSales.get(i).getSaleBaseQty());
                totalBaseQty += Integer.parseInt(arrSales.get(i).getSaleBaseQty());
                totalAltQty += Integer.parseInt(arrSales.get(i).getSaleAltQty());
            } else {
                if (Integer.parseInt(arrSales.get(i).getSaleAltQty()) > 0) {
                    qtyTV.setText(arrSales.get(i).getSaleAltQty());
                    totalAltQty += Integer.parseInt(arrSales.get(i).getSaleAltQty());
                } else {
                    qtyTV.setText(arrSales.get(i).getSaleBaseQty());
                    totalBaseQty += Integer.parseInt(arrSales.get(i).getSaleBaseQty());
                }
            }

            tableRow.addView(itemTV);
            tableRow.addView(qtyTV);
            tableRow.addView(priceTV);

            totalQty += Integer.parseInt(arrSales.get(i).getSaleQty());

            if (Integer.parseInt(arrSales.get(i).getSaleAltQty()) > 0) {
                double totPrice = Double.parseDouble(arrSales.get(i).getSaleAltQty()) * Double.parseDouble(arrSales.get(i).getAlterUOMPrice());
                grossAmt += totPrice;
            } else {
                double totPrice = Double.parseDouble(arrSales.get(i).getSaleBaseQty()) * Double.parseDouble(arrSales.get(i).getBaseUOMPrice());
                grossAmt += totPrice;
            }

            double totDis = Double.parseDouble(arrSales.get(i).getDiscountAlAmt());
            discountAmt += totDis;

            String itemCat1 = db.getItemCategory(arrSales.get(i).getItemId());
            double itemVatAmt = UtilApp.vatAmount(arrSales.get(i), itemCat1);
            // double itemVatAmt = UtilApp.vatAmount(arrSales.get(i));
            vatAmt += itemVatAmt;

            double itemPreVatAmt = UtilApp.getPreVatAmount(arrSales.get(i), itemCat1);
            preVatAmt += itemPreVatAmt;

            double itemExcise = 0;
            exciseAmt += itemExcise;

            double itemNet = itemPreVatAmt - itemExcise;
            netAmt1 += itemNet;
            long lon = Math.round(itemNet);

            arrSales.get(i).setPreVatAmt("" + Math.round(itemPreVatAmt));
            arrSales.get(i).setVatAmt("" + Math.round(itemVatAmt));
            arrSales.get(i).setExciseAmt("" + Math.round(itemExcise));
            arrSales.get(i).setNetAmt("" + lon);
            arrSales.get(i).setCategory(db.getItemCategory(arrSales.get(i).getItemId()));

            if (mDiscountId.equals("")) {
                mDiscountId = arrSales.get(i).getDiscountId();
            } else {
                mDiscountId = mDiscountId + "," + arrSales.get(i).getDiscountId();
            }

            binding.tableLayout.addView(tableRow);

        }

        if (arrFOCItem.size() > 0) {
            for (int i = 0; i < arrFOCItem.size(); i++) {
                TableRow tableRow = new TableRow(this);
                tableRow.setWeightSum(3f);
                // Set new table row layout parameters.
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 20, 0, 0);
                tableRow.setLayoutParams(layoutParams);

                TextView itemTV = new TextView(this);
                TextView qtyTV = new TextView(this);
                TextView priceTV = new TextView(this);

                priceTV.setTypeface(null, Typeface.BOLD);

                TableRow.LayoutParams paramItem = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT
                );

                TableRow.LayoutParams paramQty = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT
                );

                TableRow.LayoutParams paramPrice = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT
                );


                paramItem.weight = 1.7f;
                itemTV.setLayoutParams(paramItem);

                paramQty.weight = 0.5f;
                qtyTV.setLayoutParams(paramQty);

                paramPrice.weight = 0.8f;
                priceTV.setLayoutParams(paramPrice);


                itemTV.setText(arrFOCItem.get(i).getItemName() + " (Free Goods)");
                qtyTV.setText(arrFOCItem.get(i).getQty());
                priceTV.setText(arrFOCItem.get(i).getPrice());

                if (mPromotionId.equals("")) {
                    mPromotionId = arrFOCItem.get(i).getPerentItemId();
                } else {
                    mPromotionId = mPromotionId + "," + arrFOCItem.get(i).getPerentItemId();
                }
                tableRow.addView(itemTV);
                tableRow.addView(qtyTV);
                tableRow.addView(priceTV);

                binding.tableLayout.addView(tableRow);
            }
        }

        deleteDialog.show();

//        if (mDiscount != null) {
//
//            double value = 0;
//            if (mDiscount.getDiscountType().equalsIgnoreCase("1")) {
//                value = ((grandTot * Double.parseDouble(mDiscount.getDiscount())) / 100);
//            } else {
//                value = Double.parseDouble(mDiscount.getDiscount());
//            }
//
//            discountAmt = value;
//            binding.txtGross.setText("" + UtilApp.getNumberFormate(Math.round(grossAmt)));
//            binding.txtExcise.setText("" + UtilApp.getNumberFormate(Math.round(exciseAmt)));
//            binding.txtVat.setText("" + UtilApp.getNumberFormate(Math.round(vatAmt)));
//            binding.txtPreVat.setText("" + UtilApp.getNumberFormate(Math.round(discountAmt)));
//            binding.txtNetVal.setText("" + UtilApp.getNumberFormate(Math.round(netAmt1)));
//            grandTot = grandTot - discountAmt;
//            binding.txtGrandTot.setText("" + UtilApp.getNumberFormate(Math.round(grandTot)));
//
//        } else {

        binding.txtGross.setText("" + UtilApp.getNumberFormate(Math.round(grossAmt)));
        binding.txtExcise.setText("" + UtilApp.getNumberFormate(Math.round(exciseAmt)));
        binding.txtVat.setText("" + UtilApp.getNumberFormate(Math.round(vatAmt)));
        binding.txtPreVat.setText("" + UtilApp.getNumberFormate(Math.round(discountAmt)));
        binding.txtNetVal.setText("" + UtilApp.getNumberFormate(Math.round(netAmt1)));
        binding.txtGrandTot.setText("" + UtilApp.getNumberFormate(Math.round(grandTot)));
        // }

        binding.txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        binding.txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilApp.confirmationDialog("Are you sure you want to proceed?", SalesActivity.this, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        String selection = (String) o;
                        if (selection.equalsIgnoreCase("yes")) {

                           /* if (mCustomer.getCustomerCategory().equals("21")) {
                                Intent in = new Intent(SalesActivity.this, AddNewCustomerActivity.class);
                                in.putExtra("Type", "Sales");
                                in.putExtra("CusType", "OTC");
                                startActivityForResult(in, 2);
                            } else {
                                new getSubmitData().execute();
                            }*/
                            deleteDialog.dismiss();

                            if (arrFOCItem.size() > 0) {
                                openPurchaseCommentDialog();
                            } else {
                                new getSubmitData().execute();
                            }

                        }
                    }
                });

            }
        });

    }

    private void openPurchaseCommentDialog() {

        LayoutInflater factory = LayoutInflater.from(this);
        OrderPurchaseDialogBinding binding = DataBindingUtil.inflate(factory, R.layout.order_purchase_dialog, null, false);

        final View deleteDialogView = binding.getRoot();

        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtComment.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please enter purchaser Name!", Toast.LENGTH_SHORT).show();
                }
                if (binding.edtCommentNo.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please enter purchaser No!", Toast.LENGTH_SHORT).show();
                } else {

                    mPurchaseName = binding.edtComment.getText().toString();
                    mPurchaseNo = binding.edtCommentNo.getText().toString();
                    UtilApp.hideSoftKeyboard(SalesActivity.this);

                    deleteDialog.dismiss();
                    new getSubmitData().execute();
                }

            }
        });

        deleteDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                new getSubmitData().execute();
            }
        }
    }

    private class getSubmitData extends AsyncTask<Void, Void, Boolean> {
        private LoadingSpinner mDialog;
        boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new LoadingSpinner(SalesActivity.this);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            if (arrFOCItem.size() > 0) {

                for (int j = 0; j < arrFOCItem.size(); j++) {
                    int bsQty = Integer.parseInt(arrFOCItem.get(j).getSaleBaseQty());
                    int alQty = Integer.parseInt(arrFOCItem.get(j).getSaleAltQty());
                    boolean isBase = false, isAlter = false;
                    if (bsQty > 0) {
                        isBase = true;
                    } else if (alQty > 0) {
                        isAlter = true;
                    }
                    for (int i = 0; i < arrSales.size(); i++) {
                        if (arrFOCItem.get(j).getItemId().equalsIgnoreCase(arrSales.get(i).getItemId())) {
                            if (isAlter) {
                                alQty += Integer.parseInt(arrSales.get(i).getSaleAltQty());
                            } else if (isBase) {
                                bsQty += Integer.parseInt(arrSales.get(i).getSaleBaseQty());
                            }
                            break;
                        }
                    }

                    if (isBase) {
                        boolean isAvailble = db.isFreeGoodAvailable(arrFOCItem.get(j).getItemId(), "Base", "" + bsQty);
                        if (!isAvailble) {
                            isSuccess = false;
                            break;
                        }
                    } else if (isAlter) {
                        boolean isAvailble = db.isFreeGoodAvailable(arrFOCItem.get(j).getItemId(), "Alter", "" + alQty);
                        if (!isAvailble) {
                            isSuccess = false;
                            break;
                        }
                    }

                }
            }

            if (isSuccess) {

                //GENERATE NEXT INVOICE NUMBER
                invNum = UtilApp.getLastIndex("Invoice");

                //INSERT IN SALES INVOICE TABLE
                insertSalesInvoiceHeader(invNum, "" + totalQty, "" +
                                Math.round(grossAmt), "" +
                                Math.round(grandTot), "" +
                                Math.round(preVatAmt),
                        "" + Math.round(vatAmt), "" +
                                Math.round(exciseAmt), "" +
                                Math.round(netAmt1), "" + totalAltQty, "" + totalBaseQty, "" + Math.round(discountAmt), mDiscountId, mPromotionId,
                        App.Latitude, App.Longitude);

                //Insert into Sales Summary
                SalesSummary mSummary = new SalesSummary();
                mSummary.setTransactionNo(invNum);
                mSummary.setTransactionType("SI");
                mSummary.setCustomerNo(mCustomer.getCustomerId());
                mSummary.setCustomerName(mCustomer.getCustomerName());
                mSummary.setDiscounts("0");
                mSummary.setTotalSales("" + Math.round(grossAmt));
                db.insertSalesSummary(mSummary);

                //get Print Data
                JSONObject data = null;
                try {
                    jsonArray = createPrintData(invNum);
                    data = new JSONObject();
                    data.put("data", (JSONArray) jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //INSERT TRANSACTION
                Transaction transaction = new Transaction();
                transaction.tr_type = Constant.TRANSACTION_TYPES.TT_SALES_CREATED;
                transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
                transaction.tr_customer_num = mCustomer.getCustomerId();
                transaction.tr_customer_name = mCustomer.getCustomerName();
                transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
                transaction.tr_invoice_id = invNum;
                transaction.tr_order_id = "";
                transaction.tr_collection_id = "";
                transaction.tr_pyament_id = "";
                transaction.tr_is_posted = "No";
                transaction.tr_printData = data.toString();
                db.insertTransaction(transaction);

                //update customer Transaction
                db.updateCustomerTransaction(mCustomer.getCustomerId(), "sale");
//                try {
//                    mCustomer.setLatitude(String.valueOf(Double.parseDouble(App.Latitude)));
//                    mCustomer.setLongitude(String.valueOf(Double.parseDouble(App.Longitude)));
//                    System.out.println("LAt-->" + mCustomer.getCustomerId());
//                } catch (Exception e) {
//                }
//                JsonObject jObj = getVisitRequestForPostNew();
//                System.out.println("ppoo0--> " + jObj.get("customer_id"));
//                callVisitInvoiceLocationAPI(jObj);

                //LAT LONG
                // db.insertVisitedCustomersales(mCustomer.getCustomerId(), "" + mCustomer.getLatitude(), "" + mCustomer.getLongitude());


                //  db.insertVisitedCustomer(mCustomer.getCustomerId(),"","" +mCustomer.getLatitude(),""+mCustomer.getLongitude(),"" +mCustomer.getLatitude(),""+mCustomer.getLongitude(),"");

                // db.insertVisitedCustomersales(mCustomer.getCustomerId(),  "" + mCustomer.getLatitude(), "" + mCustomer.getLongitude());
                // db.getPostVisitRequestsalesCount();
               /* long rowInserted = db.insertVisitedCustomer(mCustomer.getCustomerId(),"","" +mCustomer.getLatitude(),""+mCustomer.getLongitude(),"" +mCustomer.getLatitude(),""+mCustomer.getLongitude(),"");
                if(rowInserted != -1)
                    Toast.makeText(myContext, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(myContext, "Something wrong", Toast.LENGTH_SHORT).show();
*/

                for (int i = 0; i < arrSales.size(); i++) {

                    updateVanStockQty(arrSales.get(i));

                    //add Invoice Items
                    db.insertSalesInvoiceItems(arrSales.get(i), invNum, mCustomer.getCustomerId(), "0");

                }
                System.out.println("ooo--> " + db.getPostVisitRequestCount());

                if (arrFOCItem.size() > 0) {
                    for (int i = 0; i < arrFOCItem.size(); i++) {

                        updateVanStockQty(arrFOCItem.get(i));

                        //add Invoice Items
                        db.insertSalesInvoiceItems(arrFOCItem.get(i), invNum, mCustomer.getCustomerId(), "0");
                    }
                }

                //store Last Invoice Number
                Settings.setString(App.INVOICE_LAST, invNum);


                //get collection Number
                CollNum = UtilApp.getLastIndex("Collection");

                //insert into Collection if Credit & TC
                db.addCollection(invNum, CollNum, mCustomer.getCustomerId(), UtilApp.getTCDueDate(mCustomer.getPaymentTerm()),
                        UtilApp.getCurrentDate(), "" + grandTot);
//                    db.insertCollection(invNum, CollNum, mCustomer.getCustomerId(), "",
//                            UtilApp.getTCDueDate(mCustomer.getPaymentTerm()), UtilApp.getCurrentDate(), "" + grandTot, "0", "0",
//                            "0", "", "", "", "0", "1", "N");

                //store Last Invoice Number
                Settings.setString(App.COLLECTION_LAST, CollNum);

            }

            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (mDialog.isShowing()) {
                mDialog.hide();
            }

            if (isSuccess) {
                UtilApp.dialogPrint(SalesActivity.this, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                            UtilApp.createBackgroundJob(getApplicationContext());
                        }
                        String selection = (String) o;
                        UtilApp.logData(SalesActivity.this, "Sales Print :" + selection);
                        if (selection.equalsIgnoreCase("yes")) {
                            PrintLog object = new PrintLog(SalesActivity.this,
                                    SalesActivity.this);
                            object.execute("", jsonArray);
                        } else {
//Riddhi

                            Intent in = new Intent(SalesActivity.this, PaymentDetailActivity.class);
                            in.putExtra("customer", mCustomer);
                            in.putExtra("amount", "" + grandTot);
                            in.putExtra("invDate", "" + UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime());
                            in.putExtra("dueDate", "" + UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime());
                            in.putExtra("invNo", "" + invNum);
                            in.putExtra("collNum", "" + CollNum);
                            in.putExtra("amtClear", "0");
                            in.putExtra("invAmt", "" + grandTot);
                            in.putExtra("customer_type", cust_type);
                            startActivity(in);
                            finish();
                           /* if (mCustomer.getCustType().equalsIgnoreCase("1")) {
                                Intent in = new Intent(SalesActivity.this, PaymentDetailActivity.class);
                                in.putExtra("customer", mCustomer);
                                in.putExtra("amount", "" + grandTot);
                                in.putExtra("invDate", "" + UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime());
                                in.putExtra("dueDate", "" + UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime());
                                in.putExtra("invNo", "" + invNum);
                                in.putExtra("collNum", "" + CollNum);
                                in.putExtra("amtClear", "0");
                                in.putExtra("invAmt", "" + grandTot);
                                in.putExtra("customer_type", cust_type);
                                startActivity(in);
                                finish();
                            }else if (mCustomer.getCustomerType().equalsIgnoreCase("7")) {
                                Intent in = new Intent(SalesActivity.this, PaymentDetailActivity.class);
                                in.putExtra("customer", mCustomer);
                                in.putExtra("amount", "" + grandTot);
                                in.putExtra("invDate", "" + UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime());
                                in.putExtra("dueDate", "" + UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime());
                                in.putExtra("invNo", "" + invNum);
                                in.putExtra("collNum", "" + CollNum);
                                in.putExtra("amtClear", "0");
                                in.putExtra("invAmt", "" + grandTot);
                                in.putExtra("customer_type", cust_type);
                                startActivity(in);
                                finish();
                            } else {
                                Intent intent = new Intent(SalesActivity.this, CustomerDetailActivity.class);
                                intent.putExtra("custmer", mCustomer);
                                intent.putExtra("tag", "old");
                                intent.putExtra("customer_type", cust_type);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }*/

                        }
                    }
                });
            } else {
                showFreeDialog();
            }
        }
    }

    public void showFreeDialog() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SalesActivity.this);
        alertDialogBuilder.setTitle("Alert")
                .setMessage("Free good stock is not available in VanStock.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void updateVanStockQty(Item sellItem) {
        Item currentItem = db.getVanStockItem(sellItem.getItemId());

        int itemUPC = Integer.parseInt(db.getItemUPC(currentItem.getItemId()));

        int exitsAlterQty = Integer.parseInt(currentItem.getAlterUOMQty());
        int exitsBaseQty = Integer.parseInt(currentItem.getBaseUOMQty());

        int sellAlterQty = Integer.parseInt(sellItem.getSaleAltQty());
        int sellBaseQty = Integer.parseInt(sellItem.getSaleBaseQty());
        int sellQty = Integer.parseInt(sellItem.getSaleQty());

        if ((sellBaseQty > 0 && sellAlterQty > 0) && (exitsBaseQty > sellBaseQty && exitsAlterQty > sellAlterQty)) {
            int updateBQty = exitsBaseQty - sellBaseQty;
            int updateAQty = exitsAlterQty - sellAlterQty;
            db.updateVanStockQty(sellItem.getItemId(), "" + updateBQty, "" + updateAQty, "Both");
        } else if (sellBaseQty > 0 && sellAlterQty > 0) {
            int qtyDiff = sellQty - exitsBaseQty;
            int remainAltyQty = (exitsAlterQty * itemUPC) - qtyDiff;

            int updateAlterQty = remainAltyQty / itemUPC;
            int updateBaseQty = remainAltyQty - (updateAlterQty * itemUPC);
            db.updateVanStockQty(sellItem.getItemId(), "" + updateBaseQty, "" + updateAlterQty, "Both");
        } else {
            if (sellBaseQty > 0) {
                if (exitsBaseQty > sellBaseQty) {
                    int updateQty = exitsBaseQty - sellBaseQty;
                    db.updateVanStockQty(sellItem.getItemId(), "" + updateQty, "0", "Base");
                } else {
                    int qtyDiff = sellQty - exitsBaseQty;
                    int remainAltyQty = (exitsAlterQty * itemUPC) - qtyDiff;

                    int updateAlterQty = remainAltyQty / itemUPC;
                    int updateBaseQty = remainAltyQty - (updateAlterQty * itemUPC);
                    db.updateVanStockQty(sellItem.getItemId(), "" + updateBaseQty, "" + updateAlterQty, "Both");
                }
            } else {
                int updateAlterQty = exitsAlterQty - sellAlterQty;
                db.updateVanStockQty(sellItem.getItemId(), "", "" + updateAlterQty, "Alter");
            }
        }

    }


    private void insertSalesInvoiceHeader(String invNum, String totalQty, String grossAmt, String totalAMt, String preVatAmt,
                                          String vatAmt, String exciseAmt, String netAmt, String altQty, String bseQty, String discAmt,
                                          String discId, String promoId, String latitude, String longitude) {
        final SalesInvoice salesInvoice = new SalesInvoice();

        salesInvoice.inv_no = invNum;
        salesInvoice.inv_type = "Sale";
        salesInvoice.exchangeNo = "";
        salesInvoice.inv_type_code = mCustomer.getCustType();
        salesInvoice.cust_code = mCustomer.getCustomerId();
        salesInvoice.inv_date = UtilApp.getCurrentDate();
        salesInvoice.del_date = UtilApp.getCurrentDate();
        salesInvoice.delivery_no = "";
        salesInvoice.total_qty = totalQty;
        salesInvoice.grossAmt = grossAmt;
        salesInvoice.tot_amnt_sales = totalAMt;
        salesInvoice.pre_VatAmt = preVatAmt;
        salesInvoice.vatAmt = vatAmt;
        salesInvoice.exciseAmt = exciseAmt;
        salesInvoice.netAmt = netAmt;
        salesInvoice.discountId = discId;
        salesInvoice.discountAmt = discAmt;
        salesInvoice.exchangeAmt = "";
        salesInvoice.grNo = "";
        salesInvoice.brNo = "";
        salesInvoice.saletime = UtilApp.getCurrent12Time();
        salesInvoice.altQty = altQty;
        salesInvoice.baseQty = bseQty;
        salesInvoice.promotionId = promoId;
        salesInvoice.latitude = latitude;
        salesInvoice.longitute = longitude;
        salesInvoice.purchaseNo = mPurchaseNo;
        salesInvoice.purchaseName = mPurchaseName;

        db.insertSalesInvoice(salesInvoice);

    }

    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createPrintData(String invNum) {
        JSONArray jArr = new JSONArray();

        try {
            double totalAmount = 0;
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.SALES_INVOICE);

            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm a"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));
            mainArr.put("invoicenumber", invNum);  //Invoice No

            mainArr.put("CUSTOMER", mCustomer.getCustomerName());
            mainArr.put("CUSTOMERID", mCustomer.getCustomerCode());
            mainArr.put("customertype", mCustomer.getCustType());
            mainArr.put("ADDRESS", "" + mCustomer.getAddress());
            mainArr.put("TRN", "");

            JSONArray TOTAL = new JSONArray();
            //SALES INVOICE
            JSONArray jData = new JSONArray();
            for (Item obj : arrSales) {

                if (Integer.parseInt(obj.getSaleBaseQty()) > 0 && Integer.parseInt(obj.getSaleAltQty()) > 0) {

                    //Add Base UOM
                    JSONArray data = new JSONArray();
                    data.put(obj.getItemCode()); // ITEM CODE
                    data.put(obj.getItemName());// DESC
                    data.put(obj.getBaseUOMName());// UOM
                    data.put(obj.getSaleBaseQty());//  QTY
                    double unitPrice = Double.parseDouble(obj.getUOMPrice());
                    data.put("" + UtilApp.getNumberFormate(unitPrice));// Unit Price
                    data.put(obj.getDiscountAmt());
                    data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price
                    jData.put(data);

                    //Add Alter UOM
                    JSONArray dataAl = new JSONArray();
                    dataAl.put(obj.getItemCode()); // ITEM CODE
                    dataAl.put(obj.getItemName());// DESC
                    dataAl.put(obj.getAlterUOMName());// UOM
                    dataAl.put(obj.getSaleAltQty());//  QTY
                    double unitAlPrice = Double.parseDouble(obj.getAlterUOMPrice());
                    dataAl.put("" + UtilApp.getNumberFormate(unitAlPrice));// Unit Price
                    dataAl.put(obj.getDiscountAmt());
                    dataAl.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price
                    jData.put(dataAl);


                } else {
                    if (Integer.parseInt(obj.getSaleBaseQty()) > 0) {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getBaseUOMName());// UOM
                        data.put(obj.getSaleBaseQty());//  QTY
                        double unitPrice = Double.parseDouble(obj.getUOMPrice());
                        data.put("" + UtilApp.getNumberFormate(unitPrice));// Unit Price
                        data.put(obj.getDiscountAmt());
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price

                        jData.put(data);
                    } else {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getAlterUOMName());// UOM
                        data.put(obj.getSaleAltQty());//  QTY
                        double unitAlPrice = Double.parseDouble(obj.getAlterUOMPrice());
                        data.put("" + UtilApp.getNumberFormate(unitAlPrice));// Unit Price
                        data.put(obj.getDiscountAmt());
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price

                        jData.put(data);
                    }
                }
            }

            for (Item obj : arrFOCItem) {

                if (Integer.parseInt(obj.getSaleBaseQty()) > 0) {
                    JSONArray data = new JSONArray();
                    data.put(obj.getItemCode()); // ITEM CODE
                    data.put(obj.getItemName());// DESC
                    data.put(obj.getBaseUOMName());// UOM
                    data.put(obj.getSaleBaseQty());//  QTY
                    data.put("0");// Unit Price
                    data.put(obj.getDiscountAmt());
                    data.put(obj.getSaleBasePrice());// Total Price

                    jData.put(data);
                } else {
                    JSONArray data = new JSONArray();

                    data.put(obj.getItemCode()); // ITEM CODE
                    data.put(obj.getItemName());// DESC
                    data.put(obj.getAlterUOMName());// UOM
                    data.put(obj.getSaleAltQty());//  QTY
                    data.put("0");// Unit Price
                    data.put(obj.getDiscountAmt());
                    data.put(obj.getSaleAltPrice());// Total Price

                    jData.put(data);
                }
            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("Total Amount", UtilApp.getNumberFormate(Math.round(grandTot)));
            totalObj.put("Discount", UtilApp.getNumberFormate(Math.round(discountAmt)));
            totalObj.put("Sub Total", UtilApp.getNumberFormate(Math.round(grossAmt)));
            totalObj.put("VAT", UtilApp.getNumberFormate(Math.round(vatAmt)));
            totalObj.put("NET", UtilApp.getNumberFormate(Math.round(netAmt1)));
            totalObj.put("Invoice Total", UtilApp.getNumberFormate(Math.round(grandTot)));
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            mainArr.put("data", jData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jArr;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void callback() {
        if (mCustomer.getCustType().equalsIgnoreCase("1")) {
            Intent in = new Intent(SalesActivity.this, PaymentDetailActivity.class);
            in.putExtra("customer", mCustomer);
            in.putExtra("amount", "" + grandTot);
            in.putExtra("invDate", "" + UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime());
            in.putExtra("dueDate", "" + UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime());
            in.putExtra("invNo", "" + invNum);
            in.putExtra("collNum", "" + CollNum);
            in.putExtra("amtClear", "0");
            in.putExtra("invAmt", "" + grandTot);
            startActivity(in);
            finish();
        } else {
            Intent intent = new Intent(SalesActivity.this, CustomerDetailActivity.class);
            intent.putExtra("custmer", mCustomer);
            intent.putExtra("tag", "old");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    public JsonObject getVisitRequestForPostNew() {

        JsonObject jObj = new JsonObject();

        // HashMap<String, String> mData = db.getPostVisitItemsINVOICE();

        try {
            System.out.println("poooiu--> " + mCustomer.getCustomerId());
            jObj.addProperty("method", App.POST_CUSTOMERVISIT_INVOICE);
            jObj.addProperty("customer_id", mCustomer.getCustomerId());
            jObj.addProperty("latitude", mCustomer.getLatitude());
            jObj.addProperty("longitude", mCustomer.getLongitude());
           /* boolean isplanned = db.isSyncCustomer(mData.get("customerid"), UtilApp.getCurrentDay());
            if (isplanned) {
                jObj.addProperty("unpland", "0");
            } else {
                jObj.addProperty("unpland", "1");
            }*/

        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }


    //Call invoice location API
    private void callVisitInvoiceLocationAPI(JsonObject jObj) {

        UtilApp.logData(SalesActivity.this, "Customer invoice location Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("invoice location Response:", response.toString());
                UtilApp.logData(SalesActivity.this, "Customer invoice location Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SalesActivity.this, "Customer Invoice Location Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            // db.updateInvoiceLocationStatus(response.body().get("customerid").getAsString());
                            App.isVisitRequestSync = false;
                        } else {
                            // Fail to Post
                            App.isVisitRequestSync = false;
                        }
                    }
                } else {
                    App.isVisitRequestSync = false;
                }

                // sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isVisitRequestSync = false;
                Log.e("Invoice Visit Fail", error.getMessage());
                UtilApp.logData(SalesActivity.this, "Customer Visit Fail: " + error.getMessage());
            }
        });
    }
}
