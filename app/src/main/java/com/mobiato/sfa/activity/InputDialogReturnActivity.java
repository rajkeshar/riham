package com.mobiato.sfa.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityInputDialogReturnBinding;
import com.mobiato.sfa.databinding.RowRecentCustBinding;
import com.mobiato.sfa.databinding.RowUomDialogItemBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.UOM;
import com.mobiato.sfa.utils.DecimalUtils;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InputDialogReturnActivity extends Activity implements View.OnClickListener {

    public ActivityInputDialogReturnBinding binding;
    private DBManager db;
    public Item mItem;
    private String custId = "", returnType = "", routeId = "", baseUOM, alterUOM, mExpDate = "";
    private Customer mCustomer;
    public double itemBasePrice = 0, itemAlterPrice = 0, itemUOMPrice = 0, discount = 0, discountAmt = 0,
            discountAl = 0, discountAlAmt = 0;
    int mDay, mMonth, mYear;

    public Intent intent;
    public static final String BROADCAST_ACTION = "com.mobiato.sfa.REDIALOG";
    public ArrayList<UOM> mSelectUOMList = new ArrayList<>();
    private CommonAdapter<UOM> mRecentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_dialog_return);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);
        getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        intent = new Intent(BROADCAST_ACTION);

        db = new DBManager(this);

        if (getIntent() != null) {

            mItem = (Item) getIntent().getSerializableExtra("item");

            custId = getIntent().getStringExtra("customer");
            if (db.checkIsCustomerExist(custId)) {
                mCustomer = db.getCustomerDetail(custId);
            } else {
                mCustomer = db.getDepotCustomerDetail(custId);
            }

            routeId = mCustomer.getRouteId();
            returnType = getIntent().getStringExtra("returnType");
        }

        if (db.isPricingItems(custId, mItem.getItemId())) {
            Item pItem = db.getCustPricingItems(custId, mItem.getItemId());
            itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
            itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
        } else if (db.isAgentPricingItems(mItem.getItemId(), routeId)) {
            Item pItem = db.getAgentPricingItems(mItem.getItemId());
            itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
            itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
        } else {
            itemBasePrice = db.getItemPrice(mItem.getItemId());
            itemAlterPrice = db.getItemAlterPrice(mItem.getItemId());
        }

        binding.txtName.setText(mItem.getItemName());
        itemUOMPrice = db.getItemPrice(mItem.getItemId());
        baseUOM = mItem.getBaseUOM();
        alterUOM = mItem.getAltrUOM();
