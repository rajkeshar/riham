package com.mobiato.sfa.activity;

import android.os.Bundle;

import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.databinding.ActivityPaymentBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Payment;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;

public class PaymentActivity extends BaseActivity {

    public ActivityPaymentBinding binding;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Payment");
        setNavigationView();

        db = new DBManager(PaymentActivity.this);

        ArrayList<Payment> payments = db.getAllPaymentToday();

        double due_amt = 0.0, cheque = 0.0, cash = 0.0;

        for (int i = 0; i < payments.size(); i++) {
            due_amt += Double.parseDouble(payments.get(i).getPayment_amount());

            if (payments.get(i).getPayment_type().equals("Cash")) {
                cash += Double.parseDouble(payments.get(i).getPayment_amount());
            }

            if (payments.get(i).getPayment_type().equals("Cheque")) {
                cheque += Double.parseDouble(payments.get(i).getPayment_amount());
            }
        }

        binding.txtAmtDue.setText(UtilApp.getNumberFormate(Math.round(due_amt)));

        binding.txtCash.setText(UtilApp.getNumberFormate(Math.round(cash)));
        binding.txtCheque.setText(UtilApp.getNumberFormate(Math.round(cheque)));

        double total = cash + cheque;
        binding.txtTotal.setText("" + UtilApp.getNumberFormate(Math.round(total)));

    }
}
