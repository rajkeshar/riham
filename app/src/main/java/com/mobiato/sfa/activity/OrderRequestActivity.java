package com.mobiato.sfa.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.Adapter.ItemAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityOrderRequestBinding;
import com.mobiato.sfa.databinding.DialogReceiptSummaryBinding;
import com.mobiato.sfa.databinding.OrderCommentDialogBinding;
import com.mobiato.sfa.databinding.RowItemCategoryBinding;
import com.mobiato.sfa.databinding.RowItemSaleBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Category;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.DepotData;
import com.mobiato.sfa.model.Discount;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.PromoOfferData;
import com.mobiato.sfa.model.SalesSummary;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.DecimalUtils;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

public class OrderRequestActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public ActivityOrderRequestBinding binding;
    public ArrayList<Item> arrItem = new ArrayList<>();
    public ArrayList<Item> arrCSDItem = new ArrayList<>();
    public ArrayList<Item> arrJuiceItem = new ArrayList<>();
    public ArrayList<Item> arrWaterItem = new ArrayList<>();
    public ArrayList<Item> arrBisItem = new ArrayList<>();
    public ArrayList<Item> arrHamperItem = new ArrayList<>();
    public ArrayList<Item> arrSales = new ArrayList<>();
    public ArrayList<Item> arrConfectionaryItem = new ArrayList<>();
    public static ArrayList<Item> arrFOCItem = new ArrayList<>();
    public ArrayList<Category> arrCategory = new ArrayList<>();
    private CommonAdapter<Category> mCategoryAdapter;
    private CommonAdapter<Item> mSelectAdapter;
    private ItemAdapter mItemAdapter;
    int catPosition = 0, itemPosition = 0, selectCatId = 0;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    int select = 1;
    private String type = "", customerCode = "", routeId = "", returnType = "", customerName = "", agentId = "",
            orderComment = "", appType = "Salesman";
    private boolean isReturn = false;
    private DBManager db;
    private JSONArray jsonArray;
    private double btnTotVal = 0, preVatAmt = 0, vatAmt = 0, exciseAmt = 0, netAmt = 0, agentexciseAmt = 0, directexciseAmt = 0;
    private Customer mCustomer;
    private boolean isDetailView = false, isDigitSign = false, isCatDisApply = false;
    public static String strDigitSign = "";
    public String[] arrDepot;
    public ArrayList<DepotData> arrDepotData = new ArrayList<>();

    public ArrayList<PromoOfferData> arrPromo = new ArrayList<>();
    public ArrayList<PromoOfferData> arrApplyPromo = new ArrayList<>();
    public boolean isCheckPromo = false;
    public boolean isCustomerPromo = false;
    public boolean isCustomerPromoExclude = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderRequestBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);

        db = new DBManager(OrderRequestActivity.this);

        registerReceiver(broadcastReceiver, new IntentFilter(InputDialogOrderActivity.BROADCAST_ACTION));
        registerReceiver(broadcastReceiverRR, new IntentFilter(InputDialogReturnActivity.BROADCAST_ACTION));

        type = getIntent().getStringExtra("Type");
        customerCode = getIntent().getStringExtra("cust_code");
        if (type.equalsIgnoreCase("Order")) {
            appType = getIntent().getStringExtra("AppType");
        }
        if (db.checkIsCustomerExist(customerCode)) {
            mCustomer = db.getCustomerDetail(customerCode);
        } else {
            mCustomer = db.getDepotCustomerDetail(customerCode);
        }
        // mCustomer = db.getCustomerDetail(customerCode);
        routeId = mCustomer.getRouteId();
        arrDepotData = db.getAllDepot();

        if (arrDepotData.size() > 0) {
            arrDepot = new String[arrDepotData.size()];
            for (int i = 0; i < arrDepotData.size(); i++) {
                arrDepot[i] = arrDepotData.get(i).getDepotName();
            }
        }

        if (type.equalsIgnoreCase("Return")) {
            returnType = getIntent().getStringExtra("ReturnType");
            isReturn = true;
            setTitle("All Items");
            isCheckPromo = false;
        } else {
            isReturn = false;

            Date c = Calendar.getInstance().getTime();
            //  System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c);
            setTitle(formattedDate);

            isCheckPromo = true;
            if (!type.equalsIgnoreCase("Load")) {
                isCustomerPromo = db.isCustomerSpecificPromo(mCustomer.getCustomerId());
                if (isCustomerPromo) {
                    arrPromo = db.getAllCustomerPromotion();
                } else {
                    isCustomerPromoExclude = db.isCustomerRoutePromoExclude(mCustomer.getCustomerId());
                    if (!isCustomerPromoExclude) {
                        arrPromo = db.getAllPromotion();
                    }
                }
            } else {
                arrPromo = db.getAllAgentPromotion();
            }
        }

        if (!type.equalsIgnoreCase("Load")) {
            customerName = db.getCustomerName(customerCode);
            binding.lytDepot.setVisibility(View.GONE);
        } else {
            customerCode = Settings.getString(App.AGENTID);
            binding.lytDepot.setVisibility(View.VISIBLE);
        }

        setCategory();
        setCategoryAdapter();

        selectCatId = 2;
        arrSales = new ArrayList<>();

        setItem();
        setCategoryItem(arrCSDItem, "CSD");

        binding.fab.setOnClickListener(this);
        binding.rlCheckout.setOnClickListener(this);
        binding.btnDigit.setOnClickListener(this);
        binding.lytDepot.setOnClickListener(this);

        baseBinding.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isReturn) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            OrderRequestActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setMinDate(now);
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }
            }
        });

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
                            selectCatId = Integer.parseInt(arrCategory.get(position).catId);

                            switch (arrCategory.get(position).catId) {
                                case "1":
                                    setCategoryItem(arrBisItem, "Biscuit");
                                    break;
                                case "2":
                                    setCategoryItem(arrCSDItem, "CSD");
                                    break;
                                case "3":
                                    setCategoryItem(arrWaterItem, "Water");
                                    break;
                                case "4":
                                    setCategoryItem(arrJuiceItem, "Juice");
                                    break;
                                case "5":
                                    setCategoryItem(arrHamperItem, "Hamper");
                                    break;
                                case "6":
                                    setCategoryItem(arrConfectionaryItem, "Confectionary");
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


    private void setItem() {
        arrCSDItem = new ArrayList<>();
        arrJuiceItem = new ArrayList<>();
        arrWaterItem = new ArrayList<>();
        arrBisItem = new ArrayList<>();
        arrHamperItem = new ArrayList<>();
        arrConfectionaryItem = new ArrayList<>();

        if (appType.equalsIgnoreCase("Merchandiser")) {
            arrCSDItem = db.getInventoryItemListByCategory("2", mCustomer.getCustomerId());
            Collections.sort(arrCSDItem, new Comparator<Item>() {
                public int compare(Item obj1, Item obj2) {
                    // ## Ascending order
                    return obj1.getItemName().compareToIgnoreCase(obj2.getItemName()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
            arrJuiceItem = db.getInventoryItemListByCategory("4", mCustomer.getCustomerId());
            Collections.sort(arrJuiceItem, new Comparator<Item>() {
                public int compare(Item obj1, Item obj2) {
                    // ## Ascending order
                    return obj1.getItemName().compareToIgnoreCase(obj2.getItemName()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
            arrWaterItem = db.getInventoryItemListByCategory("3", mCustomer.getCustomerId());
            Collections.sort(arrWaterItem, new Comparator<Item>() {
                public int compare(Item obj1, Item obj2) {
                    // ## Ascending order
                    return obj1.getItemName().compareToIgnoreCase(obj2.getItemName()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
            arrBisItem = db.getInventoryItemListByCategory("1", mCustomer.getCustomerId());
            Collections.sort(arrBisItem, new Comparator<Item>() {
                public int compare(Item obj1, Item obj2) {
                    // ## Ascending order
                    return obj1.getItemName().compareToIgnoreCase(obj2.getItemName()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
            arrHamperItem = db.getInventoryItemListByCategory("5", mCustomer.getCustomerId());
            Collections.sort(arrHamperItem, new Comparator<Item>() {
                public int compare(Item obj1, Item obj2) {
                    // ## Ascending order
                    return obj1.getItemName().compareToIgnoreCase(obj2.getItemName()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
            arrConfectionaryItem = db.getInventoryItemListByCategory("6", mCustomer.getCustomerId());
            Collections.sort(arrConfectionaryItem, new Comparator<Item>() {
                public int compare(Item obj1, Item obj2) {
                    // ## Ascending order
                    return obj1.getItemName().compareToIgnoreCase(obj2.getItemName()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
        } else {
            arrCSDItem = db.getItemListByCategory("2");
            arrJuiceItem = db.getItemListByCategory("4");
            arrWaterItem = db.getItemListByCategory("3");
            arrBisItem = db.getItemListByCategory("1");
            arrHamperItem = db.getItemListByCategory("5");
            arrConfectionaryItem = db.getItemListByCategory("6");
        }

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
                mItemAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    private void setAdapter() {

        mItemAdapter = new ItemAdapter(OrderRequestActivity.this, arrItem, type, customerCode, routeId, new ItemAdapter.ItemsAdapterListener() {
            @Override
            public void onItemSelected(Item contact, int position) {
                if (type.equalsIgnoreCase("Load")) {

                    if (agentId.equalsIgnoreCase("")) {
                        UtilApp.displayAlert(OrderRequestActivity.this, "Please select depo!");
                        return;
                    }
                }
                itemPosition = getItemIndex(contact.getItemId());
                //riddhi
                System.out.println("RIDDHI");

                if (type.equals("Return")) {
                    Intent i = new Intent(OrderRequestActivity.this, InputDialogReturnActivity.class);
                    i.putExtra("type", type);
                    i.putExtra("apptype", appType);
                    i.putExtra("returnType", returnType);
                    i.putExtra("agentexciseAmt", agentexciseAmt);
                    if (type.equalsIgnoreCase("Load")) {
                        i.putExtra("customer", agentId);
                    } else {
                        i.putExtra("customer", mCustomer.getCustomerId());
                    }

                    i.putExtra("item", contact);
                    startActivity(i);
                } else {
                    Intent i = new Intent(OrderRequestActivity.this, InputDialogOrderActivity.class);
                    i.putExtra("type", type);
                    i.putExtra("apptype", appType);
                    i.putExtra("returnType", returnType);
                    i.putExtra("agentexciseAmt", agentexciseAmt);
                    if (type.equalsIgnoreCase("Load")) {
                        i.putExtra("customer", agentId);
                    } else {
                        i.putExtra("customer", mCustomer.getCustomerId());
                    }

                    i.putExtra("item", contact);
                    startActivity(i);
                }

            }
        });

        binding.rvItemList.setAdapter(mItemAdapter);

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
                    if (type.equalsIgnoreCase("Return")) {
                        itemBasePrice = db.getItemPrice(mItem.getItemId());
                        itemAlterPrice = db.getItemAlterPrice(mItem.getItemId());
                    } else {
                        if (db.isPricingItems(customerCode, mItem.getItemId())) {
                            Item pItem = db.getCustPricingItems(customerCode, mItem.getItemId());
                            itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
                            itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                        } else {
                            itemBasePrice = db.getItemPrice(mItem.getItemId());
                            itemAlterPrice = db.getItemAlterPrice(mItem.getItemId());
                        }
                    }

                    ((RowItemSaleBinding) holder.binding).lytUnitPrice.setVisibility(View.VISIBLE);
                    ((RowItemSaleBinding) holder.binding).tvUnitAlterPrice.setText("Unit Price: " + UtilApp.getNumberFormate((itemAlterPrice - Double.parseDouble(mItem.getDiscountAlAmt()))) + " UGX");
                    ((RowItemSaleBinding) holder.binding).dividerUnitPrice.setVisibility(View.VISIBLE);
                    ((RowItemSaleBinding) holder.binding).tvUnitBaseUOMPrice.setVisibility(View.VISIBLE);
                    ((RowItemSaleBinding) holder.binding).tvUnitBaseUOMPrice.setText(UtilApp.getNumberFormate((itemBasePrice - Double.parseDouble(mItem.getDiscountAmt()))) + " UGX");


                    if (mItem.getSaleAltPrice() != null) {
                        ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
                        ((RowItemSaleBinding) holder.binding).tvAlterPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleAltPrice())) + " UGX");
                        if (mItem.getSaleBasePrice() != null) {
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText(UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleBasePrice())) + " UGX");
                        } else {
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.GONE);
                        }
                    } else {
                        if (mItem.getSaleBasePrice() != null) {
                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvAlterPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleBasePrice())) + " UGX");
                        } else {
                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.GONE);
                        }
                    }

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemPosition = position;
                            Intent i = new Intent(OrderRequestActivity.this, InputDialogOrderActivity.class);
                            i.putExtra("type", type);
                            i.putExtra("returnType", returnType);
                            if (type.equalsIgnoreCase("Load")) {
                                i.putExtra("customer", Settings.getString(App.AGENTID));
                            } else {
                                i.putExtra("customer", mCustomer.getCustomerId());
                            }

                            i.putExtra("item", arrItem.get(position));
                            startActivity(i);
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

    private void setSelectedItemAdapter() {
        ArrayList<Item> arrItem = new ArrayList<>();
        arrItem.addAll(arrSales);
        if (arrFOCItem.size() > 0) {
            arrItem.addAll(arrFOCItem);
        }
        mSelectAdapter = new CommonAdapter<Item>(arrItem) {
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

                    if (mItem.getSaleAltPrice() != null) {
                        ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
                        ((RowItemSaleBinding) holder.binding).tvAlterPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleAltPrice())) + " UGX");
                        if (mItem.getSaleBasePrice() != null) {
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText(UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleBasePrice())) + " UGX");
                        } else {
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.GONE);
                        }
                    } else {
                        if (mItem.getSaleBasePrice() != null) {
                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvAlterPrice.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleBasePrice())) + " UGX");
                        } else {
                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.GONE);
                        }

                    }

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_sale;
            }
        };

        binding.rvAllItem.setAdapter(mSelectAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (arrSales.size() > 0) {
                    if (type.equalsIgnoreCase("Load")) {
                        if (agentId.equalsIgnoreCase("")) {
                            UtilApp.displayAlert(OrderRequestActivity.this, "Please select depo!");
                            return;
                        }
                    }
                    binding.lytMainView.setVisibility(View.GONE);
                    binding.lytItemView.setVisibility(View.VISIBLE);

                    vatAmt = 0;
                    preVatAmt = 0;
                    exciseAmt = 0;
                    agentexciseAmt = 0;
                    directexciseAmt = 0;
                    netAmt = 0;
                    btnTotVal = 0;
                    isDetailView = true;
                    fillButtonPrice();
                    setSelectedItemAdapter();

                    if (type.equalsIgnoreCase("Order")) {
                        if (mCustomer.getCustomerType().equalsIgnoreCase("4")) {
                            binding.btnDigit.setVisibility(View.VISIBLE);
                            isDigitSign = true;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select items", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_checkout:
                if (isDigitSign) {
                    if (strDigitSign.equalsIgnoreCase("")) {
                        UtilApp.displayAlert(me, "Order signature is missing!\nPlease make sure an authorized personnel signs your order before proceeding.");
                    } else {
                        if (isCheckPromo) {
                            arrFOCItem.clear();
                            arrFOCItem = new ArrayList<>();
                            if (isPromoApplay()) {
                                showApplyPromoDialog(0);
                            } else {
                                makeDilog();
                            }
                        } else {
                            makeDilog();
                        }
                    }
                } else {
                    if (isCheckPromo) {
                        arrFOCItem.clear();
                        arrFOCItem = new ArrayList<>();
                        if (isPromoApplay()) {
                            showApplyPromoDialog(0);
                        } else {
                            makeDilog();
                        }
                    } else {
                        makeDilog();
                    }
                }

                break;
            case R.id.btnDigit:
                startActivity(new Intent(me, DigitSignatureActivity.class));
                break;
            case R.id.lytDepot:
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderRequestActivity.this);
                builder.setTitle("Select Agent");
                builder.setItems(arrDepot, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etDepot.setText(arrDepot[which]);
                        agentId = arrDepotData.get(which).getAgentId();
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            default:
                break;
        }
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
            if (!type.equalsIgnoreCase("Load")) {
                if (isCustomerPromo) {
                    isPromoApply = db.isSlabCustomerPromo(String.valueOf(mTotalQty), arrPromo.get(i).promotionId);
                } else {
                    isPromoApply = db.isSlabPromo(String.valueOf(mTotalQty), arrPromo.get(i).promotionId);
                }
            } else {
                isPromoApply = db.isSlabAgentPromo(String.valueOf(mTotalQty), arrPromo.get(i).promotionId);
            }

            if (isPromoApply) {
                String offerQTy = "0";
                if (!type.equalsIgnoreCase("Load")) {
                    if (isCustomerPromo) {
                        offerQTy = db.getSlabCustomerPromoOfferQty(String.valueOf(mTotalQty), arrPromo.get(i).promotionId);
                    } else {
                        offerQTy = db.getSlabPromoOfferQty(String.valueOf(mTotalQty), arrPromo.get(i).promotionId);
                    }
                } else {
                    offerQTy = db.getSlabAgentPromoOfferQty(String.valueOf(mTotalQty), arrPromo.get(i).promotionId);
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderRequestActivity.this);

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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        baseBinding.toolbarTitle.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
    }

    private void fillButtonPrice() {

        for (int i = 0; i < arrSales.size(); i++) {
            Salesman mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
            // System.out.println("p0-->"+arrSales.get(i).getAgentExcise()+"\n "+arrSales.size());

            double totAmt = Double.parseDouble(arrSales.get(i).getPrice());

            vatAmt += Double.parseDouble(arrSales.get(i).getVatAmt());
            preVatAmt += Double.parseDouble(arrSales.get(i).getPreVatAmt());
            exciseAmt += Double.parseDouble(arrSales.get(i).getExciseAmt());
            netAmt += Double.parseDouble(arrSales.get(i).getNetAmt());

           /* if(UtilApp.salesmanRole(mSalesman.getRole()).equals("Hariss salesman")){
                if(select==5) {
                   // try {
                    if(!arrSales.get(i).getAgentExcise().equals("")) {
                        double in = ((Double.parseDouble(arrSales.get(i).getPrice()) * Double.parseDouble(arrSales.get(i).getAgentExcise()) / 100));
                        exciseAmt += (in);
                        agentexciseAmt += (in);
                        System.out.println("ex-->"+exciseAmt);
                    }else {
                        exciseAmt += Double.parseDouble(arrSales.get(i).getExciseAmt());
                    }

                        //arrSales.get(i).setExciseAmt("" + Math.round(exciseAmt));
                    *//*}catch (Exception e){
                        System.out.println("C0-->"+arrSales.get(i).getPrice());
                    }*//*
                }else {
                    exciseAmt += Double.parseDouble(arrSales.get(i).getExciseAmt());
                }
            }else if(UtilApp.salesmanRole(mSalesman.getRole()).equals("Merchandiser")) {
                if (select == 5) {
                    try {
                        if(!arrSales.get(i).getDirectsellexcise().equals("")) {
                            double in = ((Double.parseDouble(arrSales.get(i).getPrice()) * Double.parseDouble(arrSales.get(i).getDirectsellexcise()) / 100));
                            exciseAmt += (in);
                            directexciseAmt += (in);
                            arrSales.get(i).setExciseAmt("" + Math.round(exciseAmt));
                        }else {
                            exciseAmt += Double.parseDouble(arrSales.get(i).getExciseAmt());
                        }
                    }catch (Exception e){

                    }
                }else {
                    exciseAmt += Double.parseDouble(arrSales.get(i).getExciseAmt());
                }
            } else {
                exciseAmt += Double.parseDouble(arrSales.get(i).getExciseAmt());
            }*/

            btnTotVal += totAmt;
        }

        binding.txtTot.setText(UtilApp.getNumberFormate(Math.round(btnTotVal)) + " UGX");
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            Item item = (Item) intent.getSerializableExtra("item");

            if (arrSales.size() == 0) {
                if (Integer.parseInt(item.getSaleBaseQty()) > 0 || Integer.parseInt(item.getSaleAltQty()) > 0)
                    arrSales.add(item);
            } else {

                boolean flag = false;
                for (int i = 0; i < arrSales.size(); i++) {
                    //System.out.println("check-->"+arrSales.get(i).getItemId()+" Equals--> "+item.getItemId());
                    if (arrSales.get(i).getItemId().equalsIgnoreCase(item.getItemId())) {
                        flag = true;
                        if (Integer.parseInt(item.getSaleBaseQty()) > 0 || Integer.parseInt(item.getSaleAltQty()) > 0) {
                            arrSales.set(i, item);
                        } else {
                            arrSales.remove(i);
                        }
                        break;
                    }
                }

                if (!flag) {
                    if (arrSales.size() > 69) {
                        UtilApp.displayAlert(OrderRequestActivity.this, "You can order 70 items at the time!");
                        return;
                    } else {
                        arrSales.add(item);
                    }
                }
            }

            if (type.equalsIgnoreCase("Order") || type.equalsIgnoreCase("Load")) {
                String custId = "";

                if (type.equalsIgnoreCase("Load")) {
                    custId = agentId;
                } else {
                    custId = mCustomer.getCustomerId();
                }

                if (type.equalsIgnoreCase("Load")) {
                    updateItem(item);
                } else {
                    updateItem(item);
                }

            } else {
                updateItem(item);
            }
        }

    };

    private BroadcastReceiver broadcastReceiverRR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            Item item = (Item) intent.getSerializableExtra("item");

            if (arrSales.size() == 0) {
                if (Integer.parseInt(item.getSaleBaseQty()) > 0 || Integer.parseInt(item.getSaleAltQty()) > 0)
                    arrSales.add(item);
            } else {

                boolean flag = false;
                for (int i = 0; i < arrSales.size(); i++) {
                    //System.out.println("check-->"+arrSales.get(i).getItemId()+" Equals--> "+item.getItemId());
                    if (arrSales.get(i).getItemId().equalsIgnoreCase(item.getItemId())) {
                        flag = true;
                        if (Integer.parseInt(item.getSaleBaseQty()) > 0 || Integer.parseInt(item.getSaleAltQty()) > 0) {
                            arrSales.set(i, item);
                        } else {
                            arrSales.remove(i);
                        }
                        break;
                    }
                }

                if (!flag) {
                    if (arrSales.size() > 69) {
                        UtilApp.displayAlert(OrderRequestActivity.this, "You can order 70 items at the time!");
                        return;
                    } else {
                        arrSales.add(item);
                    }
                }
            }

            updateItem(item);
        }

    };

    public void applyItemDiscount(Item item) {

        Discount mDiscount = null;
       // mDiscount = db.getItemSlabDiscount(item.getSaleAltQty(), item.getItemId());

        double itemAlterPrice = Double.parseDouble(item.getAlterUOMPrice());
        double value = 0;
        if (mDiscount.getDiscountType().equalsIgnoreCase("1")) {
            value = ((itemAlterPrice * Double.parseDouble(mDiscount.getDiscount())) / 100);
        } else {
            value = Double.parseDouble(mDiscount.getDiscount());
        }

        itemAlterPrice = itemAlterPrice - value;
        double discountAl = Double.parseDouble(mDiscount.getDiscount());
        double discountAlAmt = value;
        double alterSellPrice = 0;
        alterSellPrice = Integer.parseInt(item.getSaleAltQty()) * itemAlterPrice;
        double finalPrice = alterSellPrice + Double.parseDouble(item.getSaleBasePrice());

        //set Data for Discount
        item.setPrice("" + finalPrice);
        item.setDiscountAlAmt("" + DecimalUtils.round(discountAlAmt, 2));
        item.setDiscountAlPer("" + discountAl);
        item.setAlterUOMPrice("" + (itemAlterPrice + discountAlAmt));
        item.setSaleAltPrice("" + alterSellPrice);

        //update Item after discount
        updateItem(item);

    }

    public void applyCategoryDiscount(Item item, String isRoute, String custId) {
        Discount mDiscount = null;
        ArrayList<String> arrCategoryId = new ArrayList<>();
//        if (isRoute.equalsIgnoreCase("true")) {
//            mDiscount = db.getRouteCategoryDiscount();
//        } else {
//            mDiscount = db.getCategoryDiscount(custId);
//        }

        //Get Discount Category
        String categoryId = mDiscount.getCategoryId();
        String[] spCat = categoryId.split(",");
        for (int k = 0; k < spCat.length; k++) {
            arrCategoryId.add(spCat[k]);
        }

        int categoryQTy = 0;
        for (int i = 0; i < arrSales.size(); i++) {
            if (arrCategoryId.contains(arrSales.get(i).getCategory())) {
                if (arrSales.get(i).getIsFOCItem() != null) {
                    if (!arrSales.get(i).getIsFOCItem().equals("yes")) {
                        if (arrSales.get(i).getIsFreeItem().equals("0")) {
                            categoryQTy += Integer.parseInt(arrSales.get(i).getSaleAltQty());
                        }
                    }
                } else {
                    categoryQTy += Integer.parseInt(arrSales.get(i).getSaleAltQty());
                }
            }
        }

        if (mDiscount != null) {
            if (categoryQTy >= Integer.parseInt(mDiscount.getQty())) {

                for (int j = 0; j < arrSales.size(); j++) {

                    if (!arrSales.get(j).getIsFOCItem().equals("yes")) {
                        if (arrCategoryId.contains(arrSales.get(j).getCategory())) {
                            double itemAlterPrice = Double.parseDouble(arrSales.get(j).getAlterUOMPrice());
                            double value = 0;
                            if (mDiscount.getType().equalsIgnoreCase("1")) {
                                value = ((itemAlterPrice * Double.parseDouble(mDiscount.getDiscount())) / 100);
                            } else {
                                value = Double.parseDouble(mDiscount.getDiscount());
                            }

                            itemAlterPrice = itemAlterPrice - value;
                            double discountAl = Double.parseDouble(mDiscount.getDiscount());
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

                            //update Item after discount
                            updateItem(arrSales.get(j));
                        }
                    }
                }

                isCatDisApply = true;
                //Update Invoice Price
                //fillButtonPrice();

            } else {

                if (isCatDisApply) {
                    isCatDisApply = false;

                    for (int j = 0; j < arrSales.size(); j++) {

                        if (!arrSales.get(j).getIsFOCItem().equals("yes")) {
                            if (arrCategoryId.contains(arrSales.get(j).getCategory())) {
                                double itemAlterPrice = 0;

                                if (db.isPricingItems(custId, arrSales.get(j).getItemId())) {
                                    Item pItem = db.getCustPricingItems(custId, arrSales.get(j).getItemId());
                                    itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                                } else {
                                    itemAlterPrice = db.getItemAlterPrice(arrSales.get(j).getItemId());
                                }

                                double alterSellPrice = 0;
                                alterSellPrice = Integer.parseInt(arrSales.get(j).getSaleAltQty()) * itemAlterPrice;
                                double finalPrice = alterSellPrice + Double.parseDouble(arrSales.get(j).getSaleBasePrice());

                                //set Data for Discount
                                arrSales.get(j).setPrice("" + finalPrice);
                                arrSales.get(j).setDiscountAlAmt("0");
                                arrSales.get(j).setDiscountAlPer("0");
                                arrSales.get(j).setAlterUOMPrice("" + (itemAlterPrice));
                                arrSales.get(j).setSaleAltPrice("" + alterSellPrice);

                                //update Item after discount
                                updateItem(arrSales.get(j));
                            }
                        }
                    }
                }

                //Update Invoice Price
                updateItem(item);
                // fillButtonPrice();
            }
        } else {
            //Update Invoice Price
            updateItem(item);
            //fillButtonPrice();
        }
    }

    private int getItemIndex(String itemId) {
        int itemPosition = 0;

        switch (Integer.parseInt(arrCategory.get(catPosition).catId)) {
            case 2:
                for (int i = 0; i < arrCSDItem.size(); i++) {
                    if (arrCSDItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        select = 2;
                        return itemPosition;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < arrWaterItem.size(); i++) {
                    if (arrWaterItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        select = 3;
                        return itemPosition;
                    }
                }
                break;
            case 4:
                for (int i = 0; i < arrJuiceItem.size(); i++) {
                    if (arrJuiceItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        select = 4;
                        return itemPosition;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < arrBisItem.size(); i++) {
                    if (arrBisItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        select = 1;
                        return itemPosition;
                    }
                }
                break;
            case 5:
                for (int i = 0; i < arrHamperItem.size(); i++) {
                    if (arrHamperItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        select = 5;
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 6:
                for (int i = 0; i < arrConfectionaryItem.size(); i++) {
                    if (arrConfectionaryItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        select = 6;
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
        }
        return itemPosition;
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void makeDilog() {

        LayoutInflater factory = LayoutInflater.from(this);
        DialogReceiptSummaryBinding binding = DataBindingUtil.inflate(factory, R.layout.dialog_receipt_summary, null, false);

        final View deleteDialogView = binding.getRoot();

        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        if (type.equalsIgnoreCase("Return")) {
            binding.tvSummary.setText("Return Summary");
        } else if (type.equalsIgnoreCase("Load")) {
            binding.tvSummary.setText("Load Summary");
        } else {
            binding.tvSummary.setText("Order Summary");
        }

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

            if (Integer.parseInt(arrSales.get(i).getSaleAltQty()) > 0 && Integer.parseInt(arrSales.get(i).getSaleBaseQty()) > 0) {
                qtyTV.setText(arrSales.get(i).getSaleAltQty() + "/" + arrSales.get(i).getSaleBaseQty());
            } else {
                if (Integer.parseInt(arrSales.get(i).getSaleAltQty()) > 0) {
                    qtyTV.setText(arrSales.get(i).getSaleAltQty());
                } else {
                    qtyTV.setText(arrSales.get(i).getSaleBaseQty());
                }
            }

            itemTV.setText(arrSales.get(i).getItemName());
            priceTV.setText(UtilApp.getNumberFormate(Double.parseDouble(arrSales.get(i).getPrice())));

            tableRow.addView(itemTV);
            tableRow.addView(qtyTV);
            tableRow.addView(priceTV);

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

                if (Integer.parseInt(arrFOCItem.get(i).getSaleAltQty()) > 0 && Integer.parseInt(arrFOCItem.get(i).getSaleBaseQty()) > 0) {
                    qtyTV.setText(arrFOCItem.get(i).getSaleAltQty() + "/" + arrFOCItem.get(i).getSaleBaseQty());
                } else {
                    if (Integer.parseInt(arrFOCItem.get(i).getSaleAltQty()) > 0) {
                        qtyTV.setText(arrFOCItem.get(i).getSaleAltQty());
                    } else {
                        qtyTV.setText(arrFOCItem.get(i).getSaleBaseQty());
                    }
                }

                itemTV.setText(arrFOCItem.get(i).getItemName() + " (Free Goods)");
                //qtyTV.setText(arrFOCItem.get(i).getQty());
                priceTV.setText(arrFOCItem.get(i).getPrice());

                tableRow.addView(itemTV);
                tableRow.addView(qtyTV);
                tableRow.addView(priceTV);

                binding.tableLayout.addView(tableRow);
            }
        }

        deleteDialog.show();

        binding.lytExcise.setVisibility(View.VISIBLE);
        binding.txtGross.setText("" + UtilApp.getNumberFormate(Math.round(btnTotVal)));
        binding.txtExcise.setText("" + UtilApp.getNumberFormate(Math.round(exciseAmt)));
        binding.txtVat.setText("" + UtilApp.getNumberFormate(Math.round(vatAmt)));
        binding.txtPreVat.setText("" + UtilApp.getNumberFormate(Math.round(preVatAmt)));
        binding.txtNetVal.setText("" + UtilApp.getNumberFormate(Math.round(netAmt)));
        binding.txtGrandTot.setText("" + UtilApp.getNumberFormate(Math.round(btnTotVal)));

        binding.txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        binding.txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equalsIgnoreCase("Return")) {
                    UtilApp.confirmationDialog("Are you sure you want to proceed?", OrderRequestActivity.this, new OnSearchableDialog() {
                        @Override
                        public void onItemSelected(Object o) {
                            String selection = (String) o;
                            if (selection.equalsIgnoreCase("yes")) {

                                callProceed();

                                deleteDialog.dismiss();
                            }
                        }
                    });

                } else if (type.equalsIgnoreCase("Load")) {
                    deleteDialog.dismiss();
                    openCommentDialog();
                } else {
                    deleteDialog.dismiss();
                    openCommentDialog();
                }

            }
        });

    }

    private void openCommentDialog() {

        LayoutInflater factory = LayoutInflater.from(this);
        OrderCommentDialogBinding binding = DataBindingUtil.inflate(factory, R.layout.order_comment_dialog, null, false);

        final View deleteDialogView = binding.getRoot();

        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtComment.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please enter comment!", Toast.LENGTH_SHORT).show();
                } else {

                    orderComment = binding.edtComment.getText().toString();
                    UtilApp.hideSoftKeyboard(OrderRequestActivity.this);
                    UtilApp.confirmationDialog("Are you sure you want to proceed?", OrderRequestActivity.this, new OnSearchableDialog() {
                        @Override
                        public void onItemSelected(Object o) {
                            String selection = (String) o;
                            if (selection.equalsIgnoreCase("yes")) {

                                callProceed();

                                deleteDialog.dismiss();
                            }
                        }
                    });
                }

            }
        });

        deleteDialog.show();
    }

    private void callProceed() {
        String orderNo;
        //Check is it Order id or Return Id
        if (type.equalsIgnoreCase("Return")) {
            orderNo = UtilApp.getLastIndex("Return");
        } else if (type.equalsIgnoreCase("Load")) {
            orderNo = UtilApp.getLastIndex("Load");
        } else {
            orderNo = UtilApp.getLastIndex("Order");
        }

        //get Print Data
        JSONObject data = null;
        try {
            if (type.equalsIgnoreCase("Return")) {
                jsonArray = createReturnPrintData(orderNo, returnType);
            } else if (type.equalsIgnoreCase("Load")) {
                jsonArray = createLoadPrintData(orderNo, baseBinding.toolbarTitle.getText().toString());
            } else {
                jsonArray = createOrderPrintData(orderNo, baseBinding.toolbarTitle.getText().toString());
            }
            data = new JSONObject();
            data.put("data", (JSONArray) jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (type.equalsIgnoreCase("Return")) {
            db.insertORReturnItems(orderNo, UtilApp.getCurrentDate(), "" +
                            Math.round(btnTotVal), customerCode, returnType,
                    mCustomer.getSalesmanId(), ""
                            + Math.round(vatAmt), "" +
                            Math.round(preVatAmt), "" +
                            Math.round(exciseAmt),
                    "" + Math.round(netAmt), arrSales, "");

            //Insert into Sales Summary
            SalesSummary mSummary = new SalesSummary();
            mSummary.setTransactionNo(orderNo);
            mSummary.setTransactionType(returnType);
            mSummary.setCustomerNo(mCustomer.getCustomerId());
            mSummary.setCustomerName(mCustomer.getCustomerName());
            mSummary.setDiscounts("0");
            mSummary.setTotalSales("" + Math.round(btnTotVal));
            db.insertSalesSummary(mSummary);

        } else if (type.equalsIgnoreCase("Load")) {
            db.insertLoadRequestItems(orderNo, UtilApp.getCurrentDate(), baseBinding.toolbarTitle.getText().toString(), "" + DecimalUtils.round(btnTotVal, 2),
                    Settings.getString(App.SALESMANID), "" + Math.round(vatAmt),
                    "" + Math.round(preVatAmt), ""
                            + Math.round(exciseAmt),
                    "" + Math.round(netAmt), arrSales, agentId, arrFOCItem, orderComment);
        } else {
            db.insertOrderItems(orderNo, UtilApp.getCurrentDate(), baseBinding.toolbarTitle.getText().toString(), "" +
                            Math.round(btnTotVal), customerCode,
                    mCustomer.getSalesmanId(), "" +
                            Math.round(vatAmt), "" +
                            Math.round(preVatAmt), "" +
                            Math.round(exciseAmt),
                    "" + Math.round(netAmt), arrSales, arrFOCItem, orderComment);
        }

        //INSERT TRANSACTION
        Transaction transaction = new Transaction();

        if (type.equalsIgnoreCase("Return"))
            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_RETURN_CREATED;
        else if (type.equalsIgnoreCase("Load"))
            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_LOAD_CREATE;
        else
            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_OREDER_CREATED;

        transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
        transaction.tr_customer_num = customerCode;
        transaction.tr_customer_name = customerName;
        transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
        transaction.tr_invoice_id = "";
        transaction.tr_order_id = orderNo;
        transaction.tr_collection_id = "";
        transaction.tr_pyament_id = "";
        transaction.tr_printData = data.toString();
        transaction.tr_is_posted = "No";

        db.insertTransaction(transaction);

        if (type.equalsIgnoreCase("Return")) {
            db.updateCustomerTransaction(customerCode, "return");
            Settings.setString(App.RETURN_LAST, orderNo);
        } else if (type.equalsIgnoreCase("Load")) {
            Settings.setString(App.LOAD_LAST, orderNo);
        } else {
            Settings.setString(App.ORDER_LAST, orderNo);
            db.updateCustomerTransaction(customerCode, "order");
        }


        UtilApp.dialogPrint(OrderRequestActivity.this, new OnSearchableDialog() {
            @Override
            public void onItemSelected(Object o) {
                if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                    UtilApp.createBackgroundJob(getApplicationContext());
                }
                String selection = (String) o;
                if (selection.equalsIgnoreCase("yes")) {
                    PrintLog object = new PrintLog(OrderRequestActivity.this,
                            OrderRequestActivity.this);
                    object.execute("", jsonArray);
                } else {
                    finish();
                }
            }
        });

    }

    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createOrderPrintData(String orderNo, String type) {
        JSONArray jArr = new JSONArray();

        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.ORDER);

            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));
            mainArr.put("invoicenumber", orderNo);  //Invoice No
            mainArr.put("deliveryDate", type);  //Invoice No

            mainArr.put("CUSTOMER", mCustomer.getCustomerName());
            mainArr.put("CUSTOMERID", mCustomer.getCustomerCode());
            mainArr.put("customertype", mCustomer.getCustType());
            mainArr.put("ADDRESS", "" + mCustomer.getAddress());
            if (mCustomer.getCustomerType().equalsIgnoreCase("4")) {
                mainArr.put("isHyperMarket", "true");
            } else {
                mainArr.put("isHyperMarket", "false");
            }
            mainArr.put("signature", strDigitSign);
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
                    double unitPrice = Double.parseDouble(obj.getUOMPrice()) - Double.parseDouble(obj.getDiscountAmt());
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
                    double unitAlPrice = Double.parseDouble(obj.getAlterUOMPrice()) - Double.parseDouble(obj.getDiscountAlAmt());
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
                        double unitPrice = Double.parseDouble(obj.getUOMPrice()) - Double.parseDouble(obj.getDiscountAmt());
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
                        double unitAlPrice = Double.parseDouble(obj.getAlterUOMPrice()) - Double.parseDouble(obj.getDiscountAlAmt());
                        data.put("" + UtilApp.getNumberFormate(unitAlPrice));// Unit Price
                        data.put(obj.getDiscountAmt());
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price

                        jData.put(data);
                    }
                }
            }

            if (arrFOCItem.size() > 0) {
                for (Item obj : arrFOCItem) {
                    if (Integer.parseInt(obj.getSaleBaseQty()) > 0) {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getBaseUOMName());// UOM
                        data.put(obj.getSaleBaseQty());//  QTY
                        data.put("0");// Unit Price
                        data.put(obj.getDiscountAmt());
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price

                        jData.put(data);
                    } else {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getAlterUOMName());// UOM
                        data.put(obj.getSaleAltQty());//  QTY
                        data.put("0");// Unit Price
                        data.put(obj.getDiscountAmt());
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price

                        jData.put(data);
                    }
                }
            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("VAT", UtilApp.getNumberFormate(Math.round(vatAmt)));
            totalObj.put("NET", UtilApp.getNumberFormate(Math.round(netAmt)));
            totalObj.put("EXCISE", UtilApp.getNumberFormate(Math.round(exciseAmt)));
            totalObj.put("Sub Total", UtilApp.getNumberFormate(Math.round(btnTotVal)));
            totalObj.put("Total Amount", UtilApp.getNumberFormate(Math.round(btnTotVal)));
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

    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createLoadPrintData(String orderNo, String type) {
        JSONArray jArr = new JSONArray();

        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.LOAD);

            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));
            mainArr.put("invoicenumber", orderNo);  //Invoice No
            mainArr.put("deliveryDate", type);  //Invoice No

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
                    double unitPrice = Double.parseDouble(obj.getUOMPrice()) - Double.parseDouble(obj.getDiscountAmt());
                    data.put("" + UtilApp.getNumberFormate(unitPrice));// Unit Price
                    data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price
                    jData.put(data);

                    //Add Alter UOM
                    JSONArray dataAl = new JSONArray();
                    dataAl.put(obj.getItemCode()); // ITEM CODE
                    dataAl.put(obj.getItemName());// DESC
                    dataAl.put(obj.getAlterUOMName());// UOM
                    dataAl.put(obj.getSaleAltQty());//  QTY
                    double unitAlPrice = Double.parseDouble(obj.getAlterUOMPrice()) - Double.parseDouble(obj.getDiscountAlAmt());
                    dataAl.put("" + UtilApp.getNumberFormate(unitAlPrice));// Unit Price
                    dataAl.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price
                    jData.put(dataAl);


                } else {
                    if (Integer.parseInt(obj.getSaleBaseQty()) > 0) {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getBaseUOMName());// UOM
                        data.put(obj.getSaleBaseQty());//  QTY
                        double unitPrice = Double.parseDouble(obj.getUOMPrice()) - Double.parseDouble(obj.getDiscountAmt());
                        data.put("" + UtilApp.getNumberFormate(unitPrice));// Unit Price
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price

                        jData.put(data);
                    } else {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getAlterUOMName());// UOM
                        data.put(obj.getSaleAltQty());//  QTY
                        double unitPrice = Double.parseDouble(obj.getAlterUOMPrice()) - Double.parseDouble(obj.getDiscountAlAmt());
                        data.put("" + UtilApp.getNumberFormate(unitPrice));// Unit Price
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price

                        jData.put(data);
                    }
                }
            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("VAT", UtilApp.getNumberFormate(Math.round(vatAmt)));
            totalObj.put("Total Amount", UtilApp.getNumberFormate(Math.round(btnTotVal)));
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


    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createReturnPrintData(String orderNo, String type) {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.RETURN);

            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));
            mainArr.put("invoicenumber", orderNo);  //Invoice No

            mainArr.put("CUSTOMER", mCustomer.getCustomerName());
            mainArr.put("CUSTOMERID", mCustomer.getCustomerCode());
            mainArr.put("customertype", mCustomer.getCustType());
            mainArr.put("ADDRESS", "" + mCustomer.getAddress());
            mainArr.put("TRN", "");
            mainArr.put("RETURN TYPE", type);

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
                    data.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getUOMPrice())));// Unit Price
                    data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price
                    jData.put(data);

                    //Add Alter UOM
                    JSONArray dataAl = new JSONArray();
                    dataAl.put(obj.getItemCode()); // ITEM CODE
                    dataAl.put(obj.getItemName());// DESC
                    dataAl.put(obj.getAlterUOMName());// UOM
                    dataAl.put(obj.getSaleAltQty());//  QTY
                    dataAl.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getAlterUOMPrice())));// Unit Price
                    dataAl.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price
                    jData.put(dataAl);


                } else {
                    if (Integer.parseInt(obj.getSaleBaseQty()) > 0) {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getBaseUOMName());// UOM
                        data.put(obj.getSaleBaseQty());//  QTY
                        data.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getUOMPrice())));// Unit Price
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price

                        jData.put(data);
                    } else {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getAlterUOMName());// UOM
                        data.put(obj.getSaleAltQty());//  QTY
                        data.put("" + UtilApp.getNumberFormate(Double.parseDouble(obj.getAlterUOMPrice())));// Unit Price
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price

                        jData.put(data);
                    }
                }
            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("VAT", UtilApp.getNumberFormate(Math.round(vatAmt)));
            totalObj.put("Total Amount", UtilApp.getNumberFormate(Math.round(btnTotVal)));
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
    public void onBackPressed() {
        if (isDetailView) {
            isDetailView = false;
            binding.lytMainView.setVisibility(View.VISIBLE);
            binding.lytItemView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public void callback() {
        finish();
    }
}
