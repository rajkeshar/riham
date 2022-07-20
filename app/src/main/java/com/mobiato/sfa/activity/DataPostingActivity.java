package com.mobiato.sfa.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityDataPostingBinding;
import com.mobiato.sfa.databinding.RowItemPostingBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.CollectionData;
import com.mobiato.sfa.model.ReturnOrder;
import com.mobiato.sfa.model.SalesSummary;
import com.mobiato.sfa.model.Transaction;
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

public class DataPostingActivity extends BaseActivity {

    public ActivityDataPostingBinding binding;
    private ArrayList<Transaction> arrData = new ArrayList<>();
    private CommonAdapter<Transaction> mAdapter;
    private DBManager db;
    private LoadingSpinner dialog;

    private ArrayList<CollectionData> dp_arrayList;
    private ArrayList<ReturnOrder> dp_arrayReturn;
    ArrayList<SalesSummary> cashSales = new ArrayList<>();
    ArrayList<SalesSummary> creditSales = new ArrayList<>();
    ArrayList<SalesSummary> creditNote = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataPostingBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Print Transactions");
        setNavigationView();

        UtilApp.logData(DataPostingActivity.this, "On Data Post activity");

        dialog = new LoadingSpinner(this);
        db = new DBManager(DataPostingActivity.this);
        arrData = new ArrayList<>();
        arrData = db.getAllTransactions();

        registerReceiver(broadcastReceiver, new IntentFilter(SyncData.BROADCAST_ACTION));

        //setData();
        setAdapter();

        if (arrData.size() > 0) {
            binding.btnSync.setVisibility(View.VISIBLE);
        }

        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {

        } else {
            binding.viewReport.setVisibility(View.GONE);
        }
        binding.imgSaleSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashSales = new ArrayList<>();
                creditSales = new ArrayList<>();

                ArrayList<SalesSummary> arrSummary = db.getSalesSummary();
                getSalesSummary(arrSummary);

                UtilApp.logData(DataPostingActivity.this, "Click On Sales Summary Cash Sales :" + cashSales.toString());
                UtilApp.logData(DataPostingActivity.this, "Click On Sales Summary Credit Sales :" + creditSales.toString());

