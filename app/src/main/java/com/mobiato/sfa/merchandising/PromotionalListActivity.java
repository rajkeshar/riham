package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityPromotionalListBinding;
import com.mobiato.sfa.databinding.RowPromotionalBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Promotion;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PromotionalListActivity extends BaseActivity {

    private ActivityPromotionalListBinding binding;
    private DBManager db;
    private ArrayList<Promotion> arrData = new ArrayList<>();
    private CommonAdapter<Promotion> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPromotionalListBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setNavigationView();
        setTitle("PROMOTIONAL ACCOUNTABILITY");

        db = new DBManager(this);
        arrData = new ArrayList<>();
        arrData = db.getPromotionList();

        setMainAdapter();

        if (arrData.size() > 0) {
            binding.txtNoContain.setVisibility(View.GONE);
            binding.rvPromotionalList.setVisibility(View.VISIBLE);
        }

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(me, AddPromotionalActivity.class).putExtra("type", "Add"));
            }
        });
    }

    private void setMainAdapter() {

        mAdapter = new CommonAdapter<Promotion>(arrData) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowPromotionalBinding) {
                    ((RowPromotionalBinding) holder.binding).setItem(arrData.get(position));
                    holder.binding.executePendingBindings();
                    ((RowPromotionalBinding) holder.binding).txtTitle.setText(arrData.get(position).getPromotioncustomerName());
                    ((RowPromotionalBinding) holder.binding).txtPhone.setText(arrData.get(position).getPromotioncustPhone());
                    ((RowPromotionalBinding) holder.binding).txtAmount.setText(arrData.get(position).getAmount());
                    ((RowPromotionalBinding) holder.binding).txtHamper.setText(arrData.get(position).getPromotionItemName());
                    Pattern p = Pattern.compile("\\b[a-zA-Z]");
                    Matcher m = p.matcher(arrData.get(position).getPromotioncustomerName());
                    if (m.find())
                        System.out.print(m.group());
                    ((RowPromotionalBinding) holder.binding).txtShortName.setText(m.group());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           /* Intent intent = new Intent(me, AddPromotionalActivity.class);
                            intent.putExtra("type", "Details");
                            intent.putExtra("id", arrData.get(position).getPromotionId());
                            startActivity(intent);*/
                        }
                    });
                }

            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_promotional;
            }
        };

        binding.rvPromotionalList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        db = new DBManager(this);
        arrData = new ArrayList<>();
        arrData = db.getPromotionList();
        setMainAdapter();

        if (arrData.size() > 0) {
            binding.txtNoContain.setVisibility(View.GONE);
            binding.rvPromotionalList.setVisibility(View.VISIBLE);
        }
    }
}
