package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityCampaignListBinding;
import com.mobiato.sfa.databinding.RowCompaignBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Compaign;

import java.io.File;
import java.util.ArrayList;

public class CampaignListActivity extends BaseActivity {

    private ActivityCampaignListBinding binding;
    private DBManager db;
    private ArrayList<Compaign> arrData = new ArrayList<>();
    private CommonAdapter<Compaign> mAdapter;
    String CustomerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCampaignListBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("CAMPAIGN LIST");

        //get Customer List
        db = new DBManager(this);
        CustomerId = getIntent().getStringExtra("CustomerId");
        arrData = new ArrayList<>();
        arrData = db.getCompaignList();
        setMainAdapter();

        if (arrData.size() > 0) {
            binding.rvCompaignList.setVisibility(View.VISIBLE);
            binding.txtNoContain.setVisibility(View.GONE);
        }

        binding.btnAddCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(me, AddCampaignActivity.class);
                intent.putExtra("CustomerId", CustomerId);
                intent.putExtra("type", "Add");
                startActivity(intent);
            }
        });

    }

    private void setMainAdapter() {

        mAdapter = new CommonAdapter<Compaign>(arrData) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowCompaignBinding) {
                    ((RowCompaignBinding) holder.binding).setItem(arrData.get(position));
                    holder.binding.executePendingBindings();
                    ((RowCompaignBinding) holder.binding).txtCompaignId.setText(arrData.get(position).getCompaignId());
                    ((RowCompaignBinding) holder.binding).txtComment.setText(arrData.get(position).getComment());
                    String path = "/mnt/sdcard/Riham/" + arrData.get(position).getCompaign_Image1();
                    File f = new File(path);  //
                    Uri imageUri = Uri.fromFile(f);
                    ((RowCompaignBinding) holder.binding).profileImage.setImageURI(imageUri);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(me, AddCampaignActivity.class);
                            intent.putExtra("type", "Details");
                            intent.putExtra("CustomerId", CustomerId);
                            intent.putExtra("id", arrData.get(position).getCompaignId());
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_compaign;
            }
        };

        binding.rvCompaignList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //get Customer List
        db = new DBManager(this);
        arrData = new ArrayList<>();
        arrData = db.getCompaignList();
        setMainAdapter();

        if (arrData.size() > 0) {
            binding.rvCompaignList.setVisibility(View.VISIBLE);
            binding.txtNoContain.setVisibility(View.GONE);
        }

    }

}
