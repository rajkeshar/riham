package com.mobiato.sfa.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityInputDialogOrderBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.merchandising.InventoryCheckActivity;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Discount;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.utils.DecimalUtils;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InputDialogOrderActivity extends Activity implements View.OnClickListener {

    private ActivityInputDialogOrderBinding binding;
    public Intent intent;
    public static final String BROADCAST_ACTION = "com.mobiato.sfa.DIALOG";
    public Item mItem;
    public double itemBasePrice = 0, itemAlterPrice = 0, itemUOMPrice = 0, discount = 0, discountAmt = 0,
            discountAl = 0, discountAlAmt = 0;
    private String baseUOM, alterUOM, alterSellQty, baseSellQty, type = "", custId = "", returnType = "", routeId = "", agentexciseAmt = "0";
    private DBManager db;
    private int UPC = 0, focItemPosition = 0;
    private boolean isExchange = false, isBeforeFoc = false;
    private Item mFocAlItem, mFocBsItem;
    private Discount mBaseDis, mAlDis;
    private Customer mCustomer;
    private boolean isFreeApply = false;
    private String appType = "", mExpDate = "";
    int mDay, mMonth, mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_dialog_order);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);

        getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        intent = new Intent(BROADCAST_ACTION);

        db = new DBManager(this);

        if (getIntent() != null) {
            mItem = (Item) getIntent().getSerializableExtra("item");
            type = getIntent().getStringExtra("type");
            custId = getIntent().getStringExtra("customer");
            if (db.checkIsCustomerExist(custId)) {
                mCustomer = db.getCustomerDetail(custId);
            } else {
                mCustomer = db.getDepotCustomerDetail(custId);
            }

            routeId = mCustomer.getRouteId();

            if (getIntent().getStringExtra("apptype") != null) {
                appType = getIntent().getStringExtra("apptype");
            }

            if (appType.equals("Merchandiser")) {
                binding.edtSaleBase.setVisibility(View.INVISIBLE);
            }
        }

        if (mItem.getIsFOCItem().equalsIgnoreCase("yes")) {
            for (int i = 0; i < OrderRequestActivity.arrFOCItem.size(); i++) {
                if (OrderRequestActivity.arrFOCItem.get(i).getPerentItemId().equalsIgnoreCase(mItem.getItemId())) {
                    Item freeItem = OrderRequestActivity.arrFOCItem.get(i);
                    focItemPosition = i;
                    if (Integer.parseInt(freeItem.getSaleBaseQty()) > 0) {
                        mFocBsItem = freeItem;
                        binding.txtFreeItem.setText("- " + db.getItemName(mFocBsItem.getItemId()) + "    : " + mFocBsItem.getSaleBaseQty()
                                + " " + mFocBsItem.getBaseUOMName());
                        binding.llEmpty.setVisibility(View.VISIBLE);
                        binding.txtFreeItem.setVisibility(View.VISIBLE);
                    } else {
                        mFocAlItem = freeItem;
                        binding.txtFreeItem.setText("- " + db.getItemName(mFocAlItem.getItemId()) + "    : " + mFocAlItem.getSaleAltQty()
                                + " " + mFocAlItem.getAlterUOMName());
                        binding.llEmpty.setVisibility(View.VISIBLE);
                        binding.txtFreeItem.setVisibility(View.VISIBLE);
                    }
                    isBeforeFoc = true;
                    break;
                }
            }
        }


        if (type.equalsIgnoreCase("Exchange")) {
            isExchange = true;
            binding.llReason.setVisibility(View.VISIBLE);
        } else if (type.equalsIgnoreCase("Return")) {
            returnType = getIntent().getStringExtra("returnType");
            binding.llReason.setVisibility(View.VISIBLE);
            binding.llExpiryDate.setVisibility(View.GONE);
            binding.llExpiry.setVisibility(View.VISIBLE);
        } else {
            binding.llReason.setVisibility(View.GONE);
        }
        //  agentexciseAmt = getIntent().getStringExtra("agentexciseAmt");

        if (type.equals("Load")) {
            itemBasePrice = db.getItemPrice(mItem.getItemId());
            itemAlterPrice = db.getItemAlterPrice(mItem.getItemId());
        } else {
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
        }

        itemUOMPrice = db.getItemPrice(mItem.getItemId());
        baseUOM = mItem.getBaseUOM();
        alterUOM = mItem.getAltrUOM();
        binding.edtSaleAlter.setHint(db.getUOM(alterUOM));
        binding.edtSaleBase.setHint(db.getUOM(baseUOM));


        if (Integer.parseInt(mItem.getSaleBaseQty()) > 0) {
            binding.edtSaleBase.setText(mItem.getSaleBaseQty());
        }

        if (Integer.parseInt(mItem.getSaleAltQty()) > 0) {
            binding.edtSaleAlter.setText(mItem.getSaleAltQty());
        }

        binding.txtName.setText(mItem.getItemName());

        UPC = Integer.parseInt(db.getItemUPC(mItem.getItemId()));


        List<String> list = new ArrayList<String>();
        if (type.equalsIgnoreCase("return")) {
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
        } else {
            list.add("Select Reason");
            list.add("Expired");
            list.add("Short Expiry");
            list.add("Packing Issue");
            list.add("Non moving product");
            list.add("Product Replacement");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spReason.setAdapter(dataAdapter);

        if (type.equalsIgnoreCase("Return")) {
            if (mItem.getReasonCode() != null) {
                if (!mItem.getReasonCode().equalsIgnoreCase("")) {
                    int position = 0;
                    if (returnType.equalsIgnoreCase("Bad")) {
                        position = getBadReasonPosition(mItem.getReasonCode());
                    } else {
                        position = getGoodReasonPosition(mItem.getReasonCode());
                    }
                    binding.spReason.setSelection(position);
                }
            }
        }


        binding.edtSaleAlter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equalsIgnoreCase("")) {
                    if (type.equals("Load")) {
                        boolean isFree = db.isAgentFreeGoods(mItem.getItemId(), custId, mItem.getAltrUOM(), s.toString());
                        System.out.println("isfree--> " + isFree);
                        if (isFree) {
                            isFreeApply = true;
                            mFocAlItem = db.getAgentFreeGoodsItem(mItem.getItemId(), custId, mItem.getAltrUOM(), s.toString());
                            int focQuantity = Integer.parseInt(mFocAlItem.getQualitQty());
                            int inpQty = Integer.parseInt(s.toString());
                            System.out.println("plll-->" + focQuantity);
                            int assigningQuantity = Integer.parseInt(mFocAlItem.getQty());
                            int factor = (int) (inpQty / focQuantity);
                            String freeCases = String.valueOf((assigningQuantity * factor));
                            mFocAlItem.setQty(freeCases);
                            binding.llEmpty.setVisibility(View.VISIBLE);
                            System.out.println("check--> " + mFocAlItem.getQty());
                            binding.txtFreeItem.setText("- " + db.getItemName(mFocAlItem.getItemId()) + "    : " + mFocAlItem.getQty()
                                    + " " + db.getUOM(mFocAlItem.getUom()));
                            binding.txtFreeItem.setVisibility(View.VISIBLE);
                        } else {
                            isFreeApply = false;
                            mFocAlItem = null;
                            if (mFocBsItem == null && mFocAlItem == null) {
                                binding.llEmpty.setVisibility(View.GONE);
                                binding.txtFreeItem.setText("");
                                binding.txtFreeItem.setVisibility(View.GONE);
                            }
                        }
                    } else if (!type.equalsIgnoreCase("return")) {
//                        boolean isFree = db.isFreeGoods(mItem.getItemId(), custId, mItem.getAltrUOM(), s.toString());
//                        System.out.println("isfree--> " + isFree);
//                        if (isFree) {
//                            isFreeApply = true;
//                            mFocAlItem = db.getFreeGoodsItem(mItem.getItemId(), custId, mItem.getAltrUOM(), s.toString());
//                            int focQuantity = Integer.parseInt(mFocAlItem.getQualitQty());
//                            int inpQty = Integer.parseInt(s.toString());
//                            System.out.println("plll-->" + focQuantity);
//                            int assigningQuantity = Integer.parseInt(mFocAlItem.getQty());
//                            int factor = (int) (inpQty / focQuantity);
//                            String freeCases = String.valueOf((assigningQuantity * factor));
//                            mFocAlItem.setQty(freeCases);
//                            binding.llEmpty.setVisibility(View.VISIBLE);
//                            System.out.println("check--> " + mFocAlItem.getQty());
//                            binding.txtFreeItem.setText("- " + db.getItemName(mFocAlItem.getItemId()) + "    : " + mFocAlItem.getQty()
//                                    + " " + db.getUOM(mFocAlItem.getUom()));
//                            binding.txtFreeItem.setVisibility(View.VISIBLE);
//                        } else {
                        isFreeApply = false;
                        mFocAlItem = null;
                        if (mFocBsItem == null && mFocAlItem == null) {
                            binding.llEmpty.setVisibility(View.GONE);
                            binding.txtFreeItem.setText("");
                            binding.txtFreeItem.setVisibility(View.GONE);
                        }
                        //  }
                    }
                } else {
                    isFreeApply = false;
                    mFocAlItem = null;
                    if (mFocBsItem == null && mFocAlItem == null) {
                        binding.llEmpty.setVisibility(View.GONE);
                        binding.txtFreeItem.setText("");
                        binding.txtFreeItem.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtSaleBase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equalsIgnoreCase("")) {
                    if (type.equals("Load")) {
                        boolean isFree = db.isAgentFreeGoods(mItem.getItemId(), custId, mItem.getBaseUOM(), s.toString());
                        if (isFree) {
                            mFocBsItem = db.getAgentFreeGoodsItem(mItem.getItemId(), custId, mItem.getBaseUOM(), s.toString());
                            int focQuantity = Integer.parseInt(mFocBsItem.getQualitQty());
                            int inpQty = Integer.parseInt(s.toString());
                            int assigningQuantity = Integer.parseInt(mFocBsItem.getQty());
                            int factor = (int) (inpQty / focQuantity);
                            String freeCases = String.valueOf((assigningQuantity * factor));
                            mFocBsItem.setQty(freeCases);
                            binding.llEmpty.setVisibility(View.VISIBLE);
                            binding.txtFreeItem.setText("- " + db.getItemName(mFocBsItem.getItemId()) + "    : " + mFocBsItem.getQty()
                                    + " " + db.getUOM(mFocBsItem.getUom()));
                            binding.txtFreeItem.setVisibility(View.VISIBLE);
                        } else {
                            mFocBsItem = null;
                            if (mFocBsItem == null && mFocAlItem == null) {
                                binding.llEmpty.setVisibility(View.GONE);
                                binding.txtFreeItem.setText("");
                                binding.txtFreeItem.setVisibility(View.GONE);
                            }
                        }
                    } else if (!type.equalsIgnoreCase("return")) {
//                        boolean isFree = db.isFreeGoods(mItem.getItemId(), custId, mItem.getBaseUOM(), s.toString());
//                        if (isFree) {
//                            mFocBsItem = db.getFreeGoodsItem(mItem.getItemId(), custId, mItem.getBaseUOM(), s.toString());
//                            int focQuantity = Integer.parseInt(mFocBsItem.getQualitQty());
//                            int inpQty = Integer.parseInt(s.toString());
//                            int assigningQuantity = Integer.parseInt(mFocBsItem.getQty());
//                            int factor = (int) (inpQty / focQuantity);
//                            String freeCases = String.valueOf((assigningQuantity * factor));
//                            mFocBsItem.setQty(freeCases);
//                            binding.llEmpty.setVisibility(View.VISIBLE);
//                            binding.txtFreeItem.setText("- " + db.getItemName(mFocBsItem.getItemId()) + "    : " + mFocBsItem.getQty()
//                                    + " " + db.getUOM(mFocBsItem.getUom()));
//                            binding.txtFreeItem.setVisibility(View.VISIBLE);
//                        } else {
                        mFocBsItem = null;
                        if (mFocBsItem == null && mFocAlItem == null) {
                            binding.llEmpty.setVisibility(View.GONE);
                            binding.txtFreeItem.setText("");
                            binding.txtFreeItem.setVisibility(View.GONE);
                        }
                        //   }
                    }
                } else {
                    mFocBsItem = null;
                    if (mFocBsItem == null && mFocAlItem == null) {
                        binding.llEmpty.setVisibility(View.GONE);
                        binding.txtFreeItem.setText("");
                        binding.txtFreeItem.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //set OnClickListener
        binding.btnConfirm.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
        binding.edtExpiryDate.setOnClickListener(this);
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
            case R.id.edt_expiryDate:
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd1 = new DatePickerDialog(InputDialogOrderActivity.this,
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
        if (binding.edtSaleAlter.getText().toString().equalsIgnoreCase("") &&
                binding.edtSaleBase.getText().toString().equalsIgnoreCase("")) {

            Toast.makeText(InputDialogOrderActivity.this,
                    "Please input qty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isExchange) {
            String text = binding.spReason.getSelectedItem().toString();

            if (text.equalsIgnoreCase("Select Reason")) {
                Toast.makeText(getApplicationContext(), "Please select reason", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (type.equalsIgnoreCase("Return")) {
                String text = binding.spReason.getSelectedItem().toString();
                if (text.equalsIgnoreCase("Select Reason")) {
                    Toast.makeText(getApplicationContext(), "Please select reason", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        alterSellQty = binding.edtSaleAlter.getText().toString().isEmpty() ? "0" : binding.edtSaleAlter.getText().toString();
        baseSellQty = binding.edtSaleBase.getText().toString().isEmpty() ? "0" : binding.edtSaleBase.getText().toString();


        if (!isFreeApply) {

            if (mBaseDis != null) {

                if (Integer.parseInt(baseSellQty) >= Integer.parseInt(mBaseDis.getQty())) {

                    if (mBaseDis.getType().equalsIgnoreCase("1")) {
                        double value = ((itemBasePrice * Double.parseDouble(mBaseDis.getDiscount())) / 100);
                        itemBasePrice = itemBasePrice - value;
                        discount = Double.parseDouble(mBaseDis.getDiscount());
                        discountAmt = value;
                    } else {
                        itemBasePrice = itemBasePrice - Double.parseDouble(mBaseDis.getDiscount());
                        discountAmt = Double.parseDouble(mBaseDis.getDiscount());
                        discount = Double.parseDouble(mBaseDis.getDiscount());
                    }
                }
            }

            if (mAlDis != null) {
                if (Integer.parseInt(alterSellQty) >= Integer.parseInt(mAlDis.getQty())) {
                    if (mAlDis.getType().equalsIgnoreCase("1")) {
                        double value = ((itemAlterPrice * Double.parseDouble(mAlDis.getDiscount())) / 100);
                        itemAlterPrice = itemAlterPrice - value;
                        discountAl = Double.parseDouble(mAlDis.getDiscount());
                        discountAlAmt = value;
                    } else {
                        itemAlterPrice = itemAlterPrice - Double.parseDouble(mAlDis.getDiscount());
                        discountAl = Double.parseDouble(mAlDis.getDiscount());
                        discountAlAmt = Double.parseDouble(mAlDis.getDiscount());
                    }
                }
            }
        } else {
            discountAl = 0;
            discountAlAmt = 0;
            discount = 0;
            discountAmt = 0;
        }


        double alterSellPrice = 0, baseSellPrice = 0;
        if (Integer.parseInt(alterSellQty) > 0) {
            alterSellPrice = Integer.parseInt(alterSellQty) * itemAlterPrice;
        }

        if (Integer.parseInt(baseSellQty) > 0) {
            baseSellPrice = Integer.parseInt(baseSellQty) * itemBasePrice;
        }

        double final_price = alterSellPrice + baseSellPrice;

        mItem.setSaleBaseQty(baseSellQty);
        mItem.setSaleBasePrice("" + baseSellPrice);
        mItem.setSaleAltQty(alterSellQty);
        mItem.setSaleAltPrice("" + alterSellPrice);
        mItem.setUOMPrice("" + (itemBasePrice + discountAmt));
        mItem.setAlterUOMPrice("" + (itemAlterPrice + discountAlAmt));
        mItem.setPrice("" + final_price);
        mItem.setReasonCode(getReasonCode(binding.spReason.getSelectedItem().toString()));
        mItem.setReturnType(getReasonType(binding.spReason.getSelectedItem().toString()));
        String itemCat1 = db.getItemCategory(mItem.getItemId());
        //Riddhi
        double itemVatAmt = UtilApp.vatAmount(mItem, itemCat1);

        double itemPreVatAmt = UtilApp.getPreVatAmount(mItem, itemCat1);
        mItem.setPreVatAmt("" + Math.round(itemPreVatAmt));
        // System.out.println("Check-->"+mItem.getPreVatAmt());
        double itemExcise = 0;
        if (type.equalsIgnoreCase("Load")) {
            String itemCat = db.getItemCategory(mItem.getItemId());
            String itemBasVolume = db.getItemBaseVolume(mItem.getItemId());
            String itemAltVolume = db.getItemAlterVolume(mItem.getItemId());
            if (itemCat.equalsIgnoreCase("1")) {
                itemExcise = 0;
            } else {
                itemExcise = UtilApp.getExciseValue(mItem, itemCat, itemBasVolume, itemAltVolume);
            }
        } else if (type.equalsIgnoreCase("Order")) {
            if (mCustomer.getCustomerType().equalsIgnoreCase("4")) {
                String itemCat = db.getItemCategory(mItem.getItemId());
                String itemBasVolume = db.getItemBaseVolume(mItem.getItemId());
                String itemAltVolume = db.getItemAlterVolume(mItem.getItemId());
                if (itemCat.equalsIgnoreCase("1")) {
                    itemExcise = 0;
                } else {
                    itemExcise = UtilApp.getExciseValue(mItem, itemCat, itemBasVolume, itemAltVolume);
                }
            }
        }

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
        mItem.setAgentExcise(mExpDate);

        if (type.equalsIgnoreCase("Order") || type.equalsIgnoreCase("Load")) {
            if (mFocAlItem != null && mFocBsItem != null) {
                if (OrderRequestActivity.arrFOCItem.size() > 0) {
                    for (int i = 0; i < OrderRequestActivity.arrFOCItem.size(); i++) {
                        if (OrderRequestActivity.arrFOCItem.get(i).getItemId().equalsIgnoreCase(mItem.getFocItemId())) {
                            OrderRequestActivity.arrFOCItem.remove(i);
                            break;
                        }
                    }
                }
                mItem.setFocItemId("");
                mItem.setFocItem(null);
                mItem.setIsFOCItem("no");
            } else {
                if (isBeforeFoc) {
                    if (isBeforeFoc) {
                        if (mFocBsItem == null && mFocAlItem == null) {
                            mItem.setIsFOCItem("");
                            mItem.setFocItem(null);
                            mItem.setFocItemId("");
                            OrderRequestActivity.arrFOCItem.remove(focItemPosition);
                        }
                        isBeforeFoc = false;
                    }
                } else {
                    if (mFocBsItem != null) {
                        Item itemFree = new Item();
                        itemFree.setItemId(mFocBsItem.getItemId());
                        itemFree.setItemCode(db.getItemCode(mFocBsItem.getItemId()));
                        itemFree.setItemName(db.getItemName(mFocBsItem.getItemId()));
                        itemFree.setBaseUOM(mFocBsItem.getUom());
                        itemFree.setBaseUOMName(db.getUOM(mFocBsItem.getUom()));
                        itemFree.setAltrUOM(db.getItemUOM(mFocBsItem.getItemId(), "Alter"));
                        itemFree.setAlterUOMName(db.getUOM(db.getItemUOM(mFocBsItem.getItemId(), "Alter")));
                        itemFree.setSaleQty(mFocBsItem.getQty());
                        itemFree.setSaleBaseQty(mFocBsItem.getQty());
                        itemFree.setCategory(db.getItemCategory(mFocBsItem.getItemId()));
                        itemFree.setSaleAltQty("0");
                        itemFree.setPrice("0");
                        itemFree.setUOMPrice("" + itemBasePrice);
                        itemFree.setAlterUOMPrice("" + itemAlterPrice);
                        itemFree.setSaleBasePrice("0");
                        itemFree.setSaleAltPrice("0");
                        itemFree.setPreVatAmt("0");
                        itemFree.setVatAmt("0");
                        itemFree.setExciseAmt("0");
                        itemFree.setNetAmt("0");
                        itemFree.setIsFreeItem("1");
                        itemFree.setDiscountAmt("0");
                        itemFree.setDiscount("0");
                        itemFree.setDiscountAlAmt("0");
                        itemFree.setDiscountAlPer("0");
                        itemFree.setPerentItemId(mItem.getItemId());
                        mItem.setFocItem(itemFree);
                        mItem.setIsFOCItem("yes");
                        mItem.setFocItemId(mFocBsItem.getItemId());
                        OrderRequestActivity.arrFOCItem.add(itemFree);
                    } else if (mFocAlItem != null) {
                        Item itemFree = new Item();
                        itemFree.setItemId(mFocAlItem.getItemId());
                        itemFree.setItemCode(db.getItemCode(mFocAlItem.getItemId()));
                        itemFree.setItemName(db.getItemName(mFocAlItem.getItemId()));
                        itemFree.setSaleQty(mFocAlItem.getQty());
                        itemFree.setBaseUOM(db.getItemUOM(mFocAlItem.getItemId(), "Base"));
                        itemFree.setBaseUOMName(db.getUOM(db.getItemUOM(mFocAlItem.getItemId(), "Base")));
                        itemFree.setAltrUOM(mFocAlItem.getUom());
                        itemFree.setAlterUOMName(db.getUOM(mFocAlItem.getUom()));
                        itemFree.setSaleAltQty(mFocAlItem.getQty());
                        itemFree.setSaleAltPrice("0");
                        itemFree.setSaleBasePrice("0");
                        itemFree.setSaleBaseQty("0");
                        itemFree.setCategory(db.getItemCategory(mFocAlItem.getItemId()));
                        itemFree.setPrice("0");
                        itemFree.setPreVatAmt("0");
                        itemFree.setVatAmt("0");
                        itemFree.setExciseAmt("0");
                        itemFree.setNetAmt("0");
                        itemFree.setUOMPrice("" + itemBasePrice);
                        itemFree.setAlterUOMPrice("" + itemAlterPrice);
                        itemFree.setIsFreeItem("1");
                        itemFree.setDiscountAmt("0");
                        itemFree.setDiscount("0");
                        itemFree.setDiscountAlAmt("0");
                        itemFree.setDiscountAlPer("0");
                        itemFree.setPerentItemId(mItem.getItemId());
                        mItem.setFocItem(itemFree);
                        mItem.setIsFOCItem("yes");
                        mItem.setFocItemId(mFocAlItem.getItemId());
                        OrderRequestActivity.arrFOCItem.add(itemFree);
                    } else {
                        mItem.setFocItemId("");
                        mItem.setFocItem(null);
                        mItem.setIsFOCItem("no");
                    }
                }
            }
        }

        intent.putExtra("item", mItem);
        sendBroadcast(intent);
        finish();
        UtilApp.hideSoftKeyboard(InputDialogOrderActivity.this);
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
