package com.mobiato.sfa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.Adapter.CustomerAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityMasterDataBinding;
import com.mobiato.sfa.databinding.RowCategoryBinding;
import com.mobiato.sfa.databinding.RowChannelBinding;
import com.mobiato.sfa.databinding.RowCustomerMainBinding;
import com.mobiato.sfa.databinding.RowItemMasterBinding;
import com.mobiato.sfa.databinding.RowItemPromotionMasterBinding;
import com.mobiato.sfa.databinding.RowSubCategoryBinding;
import com.mobiato.sfa.databinding.RowTicketItemBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Category;
import com.mobiato.sfa.model.ChannellType;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerType;
import com.mobiato.sfa.model.FOCItems;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.NatureMaster;
import com.mobiato.sfa.model.Pricing;
import com.mobiato.sfa.model.PromoItems;
import com.mobiato.sfa.model.PromoOfferData;
import com.mobiato.sfa.model.PromotionData;
import com.mobiato.sfa.model.RecentCustomer;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.SubCategoryType;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;

public class MasterDataActivity extends BaseActivity implements View.OnClickListener {

    private ActivityMasterDataBinding binding;
    private String type = "", routeId = "", agentId = "", dept = "";
    private CommonAdapter<Customer> mAdapter;
    private CommonAdapter<Item> mItemAdapter;
    private CommonAdapter<PromoOfferData> mPromotionAdapter;
    private CommonAdapter<Category> mCategoryAdapter;
    private CommonAdapter<SubCategoryType> mSubCategoryAdapter;
    private CommonAdapter<ChannellType> mChannelAdapter;
    private CommonAdapter<NatureMaster> mNatureAdapter;
    private ArrayList<Customer> arrCustomer = new ArrayList<>();
    private ArrayList<Item> arrMaterial = new ArrayList<>();
    private ArrayList<PromoOfferData> arrPromotion = new ArrayList<>();
    private ArrayList<Category> arrCategory = new ArrayList<>();
    private ArrayList<ChannellType> arrChannel = new ArrayList<>();
    private ArrayList<SubCategoryType> arrSubCategory = new ArrayList<>();
    private DBManager db;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private LoadingSpinner progressDialog;
    boolean isSequence = false;
    boolean isAll = false;
    private ArrayList<Customer> arrData = new ArrayList<>();
    private Salesman mSalesman;
    String customer_count = "2";
    private ArrayList<NatureMaster> arrNatature = new ArrayList<>();
    private boolean isClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMasterDataBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);

        if (getIntent() != null) {
            type = getIntent().getStringExtra("Type");
        }

        db = new DBManager(this);

        if (type.equalsIgnoreCase("Customer")) {
            setTitle("Customer");
            binding.llTab.setVisibility(View.GONE);
            routeId = Settings.getString(App.ROUTEID);

            if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Merchandising")) {
                isSequence = false;
                isAll = true;
                customer_count = "0";
                arrCustomer = new ArrayList<>();
                arrCustomer = db.getCutomerList();
                arrData = new ArrayList<>();
                arrData = db.getCutomerList();
            } else {
                arrCustomer = new ArrayList<>();
                arrCustomer = db.getCutomerDepotList();
                arrData = new ArrayList<>();
                arrData = db.getCutomerDepotList();
            }
