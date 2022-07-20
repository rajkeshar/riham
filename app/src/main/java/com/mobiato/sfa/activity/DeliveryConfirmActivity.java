package com.mobiato.sfa.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityDeliveryConfirmBinding;
import com.mobiato.sfa.databinding.DialogReceiptSummaryBinding;
import com.mobiato.sfa.databinding.RowItemSaleBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Discount;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.SalesInvoice;
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

public class DeliveryConfirmActivity extends BaseActivity {

    private ActivityDeliveryConfirmBinding binding;
    private Customer mCustomer;
    private String orderNo = "", orderId = "";
    private ArrayList<Item> arrData = new ArrayList<>();
    private CommonAdapter<Item> mAdapter;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private DBManager db;
    private String invNum, CollNum;

    double grossAmt = 0;
    double vatAmt = 0;
    double preVatAmt = 0;
    double exciseAmt = 0;
    double agentexciseAmt = 0;
    double netAmt = 0;
    double grandTot = 0;
    int totalQty = 0;
    int totalAltQty = 0;
    int totalBaseQty = 0, itemPosition = 0;
    boolean isCatDisApply = false;

    JSONArray headerData = null;
    JSONArray headerJarr = null;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryConfirmBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Delivery");

        registerReceiver(broadcastReceiver, new IntentFilter(InputDialogActivity.BROADCAST_ACTION));

        db = new DBManager(DeliveryConfirmActivity.this);

        if (getIntent() != null) {
            mCustomer = (Customer) getIntent().getSerializableExtra("customer");
            orderNo = getIntent().getStringExtra("orderNo");
            orderId = getIntent().getStringExtra("orderId");
        }

        arrData = db.getDeliveryItems(orderNo);
        fillButtonPrice();
        setAdapter();

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new submitData().execute();
            }
        });
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
                            UtilApp.logData(DeliveryConfirmActivity.this, "On Item Click " + position);
                            itemPosition = position;
                            Intent i = new Intent(DeliveryConfirmActivity.this, InputDialogActivity.class);
                            i.putExtra("item", arrData.get(position));
                            i.putExtra("custId", mCustomer.getCustomerId());
                            i.putExtra("routeId", mCustomer.getRouteId());
                            i.putExtra("type", "Delivery");
                            startActivity(i);
                        }
                    });


                    holder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeliveryConfirmActivity.this);
                            alertDialogBuilder.setTitle("Delete")
                                    .setMessage("Are you sure you want to delete this item?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String itemId = arrData.get(position).getItemId();
                                            arrData.remove(position);
                                            notifyDataSetChanged();

                                            //delete delivery Item
                                            db.deleteDeliveryItem(orderId, itemId);

                                            fillButtonPrice();
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            // show it
                            alertDialog.show();
                            return false;
                        }
                    });

                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_sale;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            Item item = (Item) intent.getSerializableExtra("item");

            boolean flag = false;
            for (int i = 0; i < arrData.size(); i++) {
                if (arrData.get(i).getItemId().equalsIgnoreCase(item.getItemId())) {
                    flag = true;
                    arrData.set(i, item);
                    break;
                }
            }

            if (!flag)
                arrData.add(item);

            //check Category Discount
