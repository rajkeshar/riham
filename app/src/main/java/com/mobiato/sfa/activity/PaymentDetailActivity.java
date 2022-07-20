package com.mobiato.sfa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.databinding.ActivityPaymentDetailBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.merchandising.JourneyPlanActivity;
import com.mobiato.sfa.model.CollectionData;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Payment;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.JsonRpcUtil;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class PaymentDetailActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityPaymentDetailBinding binding;
    private String col_doc_no, invDate, dueDate, grandTotal, amt_cleared, invNo, invAMT, transactionType = "", enterAmt;
    private Customer mCustomer;
    private DBManager dbManager;
    private JSONArray jsonArray;

    String cust_type = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentDetailBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Payment Detail");

        dbManager = new DBManager(this);

        if (getIntent() != null) {
            mCustomer = (Customer) getIntent().getSerializableExtra("customer");
            col_doc_no = getIntent().getStringExtra("collNum");
            invDate = getIntent().getStringExtra("invDate");
            dueDate = getIntent().getStringExtra("dueDate");
            grandTotal = getIntent().getStringExtra("amount");
            invNo = getIntent().getStringExtra("invNo");
            amt_cleared = getIntent().getStringExtra("amtClear");
            invAMT = getIntent().getStringExtra("invAmt");

            try {
                cust_type = getIntent().getStringExtra("customer_type");
            } catch (Exception e) {
                cust_type = "0";
                e.toString();
            }
        }

        if (mCustomer.getCustType().equalsIgnoreCase("1") || mCustomer.getCustomerType().equalsIgnoreCase("7")) {
            binding.lytSwich.setVisibility(View.GONE);
            binding.lytCheque.setVisibility(View.GONE);
            binding.swcPayment.setChecked(false);
            binding.edtCashAmt.setEnabled(false);
            binding.edtCashAmt.setClickable(false);
            binding.edtCashAmt.setText(grandTotal);
        }


        //set Data
        binding.tvCustNo.setText(mCustomer.getCustomerCode());
        binding.tvCustName.setText(mCustomer.getCustomerName());
        binding.txtAmtDue.setText(UtilApp.getNumberFormate(Double.parseDouble(grandTotal)));
        binding.edtCashAmt.setText(grandTotal);

        binding.swcPayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    binding.lytCash.setVisibility(View.VISIBLE);
                    binding.lytCheque.setVisibility(View.GONE);
                } else {
                    binding.lytCash.setVisibility(View.GONE);
                    binding.lytCheque.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.edtChequeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        PaymentDetailActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Payment payment = new Payment();

                if (binding.swcPayment.isChecked()) {

                    //Cheque Payment
                    if (!binding.edtChequeAmt.getText().toString().isEmpty()) {

                        enterAmt = binding.edtChequeAmt.getText().toString();

                        payment.setInvoice_id(String.valueOf(invNo));
                        payment.setCollection_id(col_doc_no);
                        payment.setPayment_type("Cheque");
                        payment.setPayment_date(binding.edtChequeDate.getText().toString());
                        payment.setCheque_no(binding.edtChequeNumber.getText().toString());
                        payment.setBank_name(binding.edtBank.getText().toString());
                        payment.setPayment_amount(binding.edtChequeAmt.getText().toString());
                        payment.setCust_id(mCustomer.getCustomerId());

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                        return;
                    }


                } else {
                    //Cash Payment
                    if (!binding.edtCashAmt.getText().toString().isEmpty()) {

                        enterAmt = binding.edtCashAmt.getText().toString();

                        payment.setInvoice_id(String.valueOf(invNo));
                        payment.setCollection_id(col_doc_no);
                        payment.setPayment_type("Cash");
                        payment.setPayment_date(UtilApp.getCurrentDate());
                        payment.setCheque_no("");
                        payment.setBank_name("");
                        payment.setPayment_amount(binding.edtCashAmt.getText().toString());
                        payment.setCust_id(mCustomer.getCustomerId());

                        transactionType = Constant.TRANSACTION_TYPES.TT_CASH_COLLECTION;

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (Double.parseDouble(enterAmt) > Double.parseDouble(grandTotal)) {
                    Toast.makeText(PaymentDetailActivity.this, "Please don't pay more", Toast.LENGTH_SHORT).show();
                    return;
                }

                CollectionData mCollData = dbManager.getCollectionDetail(mCustomer.getCustomerId(), invNo);
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
                String PRNumber = "";

                if (mCollData != null) {
                    PRNumber = mCollData.getOrderId();
                    prevAmount = Float.parseFloat(mCollData.getAmountCleared());
                    prevCashAmountPRE = Float.parseFloat(mCollData.getCashAmt());
                    prevCheqAmountPRE = Float.parseFloat(mCollData.getChequeAmt());
                }

                prevAmount += Float.parseFloat(enterAmt);
                prevCashAmount += getcashamt();
                prevCheqAmount += getcheckamt();
                prevCashAmountPRE += getcashamt();
                prevCheqAmountPRE += getcheckamt();
                if (getcheckamt() > 0 || getcheckamt() < 0) {
                    chequeNumber = chequeNumber + "" + binding.edtChequeNumber.getText().toString();
                    chequeDate = chequeDate + "" + binding.edtChequeDate.getText().toString();
                    chequeAmount = chequeAmount + "" + String.valueOf(getcheckamt());
                    bankCode = bankCode + "" + "";
                    bankName = bankName + "" + binding.edtBank.getText().toString();
                }

                CollectionData mUpdateColl = new CollectionData();

                String collType = binding.swcPayment.isChecked() == true ? "Cheque" : "Cash";

                mUpdateColl.setCollType(collType);
                mUpdateColl.setCustomerNo(mCustomer.getCustomerId());
                mUpdateColl.setInvoiceNo(invNo);
                mUpdateColl.setOrderId(PRNumber);
                mUpdateColl.setInvoiceAmount("" + (Float.parseFloat(grandTotal) - Float.parseFloat(enterAmt)));
                mUpdateColl.setAmountPay(enterAmt);
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
                if (mCollData.getIsOutStand() == null) {
                    mUpdateColl.setIsOutStand("0");
                } else {
                    mUpdateColl.setIsOutStand(mCollData.getIsOutStand());
                }

                if (Float.parseFloat(enterAmt) == Float.parseFloat(grandTotal)) {
                    mUpdateColl.setIsInvoiceComplete(App.INVOICE_COMPLETE);
                } else {
                    mUpdateColl.setIsInvoiceComplete(App.INVOICE_INCOMPLETE);
                }

                //Collection
                dbManager.updateCollection(mUpdateColl, invNo, mCustomer.getCustomerId());


                //get Print Data
                JSONObject data = null;
                try {
                    jsonArray = createPrintData(invNo, col_doc_no);
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
                transaction.tr_invoice_id = invNo;
                transaction.tr_order_id = "";
                transaction.tr_collection_id = col_doc_no;
                transaction.tr_pyament_id = "";
                transaction.tr_is_posted = "No";
                transaction.tr_printData = data.toString();
                dbManager.insertTransaction(transaction);
                System.out.println("Id-->" + invNo + "--> " + col_doc_no);

                //insert into Payment
                dbManager.insertPayment(payment);

                //Update customer Transaction
                dbManager.updateCustomerTransaction(mCustomer.getCustomerId(), "collection");

                UtilApp.dialogPrint(PaymentDetailActivity.this, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                            UtilApp.createBackgroundJob(getApplicationContext());
                        }
                        String selection = (String) o;
                        if (selection.equalsIgnoreCase("yes")) {
                            PrintLog object = new PrintLog(PaymentDetailActivity.this,
                                    PaymentDetailActivity.this);
                            object.execute("", jsonArray);
                        } else {
//                            if(mCustomer.getCustomerType().equalsIgnoreCase("7")){
//                                UtilApp.confirmationDialog("Are you sure you want to add customer ?", PaymentDetailActivity.this, new OnSearchableDialog() {
//                                    @Override
//                                    public void onItemSelected(Object o) {
//                                        String selection = (String) o;
//                                        if (selection.equalsIgnoreCase("yes")) {
//                                            Settings.setString(App.INVOICE_CODE, invNo);
//                                            Intent in = new Intent(PaymentDetailActivity.this,AddNewCustomerActivity.class);
//                                            in.putExtra("Type","Sales");
//                                            in.putExtra("CusType","OTC");
//                                            startActivityForResult(in, 2);
//                                            finish();
//
//                                        }else {
//                                            Intent in = new Intent(PaymentDetailActivity.this, FragmentJourneyPlan.class);
//                                            startActivity(in);
//                                            finish();
//                                        }
//                                    }
//                                });
//                            }else {
                            if(cust_type.equals("OTC"))
                            {
                                Intent intent = new Intent(PaymentDetailActivity.this, FragmentJourneyPlan.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(PaymentDetailActivity.this, CustomerDetailActivity.class);
                                intent.putExtra("custmer", mCustomer);
                                intent.putExtra("tag", "old");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                            // }

                        }
                    }
                });

            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        binding.edtChequeDate.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
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
            totalObj.put("Remain Amt", String.format("%.2f", Double.parseDouble(grandTotal) - (getcashamt() + getcheckamt())));
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", totalObj);

            JSONArray jData = new JSONArray();
            JSONArray jData3 = new JSONArray();
            jData3.put(invNum);
            jData3.put(UtilApp.getTCDueDate(mCustomer.getPaymentTerm()));
            jData3.put(UtilApp.getCurrentDate());
            jData3.put(UtilApp.getNumberFormate(Math.round(Double.parseDouble(grandTotal))));
            jData3.put(UtilApp.getNumberFormate(Math.round(Double.parseDouble(String.format("%.2f", Double.parseDouble(grandTotal) - (getcashamt() + getcheckamt()))))));
            jData3.put(UtilApp.getNumberFormate(Math.round(Double.parseDouble(String.format("%.2f", getcashamt() + getcheckamt())))));
            jData.put(jData3);

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

    public double getcashamt() {
        try {
            if (binding.edtCashAmt.getText().toString().equals("")) {
                return 0;
            }
            return Double.parseDouble(binding.edtCashAmt.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getcheckamt() {
        try {
            if (binding.edtChequeAmt.getText().toString().equals("")) {
                return 0;
            }
            return Double.parseDouble(binding.edtChequeAmt.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public void callback() {
        Intent intent = new Intent(PaymentDetailActivity.this, CustomerDetailActivity.class);
        intent.putExtra("custmer", mCustomer);
        intent.putExtra("tag", "old");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(
                PaymentDetailActivity.this,
                "Please complete the transaction",
                Toast.LENGTH_SHORT).show();
    }
}
