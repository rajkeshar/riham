package com.mobiato.sfa.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityInputDialogBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Discount;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.utils.DecimalUtils;
import com.mobiato.sfa.utils.UtilApp;

public class InputDialogActivity extends Activity implements View.OnClickListener {

    public ActivityInputDialogBinding binding;
    public Intent intent;
    public static final String BROADCAST_ACTION = "com.mobiato.sfa.DIALOG";
    public Item mItem;
    public int available_Qty = 0;
    private double itemBasePrice = 0, itemAlterPrice = 0, discount = 0, discountAmt = 0,
            discountAl = 0, discountAlAmt = 0;
    private String baseUOM, alterUOM, alterSellQty, baseSellQty;
    private DBManager db;
    private int UPC = 0, saleQty = 0, altAvailableQty = 0, basAvailbaleQty = 0;
    private String custId = "", routeId = "", type = "";
    private Item mFocAlItem, mFocBsItem;
    private Discount mBaseDis, mAlDis;
    private boolean isFreeApply = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_dialog);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);

        getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        intent = new Intent(BROADCAST_ACTION);

        db = new DBManager(this);

        if (getIntent() != null) {
            mItem = (Item) getIntent().getSerializableExtra("item");
            custId = getIntent().getStringExtra("custId");
            routeId = getIntent().getStringExtra("routeId");
            type = getIntent().getStringExtra("type");
        }

        // itemUOMPrice = db.getItemPrice(mItem.getItemId());
        baseUOM = db.getItemUOM(mItem.getItemId(), "Base");
        alterUOM = db.getItemUOM(mItem.getItemId(), "Alter");
        binding.edtSaleAlter.setHint(db.getUOM(alterUOM));
        binding.edtSaleBase.setHint(db.getUOM(baseUOM));

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

