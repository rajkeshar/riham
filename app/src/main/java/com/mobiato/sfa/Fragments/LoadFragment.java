package com.mobiato.sfa.Fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.LoadVerifyActivity;
import com.mobiato.sfa.databinding.FragmentLoadBinding;
import com.mobiato.sfa.databinding.RowItemLoadBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Load;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadFragment extends Fragment {

    public FragmentLoadBinding binding;
    public ArrayList<Load> arrLoad = new ArrayList<>();
    private CommonAdapter<Load> mAdapter;
    private DBManager db;


    public LoadFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        LoadFragment fragment = new LoadFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_load, container, false);
        View view = binding.getRoot();

        UtilApp.logData(getActivity(), "LoadFragment OnScreen");

        db = new DBManager(getActivity());
        arrLoad = new ArrayList<>();
        arrLoad = db.getAllLoad();

        //set Data into List
        setAdapter();

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoad();
            }
        });


        return view;
    }


    private void setAdapter() {
        mAdapter = new CommonAdapter<Load>(arrLoad) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemLoadBinding) {
                    ((RowItemLoadBinding) holder.binding).tvLoadId.setText("Load No: " + arrLoad.get(position).getLoad_no());
                    ((RowItemLoadBinding) holder.binding).tvDate.setText("Delivery Date: " + arrLoad.get(position).getDel_date());

                    if (arrLoad.get(position).is_verified.equals("0")) {
                        ((RowItemLoadBinding) holder.binding).imgVerified.setImageResource(R.drawable.ic_icon_block_sel);
                    } else {
                        ((RowItemLoadBinding) holder.binding).imgVerified.setImageResource(R.drawable.ic_icon_verified_sel);
                    }

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UtilApp.logData(getActivity(), "OnCLick Load Items");
                            if (arrLoad.get(position).is_verified.equals("0")) {
                                Intent in = new Intent(getActivity(), LoadVerifyActivity.class);
                                in.putExtra("Load", arrLoad.get(position));
                                startActivity(in);
                            }
                        }
                    });
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_load;
            }
        };

        binding.rvLoad.setAdapter(mAdapter);
    }

    public void getLoad() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getLoadList(App.LOADLIST, Settings.getString(App.ROUTEID), Settings.getString(App.SALESMANID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Load Response", response.body().toString());
                binding.swipeRefreshLayout.setRefreshing(false);
                UtilApp.logData(getActivity(), "Load Response" + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {

                        ArrayList<Load> arrLoad = new ArrayList<>();
                        arrLoad = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Load>>() {
                                }.getType());

                        db.insertLoad(arrLoad);

                        refreshLoad();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Load Fail", error.getMessage());
                UtilApp.logData(getActivity(), "Load Fails");
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void refreshLoad() {

        if (db != null) {
            arrLoad = new ArrayList<>();
            arrLoad = db.getAllLoad();

            //set Data into List
            setAdapter();
        }
    }

}
