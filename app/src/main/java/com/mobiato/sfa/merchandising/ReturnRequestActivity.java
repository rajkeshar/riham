package com.mobiato.sfa.merchandising;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.InputDialogOrderActivity;
import com.mobiato.sfa.activity.OrderRequestActivity;
import com.mobiato.sfa.databinding.ActivityReturnRequestBinding;
import com.mobiato.sfa.databinding.DialogReceiptSummaryBinding;
import com.mobiato.sfa.databinding.RowItemCategoryBinding;
import com.mobiato.sfa.databinding.RowItemSaleBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Category;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.DecimalUtils;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ReturnRequestActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private ActivityReturnRequestBinding binding;
    public ArrayList<Item> arrItem = new ArrayList<>();
    public ArrayList<Item> arrCSDItem = new ArrayList<>();
    public ArrayList<Item> arrJuiceItem = new ArrayList<>();
    public ArrayList<Item> arrWaterItem = new ArrayList<>();
    public ArrayList<Item> arrBisItem = new ArrayList<>();
    public ArrayList<Item> arrSales = new ArrayList<>();
    public ArrayList<Category> arrCategory = new ArrayList<>();
    private CommonAdapter<Category> mCategoryAdapter;
    private CommonAdapter<Item> mAdapter, mSelectAdapter;
    int catPosition = 0, itemPosition = 0;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private String customerCode = "", returnType = "", customerName = "";
    private DBManager db;
    private double btnTotVal = 0, preVatAmt = 0, vatAmt = 0, exciseAmt = 0, netAmt = 0;
    private Customer mCustomer;
    private boolean isDetailView = false;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReturnRequestBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        db = new DBManager(ReturnRequestActivity.this);

        registerReceiver(broadcastReceiver, new IntentFilter(InputDialogOrderActivity.BROADCAST_ACTION));

        customerCode = getIntent().getStringExtra("cust_code");
        mCustomer = db.getCustomerDetail(customerCode);
        returnType = getIntent().getStringExtra("ReturnType");

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        setTitle(formattedDate);

        setCategory();
        setCategoryAdapter();

        arrSales = new ArrayList<>();
        setItem();
        setCategoryItem(arrCSDItem, "CSD");

        baseBinding.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ReturnRequestActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        binding.fab.setOnClickListener(this);
        binding.rlCheckout.setOnClickListener(this);
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

        arrCSDItem = db.getItemListByCategory("2");
        arrJuiceItem = db.getItemListByCategory("4");
        arrWaterItem = db.getItemListByCategory("3");
        arrBisItem = db.getItemListByCategory("1");
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Item>(arrItem) {
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

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemPosition = position;
                            Intent i = new Intent(ReturnRequestActivity.this, InputDialogOrderActivity.class);
                            i.putExtra("type", "Return");
                            i.putExtra("returnType", returnType);
                            i.putExtra("customer", mCustomer.getCustomerId());
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

        binding.rvItemList.setAdapter(mAdapter);
    }

    private void setSelectedItemAdapter() {

        ArrayList<Item> arrItem = new ArrayList<>();
        arrItem.addAll(arrSales);

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
                    binding.lytMainView.setVisibility(View.GONE);
                    binding.lytItemView.setVisibility(View.VISIBLE);

                    fillButtonPrice();
                    setSelectedItemAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), "Please select items", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_checkout:
                makeDilog();
                break;
            default:
                break;
        }
    }

    private void fillButtonPrice() {

        for (int i = 0; i < arrSales.size(); i++) {

            double totAmt = Double.parseDouble(arrSales.get(i).getPrice());

            vatAmt += Double.parseDouble(arrSales.get(i).getVatAmt());
            preVatAmt += Double.parseDouble(arrSales.get(i).getPreVatAmt());
            exciseAmt += Double.parseDouble(arrSales.get(i).getExciseAmt());
            netAmt += Double.parseDouble(arrSales.get(i).getNetAmt());

            btnTotVal += totAmt;
        }

        binding.txtTot.setText(UtilApp.getNumberFormate(btnTotVal) + " UGX");
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            Item item = (Item) intent.getSerializableExtra("item");

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

            updateItem(item);
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

        binding.tvSummary.setText("Return Summary");


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
            priceTV.setText(arrSales.get(i).getPrice());

            tableRow.addView(itemTV);
            tableRow.addView(qtyTV);
            tableRow.addView(priceTV);

            binding.tableLayout.addView(tableRow);
        }

        deleteDialog.show();

        binding.lytExcise.setVisibility(View.VISIBLE);
        binding.txtGross.setText("" + UtilApp.getNumberFormate(DecimalUtils.round(btnTotVal, 2)));
        binding.txtExcise.setText("" + UtilApp.getNumberFormate(DecimalUtils.round(exciseAmt, 2)));
        binding.txtVat.setText("" + UtilApp.getNumberFormate(DecimalUtils.round(vatAmt, 2)));
        binding.txtPreVat.setText("" + UtilApp.getNumberFormate(DecimalUtils.round(preVatAmt, 2)));
        binding.txtNetVal.setText("" + UtilApp.getNumberFormate(DecimalUtils.round(netAmt, 2)));
        binding.txtGrandTot.setText("" + UtilApp.getNumberFormate(DecimalUtils.round(btnTotVal, 2)));

        binding.txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        binding.txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderNo;
                //Check is it Order id or Return Id
                orderNo = UtilApp.getLastIndex("Return");

                //get Print Data
                JSONObject data = null;
                try {
                    jsonArray = createReturnPrintData(orderNo, returnType);
                    data = new JSONObject();
                    data.put("data", (JSONArray) jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                db.insertReturnRequestItems(orderNo, baseBinding.toolbarTitle.getText().toString(), "" +
                                DecimalUtils.round(btnTotVal, 2), customerCode, returnType,
                        mCustomer.getSalesmanId(), ""
                                + DecimalUtils.round(vatAmt, 2), "" +
                                DecimalUtils.round(preVatAmt, 2), "" +
                                DecimalUtils.round(exciseAmt, 2),
                        "" + DecimalUtils.round(netAmt, 2), arrSales, "");


                //INSERT TRANSACTION
                Transaction transaction = new Transaction();

                transaction.tr_type = Constant.TRANSACTION_TYPES.TT_RETURN_CREATED;
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

                db.updateCustomerTransaction(customerCode, "return");
                Settings.setString(App.RETURN_LAST, orderNo);

                if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                    UtilApp.createBackgroundJob(getApplicationContext());
                }

                deleteDialog.dismiss();

                UtilApp.dialogPrint(ReturnRequestActivity.this, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                            UtilApp.createBackgroundJob(getApplicationContext());
                        }
                        String selection = (String) o;
                        if (selection.equalsIgnoreCase("yes")) {
                            PrintLog object = new PrintLog(ReturnRequestActivity.this,
                                    ReturnRequestActivity.this);
                            object.execute("", jsonArray);
                        } else {
                            finish();
                        }
                    }
                });

            }
        });

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


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        baseBinding.toolbarTitle.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
    }

    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createReturnPrintData(String orderNo, String type) {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.RETURN_REQUEST);

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
            totalObj.put("VAT", UtilApp.getNumberFormate(vatAmt));
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

    public void callback() {
        finish();
    }

}

