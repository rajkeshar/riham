package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityComplaintListBinding;
import com.mobiato.sfa.databinding.RowComplainBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Complain;

import java.io.File;
import java.util.ArrayList;

public class ComplaintListActivity extends BaseActivity {

    public ActivityComplaintListBinding binding;
    private DBManager db;
    private ArrayList<Complain> arrData = new ArrayList<>();
    private CommonAdapter<Complain> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintListBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("COMPLAINT/FEEDBACK");

        db = new DBManager(this);
        arrData = new ArrayList<>();
        arrData = db.getAllComplainList();

        setMainAdapter();

        if (arrData.size() > 0) {
            binding.txtNoContain.setVisibility(View.GONE);
            binding.rvComplaintList.setVisibility(View.VISIBLE);
        }

        binding.btnAddComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(me, AddComplaintFeedbackActivity.class);
                intent.putExtra("type", "Add");
                startActivity(intent);
            }
        });
    }

    private void setMainAdapter() {

        mAdapter = new CommonAdapter<Complain>(arrData) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowComplainBinding) {
                    ((RowComplainBinding) holder.binding).setItem(arrData.get(position));
                    holder.binding.executePendingBindings();
                    ((RowComplainBinding) holder.binding).txtTitle.setText(arrData.get(position).getComplain_Feedback());
                    ((RowComplainBinding) holder.binding).txtBrand.setText(arrData.get(position).getComplain_brand());
                    ((RowComplainBinding) holder.binding).txtComlaint.setText(arrData.get(position).getComplanin_Note());
                    String path = "/mnt/sdcard/Riham/" + arrData.get(position).getComplain_Image1();
                    File f = new File(path);  //
                    Uri imageUri = Uri.fromFile(f);
                    ((RowComplainBinding) holder.binding).profileImage.setImageURI(imageUri);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(me, AddComplaintFeedbackActivity.class);
                            intent.putExtra("type", "Details");
                            intent.putExtra("id", arrData.get(position).getComplainId());
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_complain;
            }
        };

        binding.rvComplaintList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        db = new DBManager(this);
        arrData = new ArrayList<>();
        arrData = db.getAllComplainList();
        setMainAdapter();

        if (arrData.size() > 0) {
            binding.txtNoContain.setVisibility(View.GONE);
            binding.rvComplaintList.setVisibility(View.VISIBLE);
        }
    }
}
