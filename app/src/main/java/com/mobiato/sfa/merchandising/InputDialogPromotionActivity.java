package com.mobiato.sfa.merchandising;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityInputDialogPromotionBinding;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.utils.UtilApp;

public class InputDialogPromotionActivity extends Activity implements View.OnClickListener {

    private ActivityInputDialogPromotionBinding binding;
    public Intent intent;
    public static final String BROADCAST_ACTION = "com.mobiato.sfa.DIALOG";
    public Item mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_dialog_promotion);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);

        getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        intent = new Intent(BROADCAST_ACTION);

        if (getIntent() != null) {
            mItem = (Item) getIntent().getSerializableExtra("item");
        }

        binding.txtName.setText(mItem.getItemName());


        //set OnClickListener
        binding.btnConfirm.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                sendConfirmData();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void sendConfirmData() {
        if (binding.edtSaleAlter.getText().toString().equalsIgnoreCase("")) {

            Toast.makeText(InputDialogPromotionActivity.this,
                    "Please input qty", Toast.LENGTH_SHORT).show();
            return;
        } else if (binding.edtSaleAlter.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(InputDialogPromotionActivity.this,
                    "Please input qty", Toast.LENGTH_SHORT).show();
            return;
        }

        mItem.setQty(binding.edtSaleAlter.getText().toString());
        mItem.setPrice(binding.edtSaleAmout.getText().toString());

        intent.putExtra("item", mItem);
        sendBroadcast(intent);
        finish();
        UtilApp.hideSoftKeyboard(InputDialogPromotionActivity.this);
    }
}