//            arrData = new ArrayList<>();
//            arrData = db.getSeqCustomerList(UtilApp.getCurrentDay());
            System.out.println("pp--> " + (Settings.getString(App.DEPOTID)));
            // setCustomerAdapter();
            setMainAdapter();
        } else if (type.equalsIgnoreCase("Material")) {
            setTitle("Material");
            binding.llTab.setVisibility(View.GONE);
            routeId = Settings.getString(App.ROUTEID);
            arrMaterial = new ArrayList<>();
            arrMaterial = db.getAllItemList();
            setMaterialAdapter();
        } else if (type.equalsIgnoreCase("Discount")) {
            setTitle("Discount");
            binding.llTab.setVisibility(View.GONE);
        } else if (type.equalsIgnoreCase("AgentPrice")) {
            setTitle("Agent Price");
            binding.llTab.setVisibility(View.GONE);
            routeId = Settings.getString(App.ROUTEID);
            arrMaterial = new ArrayList<>();
            arrMaterial = db.getAllItemList();
            setMaterialAdapter();
        } else if (type.equalsIgnoreCase("Promotion")) {
            setTitle("Promotion");
            binding.llTab.setVisibility(View.GONE);
            routeId = Settings.getString(App.ROUTEID);
            agentId = Settings.getString(App.AGENTID);
            arrPromotion = new ArrayList<>();
            arrPromotion = db.getAllPromotion();

            ArrayList<PromoOfferData> arrCust = new ArrayList();
            arrCust = db.getAllCustomerPromotion();

            for (int i = 0; i < arrCust.size(); i++) {
                arrPromotion.add(arrCust.get(i));
            }
            setPromotionAdapter();
        } else if (type.equalsIgnoreCase("Category")) {
            setTitle("Category");
            binding.llTab.setVisibility(View.GONE);
            routeId = Settings.getString(App.ROUTEID);
            agentId = Settings.getString(App.AGENTID);
            arrCategory = new ArrayList<>();
            arrCategory = db.getCustCategoryList();
            setCateggoryAdapter();
        } else if (type.equalsIgnoreCase("Channel")) {
            setTitle("Channel");
            binding.llTab.setVisibility(View.GONE);
            routeId = Settings.getString(App.ROUTEID);
            agentId = Settings.getString(App.AGENTID);
            arrChannel = new ArrayList<>();
            arrChannel = db.getCustChannelList();
            setChannelAdapter();
        } else if (type.equalsIgnoreCase("SubCategory")) {
            setTitle("SubCategory");
            binding.llTab.setVisibility(View.GONE);
            routeId = Settings.getString(App.ROUTEID);
            agentId = Settings.getString(App.AGENTID);
            arrSubCategory = new ArrayList<>();
            arrSubCategory = db.getCustSubCategoryList();
            setSubCateggoryAdapter();
        } else if (type.equalsIgnoreCase("Ticket")) {
            setTitle("Ticket");
            binding.llTab.setVisibility(View.GONE);
            arrNatature = new ArrayList<>();
            arrNatature = db.getAllNatureData();
            setNatureAdapter();
        }

        progressDialog = new LoadingSpinner(MasterDataActivity.this);

        binding.fab.setOnClickListener(this);

        if (isSequence) {
            binding.btnSequence.setBackgroundResource(R.drawable.rounded_tab_select);
            binding.btnSequence.setTextColor(Color.parseColor("#FFFFFF"));
            binding.btnAll.setBackgroundResource(R.drawable.rounded_tab_unselect);
            binding.btnAll.setTextColor(Color.parseColor("#606AA9"));
            binding.btnDepot.setBackgroundResource(R.drawable.rounded_tab_unselect);
            binding.btnDepot.setTextColor(Color.parseColor("#606AA9"));
        } else {
            binding.btnAll.setBackgroundResource(R.drawable.rounded_tab_select);
            binding.btnAll.setTextColor(Color.parseColor("#FFFFFF"));
            binding.btnSequence.setBackgroundResource(R.drawable.rounded_tab_unselect);
            binding.btnSequence.setTextColor(Color.parseColor("#606AA9"));
            binding.btnDepot.setBackgroundResource(R.drawable.rounded_tab_unselect);
            binding.btnDepot.setTextColor(Color.parseColor("#606AA9"));
        }

        binding.btnSequence.setVisibility(View.GONE);
        binding.btnAll.setVisibility(View.GONE);

        binding.btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSequence = false;
                isAll = true;
                customer_count = "0";
                binding.btnAll.setBackgroundResource(R.drawable.rounded_tab_select);
                binding.btnAll.setTextColor(Color.parseColor("#FFFFFF"));
                binding.btnSequence.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnSequence.setTextColor(Color.parseColor("#606AA9"));
                binding.btnDepot.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnDepot.setTextColor(Color.parseColor("#606AA9"));

                arrData = new ArrayList<>();
                arrData = db.getCutomerList();
                setMainAdapter();
            }
        });

        binding.btnDepot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSequence = false;
                isAll = false;
                customer_count = "2";
                binding.btnAll.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnAll.setTextColor(Color.parseColor("#606AA9"));
                binding.btnSequence.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnSequence.setTextColor(Color.parseColor("#606AA9"));
                binding.btnDepot.setBackgroundResource(R.drawable.rounded_tab_select);
                binding.btnDepot.setTextColor(Color.parseColor("#FFFFFF"));

                arrData = new ArrayList<>();
                arrData = db.getCutomerDepotList();
                setMainAdapter();
            }
        });

        binding.btnSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSequence = true;
                isAll = false;
                customer_count = "1";
                binding.btnSequence.setBackgroundResource(R.drawable.rounded_tab_select);
                binding.btnSequence.setTextColor(Color.parseColor("#FFFFFF"));
                binding.btnAll.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnAll.setTextColor(Color.parseColor("#606AA9"));
                binding.btnDepot.setBackgroundResource(R.drawable.rounded_tab_unselect);
                binding.btnDepot.setTextColor(Color.parseColor("#606AA9"));
                arrData = new ArrayList<>();
                arrData = db.getSeqCustomerList(UtilApp.getCurrentDay());
                setMainAdapter();
            }
        });

    }

    private void setMainAdapter() {

        mAdapter = new CommonAdapter<Customer>(arrData) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowCustomerMainBinding) {
                    ((RowCustomerMainBinding) holder.binding).setItem(arrData.get(position));
                    ((RowCustomerMainBinding) holder.binding).imageView2.setLetter(arrData.get(position).getCustomerName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowCustomerMainBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    ((RowCustomerMainBinding) holder.binding).ivCheckIn.setVisibility(View.GONE);

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_customer_main;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    private void setCustomerAdapter() {

        mAdapter = new CommonAdapter<Customer>(arrCustomer) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowCustomerMainBinding) {
                    ((RowCustomerMainBinding) holder.binding).setItem(arrCustomer.get(position));
                    ((RowCustomerMainBinding) holder.binding).imageView2.setLetter(arrCustomer.get(position).getCustomerName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowCustomerMainBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    ((RowCustomerMainBinding) holder.binding).ivCheckIn.setVisibility(View.GONE);

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_customer_main;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    private void setMaterialAdapter() {

        mItemAdapter = new CommonAdapter<Item>(arrMaterial) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemMasterBinding) {
                    Item mItem = arrMaterial.get(position);
                    ((RowItemMasterBinding) holder.binding).setItem(mItem);
                    ((RowItemMasterBinding) holder.binding).imageView2.setLetter(mItem.getItemName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowItemMasterBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    double itemBasePrice = 0, itemAlterPrice = 0;
                    if (type.equalsIgnoreCase("Material")) {
                        itemBasePrice = db.getItemPrice(mItem.getItemId());
                        itemAlterPrice = db.getItemAlterPrice(mItem.getItemId());
                    } else {
                        if (db.isAgentPricingItems(mItem.getItemId(), routeId)) {
                            Item pItem = db.getAgentPricingItems(mItem.getItemId());
                            itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
                            itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                        } else {
                            itemBasePrice = 0;
                            itemAlterPrice = 0;
                        }
                    }

                    ((RowItemMasterBinding) holder.binding).tvUnitAlterPrice.setText("Unit Price: " + UtilApp.getNumberFormate((itemAlterPrice)) + " UGX");
                    ((RowItemMasterBinding) holder.binding).dividerUnitPrice.setVisibility(View.VISIBLE);
                    ((RowItemMasterBinding) holder.binding).tvUnitBaseUOMPrice.setVisibility(View.VISIBLE);
                    ((RowItemMasterBinding) holder.binding).tvUnitBaseUOMPrice.setText(UtilApp.getNumberFormate((itemBasePrice)) + " UGX");
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_master;
            }
        };

        binding.rvList.setAdapter(mItemAdapter);

    }

    private void setPromotionAdapter() {

        mPromotionAdapter = new CommonAdapter<PromoOfferData>(arrPromotion) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemPromotionMasterBinding) {
                    PromoOfferData mData = arrPromotion.get(position);

                    ((RowItemPromotionMasterBinding) holder.binding).tvName.setText("Promotion : " + mData.promotionName);
//                    ((RowItemPromotionMasterBinding) holder.binding).tvItemName.setText(mData.getItemName());
//                    ((RowItemPromotionMasterBinding) holder.binding).tvQty.setText("Qty: " + mData.getItemQTY() + " " + mData.getItemUOM());
//                    ((RowItemPromotionMasterBinding) holder.binding).tvFocName.setText(mData.getFocItemName());
//                    ((RowItemPromotionMasterBinding) holder.binding).tvFocQty.setText("Qty: " + mData.getFocItemQTY() + " " + mData.getFocItemUOM());

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_promotion_master;
            }
        };

        binding.rvList.setAdapter(mPromotionAdapter);
    }

    private void setCateggoryAdapter() {

        mCategoryAdapter = new CommonAdapter<Category>(arrCategory) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowCategoryBinding) {
                    Category mData = arrCategory.get(position);
                    ((RowCategoryBinding) holder.binding).setItem(mData);
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_category;
            }
        };

        binding.rvList.setAdapter(mCategoryAdapter);
    }

    private void setNatureAdapter() {

        mNatureAdapter = new CommonAdapter<NatureMaster>(arrNatature) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowTicketItemBinding) {
                    NatureMaster mData = arrNatature.get(position);
                    ((RowTicketItemBinding) holder.binding).setItem(mData);
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_ticket_item;
            }
        };

        binding.rvList.setAdapter(mNatureAdapter);
    }


    private void setSubCateggoryAdapter() {

        mSubCategoryAdapter = new CommonAdapter<SubCategoryType>(arrSubCategory) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowSubCategoryBinding) {
                    SubCategoryType mData = arrSubCategory.get(position);
                    ((RowSubCategoryBinding) holder.binding).setItem(mData);
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_sub_category;
            }
        };

        binding.rvList.setAdapter(mSubCategoryAdapter);
    }

    private void setChannelAdapter() {

        mChannelAdapter = new CommonAdapter<ChannellType>(arrChannel) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowChannelBinding) {
                    ChannellType mData = arrChannel.get(position);
                    ((RowChannelBinding) holder.binding).setItem(mData);
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_channel;
            }
        };

        binding.rvList.setAdapter(mChannelAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:

                if (!isClick) {
                    isClick = true;
                    if (type.equalsIgnoreCase("Customer")) {
                        if (customer_count.equals("2")) {
                            getCustomer_deport();
                        } else if (customer_count.equals("0")) {
                            getCustomer();
                        } else {
                            getCustomer();
                        }
                        //getCustomer();
                    } else if (type.equalsIgnoreCase("Material")) {
                        getMaterial();
                    } else if (type.equalsIgnoreCase("AgentPrice")) {
                        getAgentPrice();
                    } else if (type.equalsIgnoreCase("Promotion")) {
                        getPromotions();
                    } else if (type.equalsIgnoreCase("Category")) {
                        getCategory();
                    } else if (type.equalsIgnoreCase("Channel")) {
                        getChannel();
                    } else if (type.equalsIgnoreCase("SubCategory")) {
                        getSubCategory();
                    } else if (type.equalsIgnoreCase("Ticket")) {
                        getNature();
                    }
                }
                break;
        }
    }

    private void getCustomer() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomer(App.CUSTOMER, routeId, UtilApp.salesmanType(Settings.getString(App.ROLE)));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(MasterDataActivity.this, "Customer Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Customer> arrData = new ArrayList<>();
                        arrData = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Customer>>() {
                                }.getType());

                        db.insertCustomer(arrData);
                        if (customer_count.equals("0")) {
                            //getCustomer();
                            arrData = new ArrayList<>();
                            arrData.clear();
                            arrData = db.getSeqCustomerList(UtilApp.getCurrentDay());
                            setMainAdapter();
                        } else {
                            //Set Customer Data
                            arrCustomer = new ArrayList<>();
                            arrCustomer = db.getCutomerList();
                            setCustomerAdapter();
                        }
                    }
                }

                progressDialog.hide();
                isClick = false;

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                isClick = false;
                progressDialog.hide();
                UtilApp.logData(MasterDataActivity.this, "Customer Failure: " + error.getMessage());
            }
        });
    }

    private void getMaterial() {

        new RetrieveItemData().execute();

//        progressDialog.show();
//
//        final Call<JsonObject> labelResponse = ApiClient.getService().getItem(App.ITEM);
//
//        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                Log.e("Item Response", response.toString());
//                if (response.body() != null) {
//                    UtilApp.logData(MasterDataActivity.this, "Item Response: " + response.body().toString());
//                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
//                        ArrayList<Item> arrData = new ArrayList<>();
//                        arrData = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
//                                new TypeToken<List<Item>>() {
//                                }.getType());
//
//                        db.insertItem(arrData);
//                    }
//                }
//
//                progressDialog.hide();
//
//                //Set Material Data
//                arrMaterial = new ArrayList<>();
//                arrMaterial = db.getAllItemList();
//                setMaterialAdapter();
//                isClick = false;
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable error) {
//                Log.e("Material Fail", error.getMessage());
//                isClick = false;
//                progressDialog.hide();
//                UtilApp.logData(MasterDataActivity.this, "Material Failure: " + error.getMessage());
//            }
//        });
    }

    private void getAgentPrice() {

        new RetrieveAgentPriceData().execute();

//        progressDialog.show();
//
//        final Call<JsonObject> labelResponse = ApiClient.getService().getPricingList(App.PRICING_ROUTE_LIST, routeId, Settings.getString(App.AGENTID));
//
//        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                Log.e("Price Response", response.toString());
//                if (response.body() != null) {
//                    UtilApp.logData(MasterDataActivity.this, "Price Response: " + response.body().toString());
//                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
//                        ArrayList<Pricing> arrPricing = new ArrayList<>();
//                        arrPricing = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
//                                new TypeToken<List<Pricing>>() {
//                                }.getType());
//                        db.deleteTable(db.TABLE_AGENT_PRICE_COUNT);
//                        db.insertAgentPricingCount(arrPricing, routeId);
//                        db.deleteTable(db.TABLE_AGENT_PRICING);
//                        db.insertAgentPricingItems(arrPricing, routeId);
//                    }
//                }
//
//                progressDialog.hide();
//
//                //Set Material Data
//                arrMaterial = new ArrayList<>();
//                arrMaterial = db.getAllItemList();
//                setMaterialAdapter();
//                isClick = false;
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable error) {
//                Log.e("Price Fail", error.getMessage());
//                isClick = false;
//                progressDialog.hide();
//                UtilApp.logData(MasterDataActivity.this, "Price Failure: " + error.getMessage());
//            }
//        });

    }

    private void getPromotions() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getRoutePromotion(App.FREE_GOODS_NEW, routeId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Good Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(MasterDataActivity.this, "Free Good Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<PromoItems> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<PromoItems>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_PROMO);
                        db.deleteTable(db.TABLE_PROMO_CUSTOMER_EXCLUDE);
                        db.deleteTable(db.TABLE_PROMO_SLAB);
                        db.deleteTable(db.TABLE_ORDER_ITEM);
                        db.deleteTable(db.TABLE_OFFER_ITEM);
                        db.insertNewFreeGoods(arrItems);
                    }
                }

                getCustomerPromotions();