//        if (db.isCustomerDiscount(custId, mItem.getItemId())) {
//            if (db.isCustomerItemDiscount(custId, mItem.getItemId(), mItem.getBaseUOM())) {
//                mBaseDis = db.getCustomerItemDiscount(custId, mItem.getItemId(), mItem.getBaseUOM());
//            }
//            if (db.isCustomerItemDiscount(custId, mItem.getItemId(), mItem.getAltrUOM())) {
//                mAlDis = db.getCustomerItemDiscount(custId, mItem.getItemId(), mItem.getAltrUOM());
//            }
//        } else if (db.isRouteDiscount(mItem.getItemId())) {
//            if (db.isRouteItemDiscount(mItem.getItemId(), mItem.getBaseUOM())) {
//                mBaseDis = db.getRouteItemDiscount(mItem.getItemId(), mItem.getBaseUOM());
//            }
//            if (db.isRouteItemDiscount(mItem.getItemId(), mItem.getAltrUOM())) {
//                mAlDis = db.getRouteItemDiscount(mItem.getItemId(), mItem.getAltrUOM());
//            }
//        } else if (db.isItemDiscount(mItem.getItemId())) {
//            if (db.isItemUOMDiscount(mItem.getItemId(), mItem.getBaseUOM())) {
//                mBaseDis = db.getItemDiscount(mItem.getItemId(), mItem.getBaseUOM());
//            }
//            if (db.isItemUOMDiscount(mItem.getItemId(), mItem.getAltrUOM())) {
//                mAlDis = db.getItemDiscount(mItem.getItemId(), mItem.getAltrUOM());
//            }
//        }


        if (Integer.parseInt(mItem.getSaleBaseQty()) > 0) {
            binding.edtSaleBase.setText(mItem.getSaleBaseQty());
        }

        if (Integer.parseInt(mItem.getSaleAltQty()) > 0) {
            binding.edtSaleAlter.setText(mItem.getSaleAltQty());
        }

        if (type.equalsIgnoreCase("Sale")) {
            Item availableItem = db.getVanStockItem(mItem.getItemId());
            altAvailableQty = Integer.parseInt(availableItem.getAlterUOMQty());
            basAvailbaleQty = Integer.parseInt(availableItem.getBaseUOMQty());

            int deliveryAltQty = db.getDeliveryStockItem(mItem.getItemId(), "alter");
            int deliveryBsQty = db.getDeliveryStockItem(mItem.getItemId(), "Base");

            if (altAvailableQty > deliveryAltQty) {
                altAvailableQty = altAvailableQty - deliveryAltQty;
            } else {
                altAvailableQty = deliveryAltQty - altAvailableQty;
            }

            if (basAvailbaleQty > deliveryBsQty) {
                basAvailbaleQty = basAvailbaleQty - deliveryBsQty;
            } else {
                basAvailbaleQty = deliveryBsQty - basAvailbaleQty;
            }

        } else {

            Item availableItem = db.getVanStockItem(mItem.getItemId());
            if (availableItem.getAlterUOMQty() != null) {
                altAvailableQty = Integer.parseInt(availableItem.getAlterUOMQty());
                basAvailbaleQty = Integer.parseInt(availableItem.getBaseUOMQty());
            } else {
                altAvailableQty = 0;
                basAvailbaleQty = 0;
            }

            int deliveryAltQty = db.getDeliveryStockItem(mItem.getItemId(), "alter");
            int deliveryBsQty = db.getDeliveryStockItem(mItem.getItemId(), "Base");

            if (altAvailableQty > deliveryAltQty) {
                altAvailableQty = deliveryAltQty;
            }

            if (basAvailbaleQty > deliveryBsQty) {
                basAvailbaleQty = deliveryBsQty;
            }

        }


        binding.txtName.setText(mItem.getItemName());
        //Get Item Available Qty from Table Item Master
        binding.txtQty.setText("Available Qty: " + db.getUOM(alterUOM) + ": " + altAvailableQty + " | " + db.getUOM(baseUOM) + ": " + basAvailbaleQty);

        UPC = Integer.parseInt(db.getItemUPC(mItem.getItemId()));
        int alterUPC = altAvailableQty * UPC;
        available_Qty = alterUPC + basAvailbaleQty;
        //binding.txtQty.setText("Available Qty: " + available_Qty);


        binding.edtSaleAlter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!s.toString().equalsIgnoreCase("")) {
//                    String mPriority = db.getPriorityTypeForItem(mItem.getItemId());
//                    if (mPriority.equals("Promo")) {
//                        boolean isFree = db.isFreeGoods(mItem.getItemId(), custId, mItem.getAltrUOM(), s.toString());
//                        if (isFree) {
//                            isFreeApply = true;
//                            mFocAlItem = db.getFreeGoodsItem(mItem.getItemId(), custId, mItem.getAltrUOM(), s.toString());
//                            int focQuantity = Integer.parseInt(mFocAlItem.getQualitQty());
//                            int inpQty = Integer.parseInt(s.toString());
//                            int assigningQuantity = Integer.parseInt(mFocAlItem.getQty());
//                            int factor = (int) (inpQty / focQuantity);
//                            String freeCases = String.valueOf((assigningQuantity * factor));
//                            mFocAlItem.setQty(freeCases);
//                            binding.llEmpty.setVisibility(View.VISIBLE);
//                            binding.txtFreeItem.setText("- " + db.getItemName(mFocAlItem.getItemId()) + "    : " + mFocAlItem.getQty()
//                                    + " " + db.getUOM(mFocAlItem.getUom()));
//                            binding.txtFreeItem.setVisibility(View.VISIBLE);
//                        } else {
//                            isFreeApply = false;
//                            mFocAlItem = null;
//                            if (mFocBsItem == null && mFocAlItem == null) {
//                                binding.llEmpty.setVisibility(View.GONE);
//                                binding.txtFreeItem.setText("");
//                                binding.txtFreeItem.setVisibility(View.GONE);
//                            }
//                        }
//                    }
//                } else {
                isFreeApply = false;
                mFocAlItem = null;
                if (mFocBsItem == null && mFocAlItem == null) {
                    binding.llEmpty.setVisibility(View.GONE);
                    binding.txtFreeItem.setText("");
                    binding.txtFreeItem.setVisibility(View.GONE);
                }

                // }
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
//                if (!s.toString().equalsIgnoreCase("")) {
//                    String mPriority = db.getPriorityTypeForItem(mItem.getItemId());
//                    if (mPriority.equals("Promo")) {
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
//                            mFocBsItem = null;
//                            if (mFocBsItem == null && mFocAlItem == null) {
//                                binding.llEmpty.setVisibility(View.GONE);
//                                binding.txtFreeItem.setText("");
//                                binding.txtFreeItem.setVisibility(View.GONE);
//                            }
//                        }
//                    }
//                } else {
                mFocBsItem = null;
                if (mFocBsItem == null && mFocAlItem == null) {
                    binding.llEmpty.setVisibility(View.GONE);
                    binding.txtFreeItem.setText("");
                    binding.txtFreeItem.setVisibility(View.GONE);
                }
                // }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

        if (mFocAlItem != null || mFocBsItem != null) {

            if (mFocBsItem != null) {
                boolean isAvailble = db.isFreeGoodAvailable(mFocBsItem.getItemId(), "Base", mFocBsItem.getQty());
                if (!isAvailble) {
                    showFreeDialog();
                } else {
                    confirmSell();
                }
            } else {
                boolean isAvailble = db.isFreeGoodAvailable(mFocAlItem.getItemId(), "Alter", mFocAlItem.getQty());
                if (!isAvailble) {
                    showFreeDialog();
                } else {
                    confirmSell();
                }
            }
        } else {
            confirmSell();
        }

    }

    public void showFreeDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InputDialogActivity.this);
        alertDialogBuilder.setTitle("Alert")
                .setMessage("Free good stock is not available in VanStock. Are you sure you want to sell without that?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        confirmSell();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    public void confirmSell() {
        if (binding.edtSaleAlter.getText().toString().equalsIgnoreCase("") &&
                binding.edtSaleBase.getText().toString().equalsIgnoreCase("")) {

            Toast.makeText(InputDialogActivity.this,
                    "Please input selling qty", Toast.LENGTH_SHORT).show();
            return;
        }

        alterSellQty = binding.edtSaleAlter.getText().toString();
        baseSellQty = binding.edtSaleBase.getText().toString();

        if (!alterSellQty.equalsIgnoreCase("")) {
            if (Integer.parseInt(alterSellQty) > altAvailableQty) {
                Toast.makeText(InputDialogActivity.this,
                        "You have enter " + db.getUOM(alterUOM) + " qty more then stock", Toast.LENGTH_SHORT).show();
                return;
            } else {
                saleQty = Integer.parseInt(alterSellQty) * UPC;
            }
        } else {
            alterSellQty = "0";
        }

        if (!baseSellQty.equalsIgnoreCase("")) {
            saleQty = saleQty + Integer.parseInt(baseSellQty);
        } else {
            baseSellQty = "0";
        }

        if (available_Qty < saleQty) {
            Toast.makeText(InputDialogActivity.this,
                    "You have entered qty more then stock",
                    Toast.LENGTH_SHORT).show();
            return;
        }


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
        }


        double alterSellPrice = 0, baseSellPrice = 0;
        if (Integer.parseInt(alterSellQty) > 0) {
            alterSellPrice = Integer.parseInt(alterSellQty) * itemAlterPrice;
        }

        double final_price = 0;
        if (Integer.parseInt(baseSellQty) > 0) {
            baseSellPrice = Integer.parseInt(baseSellQty) * itemBasePrice;
        }

        final_price = alterSellPrice + baseSellPrice;

        intent.putExtra("sellQty", saleQty);
        intent.putExtra("tot_price", final_price);
        mItem.setUOMPrice("" + (itemBasePrice + discountAmt));
        mItem.setAlterUOMPrice("" + (itemAlterPrice + discountAlAmt));
        mItem.setSaleQty("" + saleQty);
        mItem.setSaleBaseQty(baseSellQty);
        mItem.setSaleBasePrice("" + baseSellPrice);
        mItem.setSaleAltQty(alterSellQty);
        mItem.setSaleAltPrice("" + alterSellPrice);
        mItem.setPrice("" + final_price);
        mItem.setIsFreeItem("0");
        mItem.setDiscountAmt("" + DecimalUtils.round(discountAmt, 2));
        mItem.setDiscount("" + DecimalUtils.round(discount, 2));
        mItem.setDiscountAlAmt("" + DecimalUtils.round(discountAlAmt, 2));
        mItem.setDiscountAlPer("" + DecimalUtils.round(discountAl, 2));

        if (mFocAlItem != null && mFocBsItem != null) {
            if (SalesActivity.arrFOCItem.size() > 0) {
                for (int i = 0; i < SalesActivity.arrFOCItem.size(); i++) {
                    if (SalesActivity.arrFOCItem.get(i).getItemId().equalsIgnoreCase(mItem.getFocItemId())) {
                        SalesActivity.arrFOCItem.remove(i);
                        break;
                    }
                }
            }
            mItem.setFocItemId("");
            mItem.setFocItem(null);
            mItem.setIsFOCItem("no");
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
                itemFree.setQty(mFocBsItem.getQty());
                itemFree.setSaleAltQty("0");
                itemFree.setPrice("0");
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
                itemFree.setAgentExcise(db.getItemCode(mFocBsItem.getAgentExcise()));
                itemFree.setDirectsellexcise("0");
                mItem.setFocItem(itemFree);
                mItem.setIsFOCItem("yes");
                mItem.setFocItemId(mFocBsItem.getItemId());
                SalesActivity.arrFOCItem.add(itemFree);
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
                itemFree.setQty(mFocAlItem.getQty());
                itemFree.setCategory(db.getItemCategory(mFocAlItem.getItemId()));
                itemFree.setPrice("0");
                itemFree.setPreVatAmt("0");
                itemFree.setVatAmt("0");
                itemFree.setExciseAmt("0");
                itemFree.setAgentExcise("0");
                itemFree.setDirectsellexcise("0");
                itemFree.setNetAmt("0");
                itemFree.setIsFreeItem("1");
                itemFree.setDiscountAmt("0");
                itemFree.setDiscount("0");
                itemFree.setDiscountAlAmt("0");
                itemFree.setDiscountAlPer("0");
                mItem.setFocItem(itemFree);
                mItem.setIsFOCItem("yes");
                mItem.setFocItemId(mFocAlItem.getItemId());
                SalesActivity.arrFOCItem.add(itemFree);
            } else {
                mItem.setFocItemId("");
                mItem.setFocItem(null);
                mItem.setIsFOCItem("no");
            }
        }
        intent.putExtra("item", mItem);
        sendBroadcast(intent);
        finish();
        UtilApp.hideSoftKeyboard(InputDialogActivity.this);
    }
}
