package com.mobiato.sfa.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.Fragments.DashboardFragment;
import com.mobiato.sfa.R;
import com.mobiato.sfa.Techinician.fragment.TechnicianDashboardFragment;
import com.mobiato.sfa.Techinician.fragment.TechnicianFragmentThree;
import com.mobiato.sfa.databinding.ActivityDownloadingDataBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.merchandising.MerchantDashboardActivity;
import com.mobiato.sfa.model.ASSETS_MODEL;
import com.mobiato.sfa.model.ChannellType;
import com.mobiato.sfa.model.ChillerTechnician;
import com.mobiato.sfa.model.CollectionData;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerTechnician;
import com.mobiato.sfa.model.CustomerType;
import com.mobiato.sfa.model.Delivery;
import com.mobiato.sfa.model.DepotData;
import com.mobiato.sfa.model.Discount;
import com.mobiato.sfa.model.DiscountData;
import com.mobiato.sfa.model.Distribution;
import com.mobiato.sfa.model.FOCItems;
import com.mobiato.sfa.model.FridgeMaster;
import com.mobiato.sfa.model.Inventory;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Load;
import com.mobiato.sfa.model.NatureMaster;
import com.mobiato.sfa.model.NotificationTechnician;
import com.mobiato.sfa.model.Planogram;
import com.mobiato.sfa.model.Pricing;
import com.mobiato.sfa.model.PromoItems;
import com.mobiato.sfa.model.Promotion_Item;
import com.mobiato.sfa.model.Return;
import com.mobiato.sfa.model.SubCategoryType;
import com.mobiato.sfa.model.Survey_Tools;
import com.mobiato.sfa.model.UOM;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class DownloadingDataActivity extends BaseActivity {

    private ActivityDownloadingDataBinding binding;
    private DBManager db;
    private String routeId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDownloadingDataBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        setFullScreen();

        routeId = Settings.getString(App.ROUTEID);
        db = new DBManager(this);

        UtilApp.logData(DownloadingDataActivity.this, "Downloading user data");
        downloadData();
    }

    public void downloadData() {

        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
            getCustomer_technician();
        } else {
            if (Integer.parseInt(db.getMaterialCount()) > 0) {
                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Merchandising")) {
                    if (Integer.parseInt(db.getCustomerCount()) > 0) {
                        if (Integer.parseInt(db.getUOMCount()) > 0) {
                            if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                                if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                                    if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                        getLoadList();
                                    } else {
                                        getPricingList();
                                    }
                                } else {
                                    getRoutePrice();
                                }
                            } else {
                                getRoutePrice();
                            }
                        } else {
                            getUOM();
                        }
                    } else {
                        getCustomer();
                    }
                } else {
                    //if (Integer.parseInt(db.getDepotCustomerCount()) > 0) {
                    if (Integer.parseInt(db.getUOMCount()) > 0) {
                        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                            if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                                if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                    getLoadList();
                                } else {
                                    getPricingList();
                                }
                            } else {
                                getRoutePrice();
                            }
                        } else {
                            getRoutePrice();
                        }
                    } else {
                        getUOM();
                    }
