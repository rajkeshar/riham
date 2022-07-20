package com.mobiato.sfa.activity;

import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityTransactionBinding;
import com.mobiato.sfa.databinding.RowItemTransactionPostingBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionActivity extends BaseActivity {

    private ActivityTransactionBinding binding;
    private String strCustId = "";
    private DBManager db;
    private ArrayList<Transaction> arrData = new ArrayList<>();
    private CommonAdapter<Transaction> mAdapter;
    private LoadingSpinner dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Transaction");

        strCustId = getIntent().getStringExtra("custId");

        db = new DBManager(me);
        dialog = new LoadingSpinner(this);
        arrData = new ArrayList<>();
        arrData = db.getCusTransactions(strCustId);

        setAdapter();

    }

    private void setAdapter() {

        mAdapter = new CommonAdapter<Transaction>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemTransactionPostingBinding) {
                    Transaction item = arrData.get(position);
                    switch (item.tr_type) {
                        case App.LoadConfirmation_TR:
                            ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("LC");
                            ((RowItemTransactionPostingBinding) holder.binding).tvTransaction.setText(item.tr_invoice_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_SALES_CREATED:
                            ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("SI");
                            ((RowItemTransactionPostingBinding) holder.binding).tvTransaction.setText(item.tr_invoice_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_RETURN_CREATED:
                            String returnType = db.getReturnType(item.tr_order_id);
                            if (returnType.equalsIgnoreCase("Bad")) {
                                ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("BR");
                            } else {
                                ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("GR");
                            }
                            ((RowItemTransactionPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_OREDER_CREATED:
                            ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("OR");
                            ((RowItemTransactionPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CASH_COLLECTION:
                            ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("COL");
                            ((RowItemTransactionPostingBinding) holder.binding).tvTransaction.setText(item.tr_collection_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CREDIT_COLLECTION:
                            ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("COL");
                            ((RowItemTransactionPostingBinding) holder.binding).tvTransaction.setText(item.tr_collection_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_LOAD_CREATE:
                            ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("LCR");
                            ((RowItemTransactionPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_UNLOAD:
                            ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("UL");
                            ((RowItemTransactionPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_EXCHANGE_CREATED:
                            ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("EXCH");
                            ((RowItemTransactionPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CUST_CREATED:
                            ((RowItemTransactionPostingBinding) holder.binding).ivPrint.setVisibility(View.GONE);
                            ((RowItemTransactionPostingBinding) holder.binding).tvType.setText("CUS");
                            ((RowItemTransactionPostingBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                    }

                    ((RowItemTransactionPostingBinding) holder.binding).ivPrint.setOnClickListener(new View.OnClickListener() {
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

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_transaction_posting;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    private void showPrintDialog(JSONArray jArr) {

        UtilApp.dialogPrint(TransactionActivity.this, new OnSearchableDialog() {
            @Override
            public void onItemSelected(Object o) {
                String selection = (String) o;
                if (selection.equalsIgnoreCase("yes")) {
                    if (jArr.length() > 0) {
                        dialog.show();
                        PrintLog object = new PrintLog(TransactionActivity.this,
                                TransactionActivity.this);
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
}
