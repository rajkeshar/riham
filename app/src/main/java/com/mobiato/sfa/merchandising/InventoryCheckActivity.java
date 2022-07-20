package com.mobiato.sfa.merchandising;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityInventoryCheckBinding;
import com.mobiato.sfa.databinding.DialogExpirySummaryBinding;
import com.mobiato.sfa.databinding.RowInventoryCheckNewBinding;
import com.mobiato.sfa.databinding.RowInventoryExpiryBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InventoryCheckActivity extends BaseActivity {

    private ActivityInventoryCheckBinding binding;
    private InventoryItemAdapter mItemAdapter;
    private DBManager db;
    private Customer customer;
    String dateStr = "";
    public ArrayList<Item> arrItem = new ArrayList<>();
    public ArrayList<Item> arrCSDItem = new ArrayList<>();
    public ArrayList<Item> arrJuiceItem = new ArrayList<>();
    public ArrayList<Item> arrWaterItem = new ArrayList<>();
    public ArrayList<Item> arrBisItem = new ArrayList<>();
    public ArrayList<Item> arrSales = new ArrayList<>();
    public ArrayList<Item> arrItemExpiry = new ArrayList<>();
    private int selectedCat = 0;
    private SearchView searchView;
    String custId = "", inventoryId = "";
    private ArrayList<Item> arrInventoryData = new ArrayList<>();
    private RecyclerView rvExpryList;
    private CommonAdapter<Item> mAdapter1;
    int mDay, mMonth, mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventoryCheckBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Stock in Store");

        db = new DBManager(this);
        custId = getIntent().getStringExtra("id");

        customer = db.getCustomerDetail(custId);
        System.out.println("Customer Details: " + customer);
        inventoryId = db.getCustomerInventoryId(customer.getCustomerId());

        arrInventoryData = db.getCustomerInventory(custId);
        if (arrInventoryData.size() > 0) {
            binding.layoutSubmit.setVisibility(View.GONE);
        }

        arrSales = new ArrayList<>();
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("CSD"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Juice"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Water"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Biscuit"));

        dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    setCategoryItem(arrCSDItem, "CSD");
                } else if (tab.getPosition() == 1) {
                    setCategoryItem(arrJuiceItem, "Juice");
                } else if (tab.getPosition() == 2) {
                    setCategoryItem(arrWaterItem, "Water");
                } else if (tab.getPosition() == 3) {
                    setCategoryItem(arrBisItem, "Biscuit");
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

        binding.layoutSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (arrSales.size() > 0) {
                    makeDilog();
                } else {
                    UtilApp.displayAlert(InventoryCheckActivity.this, "Please insert stock first!");
                }

            }
        });
    }

    private void setItem() {
        arrCSDItem = new ArrayList<>();
        arrJuiceItem = new ArrayList<>();
        arrWaterItem = new ArrayList<>();
        arrBisItem = new ArrayList<>();

        arrCSDItem = db.getInventoryItemListByCategory("2", custId);
        arrJuiceItem = db.getInventoryItemListByCategory("4", custId);
        arrWaterItem = db.getInventoryItemListByCategory("3", custId);
        arrBisItem = db.getInventoryItemListByCategory("1", custId);

        if (arrInventoryData.size() > 0) {
            setInventoryData();
        }
    }

    private void setInventoryData() {
        for (int i = 0; i < arrInventoryData.size(); i++) {

            for (int j = 0; j < arrCSDItem.size(); j++) {
                if (arrInventoryData.get(i).getItemId().equalsIgnoreCase(arrCSDItem.get(j).getItemId())) {
                    arrCSDItem.get(j).setQty(arrInventoryData.get(i).getQty());
                    arrCSDItem.get(j).setExpiryItem(arrInventoryData.get(i).getExpiryItem());
                    break;
                }
            }
        }

        for (int i = 0; i < arrInventoryData.size(); i++) {

            for (int j = 0; j < arrBisItem.size(); j++) {
                if (arrInventoryData.get(i).getItemId().equalsIgnoreCase(arrBisItem.get(j).getItemId())) {
                    arrBisItem.get(j).setQty(arrInventoryData.get(i).getQty());
                    arrBisItem.get(j).setExpiryItem(arrInventoryData.get(i).getExpiryItem());
                    break;
                }
            }
        }

        for (int i = 0; i < arrInventoryData.size(); i++) {

            for (int j = 0; j < arrWaterItem.size(); j++) {
                if (arrInventoryData.get(i).getItemId().equalsIgnoreCase(arrWaterItem.get(j).getItemId())) {
                    arrWaterItem.get(j).setQty(arrInventoryData.get(i).getQty());
                    arrWaterItem.get(j).setExpiryItem(arrInventoryData.get(i).getExpiryItem());
                    break;
                }
            }
        }

        for (int i = 0; i < arrInventoryData.size(); i++) {

            for (int j = 0; j < arrJuiceItem.size(); j++) {
                if (arrInventoryData.get(i).getItemId().equalsIgnoreCase(arrJuiceItem.get(j).getItemId())) {
                    arrJuiceItem.get(j).setQty(arrInventoryData.get(i).getQty());
                    arrJuiceItem.get(j).setExpiryItem(arrInventoryData.get(i).getExpiryItem());
                    break;
                }
            }
        }
    }

    private void setCategoryItem(ArrayList<Item> arrData, String catName) {
        arrItem = new ArrayList<>();
        arrItem = arrData;

        setAdapter();

    }

    private void setAdapter() {

        mItemAdapter = new InventoryItemAdapter(InventoryCheckActivity.this, arrItem);
        binding.rvInventoryList.setAdapter(mItemAdapter);
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


    public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.MyViewHolder>
            implements Filterable {
        private Context context;
        private List<Item> contactList;
        private List<Item> contactListFiltered;
        private LayoutInflater layoutInflater;
        private int mDay, mMonth, mYear;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private final RowInventoryCheckNewBinding binding;

            public MyViewHolder(final RowInventoryCheckNewBinding itemBinding) {
                super(itemBinding.getRoot());
                this.binding = itemBinding;

            }
        }

        public InventoryItemAdapter(Context context, List<Item> contactList) {
            this.context = context;
            this.contactList = contactList;
            this.contactListFiltered = contactList;

        }

        @Override
        public InventoryItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.getContext());
            }
            RowInventoryCheckNewBinding binding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_inventory_check_new, parent, false);
            return new InventoryItemAdapter.MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(InventoryItemAdapter.MyViewHolder holder, final int position) {
            Item mItem = contactListFiltered.get(position);
            holder.binding.setItem(mItem);
            holder.binding.txtTitleId.setText("[" + contactListFiltered.get(position).getItemCode() + "]");
            //holder.binding.edtQuantity.setText(mItem.getQty());

            holder.setIsRecyclable(false);

            holder.binding.btnAddStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowExpiryDate(InventoryCheckActivity.this, contactListFiltered.get(holder.getAdapterPosition()));
                }
            });
            /*holder.binding.edtDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Calendar calendar = Calendar.getInstance();
                    mYear = calendar.get(Calendar.YEAR);
                    mMonth = calendar.get(Calendar.MONTH);
                    mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dpd1 = new DatePickerDialog(InventoryCheckActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    String dayOfMonths = "";
                                    String monthOfYears = "";
                                    if (dayOfMonth < 10) {
                                        dayOfMonths = "0" + String.valueOf(dayOfMonth);
                                    } else {
                                        dayOfMonths = String.valueOf(dayOfMonth);
                                    }
                                    if (monthOfYear < 9) {
                                        monthOfYears = "0" + String.valueOf(monthOfYear + 1);
                                    } else {
                                        monthOfYears = String.valueOf(monthOfYear + 1);
                                    }
                                    String strDob = (dayOfMonths) + "/" + monthOfYears + "/" + year;
                                    holder.binding.edtDate.setText(strDob);
                                    contactListFiltered.get(holder.getAdapterPosition()).setExpiryItem("" + year + "-" + monthOfYears + "-" + dayOfMonths);
                                    getItemExpIndex(mItem.getItemId(), "" + year + "-" + monthOfYears + "-" + dayOfMonths);
                                }
                            }, mYear, mMonth, mDay);
                    dpd1.show();
                }
            });

            holder.binding.edtQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    contactListFiltered.get(holder.getAdapterPosition()).setQty(s.toString());
                    getItemQtyIndex(mItem.getItemId(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
*/
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return contactListFiltered.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        contactListFiltered = contactList;
                    } else {
                        List<Item> filteredList = new ArrayList<>();
                        for (Item row : contactList) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getItemName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        contactListFiltered = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = contactListFiltered;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    contactListFiltered = (ArrayList<Item>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

    }

    public void ShowExpiryDate(Activity activity, Item mItem) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_expiray);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.95);
        dialog.getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout layoutAdd = (LinearLayout) dialog.findViewById(R.id.layoutAdd);
        LinearLayout layoutCancel = (LinearLayout) dialog.findViewById(R.id.layoutCancel);
        LinearLayout layoutShow = (LinearLayout) dialog.findViewById(R.id.layoutShow);
        TextView edtUom = (TextView) dialog.findViewById(R.id.edtUom);
        TextView txtTitleId = (TextView) dialog.findViewById(R.id.txtTitleId);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        EditText edtPC = (EditText) dialog.findViewById(R.id.edtPC);
        TextView edtExpiry = (TextView) dialog.findViewById(R.id.edtExpiry);
        LinearLayout layoutExp = (LinearLayout) dialog.findViewById(R.id.lytExpiry);
        rvExpryList = (RecyclerView) dialog.findViewById(R.id.rvExpiryList);
        LinearLayout layoutShowItem = (LinearLayout) dialog.findViewById(R.id.layoutShowItem);
        FrameLayout frameExpiry = (FrameLayout) dialog.findViewById(R.id.frameExpiry);
        TextView btnAdd_Expry = (TextView) dialog.findViewById(R.id.btnAddExpiry);

        edtUom.setText(mItem.getAlterUOMName());
        txtTitleId.setText(mItem.getItemCode());
        txtTitle.setText(mItem.getItemName());

        btnAdd_Expry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutAdd.setEnabled(true);
                layoutAdd.setAlpha(1f);
                frameExpiry.setVisibility(View.GONE);
                layoutShowItem.setVisibility(View.VISIBLE);

            }
        });

        layoutExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd1 = new DatePickerDialog(InventoryCheckActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String dayOfMonths = "";
                                String monthOfYears = "";
                                if (dayOfMonth < 10) {
                                    dayOfMonths = "0" + String.valueOf(dayOfMonth);
                                } else {
                                    dayOfMonths = String.valueOf(dayOfMonth);
                                }
                                if (monthOfYear < 9) {
                                    monthOfYears = "0" + String.valueOf(monthOfYear + 1);
                                } else {
                                    monthOfYears = String.valueOf(monthOfYear + 1);
                                }
                                String strDob = year + "-" + monthOfYears + "-" + (dayOfMonths);
                                edtExpiry.setText(strDob);
                            }
                        }, mYear, mMonth, mDay);

                dpd1.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dpd1.show();

            }
        });

        layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameExpiry.setVisibility(View.GONE);
                layoutShowItem.setVisibility(View.VISIBLE);
                if (edtPC.getText().toString().equals("")) {
                    Toast.makeText(InventoryCheckActivity.this, "Please insert Qty", Toast.LENGTH_SHORT).show();
                } else {
                    Item addItem = new Item();
                    addItem.setItemId(mItem.getItemId());
                    addItem.setItemName(mItem.getItemName());
                    addItem.setAltrUOM(mItem.getAltrUOM());
                    addItem.setAlterUOMName(mItem.getAlterUOMName());
                    addItem.setQty(edtPC.getText().toString());
                    addItem.setExpiryItem(edtExpiry.getText().toString());
                    addItem.setInventoryId(inventoryId);
                    arrSales.add(addItem);
                    Toast.makeText(InventoryCheckActivity.this, "Stock added successfully!", Toast.LENGTH_SHORT).show();
                    edtPC.setText("");
                    edtExpiry.setText("");
                }
            }
        });

        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        layoutShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutShowItem.setVisibility(View.GONE);
                rvExpryList.setVisibility(View.VISIBLE);
                frameExpiry.setVisibility(View.VISIBLE);
                layoutAdd.setEnabled(false);
                layoutAdd.setAlpha(0.5f);

                arrItemExpiry = new ArrayList<>();
                arrItemExpiry = getItemExpiryStock(mItem.getItemId());
                setMainAdapter1();

            }
        });
        dialog.show();

    }

    private ArrayList<Item> getItemExpiryStock(String itemId) {

        ArrayList<Item> arrExpiry = new ArrayList<>();
        if (arrSales.size() > 0) {

            for (int i = 0; i < arrSales.size(); i++) {
                if (arrSales.get(i).getItemId().equalsIgnoreCase(itemId)) {
                    arrExpiry.add(arrSales.get(i));
                }
            }
        }

        return arrExpiry;
    }

    private void setMainAdapter1() {

        mAdapter1 = new CommonAdapter<Item>(arrItemExpiry) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowInventoryExpiryBinding) {
                    ((RowInventoryExpiryBinding) holder.binding).setItem(arrItemExpiry.get(position));
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_inventory_expiry;
            }
        };

        rvExpryList.setAdapter(mAdapter1);
    }


    private void getItemExpIndex(String itemId, String expDate) {

        switch (selectedCat) {
            case 0:
                for (int i = 0; i < arrCSDItem.size(); i++) {
                    if (arrCSDItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        arrCSDItem.get(i).setExpiryItem(expDate);
                        arrItem.get(i).setExpiryItem(expDate);
                        break;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < arrJuiceItem.size(); i++) {
                    if (arrJuiceItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        arrJuiceItem.get(i).setExpiryItem(expDate);
                        arrItem.get(i).setExpiryItem(expDate);
                        break;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < arrWaterItem.size(); i++) {
                    if (arrWaterItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        arrWaterItem.get(i).setExpiryItem(expDate);
                        arrItem.get(i).setExpiryItem(expDate);
                        break;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < arrBisItem.size(); i++) {
                    if (arrBisItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        arrBisItem.get(i).setExpiryItem(expDate);
                        arrItem.get(i).setExpiryItem(expDate);
                        break;
                    }
                }
                break;
        }
    }

    private void getItemQtyIndex(String itemId, String qty) {

        switch (selectedCat) {
            case 0:
                for (int i = 0; i < arrCSDItem.size(); i++) {
                    if (arrCSDItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        arrCSDItem.get(i).setQty(qty);
                        arrItem.get(i).setQty(qty);
                        break;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < arrJuiceItem.size(); i++) {
                    if (arrJuiceItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        arrJuiceItem.get(i).setQty(qty);
                        arrItem.get(i).setQty(qty);
                        break;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < arrWaterItem.size(); i++) {
                    if (arrWaterItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        arrWaterItem.get(i).setQty(qty);
                        arrItem.get(i).setQty(qty);
                        break;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < arrBisItem.size(); i++) {
                    if (arrBisItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        arrBisItem.get(i).setQty(qty);
                        arrItem.get(i).setQty(qty);
                        break;
                    }
                }
                break;
        }
    }

    private class getInventoryDaya extends AsyncTask<Void, Void, Boolean> {
        private LoadingSpinner mDialog;
        private String cmpNo = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new LoadingSpinner(InventoryCheckActivity.this);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {


            cmpNo = UtilApp.getInventoryNo();

            /*arrSales = new ArrayList<>();
            for (int i = 0; i < arrCSDItem.size(); i++) {
                if (Integer.parseInt(arrCSDItem.get(i).getQty()) > 0) {
                    Item mItem = arrCSDItem.get(i);
                    mItem.setInventoryId(inventoryId);
                    arrSales.add(mItem);
                }
            }

            for (int i = 0; i < arrBisItem.size(); i++) {
                if (Integer.parseInt(arrBisItem.get(i).getQty()) > 0) {
                    Item mItem = arrBisItem.get(i);
                    mItem.setInventoryId(inventoryId);
                    arrSales.add(mItem);
                }
            }

            for (int i = 0; i < arrJuiceItem.size(); i++) {
                if (Integer.parseInt(arrJuiceItem.get(i).getQty()) > 0) {
                    Item mItem = arrJuiceItem.get(i);
                    mItem.setInventoryId(inventoryId);
                    arrSales.add(mItem);
                }
            }

            for (int i = 0; i < arrWaterItem.size(); i++) {
                if (Integer.parseInt(arrWaterItem.get(i).getQty()) > 0) {
                    Item mItem = arrWaterItem.get(i);
                    mItem.setInventoryId(inventoryId);
                    arrSales.add(mItem);
                }
            }*/

            db.insertInventoryHeader(inventoryId, custId, cmpNo);
            db.updateInventoryExpiry(arrSales, custId, dateStr);

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (mDialog.isShowing())
                mDialog.hide();

            if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                UtilApp.createBackgroundJob(getApplicationContext());
            }

            //INSERT TRANSACTION
            Transaction transaction = new Transaction();
            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_INVENTORY;
            transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
            transaction.tr_customer_num = custId;
            transaction.tr_customer_name = "";
            transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
            transaction.tr_invoice_id = "";
            transaction.tr_order_id = cmpNo;
            transaction.tr_collection_id = "";
            transaction.tr_pyament_id = "";
            transaction.tr_printData = "";
            transaction.tr_is_posted = "No";

            db.insertTransaction(transaction);

            finish();
        }
    }

    private void makeDilog() {

        LayoutInflater factory = LayoutInflater.from(this);
        DialogExpirySummaryBinding binding = DataBindingUtil.inflate(factory, R.layout.dialog_expiry_summary, null, false);

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


            paramItem.weight = 1.5f;
            itemTV.setLayoutParams(paramItem);

            paramQty.weight = 0.5f;
            qtyTV.setLayoutParams(paramQty);

            paramPrice.weight = 1.0f;
            priceTV.setLayoutParams(paramPrice);

            itemTV.setText(arrSales.get(i).getItemName());
            qtyTV.setText(arrSales.get(i).getQty());
            priceTV.setText(arrSales.get(i).getExpiryItem());

            tableRow.addView(itemTV);
            tableRow.addView(qtyTV);
            tableRow.addView(priceTV);

            binding.tableLayout.addView(tableRow);

        }

        deleteDialog.show();

        binding.txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        binding.txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilApp.confirmationDialog("Are you sure you want to proceed?", InventoryCheckActivity.this, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        String selection = (String) o;
                        if (selection.equalsIgnoreCase("yes")) {

                            new getInventoryDaya().execute();

                            deleteDialog.dismiss();
                        }
                    }
                });

            }
        });
    }

}
