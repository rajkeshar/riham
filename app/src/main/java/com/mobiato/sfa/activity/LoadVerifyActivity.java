package com.mobiato.sfa.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityLoadVerifyBinding;
import com.mobiato.sfa.databinding.RowItemLoadVerifyBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Load;
import com.mobiato.sfa.model.Pricing;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;

public class LoadVerifyActivity extends BaseActivity {

    private ActivityLoadVerifyBinding binding;
    private ArrayList<Item> arrItem = new ArrayList<>();
    private CommonAdapter<Item> mAdapter;
    private Load mLoad;
    private DBManager db;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;

    private LoadingSpinner progressDialog;
    public String strDigitSign = "";
    public Bitmap bmpSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadVerifyBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Load Verify");

        UtilApp.logData(LoadVerifyActivity.this, "Load Verify OnScreen");

        mLoad = (Load) getIntent().getSerializableExtra("Load");
        db = new DBManager(this);

        progressDialog = new LoadingSpinner(LoadVerifyActivity.this);

        arrItem = db.getLoadItemList(mLoad.getSub_loadId());
        setAdapter();

        String total = db.getLoadAmount(mLoad.getSub_loadId());
        //binding.tvAmount.setText(UtilApp.getNumberFormate(Double.parseDouble(String.format("%1$.2f", DecimalUtils.round(total, 2)))) + " UGX");
        binding.tvAmount.setText(total);

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilApp.logData(LoadVerifyActivity.this, "OnCLick Load Verify Button");

