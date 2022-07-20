package com.mobiato.sfa.merchandising;

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
import com.mobiato.sfa.databinding.ActivityMerchantDataPostingBinding;
import com.mobiato.sfa.databinding.RowItemPostingBinding;
import com.mobiato.sfa.databinding.RowItemPostingMerchBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.rest.SyncData;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;

public class MerchantDataPostingActivity extends BaseActivity {

    private ActivityMerchantDataPostingBinding binding;
    private ArrayList<Transaction> arrData = new ArrayList<>();
    private CommonAdapter<Transaction> mAdapter;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMerchantDataPostingBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Print Transactions");
        setNavigationView();

        db = new DBManager(MerchantDataPostingActivity.this);
        arrData = new ArrayList<>();
        arrData = db.getAllTransactions();

        registerReceiver(broadcastReceiver, new IntentFilter(SyncData.BROADCAST_ACTION));

        //setData();
        setAdapter();

        if (arrData.size() > 0) {
            binding.btnSync.setVisibility(View.VISIBLE);
        }

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
                        if (!arrData.get(i).tr_is_posted.equals("Yes"))
                            arrData.get(i).setTr_isCheck(type);
                    }

                    setAdapter();
                }
            }
        });

        binding.btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < arrData.size(); i++) {
                    Transaction item = arrData.get(i);
                    if (item.getTr_isCheck().equalsIgnoreCase("true")) {
                        if (item.tr_is_posted.equalsIgnoreCase("Fail") ||
                                item.tr_is_posted.equalsIgnoreCase("No")) {
                            switch (item.tr_type) {
                                case Constant.TRANSACTION_TYPES.TT_COMPLAINT_FEEDBACK:
                                    db.markForCOmplaintPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_COMPAIGN_FEEDBACK:
                                    db.markForCompaignPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_CHILER_REQUEST:
                                    db.markForChillerRequestPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_RETURN_CREATED:
                                    db.markForReturnPost(item.tr_order_id);
                                    break;
                                case Constant.TRANSACTION_TYPES.TT_OREDER_CREATED:
                                    db.markForOrderPost(item.tr_order_id);
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
                if (holder.binding instanceof RowItemPostingMerchBinding) {
                    Transaction item = arrData.get(position);
                    switch (item.tr_type) {
                        case Constant.TRANSACTION_TYPES.TT_COMPLAINT_FEEDBACK:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("COMP");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_COMPAIGN_FEEDBACK:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("CAMP");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_PLANOGRAM:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("PLN");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_RETURN_CREATED:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("RT");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_OREDER_CREATED:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("ORD");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_COMPATITOR:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("CMPT");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_INVENTORY:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("INV");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_ASSETS:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("ASSETS");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CONS_SURVEY:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("CON_SRV");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_ASSETS_SURVEY:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("ASSET_SRV");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_SENSURY_SURVEY:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("SEN_SRV");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_DISTRIBUTION_SURVEY:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("TOOL_SRV");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_EXPIRYITEM:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("EXPR");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_DERITEM:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("DER");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_PRAMOTION:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("PRA");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                        case Constant.TRANSACTION_TYPES.TT_CHILER_REQUEST:
                            ((RowItemPostingMerchBinding) holder.binding).tvType.setText("CR");
                            ((RowItemPostingMerchBinding) holder.binding).tvTransaction.setText(item.tr_order_id);
                            break;
                    }

                    if (item.tr_is_posted.equalsIgnoreCase("Yes")) {
                        ((RowItemPostingMerchBinding) holder.binding).ivSync.setImageResource(R.drawable.ic_icon_verified_sel);
                    } else {
                        ((RowItemPostingMerchBinding) holder.binding).ivSync.setImageResource(R.drawable.ic_action_sync);
                    }

                    if (item.tr_isCheck.equalsIgnoreCase("true")) {
                        ((RowItemPostingMerchBinding) holder.binding).chk.setChecked(true);
                    } else {
                        ((RowItemPostingMerchBinding) holder.binding).chk.setChecked(false);
                    }

                    ((RowItemPostingMerchBinding) holder.binding).chk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrData.get(position).getTr_isCheck().equalsIgnoreCase("false")) {
                                ((RowItemPostingMerchBinding) holder.binding).chk.setChecked(true);
                                arrData.get(position).setTr_isCheck("true");
                            } else {
                                ((RowItemPostingMerchBinding) holder.binding).chk.setChecked(false);
                                arrData.get(position).setTr_isCheck("false");
                            }

                            notifyDataSetChanged();
                        }
                    });

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_posting_merch;
            }
        };

        binding.rvList.setAdapter(mAdapter);
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
}
