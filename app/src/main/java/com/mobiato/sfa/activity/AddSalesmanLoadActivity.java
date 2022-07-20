package com.mobiato.sfa.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.Adapter.ItemAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityAddSalesmanLoadBinding;
import com.mobiato.sfa.databinding.ActivitySalesmanLoadBinding;
import com.mobiato.sfa.databinding.RowItemCategoryBinding;
import com.mobiato.sfa.databinding.RowItemSaleBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Category;
import com.mobiato.sfa.model.DepotData;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.RouteData;
import com.mobiato.sfa.model.RouteSalesmanData;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.DecimalUtils;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;

public class AddSalesmanLoadActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private ActivityAddSalesmanLoadBinding binding;
    private int countPlus = 0;
    public String[] arrType = {"Depot Sales Representative", "Hariss Sales Executive", "Projects Sales Executive"};
    public String strType = "", strRouteId = "", strSalesmanId = "", loadDate = "", strSalesman = "", strRoute = "";
    private LoadingSpinner progressDialog;
    private DBManager db;
    ArrayList<RouteData> arrRouteData = new ArrayList<>();
    ArrayList<RouteSalesmanData> arrSalesmanData = new ArrayList<>();
    public String[] arrRoute, arrSalesman;
    private JSONArray jsonArray;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    int catPosition = 0, itemPosition = 0, selectCatId = 0;
    public ArrayList<Item> arrItem = new ArrayList<>();
    public ArrayList<Item> arrCSDItem = new ArrayList<>();
    public ArrayList<Item> arrJuiceItem = new ArrayList<>();
    public ArrayList<Item> arrWaterItem = new ArrayList<>();
    public ArrayList<Item> arrBisItem = new ArrayList<>();
    public ArrayList<Item> arrHamperItem = new ArrayList<>();
    public ArrayList<Item> arrConfectionaryItem = new ArrayList<>();
    public ArrayList<Category> arrCategory = new ArrayList<>();
    public ArrayList<Item> arrSales = new ArrayList<>();
    private CommonAdapter<Category> mCategoryAdapter;
    private ItemAdapter mItemAdapter;
    private CommonAdapter<Item> mSelectAdapter;
    int select = 1;
    private boolean isClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSalesmanLoadBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setTitle("Add Salesman Load");

        db = new DBManager(AddSalesmanLoadActivity.this);
        progressDialog = new LoadingSpinner(AddSalesmanLoadActivity.this);

        registerReceiver(broadcastReceiver, new IntentFilter(InputDialogOrderActivity.BROADCAST_ACTION));

        binding.stepView.setStepsNumber(3);
        binding.stepView.go(countPlus, true);

        Date c = Calendar.getInstance().getTime();
        //  System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        loadDate = formattedDate;

        getRouteList();

        setCategory();
        setCategoryAdapter();

        selectCatId = 2;
        arrSales = new ArrayList<>();

        setItem();
        setCategoryItem(arrCSDItem, "CSD");

        binding.lytDate.setOnClickListener(this);
        binding.btnPrevious.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);
        binding.lytDepot.setOnClickListener(this);
        binding.lytSalesman.setOnClickListener(this);
        binding.lytRoute.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrevious:
                countPlus--;
                updateData(countPlus);
                break;
            case R.id.btnAdd:

                if (countPlus == 0) {
                    if (strType.isEmpty()) {
                        UtilApp.displayAlert(AddSalesmanLoadActivity.this, "Select type");
                    } else if (strRouteId.isEmpty()) {
                        UtilApp.displayAlert(AddSalesmanLoadActivity.this, "Select Route");
                    } else if (strSalesmanId.isEmpty()) {
                        UtilApp.displayAlert(AddSalesmanLoadActivity.this, "Select Salesman");
                    } else {
                        countPlus++;
                        updateData(countPlus);
                    }
                } else if (countPlus == 1) {

                    if (arrSales.size() > 0) {
                        countPlus++;
                        updateData(countPlus);
                    } else {
                        UtilApp.displayAlert(AddSalesmanLoadActivity.this, "Please select items");
                    }
                } else if (countPlus == 2) {

                    if (!isClick) {
                        isClick = true;

                        new getAsyncTask().execute();
                    }
                }
                break;
            case R.id.lytDate:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddSalesmanLoadActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.lytDepot:
                AlertDialog.Builder builder = new AlertDialog.Builder(AddSalesmanLoadActivity.this);
                builder.setTitle("Select Type");
                builder.setItems(arrType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etDepot.setText(arrType[which]);
                        if (which == 0) {
                            strType = "3";
                        } else if (which == 1) {
                            strType = "2";
                        } else {
                            strType = "6";
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.lytRoute:
                if (strType.isEmpty()) {
                    UtilApp.displayAlert(AddSalesmanLoadActivity.this, "Select type first");
                } else {
                    AlertDialog.Builder builderR = new AlertDialog.Builder(AddSalesmanLoadActivity.this);
                    builderR.setTitle("Select Route");
                    builderR.setItems(arrRoute, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            binding.etRoute.setText(arrRoute[which]);
                            strRouteId = arrRouteData.get(which).getRoute_id();
                            strRoute = arrRouteData.get(which).getRoutename();
                            getRouteSalesmanList();
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialogR = builderR.create();
                    dialogR.show();
                }

                break;

            case R.id.lytSalesman:
                if (strRouteId.isEmpty()) {
                    UtilApp.displayAlert(AddSalesmanLoadActivity.this, "Select Route first");
                } else {
                    AlertDialog.Builder builderS = new AlertDialog.Builder(AddSalesmanLoadActivity.this);
                    builderS.setTitle("Select Salesman");
                    builderS.setItems(arrSalesman, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            binding.etSalesman.setText(arrSalesman[which]);
                            strSalesmanId = arrSalesmanData.get(which).getSalesman_id();
                            strSalesman = arrSalesmanData.get(which).getSalesmanname();
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialogS = builderS.create();
                    dialogS.show();

                }
                break;
            default:
                break;
        }
    }

    private void getRouteList() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getDepotRoute(App.GET_DEPOT_ROUTE, Settings.getString(App.DEPOTID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Route List Response", response.toString());

                if (response.body() != null) {
                    UtilApp.logData(AddSalesmanLoadActivity.this, "Route List Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        arrRouteData = new ArrayList<>();
                        arrRouteData = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<RouteData>>() {
                                }.getType());

                    }
                } else {
                    UtilApp.logData(AddSalesmanLoadActivity.this, "Route List Response: Blank");
                }

                progressDialog.hide();

                getRouteData();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Agent List Fail", error.getMessage());
                progressDialog.hide();
                UtilApp.logData(AddSalesmanLoadActivity.this, "Route List Fail: " + error.getMessage());
                getRouteData();
            }
        });
    }

    private void getRouteSalesmanList() {

        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().getRouteSalesmanList(App.GET_DEPOT_SALESMAN, strRouteId, strType);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Salesman List Response", response.toString());

                if (response.body() != null) {
                    UtilApp.logData(AddSalesmanLoadActivity.this, "Salesman List Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        arrSalesmanData = new ArrayList<>();
                        arrSalesmanData = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<RouteSalesmanData>>() {
                                }.getType());

                    }
                } else {
                    UtilApp.logData(AddSalesmanLoadActivity.this, "Salesman List Response: Blank");
                }

                progressDialog.hide();

                getSalesmanData();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Salesman List Fail", error.getMessage());
                progressDialog.hide();
                UtilApp.logData(AddSalesmanLoadActivity.this, "Salesman List Fail: " + error.getMessage());
                getSalesmanData();
            }
        });
    }

    public void getRouteData() {
        if (arrRouteData.size() > 0) {
            arrRoute = new String[arrRouteData.size()];
            for (int i = 0; i < arrRouteData.size(); i++) {
                arrRoute[i] = arrRouteData.get(i).getRoutename();
            }
        }
    }

    public void getSalesmanData() {
        if (arrSalesmanData.size() > 0) {
            arrSalesman = new String[arrSalesmanData.size()];
            for (int i = 0; i < arrSalesmanData.size(); i++) {
                arrSalesman[i] = arrSalesmanData.get(i).getSalesmanname();
            }
        }
    }

    private void updateData(int countPlus) {

        binding.stepView.go(countPlus, true);

        if (countPlus == 1) {
            binding.btnPrevious.setVisibility(View.VISIBLE);
            binding.tvTitle.setText("Select Item for Load");
            binding.lytSelectionView.setVisibility(View.GONE);
            binding.lytItemView.setVisibility(View.VISIBLE);
            binding.lytReView.setVisibility(View.GONE);
            binding.btnAdd.setImageResource(R.drawable.ic_arrow_next);
        } else if (countPlus == 2) {
            binding.btnPrevious.setVisibility(View.VISIBLE);
            binding.tvTitle.setText("Review Load");
            binding.lytSelectionView.setVisibility(View.GONE);
            binding.lytItemView.setVisibility(View.GONE);
            binding.lytReView.setVisibility(View.VISIBLE);
            binding.btnAdd.setImageResource(R.drawable.ic_check_white);
            setSelectedItemAdapter();
        } else if (countPlus == 0) {
            binding.btnPrevious.setVisibility(View.GONE);
            binding.lytSelectionView.setVisibility(View.VISIBLE);
            binding.lytItemView.setVisibility(View.GONE);
            binding.lytReView.setVisibility(View.GONE);
            binding.btnAdd.setImageResource(R.drawable.ic_arrow_next);
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
                            selectCatId = Integer.parseInt(arrCategory.get(position).catId);

                            switch (arrCategory.get(position).catId) {
                                case "1":
                                    setCategoryItem(arrBisItem, "Biscuit");
                                    break;
                                case "2":
                                    setCategoryItem(arrCSDItem, "CSD");
                                    break;
                                case "3":
                                    setCategoryItem(arrWaterItem, "Water");
                                    break;
                                case "4":
                                    setCategoryItem(arrJuiceItem, "Juice");
                                    break;
                                case "5":
                                    setCategoryItem(arrHamperItem, "Hamper");
                                    break;
                                case "6":
                                    setCategoryItem(arrConfectionaryItem, "Confectionary");
                                    break;
                                default:
                                    break;
                            }

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

    private void setCategoryItem(ArrayList<Item> arrData, String catName) {
        arrItem = new ArrayList<>();
        arrItem = arrData;

        if (arrItem.size() > 0) {
            binding.lytNoData.setVisibility(View.GONE);
            binding.rvList.setVisibility(View.VISIBLE);
            setAdapter();
        } else {
            binding.lytNoData.setVisibility(View.VISIBLE);
            binding.rvList.setVisibility(View.GONE);
            binding.tvNoText.setText(catName + " items not available!");
        }
    }

    private void setAdapter() {

        mItemAdapter = new ItemAdapter(AddSalesmanLoadActivity.this, arrItem, "SalesmanLoad", "", "", new ItemAdapter.ItemsAdapterListener() {
            @Override
            public void onItemSelected(Item contact, int position) {

                itemPosition = getItemIndex(contact.getItemId());
                //riddhi
                System.out.println("RIDDHI");

                Intent i = new Intent(AddSalesmanLoadActivity.this, InputDialogOrderActivity.class);
                i.putExtra("type", "Load");
                i.putExtra("apptype", "Salesman");
                i.putExtra("returnType", "");
                i.putExtra("agentexciseAmt", "0");
                i.putExtra("customer", Settings.getString(App.AGENTID));
                i.putExtra("item", contact);
                startActivity(i);

            }
        });

        binding.rvList.setAdapter(mItemAdapter);

    }

    private int getItemIndex(String itemId) {
        int itemPosition = 0;

        switch (Integer.parseInt(arrCategory.get(catPosition).catId)) {
            case 2:
                for (int i = 0; i < arrCSDItem.size(); i++) {
                    if (arrCSDItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        select = 2;
                        return itemPosition;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < arrWaterItem.size(); i++) {
                    if (arrWaterItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        select = 3;
                        return itemPosition;
                    }
                }
                break;
            case 4:
                for (int i = 0; i < arrJuiceItem.size(); i++) {
                    if (arrJuiceItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        select = 4;
                        return itemPosition;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < arrBisItem.size(); i++) {
                    if (arrBisItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        itemPosition = i;
                        select = 1;
                        return itemPosition;
                    }
                }
                break;
            case 5:
                for (int i = 0; i < arrHamperItem.size(); i++) {
                    if (arrHamperItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        select = 5;
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
            case 6:
                for (int i = 0; i < arrConfectionaryItem.size(); i++) {
                    if (arrConfectionaryItem.get(i).getItemId().equalsIgnoreCase(itemId)) {
                        select = 6;
                        itemPosition = i;
                        return itemPosition;
                    }
                }
                break;
        }
        return itemPosition;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            Item item = (Item) intent.getSerializableExtra("item");

            if (arrSales.size() == 0) {
                if (Integer.parseInt(item.getSaleBaseQty()) > 0 || Integer.parseInt(item.getSaleAltQty()) > 0)
                    arrSales.add(item);
            } else {

                boolean flag = false;
                for (int i = 0; i < arrSales.size(); i++) {
                    //System.out.println("check-->"+arrSales.get(i).getItemId()+" Equals--> "+item.getItemId());
                    if (arrSales.get(i).getItemId().equalsIgnoreCase(item.getItemId())) {
                        flag = true;
                        if (Integer.parseInt(item.getSaleBaseQty()) > 0 || Integer.parseInt(item.getSaleAltQty()) > 0) {
                            arrSales.set(i, item);
                        } else {
                            arrSales.remove(i);
                        }
                        break;
                    }
                }

                if (!flag) {
                    if (arrSales.size() > 69) {
                        UtilApp.displayAlert(AddSalesmanLoadActivity.this, "You can order 70 items at the time!");
                        return;
                    } else {
                        arrSales.add(item);
                    }
                }
            }

            updateItem(item);

        }

    };

    private void updateItem(Item mItem) {
        switch (Integer.parseInt(arrCategory.get(catPosition).catId)) {
            case 2:
                arrCSDItem.set(itemPosition, mItem);
                setCategoryItem(arrCSDItem, "CSD");
                break;
            case 4:
                arrJuiceItem.set(itemPosition, mItem);
                setCategoryItem(arrJuiceItem, "Juice");
                break;
            case 3:
                arrWaterItem.set(itemPosition, mItem);
                setCategoryItem(arrWaterItem, "Water");
                break;
            case 1:
                arrBisItem.set(itemPosition, mItem);
                setCategoryItem(arrBisItem, "Biscuit");
                break;
            case 5:
                arrHamperItem.set(itemPosition, mItem);
                setCategoryItem(arrHamperItem, "Hamper");
                break;
            case 6:
                arrConfectionaryItem.set(itemPosition, mItem);
                setCategoryItem(arrConfectionaryItem, "Confectionary");
                break;
        }
    }

    private void setItem() {

        arrCSDItem = new ArrayList<>();
        arrJuiceItem = new ArrayList<>();
        arrWaterItem = new ArrayList<>();
        arrBisItem = new ArrayList<>();
        arrHamperItem = new ArrayList<>();
        arrConfectionaryItem = new ArrayList<>();

        arrCSDItem = db.getItemListByCategory("2");
        arrJuiceItem = db.getItemListByCategory("4");
        arrWaterItem = db.getItemListByCategory("3");
        arrBisItem = db.getItemListByCategory("1");
        arrHamperItem = db.getItemListByCategory("5");
        arrConfectionaryItem = db.getItemListByCategory("6");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        binding.etDate.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
        loadDate = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
    }

    private void setSelectedItemAdapter() {
        ArrayList<Item> arrItem = new ArrayList<>();
        arrItem.addAll(arrSales);

        mSelectAdapter = new CommonAdapter<Item>(arrItem) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemSaleBinding) {
                    Item mItem = arrItem.get(position);
                    ((RowItemSaleBinding) holder.binding).setItem(mItem);

                    ((RowItemSaleBinding) holder.binding).imageView2.setLetter(arrItem.get(position).getItemName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowItemSaleBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);
                    if (mItem.getAltrUOM() != null) {
                        ((RowItemSaleBinding) holder.binding).tvAlterUOM.setText(mItem.getAlterUOMName() + ": " + mItem.getSaleAltQty());
                        if (mItem.getBaseUOM() != null) {
                            ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setVisibility(View.VISIBLE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getSaleBaseQty());
                        } else {
                            ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                            ((RowItemSaleBinding) holder.binding).tvBaseUOM.setVisibility(View.GONE);
                        }
                    } else {
                        ((RowItemSaleBinding) holder.binding).dividerUOM.setVisibility(View.GONE);
                        ((RowItemSaleBinding) holder.binding).tvAlterUOM.setVisibility(View.GONE);
                        ((RowItemSaleBinding) holder.binding).tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getSaleBaseQty());
                    }


//                    if (mItem.getSaleAltPrice() != null) {
//                        ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
//                        ((RowItemSaleBinding) holder.binding).tvAlterPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleAltPrice())) + " UGX");
//                        if (mItem.getSaleBasePrice() != null) {
//                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.VISIBLE);
//                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.VISIBLE);
//                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText(UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleBasePrice())) + " UGX");
//                        } else {
//                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
//                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setVisibility(View.GONE);
//                        }
//                    } else {
//                        if (mItem.getSaleBasePrice() != null) {
//                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.VISIBLE);
//                            ((RowItemSaleBinding) holder.binding).dividerPrice.setVisibility(View.GONE);
//                            ((RowItemSaleBinding) holder.binding).tvAlterPrice.setVisibility(View.GONE);
//                            ((RowItemSaleBinding) holder.binding).tvBaseUOMPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleBasePrice())) + " UGX");
//                        } else {
//                            ((RowItemSaleBinding) holder.binding).lytPrice.setVisibility(View.GONE);
//                        }
//
//                    }

                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_sale;
            }
        };

        binding.rvListView.setAdapter(mSelectAdapter);
    }

    /*****************************************************************************************
     @ Creating data for print and saving it in DB for future printing
     ****************************************************************************************/
    public JSONArray createLoadPrintData(String orderNo) {
        JSONArray jArr = new JSONArray();

        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.SALESMAN_LOAD);

            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", strRouteId);
            mainArr.put("DOC DATE", UtilApp.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", UtilApp.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.SALESMANNAME));
            mainArr.put("Load SALESMAN", strSalesmanId);
            mainArr.put("ROUTENAME", strRoute);
            mainArr.put("SALESMAN_NAME", strSalesman);
            mainArr.put("invoicenumber", orderNo);  //Invoice No
            mainArr.put("deliveryDate", loadDate);  //Invoice No

            mainArr.put("CUSTOMER", "");
            mainArr.put("CUSTOMERID", "");
            mainArr.put("customertype", "");
            mainArr.put("ADDRESS", "");
            mainArr.put("TRN", "");

            JSONArray TOTAL = new JSONArray();
            //SALES INVOICE
            JSONArray jData = new JSONArray();
            for (Item obj : arrSales) {

                if (Integer.parseInt(obj.getSaleBaseQty()) > 0 && Integer.parseInt(obj.getSaleAltQty()) > 0) {

                    //Add Base UOM
                    JSONArray data = new JSONArray();
                    data.put(obj.getItemCode()); // ITEM CODE
                    data.put(obj.getItemName());// DESC
                    data.put(obj.getBaseUOMName());// UOM
                    data.put(obj.getSaleBaseQty());//  QTY
                    double unitPrice = Double.parseDouble(obj.getUOMPrice()) - Double.parseDouble(obj.getDiscountAmt());
                    data.put("" + UtilApp.getNumberFormate(unitPrice));// Unit Price
                    data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price
                    jData.put(data);

                    //Add Alter UOM
                    JSONArray dataAl = new JSONArray();
                    dataAl.put(obj.getItemCode()); // ITEM CODE
                    dataAl.put(obj.getItemName());// DESC
                    dataAl.put(obj.getAlterUOMName());// UOM
                    dataAl.put(obj.getSaleAltQty());//  QTY
                    double unitAlPrice = Double.parseDouble(obj.getAlterUOMPrice()) - Double.parseDouble(obj.getDiscountAlAmt());
                    dataAl.put("" + UtilApp.getNumberFormate(unitAlPrice));// Unit Price
                    dataAl.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price
                    jData.put(dataAl);


                } else {
                    if (Integer.parseInt(obj.getSaleBaseQty()) > 0) {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getBaseUOMName());// UOM
                        data.put(obj.getSaleBaseQty());//  QTY
                        double unitPrice = Double.parseDouble(obj.getUOMPrice()) - Double.parseDouble(obj.getDiscountAmt());
                        data.put("" + UtilApp.getNumberFormate(unitPrice));// Unit Price
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleBasePrice())));// Total Price

                        jData.put(data);
                    } else {
                        JSONArray data = new JSONArray();

                        data.put(obj.getItemCode()); // ITEM CODE
                        data.put(obj.getItemName());// DESC
                        data.put(obj.getAlterUOMName());// UOM
                        data.put(obj.getSaleAltQty());//  QTY
                        double unitPrice = Double.parseDouble(obj.getAlterUOMPrice()) - Double.parseDouble(obj.getDiscountAlAmt());
                        data.put("" + UtilApp.getNumberFormate(unitPrice));// Unit Price
                        data.put(UtilApp.getNumberFormate(Double.parseDouble(obj.getSaleAltPrice())));// Total Price

                        jData.put(data);
                    }
                }
            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("VAT", "");
            totalObj.put("Total Amount", "");
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

    private class getAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private LoadingSpinner mDialog;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (mDialog.isShowing())
                mDialog.hide();

            UtilApp.dialogPrint(AddSalesmanLoadActivity.this, new OnSearchableDialog() {
                @Override
                public void onItemSelected(Object o) {
                    if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                        UtilApp.createBackgroundJob(getApplicationContext());
                    }
                    String selection = (String) o;
                    if (selection.equalsIgnoreCase("yes")) {
                        PrintLog object = new PrintLog(AddSalesmanLoadActivity.this,
                                AddSalesmanLoadActivity.this);
                        object.execute("", jsonArray);
                    } else {
                        finish();
                    }
                }
            });


        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            String orderNo = UtilApp.getSalesmanLoadNo();

            db.insertSalesmanLoad(orderNo, loadDate, strRouteId, strSalesmanId, arrSales, strSalesman, strRoute);

            //get Print Data
            JSONObject data = null;
            try {
                jsonArray = createLoadPrintData(orderNo);
                data = new JSONObject();
                data.put("data", (JSONArray) jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //INSERT TRANSACTION
            Transaction transaction = new Transaction();

            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_SALESMAN_LOAD_CREATED;
            transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
            transaction.tr_customer_num = "";
            transaction.tr_customer_name = "";
            transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
            transaction.tr_invoice_id = "";
            transaction.tr_order_id = orderNo;
            transaction.tr_collection_id = "";
            transaction.tr_pyament_id = "";
            transaction.tr_printData = data.toString();
            transaction.tr_is_posted = "No";

            db.insertTransaction(transaction);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new LoadingSpinner(AddSalesmanLoadActivity.this);
            mDialog.show();
        }
    }

    public void callback() {
        finish();
    }
}