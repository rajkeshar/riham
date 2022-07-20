package com.mobiato.sfa.merchandising;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityStoreCheckBrandBinding;
import com.mobiato.sfa.databinding.ListviewItemBinding;
import com.mobiato.sfa.databinding.RowStoreCheckBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Category;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Distribution;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;

public class StoreCheckBrandActivity extends BaseActivity {

    private ActivityStoreCheckBrandBinding binding;
    private ArrayList<Category> arrData = new ArrayList<>();
    private ArrayList<Distribution> arrToolsData = new ArrayList<>();
    private ArrayList<Distribution> arrCatTool = new ArrayList<>();
    private CommonAdapter<Category> mAdapter;
    private CommonAdapter<Distribution> mAdapter1;
    private DBManager db;
    RecyclerView list1;
    Customer customer;
    ListView list;
    Dialog listDialog;
    private String itemCategory = "", customerId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreCheckBrandBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle(getString(R.string.title_StoreBrands));

        customerId = getIntent().getStringExtra("CustomerId");

        db = new DBManager(this);
        customer = db.getCustomerDetail(customerId);
        System.out.println("Customer Details: " + customer);
        binding.txtCountCustomer.setText(customer.getCustomerName());
        arrToolsData = new ArrayList<>();
        arrToolsData = db.getrToolsList(customer.getCustomerId());

        setMainData();
        setMainAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        binding.rvStoreList.setLayoutManager(gridLayoutManager);
    }

    private void setMainData() {
        arrData = new ArrayList<>();

        Category c1 = new Category();
        c1.name = "CSD";
        c1.catId = "2";
        c1.is_click = "1";
        arrData.add(c1);

        c1 = new Category();
        c1.name = "Juice";
        c1.catId = "4";
        c1.is_click = "0";
        arrData.add(c1);

        c1 = new Category();
        c1.name = "Water";
        c1.catId = "3";
        c1.is_click = "0";
        arrData.add(c1);

        c1 = new Category();
        c1.name = "Biscuit";
        c1.catId = "1";
        c1.is_click = "0";
        arrData.add(c1);
    }

    private void setMainAdapter() {

        mAdapter = new CommonAdapter<Category>(arrData) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowStoreCheckBinding) {
                    ((RowStoreCheckBinding) holder.binding).setItem(arrData.get(position));
                    holder.binding.executePendingBindings();
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemCategory = arrData.get(position).catId;
                        getCategoryToolList(itemCategory);
                    }
                });

            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_store_check;
            }
        };

        binding.rvStoreList.setAdapter(mAdapter);
    }

    private void getCategoryToolList(String catId) {
        arrCatTool = new ArrayList<>();

        for (int i = 0; i < arrToolsData.size(); i++) {
            if (arrToolsData.get(i).getItem_Category().equalsIgnoreCase(catId)) {
                if (arrCatTool.size() > 0) {
                    boolean isCOntain = false;
                    for (int j = 0; j < arrCatTool.size(); j++) {
                        if (arrCatTool.get(j).getDistribution_Tool_Id().equalsIgnoreCase(arrToolsData.get(i).getDistribution_Tool_Id())) {
                            isCOntain = true;
                            break;
                        }
                    }

                    if (!isCOntain)
                        arrCatTool.add(arrToolsData.get(i));
                } else {
                    arrCatTool.add(arrToolsData.get(i));
                }

            }
        }

        if (arrCatTool.size() > 0) {
            showdialog();
        } else {
            UtilApp.displayAlert(StoreCheckBrandActivity.this, "No display tool available for this category!");
        }
    }

    private void showdialog() {
        listDialog = new Dialog(this);
        listDialog.setTitle("Select Item");
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.distribution_display_lit, null, false);
        listDialog.setContentView(v);
        listDialog.setCancelable(true);
        listDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        list1 = (RecyclerView) listDialog.findViewById(R.id.mylistview);
        TextView txtTitle = (TextView) listDialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Select Distribution Tools");
        setMainAdapter1();

        listDialog.show();
    }

    private void setMainAdapter1() {

        mAdapter1 = new CommonAdapter<Distribution>(arrCatTool) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof ListviewItemBinding) {
                    ((ListviewItemBinding) holder.binding).tvtextItem.setText(arrCatTool.get(position).getDistribution_Tool_Name());
                    holder.binding.executePendingBindings();
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listDialog.dismiss();
                        Intent intent = new Intent(StoreCheckBrandActivity.this, DiskAreaCheck.class);
                        intent.putExtra("title", arrCatTool.get(position).getDistribution_Tool_Name());
                        intent.putExtra("id", arrCatTool.get(position).getDistribution_Tool_Id());
                        intent.putExtra("item_id", itemCategory);
                        intent.putExtra("customer_id", customerId);
                        startActivity(intent);

                    }
                });

            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.listview_item;
            }
        };

        list1.setAdapter(mAdapter1);
    }

}