                if (db.checkIfAgentPriceExists()) {

                    String totalCount = db.getRoutePriceCount(Settings.getString(App.ROUTEID));
                    String agentPrice = db.getAgentPriceCount();

                    if (Integer.parseInt(agentPrice) < Integer.parseInt(totalCount)) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadVerifyActivity.this);
                        alertDialogBuilder.setTitle("Alert")
                                .setMessage("Pricing data not downloaded. Please Goto Setting Master and Download Agent Pricing!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        getAgentPrice();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    } else {

                        if (db.checkIfDepotCustomerExists()) {

                            String totalCusCount = db.getDepotCustomerCount(Settings.getString(App.DEPOTID));
                            String depotPrice = db.getDepotCusCount();

                            if (Integer.parseInt(depotPrice) < Integer.parseInt(totalCusCount)) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadVerifyActivity.this);
                                alertDialogBuilder.setTitle("Alert")
                                        .setMessage("Customer data not downloaded. Please Goto Setting Master and Download Customer Master!")
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
                                showCustomerSignCapture();
                            }

                        } else {
                            UtilApp.displayAlert(LoadVerifyActivity.this, "Customer data not downloaded. Please Goto Setting Master and download Customer Master");
                        }
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadVerifyActivity.this);
                    alertDialogBuilder.setTitle("Alert")
                            .setMessage("Pricing data not downloaded. Please Goto Setting Master and Download Agent Pricing!")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getAgentPrice();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            }
        });
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Item>(arrItem) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {

                if (holder.binding instanceof RowItemLoadVerifyBinding) {
                    Item mItem = arrItem.get(position);
                    ((RowItemLoadVerifyBinding) holder.binding).setItem(mItem);

                    if (mItem.getAltrUOM() != null) {
                        ((RowItemLoadVerifyBinding) holder.binding).tvAlterUOM.setText(mItem.getAlterUOMName() + ": " + mItem.getAlterUOMQty());
                        if (mItem.getBaseUOM() != null) {
                            ((RowItemLoadVerifyBinding) holder.binding).dividerUOM.setVisibility(View.VISIBLE);
                            ((RowItemLoadVerifyBinding) holder.binding).tvBaseUOM.setVisibility(View.VISIBLE);
                            ((RowItemLoadVerifyBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
                        } else {
                            ((RowItemLoadVerifyBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                            ((RowItemLoadVerifyBinding) holder.binding).tvBaseUOM.setVisibility(View.GONE);
                        }
                    } else {
                        ((RowItemLoadVerifyBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                        ((RowItemLoadVerifyBinding) holder.binding).tvAlterUOM.setVisibility(View.GONE);
                        ((RowItemLoadVerifyBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
                    }

//                    if (mItem.getAlterUOMPrice() != null) {
//                        double alterPrice = (Double.parseDouble(mItem.getAlterUOMPrice()) * Integer.parseInt(mItem.getAlterUOMQty()));
//                        ((RowItemLoadVerifyBinding) holder.binding).tvAlterUOMPrice.setText("Price: " + UtilApp.getNumberFormate(alterPrice) + " UGX");
//                        if (mItem.getBaseUOMPrice() != null) {
//                            double BasePrice = (Double.parseDouble(mItem.getBaseUOMPrice()) * Integer.parseInt(mItem.getBaseUOMQty()));
//                            ((RowItemLoadVerifyBinding) holder.binding).dividerPrice.setVisibility(View.VISIBLE);
//                            ((RowItemLoadVerifyBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.VISIBLE);
//                            ((RowItemLoadVerifyBinding) holder.binding).tvBaseUOMPrice.setText(UtilApp.getNumberFormate(BasePrice) + " UGX");
//                        } else {
//                            ((RowItemLoadVerifyBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
//                            ((RowItemLoadVerifyBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.GONE);
//                        }
//                    } else {
//                        double BasePrice = (Double.parseDouble(mItem.getBaseUOMPrice()) * Integer.parseInt(mItem.getBaseUOMQty()));
//                        ((RowItemLoadVerifyBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
//                        ((RowItemLoadVerifyBinding) holder.binding).tvAlterUOMPrice.setVisibility(View.GONE);
//                        ((RowItemLoadVerifyBinding) holder.binding).tvBaseUOMPrice.setText("Price: " + UtilApp.getNumberFormate(BasePrice) + " UGX");
//                    }

                    ((RowItemLoadVerifyBinding) holder.binding).imageView2.setLetter(mItem.getItemName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowItemLoadVerifyBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_load_verify;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    /*****************************************************************************************
     @ Callback function once the printing is completed
     ****************************************************************************************/
    public void callbackFunction() {
        if (!db.checkIfLoadExists()) {
            Settings.setString(App.IS_LOAD_VERIFY, "1");
            Intent in = new Intent(LoadVerifyActivity.this, FragmentJourneyPlan.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
            finish();
        } else {
            finish();
        }
    }

    private class addItemToVanStock extends AsyncTask<String, Void, Void> {

        JSONArray jsonArray;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new LoadingSpinner(LoadVerifyActivity.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //Save Signature
            saveImage(bmpSign);

            //Insert to vanStock
            db.insertVanStockItem(arrItem, mLoad);
            db.updateLoadVerified(mLoad.getSub_loadId(), strDigitSign);

            //get Print Data
            JSONObject data = null;
            try {
                jsonArray = createPrintData(mLoad.getDel_date(), mLoad.getLoad_no());
                data = new JSONObject();
                data.put("data", (JSONArray) jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Add Transaction Data
            Transaction transaction = new Transaction();
            transaction.tr_type = App.LoadConfirmation_TR;
            transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
            transaction.tr_customer_num = "";
            transaction.tr_customer_name = "";
            transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
            transaction.tr_invoice_id = mLoad.getSub_loadId();
            transaction.tr_order_id = "";
            transaction.tr_collection_id = "";
            transaction.tr_pyament_id = "";
            transaction.tr_is_posted = "No";
            transaction.tr_printData = data.toString();

            db.insertTransaction(transaction);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }

            Settings.setString(App.IS_LOAD_VERIFY, "1");
            UtilApp.dialogPrint(LoadVerifyActivity.this, new OnSearchableDialog() {
                @Override
                public void onItemSelected(Object o) {
                    if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                        UtilApp.createBackgroundJob(getApplicationContext());
                    }
                    String selection = (String) o;
                    if (selection.equalsIgnoreCase("yes")) {
                        PrintLog object = new PrintLog(LoadVerifyActivity.this,
                                LoadVerifyActivity.this);
                        object.execute("", jsonArray);
                    } else {
                        if (!db.checkIfLoadExists()) {
                            Settings.setString(App.IS_LOAD_VERIFY, "1");
                            Intent in = new Intent(LoadVerifyActivity.this, FragmentJourneyPlan.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                            finish();
                        } else {
                            finish();
                        }
                    }
                }
            });

        }
    }


    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createPrintData(String loadDate, String loadNo) {
        JSONArray jArr = new JSONArray();
        try {
            int totalAmount = 0;
            int totalBsQty = 0, totalAltQty = 0;
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.LOAD_SUMMARY_REQUEST);

            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));
            mainArr.put("DOCUMENT NO", loadNo);  //Load Summary No
            mainArr.put("Load Number", loadNo);
            mainArr.put("signature", strDigitSign);

            JSONArray TOTAL = new JSONArray();
            JSONArray jData = new JSONArray();

            for (int i = 0; i < arrItem.size(); i++) {
                Item mItem = arrItem.get(i);

                JSONArray data = new JSONArray();
                data.put(mItem.getItemCode());//Item Code
                data.put(mItem.getItemName());// Item Name
                data.put(mItem.getBaseUOMQty());//Item Base Qty
                data.put(mItem.getBaseUOMName());//Base UOM
                data.put(mItem.getAlterUOMQty());//Item Alter Qty
                data.put(mItem.getAlterUOMName());//Alter UOM
                data.put("0");//Variance

                totalBsQty += Integer.parseInt(mItem.getBaseUOMQty());
                totalAltQty += Integer.parseInt(mItem.getAlterUOMQty());

//                int UPC = Integer.parseInt(db.getItemUPC(mItem.getItemId()));
//                int alterUPC = Integer.parseInt(mItem.getAlterUOMQty()) * UPC;
//                int Qty = Integer.parseInt(mItem.getBaseUOMQty()) + alterUPC;
//                double amt = (Qty * db.getItemPrice(mItem.getItemId()));
//                totalAmount += amt;
                jData.put(data);

            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("Total Amount", String.valueOf(totalAmount));
            totalObj.put("BaseQty", String.valueOf(totalBsQty));
            totalObj.put("AlterQty", String.valueOf(totalAltQty));
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

    private void showCustomerSignCapture() {
        final Dialog dialog = new Dialog(LoadVerifyActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cust_sign);

        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        int width;
        width = manager.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
        Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        final SignaturePad viewSign = dialog.findViewById(R.id.signature);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (!viewSign.isEmpty()) {
                    bmpSign = viewSign.getSignatureBitmap();

                    new addItemToVanStock().execute();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadVerifyActivity.this);
                    alertDialogBuilder
                            .setMessage("Please add signature before proceed!")
                            .setCancelable(false)
                            .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
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
            }
        });

    }

    /**
     * save the signature to an sd card directory
     *
     * @param signature bitmap
     */
    final void saveImage(Bitmap signature) {

        String root = Environment.getExternalStorageDirectory().toString();

        // the directory where the signature will be saved
        File myDir = new File(root + "/saved_signature");

        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // set the file name of your choice
        String fname = "sa_signature.png";

        // in our case, we delete the previous file, you can remove this
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }

        //Store Path
        strDigitSign = file.getAbsolutePath();

        try {

            // save the signature
            FileOutputStream out = new FileOutputStream(file);
            signature.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAgentPrice() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getPricingList(App.PRICING_ROUTE_LIST, Settings.getString(App.ROUTEID), Settings.getString(App.AGENTID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Price Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(LoadVerifyActivity.this, "Price Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Pricing> arrPricing = new ArrayList<>();
                        arrPricing = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Pricing>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_AGENT_PRICE_COUNT);
                        db.insertAgentPricingCount(arrPricing, Settings.getString(App.ROUTEID));
                        db.deleteTable(db.TABLE_AGENT_PRICING);
                        db.insertAgentPricingItems(arrPricing, Settings.getString(App.ROUTEID));
                    }
                }

                progressDialog.hide();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Price Fail", error.getMessage());
                progressDialog.hide();
                UtilApp.logData(LoadVerifyActivity.this, "Price Failure: " + error.getMessage());
            }
        });
    }

}
