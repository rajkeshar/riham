package com.mobiato.sfa.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityReturnUpdateBinding;
import com.mobiato.sfa.databinding.DialogReceiptSummaryBinding;
import com.mobiato.sfa.databinding.RowItemSaleBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.SalesSummary;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.DecimalUtils;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ReturnUpdateActivity extends BaseActivity implements View.OnClickListener {

    public ActivityReturnUpdateBinding binding;
    private ArrayList<Item> arrData = new ArrayList<>();
    private String orderNo = "", returnType = "", customerCode = "";
    private DBManager db;
    private CommonAdapter<Item> mAdapter;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private double totalAMt = 0, preVatAmt = 0, vatAmt = 0, exciseAmt = 0, netAmt = 0;
    private JSONArray jsonArray;
    private Customer mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReturnUpdateBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Return Detail");

        registerReceiver(broadcastReceiver, new IntentFilter(InputDialogOrderActivity.BROADCAST_ACTION));

        db = new DBManager(ReturnUpdateActivity.this);

        arrData = new ArrayList<>();
        orderNo = getIntent().getStringExtra("orderNo");
        returnType = getIntent().getStringExtra("ReturnType");
        customerCode = getIntent().getStringExtra("cust_code");
        mCustomer = db.getCustomerDetail(customerCode);

        arrData = db.getReturnItems(orderNo);
        totalAMt = db.getReturnTotalAmt(orderNo);

        binding.txtTot.setText("Total: " + UtilApp.getNumberFormate(totalAMt) + " UGX");

        setAdapter();

        binding.rlCheckout.setOnClickListener(this);

    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Item>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemSaleBinding) {
                    Item mItem = arrData.get(position);
                    ((RowItemSaleBinding) holder.binding).setItem(mItem);

                    ((RowItemSaleBinding) holder.binding).imageView2.setLetter(mItem.getItemName());
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
                            Intent i = new Intent(ReturnUpdateActivity.this, InputDialogOrderActivity.class);
                            i.putExtra("type", "Return");
                            i.putExtra("returnType", returnType);
                            i.putExtra("customer", mCustomer.getCustomerId());
                            i.putExtra("item", arrData.get(position));
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

        binding.rvList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_checkout:
                preVatAmt = 0;
                netAmt = 0;
                exciseAmt = 0;
                vatAmt = 0;
                makeDilog();
                break;
            default:
                break;
        }
    }

    private void makeDilog() {

        LayoutInflater factory = LayoutInflater.from(this);
        DialogReceiptSummaryBinding binding = DataBindingUtil.inflate(factory, R.layout.dialog_receipt_summary, null, false);

        final View deleteDialogView = binding.getRoot();

        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        binding.tvSummary.setText("Return Summary");

        for (int i = 0; i < arrData.size(); i++) {

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

            if (Integer.parseInt(arrData.get(i).getSaleAltQty()) > 0 && Integer.parseInt(arrData.get(i).getSaleBaseQty()) > 0) {
                qtyTV.setText(arrData.get(i).getSaleAltQty() + "/" + arrData.get(i).getSaleBaseQty());
            } else {
                if (Integer.parseInt(arrData.get(i).getSaleAltQty()) > 0) {
                    qtyTV.setText(arrData.get(i).getSaleAltQty());
                } else {
                    qtyTV.setText(arrData.get(i).getSaleBaseQty());
                }
            }

            itemTV.setText(arrData.get(i).getItemName());
            priceTV.setText(UtilApp.getNumberFormate(Double.parseDouble(arrData.get(i).getPrice())));

            String itemCat1 = db.getItemCategory(arrData.get(i).getItemId());
            double itemVatAmt = UtilApp.vatAmount(arrData.get(i),itemCat1);
            vatAmt += itemVatAmt;

            double itemPreVatAmt = UtilApp.getPreVatAmount(arrData.get(i),itemCat1);
            preVatAmt += itemPreVatAmt;

            double itemExcise = 0;
//            String itemCat = db.getItemCategory(arrData.get(i).getItemId());
//            String itemBasVolume = db.getItemBaseVolume(arrData.get(i).getItemId());
//            String itemAltVolume = db.getItemAlterVolume(arrData.get(i).getItemId());
//            if (itemCat.equalsIgnoreCase("1")) {
//                itemExcise = 0;
//            } else {
//                itemExcise = UtilApp.getExciseValue(arrData.get(i), itemCat, itemBasVolume, itemAltVolume);
//            }
            exciseAmt += itemExcise;

            double itemNet = itemPreVatAmt - itemExcise;
            netAmt += itemNet;

            tableRow.addView(itemTV);
            tableRow.addView(qtyTV);
            tableRow.addView(priceTV);

            binding.tableLayout.addView(tableRow);
        }

        deleteDialog.show();

        binding.lytExcise.setVisibility(View.GONE);
        binding.txtGross.setText("" + UtilApp.getNumberFormateInt((int) Math.round(totalAMt)));
        binding.txtExcise.setText("" + UtilApp.getNumberFormate((int) Math.round(exciseAmt)));
        binding.txtVat.setText("" + UtilApp.getNumberFormate((int) Math.round(vatAmt)));
        binding.txtPreVat.setText("" + UtilApp.getNumberFormate((int) Math.round(preVatAmt)));
        binding.txtNetVal.setText("" + UtilApp.getNumberFormate((int) Math.round(netAmt)));
        binding.txtGrandTot.setText("" + UtilApp.getNumberFormate((int) Math.round(totalAMt)));

        binding.txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        binding.txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get Print Data
                JSONObject data = null;
                try {
                    jsonArray = createReturnPrintData(orderNo, returnType);
                    data = new JSONObject();
                    data.put("data", (JSONArray) jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Delete Previous Items
                boolean isSuccess = db.deleteReturnItem(orderNo);

                //update Return Items
                db.updateReturnItems(orderNo, UtilApp.getCurrentDate(), "" +
                                Math.round(totalAMt), customerCode, returnType,
                        Settings.getString(App.SALESMANID), ""
                                + Math.round(vatAmt), "" +
                                Math.round(preVatAmt), "" +
                                Math.round(exciseAmt),
                        "" + Math.round(netAmt), arrData, "");


                //Insert into Sales Summary
                SalesSummary mSummary = new SalesSummary();
                mSummary.setTransactionNo(orderNo);
                mSummary.setTransactionType(returnType);
                mSummary.setCustomerNo(mCustomer.getCustomerId());
                mSummary.setCustomerName(mCustomer.getCustomerName());
                mSummary.setDiscounts("0");
                mSummary.setTotalSales("" + DecimalUtils.round(totalAMt, 2));
                db.insertSalesSummary(mSummary);


                //INSERT TRANSACTION
                Transaction transaction = new Transaction();

                transaction.tr_type = Constant.TRANSACTION_TYPES.TT_RETURN_CREATED;
                transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
                transaction.tr_customer_num = customerCode;
                transaction.tr_customer_name = mCustomer.getCustomerName();
                transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
                transaction.tr_invoice_id = "";
                transaction.tr_order_id = orderNo;
                transaction.tr_collection_id = "";
                transaction.tr_pyament_id = "";
                transaction.tr_printData = data.toString();
                transaction.tr_is_posted = "No";

                db.insertTransaction(transaction);

                //Update customer Transaction
                db.updateCustomerTransaction(customerCode, "return");


                deleteDialog.dismiss();

                UtilApp.dialogPrint(ReturnUpdateActivity.this, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                            UtilApp.createBackgroundJob(getApplicationContext());
                        }
                        String selection = (String) o;
                        if (selection.equalsIgnoreCase("yes")) {
                            PrintLog object = new PrintLog(ReturnUpdateActivity.this,
                                    ReturnUpdateActivity.this);
                            object.execute("", jsonArray);
                        } else {
                            finish();
                        }
                    }
                });

            }
        });
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
            for (Item obj : arrData) {

                double itemBasePrice = 0, itemAlterPrice = 0;
                if (db.isPricingItems(mCustomer.getCustomerId(), obj.getItemId())) {
                    Item pItem = db.getCustPricingItems(mCustomer.getCustomerId(), obj.getItemId());
                    itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
                    itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                } else if (db.isAgentPricingItems(obj.getItemId(), mCustomer.getRouteId())) {
                    Item pItem = db.getAgentPricingItems(obj.getItemId());
                    itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
                    itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                } else {
                    itemBasePrice = db.getItemPrice(obj.getItemId());
                    itemAlterPrice = db.getItemAlterPrice(obj.getItemId());
                }

                if (Integer.parseInt(obj.getSaleBaseQty()) > 0 && Integer.parseInt(obj.getSaleAltQty()) > 0) {

                    //Add Base UOM
                    JSONArray data = new JSONArray();
                    data.put(obj.getItemCode()); // ITEM CODE
                    data.put(obj.getItemName());// DESC
                    data.put(obj.getBaseUOMName());// UOM
                    data.put(obj.getSaleBaseQty());//  QTY
                    data.put("" + UtilApp.getNumberFormate(itemBasePrice));// Unit Price
                    data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price
                    jData.put(data);

                    //Add Alter UOM
                    JSONArray dataAl = new JSONArray();
                    dataAl.put(obj.getItemCode()); // ITEM CODE
                    dataAl.put(obj.getItemName());// DESC
                    dataAl.put(obj.getAlterUOMName());// UOM
                    dataAl.put(obj.getSaleAltQty());//  QTY
                    dataAl.put("" + UtilApp.getNumberFormate(itemAlterPrice));// Unit Price
                    dataAl.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price
                    jData.put(dataAl);


                } else {
                    if (Integer.parseInt(obj.getSaleBaseQty()) > 0) {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getBaseUOMName());// UOM
                        data.put(obj.getSaleBaseQty());//  QTY
                        data.put("" + UtilApp.getNumberFormate(itemBasePrice));// Unit Price
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price

                        jData.put(data);
                    } else {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getAlterUOMName());// UOM
                        data.put(obj.getSaleAltQty());//  QTY
                        data.put("" + UtilApp.getNumberFormate(itemAlterPrice));// Unit Price
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price

                        jData.put(data);
                    }
                }
            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("VAT", UtilApp.getNumberFormate(vatAmt));
            totalObj.put("Total Amount", UtilApp.getNumberFormate(Math.round(totalAMt)));
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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            Item item = (Item) intent.getSerializableExtra("item");

            for (int i = 0; i < arrData.size(); i++) {
                if (arrData.get(i).getItemId().equalsIgnoreCase(item.getItemId())) {
                    arrData.set(i, item);
                    break;
                }
            }

            fillButtonPrice();
            setAdapter();
        }
    };

    private void fillButtonPrice() {
        totalAMt = 0;
        for (int i = 0; i < arrData.size(); i++) {

            double totAmt = Double.parseDouble(arrData.get(i).getPrice());

            totalAMt += totAmt;
        }

        binding.txtTot.setText(UtilApp.getNumberFormate(totalAMt) + " UGX");
    }

}