//                    } else {
//                        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
//                            getCustomer_technician();
//                        } else {
//                            getCustomer_Depot();
//                        }
//                    }
                }

            } else {
                getItem();
            }
        }
    }

    private void go() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
                    App.counttech_PM_service = 1;
                    App.counttech_BD_service = 1;
                    App.counttech_SA_service = 1;
                    App.counttech_CA_service = 1;
                    intent = new Intent(DownloadingDataActivity.this, TechnicianDashboardFragment.class);
                } else if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                    intent = new Intent(DownloadingDataActivity.this, DashboardFragment.class);
                } else {
                    intent = new Intent(DownloadingDataActivity.this, MerchantDashboardActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    private void getCustomer() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomer(App.CUSTOMER, routeId, UtilApp.salesmanType(Settings.getString(App.ROLE)));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Customer Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Customer> arrCustomer = new ArrayList<>();
                        arrCustomer = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Customer>>() {
                                }.getType());
                        //db.deleteTable(DBManager.TABLE_CUSTOMER);
                        db.insertCustomer(arrCustomer);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Customer Response: Blank");
                }

               /* if (Integer.parseInt(db.getUOMCount()) > 0) {
                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                        if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                            if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                getLoadList();
                            } else {
                                getPricingList();
                            }
                        } else {
                            getRoutePrice();
                        }
                    } else {
                        getRoutePrice();
                    }
                } else {
                    getUOM();
                }*/
                getCustomer_Depot();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Failure: " + error.getMessage());
                /*if (Integer.parseInt(db.getUOMCount()) > 0) {
                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                        if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                            if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                getLoadList();
                            } else {
                                getPricingList();
                            }
                        } else {
                            getRoutePrice();
                        }
                    } else {
                        getRoutePrice();
                    }
                } else {
                    getUOM();
                }*/
                getCustomer_Depot();
            }
        });
    }

    private void getCustomer_Depot() {
        String dept = Settings.getString(App.DEPOTID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomer_depot(App.CUSTOMER_DEPORT, dept, UtilApp.salesmanType(Settings.getString(App.ROLE)));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Customer Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Customer> arrCustomer = new ArrayList<>();
                        arrCustomer = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Customer>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_DEPOT_CUSTOMER);
                        String mtotalCOunt = response.body().get("total_cust").getAsString();
                        db.deleteTable(db.TABLE_DEPOT_CUSTOMER_COUNT);
                        db.insertDepotCustomerCount(mtotalCOunt, dept);
                        db.insertDept(arrCustomer);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Customer Response: Blank");
                }

                if (Integer.parseInt(db.getUOMCount()) > 0) {
                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")
                            || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                            UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                            UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                            UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                            UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {
                        if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                            if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                getLoadList();
                            } else {
                                getPricingList();
                            }
                        } else {
                            getRoutePrice();
                        }
                    } else {
                        getRoutePrice();
                    }
                } else {
                    getUOM();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Failure: " + error.getMessage());
                if (Integer.parseInt(db.getUOMCount()) > 0) {
                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")
                            || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                            UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                            UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                            UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                            UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {
                        if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                            if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                getLoadList();
                            } else {
                                getPricingList();
                            }
                        } else {
                            getRoutePrice();
                        }
                    } else {
                        getRoutePrice();
                    }
                } else {
                    getUOM();
                }
            }
        });
    }

    private void getCustomer_technician() {
        String custId = Settings.getString(App.SALESMANID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomerTechnician(App.CUSTOMER_TECHNICIAN, custId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Response technician", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Customer Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<CustomerTechnician> arrCustomer = new ArrayList<>();
                        arrCustomer = new Gson().fromJson(response.body().get("Result").getAsJsonArray().toString(),
                                new TypeToken<List<CustomerTechnician>>() {
                                }.getType());
                        //db.deleteTable(DBManager.TABLE_CUSTOMER);
                        db.insertCustomerTachnician(arrCustomer);
                        db.insertChillerCustomerTachnician(arrCustomer);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Customer Response: Blank");
                }

               /* if (Integer.parseInt(db.getUOMCount()) > 0) {
                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                        if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                            if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                getLoadList();
                            } else {
                                getPricingList();
                            }
                        } else {
                            getRoutePrice();
                        }
                    } else {
                        getRoutePrice();
                    }
                } else {
                    getUOM();
                }*/
                getChiller_technician();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Failure: " + error.getMessage());
               /* if (Integer.parseInt(db.getUOMCount()) > 0) {
                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                        if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                            if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                getLoadList();
                            } else {
                                getPricingList();
                            }
                        } else {
                            getRoutePrice();
                        }
                    } else {
                        getRoutePrice();
                    }
                } else {
                    getUOM();
                }*/
                getChiller_technician();
            }
        });
    }

    private void getChiller_technician() {
        String custId = Settings.getString(App.SALESMANID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getChillerTechnician(App.CHILLER_APPROVAL_TECHNICIAN, custId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Chiller Response technician", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Chiller Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<ChillerTechnician> arrCustomer_tech = new ArrayList<>();
                        arrCustomer_tech = new Gson().fromJson(response.body().get("Result").getAsJsonArray().toString(),
                                new TypeToken<List<ChillerTechnician>>() {
                                }.getType());
                        //db.deleteTable(DBManager.TABLE_CUSTOMER);
                        db.insertChillerTachnician(arrCustomer_tech);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Customer Response: Blank");
                }

               /* if (Integer.parseInt(db.getUOMCount()) > 0) {
                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                        if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                            if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                getLoadList();
                            } else {
                                getPricingList();
                            }
                        } else {
                            getRoutePrice();
                        }
                    } else {
                        getRoutePrice();
                    }
                } else {
                    getUOM();
                }*/
                //go();
                getNotification_technician();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Failure: " + error.getMessage());
                //go();
                getNotification_technician();
            }
        });
    }

    private void getNotification_technician() {
        String custId = Settings.getString(App.SALESMANID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getNotificationTechnician(App.CHILLER_NOTIFICATION_TECHNICIAN, custId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Notification Response technician", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Notification Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<NotificationTechnician> arrCustomer = new ArrayList<>();
                        arrCustomer = new Gson().fromJson(response.body().get("Result").getAsJsonArray().toString(),
                                new TypeToken<List<NotificationTechnician>>() {
                                }.getType());
                        // count = arrCustomer.size();
                        App.countNoti = arrCustomer.size();
                        Settings.setString(App.countNoti_tech, String.valueOf(arrCustomer.size()));
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        // write all the data entered by the user in SharedPreference and apply
                        myEdit.putString("Notificationcount", String.valueOf(arrCustomer.size()));
                        myEdit.apply();

                        // System.out.println("No-->"+arrNotification.get(0).getSchudule_date());
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Customer Response: Blank");
                }
                getTechinq_FridegMaster();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Notification Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Failure: " + error.getMessage());
                getTechinq_FridegMaster();
            }
        });
    }

    private void getTechinq_FridegMaster() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getItem(App.FRIDGE_MASTER);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("FridgeMasetr Response technician", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "FridgeMaster Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<FridgeMaster> arrFridge = new ArrayList<>();
                        arrFridge = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<FridgeMaster>>() {
                                }.getType());
                        db.insertFridgeMaster(arrFridge);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "FridgeMAster Response: Blank");
                }
                getTechinq_NatureMaster();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Fridge Master Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "FridgeMAster Failure: " + error.getMessage());
                getTechinq_NatureMaster();
            }
        });
    }

    private void getTechinq_NatureMaster() {
        String custId = Settings.getString(App.SALESMANID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getNatureOfCallList(App.NATURE_OF_CAL_MASTER, custId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("NatureMaster Response technician", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "NatureMaster Response: " + response.body().toString());
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
                    UtilApp.logData(DownloadingDataActivity.this, "NatureMaster Response: Blank");
                }
                go();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("NatureMaster Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "NatureMaster Failure: " + error.getMessage());
                go();
            }
        });
    }

    private void getItem() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getItem(App.ITEM);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Item Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Item Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Item> arrItem = new ArrayList<>();
                        arrItem = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Item>>() {
                                }.getType());
                        Log.e("Item Count:", String.valueOf(arrItem.size()));
                        // db.deleteTable(DBManager.TABLE_ITEM);
                        db.insertItem(arrItem);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Item Response: Blank");
                }

                //getUOM();
                if (Integer.parseInt(db.getDepotCustomerCount()) > 0) {
                    if (Integer.parseInt(db.getUOMCount()) > 0) {
                        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")
                                || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {
                            if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                                if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                    getLoadList();
                                } else {
                                    getPricingList();
                                }
                            } else {
                                getRoutePrice();
                            }
                        } else {
                            getRoutePrice();
                        }
                    } else {
                        getUOM();
                    }
                } else {
                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
                        getCustomer_technician();

                    } else {
                        getCustomer_Depot();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Item Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Item Fail: " + error.getMessage());
                if (Integer.parseInt(db.getDepotCustomerCount()) > 0) {
                    if (Integer.parseInt(db.getUOMCount()) > 0) {
                        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")
                                || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {
                            if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                                if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                                    getLoadList();
                                } else {
                                    getPricingList();
                                }
                            } else {
                                getRoutePrice();
                            }
                        } else {
                            getRoutePrice();
                        }
                    } else {
                        getUOM();
                    }
                } else {
                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
                        getCustomer_technician();

                    } else {
                        getCustomer_Depot();
                    }
                }
            }
        });
    }

    private void getUOM() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.UOM);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("UOM Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "UOM Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<UOM> arrItem = new ArrayList<>();
                        arrItem = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<UOM>>() {
                                }.getType());
                        db.insertUOM(arrItem);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "UOM Response: Blank");
                }

                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")
                        || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {
                    if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                        if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                            getLoadList();
                        } else {
                            getPricingList();
                        }
                    } else {
                        getRoutePrice();
                    }
                } else {
                    getRoutePrice();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("UOM Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "UOM Fail: " + error.getMessage());
                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman") || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {
                    if (Integer.parseInt(db.getAgentPriceCount()) > 0) {
                        if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                            getLoadList();
                        } else {
                            getPricingList();
                        }
                    } else {
                        getRoutePrice();
                    }
                } else {
                    getRoutePrice();
                }
            }
        });
    }

    private void getLoadList() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getLoadList(App.LOADLIST, routeId, Settings.getString(App.SALESMANID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Load Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Load Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Load> arrLoad = new ArrayList<>();
                        arrLoad = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Load>>() {
                                }.getType());
                        db.insertLoad(arrLoad);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Load Response: Blank");
                }

                getAgentList();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Load Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Load Fail: " + error.getMessage());
                getAgentList();
            }
        });

    }

    //Deport AGENT
    private void getAgentList() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getDeliveryList(App.GET_AGENT_LIST, Settings.getString(App.SALESMANID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Agent List Response", response.toString());

                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Agent List Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<DepotData> arrData = new ArrayList<>();
                        arrData = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<DepotData>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_DEPOT);
                        db.insertDepot(arrData);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Agent List Response: Blank");
                }

                getCollectionList();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Agent List Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Agent List Fail: " + error.getMessage());
                getCollectionList();
            }
        });

    }

    private void getCollectionList() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getCollectionList(App.OUTSTANDING_INVOICE, routeId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Collection Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Collection Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<CollectionData> arrCollection = new ArrayList<>();
                        arrCollection = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<CollectionData>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_COLLECTION);
                        db.insertOutstandingCollection(arrCollection);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Collection Response: Blank");
                }

                getDeliveryList();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Collection Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Collection Fail: " + error.getMessage());
                getDeliveryList();
            }
        });

    }

    private void getDeliveryList() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getDeliveryList(App.GET_DELIVERY, Settings.getString(App.SALESMANID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Delivery Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Delivery Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Delivery> arrDelivery = new ArrayList<>();
                        arrDelivery = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Delivery>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_DELIVERY_HEADER);
                        db.deleteTable(db.TABLE_DELIVERY_ITEMS);
                        db.insertDeliveryItems(arrDelivery);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Delivery Response: Blank");
                }

                getReturnList();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Delivery Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Delivery Fail: " + error.getMessage());
                getReturnList();
            }
        });
    }

    private void getReturnList() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getDeliveryList(App.RETURN_LIST, Settings.getString(App.SALESMANID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Return List Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Return List Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Return> arrReturn = new ArrayList<>();
                        arrReturn = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Return>>() {
                                }.getType());
                        db.insertReturnListItems(arrReturn);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Return List Response: Blank");
                }

                // getRoutePrice();
                getFreeGoods();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Return List Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Return List Fail: " + error.getMessage());
                //getRoutePrice();
                getFreeGoods();
            }
        });
    }

    private void getRoutePrice() {
        String routeId = Settings.getString(App.ASSOCIATED_ROUTEID);

        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")
                || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {
            routeId = Settings.getString(App.ROUTEID);
            getAgentPricingList(routeId, true);
            //getPricingList();
        } else if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Merchandising")) {
            routeId = Settings.getString(App.ROUTEID);
            getAgentPricingList(routeId, true);
            //getPricingList();
        } else {
            if (!routeId.equalsIgnoreCase("")) {
                String[] spltRout = routeId.split(",");

                for (int i = 0; i < spltRout.length; i++) {
                    boolean isLast = false;
                    if (i == (spltRout.length - 1)) {
                        isLast = true;
                    }
                    getAgentPricingList(spltRout[i], isLast);
                }
            } else {
                getPricingList();
            }
        }

    }

    private void getAgentPricingList(String routeId, boolean isLast) {

        final Call<JsonObject> labelResponse = ApiClient.getService().getPricingList(App.PRICING_ROUTE_LIST, routeId, Settings.getString(App.AGENTID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Agent Pricing Response", response.body().toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Agent Pricing Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Pricing> arrPricing = new ArrayList<>();
                        arrPricing = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Pricing>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_AGENT_PRICE_COUNT);
                        db.insertAgentPricingCount(arrPricing, routeId);
                        db.deleteTable(db.TABLE_AGENT_PRICING);
                        db.insertAgentPricingItems(arrPricing, routeId);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Agent Pricing Response: Blank");
                }

                if (isLast) {
                    if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                        getLoadList();
                    } else {
                        getPricingList();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Agent Pricing Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Agent Pricing Fail: " + error.getMessage());
                if (isLast) {
                    if (Integer.parseInt(db.getCustomerPriceCount()) > 0) {
                        getLoadList();
                    } else {
                        getPricingList();
                    }
                }
            }
        });
    }

    private void getPricingList() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getPricingList(App.PRICING_LIST, Settings.getString(App.ROUTEID), Settings.getString(App.AGENTID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Pricing Response", response.toString());

                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Pricing Response: " + response.body().toString());

                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Pricing> arrPricing = new ArrayList<>();
                        arrPricing = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Pricing>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_CUSTOMER_PRICING);
                        db.insertPricingItems(arrPricing);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Pricing Response: Blank");
                }

                getLoadList();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Pricing Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Pricing Fail: " + error.getMessage());
                getLoadList();
            }
        });
    }

    private void getFreeGoods() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getRoutePromotion(App.FREE_GOODS_NEW, Settings.getString(App.ROUTEID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Goods Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Free Goods Response: " + response.body().toString());
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
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Free Goods Response: Blank");
                }

                getCustomerFreeGoods();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Goods Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Free Goods Fail: " + error.getMessage());
                getCustomerFreeGoods();
            }
        });
    }

    private void getCustomerFreeGoods() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getRoutePromotion(App.CUSTOMER_FREE_GOODS_NEW, Settings.getString(App.ROUTEID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("CustomerFree Goods Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "CustomerFree Goods Response: " + response.body().toString());
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
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "CustomerFree Goods Response: Blank");
                }

                getAgentFreeGoods();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("CustomerFree Goods Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "CustomerFree Goods Fail: " + error.getMessage());
                getAgentFreeGoods();
            }
        });
    }

    private void getAgentFreeGoods() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getAgentPromotion(App.AGENT_FREE_GOODS_NEW, Settings.getString(App.AGENTID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Free Goods Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Free Goods Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<PromoItems> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<PromoItems>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_AGENT_PROMO);
                        db.deleteTable(db.TABLE_AGENT_PROMO_SLAB);
                        db.deleteTable(db.TABLE_AGENT_ORDER_ITEM);
                        db.deleteTable(db.TABLE_AGENT_OFFER_ITEM);
                        db.insertNewAgentFreeGoods(arrItems);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Free Goods Response: Blank");
                }

                getItemDiscount();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Free Goods Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Free Goods Fail: " + error.getMessage());
                getItemDiscount();
            }
        });
    }

    private void getItemDiscount() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.ROUTE_ITEM_DISCOUNT);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Item Discount Response", response.toString());

                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Item Discount Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<DiscountData> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<DiscountData>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_DISCOUNT_MAIN_HEADER);
                        db.deleteTable(db.TABLE_DISCOUNT_SITEM);
                        db.deleteTable(db.TABLE_DISCOUNT_SLAB);
                        db.insertItemDiscount(arrItems);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Item Discount Response: Blank");
                }

                getCoustomerItemDiscount();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Item Discount Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Item Discount Fail: " + error.getMessage());
                getCoustomerItemDiscount();
            }
        });
    }
    private void getCoustomerItemDiscount() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomerDiscountList(App.CUSTOMER_ITEM_DISCOUNT, Settings.getString(App.ROUTEID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Item Discount Response", response.body().toString());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Item Discount Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<DiscountData> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<DiscountData>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_DISCOUNT_CUSTOMER_HEADER);
                        db.insertCustomerItemDiscount(arrItems);
                    }
                }

                getRouteCategoryDiscount();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Item Discount Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Item Discount Fail: " + error.getMessage());
                getRouteCategoryDiscount();
            }
        });
    }

