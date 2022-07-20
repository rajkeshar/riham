package com.mobiato.sfa.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.Adapter.ItemAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityExchangeBinding;
import com.mobiato.sfa.databinding.RowItemCategoryBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Category;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.SalesInvoice;
import com.mobiato.sfa.model.SalesSummary;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.DecimalUtils;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ExchangeActivity extends BaseActivity implements View.OnClickListener {

    public ActivityExchangeBinding binding;
    public ArrayList<Category> arrCategory = new ArrayList<>();
    public ArrayList<Item> arrItem = new ArrayList<>();
    public ArrayList<Item> arrCSDItem = new ArrayList<>();
    public ArrayList<Item> arrJuiceItem = new ArrayList<>();
    public ArrayList<Item> arrHamperItem = new ArrayList<>();
    public ArrayList<Item> arrWaterItem = new ArrayList<>();
    public ArrayList<Item> arrBisItem = new ArrayList<>();
    public ArrayList<Item> arrSLCSDItem = new ArrayList<>();
    public ArrayList<Item> arrSLJuiceItem = new ArrayList<>();
    public ArrayList<Item> arrSLWaterItem = new ArrayList<>();
    public ArrayList<Item> arrSLBisItem = new ArrayList<>();
    public ArrayList<Item> arrSLHamperItem = new ArrayList<>();
    public ArrayList<Item> arrSLConfectionaryItem = new ArrayList<>();
    public ArrayList<Item> arrExchange = new ArrayList<>();
    public ArrayList<Item> arrSales = new ArrayList<>();
    public ArrayList<Item> arrBadReturn = new ArrayList<>();
    public ArrayList<Item> arrGoodReturn = new ArrayList<>();
    public ArrayList<Item> arrConfectionaryItem = new ArrayList<>();
    private CommonAdapter<Category> mCategoryAdapter;
    //private CommonAdapter<Item> mAdapter;
    private ItemAdapter mAdapter;
    int catPosition = 0, itemPosition = 0, selectedType = 0;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private Customer mCustomer;
    private DBManager db;
    private boolean isExchange = true;
    private double exchangeAmt = 0, saleAmt = 0, brAmt = 0, brPreVatAmt = 0, brVatAmt = 0, brNetAmt = 0, brExciseAmt = 0,
            grAmt = 0, grPreVatAmt = 0, grVatAmt = 0, grNetAmt = 0, grExciseAmt = 0,
            slPreVatAmt = 0, slVatAmt = 0, slNetAmt = 0, slExciseAmt = 0, totalQty = 0, totalAlQty = 0, totalBaseQty = 0;
    private String invNum, BRNo = "", GRNo = "", CollNum, exchangeNum;
    private JSONArray jsonArray;
    private SearchView searchView;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExchangeBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Exchange");

        registerReceiver(broadcastReceiver, new IntentFilter(InputDialogOrderActivity.BROADCAST_ACTION));

        db = new DBManager(ExchangeActivity.this);

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

        if (getIntent() != null) {
            mCustomer = (Customer) getIntent().getSerializableExtra("customer");
        }

        setCategory();
        setCategoryAdapter();

        arrSales = new ArrayList<>();
        arrExchange = new ArrayList<>();

        setItem();
        setSalesItem();
        setCategoryItem(arrCSDItem, "CSD");

        binding.fab.setOnClickListener(this);
        binding.fabNext.setOnClickListener(this);
        binding.fabPrevious.setOnClickListener(this);
    }

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
            binding.tvNoText.setText(catName + " items not available!");
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

    private void setCategory() {
        arrCategory = new ArrayList<>();
        Category c1 = new Category();
        c1.name = "CSD";
        c1.catId = "2";
        c1.is_click = "1";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Juice";
        c1.catId = "4";
        c1.is_click = "0";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Water";
        c1.catId = "3";
        c1.is_click = "0";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Biscuit";
        c1.catId = "1";
        c1.is_click = "0";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Hamper";
        c1.catId = "5";
        c1.is_click = "0";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Confectionary";
        c1.catId = "6";
        c1.is_click = "0";
        arrCategory.add(c1);

    }

    private void setItem() {
        arrCSDItem = new ArrayList<>();
        arrJuiceItem = new ArrayList<>();
        arrWaterItem = new ArrayList<>();
        arrBisItem = new ArrayList<>();
        arrHamperItem = new ArrayList<>();
        arrConfectionaryItem = new ArrayList<>();

        arrCSDItem = db.getItemListByCategory("2");
        arrJuiceItem = db.getItemListByCategory("4");
        arrWaterItem = db.getItemListByCategory("3");
        arrBisItem = db.getItemListByCategory("1");
        arrHamperItem = db.getItemListByCategory("5");
        arrConfectionaryItem = db.getItemListByCategory("6");
    }

    private void setSalesItem() {
        arrSLCSDItem = new ArrayList<>();
        arrSLJuiceItem = new ArrayList<>();
        arrSLWaterItem = new ArrayList<>();
        arrSLBisItem = new ArrayList<>();
        arrSLHamperItem = new ArrayList<>();
        arrSLConfectionaryItem
                = new ArrayList<>();


        arrSLCSDItem = db.getVanStockItemList("2");
        arrSLJuiceItem = db.getVanStockItemList("4");
        arrSLWaterItem = db.getVanStockItemList("3");
        arrSLBisItem = db.getVanStockItemList("1");
        arrSLHamperItem = db.getVanStockItemList("5");
        arrSLConfectionaryItem = db.getVanStockItemList("6");

    }

    private void setCategoryAdapter() {

        mCategoryAdapter = new CommonAdapter<Category>(arrCategory) {

            @Override
            public void onBind(CommonViewHolder holder, int position) {

                if (holder.binding instanceof RowItemCategoryBinding) {
                    ((RowItemCategoryBinding) holder.binding).tvCategory.setText(arrCategory.get(position).name);

                    if (arrCategory.get(position).is_click.equals("1")) {
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setBackgroundResource(R.drawable.rounded_rect_blue_capsule);
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setBackgroundResource(R.drawable.rounded_rect_white_capsule);
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setTextColor(Color.parseColor("#606AA9"));
                    }

                    ((RowItemCategoryBinding) holder.binding).tvCategory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            arrCategory.get(position).is_click = "1";
                            arrCategory.get(catPosition).is_click = "0";
                            catPosition = position;
                            notifyDataSetChanged();

                            switch (arrCategory.get(position).catId) {
                                case "1":
                                    if (isExchange)
                                        setCategoryItem(arrBisItem, "Biscuit");
                                    else
                                        setCategoryItem(arrSLBisItem, "Biscuit");
                                    break;
                                case "2":
                                    if (isExchange)
                                        setCategoryItem(arrCSDItem, "CSD");
                                    else
                                        setCategoryItem(arrSLCSDItem, "CSD");
                                    break;
                                case "3":
                                    if (isExchange)
                                        setCategoryItem(arrWaterItem, "Water");
                                    else
                                        setCategoryItem(arrSLWaterItem, "Water");
                                    break;
                                case "4":
                                    if (isExchange)
                                        setCategoryItem(arrJuiceItem, "Juice");
                                    else
                                        setCategoryItem(arrSLJuiceItem, "Juice");
                                    break;
                                case "5":
                                    if (isExchange)
                                        setCategoryItem(arrHamperItem, "Hamper");
                                    else
                                        setCategoryItem(arrSLHamperItem, "Hamper");
                                    break;
                                case "6":
                                    if (isExchange)
                                        setCategoryItem(arrConfectionaryItem, "Confectionary");
                                    else
                                        setCategoryItem(arrSLConfectionaryItem, "Confectionary");
                                    break;
                                default:
                                    break;
                            }


                        }
                    });
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_category;
            }
        };

        binding.rvCategory.setAdapter(mCategoryAdapter);
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
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }


    private void setAdapter() {

        mAdapter = new ItemAdapter(ExchangeActivity.this, arrItem, "", mCustomer.getCustomerId(), mCustomer.getRouteId(), new ItemAdapter.ItemsAdapterListener() {
            @Override
            public void onItemSelected(Item contact, int position) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                    searchView.clearFocus();
                    UtilApp.hideSoftKeyboard(ExchangeActivity.this);
                }

                itemPosition = getItemIndex(contact.getItemId());
                UtilApp.logData(ExchangeActivity.this, "On Item Click " + position);
                if (isExchange) {
                    Intent i = new Intent(ExchangeActivity.this, InputDialogOrderActivity.class);
                    i.putExtra("type", "Exchange");
                    i.putExtra("customer", mCustomer.getCustomerId());
                    i.putExtra("item", contact);
                    startActivity(i);
                } else {
                    Intent i = new Intent(ExchangeActivity.this, InputDialogActivity.class);
                    i.putExtra("item", arrItem.get(position));
                    i.putExtra("custId", mCustomer.getCustomerId());
                    i.putExtra("routeId", mCustomer.getRouteId());
                    i.putExtra("type", "Sale");
                    startActivity(i);
                }

            }
        });

        binding.rvItemList.setAdapter(mAdapter);

        /*mAdapter = new CommonAdapter<Item>(arrItem) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemSaleBinding) {
                    Item mItem = arrItem.get(position);
                    ((RowItemSaleBinding) holder.binding).setItem(mItem);

                    ((RowItemSaleBinding) holder.binding).imageView2.setLetter(arrItem.get(position).getItemName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowItemSaleBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    if (mItem.getAltrUOM() != null) {
                        ((RowItemSaleBinding) holder.binding).tvAlterUOM.setText(mItem.getAlterUOMName() + ": " + mItem.getSaleAltQty());
                        if (mItem.getBaseUOM() != null) {
                            ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getSaleBaseQty());
                        } else {
                            ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setVisibility(View.GONE);
                        }
                    } else {
                        ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                        ((RowItemSaleBinding) holder.binding).tvAlterUOM.setVisibility(View.GONE);
                        ((RowItemSaleBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getSaleBaseQty());
                    }

                    double itemBasePrice = 0, itemAlterPrice = 0;

                    if (db.isPricingItems(mCustomer.getCustomerId(), mItem.getItemId())) {
                        Item pItem = db.getCustPricingItems(mCustomer.getCustomerId(), mItem.getItemId());
                        itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
                        itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                    } else if (db.isAgentPricingItems(mItem.getItemId())) {
                        Item pItem = db.getAgentPricingItems(mItem.getItemId());
                        itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
                        itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                    } else {
                        itemBasePrice = db.getItemPrice(mItem.getItemId());
                        itemAlterPrice = db.getItemAlterPrice(mItem.getItemId());
                    }

                    ((RowItemSaleBinding) holder.binding).lytUnitPrice.setVisibility(View.VISIBLE);
                    ((RowItemSaleBinding) holder.binding).tvUnitAlterPrice.setText("Unit Price: " + UtilApp.getNumberFormate((itemAlterPrice - Double.parseDouble(mItem.getDiscountAlAmt()))) + " UGX");
                    ((RowItemSaleBinding) holder.binding).dividerUnitPrice.setVisibility(View.VISIBLE);
                    ((RowItemSaleBinding) holder.binding).tvUnitBaseUOMPrice.setVisibility(View.VISIBLE);
                    ((RowItemSaleBinding) holder.binding).tvUnitBaseUOMPrice.setText(UtilApp.getNumberFormate((itemBasePrice - Double.parseDouble(mItem.getDiscountAmt()))) + " UGX");


                    if (mItem.getSaleAltPrice() != null) {
                        ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
                        ((RowItemSaleBinding) holder.binding).tvAlterPrice.setText("Price: " + mItem.getSaleAltPrice() + " UGX");
                        if (mItem.getSaleBasePrice() != null) {
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText(mItem.getSaleBasePrice() + " UGX");
                        } else {
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.GONE);
                        }
                    } else {
                        if (mItem.getSaleBasePrice() != null) {
                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvAlterPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText("Price: " + mItem.getSaleBasePrice() + " UGX");
                        } else {
                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.GONE);
                        }

                    }

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemPosition = position;
                            if (isExchange) {
                                Intent i = new Intent(ExchangeActivity.this, InputDialogOrderActivity.class);
                                i.putExtra("type", "Exchange");
                                i.putExtra("customer", mCustomer.getCustomerId());
                                i.putExtra("item", arrItem.get(position));
                                startActivity(i);
                            } else {
                                Intent i = new Intent(ExchangeActivity.this, InputDialogActivity.class);
                                i.putExtra("item", arrItem.get(position));
                                i.putExtra("custId", mCustomer.getCustomerId());
                                startActivity(i);
                            }
                        }
                    });
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_sale;
            }
        };

        binding.rvItemList.setAdapter(mAdapter);*/
    }

    private int getItemIndex(String itemId) {
        int itemPosition = 0;
        ArrayList<Item> arrItem;
        switch (catPosition) {
            case 0:
                arrItem = new ArrayList<>();
                if (isExchange)
                    arrItem = arrCSDItem;
                else
                    arrItem = arrSLCSDItem;

                for (int i = 0; i < arrItem.size(); i++) {
                    if (arrItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 1:
                arrItem = new ArrayList<>();
                if (isExchange)
                    arrItem = arrJuiceItem;
                else
                    arrItem = arrSLJuiceItem;

                for (int i = 0; i < arrItem.size(); i++) {
                    if (arrItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 2:
                arrItem = new ArrayList<>();
                if (isExchange)
                    arrItem = arrWaterItem;
                else
                    arrItem = arrSLWaterItem;
                for (int i = 0; i < arrItem.size(); i++) {
                    if (arrItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 3:
                arrItem = new ArrayList<>();
                if (isExchange)
                    arrItem = arrBisItem;
                else
                    arrItem = arrSLBisItem;
                for (int i = 0; i < arrItem.size(); i++) {
                    if (arrItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 4:
                arrItem = new ArrayList<>();
                if (isExchange)
                    arrItem = arrHamperItem;
                else
                    arrItem = arrSLHamperItem;

                for (int i = 0; i < arrItem.size(); i++) {
                    if (arrItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;

            case 5:
                arrItem = new ArrayList<>();
                if (isExchange)
                    arrItem = arrConfectionaryItem;
                else
                    arrItem = arrSLConfectionaryItem;

                for (int i = 0; i < arrItem.size(); i++) {
                    if (arrItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
        }
        return itemPosition;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                if (exchangeAmt == saleAmt) {
                    new getDataConfirm().execute();
                } else {
                    UtilApp.displayAlert(ExchangeActivity.this, "Please sale item based on exchange amount!");
                }
                break;
            case R.id.fabNext:
                if (exchangeAmt == 0) {
                    UtilApp.displayAlert(ExchangeActivity.this, "Please select any item!");
                } else {
                    setTitle("Sale");
                    isExchange = false;
                    arrCategory.get(0).is_click = "1";
                    arrCategory.get(catPosition).is_click = "0";
                    catPosition = 0;
                    setCategoryAdapter();
                    setCategoryItem(arrSLCSDItem, "CSD");
                    binding.fabPrevious.setVisibility(View.VISIBLE);
                    binding.fab.setVisibility(View.VISIBLE);
                    binding.fabNext.setVisibility(View.GONE);
                    binding.titleAmount.setText("Balance");
                    double balance = exchangeAmt - saleAmt;
                    binding.tvAmount.setText(UtilApp.getNumberFormate(balance) + " UGX");
                }

                break;
            case R.id.fabPrevious:
                setTitle("Exchange");
                arrCategory.get(0).is_click = "1";
                arrCategory.get(catPosition).is_click = "0";
                catPosition = 0;
                setCategoryItem(arrCSDItem, "CSD");
                setCategoryAdapter();
                binding.fabNext.setVisibility(View.VISIBLE);
                binding.fabPrevious.setVisibility(View.GONE);
                binding.fab.setVisibility(View.GONE);
                isExchange = true;
                setCategoryItem(arrCSDItem, "CSD");
                binding.titleAmount.setText("Total Exchange");
                binding.tvAmount.setText(UtilApp.getNumberFormate(exchangeAmt) + " UGX");
                break;
            default:
                break;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            Item item = (Item) intent.getSerializableExtra("item");

            if (isExchange) {
                if (arrExchange.size() == 0) {
                    arrExchange.add(item);
                } else {

                    boolean flag = false;
                    for (int i = 0; i < arrExchange.size(); i++) {
                        if (arrExchange.get(i).getItemId().equalsIgnoreCase(item.getItemId())) {
                            flag = true;
                            arrExchange.set(i, item);
                            break;
                        }
                    }

                    if (!flag)
                        arrExchange.add(item);
                }

                updateItem(item);
                fillExchangePrice();
            } else {
                if (arrSales.size() == 0) {
                    arrSales.add(item);
                } else {

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

                updateSaleItem(item);
                fillSalePrice();
            }

        }

    };

    private void updateItem(Item mItem) {
        switch (Integer.parseInt(arrCategory.get(catPosition).catId)) {
            case 2:
                arrCSDItem.set(itemPosition, mItem);
                setCategoryItem(arrCSDItem, "CSD");
                break;
            case 4:
                arrJuiceItem.set(itemPosition, mItem);
                setCategoryItem(arrJuiceItem, "Juice");
                break;
            case 3:
                arrWaterItem.set(itemPosition, mItem);
                setCategoryItem(arrWaterItem, "Water");
                break;
            case 1:
                arrBisItem.set(itemPosition, mItem);
                setCategoryItem(arrBisItem, "Biscuit");
                break;
            case 5:
                arrHamperItem.set(itemPosition, mItem);
                setCategoryItem(arrHamperItem, "Hamper");
                break;
            case 6:
                arrConfectionaryItem.set(itemPosition, mItem);
                setCategoryItem(arrConfectionaryItem, "Confectionary");
                break;

        }
    }

    private void fillExchangePrice() {
        exchangeAmt = 0;
        for (int i = 0; i < arrExchange.size(); i++) {

            double totAmt = Double.parseDouble(arrExchange.get(i).getPrice());

            exchangeAmt += totAmt;
        }

        binding.titleAmount.setText("Total Exchange");
        binding.tvAmount.setText(UtilApp.getNumberFormate(exchangeAmt) + " UGX");
    }

    private void fillSalePrice() {
        saleAmt = 0;
        totalQty = 0;
        slExciseAmt = 0;
        slVatAmt = 0;
        slPreVatAmt = 0;
        slNetAmt = 0;
        totalAlQty = 0;
        totalBaseQty = 0;


        for (int i = 0; i < arrSales.size(); i++) {

            double totAmt = Double.parseDouble(arrSales.get(i).getPrice());

            saleAmt += totAmt;

            if (Integer.parseInt(arrSales.get(i).getSaleAltQty()) > 0 && Integer.parseInt(arrSales.get(i).getSaleBaseQty()) > 0) {
                totalBaseQty += Integer.parseInt(arrSales.get(i).getSaleBaseQty());
                totalAlQty += Integer.parseInt(arrSales.get(i).getSaleAltQty());
            } else {
                if (Integer.parseInt(arrSales.get(i).getSaleAltQty()) > 0) {
                    totalAlQty += Integer.parseInt(arrSales.get(i).getSaleAltQty());
                } else {
                    totalBaseQty += Integer.parseInt(arrSales.get(i).getSaleBaseQty());
                }
            }

            totalQty += Integer.parseInt(arrSales.get(i).getSaleQty());

            double itemVatAmt = UtilApp.getVat(Double.parseDouble(arrSales.get(i).getPrice()));
            slVatAmt += itemVatAmt;

            double itemPreVatAmt = Double.parseDouble(arrSales.get(i).getPrice()) - itemVatAmt;
            slPreVatAmt += itemPreVatAmt;

            double itemExcise = 0;
            /*String itemCat = db.getItemCategory(arrSellItem.get(i).getItemId());
            if (itemCat.equalsIgnoreCase("1")) {
                itemExcise = 0;
            } else {
                if (itemCat.equalsIgnoreCase("3")) {
                    itemExcise = UtilApp.getExciseSecondMethod(Double.parseDouble(arrSellItem.get(i).getPrice()), itemCat);
                } else {
                    double exciseFirst = UtilApp.getExciseFirstMethod(3.84, itemCat);
                    double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(arrSellItem.get(i).getPrice()), itemCat);

                    if (exciseFirst > exciseSecond) {
                        itemExcise = exciseFirst;
                    } else {
                        itemExcise = exciseSecond;
                    }
                }
            }
*/
            slExciseAmt += itemExcise;

            double itemNet = itemPreVatAmt - itemExcise;
            slNetAmt += itemNet;

        }

        binding.titleAmount.setText("Balance");
        double balance = exchangeAmt - saleAmt;
        binding.tvAmount.setText(UtilApp.getNumberFormate(balance) + " UGX");

    }

    private void updateSaleItem(Item mItem) {
        switch (Integer.parseInt(arrCategory.get(catPosition).catId)) {
            case 2:
                arrSLCSDItem.set(itemPosition, mItem);
                setCategoryItem(arrSLCSDItem, "CSD");
                break;
            case 4:
                arrSLJuiceItem.set(itemPosition, mItem);
                setCategoryItem(arrSLJuiceItem, "Juice");
                break;
            case 3:
                arrSLWaterItem.set(itemPosition, mItem);
                setCategoryItem(arrSLWaterItem, "Water");
                break;
            case 1:
                arrSLBisItem.set(itemPosition, mItem);
                setCategoryItem(arrSLBisItem, "Biscuit");
                break;
            case 5:
                arrSLHamperItem.set(itemPosition, mItem);
                setCategoryItem(arrSLHamperItem, "Hamper");
                break;
            case 6:
                arrSLConfectionaryItem.set(itemPosition, mItem);
                setCategoryItem(arrSLConfectionaryItem, "Confectionary");
                break;

        }
    }

    private void confirmData() {

        //GENERATE NEXT Exchange NUMBER
        exchangeNum = UtilApp.getLastIndex("Exchange");
        //exchangeNum = "EXCH000001";

        if (arrExchange.size() > 0) {

            arrBadReturn = new ArrayList<>();
            arrBadReturn = getReturnItem("Bad");

            arrGoodReturn = new ArrayList<>();
            arrGoodReturn = getReturnItem("Good");
            System.out.println("chek-->" + arrBadReturn.size() + " Good--> " + arrGoodReturn.size());

            if (arrBadReturn.size() > 0) {

                //GENERATE NEXT RETURN NUMBER
                BRNo = UtilApp.getLastIndex("Return");

                db.insertReturnItems(BRNo, UtilApp.getCurrentDate(), "" +
                                DecimalUtils.round(brAmt, 2), mCustomer.getCustomerId(), "Bad",
                        Settings.getString(App.SALESMANID), ""
                                + DecimalUtils.round(brVatAmt, 2), "" +
                                DecimalUtils.round(brPreVatAmt, 2), "" +
                                DecimalUtils.round(brExciseAmt, 2),
                        "" + DecimalUtils.round(brNetAmt, 2), arrBadReturn, exchangeNum);


                Settings.setString(App.RETURN_LAST, BRNo);
            }

            if (arrGoodReturn.size() > 0) {

                //GENERATE NEXT RETURN NUMBER
                GRNo = UtilApp.getLastIndex("Return");

                db.insertReturnItems(GRNo, UtilApp.getCurrentDate(), "" +
                                DecimalUtils.round(grAmt, 2), mCustomer.getCustomerId(), "Good",
                        Settings.getString(App.SALESMANID), ""
                                + DecimalUtils.round(grVatAmt, 2), "" +
                                DecimalUtils.round(grPreVatAmt, 2), "" +
                                DecimalUtils.round(grExciseAmt, 2),
                        "" + DecimalUtils.round(grNetAmt, 2), arrGoodReturn, exchangeNum);


                Settings.setString(App.RETURN_LAST, GRNo);
            }

            double bExchAmt = brAmt + grAmt;
            String exchange_total = Settings.getString(App.EXCHANGE_AMONUNT);
            try {
                if (exchange_total.equals("null")) {
                    exchange_total = "0";
                }
            } catch (Exception e) {
                exchange_total = "0";
            }
            double pp = bExchAmt + Double.valueOf(exchange_total);
            System.out.println("exchange-->" + pp);
            try {
                if (exchange_total.equals("null")) {
                    exchange_total = "0";
                }
                double exchange_send = DecimalUtils.round(bExchAmt, 2) + (DecimalUtils.round(Double.valueOf(exchange_total), 2));
                System.out.println("exchange1-->" + exchange_send);
                Settings.setString(App.EXCHANGE_AMONUNT, String.valueOf(DecimalUtils.round(exchange_send, 2)));

            } catch (Exception e) {
                e.toString();
            }

            SalesSummary mSummary = new SalesSummary();
            mSummary.setTransactionNo(exchangeNum);
            mSummary.setTransactionType("Bad");
            mSummary.setCustomerNo(mCustomer.getCustomerId());
            mSummary.setCustomerName(mCustomer.getCustomerName());
            mSummary.setDiscounts("0");
            mSummary.setTotalSales("" + DecimalUtils.round(bExchAmt, 2));
            db.insertSalesSummary(mSummary);

        }

        //Then Take Sales
        if (arrSales.size() > 0) {
            //GENERATE NEXT INVOICE NUMBER
            invNum = UtilApp.getLastIndex("Invoice");

            //INSERT IN SALES INVOICE TABLE
           /* insertSalesInvoiceHeader(invNum, "" + totalQty, "" +
                            DecimalUtils.round(saleAmt, 2), "" +
                            DecimalUtils.round(slPreVatAmt, 2),
                    "" + DecimalUtils.round(slVatAmt, 2), "" +
                            DecimalUtils.round(slExciseAmt, 2), "" +
                            DecimalUtils.round(slNetAmt, 2), "" +
                            DecimalUtils.round(exchangeAmt, 2), BRNo, GRNo, exchangeNum, "" + totalAlQty, "" + totalBaseQty);*/

            insertSalesInvoiceHeader(invNum, "" + totalQty, "", "" +
                            DecimalUtils.round(slPreVatAmt, 2),
                    "" + DecimalUtils.round(slVatAmt, 2), "" +
                            DecimalUtils.round(slExciseAmt, 2), "" +
                            DecimalUtils.round(slNetAmt, 2), "" +
                            DecimalUtils.round(exchangeAmt, 2), BRNo, GRNo, exchangeNum, "" + totalAlQty, "" + totalBaseQty);


            for (int i = 0; i < arrSales.size(); i++) {

                updateVanStockQty(arrSales.get(i));

                //add Invoice Items
                db.insertSalesInvoiceItems(arrSales.get(i), invNum, mCustomer.getCustomerId(), "1");

            }

            //Riddhi change
            //Insert into Sales Summary
          /*  SalesSummary mSummary = new SalesSummary();
            mSummary.setTransactionNo(exchangeNum);
            mSummary.setTransactionType("SI");
            mSummary.setCustomerNo(mCustomer.getCustomerId());
            mSummary.setCustomerName(mCustomer.getCustomerName());
            mSummary.setDiscounts("0");
            mSummary.setTotalSales("" + DecimalUtils.round(saleAmt, 2));
            db.insertSalesSummary(mSummary);*/


            //store Last Invoice Number
            // Settings.setString(App.INVOICE_LAST, invNum);

        }

        String creditNoteNo = "";
        if (!BRNo.equalsIgnoreCase("") && !GRNo.equalsIgnoreCase("")) {
            creditNoteNo = BRNo + "-" + GRNo;
        } else {
            if (!BRNo.equalsIgnoreCase("")) {
                creditNoteNo = BRNo;
            } else {
                creditNoteNo = GRNo;
            }
        }

        //get Print Data
        JSONObject data = null;
        try {
            jsonArray = createOrderPrintData(exchangeNum, creditNoteNo, invNum);
            data = new JSONObject();
            data.put("data", (JSONArray) jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //INSERT TRANSACTION
        Transaction transaction = new Transaction();
        transaction.tr_type = Constant.TRANSACTION_TYPES.TT_EXCHANGE_CREATED;
        transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
        transaction.tr_customer_num = mCustomer.getCustomerId();
        transaction.tr_customer_name = mCustomer.getCustomerName();
        transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
        transaction.tr_invoice_id = "";
        transaction.tr_order_id = exchangeNum;
        transaction.tr_collection_id = "";
        transaction.tr_pyament_id = "";
        transaction.tr_is_posted = "No";
        transaction.tr_printData = data.toString();
        db.insertTransaction(transaction);

        Settings.setString(App.EXCHANGE_LAST, exchangeNum);

    }

    private ArrayList<Item> getReturnItem(String type) {
        ArrayList<Item> arrData = new ArrayList<>();

        double grossAmt = 0;
        double vatAmt = 0;
        double preVatAmt = 0;
        double exciseAmt = 0;
        double netAmt = 0;
        for (int i = 0; i < arrExchange.size(); i++) {
            if (arrExchange.get(i).getReturnType().equalsIgnoreCase(type)) {
                arrData.add(arrExchange.get(i));
                double totPrice = Double.parseDouble(arrExchange.get(i).getPrice());
                grossAmt += totPrice;

                double itemVatAmt = Double.parseDouble(arrExchange.get(i).getVatAmt());
                vatAmt += itemVatAmt;

                double itemPreVatAmt = Double.parseDouble(arrExchange.get(i).getPreVatAmt());
                preVatAmt += itemPreVatAmt;

                double itemExcise = Double.parseDouble(arrExchange.get(i).getExciseAmt());
                exciseAmt += itemExcise;

                double itemNet = Double.parseDouble(arrExchange.get(i).getNetAmt());
                netAmt += itemNet;
            }
        }

        if (type.equalsIgnoreCase("Bad")) {
            brAmt = grossAmt;
            brVatAmt = vatAmt;
            brPreVatAmt = preVatAmt;
            brNetAmt = netAmt;
            brExciseAmt = exciseAmt;
        } else {
            grAmt = grossAmt;
            grVatAmt = vatAmt;
            grPreVatAmt = preVatAmt;
            brNetAmt = netAmt;
            grExciseAmt = exciseAmt;
        }
        return arrData;
    }

    private void insertSalesInvoiceHeader(String invNum, String totalQty, String totalAMt, String preVatAmt,
                                          String vatAmt, String exciseAmt, String netAmt, String exhangAmt, String brNo,
                                          String grNo, String exchangeNum, String altQty, String baseQty) {
        final SalesInvoice salesInvoice = new SalesInvoice();

        salesInvoice.inv_no = invNum;
        salesInvoice.inv_type = "Sale";
        salesInvoice.exchangeNo = exchangeNum;
        salesInvoice.inv_type_code = mCustomer.getCustType();
        salesInvoice.cust_code = mCustomer.getCustomerId();
        salesInvoice.inv_date = UtilApp.getCurrentDate();
        salesInvoice.del_date = UtilApp.getCurrentDate();
        salesInvoice.delivery_no = "";
        salesInvoice.total_qty = totalQty;
        salesInvoice.tot_amnt_sales = totalAMt;
        salesInvoice.pre_VatAmt = preVatAmt;
        salesInvoice.vatAmt = vatAmt;
        salesInvoice.exciseAmt = exciseAmt;
        salesInvoice.netAmt = netAmt;
        salesInvoice.exchangeAmt = exhangAmt;
        salesInvoice.grNo = grNo;
        salesInvoice.brNo = brNo;
        salesInvoice.altQty = altQty;
        salesInvoice.baseQty = baseQty;
        salesInvoice.saletime = UtilApp.getCurrent12Time();
        salesInvoice.latitude = App.Latitude;
        salesInvoice.longitute = App.Longitude;
        salesInvoice.purchaseName = "";
        salesInvoice.purchaseNo = "";

        db.insertSalesInvoice(salesInvoice);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private class getDataConfirm extends AsyncTask<Void, Void, Boolean> {

        private LoadingSpinner mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new LoadingSpinner(ExchangeActivity.this);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            //GENERATE NEXT Exchange NUMBER
            exchangeNum = UtilApp.getLastIndex("Exchange");
            //exchangeNum = "EXCH000001";

            if (arrExchange.size() > 0) {

                arrBadReturn = new ArrayList<>();
                arrBadReturn = getReturnItem("Bad");

                arrGoodReturn = new ArrayList<>();
                arrGoodReturn = getReturnItem("Good");
                System.out.println("chek-->" + arrBadReturn.size() + " Good--> " + arrGoodReturn.size());

                if (arrBadReturn.size() > 0) {

                    //GENERATE NEXT RETURN NUMBER
                    BRNo = UtilApp.getLastIndex("Return");

                    db.insertReturnItems(BRNo, UtilApp.getCurrentDate(), "" +
                                    DecimalUtils.round(brAmt, 2), mCustomer.getCustomerId(), "Bad",
                            Settings.getString(App.SALESMANID), ""
                                    + DecimalUtils.round(brVatAmt, 2), "" +
                                    DecimalUtils.round(brPreVatAmt, 2), "" +
                                    DecimalUtils.round(brExciseAmt, 2),
                            "" + DecimalUtils.round(brNetAmt, 2), arrBadReturn, exchangeNum);


                    Settings.setString(App.RETURN_LAST, BRNo);
                }

                if (arrGoodReturn.size() > 0) {

                    //GENERATE NEXT RETURN NUMBER
                    GRNo = UtilApp.getLastIndex("Return");

                    db.insertReturnItems(GRNo, UtilApp.getCurrentDate(), "" +
                                    DecimalUtils.round(grAmt, 2), mCustomer.getCustomerId(), "Good",
                            Settings.getString(App.SALESMANID), ""
                                    + DecimalUtils.round(grVatAmt, 2), "" +
                                    DecimalUtils.round(grPreVatAmt, 2), "" +
                                    DecimalUtils.round(grExciseAmt, 2),
                            "" + DecimalUtils.round(grNetAmt, 2), arrGoodReturn, exchangeNum);


                    Settings.setString(App.RETURN_LAST, GRNo);
                }

                double bExchAmt = brAmt + grAmt;
                String exchange_total = Settings.getString(App.EXCHANGE_AMONUNT);
                try {
                    if (exchange_total.equals("null")) {
                        exchange_total = "0";
                    }
                } catch (Exception e) {
                    exchange_total = "0";
                }
                double pp = bExchAmt + Double.valueOf(exchange_total);
                System.out.println("exchange-->" + pp);
                try {
                    if (exchange_total.equals("null")) {
                        exchange_total = "0";
                    }
                    double exchange_send = DecimalUtils.round(bExchAmt, 2) + (DecimalUtils.round(Double.valueOf(exchange_total), 2));
                    System.out.println("exchange1-->" + exchange_send);
                    Settings.setString(App.EXCHANGE_AMONUNT, String.valueOf(DecimalUtils.round(exchange_send, 2)));

                } catch (Exception e) {
                    e.toString();
                }

                SalesSummary mSummary = new SalesSummary();
                mSummary.setTransactionNo(exchangeNum);
                mSummary.setTransactionType("Bad");
                mSummary.setCustomerNo(mCustomer.getCustomerId());
                mSummary.setCustomerName(mCustomer.getCustomerName());
                mSummary.setDiscounts("0");
                mSummary.setTotalSales("" + DecimalUtils.round(bExchAmt, 2));
                db.insertSalesSummary(mSummary);

            }

            //Then Take Sales
            if (arrSales.size() > 0) {
                //GENERATE NEXT INVOICE NUMBER
                invNum = UtilApp.getLastIndex("Invoice");

                //INSERT IN SALES INVOICE TABLE
           /* insertSalesInvoiceHeader(invNum, "" + totalQty, "" +
                            DecimalUtils.round(saleAmt, 2), "" +
                            DecimalUtils.round(slPreVatAmt, 2),
                    "" + DecimalUtils.round(slVatAmt, 2), "" +
                            DecimalUtils.round(slExciseAmt, 2), "" +
                            DecimalUtils.round(slNetAmt, 2), "" +
                            DecimalUtils.round(exchangeAmt, 2), BRNo, GRNo, exchangeNum, "" + totalAlQty, "" + totalBaseQty);*/

                insertSalesInvoiceHeader(invNum, "" + totalQty, "", "" +
                                DecimalUtils.round(slPreVatAmt, 2),
                        "" + DecimalUtils.round(slVatAmt, 2), "" +
                                DecimalUtils.round(slExciseAmt, 2), "" +
                                DecimalUtils.round(slNetAmt, 2), "" +
                                DecimalUtils.round(exchangeAmt, 2), BRNo, GRNo, exchangeNum, "" + totalAlQty, "" + totalBaseQty);


                for (int i = 0; i < arrSales.size(); i++) {

                    updateVanStockQty(arrSales.get(i));

                    //add Invoice Items
                    db.insertSalesInvoiceItems(arrSales.get(i), invNum, mCustomer.getCustomerId(), "1");

                }

                //Riddhi change
                //Insert into Sales Summary
          /*  SalesSummary mSummary = new SalesSummary();
            mSummary.setTransactionNo(exchangeNum);
            mSummary.setTransactionType("SI");
            mSummary.setCustomerNo(mCustomer.getCustomerId());
            mSummary.setCustomerName(mCustomer.getCustomerName());
            mSummary.setDiscounts("0");
            mSummary.setTotalSales("" + DecimalUtils.round(saleAmt, 2));
            db.insertSalesSummary(mSummary);*/


                //store Last Invoice Number
                // Settings.setString(App.INVOICE_LAST, invNum);

            }

            String creditNoteNo = "";
            if (!BRNo.equalsIgnoreCase("") && !GRNo.equalsIgnoreCase("")) {
                creditNoteNo = BRNo + "-" + GRNo;
            } else {
                if (!BRNo.equalsIgnoreCase("")) {
                    creditNoteNo = BRNo;
                } else {
                    creditNoteNo = GRNo;
                }
            }

            //get Print Data
            JSONObject data = null;
            try {
                jsonArray = createOrderPrintData(exchangeNum, creditNoteNo, invNum);
                data = new JSONObject();
                data.put("data", (JSONArray) jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //INSERT TRANSACTION
            Transaction transaction = new Transaction();
            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_EXCHANGE_CREATED;
            transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
            transaction.tr_customer_num = mCustomer.getCustomerId();
            transaction.tr_customer_name = mCustomer.getCustomerName();
            transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
            transaction.tr_invoice_id = "";
            transaction.tr_order_id = exchangeNum;
            transaction.tr_collection_id = "";
            transaction.tr_pyament_id = "";
            transaction.tr_is_posted = "No";
            transaction.tr_printData = data.toString();
            db.insertTransaction(transaction);

            Settings.setString(App.EXCHANGE_LAST, exchangeNum);

            //  confirmData();

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (mDialog.isShowing())
                mDialog.hide();

            UtilApp.dialogPrint(ExchangeActivity.this, new OnSearchableDialog() {
                @Override
                public void onItemSelected(Object o) {
                    if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                        UtilApp.createBackgroundJob(getApplicationContext());
                    }
                    String selection = (String) o;
                    if (selection.equalsIgnoreCase("yes")) {
                        PrintLog object = new PrintLog(ExchangeActivity.this,
                                ExchangeActivity.this);
                        object.execute("", jsonArray);
                    } else {
                        finish();
                    }
                }
            });
        }
    }

    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createOrderPrintData(String orderNo, String creditNoteNo, String invNum) {
        JSONArray jArr = new JSONArray();

        try {

            double grossCreditAmt = 0;
            double vatCreditAmt = 0;
            double preVatCreditAmt = 0;
            double totalCreditAmt = 0;
            double grossSaleAmt = 0;
            double vatSaleAmt = 0;
            double preVatSaleAmt = 0;
            double totalSaleAmt = 0;


            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.EXCHANGE);

            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm a"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));
            mainArr.put("exchangeNo", orderNo);  //Exchange No
            mainArr.put("creditNo", creditNoteNo);  //Credit Note No
            mainArr.put("invoiceNumber", invNum);  //Invoice No

            mainArr.put("CUSTOMER", mCustomer.getCustomerName());
            mainArr.put("CUSTOMERID", mCustomer.getCustomerCode());
            mainArr.put("customertype", mCustomer.getCustType());
            mainArr.put("ADDRESS", "" + mCustomer.getAddress());
            mainArr.put("TRN", "");

            JSONArray TOTAL = new JSONArray();

            JSONArray jExchangeData = new JSONArray();
            if (arrBadReturn.size() > 0) {
                for (Item obj : arrBadReturn) {

                    if (Integer.parseInt(obj.getSaleBaseQty()) > 0 && Integer.parseInt(obj.getSaleAltQty()) > 0) {
                        //Add Base UOM
                        JSONArray data = new JSONArray();
                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getBaseUOMName());// UOM
                        data.put(obj.getSaleBaseQty());//  QTY
                        data.put("" + String.format("%.2f", obj.getUOMPrice()));// Unit Price
                        data.put("0");
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price
                        jExchangeData.put(data);

                        grossCreditAmt += Double.parseDouble(obj.getSaleBasePrice());

                        //Add Alter UOM
                        JSONArray dataAl = new JSONArray();
                        dataAl.put(obj.getItemCode()); // ITEM CODE
                        dataAl.put(obj.getItemName());// DESC
                        dataAl.put(obj.getAlterUOMName());// UOM
                        dataAl.put(obj.getSaleAltQty());//  QTY
                        dataAl.put("" + String.format("%.2f", Double.parseDouble(obj.getAlterUOMPrice())));// Unit Price
                        dataAl.put("0");
                        dataAl.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price
                        jExchangeData.put(dataAl);

                        grossCreditAmt += Double.parseDouble(obj.getSaleAltPrice());
                    } else {
                        if (Integer.parseInt(obj.getSaleBaseQty()) > 0) {
                            JSONArray data = new JSONArray();

                            data.put(obj.getItemCode()); // ITEM CODE
                            data.put(obj.getItemName());// DESC
                            data.put(obj.getBaseUOMName());// UOM
                            data.put(obj.getSaleBaseQty());//  QTY
                            data.put("" + String.format("%.2f", Double.parseDouble(obj.getUOMPrice())));// Unit Price
                            data.put("0");
                            data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price

                            jExchangeData.put(data);

                            grossCreditAmt += Double.parseDouble(obj.getSaleBasePrice());

                        } else {
                            JSONArray data = new JSONArray();

                            data.put(obj.getItemCode()); // ITEM CODE
                            data.put(obj.getItemName());// DESC
                            data.put(obj.getAlterUOMName());// UOM
                            data.put(obj.getSaleAltQty());//  QTY
                            data.put("" + String.format("%.2f", Double.parseDouble(obj.getAlterUOMPrice())));// Unit Price
                            data.put("0");
                            data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price

                            jExchangeData.put(data);

                            grossCreditAmt += Double.parseDouble(obj.getSaleAltPrice());
                        }
                    }
                }
            }

            if (arrGoodReturn.size() > 0) {
                for (Item obj : arrGoodReturn) {
                    if (Integer.parseInt(obj.getSaleBaseQty()) > 0 && Integer.parseInt(obj.getSaleAltQty()) > 0) {
                        //Add Base UOM
                        JSONArray data = new JSONArray();
                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getBaseUOMName());// UOM
                        data.put(obj.getSaleBaseQty());//  QTY
                        data.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getUOMPrice())));// Unit Price
                        data.put("0");
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price
                        jExchangeData.put(data);

                        grossCreditAmt += Double.parseDouble(obj.getSaleBasePrice());

                        //Add Alter UOM
                        JSONArray dataAl = new JSONArray();
                        dataAl.put(obj.getItemCode()); // ITEM CODE
                        dataAl.put(obj.getItemName());// DESC
                        dataAl.put(obj.getAlterUOMName());// UOM
                        dataAl.put(obj.getSaleAltQty());//  QTY
                        dataAl.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getAlterUOMPrice())));// Unit Price
                        dataAl.put("0");
                        dataAl.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price
                        jExchangeData.put(dataAl);

                        grossCreditAmt += Double.parseDouble(obj.getSaleAltPrice());
                    } else {
                        if (Integer.parseInt(obj.getSaleBaseQty()) > 0) {
                            JSONArray data = new JSONArray();

                            data.put(obj.getItemCode()); // ITEM CODE
                            data.put(obj.getItemName());// DESC
                            data.put(obj.getBaseUOMName());// UOM
                            data.put(obj.getSaleBaseQty());//  QTY
                            data.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getUOMPrice())));// Unit Price
                            data.put("0");
                            data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price

                            jExchangeData.put(data);

                            grossCreditAmt += Double.parseDouble(obj.getSaleBasePrice());
                        } else {
                            JSONArray data = new JSONArray();

                            data.put(obj.getItemCode()); // ITEM CODE
                            data.put(obj.getItemName());// DESC
                            data.put(obj.getAlterUOMName());// UOM
                            data.put(obj.getSaleAltQty());//  QTY
                            data.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getAlterUOMPrice())));// Unit Price
                            data.put("0");
                            data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price

                            jExchangeData.put(data);
                            grossCreditAmt += Double.parseDouble(obj.getSaleAltPrice());
                        }
                    }
                }
            }

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
                    data.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getUOMPrice())));// Unit Price
                    data.put(obj.getDiscountAmt());
                    data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price
                    jData.put(data);

                    grossSaleAmt += Double.parseDouble(obj.getSaleBasePrice());

                    //Add Alter UOM
                    JSONArray dataAl = new JSONArray();
                    dataAl.put(obj.getItemCode()); // ITEM CODE
                    dataAl.put(obj.getItemName());// DESC
                    dataAl.put(obj.getAlterUOMName());// UOM
                    dataAl.put(obj.getSaleAltQty());//  QTY
                    dataAl.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getAlterUOMPrice())));// Unit Price
                    dataAl.put(obj.getDiscountAmt());
                    dataAl.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price
                    jData.put(dataAl);

                    grossSaleAmt += Double.parseDouble(obj.getSaleAltPrice());

                } else {
                    if (Integer.parseInt(obj.getSaleBaseQty()) > 0) {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getBaseUOMName());// UOM
                        data.put(obj.getSaleBaseQty());//  QTY
                        data.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getUOMPrice())));// Unit Price
                        data.put(obj.getDiscountAmt());
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price

                        jData.put(data);

                        grossSaleAmt += Double.parseDouble(obj.getSaleBasePrice());
                    } else {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getAlterUOMName());// UOM
                        data.put(obj.getSaleAltQty());//  QTY
                        data.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getAlterUOMPrice())));// Unit Price
                        data.put(obj.getDiscountAmt());
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price

                        jData.put(data);

                        grossSaleAmt += Double.parseDouble(obj.getSaleAltPrice());
                    }
                }
            }

            vatCreditAmt = UtilApp.getVat(grossCreditAmt);
            preVatCreditAmt = grossCreditAmt - vatCreditAmt;
            totalCreditAmt = grossCreditAmt;

            vatSaleAmt = UtilApp.getVat(grossSaleAmt);
            preVatSaleAmt = grossSaleAmt - vatSaleAmt;
            totalSaleAmt = grossSaleAmt;


            JSONObject totalObj = new JSONObject();
            totalObj.put("VATCredit", UtilApp.getNumberFormate(vatCreditAmt));
            totalObj.put("PreVatCredit", UtilApp.getNumberFormate(preVatCreditAmt));
            totalObj.put("Sub Total Credit", UtilApp.getNumberFormate(grossCreditAmt));
            totalObj.put("Total Amount Credit", UtilApp.getNumberFormate(totalCreditAmt));
            totalObj.put("VAT", UtilApp.getNumberFormate(vatSaleAmt));
            totalObj.put("NET", UtilApp.getNumberFormate(preVatSaleAmt));
            totalObj.put("Sub Total", UtilApp.getNumberFormate(grossSaleAmt));
            totalObj.put("Invoice Total", UtilApp.getNumberFormate(totalSaleAmt));
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            mainArr.put("data", jData);
            mainArr.put("exchangeData", jExchangeData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jArr;
    }

    public void callback() {
        finish();
    }
}
