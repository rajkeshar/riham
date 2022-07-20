package com.mobiato.sfa.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityBadReturnBinding;
import com.mobiato.sfa.databinding.DialogInputUnloadBinding;
import com.mobiato.sfa.databinding.RowItemFreshunloadBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BadReturnActivity extends BaseActivity implements View.OnClickListener {

    private ActivityBadReturnBinding binding;
    private ArrayList<Item> arrItem = new ArrayList<>();
    private CommonAdapter<Item> mAdapter;
    public ArrayList<String> arrCheckItem = new ArrayList<>();
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private String alterActQty, baseActQty;
    private int itemPosition = 0;

    private DBManager db;
    private LoadingSpinner progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBadReturnBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Bad Return");

        db = new DBManager(BadReturnActivity.this);
        arrItem = new ArrayList<>();
        arrItem = db.getBadReturnItems("Bad");

        setAdapter();

        if (arrItem.size() == 0) {
            binding.lytSelect.setVisibility(View.GONE);
            binding.btnUnload.setVisibility(View.GONE);
        }

        binding.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < arrItem.size(); i++) {
                        arrCheckItem.add(arrItem.get(i).getItemId());
                    }
                } else {
                    arrCheckItem.clear();
                }

                mAdapter.notifyDataSetChanged();
            }
        });

        binding.btnUnload.setVisibility(View.GONE);
        binding.btnUnload.setOnClickListener(this);
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Item>(arrItem) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemFreshunloadBinding) {
                    Item mItem = arrItem.get(position);
                    ((RowItemFreshunloadBinding) holder.binding).setItem(mItem);

                    if (mItem.getAltrUOM() != null) {
                        ((RowItemFreshunloadBinding) holder.binding).tvAlterUOM.setText(mItem.getAlterUOMName() + ": " + mItem.getAlterUOMQty());
                        if (mItem.getBaseUOM() != null) {
                            ((RowItemFreshunloadBinding) holder.binding).dividerUOM.setVisibility(View.VISIBLE);
                            ((RowItemFreshunloadBinding) holder.binding).tvBaseUOM.setVisibility(View.VISIBLE);
                            ((RowItemFreshunloadBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
                        } else {
                            ((RowItemFreshunloadBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                            ((RowItemFreshunloadBinding) holder.binding).tvBaseUOM.setVisibility(View.GONE);
                        }
                    } else {
                        ((RowItemFreshunloadBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                        ((RowItemFreshunloadBinding) holder.binding).tvAlterUOM.setVisibility(View.GONE);
                        ((RowItemFreshunloadBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
                    }

                    ((RowItemFreshunloadBinding) holder.binding).imageView2.setLetter(mItem.getItemName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowItemFreshunloadBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    ((RowItemFreshunloadBinding) holder.binding).chk.setVisibility(View.GONE);


                    /*if (arrCheckItem.contains(mItem.getItemId()))
                        ((RowItemFreshunloadBinding) holder.binding).chk.setChecked(true);
                    else
                        ((RowItemFreshunloadBinding) holder.binding).chk.setChecked(false);

                    ((RowItemFreshunloadBinding) holder.binding).chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                arrCheckItem.add(mItem.getItemId());
                            } else {
                                arrCheckItem.remove(mItem.getItemId());
                            }
                        }
                    });*/

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((RowItemFreshunloadBinding) holder.binding).chk.isChecked())
                                makeDilog(BadReturnActivity.this, mItem);
                            else
                                UtilApp.displayAlert(BadReturnActivity.this, "Please select item first!");
                        }
                    });
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_freshunload;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUnload:
                if (arrCheckItem.size() > 0) {
                    new getNextView().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Please select at least one item!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void makeDilog(final Context context, final Item item) {
        List<String> list = new ArrayList<String>();

        list.add("Select Reason");
        list.add("Theft/Variance");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        LayoutInflater dialog = LayoutInflater.from(context);
        DialogInputUnloadBinding dialogBinding = DataBindingUtil.inflate(dialog, R.layout.dialog_input_unload, null, false);
        final View dialogView = dialogBinding.getRoot();
        dialogBinding.spReason.setAdapter(dataAdapter);
        dialogBinding.llReason.setVisibility(View.GONE);

        dialogBinding.txtName.setText(item.getItemName());
        dialogBinding.edtSaleAlter.setText(item.getAlterUOMQty());
        dialogBinding.edtSaleBase.setText(item.getBaseUOMQty());

        if (Integer.parseInt(item.getBaseUOMQty()) > 0) {
            dialogBinding.edtSaleBase.setEnabled(true);
            dialogBinding.edtSaleBase.setClickable(true);
            dialogBinding.edtActBase.setEnabled(true);
            dialogBinding.edtActBase.setClickable(true);
            dialogBinding.edtActBase.setAlpha(1.0f);
            dialogBinding.edtSaleBase.setAlpha(1.0f);
        } else {
            dialogBinding.edtSaleBase.setEnabled(false);
            dialogBinding.edtSaleBase.setClickable(false);
            dialogBinding.edtActBase.setEnabled(false);
            dialogBinding.edtActBase.setClickable(false);
            dialogBinding.edtActBase.setAlpha(0.5f);
            dialogBinding.edtSaleBase.setAlpha(0.5f);
        }

        if (Integer.parseInt(item.getAlterUOMQty()) > 0) {
            dialogBinding.edtSaleAlter.setEnabled(true);
            dialogBinding.edtSaleAlter.setClickable(true);
            dialogBinding.edtActAlter.setEnabled(true);
            dialogBinding.edtActAlter.setClickable(true);
            dialogBinding.edtActAlter.setAlpha(1.0f);
            dialogBinding.edtSaleAlter.setAlpha(1.0f);
        } else {
            dialogBinding.edtSaleAlter.setEnabled(false);
            dialogBinding.edtSaleAlter.setClickable(false);
            dialogBinding.edtActAlter.setEnabled(false);
            dialogBinding.edtActAlter.setClickable(false);
            dialogBinding.edtActAlter.setAlpha(0.5f);
            dialogBinding.edtSaleAlter.setAlpha(0.5f);
        }

        final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.setView(dialogView);

        dialogBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String text = dialogBinding.spReason.getSelectedItem().toString();
//
//                if (text.equalsIgnoreCase("Select Reason")) {
//                    Toast.makeText(getApplicationContext(), "Please select reason", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                alterActQty = dialogBinding.edtActAlter.getText().toString().isEmpty() ? "0" : dialogBinding.edtActAlter.getText().toString();
                baseActQty = dialogBinding.edtActBase.getText().toString().isEmpty() ? "0" : dialogBinding.edtActBase.getText().toString();

                if (Integer.parseInt(alterActQty) > 0) {
                    int qty = Integer.parseInt(item.getAlterUOMQty()) - Integer.parseInt(alterActQty);
                    alterActQty = String.valueOf(qty);
                }

                if (Integer.parseInt(baseActQty) > 0) {
                    int qty = Integer.parseInt(item.getBaseUOMQty()) - Integer.parseInt(baseActQty);
                    baseActQty = String.valueOf(qty);
                }

                Item mItem = arrItem.get(itemPosition);
                mItem.setSaleAltQty(alterActQty);
                mItem.setSaleBaseQty(baseActQty);
                mItem.setReasonCode("5");
                arrItem.set(itemPosition, mItem);
                mAdapter.notifyDataSetChanged();

                deleteDialog.dismiss();

            }
        });
        deleteDialog.show();
    }

    private String getReasonCode(String name) {
        String code = "0";

        switch (name) {
            case "Theft/Variance":
                code = "4";
                break;
        }
        return code;

    }

    private class getNextView extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new LoadingSpinner(BadReturnActivity.this);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (arrCheckItem.size() > 0) {
                for (int i = 0; i < arrItem.size(); i++) {
                    if (arrCheckItem.contains(arrItem.get(i).getItemId())) {

                        if (Integer.parseInt(arrItem.get(i).getSaleBaseQty()) > 0 && Integer.parseInt(arrItem.get(i).getSaleAltQty()) > 0) {
                            //Insert into Variance
                            db.insertUnLoadVariance(arrItem.get(i));
                        } else {
                            Item mItem = arrItem.get(i);
                            if (Integer.parseInt(arrItem.get(i).getSaleBaseQty()) > 0) {
                                mItem.setSaleBaseQty(arrItem.get(i).getSaleBaseQty());
                                mItem.setSaleAltQty(arrItem.get(i).getAlterUOMQty());
                            } else if (Integer.parseInt(arrItem.get(i).getSaleAltQty()) > 0) {
                                mItem.setSaleBaseQty(arrItem.get(i).getBaseUOMQty());
                                mItem.setSaleAltQty(arrItem.get(i).getSaleAltQty());
                            } else {
                                mItem.setSaleBaseQty(arrItem.get(i).getBaseUOMQty());
                                mItem.setSaleAltQty(arrItem.get(i).getAlterUOMQty());
                                mItem.setReasonCode("5");
                            }

                            //Insert into Variance
                            db.insertUnLoadVariance(arrItem.get(i));
                        }

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }

            Settings.setString(App.IS_BADCAPTURE, "1");
            finish();
        }
    }
}