                //show Print Dialog
                showPrintDialog(prepareSalesSummaryPrint());
            }
        });

        binding.imgDepositeSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp_arrayList = new ArrayList<>();
                dp_arrayReturn = new ArrayList<>();

                dp_arrayList = db.getCashCollection();
                dp_arrayReturn = db.getCreditReturn();

                UtilApp.logData(DataPostingActivity.this, "Click On Deposit Summary Cash Collection :" + dp_arrayList.toString());
                UtilApp.logData(DataPostingActivity.this, "Click On Deposit Summary Credit Return :" + dp_arrayReturn.toString());


                //show Print Dialog
                showPrintDialog(prepareDepositeSummaryPrint());
            }
        });

        binding.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (arrData.size() > 0) {
                    String type = "";
                    if (isChecked) {
                        type = "true";
                    } else {
                        type = "false";
                    }

                    for (int i = 0; i < arrData.size(); i++) {
                        arrData.get(i).setTr_isCheck(type);
                    }

                    setAdapter();
                }
            }
        });

        binding.btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < arrData.size(); i++) {
                    Transaction item = arrData.get(i);
                    if (item.getTr_isCheck().equalsIgnoreCase("true")) {
                        if (item.tr_is_posted.equalsIgnoreCase("Fail") ||
                                item.tr_is_posted.equalsIgnoreCase("No")) {
                            switch (item.tr_type) {
                                case App.LoadConfirmation_TR:
                                    db.markForLoadPost(item.tr_invoice_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_SALES_CREATED:
                                    db.markForInvoicePost(item.tr_invoice_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_RETURN_CREATED:
                                    db.markForReturnPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_OREDER_CREATED:
                                    db.markForOrderPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_LOAD_CREATE:
                                    db.markForLoadRequestPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_SALESMAN_LOAD_CREATED:
                                    db.markForSalesmanLoadRequestPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_CASH_COLLECTION:
                                    db.markForCollectionPost(item.tr_collection_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_CREDIT_COLLECTION:
                                    db.markForCollectionPost(item.tr_collection_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_EXCHANGE_CREATED:
                                    if (db.checkExchangeExist(item.tr_order_id)) {
                                        if (db.getInvoiceExchandePosted(item.tr_order_id)) {
                                            db.updateOrderTransaction(item.tr_order_id, "1", "");
                                        } else {
                                            db.markForExchangeInvoicePost(item.tr_order_id);
                                        }
                                        if (!db.getReturnExchandePosted(item.tr_order_id)) {
                                            db.markForExchangeReturnPost(item.tr_order_id);
                                        }
                                    } else {
                                        db.updateOrderTransaction(item.tr_order_id, "1", "");
                                    }
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_CREATED:
                                    db.markForCustomerPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_UPDATE:
                                    db.markForCustomerUpdatePost(item.tr_customer_num);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_ADD_DEPT_CUSTOEMR_UPDATE:
                                    db.markForDepotCustomerUpdatePost(item.tr_customer_num);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_CHILER_REQUEST:
                                    db.markForChillerRequestPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_FRIDGE_ADD:
                                    db.markForFridgePost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_CHILER_ADD:
                                    db.markForChillerAddPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_CHILER_TRACK:
                                    db.markForChillerTrackPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_SERVICE_VISIT:
                                    db.markForServiceVisitPost(item.tr_order_id);
                                    break;
                            }

                        }
                    }
                }

                if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                    UtilApp.createBackgroundJob(getApplicationContext());
                }
            }
        });
    }

    private void setAdapter() {

        mAdapter = new CommonAdapter<Transaction>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemPostingBinding) {
                    Transaction item = arrData.get(position);
                    switch (item.tr_type) {
                        case App.LoadConfirmation_TR:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("LC");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_invoice_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_SALES_CREATED:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("SI");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_invoice_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_RETURN_CREATED:
                            String returnType = db.getReturnType(item.tr_order_id);
                            if (returnType.equalsIgnoreCase("Bad")) {
                                ((RowItemPostingBinding) holder.binding).tvType.setText("BR");
                            } else {
                                ((RowItemPostingBinding) holder.binding).tvType.setText("GR");
                            }
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_OREDER_CREATED:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("OR");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CASH_COLLECTION:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("COL");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_collection_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CREDIT_COLLECTION:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("COL");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_collection_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_LOAD_CREATE:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("LCR");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_SALESMAN_LOAD_CREATED:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("SLC");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_UNLOAD:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("UL");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_EXCHANGE_CREATED:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("EXCH");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_CREATED:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("CUS");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            ((RowItemPostingBinding) holder.binding).ivPrint.setVisibility(View.GONE);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_UPDATE:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("CUS");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            ((RowItemPostingBinding) holder.binding).ivPrint.setVisibility(View.GONE);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_ADD_DEPT_CUSTOEMR_UPDATE:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("CUS");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            ((RowItemPostingBinding) holder.binding).ivPrint.setVisibility(View.GONE);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_SENSURY_SURVEY:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("SEN_SRV");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CHILER_REQUEST:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("CR");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            ((RowItemPostingBinding) holder.binding).ivPrint.setVisibility(View.GONE);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CHILER_TRANSFER:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("CT");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            ((RowItemPostingBinding) holder.binding).ivPrint.setVisibility(View.GONE);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CHILER_ADD:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("CA");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            ((RowItemPostingBinding) holder.binding).ivPrint.setVisibility(View.GONE);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_FRIDGE_ADD:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("CA");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            ((RowItemPostingBinding) holder.binding).ivPrint.setVisibility(View.GONE);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CHILER_TRACK:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("CA");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            ((RowItemPostingBinding) holder.binding).ivPrint.setVisibility(View.GONE);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_SERVICE_VISIT:
                            ((RowItemPostingBinding) holder.binding).tvType.setText("SV");
                            ((RowItemPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            ((RowItemPostingBinding) holder.binding).ivPrint.setVisibility(View.GONE);
                            break;
                    }

                    if (item.tr_is_posted.equalsIgnoreCase("Yes")) {
                        ((RowItemPostingBinding) holder.binding).ivSync.setImageResource(R.drawable.ic_icon_verified_sel);
                        ((RowItemPostingBinding) holder.binding).ivInfo.setVisibility(View.GONE);
                    } else if (item.tr_is_posted.equalsIgnoreCase("Fail")) {
                        ((RowItemPostingBinding) holder.binding).ivSync.setImageResource(R.drawable.ic_action_sync);
                        ((RowItemPostingBinding) holder.binding).ivInfo.setVisibility(View.VISIBLE);
                    } else {
                        ((RowItemPostingBinding) holder.binding).ivSync.setImageResource(R.drawable.ic_action_sync);
                        ((RowItemPostingBinding) holder.binding).ivInfo.setVisibility(View.GONE);
                    }

                    if (item.tr_isCheck.equalsIgnoreCase("true")) {
                        ((RowItemPostingBinding) holder.binding).chkRow.setChecked(true);
                    } else {
                        ((RowItemPostingBinding) holder.binding).chkRow.setChecked(false);
                    }

                    ((RowItemPostingBinding) holder.binding).chkRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrData.get(position).getTr_isCheck().equalsIgnoreCase("false")) {
                                ((RowItemPostingBinding) holder.binding).chkRow.setChecked(true);
                                arrData.get(position).setTr_isCheck("true");
                            } else {
                                ((RowItemPostingBinding) holder.binding).chkRow.setChecked(false);
                                arrData.get(position).setTr_isCheck("false");
                            }

                            notifyDataSetChanged();
                        }
                    });

                    /*((RowItemPostingBinding) holder.binding).chkRow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                arrData.get(position).setTr_isCheck("true");
                            } else {
                                arrData.get(position).setTr_isCheck("false");
                            }

                            runOnUiThread(new Runnable(){
                                public void run() {
                                    // UI code goes here
                                    notifyDataSetChanged();
                                }
                            });

                        }
                    });*/

                    ((RowItemPostingBinding) holder.binding).ivSync.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (item.tr_is_posted.equalsIgnoreCase("Fail") ||
                                    item.tr_is_posted.equalsIgnoreCase("No")) {
                                switch (item.tr_type) {
                                    case App.LoadConfirmation_TR:
                                        db.markForLoadPost(item.tr_invoice_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_SALES_CREATED:
                                        db.markForInvoicePost(item.tr_invoice_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_RETURN_CREATED:
                                        db.markForReturnPost(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_OREDER_CREATED:
                                        db.markForOrderPost(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_LOAD_CREATE:
                                        db.markForLoadRequestPost(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_SALESMAN_LOAD_CREATED:
                                        db.markForSalesmanLoadRequestPost(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_CASH_COLLECTION:
                                        db.markForCollectionPost(item.tr_collection_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_CREDIT_COLLECTION:
                                        db.markForCollectionPost(item.tr_collection_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_EXCHANGE_CREATED:
                                        if (db.checkExchangeExist(item.tr_order_id)) {
                                            if (db.getInvoiceExchandePosted(item.tr_order_id)) {
                                                db.updateOrderTransaction(item.tr_order_id, "1", "");
                                            } else {
                                                db.markForExchangeInvoicePost(item.tr_order_id);
                                            }
                                            if (!db.getReturnExchandePosted(item.tr_order_id)) {
                                                db.markForExchangeReturnPost(item.tr_order_id);
                                            }
                                        } else {
                                            db.updateOrderTransaction(item.tr_order_id, "1", "");
                                        }
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_UPDATE:
                                        db.markForCustomerUpdatePost(item.tr_customer_num);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_CREATED:
                                        db.markForCustomerPost(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_ADD_DEPT_CUSTOEMR_UPDATE:
                                        db.markForDepotCustomerUpdatePost(item.tr_customer_num);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_CHILER_REQUEST:
                                        db.markForChillerRequestPost(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_FRIDGE_ADD:
                                        db.markForFridgePost(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_CHILER_ADD:
                                        db.markForChillerAddPost(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_CHILER_TRACK:
                                        db.markForChillerTrackPost(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_SERVICE_VISIT:
                                        db.markForServiceVisitPost(item.tr_order_id);
                                        break;
                                }

                                if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                                    UtilApp.createBackgroundJob(getApplicationContext());
                                }
                            }
                        }
                    });


                    ((RowItemPostingBinding) holder.binding).ivPrint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONArray jsonArray = null;
                            try {
                                String jsonString = item.tr_printData;
                                jsonString = UtilApp.decodeString(jsonString.replaceAll("%", "123ABC"));
                                JSONObject jsonObject = new JSONObject(jsonString.replaceAll("123ABC", "%"));
                                jsonArray = jsonObject.getJSONArray("data");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            showPrintDialog(jsonArray);

                        }
                    });

                    ((RowItemPostingBinding) holder.binding).ivInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UtilApp.displayAlert(DataPostingActivity.this, item.tr_message);
                        }
                    });


                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_posting;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    private void showPrintDialog(JSONArray jArr) {

        UtilApp.dialogPrint(DataPostingActivity.this, new OnSearchableDialog() {
            @Override
            public void onItemSelected(Object o) {
                String selection = (String) o;
                if (selection.equalsIgnoreCase("yes")) {
                    if (jArr.length() > 0) {
                        dialog.show();
                        PrintLog object = new PrintLog(DataPostingActivity.this,
                                DataPostingActivity.this);
                        object.execute("", jArr);
                    }
                }
            }
        });
    }

    public void callback(String callingFunction) {
        if (dialog.isShowing()) {
            dialog.hide();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            arrData.clear();
            arrData = db.getAllTransactions();

            if (binding.rvList != null) {
                setAdapter();
            }

        }
    };

    private JSONArray prepareDepositeSummaryPrint() {
        JSONArray jArr = new JSONArray();
        try {
            float totalCash = 0;
            float totalCheque = 0;
            float totalCredit = 0;

            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.DEPOSITE_SUMMARY);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));

            JSONArray TOTAL = new JSONArray();
            JSONArray TOTALCredit = new JSONArray();

            JSONArray jData = new JSONArray();
            for (CollectionData item : dp_arrayList) {

                JSONArray jData1 = new JSONArray();
                int SINo = (jData.length() + 1);
                jData1.put(SINo + ""); // SI No.
                jData1.put(item.getOrderId());// Receipt No.
                jData1.put(item.getInvoiceNo());// Invoice. No
                jData1.put(item.getInvoiceDate());// Invoice. Date
                String code = db.getCustomerCode(item.getCustomerNo());
                if (code.isEmpty()) {
                    code = db.getDepotCustomerCode(item.getCustomerNo());
                }
                String name = db.getCustomerName(item.getCustomerNo());
                if (name.isEmpty()) {
                    name = db.getDepotCustomerName(item.getCustomerNo());
                }
                jData1.put(code);// Customer Code
                jData1.put(name);// Customer
                jData1.put(item.getChequeDat().equalsIgnoreCase("") ? "-" : item.getChequeDat());// Cheque Date
                jData1.put(UtilApp.getNumberFormate(Double.parseDouble(item.getChequeAmt())));// Cheque Amount
                jData1.put(UtilApp.getNumberFormate(Double.parseDouble(item.getCashAmt())));// Cash Amount
                totalCash += Double.parseDouble(item.getCashAmt() + "");
                totalCheque += Double.parseDouble(item.getChequeAmt() + "");
                jData.put(jData1);
            }

            JSONArray jDataCredit = new JSONArray();
            //All Returns
            for (ReturnOrder item : dp_arrayReturn) {
                JSONArray jData1 = new JSONArray();
                int SINo = (jDataCredit.length() + 1);
                jData1.put(SINo + ""); // SI No.
                jData1.put(item.orderNo);// Receipt No.
                jData1.put(item.orderNo);// Invoice. No
                jData1.put(item.orderDate);// Invoice. Date
                String code = db.getCustomerCode(item.cust_no);
                if (code.isEmpty()) {
                    code = db.getDepotCustomerCode(item.cust_no);
                }
                String name = db.getCustomerName(item.cust_no);
                if (name.isEmpty()) {
                    name = db.getDepotCustomerName(item.cust_no);
                }
                jData1.put(code);// Customer Code
                jData1.put(name);// Customer
                jData1.put("-");// Cheque Date
                jData1.put("-");// Cheque Amount
                jData1.put(UtilApp.getNumberFormate(Double.parseDouble(item.totalAmt)));// Cash Amount
                totalCredit += Double.parseDouble(item.totalAmt);
                jDataCredit.put(jData1);
            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("Cheque Amount", UtilApp.getNumberFormate(totalCheque));
            totalObj.put("Cash Amount", UtilApp.getNumberFormate(totalCash));  //Summation of all
            TOTAL.put(totalObj);

            JSONObject totalObjCredit = new JSONObject();
            totalObjCredit.put("Cash Amount", "" + UtilApp.getNumberFormate(totalCredit));
            TOTALCredit.put(totalObjCredit);
            mainArr.put("TOTAL", TOTAL);
            mainArr.put("TOTALCredit", TOTALCredit);
            mainArr.put("TOTAL DEPOSIT AMOUNT", UtilApp.getNumberFormate(totalCash + totalCheque));
            mainArr.put("TOTAL CASH AMOUNT", UtilApp.getNumberFormate(totalCash));
            mainArr.put("TOTAL CHEQUE AMOUNT", UtilApp.getNumberFormate(totalCheque));
            mainArr.put("TOTAL CREDIT AMOUNT", UtilApp.getNumberFormate(totalCredit));
            double grandTotal = ((totalCash + totalCheque) - totalCredit);
            mainArr.put("GRAND TOTAL", UtilApp.getNumberFormate(grandTotal));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DATA", jData);
            jsonObject.put("DATACredit", jDataCredit);
            jsonObject.put("TOTAL", totalObj);
            jsonObject.put("TOTALCredit", totalObjCredit);
            JSONArray jDataNew = new JSONArray();
            jDataNew.put(jsonObject);
            mainArr.put("data", jDataNew);
            //mainArr.put("data",jData);

            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jArr;
    }

    private JSONArray prepareSalesSummaryPrint() {
        JSONArray jArr = new JSONArray();
        try {

            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.SALES_SUMMARY);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));

            JSONArray TOTAL = new JSONArray();

            Double TotalCase = 0.0, TotalCasePaid = 0.0;
            Double TotalCredit = 0.0, TotalCreditPaid = 0.0;
            Double TotalCreditNote = 0.0;
            Double TotalSales = 0.0, TotalSalesPaid = 0.0;

            JSONArray cashData = new JSONArray();
            JSONArray tcData = new JSONArray();
            JSONArray creditNData = new JSONArray();
            for (SalesSummary mSummary : cashSales) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put("" + (cashData.length() + 1));
                jsonArray.put(mSummary.getTransactionNo()); // Invoice No
                jsonArray.put(mSummary.getCustomerNo());// Customer No
                jsonArray.put(mSummary.getCustomerName());// Customer Name
                double amt = Double.parseDouble(mSummary.getTotalSales());
                jsonArray.put("" + UtilApp.getNumberFormate(amt));// Net Sales
                jsonArray.put(mSummary.getDiscounts());// Discount
                double amtPaid = 0;
                if (mSummary.getAmountPaid() != null)
                    amtPaid = Double.parseDouble(mSummary.getAmountPaid());

                if (amtPaid == 0) {
                    jsonArray.put("0000");// AMt Paid
                } else {
                    jsonArray.put(UtilApp.getNumberFormate(amtPaid));// AMt Paid
                }


                TotalCasePaid += amtPaid;
                TotalCase += Double.parseDouble(String.format("%.2f", amt));
                cashData.put(jsonArray);
            }

            for (SalesSummary mSummary : creditSales) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put("" + (tcData.length() + 1));
                jsonArray.put(mSummary.getTransactionNo()); // Invoice No
                jsonArray.put(mSummary.getCustomerNo());// Customer No
                jsonArray.put(mSummary.getCustomerName());// Customer Name
                double amt = Double.parseDouble(mSummary.getTotalSales());
                jsonArray.put("" + UtilApp.getNumberFormate(amt));// Net Sales
                jsonArray.put(mSummary.getDiscounts());// Discount
                double amtPaid = 0;
                if (mSummary.getAmountPaid() != null)
                    amtPaid = Double.parseDouble(mSummary.getAmountPaid());

                if (amtPaid == 0) {
                    jsonArray.put("0000");// AMt Paid
                } else {
                    jsonArray.put(UtilApp.getNumberFormate(amtPaid));// AMt Paid
                }

                TotalCreditPaid += amtPaid;
                TotalCredit += Double.parseDouble(String.format("%.2f", amt));
                tcData.put(jsonArray);
            }

            for (SalesSummary mSummary : creditNote) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put("" + (creditNData.length() + 1));
                jsonArray.put(mSummary.getTransactionNo()); // Invoice No
                jsonArray.put(mSummary.getCustomerNo());// Customer No
                jsonArray.put(mSummary.getCustomerName());// Customer Name
                double amt = Double.parseDouble(mSummary.getTotalSales());
                jsonArray.put("" + UtilApp.getNumberFormate(amt));// Net Sales
                jsonArray.put(mSummary.getDiscounts());// Discount
                double amtPaid = 0;
                if (mSummary.getAmountPaid() != null)
                    amtPaid = Double.parseDouble(mSummary.getAmountPaid());

                if (amtPaid == 0) {
                    jsonArray.put("0000");// AMt Paid
                } else {
                    jsonArray.put(UtilApp.getNumberFormate(amtPaid));// AMt Paid
                }

                TotalCreditNote += Double.parseDouble(String.format("%.2f", amt));
                creditNData.put(jsonArray);
            }

            TotalSales = TotalCase + TotalCredit;
            TotalSalesPaid = TotalCasePaid + TotalCreditPaid;

            JSONArray jData = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DATA", jData);
            JSONObject totalObj = new JSONObject();
            totalObj.put("Total_Case", "" + UtilApp.getNumberFormate(TotalCase));
            totalObj.put("Total_Credit", "" + UtilApp.getNumberFormate(TotalCredit));
            totalObj.put("Total_Sales", "" + UtilApp.getNumberFormate(TotalSales));
            totalObj.put("Total_CasePaid", "" + UtilApp.getNumberFormate(TotalCasePaid));
            totalObj.put("Total_CreditPaid", "" + UtilApp.getNumberFormate(TotalCreditPaid));
            totalObj.put("Total_SalesPaid", "" + UtilApp.getNumberFormate(TotalSalesPaid));
            totalObj.put("Total_CreditReturn", "" + UtilApp.getNumberFormate(TotalCreditNote));
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            jsonObject.put("TOTAL", totalObj);
            JSONArray jDataNew = new JSONArray();
            jDataNew.put(jsonObject);
            mainArr.put("data", cashData);
            mainArr.put("tcData", tcData);
            mainArr.put("creditData", creditNData);

            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jArr;
    }

    private void getSalesSummary(ArrayList<SalesSummary> arrData) {

        for (SalesSummary mItem : arrData) {
            SalesSummary mSummary = new SalesSummary();

            mSummary.setTransactionNo(mItem.getTransactionNo());
            String code = db.getCustomerCode(mItem.getCustomerNo());
            if (code.isEmpty()) {
                code = db.getDepotCustomerCode(mItem.getCustomerNo());
            }
            mSummary.setCustomerNo(code);
            mSummary.setCustomerName(mItem.getCustomerName());

            if (mItem.getTransactionType().equalsIgnoreCase("SI")) {
                mSummary.setTransactionType(mItem.getTransactionType());
                mSummary.setTotalSales(mItem.getTotalSales());
                if (mItem.getDiscounts() != null) {
                    if (Double.parseDouble(mItem.getDiscounts()) > 0)
                        mSummary.setDiscounts(mItem.getDiscounts());
                    else
                        mSummary.setDiscounts("00");
                } else {
                    mSummary.setDiscounts("00");
                }

                ArrayList<CollectionData> custCollec = db.getCustomerCollection(mItem.getCustomerNo());

                for (int i = 0; i < custCollec.size(); i++) {
                    if (custCollec.get(i).getInvoiceNo().equalsIgnoreCase(mItem.getTransactionNo())) {
                        if (Double.parseDouble(custCollec.get(i).getAmountPay()) > 0)
                            mSummary.setAmountPaid(custCollec.get(i).getAmountPay());
                        else
                            mSummary.setAmountPaid("0000");
                    }
                }

                if (db.getCustomerType(mItem.getCustomerNo()).equalsIgnoreCase("1")) {
                    cashSales.add(mSummary);
                } else if (db.getCustomerType(mItem.getCustomerNo()).equalsIgnoreCase("2")) {
                    creditSales.add(mSummary);
                } else {
                    cashSales.add(mSummary);
                }
            } else {
                if (mItem.getTransactionType().equalsIgnoreCase("Bad")) {
                    mSummary.setTransactionType("BR");
                } else {
                    mSummary.setTransactionType("GR");
                }
                mSummary.setTotalSales(mItem.getTotalSales());
                mSummary.setDiscounts("00");
                mSummary.setAmountPaid("0000");
                creditNote.add(mSummary);
            }

        }
    }
}
