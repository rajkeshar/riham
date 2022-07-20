package com.mobiato.sfa.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityFreshUnloadBinding;
import com.mobiato.sfa.databinding.DialogInputUnloadBinding;
import com.mobiato.sfa.databinding.RowItemFreshunloadBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.model.UnloadSummary;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.rest.SyncData;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FreshUnloadActivity extends BaseActivity implements View.OnClickListener {

    private ActivityFreshUnloadBinding binding;
    public ArrayList<Item> arrAllItem = new ArrayList<>();
    private CommonAdapter<Item> mAdapter;
    public ArrayList<String> arrCheckItem = new ArrayList<>();
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private String alterActQty, baseActQty;
    private int itemPosition = 0;
    private int countPlus = 0;
    private DBManager db;
    private String type = "ROT";

    private LoadingSpinner progressDialog;
    private JSONArray jsonArray;
    public ArrayList<UnloadSummary> arrEndData = new ArrayList<>();
    private boolean isClick = false;
    private boolean isUnloadCLick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFreshUnloadBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Fresh Unload");

        db = new DBManager(FreshUnloadActivity.this);
        arrAllItem = new ArrayList<>();
        arrAllItem = db.getAllVanStockItemList();

        setAdapter();

        registerReceiver(broadcastReceiver, new IntentFilter(InputDialogOrderActivity.BROADCAST_ACTION));

        type = "Truck Damage";
        binding.tvTitle.setText("Select Truck Damage");

        binding.stepView.setStepsNumber(3);
        binding.stepView.go(countPlus, true);

        binding.btnUnload.setOnClickListener(this);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            ArrayList<Transaction> arrData = new ArrayList<>();
            arrData.clear();
            arrData = db.getAllTransactions();
        }

    };


    private void setAdapter() {
        mAdapter = new CommonAdapter<Item>(arrAllItem) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemFreshunloadBinding) {
                    Item mItem = arrAllItem.get(position);
                    ((RowItemFreshunloadBinding) holder.binding).setItem(mItem);

                    if (mItem.getAltrUOM() != null) {
                        ((RowItemFreshunloadBinding) holder.binding).tvAlterUOM.setText(mItem.getAlterUOMName() + ": " + mItem.getAlterUOMQty());
                        if (mItem.getBaseUOM() != null) {
                            ((RowItemFreshunloadBinding) holder.binding).dividerUOM.setVisibility(View.VISIBLE);
                            ((RowItemFreshunloadBinding) holder.binding).tvBaseUOM.setVisibility(View.VISIBLE);
                            ((RowItemFreshunloadBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
                        } else {
                            ((RowItemFreshunloadBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                            ((RowItemFreshunloadBinding) holder.binding).tvBaseUOM.setVisibility(View.GONE);
                        }
                    } else {
                        ((RowItemFreshunloadBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                        ((RowItemFreshunloadBinding) holder.binding).tvAlterUOM.setVisibility(View.GONE);
                        ((RowItemFreshunloadBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
                    }

                    ((RowItemFreshunloadBinding) holder.binding).imageView2.setLetter(mItem.getItemName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowItemFreshunloadBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    ((RowItemFreshunloadBinding) holder.binding).chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                arrCheckItem.add(mItem.getItemId());
                            } else {
                                arrCheckItem.remove(mItem.getItemId());
                            }
                        }
                    });

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((RowItemFreshunloadBinding) holder.binding).chk.isChecked()) {
                                itemPosition = position;
                                makeDilog(FreshUnloadActivity.this, mItem);
                            } else {
                                UtilApp.displayAlert(FreshUnloadActivity.this, "Please select item first!");
                            }
                        }
                    });
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_freshunload;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUnload:
                if (!isClick) {
                    isClick = true;
                    new getNextView().execute();
                }
                break;
            default:
                break;
        }
    }

    private void makeDilog(final Context context, final Item item) {
        List<String> list = new ArrayList<String>();

        list.add("Select Reason");
        list.add("ROT");
        list.add("Truck Damage");
        list.add("Van Expiry");
        list.add("Theft/Variance");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        LayoutInflater dialog = LayoutInflater.from(context);
        DialogInputUnloadBinding dialogBinding = DataBindingUtil.inflate(dialog, R.layout.dialog_input_unload, null, false);
        final View dialogView = dialogBinding.getRoot();
        dialogBinding.spReason.setAdapter(dataAdapter);

        dialogBinding.llReason.setVisibility(View.GONE);
        dialogBinding.txtName.setText(item.getItemName());
        dialogBinding.edtSaleAlter.setText(item.getAlterUOMQty());
        dialogBinding.edtSaleBase.setText(item.getBaseUOMQty());

        final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.setView(dialogView);

        if (Integer.parseInt(arrAllItem.get(itemPosition).getBaseUOMQty()) > 0) {
            dialogBinding.edtSaleBase.setEnabled(false);
            dialogBinding.edtSaleBase.setClickable(false);
            dialogBinding.edtActBase.setEnabled(true);
            dialogBinding.edtActBase.setClickable(true);
            dialogBinding.edtActBase.setAlpha(1.0f);
            dialogBinding.edtSaleBase.setAlpha(0.5f);
        } else {
            dialogBinding.edtSaleBase.setEnabled(false);
            dialogBinding.edtSaleBase.setClickable(false);
            dialogBinding.edtActBase.setEnabled(false);
            dialogBinding.edtActBase.setClickable(false);
            dialogBinding.edtActBase.setAlpha(0.5f);
            dialogBinding.edtSaleBase.setAlpha(0.5f);
        }

        if (Integer.parseInt(arrAllItem.get(itemPosition).getAlterUOMQty()) > 0) {
            dialogBinding.edtSaleAlter.setEnabled(false);
            dialogBinding.edtSaleAlter.setClickable(false);
            dialogBinding.edtActAlter.setEnabled(true);
            dialogBinding.edtActAlter.setClickable(true);
            dialogBinding.edtActAlter.setAlpha(1.0f);
            dialogBinding.edtSaleAlter.setAlpha(0.5f);
        } else {
            dialogBinding.edtSaleAlter.setEnabled(false);
            dialogBinding.edtSaleAlter.setClickable(false);
            dialogBinding.edtActAlter.setEnabled(false);
            dialogBinding.edtActAlter.setClickable(false);
            dialogBinding.edtActAlter.setAlpha(0.5f);
            dialogBinding.edtSaleAlter.setAlpha(0.5f);
        }

        dialogBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String text = dialogBinding.spReason.getSelectedItem().toString();
//
//                if (text.equalsIgnoreCase("Select Reason")) {
//                    Toast.makeText(getApplicationContext(), "Please select reason", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                alterActQty = dialogBinding.edtActAlter.getText().toString().isEmpty() ? "0" : dialogBinding.edtActAlter.getText().toString();
                baseActQty = dialogBinding.edtActBase.getText().toString().isEmpty() ? "0" : dialogBinding.edtActBase.getText().toString();

                if (Integer.parseInt(alterActQty) > 0) {
                    int qty = Integer.parseInt(arrAllItem.get(itemPosition).getAlterUOMQty()) - Integer.parseInt(alterActQty);
                    alterActQty = String.valueOf(qty);
                }

                if (Integer.parseInt(baseActQty) > 0) {
                    int qty = Integer.parseInt(arrAllItem.get(itemPosition).getBaseUOMQty()) - Integer.parseInt(baseActQty);
                    baseActQty = String.valueOf(qty);
                }


                Item mItem = arrAllItem.get(itemPosition);
                mItem.setSaleAltQty(alterActQty);
                mItem.setSaleBaseQty(baseActQty);
//                mItem.setReasonCode(getReasonCode(dialogBinding.spReason.getSelectedItem().toString()));
                mItem.setReasonCode(getReasonCode(type));
                arrAllItem.set(itemPosition, mItem);
                mAdapter.notifyDataSetChanged();

                deleteDialog.dismiss();
                UtilApp.hideSoftKeyboard(FreshUnloadActivity.this);
            }
        });
        deleteDialog.show();
    }

    private String getReasonCode(String name) {
        String code = "0";

        switch (name) {
            case "ROT":
                code = "1";
                break;
            case "Truck Damage":
                code = "2";
                break;
            case "Van Expiry":
                code = "3";
                break;
            case "Theft/Variance":
                code = "4";
                break;
        }
        return code;

    }

    private void updateData(int countPlus) {
        switch (countPlus) {
            /*case 1:
                type = "Truck Damage";
                binding.tvTitle.setText("Select Truck Damage");
                break;*/
            case 1:
                type = "Van Expiry";
                binding.tvTitle.setText("Select Van Expiry");
                break;
            case 2:
                type = "Theft/Variance";
                binding.tvTitle.setText("Select Variance");
                binding.btnUnload.setImageResource(R.drawable.ic_check_white);
                break;
        }

        arrCheckItem = new ArrayList<>();
        arrAllItem = new ArrayList<>();
        arrAllItem = db.getAllVanStockItemList();

        setAdapter();

    }


    private class getNextView extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new LoadingSpinner(FreshUnloadActivity.this);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            if (arrCheckItem.size() > 0) {
                for (int i = 0; i < arrAllItem.size(); i++) {
                    if (arrCheckItem.contains(arrAllItem.get(i).getItemId())) {
                        //Insert into Variance
                        db.insertUnLoadVariance(arrAllItem.get(i));

                        String bsQty = arrAllItem.get(i).getSaleBaseQty().equals("0") ? arrAllItem.get(i).getBaseUOMQty() : arrAllItem.get(i).getSaleBaseQty();
                        String altQty = arrAllItem.get(i).getSaleAltQty().equals("0") ? arrAllItem.get(i).getAlterUOMQty() : arrAllItem.get(i).getSaleAltQty();

                        if (bsQty.equalsIgnoreCase(arrAllItem.get(i).getBaseUOMQty()) &&
                                altQty.equalsIgnoreCase(arrAllItem.get(i).getAlterUOMQty())) {
                            db.updateVanStockDeleteQty(arrAllItem.get(i).getItemId(), "0", "0", "Both");
                        } else {
                            int updateBsQty = Integer.parseInt(arrAllItem.get(i).getBaseUOMQty()) - Integer.parseInt(bsQty);
                            int updateAlQty = Integer.parseInt(arrAllItem.get(i).getAlterUOMQty()) - Integer.parseInt(altQty);
                            db.updateVanStockQty(arrAllItem.get(i).getItemId(), "" + updateBsQty, "" + updateAlQty, "Both");
                        }
                    }
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }

            if (countPlus == 2) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FreshUnloadActivity.this);
                alertDialogBuilder.setTitle("Alert")
                        .setMessage("Are you sure you want to Unload Inventory?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isUnloadCLick) {
                                    isUnloadCLick = true;
                                    new getAsyncTask().execute();
                                }

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialogBuilder.show();

            } else {
                isClick = false;
                countPlus++;
                binding.stepView.go(countPlus, true);
                updateData(countPlus);
            }
        }
    }

    private class getAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private LoadingSpinner mDialog;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (mDialog.isShowing())
                mDialog.hide();

            isClick = false;
            UtilApp.dialogPrint(FreshUnloadActivity.this, new OnSearchableDialog() {
                @Override
                public void onItemSelected(Object o) {
                    if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                        UtilApp.createBackgroundJob(getApplicationContext());
                    }
                    String selection = (String) o;
                    if (selection.equalsIgnoreCase("yes")) {
                        PrintLog object = new PrintLog(FreshUnloadActivity.this,
                                FreshUnloadActivity.this);
                        object.execute("", jsonArray);
                    } else {
                        finish();
                    }
                }
            });


        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            if (countPlus == 2) {
                arrAllItem = new ArrayList<>();
                arrAllItem = db.getAllVanStockItemList();

                for (int i = 0; i < arrAllItem.size(); i++) {

                    arrAllItem.get(i).setReasonCode("0");
                    String bsQty = arrAllItem.get(i).getSaleBaseQty().equals("0") ? arrAllItem.get(i).getBaseUOMQty() : arrAllItem.get(i).getSaleBaseQty();
                    String altQty = arrAllItem.get(i).getSaleAltQty().equals("0") ? arrAllItem.get(i).getAlterUOMQty() : arrAllItem.get(i).getSaleAltQty();

                    arrAllItem.get(i).setSaleBaseQty(bsQty);
                    arrAllItem.get(i).setSaleAltQty(altQty);

                    //Insert into Variance
                    db.insertUnLoadVariance(arrAllItem.get(i));

                    db.updateVanStockDeleteQty(arrAllItem.get(i).getItemId(), "0", "0", "Both");
                }
            }

            ArrayList<Item> arrItem = db.getBadReturnItems("Bad");
            for (int i = 0; i < arrItem.size(); i++) {

                Item mItem = arrItem.get(i);
                mItem.setSaleBaseQty(arrItem.get(i).getBaseUOMQty());
                mItem.setSaleAltQty(arrItem.get(i).getAlterUOMQty());
                mItem.setReasonCode("5");

                //Insert into Variance
                db.insertUnLoadVariance(arrItem.get(i));
            }

            //GENERATE NEXT INVOICE NUMBER
            String unLoadNum = UtilApp.getLastIndex("UnLoad");

            //Get EndInventory Data
            parseEndInventoryData();

            //get Print Data
            JSONObject data = null;
            try {
                jsonArray = createPrintData(unLoadNum);
                data = new JSONObject();
                data.put("data", (JSONArray) jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //INSERT TRANSACTION
            Transaction transaction = new Transaction();
            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_UNLOAD;
            transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
            transaction.tr_customer_num = "";
            transaction.tr_customer_name = "";
            transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
            transaction.tr_invoice_id = "";
            transaction.tr_order_id = unLoadNum;
            transaction.tr_collection_id = "";
            transaction.tr_pyament_id = "";
            transaction.tr_is_posted = "No";
            transaction.tr_printData = data.toString();
            db.insertTransaction(transaction);

            //store Last Invoice Number
            Settings.setString(App.UNLOAD_LAST, unLoadNum);

            Settings.setString(App.IS_ENDDAY, "1");

            if (db.getPostUnLoadRequestCount() == 0) {
                db.updateUnloadTransaction(unLoadNum, "1", "");
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new LoadingSpinner(FreshUnloadActivity.this);
            mDialog.show();
        }
    }

    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createPrintData(String invNum) {
        JSONArray jArr = new JSONArray();

        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.END_INVENTORY);
            Salesman mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm a"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));
            mainArr.put("ROUTENAME", mSalesman.getRouteName());
            mainArr.put("invoicenumber", invNum);  //Invoice No

            JSONArray jData = new JSONArray();
            for (UnloadSummary obj : arrEndData) {

                //Add Base UOM
                JSONArray data = new JSONArray();
                data.put(obj.getItemCode()); // ITEM CODE
                data.put(obj.getItemName());// DESC
                data.put(obj.getAlterUOM() + "/" + obj.getBaseUOM());// UOM
                data.put(obj.getAlterLoadQTy() + "/" + obj.getBaseLoadQty());//Load Qty
                data.put(obj.getAlterSaleQty() + "/" + obj.getBaseSaleQty());//Sale Qty
                data.put(obj.getAlterUnloadQty() + "/" + obj.getBaseUnloadQty());//UnLoadQty
                data.put(obj.getAlterBadQty() + "/" + obj.getBaseBadQty());//Bad Qty

                data.put(obj.getBaseLoadQty());
                data.put(obj.getAlterLoadQTy());

                data.put(obj.getBaseSaleQty());
                data.put(obj.getAlterSaleQty());

                data.put(obj.getBaseUnloadQty());
                data.put(obj.getAlterUnloadQty());

                data.put(obj.getBaseBadQty());
                data.put(obj.getAlterBadQty());
                data.put(obj.getPrice());
                jData.put(data);

            }

            mainArr.put("data", jData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jArr;
    }

    public void parseEndInventoryData() {
        arrEndData = new ArrayList<>();

        ArrayList<Item> arrVanItem = db.getDeleteVanStockItemList();

        for (Item obj : arrVanItem) {

            UnloadSummary mSummary = new UnloadSummary();
            mSummary.setItemId(obj.getItemId());
            mSummary.setItemCode(obj.getItemCode());
            mSummary.setItemName(obj.getItemName());
            mSummary.setAlterActQty(obj.getActualAltQty());
            mSummary.setBaseActQty(obj.getActuakBaseQty());
            mSummary.setBaseUOM(obj.getBaseUOMName());
            mSummary.setAlterUOM(obj.getAlterUOMName());

            int baseLoad = db.getLoadItemQty(obj.getItemId(), "Base");
            int alterLoad = db.getLoadItemQty(obj.getItemId(), "Alter");
            mSummary.setBaseLoadQty("" + baseLoad);
            mSummary.setAlterLoadQTy("" + alterLoad);

            int baseSaleQty = db.getSaleItemQty(obj.getItemId(), "Base");
            int alterSaleQty = db.getSaleItemQty(obj.getItemId(), "Alter");
            mSummary.setBaseSaleQty("" + baseSaleQty);
            mSummary.setAlterSaleQty("" + alterSaleQty);

            int baseBadQty = db.getBadVarianceItem(obj.getItemId(), "Base");
            int alterBadQty = db.getBadVarianceItem(obj.getItemId(), "Alter");
            mSummary.setBaseBadQty("" + baseBadQty);
            mSummary.setAlterBadQty("" + alterBadQty);

            int baseUnQty = db.getUnloadItemQty(obj.getItemId(), "Base");
            int alterUnQty = db.getUnloadItemQty(obj.getItemId(), "Alter");
            mSummary.setBaseUnloadQty("" + baseUnQty);
            mSummary.setAlterUnloadQty("" + alterUnQty);

            double baseSalePrice = db.getSaleItemPrice(obj.getItemId(), "Base");
            double alterSalePrice = db.getSaleItemPrice(obj.getItemId(), "Alter");
            double totalPrice = baseSalePrice + alterSalePrice;
            int mfinalPrice = (int) totalPrice;
            mSummary.setPrice("" + mfinalPrice);
            arrEndData.add(mSummary);
        }

        ArrayList<Item> arrBadItem = db.getVarianceItem("5");

        for (int i = 0; i < arrBadItem.size(); i++) {

            boolean isContain = false;
            for (int j = 0; j < arrEndData.size(); j++) {

                if (arrBadItem.get(i).getItemId().equalsIgnoreCase(arrEndData.get(j).getItemId())) {
//                    UnloadSummary msummary = arrEndData.get(j);
//
//                    int basQty = Integer.parseInt(arrEndData.get(j).getBaseBadQty()) + Integer.parseInt(arrBadItem.get(i).getBaseUOMQty());
//                    int alQty = Integer.parseInt(arrEndData.get(j).getAlterBadQty()) + Integer.parseInt(arrBadItem.get(i).getAlterUOMQty());
//
//                    msummary.setBaseBadQty("" + basQty);
//                    msummary.setAlterBadQty("" + alQty);
//
//                    arrEndData.set(j, msummary);
                    isContain = true;
                    break;
                }
            }

            if (!isContain) {
                Item obj = arrBadItem.get(i);
                UnloadSummary mSummary = new UnloadSummary();
                mSummary.setItemId(obj.getItemId());
                mSummary.setItemCode(obj.getItemCode());
                mSummary.setItemName(obj.getItemName());
                mSummary.setAlterActQty("0");
                mSummary.setBaseActQty("0");
                mSummary.setBaseUOM(obj.getBaseUOMName());
                mSummary.setAlterUOM(obj.getAlterUOMName());
                mSummary.setBaseLoadQty("0");
                mSummary.setAlterLoadQTy("0");
                mSummary.setBaseSaleQty("0");
                mSummary.setAlterSaleQty("0");
                mSummary.setBaseUnloadQty("0");
                mSummary.setAlterUnloadQty("0");
                mSummary.setPrice("0");

                int baseBadQty = db.getBadVarianceItem(obj.getItemId(), "Base");
                int alterBadQty = db.getBadVarianceItem(obj.getItemId(), "Alter");
                mSummary.setBaseBadQty("" + baseBadQty);
                mSummary.setAlterBadQty("" + alterBadQty);

                arrEndData.add(mSummary);
            }
        }
    }

    public void callback() {
        finish();
    }
}
