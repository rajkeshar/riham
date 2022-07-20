package com.mobiato.sfa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityMultiCollectionBinding;
import com.mobiato.sfa.databinding.RowItemMultiCollectionBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.CollectionData;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.JsonRpcUtil;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class MultiCollectionActivity extends BaseActivity implements View.OnClickListener {

    private ActivityMultiCollectionBinding binding;
    private ArrayList<CollectionData> arrData = new ArrayList<>();
    private CommonAdapter<CollectionData> mAdapter;
    private DBManager db;
    private Customer mCustomer;
    public double splitEnterAmount = 0.0;
    public double enterAmount = 0.0;
    public double TempenterAmount = 0.0;

    boolean isCashSplite = false;
    boolean isChecqueSplite = false;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiCollectionBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Collection");

        db = new DBManager(MultiCollectionActivity.this);

        if (getIntent() != null) {
            mCustomer = (Customer) getIntent().getSerializableExtra("customer");
        }

        setData();
        setAdapter();

        binding.edtCashAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                enterAmount = getcashamt();
                splitEnterAmount = getcashamt();
                NotifyAdapter();
            }
        });
        binding.edtChequeAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                enterAmount = getcheckamt();
                splitEnterAmount = getcheckamt();
                NotifyAdapter();
            }
        });

        binding.btnCash.setOnClickListener(this);
        binding.btnCheque.setOnClickListener(this);
        binding.fab.setOnClickListener(this);
    }

    private void setData() {
        arrData = new ArrayList<>();
        arrData = db.getAllCollection(mCustomer.getCustomerId());
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<CollectionData>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemMultiCollectionBinding) {
                    ((RowItemMultiCollectionBinding) holder.binding).setItem(arrData.get(position));

                    ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmount.setText(UtilApp.getNumberFormate(Math.round(Double.parseDouble(arrData.get(position).getAmountCleared()))));

                    Double due = (Double.parseDouble(arrData.get(position).getTempAmountDue()));
                    ((RowItemMultiCollectionBinding) holder.binding).tvAmtDue.setText("Amount Due" + " : " + UtilApp.getNumberFormate(Math.round(Double.parseDouble(String.format("%.2f", due)))));

                    if (arrData.get(position).isInvoiceSplite()) {
                        ((RowItemMultiCollectionBinding) holder.binding).cbSpliteCollection.setVisibility(View.VISIBLE);
                        ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setVisibility(View.VISIBLE);

                        if (arrData.get(position).isSpliteChecked()) {
                            ((RowItemMultiCollectionBinding) holder.binding).cbSpliteCollection.setChecked(true);
                            ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setEnabled(true);
                            if (arrData.get(position).getAmountEnter().equals("0"))
                                ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setHint(arrData.get(position).getAmountEnter());
                            else
                                ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setText(arrData.get(position).getAmountEnter());

                            final boolean[] isEdit = {false};
                            ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    //((CollectionsActivity) context).enterAmount(s.toString().trim().equals("")? "0": s.toString().trim(), pos);

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    isEdit[0] = true;
                                    arrData.get(position).setAmountEnter(s.toString().trim().equals("") ? "0" : s.toString().trim());
                                    Log.e("Save", " " + arrData.get(position).getAmountEnter());
                                }
                            });

                            ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    if (!b) {
                                        if (isEdit[0]) {
                                            if (Double.parseDouble(arrData.get(position).getInvoiceAmount()) < Double.parseDouble(arrData.get(position).getAmountEnter())) {
                                                Toast.makeText(MultiCollectionActivity.this, "You can not pay more than amount due", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Double due = (Double.parseDouble(arrData.get(position).getInvoiceAmount())) - Double.parseDouble(arrData.get(position).getAmountEnter());
                                                arrData.get(position).setTempAmountDue(String.format("%.2f", due));
                                                ((RowItemMultiCollectionBinding) holder.binding).tvAmtDue.setText("Amount Due" + " : " + UtilApp.getNumberFormate(Math.round(Double.parseDouble(String.format("%.2f", due)))));
                                                splitEnterAmount -= Double.parseDouble(arrData.get(position).getAmountEnter());
                                            }
                                        }
                                    }
                                }
                            });

                            ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                                        if (isEdit[0]) {
                                            if (Double.parseDouble(arrData.get(position).getInvoiceAmount()) < Double.parseDouble(arrData.get(position).getAmountEnter())) {
                                                Toast.makeText(MultiCollectionActivity.this, "You can not pay more than amount due", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Double due = (Double.parseDouble(arrData.get(position).getInvoiceAmount())) - Double.parseDouble(arrData.get(position).getAmountEnter());
                                                arrData.get(position).setTempAmountDue(String.format("%.2f", due));
                                                ((RowItemMultiCollectionBinding) holder.binding).tvAmtDue.setText("Amount Due" + " : " + UtilApp.getNumberFormate(Math.round(Double.parseDouble(String.format("%.2f", due)))));
                                                splitEnterAmount -= Double.parseDouble(arrData.get(position).getAmountEnter());
                                            }
                                        }
                                        return false;
                                    } else return false;
                                }
                            });

                        } else {
                            ((RowItemMultiCollectionBinding) holder.binding).cbSpliteCollection.setChecked(false);
                            arrData.get(position).setAmountEnter("0");
                            ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setText("");
                            ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setHint("0");
                            ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setEnabled(false);
                        }
                    } else {
                        ((RowItemMultiCollectionBinding) holder.binding).cbSpliteCollection.setVisibility(View.GONE);
                        ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setVisibility(View.GONE);
                        ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setEnabled(true);
                        ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setText("");
                        ((RowItemMultiCollectionBinding) holder.binding).tvInvoiceAmountEnter.setHint("0");
                        arrData.get(position).setAmountEnter("0");
                    }

                    ((RowItemMultiCollectionBinding) holder.binding).cbSpliteCollection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (arrData.get(position).isSpliteChecked()) {
                                clickCheckbox(false, position);

                                arrData.get(position).setTempAmountDue(arrData.get(position).getInvoiceAmount());
                                Double due = (Double.parseDouble(arrData.get(position).getInvoiceAmount()));
                                ((RowItemMultiCollectionBinding) holder.binding).tvAmtDue.setText("Amount Due" + " : " + String.format("%.2f", due));

                            } else {
                                clickCheckbox(true, position);
                            }
                        }
                    });

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_multi_collection;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCash:
                if (isCashSplite) {
                    isCashSplite = false;
                    binding.tvArrowDownCash.setImageResource(R.drawable.ic_keyboard_arrow_down);
                    binding.lytCash.setVisibility(View.GONE);
                    binding.edtCashAmt.setText("");
                } else {
                    isCashSplite = true;
                    binding.tvArrowDownCash.setImageResource(R.drawable.ic_keyboard_arrow_up);
                    binding.lytCash.setVisibility(View.VISIBLE);
                    isChecqueSplite = false;
                    binding.tvArrowDownCheque.setImageResource(R.drawable.ic_keyboard_arrow_down);
                    binding.lytCheque.setVisibility(View.GONE);

                    binding.edtChequeAmt.setText("");
                    binding.edtChequeNumber.setText("");
                }
                NotifyAdapter();

                break;
            case R.id.btnCheque:
                if (isChecqueSplite) {
                    isChecqueSplite = false;
                    binding.tvArrowDownCheque.setImageResource(R.drawable.ic_keyboard_arrow_down);
                    binding.lytCheque.setVisibility(View.GONE);

                    binding.edtChequeAmt.setText("");
                    binding.edtChequeNumber.setText("");
                } else {
                    isChecqueSplite = true;
                    binding.tvArrowDownCheque.setImageResource(R.drawable.ic_keyboard_arrow_up);
                    binding.lytCheque.setVisibility(View.VISIBLE);
                    isCashSplite = false;
                    binding.tvArrowDownCash.setImageResource(R.drawable.ic_keyboard_arrow_down);
                    binding.lytCash.setVisibility(View.GONE);

                    binding.edtCashAmt.setText("");
                }
                NotifyAdapter();
                break;
            case R.id.fab:
                if (isCashSplite || isChecqueSplite) {
                    TempenterAmount = 0.0;
                    boolean isProper = true;
                    for (int i = 0; i < arrData.size(); i++) {
                        if (arrData.get(i).isSpliteChecked()) {
                            TempenterAmount += Double.parseDouble(arrData.get(i).getAmountEnter());
                            if (Double.parseDouble(arrData.get(i).getInvoiceAmount()) < Double.parseDouble(arrData.get(i).getAmountEnter())) {
                                isProper = false;
                            }
                        }
                    }

                    if (!isProper) {
                        Toast.makeText(MultiCollectionActivity.this, "You can not pay more than amount due", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TempenterAmount != enterAmount) {
                        Toast.makeText(MultiCollectionActivity.this, "Enter amount not match.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String InvoiceNo = "";
                    final String PRNumber = UtilApp.getLastIndex("Collection");
                    for (int i = 0; i < arrData.size(); i++) {
                        if (arrData.get(i).isSpliteChecked()) {
                            if (InvoiceNo.equals("")) {
                                InvoiceNo = arrData.get(i).getInvoiceNo();
                            } else {
                                InvoiceNo = InvoiceNo + "," + arrData.get(i).getInvoiceNo();
                            }

                            CollectionData mCollData = db.getCollectionDetail(mCustomer.getCustomerId(), arrData.get(i).getInvoiceNo());
                            float prevAmount = 0;
                            float prevCashAmount = 0;
                            float prevCheqAmount = 0;
                            float prevCashAmountPRE = 0;
                            float prevCheqAmountPRE = 0;
                            String chequeNumber = "";
                            String chequeDate = "";
                            String bankName = "";
                            String bankCode = "";
                            String chequeAmount = "";

                            if (mCollData != null) {
                                prevAmount = Float.parseFloat(mCollData.getAmountCleared());
                                prevCashAmountPRE = Float.parseFloat(mCollData.getCashAmt());
                                prevCheqAmountPRE = Float.parseFloat(mCollData.getChequeAmt());

                            }
                            prevAmount += Float.parseFloat(arrData.get(i).getAmountEnter());

                            if (isCashSplite) {
                                prevCashAmount += Float.parseFloat(arrData.get(i).getAmountEnter());
                                prevCashAmountPRE += Float.parseFloat(arrData.get(i).getAmountEnter());
                            } else {
                                prevCheqAmount += Float.parseFloat(arrData.get(i).getAmountEnter());
                                prevCheqAmountPRE += Float.parseFloat(arrData.get(i).getAmountEnter());

                                if (getcheckamt() > 0) {
                                    chequeAmount = chequeAmount + "" + String.valueOf(getcheckamt());
                                    chequeNumber = chequeNumber + "" + binding.edtChequeNumber.getText().toString();
                                    chequeDate = chequeDate + "" + binding.edtChequeDate.getText().toString();
                                    bankCode = bankCode + "" + binding.edtBank.getText().toString();
                                    bankName = bankName + "" + binding.edtBank.getText().toString();
                                }
                            }

                            CollectionData mUpdateColl = new CollectionData();

                            String collType = isCashSplite == true ? "Cash" : "Cheque";
                            mUpdateColl.setCollType(collType);
                            mUpdateColl.setCustomerNo(mCustomer.getCustomerId());
                            mUpdateColl.setInvoiceNo(arrData.get(i).getInvoiceNo());
                            mUpdateColl.setOrderId(PRNumber);
                            mUpdateColl.setInvoiceAmount("" + (Float.parseFloat(arrData.get(i).getInvoiceAmount()) - Float.parseFloat(arrData.get(i).getAmountEnter())));
                            mUpdateColl.setAmountPay(arrData.get(i).getAmountEnter());
                            mUpdateColl.setIndicator(App.ADD_INDICATOR);
                            mUpdateColl.setAmountCleared(String.format("%.2f", prevAmount));
                            mUpdateColl.setChequeNo(chequeNumber);
                            mUpdateColl.setChequeDat(chequeDate);
                            mUpdateColl.setCashAmt(String.format("%.2f", prevCashAmount));
                            mUpdateColl.setChequeAmt(String.format("%.2f", prevCheqAmount));
                            mUpdateColl.setCashAmtPre(String.format("%.2f", prevCashAmountPRE));
                            mUpdateColl.setChequeAmtPre(String.format("%.2f", prevCheqAmountPRE));
                            mUpdateColl.setChequeAmtIndividule(chequeAmount);
                            mUpdateColl.setBankCode(bankCode);
                            mUpdateColl.setBankName(bankName);
                            mUpdateColl.setIsOutStand(mCollData.getIsOutStand());
                            if (Float.parseFloat(arrData.get(i).getAmountEnter()) == Float.parseFloat(arrData.get(i).getInvoiceAmount())) {
                                mUpdateColl.setIsInvoiceComplete(App.INVOICE_COMPLETE);
                            } else {
                                mUpdateColl.setIsInvoiceComplete(App.INVOICE_INCOMPLETE);
                            }

                            //Collection
                            db.updateCollection(mUpdateColl, arrData.get(i).getInvoiceNo(), mCustomer.getCustomerId());

                        }
                    }

                    //get Print Data
                    JSONObject data = null;
                    try {
                        jsonArray = createPrintData(InvoiceNo, PRNumber);
                        data = new JSONObject();
                        data.put("data", (JSONArray) jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //INSERT TRANSACTION
                    Transaction transaction = new Transaction();
                    transaction.tr_type = Constant.TRANSACTION_TYPES.TT_CASH_COLLECTION;
                    transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
                    transaction.tr_customer_num = mCustomer.getCustomerId();
                    transaction.tr_customer_name = mCustomer.getCustomerName();
                    transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
                    transaction.tr_invoice_id = InvoiceNo;
                    transaction.tr_order_id = "";
                    transaction.tr_collection_id = PRNumber;
                    transaction.tr_pyament_id = "";
                    transaction.tr_is_posted = "No";
                    transaction.tr_printData = data.toString();
                    db.insertTransaction(transaction);

                    //store Last Invoice Number
                    Settings.setString(App.COLLECTION_LAST, PRNumber);

                    UtilApp.dialogPrint(MultiCollectionActivity.this, new OnSearchableDialog() {
                        @Override
                        public void onItemSelected(Object o) {
                            if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                                UtilApp.createBackgroundJob(getApplicationContext());
                            }
                            String selection = (String) o;
                            if (selection.equalsIgnoreCase("yes")) {
                                PrintLog object = new PrintLog(MultiCollectionActivity.this,
                                        MultiCollectionActivity.this);
                                object.execute("", jsonArray);
                            } else {

                                Intent intent = new Intent(MultiCollectionActivity.this, CustomerDetailActivity.class);
                                intent.putExtra("custmer", mCustomer);
                                intent.putExtra("tag", "old");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    public void clickCheckbox(boolean isCheck, int position) {

        if (arrData.get(position).getIndicator().equals(App.ADD_INDICATOR)) {
            splitEnterAmount = getcashamt() + getcheckamt();
            for (int i = 0; i < arrData.size(); i++) {
                if (arrData.get(i).isSpliteChecked()) {
                    splitEnterAmount -= Double.parseDouble(arrData.get(i).getAmountEnter());
                }
            }
            if (splitEnterAmount > 0) {
                arrData.get(position).setSpliteChecked(isCheck);
                double amt = 0.0;
                if (splitEnterAmount > Double.parseDouble(arrData.get(position).getInvoiceAmount())) {
                    amt = Double.parseDouble(arrData.get(position).getInvoiceAmount());
                } else {
                    amt = splitEnterAmount;
                }
                if (isCheck) {
                    arrData.get(position).setAmountEnter(String.format("%.2f", amt));
                    Double due = (Double.parseDouble(arrData.get(position).getInvoiceAmount())) - Double.parseDouble(String.format("%.2f", amt));
                    arrData.get(position).setTempAmountDue(String.format("%.2f", due));
                } else {
                    arrData.get(position).setTempAmountDue(arrData.get(position).getInvoiceAmount());
                }

                binding.rvList.setAdapter(null);
                setAdapter();
                binding.rvList.getLayoutManager().scrollToPosition(position);
            } else {
                arrData.get(position).setSpliteChecked(false);

                binding.rvList.setAdapter(null);
                setAdapter();
                binding.rvList.getLayoutManager().scrollToPosition(position);
            }
        } else {
            Toast.makeText(MultiCollectionActivity.this, getString(R.string.debit_invoice), Toast.LENGTH_SHORT).show();
        }
    }

    public double getcashamt() {
        try {
            if (binding.edtCashAmt.getText().toString().trim().equals("")) {
                return 0;
            }
            return Double.parseDouble(binding.edtCashAmt.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getcheckamt() {
        try {
            if (binding.edtChequeAmt.getText().toString().trim().equals("")) {
                return 0;
            }
            return Double.parseDouble(binding.edtChequeAmt.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    private void NotifyAdapter() {
        if (isCashSplite || isChecqueSplite) {
            for (int i = 0; i < arrData.size(); i++) {
                arrData.get(i).setInvoiceSplite(true);
                arrData.get(i).setSpliteChecked(false);
                arrData.get(i).setAmountEnter("0");
                arrData.get(i).setTempAmountDue(arrData.get(i).getInvoiceAmount());
            }
        } else {
            for (int i = 0; i < arrData.size(); i++) {
                arrData.get(i).setInvoiceSplite(false);
                arrData.get(i).setSpliteChecked(false);
                arrData.get(i).setAmountEnter("0");
                arrData.get(i).setTempAmountDue(arrData.get(i).getInvoiceAmount());
            }
        }
        binding.rvList.setAdapter(null);
        setAdapter();
    }

    public void callback() {
        Intent intent = new Intent(MultiCollectionActivity.this, CustomerDetailActivity.class);
        intent.putExtra("custmer", mCustomer);
        intent.putExtra("tag", "old");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createPrintData(String invNum, String collNum) {
        JSONArray jArr = new JSONArray();

        try {
            int totalAmount = 0;
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.COLLECTION);

            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));
            mainArr.put("invoicenumber", invNum);  //Invoice No
            mainArr.put("ORDERNO", collNum);
            mainArr.put("invoicepaymentterms", "3");
            mainArr.put("DOCUMENT NO", collNum);

            mainArr.put("CUSTOMER", mCustomer.getCustomerName());
            mainArr.put("CUSTOMERID", mCustomer.getCustomerCode());
            mainArr.put("customertype", mCustomer.getCustType());
            mainArr.put("ADDRESS", "" + mCustomer.getAddress());
            mainArr.put("TRN", "");

            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();

            HEADERS.put("Invoice#");
            HEADERS.put("Due Date");
            HEADERS.put("Invoice Date");
            HEADERS.put("Due Amount");
            HEADERS.put("Invoice Balance");
            HEADERS.put("Amount Paid");

            mainArr.put("HEADERS", HEADERS);

            JSONObject jCash = new JSONObject();
            jCash.put("Amount", UtilApp.getNumberFormate(Math.round(Double.parseDouble(String.format("%.2f", getcashamt())))));
            mainArr.put("Cash", jCash);

            String paymentType = "";
            if (getcheckamt() > 0) {
                paymentType = "1";
            } else if (getcashamt() > 0) {
                paymentType = "0";
            }
            Log.e("Payment Type", "" + paymentType);
            mainArr.put("PaymentType", paymentType);

            JSONArray jCheque = new JSONArray();
            JSONObject jChequeData = new JSONObject();
            jChequeData.put("Cheque Date", binding.edtChequeDate.getText().toString());
            jChequeData.put("Cheque No", binding.edtChequeNumber.getText().toString());
            jChequeData.put("Bank", binding.edtBank.getText().toString());
            jChequeData.put("Amount", UtilApp.getNumberFormate(Math.round(Double.parseDouble(String.format("%.2f", getcheckamt())))));
            jCheque.put(jChequeData);
            mainArr.put("Cheque", jCheque);
            mainArr.put("expayment", "");

            JSONObject totalObj = new JSONObject();
            totalObj.put("Amount Paid", UtilApp.getNumberFormate(Math.round(Double.parseDouble(String.format("%.2f", getcashamt() + getcheckamt())))));
            totalObj.put("Remain Amt", "0");
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", totalObj);

            JSONArray jData = new JSONArray();
            for (CollectionData obj : arrData) {
                if (obj.isSpliteChecked()) {
                    JSONArray jData3 = new JSONArray();
                    jData3.put(obj.getInvoiceNo());
                    jData3.put(UtilApp.getTCDueDate(mCustomer.getPaymentTerm()));
                    jData3.put(obj.getInvoiceDate());
                    jData3.put(UtilApp.getNumberFormate(Math.round(Double.parseDouble(obj.getInvoiceAmount()))));
                    jData3.put(UtilApp.getNumberFormate(Math.round(Double.parseDouble(obj.getInvoiceAmount()) - (Double.parseDouble(obj.getAmountEnter())))));
                    jData3.put(UtilApp.getNumberFormate(Math.round(Double.parseDouble(obj.getAmountEnter()))));

                    jData.put(jData3);
                }
            }

            mainArr.put("data", jData);
            mainArr.put(JsonRpcUtil.PARAM_DATA, jData);

            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);

            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jArr;
    }
}