//    private void getCategoryDiscount() {
//
//        final Call<JsonObject> labelResponse = ApiClient.getService().getPricingList(App.CUSTOMER_CATEGORY_DISCOUNT, Settings.getString(App.ROUTEID), Settings.getString(App.AGENTID));
//
//        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                Log.e("Category Discount Response", response.body().toString());
//                UtilApp.logData(DownloadingDataActivity.this, "Category Discount Response: " + response.body().toString());
//                if (response.body() != null) {
//                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
//                        ArrayList<Discount> arrItems = new ArrayList<>();
//                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
//                                new TypeToken<List<Discount>>() {
//                                }.getType());
//                        db.deleteTable(db.TABLE_CATEGORY_DISCOUNT);
//                        db.insertCategoryDiscount(arrItems);
//                    }
//                }
//
//                getRouteCategoryDiscount();
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable error) {
//                Log.e("Category Discount Fail", error.getMessage());
//                UtilApp.logData(DownloadingDataActivity.this, "Category Discount Fail: " + error.getMessage());
//                getRouteCategoryDiscount();
//            }
//        });
//    }

    private void getRouteCategoryDiscount() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomerDiscountList(App.ROUTE_CATEGORY_DISCOUNT, Settings.getString(App.ROUTEID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Route Category Discount Response", response.body().toString());
                UtilApp.logData(DownloadingDataActivity.this, "Route Category Discount Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<DiscountData> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<DiscountData>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_DISCOUNT_CUSTOMER_EXCLUDE);
                        db.deleteTable(db.TABLE_DISCOUNT_MAIN_CATEGORY);
                        db.insertRouteCategoryDiscount(arrItems);
                    }
                }

                getCustomerType();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Route Category Discount Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Route Category Discount Fail: " + error.getMessage());
                getCustomerType();
            }
        });
    }

    private void getCustomerType() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.CUSTOMER_TYPE);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Type Response", response.body().toString());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Type Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<CustomerType> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<CustomerType>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_CUSTOMER_TYPE);
                        db.insertCustomerType(arrItems);
                    }
                }

                getCustomerCategory();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Type Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Type Fail: " + error.getMessage());
                getCustomerCategory();
            }
        });
    }

    private void getCustomerCategory() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.CUSTOMER_CATEGORY);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Category Response", response.body().toString());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Category Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<CustomerType> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<CustomerType>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_CUSTOMER_CATEGORY);
                        db.insertCustomerCategory(arrItems);
                    }
                }

                getCustomerChannel();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Category Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Category Fail: " + error.getMessage());
                getCustomerChannel();
            }
        });
    }

    private void getCustomerChannel() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.CUSTOMER_CHANNEL);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Channel Response", response.body().toString());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Channel Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<ChannellType> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<ChannellType>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_CUSTOMER_CHANNEL);
                        db.insertCustomerChannel(arrItems);
                    }
                }

                getCustomerSubCategory();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Channel Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Channel Fail: " + error.getMessage());
                getCustomerSubCategory();
            }
        });
    }

    private void getCustomerSubCategory() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getUOM(App.CUSTOMER_SUB_CATEGORY);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer SubCategory Response", response.body().toString());
                UtilApp.logData(DownloadingDataActivity.this, "Customer SubCategory Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<SubCategoryType> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<SubCategoryType>>() {
                                }.getType());
                        db.deleteTable(db.TABLE_CUSTOMER_SUB_CATEGORY);
                        db.insertCustomerSubCategory(arrItems);
                    }
                }

                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                    getHSPSensorSurvey();
                } else {
                    getPlanogramList();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer SubCategory Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer SubCategory Fail: " + error.getMessage());
                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                    getHSPSensorSurvey();
                } else {
                    getPlanogramList();
                }
            }
        });
    }


    private void getPlanogramList() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getPlanogramList(App.PLANOGRAM, Settings.getString(App.ROUTEID), Settings.getString(App.ASSOCIATED_ROUTEID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Planogram Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Planogram Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Planogram> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Planogram>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_PLANOGRAM);
                        db.insertPlanogram(arrItems);
                    }
                }

                getAssetsList();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Planogram Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Planogram Fail: " + error.getMessage());
                getAssetsList();
            }
        });

    }

    private void getAssetsList() {
        final Call<JsonObject> labelResponse = ApiClient.getService().getPlanogramList(App.ASSETS_LIST, Settings.getString(App.ROUTEID), Settings.getString(App.ASSOCIATED_ROUTEID));
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Assets Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Assets Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<ASSETS_MODEL> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<ASSETS_MODEL>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_CUSTOMER_ASSETS);
                        db.insertCustomerAssets(arrItems);
                    }
                }
                getAssetsSurveyTools();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Assets Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Assets Fail: " + error.getMessage());
                getAssetsSurveyTools();
            }
        });
    }

    private void getAssetsSurveyTools() {
        final Call<JsonObject> labelResponse = ApiClient.getService().getPlanogramList(App.ASSETS_SURVEY_LIST, Settings.getString(App.ROUTEID), Settings.getString(App.ASSOCIATED_ROUTEID));
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Assets Survey Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Assets Survey Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Survey_Tools> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Survey_Tools>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_ASSETS_QUESTIONS);
                        db.insertAssetsSurvey(arrItems);
                    }
                }

                getSurvetTools();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Assets Survey Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Assets Survey Fail: " + error.getMessage());
                getSurvetTools();
            }
        });
    }

    private void getSurvetTools() {
        final Call<JsonObject> labelResponse = ApiClient.getService().getSurveyTool(App.SURVEY_TOOLS);
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Survey Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Survey Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Survey_Tools> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Survey_Tools>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_SURVEY_QUESTIONS);
                        db.insertSurvey(arrItems);
                    }
                }

                getSensorSurvey();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Survey Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Survey Fail: " + error.getMessage());
                getSensorSurvey();
            }
        });
    }

    private void getHSPSensorSurvey() {
        final Call<JsonObject> labelResponse = ApiClient.getService().getSurveyTool(App.SENSOR_SURVEY);
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("SensorySurvey Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "SensorySurvey Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Survey_Tools> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Survey_Tools>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_SENSOR_QUESTIONS);
                        db.insertSensorSurvey(arrItems);
                    }
                }
                getCustomer_otc();
                //  go();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("SensorySurvey Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "SensorySurvey Fail: " + error.getMessage());
                //go();
                getCustomer_otc();
            }
        });
    }

    private void getSensorSurvey() {
        final Call<JsonObject> labelResponse = ApiClient.getService().getSurveyTool(App.SENSOR_SURVEY);
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("SensorySurvey Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "SensorySurvey Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Survey_Tools> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Survey_Tools>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_SENSOR_QUESTIONS);
                        db.insertSensorSurvey(arrItems);
                    }
                }

                getConsumerSurvey();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("SensorySurvey Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "SensorySurvey Fail: " + error.getMessage());
                getConsumerSurvey();
            }
        });
    }

    private void getConsumerSurvey() {
        final Call<JsonObject> labelResponse = ApiClient.getService().getPlanogramList(App.CONSUMER_SURVEY, Settings.getString(App.ROUTEID), Settings.getString(App.ASSOCIATED_ROUTEID));
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("ConsumerSurvey Response", response.body().toString());
                UtilApp.logData(DownloadingDataActivity.this, "ConsumerSurvey Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Survey_Tools> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Survey_Tools>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_CONSUMER_QUESTIONS);
                        db.insertConsumerSurvey(arrItems);
                    }
                }

                getInventory();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("ConsumerSurvey Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "ConsumerSurvey Fail: " + error.getMessage());
                getInventory();
            }
        });
    }

    private void getInventory() {
        final Call<JsonObject> labelResponse = ApiClient.getService().getPlanogramList(App.INVENTORY, Settings.getString(App.ROUTEID), Settings.getString(App.ASSOCIATED_ROUTEID));
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Inventory Response", response.body().toString());
                UtilApp.logData(DownloadingDataActivity.this, "Inventory Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Inventory> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Inventory>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_CUSTOMER_INVENTORY_HEADER);
                        db.deleteTable(DBManager.TABLE_CUSTOMER_INVENTORY_ITEMS);
                        db.insertInventory(arrItems);
                    }
                }

                getPromotionalItem();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Inventory Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Inventory Fail: " + error.getMessage());
                getPromotionalItem();
            }
        });
    }

    private void getPromotionalItem() {
        final Call<JsonObject> labelResponse = ApiClient.getService().getSurveyTool(App.PROMOTIONAL_LIST);
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Pramotional Response", response.body().toString());
                UtilApp.logData(DownloadingDataActivity.this, "Pramotional Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Promotion_Item> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Promotion_Item>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_PROMOTION_ITEM);
                        db.insertPromotionItemList(arrItems);
                    }
                }

                getDistribution();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Pramotional Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Pramotional Fail: " + error.getMessage());
                getDistribution();
            }
        });
    }

    private void getDistribution() {
        final Call<JsonObject> labelResponse = ApiClient.getService().getPlanogramList(App.DISTRIBUTION_ITEM, Settings.getString(App.ROUTEID), Settings.getString(App.ASSOCIATED_ROUTEID));
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Distribution Response", response.body().toString());
                UtilApp.logData(DownloadingDataActivity.this, "Distribution Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Distribution> arrItems = new ArrayList<>();
                        arrItems = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Distribution>>() {
                                }.getType());
                        db.deleteTable(DBManager.TABLE_DISTRIBUTION);
                        db.insertDISTRIBUTION(arrItems);
                    }
                }

                go();
                // getCustomer_otc();
                //getDeliveryMerchantList();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Distribution Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Distribution Fail: " + error.getMessage());
                //getDeliveryMerchantList();
                go();
                // getCustomer_otc();
            }
        });
    }

    private void getCustomer_otc() {
        routeId = Settings.getString(App.ROUTEID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomer_otc(App.CUSTOMER_OTC, routeId, UtilApp.salesmanType(Settings.getString(App.ROLE)));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Customer Response1: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Customer> arrCustomer = new ArrayList<>();
                        arrCustomer = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Customer>>() {
                                }.getType());
                        //db.deleteTable(DBManager.TABLE_CUSTOMER);
                        db.insertCustomer(arrCustomer);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Customer Response: Blank");
                }
                go();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Customer Failure: " + error.getMessage());
                go();
            }
        });
    }

    private void getDeliveryMerchantList() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getDeliveryMerchantList(App.GET_MERCHANT_DELIVERY, Settings.getString(App.ROUTEID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Merchant Delivery Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(DownloadingDataActivity.this, "Merchant Delivery Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Delivery> arrDelivery = new ArrayList<>();
                        arrDelivery = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Delivery>>() {
                                }.getType());
                        db.insertDeliveryAcceptItems(arrDelivery);
                    }
                } else {
                    UtilApp.logData(DownloadingDataActivity.this, "Merchant Delivery Response: Blank");
                }

                go();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Merchant Delivery Fail", error.getMessage());
                UtilApp.logData(DownloadingDataActivity.this, "Merchant Delivery Fail: " + error.getMessage());
                go();
            }
        });
    }
}