//                arrPromotion = new ArrayList<>();
//                arrPromotion = db.getAllPromotion();
//                setPromotionAdapter();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Good Fail", error.getMessage());
                getCustomerPromotions();
                UtilApp.logData(MasterDataActivity.this, "Free Good Failure: " + error.getMessage());
            }
        });
    }

    private void getCustomerPromotions() {


        final Call<JsonObject> labelResponse = ApiClient.getService().getRoutePromotion(App.CUSTOMER_FREE_GOODS_NEW, routeId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Good Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(MasterDataActivity.this, "Free Good Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<PromoItems> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<PromoItems>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_CUSTOMER_PROMO);
                        db.deleteTable(db.TABLE_PROMO_CUSTOMER);
                        db.deleteTable(db.TABLE_CUSTOMER_PROMO_SLAB);
                        db.deleteTable(db.TABLE_CUSTOMER_ORDER_ITEM);
                        db.deleteTable(db.TABLE_CUSTOMER_OFFER_ITEM);
                        db.insertNewCustomerFreeGoods(arrItems);
                    }
                }

                progressDialog.hide();

                arrPromotion = new ArrayList<>();
                arrPromotion = db.getAllPromotion();

                ArrayList<PromoOfferData> arrCust = new ArrayList<>();
                arrCust = db.getAllCustomerPromotion();
                for (int i = 0; i < arrCust.size(); i++) {
                    arrPromotion.add(arrCust.get(i));
                }
                setPromotionAdapter();
                isClick = false;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Good Fail", error.getMessage());
                isClick = false;
                progressDialog.hide();
                UtilApp.logData(MasterDataActivity.this, "Free Good Failure: " + error.getMessage());
            }
        });
    }

    private void getCategory() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.CUSTOMER_CATEGORY);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Good Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(MasterDataActivity.this, "Free Good Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<CustomerType> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<CustomerType>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_CUSTOMER_CATEGORY);
                        db.insertCustomerCategory(arrItems);
                    }
                }

                progressDialog.hide();

                arrCategory = new ArrayList<>();
                arrCategory = db.getCustCategoryList();
                setCateggoryAdapter();
                isClick = false;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Good Fail", error.getMessage());
                isClick = false;
                progressDialog.hide();
                UtilApp.logData(MasterDataActivity.this, "Free Good Failure: " + error.getMessage());
            }
        });
    }

    private void getChannel() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.CUSTOMER_CHANNEL);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Good Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(MasterDataActivity.this, "Free Good Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<ChannellType> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<ChannellType>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_CUSTOMER_CHANNEL);
                        db.insertCustomerChannel(arrItems);
                    }
                }

                progressDialog.hide();

                arrChannel = new ArrayList<>();
                arrChannel = db.getCustChannelList();
                setChannelAdapter();
                isClick = false;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Good Fail", error.getMessage());
                isClick = false;
                progressDialog.hide();
                UtilApp.logData(MasterDataActivity.this, "Free Good Failure: " + error.getMessage());
            }
        });
    }

    private void getSubCategory() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.CUSTOMER_SUB_CATEGORY);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Good Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(MasterDataActivity.this, "Free Good Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<SubCategoryType> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<SubCategoryType>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_CUSTOMER_SUB_CATEGORY);
                        db.insertCustomerSubCategory(arrItems);
                    }
                }

                progressDialog.hide();

                arrSubCategory = new ArrayList<>();
                arrSubCategory = db.getCustSubCategoryList();
                setSubCateggoryAdapter();
                isClick = false;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Good Fail", error.getMessage());
                isClick = false;
                progressDialog.hide();
                UtilApp.logData(MasterDataActivity.this, "Free Good Failure: " + error.getMessage());
            }
        });
    }

    private void getNature() {

        progressDialog.show();

        String custId = Settings.getString(App.SALESMANID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getNatureOfCallList(App.NATURE_OF_CAL_MASTER, custId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Good Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(MasterDataActivity.this, "NatureMaster Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<NatureMaster> arrFridge = new ArrayList<>();
                        if (response.body().get("RESULT").getAsJsonArray() != null) {
                            arrFridge = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                    new TypeToken<List<NatureMaster>>() {
                                    }.getType());
                            db.insertNatureMaster(arrFridge);
                        }
                    }
                } else {
                    UtilApp.logData(MasterDataActivity.this, "NatureMaster Response: Blank");
                }

                progressDialog.hide();

                arrNatature = new ArrayList<>();
                arrNatature = db.getAllNatureData();
                setNatureAdapter();
                isClick = false;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Good Fail", error.getMessage());
                isClick = false;
                progressDialog.hide();
                UtilApp.logData(MasterDataActivity.this, "Free Good Failure: " + error.getMessage());
            }
        });
    }


    private void getCustomer_deport() {

        new RetrieveCustomerData().execute();

//        progressDialog.show();
//
//        dept = Settings.getString(App.DEPOTID);
//        db.deleteDepot("3");
//
//
//        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomer_depot(App.CUSTOMER_DEPORT, dept, UtilApp.salesmanType(Settings.getString(App.ROLE)));
//
//        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                Log.e("Customer Response", response.toString());
//                if (response.body() != null) {
//                    UtilApp.logData(MasterDataActivity.this, "Customer Response1: " + response.body().toString());
//                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
//                        ArrayList<Customer> arrCustomer = new ArrayList<>();
//                        arrCustomer = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
//                                new TypeToken<List<Customer>>() {
//                                }.getType());
//                        //db.deleteTable(DBManager.TABLE_CUSTOMER);
////2
//                        String mtotalCOunt = response.body().get("total_cust").getAsString();
//                        db.deleteTable(db.TABLE_DEPOT_CUSTOMER_COUNT);
//                        db.insertDepotCustomerCount(mtotalCOunt, dept);
//                        db.deleteTable(db.TABLE_DEPOT_CUSTOMER);
//                        db.insertDept(arrCustomer);
//                        progressDialog.hide();
//                    }
//                } else {
//                    UtilApp.logData(MasterDataActivity.this, "Customer Response: Blank");
//                    progressDialog.hide();
//                }
//
//                isClick = false;
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable error) {
//                Log.e("Customer Fail", error.getMessage());
//                UtilApp.logData(MasterDataActivity.this, "Customer Failure: " + error.getMessage());
//                progressDialog.hide();
//                isClick = false;
//            }
//        });

    }

    class RetrieveCustomerData extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        protected String doInBackground(String... urls) {
            try {

                File root = android.os.Environment.getExternalStorageDirectory();
                File dir = new File(root.getAbsolutePath() + "/Riham/master");
                File file = new File(dir, "customer.txt");

                if (file.exists()) {
                    String custData = readfile(file);
                    try {
                        JSONArray obj = new JSONArray(custData);
                        Log.d("Customer Data", obj.toString());

                        ArrayList<Customer> arrCustomer = new ArrayList<>();
                        arrCustomer = new Gson().fromJson(obj.toString(),
                                new TypeToken<List<Customer>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_DEPOT_CUSTOMER);
                        String mtotalCOunt = String.valueOf(arrCustomer.size());
                        db.deleteTable(db.TABLE_DEPOT_CUSTOMER_COUNT);
                        String dept = Settings.getString(App.DEPOTID);
                        db.insertDepotCustomerCount(mtotalCOunt, dept);
                        db.insertDept(arrCustomer);

                    } catch (Throwable t) {

                    }
                } else {
                    File mFIle = downloadFile(Settings.getString(App.CUSTOMERFILE), "customer.txt");
                    String custData = readfile(mFIle);
                    try {
                        JSONArray obj = new JSONArray(custData);
                        Log.d("Customer Data", obj.toString());

                        ArrayList<Customer> arrCustomer = new ArrayList<>();
                        arrCustomer = new Gson().fromJson(obj.toString(),
                                new TypeToken<List<Customer>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_DEPOT_CUSTOMER);
                        String mtotalCOunt = String.valueOf(arrCustomer.size());
                        db.deleteTable(db.TABLE_DEPOT_CUSTOMER_COUNT);
                        String dept = Settings.getString(App.DEPOTID);
                        db.insertDepotCustomerCount(mtotalCOunt, dept);
                        db.insertDept(arrCustomer);

                    } catch (Throwable t) {

                    }
                }

            } catch (Exception e) {
                this.exception = e;
                return null;
            } finally {
                return "";
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            progressDialog.hide();

            arrCustomer = new ArrayList<>();
            arrCustomer = db.getCutomerDepotList();
            arrData = new ArrayList<>();
            arrData = db.getCutomerDepotList();
            setMainAdapter();
            isClick = false;
        }
    }

    class RetrieveAgentPriceData extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        protected String doInBackground(String... urls) {
            try {

                File root = android.os.Environment.getExternalStorageDirectory();
                File dir = new File(root.getAbsolutePath() + "/Riham/master");
                File file = new File(dir, "pricing.txt");

                if (file.exists()) {
                    String custData = readfile(file);
                    try {
                        JSONArray obj = new JSONArray(custData);
                        Log.d("Price Data", obj.toString());

                        String routeId = Settings.getString(App.ROUTEID);

                        ArrayList<Pricing> arrPricing = new ArrayList<>();
                        arrPricing = new Gson().fromJson(obj.toString(),
                                new TypeToken<List<Pricing>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_AGENT_PRICE_COUNT);
                        db.insertAgentPricingCount(arrPricing, routeId);
                        db.deleteTable(db.TABLE_AGENT_PRICING);
                        db.insertAgentPricingItems(arrPricing, routeId);

                    } catch (Throwable t) {

                    }
                } else {
                    File mFIle = downloadFile(Settings.getString(App.PRICEFILE), "pricing.txt");
                    String custData = readfile(mFIle);
                    try {
                        JSONArray obj = new JSONArray(custData);
                        Log.d("Price Data", obj.toString());

                        String routeId = Settings.getString(App.ROUTEID);

                        ArrayList<Pricing> arrPricing = new ArrayList<>();
                        arrPricing = new Gson().fromJson(obj.toString(),
                                new TypeToken<List<Pricing>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_AGENT_PRICE_COUNT);
                        db.insertAgentPricingCount(arrPricing, routeId);
                        db.deleteTable(db.TABLE_AGENT_PRICING);
                        db.insertAgentPricingItems(arrPricing, routeId);

                    } catch (Throwable t) {

                    }
                }

            } catch (Exception e) {
                this.exception = e;
                return null;
            } finally {
                return "";
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            progressDialog.hide();

            arrMaterial = new ArrayList<>();
            arrMaterial = db.getAllItemList();
            setMaterialAdapter();
            isClick = false;
        }
    }

    class RetrieveItemData extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        protected String doInBackground(String... urls) {
            try {

                File root = android.os.Environment.getExternalStorageDirectory();
                File dir = new File(root.getAbsolutePath() + "/Riham/master");
                File file = new File(dir, "item.txt");

                if (file.exists()) {
                    String custData = readfile(file);
                    try {
                        JSONArray obj = new JSONArray(custData);
                        Log.d("Item Data", obj.toString());

                        ArrayList<Item> arrItem = new ArrayList<>();
                        arrItem = new Gson().fromJson(obj.toString(),
                                new TypeToken<List<Item>>() {
                                }.getType());
                        Log.e("Item Count:", String.valueOf(arrItem.size()));
                        db.deleteTable(DBManager.TABLE_ITEM);
                        db.insertItem(arrItem);

                    } catch (Throwable t) {

                    }
                } else {
                    File mFIle = downloadFile(Settings.getString(App.ITEMFILE), "item.txt");
                    String custData = readfile(mFIle);
                    try {
                        JSONArray obj = new JSONArray(custData);
                        Log.d("Item Data", obj.toString());

                        ArrayList<Item> arrItem = new ArrayList<>();
                        arrItem = new Gson().fromJson(obj.toString(),
                                new TypeToken<List<Item>>() {
                                }.getType());
                        Log.e("Item Count:", String.valueOf(arrItem.size()));
                        db.deleteTable(DBManager.TABLE_ITEM);
                        db.insertItem(arrItem);

                    } catch (Throwable t) {

                    }
                }

            } catch (Exception e) {
                this.exception = e;
                return null;
            } finally {
                return "";
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            progressDialog.hide();

            arrMaterial = new ArrayList<>();
            arrMaterial = db.getAllItemList();
            setMaterialAdapter();
            isClick = false;
        }
    }

    public String readfile(File file) {
        // File file = new File(Environment.getExternalStorageDirectory(), "mytextfile.txt");
        StringBuilder builder = new StringBuilder();
        Log.e("main", "read start");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            br.close();
        } catch (Exception e) {
            Log.e("main", " error is " + e.toString());
        }
        Log.e("main", " read text is " + builder.toString());
        return builder.toString();
    }

    private File downloadFile(String fileUrl, String fileName) {

        File mFIle = null;
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/Riham/master");
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            String mUrl = App.BASE_URL + "API/" + fileUrl;
            URL url = new URL(mUrl);
            File file = new File(dir, fileName);

            mFIle = file;

            long startTime = System.currentTimeMillis();
            Log.d("DownloadManager", "download url:" + url);
            Log.d("DownloadManager", "download file name:" + fileName);

            URLConnection uconn = url.openConnection();
//            uconn.setReadTimeout(TIMEOUT_CONNECTION);
//            uconn.setConnectTimeout(TIMEOUT_SOCKET);

            InputStream is = uconn.getInputStream();
            BufferedInputStream bufferinstream = new BufferedInputStream(is);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            //We create an array of bytes
            byte[] data = new byte[50];
            int current = 0;

            while ((current = bufferinstream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, current);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer.toByteArray());
            fos.flush();
            fos.close();
            Log.d("DownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + "sec");
            int dotindex = fileName.lastIndexOf('.');
            if (dotindex >= 0) {
                fileName = fileName.substring(0, dotindex);
            }

        } catch (IOException e) {
            Log.d("DownloadManager", "Error:" + e);
        }

        return mFIle;
    }
}
