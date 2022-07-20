package com.mobiato.sfa.Fragments;

import android.app.SearchManager;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.Adapter.VanStockItemAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.FragmentVanStockBinding;
import com.mobiato.sfa.databinding.RowItemCategoryBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Category;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.SEARCH_SERVICE;

public class VanStockFragment extends Fragment {

    public FragmentVanStockBinding binding;
    public ArrayList<Category> arrCategory = new ArrayList<>();
    public ArrayList<Item> arrItem = new ArrayList<>();
    public ArrayList<Item> arrAllItem = new ArrayList<>();
    private CommonAdapter<Category> mCategoryAdapter;
    private VanStockItemAdapter mAdapter;
    int catPosition = 0;

    private DBManager db;
    private SearchView searchView;

    // TODO: Rename and change types and number of parameters
    public static VanStockFragment newInstance() {
        VanStockFragment fragment = new VanStockFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_van_stock, container, false);
        View view = binding.getRoot();

        db = new DBManager(getActivity());
        arrItem = new ArrayList<>();
        arrAllItem = new ArrayList<>();
        arrAllItem = db.getAllVanStockItemList();

        setCategory();
        setCategoryAdapter();

        //get Vanstock Item
        getVanStockItem(arrCategory.get(0));

        binding.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilApp.dialogPrint(getActivity(), new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        String selection = (String) o;
                        if (selection.equalsIgnoreCase("yes")) {

                            JSONArray jsonArray = createPrintData();
                            PrintLog object = new PrintLog(getActivity(),
                                    getActivity());
                            object.execute("", jsonArray);
                        }
                    }
                });
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getContext().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setHint("Search");
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));

        ImageView searchMagIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchMagIcon.setImageResource(R.drawable.ic_action_action_search);

        ImageView searchMagCloseIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchMagCloseIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (arrItem.size() > 0)
                    mAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void getVanStockItem(Category mCategory) {
        arrItem = db.getVanStockItemList(mCategory.catId);

        if (arrItem.size() > 0) {
            binding.lytNoData.setVisibility(View.GONE);
            binding.rvItemList.setVisibility(View.VISIBLE);
            setAdapter();
        } else {
            binding.lytNoData.setVisibility(View.VISIBLE);
            binding.rvItemList.setVisibility(View.GONE);
            binding.tvNoText.setText("No Items for " + mCategory.name + " in VanStock!");
        }
    }

    private void setCategory() {
        arrCategory = new ArrayList<>();
        Category c1 = new Category();
        c1.name = "CSD";
        c1.catId = "2";
        c1.is_click = "1";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Juice";
        c1.catId = "4";
        c1.is_click = "0";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Water";
        c1.catId = "3";
        c1.is_click = "0";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Biscuit";
        c1.catId = "1";
        c1.is_click = "0";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Hamper";
        c1.catId = "5";
        c1.is_click = "0";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Confectionary";
        c1.catId = "6";
        c1.is_click = "0";
        arrCategory.add(c1);

    }

    private void setCategoryAdapter() {

        mCategoryAdapter = new CommonAdapter<Category>(arrCategory) {

            @Override
            public void onBind(CommonViewHolder holder, int position) {

                if (holder.binding instanceof RowItemCategoryBinding) {
                    ((RowItemCategoryBinding) holder.binding).tvCategory.setText(arrCategory.get(position).name);

                    if (arrCategory.get(position).is_click.equals("1")) {
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setBackgroundResource(R.drawable.rounded_rect_blue_capsule);
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setBackgroundResource(R.drawable.rounded_rect_white_capsule);
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setTextColor(Color.parseColor("#606AA9"));
                    }

                    ((RowItemCategoryBinding) holder.binding).tvCategory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            arrCategory.get(position).is_click = "1";
                            arrCategory.get(catPosition).is_click = "0";
                            catPosition = position;
                            notifyDataSetChanged();

                            if (!searchView.isIconified()) {
                                searchView.setIconified(true);
                                searchView.clearFocus();
                                UtilApp.hideSoftKeyboard(getActivity());
                            }

                            //get Item from VanStock
                            getVanStockItem(arrCategory.get(position));
                        }
                    });
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_category;
            }
        };

        binding.rvCategory.setAdapter(mCategoryAdapter);
    }

    private void setAdapter() {

        mAdapter = new VanStockItemAdapter(getActivity(), arrItem);
        binding.rvItemList.setAdapter(mAdapter);
    }

    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createPrintData() {
        JSONArray jArr = new JSONArray();

        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.VAN_STOCK);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTEID));
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm"));

            JSONArray TOTAL = new JSONArray();
            JSONArray jData = new JSONArray();
            int totalPcs = 0, totalCase = 0;
            int totalSale = 0, totalCaseSale = 0;
            int totalRemaining = 0, totalRemainingCase = 0;

            for (Item obj : arrAllItem) {

                JSONArray data = new JSONArray();
                data.put(obj.getItemCode());
                data.put(obj.getItemName());
                data.put(obj.getActuakBaseQty() + "/" + obj.getActualAltQty());
                String Sale;
                Sale = String.valueOf(db.getSaleItemQty(obj.getItemId(), "Base"));
                String SaleAl = String.valueOf(db.getSaleItemQty(obj.getItemId(), "Alter"));

                data.put(Sale + "/" + SaleAl);
                data.put(obj.getBaseUOMQty() + "/" + obj.getAlterUOMQty());
                totalPcs += Double.parseDouble(obj.getActuakBaseQty());
                totalSale += Integer.parseInt(Sale);
                totalRemaining += Double.parseDouble(obj.getBaseUOMQty());

                totalCase += Double.parseDouble(obj.getActualAltQty());
                totalCaseSale += Integer.parseInt(SaleAl);
                totalRemainingCase += Double.parseDouble(obj.getAlterUOMQty());

                jData.put(data);

            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("LOADED QTY", "+" + String.valueOf(totalPcs) + "/" + String.valueOf(totalCase));
            totalObj.put("SALE QTY", "-" + String.valueOf(totalSale) + "/" + String.valueOf(totalCaseSale));
            totalObj.put("TRUCK STOCK", "+" + String.valueOf(totalRemaining) + "/" + String.valueOf(totalRemainingCase));
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            mainArr.put("data", jData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jArr;
    }

}