//            if (db.isCategoryDiscount(mCustomer.getCustomerId())) {
//                applyCategoryDiscount(item, "false");
//            } else if (db.isRouteCategoryDiscount()) {
//                applyCategoryDiscount(item, "true");
//            } else {
            //Update Invoice Price
            setAdapter();
            fillButtonPrice();
            // }

        }

    };

    public void applyCategoryDiscount(Item item, String isRoute) {
        Discount mDiscount = null;
        ArrayList<String> arrCategoryId = new ArrayList<>();
        if (isRoute.equalsIgnoreCase("true")) {
            //  mDiscount = db.getRouteCategoryDiscount();
        } else {
            //   mDiscount = db.getCategoryDiscount(mCustomer.getCustomerId());
        }

        //Get Discount Category
        String categoryId = mDiscount.getCategoryId();
        String[] spCat = categoryId.split(",");
        for (int k = 0; k < spCat.length; k++) {
            arrCategoryId.add(spCat[k]);
        }

        int categoryQTy = 0;
        for (int i = 0; i < arrData.size(); i++) {
            if (arrCategoryId.contains(arrData.get(i).getCategory())) {
                categoryQTy += Integer.parseInt(arrData.get(i).getSaleAltQty());
            }
        }

        if (mDiscount != null) {

            if (categoryQTy >= Integer.parseInt(mDiscount.getQty())) {

                for (int j = 0; j < arrData.size(); j++) {

                    if (arrCategoryId.contains(arrData.get(j).getCategory())) {
                        double itemAlterPrice = Double.parseDouble(arrData.get(j).getAlterUOMPrice());
                        double value = 0;
                        if (mDiscount.getType().equalsIgnoreCase("1")) {
                            value = ((itemAlterPrice * Double.parseDouble(mDiscount.getDiscount())) / 100);
                        } else {
                            value = Double.parseDouble(mDiscount.getDiscount());
                        }

                        itemAlterPrice = itemAlterPrice - value;
                        double discountAl = Double.parseDouble(mDiscount.getDiscount());
                        double discountAlAmt = Double.parseDouble(arrData.get(j).getDiscountAlAmt()) + value;
                        double alterSellPrice = 0;
                        alterSellPrice = Integer.parseInt(arrData.get(j).getSaleAltQty()) * itemAlterPrice;
                        double finalPrice = alterSellPrice + Double.parseDouble(arrData.get(j).getSaleBasePrice());

                        //set Data for Discount
                        arrData.get(j).setPrice("" + finalPrice);
                        arrData.get(j).setDiscountAlAmt("" + DecimalUtils.round(discountAlAmt, 2));
                        arrData.get(j).setDiscountAlPer("" + discountAl);
                        arrData.get(j).setAlterUOMPrice("" + (itemAlterPrice + discountAlAmt));
                        arrData.get(j).setSaleAltPrice("" + alterSellPrice);

                        //update Item after discount
                        setAdapter();
                    }
                }

                isCatDisApply = true;

                //Update Invoice Price
                fillButtonPrice();

            } else {

                if (isCatDisApply) {
                    isCatDisApply = false;

                    for (int j = 0; j < arrData.size(); j++) {

                        if (arrCategoryId.contains(arrData.get(j).getCategory())) {
                            double itemAlterPrice = 0;
                            if (db.isPricingItems(mCustomer.getCustomerId(), arrData.get(j).getItemId())) {
                                Item pItem = db.getCustPricingItems(mCustomer.getCustomerId(), arrData.get(j).getItemId());
                                itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                            } else {
                                itemAlterPrice = db.getItemAlterPrice(arrData.get(j).getItemId());
                            }
                            double alterSellPrice = 0;
                            alterSellPrice = Integer.parseInt(arrData.get(j).getSaleAltQty()) * itemAlterPrice;
                            double finalPrice = alterSellPrice + Double.parseDouble(arrData.get(j).getSaleBasePrice());

                            //set Data for Discount
                            arrData.get(j).setPrice("" + finalPrice);
                            arrData.get(j).setDiscountAlAmt("0");
                            arrData.get(j).setDiscountAlPer("0");
                            arrData.get(j).setAlterUOMPrice("" + (itemAlterPrice));
                            arrData.get(j).setSaleAltPrice("" + alterSellPrice);

                            //update Item after discount
                            setAdapter();
                        }
                    }
                }

                //Update Invoice Price
                setAdapter();
                fillButtonPrice();
            }
        } else {
            //Update Invoice Price
            setAdapter();
            fillButtonPrice();
        }
    }

    private void fillButtonPrice() {
        double btnTotVal = 0;
        for (int i = 0; i < arrData.size(); i++) {

            double totAmt = Double.parseDouble(arrData.get(i).getPrice());

            btnTotVal += totAmt;
        }

        grandTot = btnTotVal;
        binding.tvAmount.setText(UtilApp.getNumberFormate(Math.round(btnTotVal)) + " UGX");
    }


    private class submitData extends AsyncTask<Void, Void, Boolean> {

        private LoadingSpinner mDialog;
        private boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new LoadingSpinner(DeliveryConfirmActivity.this);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            for (int i = 0; i < arrData.size(); i++) {
                int saleBQty = Integer.parseInt(arrData.get(i).getSaleBaseQty());
                int saleAQty = Integer.parseInt(arrData.get(i).getSaleAltQty());

                if (saleAQty > 0 && saleBQty > 0) {
                    boolean isAvailble = db.isItemAvailable(arrData.get(i).getItemId(), "" + saleBQty, "" + saleAQty);
                    if (!isAvailble) {
                        isSuccess = false;
                        break;
                    }
                } else {
                    if (saleBQty > 0) {
                        boolean isAvailble = db.isFreeGoodAvailable(arrData.get(i).getItemId(), "Base", "" + saleBQty);
                        if (!isAvailble) {
                            isSuccess = false;
                            break;
                        }
                    } else {
                        boolean isAvailble = db.isFreeGoodAvailable(arrData.get(i).getItemId(), "Alter", "" + saleAQty);
                        if (!isAvailble) {
                            isSuccess = false;
                            break;
                        }
                    }
                }
            }

            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (mDialog.isShowing())
                mDialog.hide();

            if (isSuccess) {
                grossAmt = 0;
                netAmt = 0;
                vatAmt = 0;
                exciseAmt = 0;
                agentexciseAmt = 0;
                totalQty = 0;
                preVatAmt = 0;
                totalAltQty = 0;
                totalBaseQty = 0;
                grandTot = 0;
                makeDilog();
            } else {
                UtilApp.displayAlert(DeliveryConfirmActivity.this, "Not enough stock in vanstock! You are not deliver this order!");
            }
        }
    }


    private void makeDilog() {

        LayoutInflater factory = LayoutInflater.from(this);
        DialogReceiptSummaryBinding binding = DataBindingUtil.inflate(factory, R.layout.dialog_receipt_summary, null, false);

        final View deleteDialogView = binding.getRoot();

        final android.support.v7.app.AlertDialog deleteDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        headerJarr = new JSONArray();

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

            headerData = new JSONArray();

            itemTV.setText(arrData.get(i).getItemName());
            priceTV.setText(arrData.get(i).getPrice());

            headerData.put(arrData.get(i).getItemCode()); // ITEM CODE
            headerData.put(arrData.get(i).getItemName());// DESC
            headerData.put(arrData.get(i).getBaseUOMName());// Base UOM
            headerData.put(arrData.get(i).getBaseUOMQty());// Base QTY
            headerData.put(arrData.get(i).getAlterUOMName());// Alter UOM
            headerData.put(arrData.get(i).getAlterUOMQty());// Alter QTY

            tableRow.addView(itemTV);
            tableRow.addView(qtyTV);
            tableRow.addView(priceTV);

            if (Integer.parseInt(arrData.get(i).getSaleAltQty()) > 0 && Integer.parseInt(arrData.get(i).getSaleBaseQty()) > 0) {
                qtyTV.setText(arrData.get(i).getSaleAltQty() + "/" + arrData.get(i).getSaleBaseQty());
                totalBaseQty += Integer.parseInt(arrData.get(i).getSaleBaseQty());
                totalAltQty += Integer.parseInt(arrData.get(i).getSaleAltQty());
            } else {
                if (Integer.parseInt(arrData.get(i).getSaleAltQty()) > 0) {
                    qtyTV.setText(arrData.get(i).getSaleAltQty());
                    totalAltQty += Integer.parseInt(arrData.get(i).getSaleAltQty());
                } else {
                    qtyTV.setText(arrData.get(i).getSaleBaseQty());
                    totalBaseQty += Integer.parseInt(arrData.get(i).getSaleBaseQty());
                }
            }

            totalQty += Integer.parseInt(arrData.get(i).getSaleQty());

            double totPrice = Double.parseDouble(arrData.get(i).getPrice());
            grossAmt += totPrice;

            double itemVatAmt = UtilApp.getVat(Double.parseDouble(arrData.get(i).getPrice()));
            vatAmt += itemVatAmt;

            double itemPreVatAmt = Double.parseDouble(arrData.get(i).getPrice()) - itemVatAmt;
            preVatAmt += itemPreVatAmt;

            double itemExcise = 0;
            exciseAmt += itemExcise;

            //Riddhi
            double itemAgentExcise = 0;
            agentexciseAmt += itemAgentExcise;

            double itemNet = itemPreVatAmt - itemExcise;
            netAmt += itemNet;

            double itemTotal = itemNet + itemVatAmt;
            grandTot += itemTotal;

            arrData.get(i).setPreVatAmt("" + Math.round(itemPreVatAmt));
            arrData.get(i).setVatAmt("" + Math.round(itemVatAmt));
            arrData.get(i).setExciseAmt("" + Math.round(itemExcise));
            arrData.get(i).setAgentExcise("" + Math.round(itemAgentExcise));
            arrData.get(i).setNetAmt("" + Math.round(itemNet));
            arrData.get(i).setCategory(db.getItemCategory(arrData.get(i).getItemId()));

            binding.tableLayout.addView(tableRow);

            headerJarr.put(headerData);

        }

        deleteDialog.show();

        binding.txtGross.setText("" + UtilApp.getNumberFormate(Math.round(grossAmt)));
        binding.txtExcise.setText("" + UtilApp.getNumberFormate(Math.round(exciseAmt)));
        binding.txtVat.setText("" + UtilApp.getNumberFormate(Math.round(vatAmt)));
        binding.txtPreVat.setText("" + UtilApp.getNumberFormate(Math.round(preVatAmt)));
        binding.txtNetVal.setText("" + UtilApp.getNumberFormate(Math.round(netAmt)));
        binding.txtGrandTot.setText("" + UtilApp.getNumberFormate(Math.round(grandTot)));

        binding.txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        binding.txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GENERATE NEXT INVOICE NUMBER
                invNum = UtilApp.getLastIndex("Invoice");

                //INSERT IN SALES INVOICE TABLE
                insertSalesInvoiceHeader(invNum, "" + totalQty, "" +
                                DecimalUtils.round(grossAmt, 2), "" +
                                DecimalUtils.round(preVatAmt, 2),
                        "" + DecimalUtils.round(vatAmt, 2), "" +
                                DecimalUtils.round(exciseAmt, 2), "" +
                                DecimalUtils.round(netAmt, 2), orderId, "" + totalAltQty, "" + totalBaseQty);

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

                for (int i = 0; i < arrData.size(); i++) {

                    //update VanStock Qty
                    updateVanStockQty(arrData.get(i));

                    //add Invoice Items
                    db.insertSalesInvoiceItems(arrData.get(i), invNum, mCustomer.getCustomerId(), "0");

                    deleteDialog.dismiss();

                }

                //store Last Invoice Number
                Settings.setString(App.INVOICE_LAST, invNum);

                //get collection Number
                CollNum = UtilApp.getLastIndex("Collection");

                //insert into Collection if Credit & TC
                db.addCollection(invNum, CollNum, mCustomer.getCustomerId(), UtilApp.getTCDueDate(mCustomer.getPaymentTerm()),
                        UtilApp.getCurrentDate(), "" + grandTot);