//        binding.edtSaleAlter.setHint(db.getUOM(alterUOM));
//        binding.edtSaleBase.setHint(db.getUOM(baseUOM));

        List<String> listUOM = new ArrayList<String>();
        listUOM.add("Select UOM");
        listUOM.add(db.getUOM(baseUOM));
        listUOM.add(db.getUOM(alterUOM));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, listUOM);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spUom.setAdapter(dataAdapter);

        List<String> list = new ArrayList<String>();
        list.add("Select Reason");
        if (returnType.equalsIgnoreCase("Bad")) {
            list.add("Damage");
            list.add("Expired");
            list.add("Packing Issue");
        } else {
            list.add("Short Expiry");
            list.add("Non moving product");
            list.add("Product Replacement");
        }

        ArrayAdapter<String> dataRSAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        dataRSAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spReason.setAdapter(dataRSAdapter);

        mSelectUOMList = new ArrayList<>();
        mSelectUOMList = mItem.getArrSelectUOM();
        setUpRecycler();

        binding.btnConfirm.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
        binding.llAddMore.setOnClickListener(this);
        binding.edtExpiryDate.setOnClickListener(this);
    }

    private void setUpRecycler() {

        if (mSelectUOMList.size() > 0) {
            binding.llUomList.setVisibility(View.VISIBLE);
        } else {
            binding.llUomList.setVisibility(View.GONE);
        }

        mRecentAdapter = new CommonAdapter<UOM>(mSelectUOMList) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowUomDialogItemBinding) {
                    ((RowUomDialogItemBinding) holder.binding).setItem(mSelectUOMList.get(position));

                    ((RowUomDialogItemBinding) holder.binding).tvUom.setText(mSelectUOMList.get(position).getUomName());
                    ((RowUomDialogItemBinding) holder.binding).tvQty.setText(mSelectUOMList.get(position).getUomQty());
                    ((RowUomDialogItemBinding) holder.binding).tvPrice.setText(mSelectUOMList.get(position).getUomExp());


                    ((RowUomDialogItemBinding) holder.binding).btnEdit.setVisibility(View.GONE);
                    ((RowUomDialogItemBinding) holder.binding).btnRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mSelectUOMList.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                    holder.binding.executePendingBindings();
                }

            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_uom_dialog_item;
            }
        };

        binding.rvQtyList.setAdapter(mRecentAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:

                if (mSelectUOMList.size() > 0) {

                    String reasonCode = "", rreasonType = "";
                    int mAlterQty = 0, baseQtyy = 0;
                    for (int i = 0; i < mSelectUOMList.size(); i++) {

                        if (mSelectUOMList.get(i).getUomType().equals("Alter")) {
                            mAlterQty = mAlterQty + Integer.parseInt(mSelectUOMList.get(i).getUomQty());
                        } else {
                            baseQtyy = baseQtyy + Integer.parseInt(mSelectUOMList.get(i).getUomQty());
                        }
                        reasonCode = mSelectUOMList.get(i).getUomreasonCode();
                        rreasonType = mSelectUOMList.get(i).getUomReturnnType();
                    }

                    double alterSellPrice = 0, baseSellPrice = 0;
                    if (mAlterQty > 0) {
                        alterSellPrice = mAlterQty * itemAlterPrice;
                    }

                    if (baseQtyy > 0) {
                        baseSellPrice = baseQtyy * itemBasePrice;
                    }

                    double final_price = alterSellPrice + baseSellPrice;

                    mItem.setSaleBaseQty("" + baseQtyy);
                    mItem.setSaleBasePrice("" + baseSellPrice);
                    mItem.setSaleAltQty("" + mAlterQty);
                    mItem.setSaleAltPrice("" + alterSellPrice);
                    mItem.setUOMPrice("" + (itemBasePrice + discountAmt));
                    mItem.setAlterUOMPrice("" + (itemAlterPrice + discountAlAmt));
                    mItem.setPrice("" + final_price);

                    String itemCat1 = db.getItemCategory(mItem.getItemId());
                    //Riddhi
                    double itemVatAmt = UtilApp.vatAmount(mItem, itemCat1);

                    double itemPreVatAmt = UtilApp.getPreVatAmount(mItem, itemCat1);
                    mItem.setPreVatAmt("" + Math.round(itemPreVatAmt));
                    // System.out.println("Check-->"+mItem.getPreVatAmt());
                    double itemExcise = 0;
                    double itemNet = itemPreVatAmt - itemExcise;

                    mItem.setVatAmt("" + Math.round(itemVatAmt));
                    mItem.setExciseAmt("" + Math.round(itemExcise));
                    mItem.setNetAmt("" + Math.round(itemNet));
                    mItem.setDiscountAmt("" + DecimalUtils.round(discountAmt, 2));
                    mItem.setDiscount("" + DecimalUtils.round(discount, 2));
                    mItem.setDiscountAlAmt("" + DecimalUtils.round(discountAlAmt, 2));
                    mItem.setDiscountAlPer("" + DecimalUtils.round(discountAl, 2));
                    mItem.setIsFreeItem("0");
                    mItem.setPerentItemId("");
                    mItem.setAgentExcise("");
                    mItem.setReasonCode(reasonCode);
                    mItem.setReturnType(rreasonType);
                    mItem.setArrSelectUOM(mSelectUOMList);

                    intent.putExtra("item", mItem);
                    sendBroadcast(intent);
                    finish();
                    UtilApp.hideSoftKeyboard(InputDialogReturnActivity.this);
                } else {
                    Toast.makeText(InputDialogReturnActivity.this,
                            "Please add at least one row", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_add_more:
                sendConfirmData();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.edt_expiryDate:
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd1 = new DatePickerDialog(InputDialogReturnActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String dayOfMonths = "";
                                String monthOfYears = "";
                                if (dayOfMonth < 10) {
                                    dayOfMonths = "0" + String.valueOf(dayOfMonth);
                                } else {
                                    dayOfMonths = String.valueOf(dayOfMonth);
                                }
                                if (monthOfYear < 9) {
                                    monthOfYears = "0" + String.valueOf(monthOfYear + 1);
                                } else {
                                    monthOfYears = String.valueOf(monthOfYear + 1);
                                }
                                String strDob = year + "-" + monthOfYears + "-" + (dayOfMonths);
                                binding.edtExpiryDate.setText(strDob);
                                mExpDate = strDob;
                            }
                        }, mYear, mMonth, mDay);

                if (returnType.equalsIgnoreCase("Bad")) {
                    dpd1.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                } else {
                    dpd1.getDatePicker().setMinDate(calendar.getTimeInMillis());
                }
                dpd1.show();
                break;
        }
    }

    private void sendConfirmData() {

        if (binding.edtSaleAlter.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(InputDialogReturnActivity.this,
                    "Please input qty", Toast.LENGTH_SHORT).show();
            return;
        } else if (binding.spUom.getSelectedItem().toString().equalsIgnoreCase("Select UOM")) {
            Toast.makeText(InputDialogReturnActivity.this,
                    "Please select UOM", Toast.LENGTH_SHORT).show();
            return;
        } else if (binding.spReason.getSelectedItem().toString().equalsIgnoreCase("Select Reason")) {
            Toast.makeText(InputDialogReturnActivity.this,
                    "Please select reason", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isAlterUOm = false;

        String selectUOM = binding.spUom.getSelectedItem().toString();
        if (selectUOM.equals(db.getUOM(alterUOM))) {
            isAlterUOm = true;
        } else {
            isAlterUOm = false;
        }

        if (isAlterUOm) {

            String alterSellQty = binding.edtSaleAlter.getText().toString().isEmpty() ? "0" : binding.edtSaleAlter.getText().toString();
            String baseSellQty = "0";

            double alterSellPrice = 0, baseSellPrice = 0;
            if (Integer.parseInt(alterSellQty) > 0) {
                alterSellPrice = Integer.parseInt(alterSellQty) * itemAlterPrice;
            }


            UOM mUOMItem = new UOM();

            mUOMItem.setUomId(alterUOM);
            mUOMItem.setUomName(selectUOM);
            mUOMItem.setUomQty(alterSellQty);
            mUOMItem.setUomType("Alter");
            mUOMItem.setUomGross("" + alterSellPrice);
            mUOMItem.setUomPrice("" + (itemAlterPrice + discountAlAmt));
            mUOMItem.setUomReason(binding.spReason.getSelectedItem().toString());
            mUOMItem.setUomreasonCode(getReasonCode(binding.spReason.getSelectedItem().toString()));
            mUOMItem.setUomReturnnType(getReasonType(binding.spReason.getSelectedItem().toString()));
            String itemCat1 = db.getItemCategory(mItem.getItemId());
            //Riddhi
            double itemVatAmt = UtilApp.vatUOMAmount("" + alterSellPrice, itemCat1);


            double itemPreVatAmt = alterSellPrice - itemVatAmt;
            mUOMItem.setUomPrevat("" + Math.round(itemPreVatAmt));

            double itemExcise = 0;
            double itemNet = itemPreVatAmt - itemExcise;

            mUOMItem.setUomVat("" + Math.round(itemVatAmt));
            mUOMItem.setUomExcise("" + Math.round(itemExcise));
            mUOMItem.setUomNet("" + Math.round(itemNet));
            mUOMItem.setUomExp(mExpDate);

            mSelectUOMList.add(mUOMItem);
        } else {

            String baseSellQty = binding.edtSaleAlter.getText().toString().isEmpty() ? "0" : binding.edtSaleAlter.getText().toString();
            String alterSellQty = "0";

            double alterSellPrice = 0, baseSellPrice = 0;
            if (Integer.parseInt(baseSellQty) > 0) {
                baseSellPrice = Integer.parseInt(baseSellQty) * itemBasePrice;
            }
            double final_price = alterSellPrice + baseSellPrice;

            UOM mUOMItem = new UOM();

            mUOMItem.setUomId(baseUOM);
            mUOMItem.setUomName(selectUOM);
            mUOMItem.setUomQty(baseSellQty);
            mUOMItem.setUomType("Base");
            mUOMItem.setUomGross("" + baseSellPrice);
            mUOMItem.setUomPrice("" + (itemBasePrice + discountAlAmt));
            mUOMItem.setUomReason(binding.spReason.getSelectedItem().toString());
            mUOMItem.setUomreasonCode(getReasonCode(binding.spReason.getSelectedItem().toString()));
            mUOMItem.setUomReturnnType(getReasonType(binding.spReason.getSelectedItem().toString()));
            String itemCat1 = db.getItemCategory(mItem.getItemId());
            //Riddhi
            double itemVatAmt = UtilApp.vatUOMAmount("" + baseSellPrice, itemCat1);

            double itemPreVatAmt = baseSellPrice - itemVatAmt;
            mUOMItem.setUomPrevat("" + Math.round(itemPreVatAmt));

            double itemExcise = 0;
            double itemNet = itemPreVatAmt - itemExcise;

            mUOMItem.setUomVat("" + Math.round(itemVatAmt));
            mUOMItem.setUomExcise("" + Math.round(itemExcise));
            mUOMItem.setUomNet("" + Math.round(itemNet));
            mUOMItem.setUomExp(mExpDate);

            mSelectUOMList.add(mUOMItem);
        }

        binding.edtSaleAlter.setText("");
        binding.edtExpiryDate.setText("Select Date");
        mExpDate = "";
        binding.spReason.setSelection(0);
        binding.spUom.setSelection(0);

        setUpRecycler();

    }

    private String getReasonCode(String name) {
        String code = "0";

        switch (name) {
            case "Damage":
                code = "2";
                break;
            case "Expired":
                code = "3";
                break;
            case "Short Expiry":
                code = "6";
                break;
            case "Packing Issue":
                code = "7";
                break;
            case "Product Replacement":
                code = "8";
                break;
            case "Non moving product":
                code = "9";
                break;
        }
        return code;

    }

    private String getReasonType(String name) {
        String code = "Bad";

        switch (name) {
            case "Damage":
                code = "Bad";
                break;
            case "Expired":
                code = "Bad";
                break;
            case "Short Expiry":
                code = "Good";
                break;
            case "Packing Issue":
                code = "Bad";
                break;
            case "Product Replacement":
                code = "Good";
                break;
            case "Non moving product":
                code = "Good";
                break;
        }
        return code;

    }

    private int getBadReasonPosition(String name) {
        int code = 0;

        switch (name) {
            case "2":
                code = 1;
                break;
            case "3":
                code = 2;
                break;
            case "7":
                code = 3;
                break;
        }
        return code;

    }

    private int getGoodReasonPosition(String name) {
        int code = 0;
        switch (name) {
            case "6":
                code = 1;
                break;
            case "9":
                code = 2;
                break;
            case "8":
                code = 3;
                break;
        }
        return code;

    }
}