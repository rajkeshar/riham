package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityCompetitorInformationBinding;
import com.mobiato.sfa.databinding.RowCompititorBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Compititor;

import java.io.File;
import java.util.ArrayList;

public class CompetitorInformationActivity extends BaseActivity {

    public ActivityCompetitorInformationBinding binding;
    private DBManager db;
    private ArrayList<Compititor> arrData = new ArrayList<>();
    private CommonAdapter<Compititor> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompetitorInformationBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setNavigationView();
        setTitle("COMPETITOR INFORMATION");

        db = new DBManager(this);
        arrData = new ArrayList<>();
        arrData = db.getCompititorList();
        setMainAdapter();

        if (arrData.size() > 0) {
            binding.rvCompititorList.setVisibility(View.VISIBLE);
            binding.txtNoContain.setVisibility(View.GONE);
        }
        binding.btnAddCompititor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(me, AddCompetitiorActivity.class);
                intent.putExtra("type", "Add");
                startActivity(intent);
            }
        });
    }

    private void setMainAdapter() {

        mAdapter = new CommonAdapter<Compititor>(arrData) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowCompititorBinding) {
                    ((RowCompititorBinding) holder.binding).setItem(arrData.get(position));
                    holder.binding.executePendingBindings();
                    ((RowCompititorBinding) holder.binding).txtTitle.setText(arrData.get(position).getCompititorCompanyName());
                    ((RowCompititorBinding) holder.binding).txtBrand.setText(arrData.get(position).getCompititor_brand());
                    ((RowCompititorBinding) holder.binding).txtItem.setText(arrData.get(position).getCompititor_ItemName());
                    ((RowCompititorBinding) holder.binding).txtPrice.setText(arrData.get(position).getCOMPITITOR_Price());
                    String path = "/mnt/sdcard/Riham/" + arrData.get(position).getCompititor_Image1();
                    File f = new File(path);  //
                    Uri imageUri = Uri.fromFile(f);
                    ((RowCompititorBinding) holder.binding).profileImage.setImageURI(imageUri);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(me, AddCompetitiorActivity.class);
                            intent.putExtra("type", "Details");
                            intent.putExtra("id", arrData.get(position).getCompititorId());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_compititor;
            }
        };

        binding.rvCompititorList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        db = new DBManager(this);
        arrData = new ArrayList<>();
        arrData = db.getCompititorList();
        setMainAdapter();

        if (arrData.size() > 0) {
            binding.rvCompititorList.setVisibility(View.VISIBLE);
            binding.txtNoContain.setVisibility(View.GONE);
        }
    }
}