//                //insert into Collection if Credit & TC
//                if (!mCustomer.getCustType().equalsIgnoreCase("1")) {
//                    db.insertCollection(invNum, CollNum, mCustomer.getCustomerId(), "",
//                            UtilApp.getTCDueDate(mCustomer.getPaymentTerm()), UtilApp.getCurrentDate(), "" + grandTot, "0", "0",
//                            "0", "", "", "", "0", "1", "N");
//                }

                //store Last Invoice Number
                Settings.setString(App.COLLECTION_LAST, CollNum);

                //Update delivery Status Confrim
                db.deliveryStatus(orderId);
                db.deliveryItemStatus(orderId);

                UtilApp.dialogPrint(DeliveryConfirmActivity.this, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                            UtilApp.createBackgroundJob(getApplicationContext());
                        }
                        String selection = (String) o;
                        if (selection.equalsIgnoreCase("yes")) {
                            PrintLog object = new PrintLog(DeliveryConfirmActivity.this,
                                    DeliveryConfirmActivity.this);
                            object.execute("", jsonArray);
                        } else {

                            if (mCustomer.getCustType().equalsIgnoreCase("1")) {
                                Intent in = new Intent(DeliveryConfirmActivity.this, PaymentDetailActivity.class);
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
                                Intent intent = new Intent(DeliveryConfirmActivity.this, CustomerDetailActivity.class);
                                intent.putExtra("custmer", mCustomer);
                                intent.putExtra("tag", "old");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    }
                });


            }
        });
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

    private void insertSalesInvoiceHeader(String invNum, String totalQty, String totalAMt, String preVatAmt,
                                          String vatAmt, String exciseAmt, String netAmt, String deliveryNo, String altQty, String basQty) {
        final SalesInvoice salesInvoice = new SalesInvoice();

        salesInvoice.inv_no = invNum;
        salesInvoice.inv_type = "Delivery";
        salesInvoice.exchangeNo = "";
        salesInvoice.inv_type_code = mCustomer.getCustType();
        salesInvoice.cust_code = mCustomer.getCustomerId();
        salesInvoice.inv_date = UtilApp.getCurrentDate();
        salesInvoice.del_date = UtilApp.getCurrentDate();
        salesInvoice.delivery_no = deliveryNo;
        salesInvoice.total_qty = totalQty;
        salesInvoice.tot_amnt_sales = totalAMt;
        salesInvoice.pre_VatAmt = preVatAmt;
        salesInvoice.vatAmt = vatAmt;
        salesInvoice.exciseAmt = exciseAmt;
        salesInvoice.netAmt = netAmt;
        salesInvoice.exchangeAmt = "";
        salesInvoice.grNo = "";
        salesInvoice.brNo = "";
        salesInvoice.brNo = "";
        salesInvoice.altQty = altQty;
        salesInvoice.baseQty = basQty;
        salesInvoice.saletime = UtilApp.getCurrent12Time();
        salesInvoice.purchaseName = "";
        salesInvoice.purchaseNo = "";

        db.insertSalesInvoice(salesInvoice);

    }

    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createPrintData(String invNum) {
        JSONArray jArr = new JSONArray();

        try {
            int totalAmount = 0;
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.SALES_INVOICE);

            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));
            mainArr.put("invoicenumber", invNum);  //Invoice No

            mainArr.put("CUSTOMER", mCustomer.getCustomerName());
            mainArr.put("CUSTOMERID", mCustomer.getCustomerCode());
            mainArr.put("customertype", "1");
            mainArr.put("ADDRESS", "" + mCustomer.getAddress());
            mainArr.put("TRN", "");

            JSONArray TOTAL = new JSONArray();
            //SALES INVOICE
            JSONArray jData = new JSONArray();
            for (Item obj : arrData) {

                JSONArray data = new JSONArray();

                data.put(obj.getItemCode()); // ITEM CODE
                data.put(obj.getItemName());// DESC
                data.put(obj.getBaseUOMName());// Base UOM
                data.put(obj.getSaleBaseQty());// Base QTY
                data.put(obj.getBaseUOMPrice());// Base Price
                data.put(obj.getAlterUOMName());// Alter UOM
                data.put(obj.getSaleAltQty());// Alter QTY
                data.put(obj.getAlterUOMPrice());// Alter UOM
                data.put(obj.getPreVatAmt());// Pre Vat Amt
                data.put(obj.getVatAmt());// Vat Amt
                data.put(obj.getExciseAmt());// Excise Amt
                data.put(obj.getAgentExcise());
                data.put(obj.getNetAmt());// Net AMt
                data.put(obj.getPrice());// Total

                jData.put(data);
            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("Total Befor TAX(AED)", String.format("%.2f", totalAmount));
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

}
