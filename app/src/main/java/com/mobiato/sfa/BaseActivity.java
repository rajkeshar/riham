package com.mobiato.sfa;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.Fragments.DashboardFragment;
import com.mobiato.sfa.Techinician.NotificationTechnicianActivity;
import com.mobiato.sfa.Techinician.fragment.TechnicianChillerListFragment;
import com.mobiato.sfa.Techinician.fragment.TechnicianCustomerListFragment;
import com.mobiato.sfa.Techinician.fragment.TechnicianDashboardFragment;
import com.mobiato.sfa.activity.DataPostingActivity;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.activity.LoadRequestActivity;
import com.mobiato.sfa.activity.LoginActivity;
import com.mobiato.sfa.activity.ManageInventoryActivity;
import com.mobiato.sfa.activity.MasterDataActivity;
import com.mobiato.sfa.activity.MultiCollectionActivity;
import com.mobiato.sfa.activity.NotificationActivity;
import com.mobiato.sfa.activity.PaymentActivity;
import com.mobiato.sfa.activity.SalesmanLoadActivity;
import com.mobiato.sfa.activity.SettingActivity;
import com.mobiato.sfa.activity.SplashActivity;
import com.mobiato.sfa.activity.forms.FormsActivity;
import com.mobiato.sfa.activity.servicevisit.PendingBDActivity;
import com.mobiato.sfa.activity.servicevisit.ServiceEquipmentActivity;
import com.mobiato.sfa.activity.servicevisit.ServiceVisitThreeActivity;
import com.mobiato.sfa.databinding.ActivityBaseBinding;
import com.mobiato.sfa.databinding.RowDrawerFooterItemBinding;
import com.mobiato.sfa.databinding.RowDrawerHeaderBinding;
import com.mobiato.sfa.databinding.RowDrawerListItemBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.merchandising.CompetitorInformationActivity;
import com.mobiato.sfa.merchandising.DeliveryListActivity;
import com.mobiato.sfa.merchandising.JourneyPlanActivity;
import com.mobiato.sfa.merchandising.MerchantDashboardActivity;
import com.mobiato.sfa.merchandising.MerchantDataPostingActivity;
import com.mobiato.sfa.merchandising.PromotionalListActivity;
import com.mobiato.sfa.merchandising.SensoryListActivity;
import com.mobiato.sfa.model.CollectionData;
import com.mobiato.sfa.model.DrawerEnum;
import com.mobiato.sfa.model.DrawerItem;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Profile;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.rest.SyncData;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class BaseActivity extends AbstractBaseActivity {

    public BaseActivity me;
    public ActivityBaseBinding baseBinding;
    public FragmentManager fragmentManager;
    public String url;
    private List<Object> drawerItemArrayList = new ArrayList<>();
    private DrawerRecyclerAdapter adapter;
    private DBManager db;
    private File photoFile_chiller;
    private String mCurrentPhotoPath, mFilePath;
    private static final int TAKE_PHOTO_CHILLER_REQUEST = 70;
    private String strAttendanceType = "In";
    private LoadingSpinner progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath(getResources().getString(R.string.regular_font))
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.regular_font))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/

        db = new DBManager(BaseActivity.this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        init();
        version();

        progressDialog = new LoadingSpinner(BaseActivity.this);

    }

    /**
     * Initialization of object members
     */
    private void init() {

        /**Initialize me object*/
        me = this;

        url = "android.resource://" + getPackageName() + "/";

        /**Setup window setting*/
        setToolBar(baseBinding.mToolbar);

        /**Initialize of fragmentManager*/
        fragmentManager = getSupportFragmentManager();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

//        baseBinding.mSearchView.setHint(getString(R.string.search));

        if (getRedirectClass() == null)
            setRedirectClass(SplashActivity.class);


        baseBinding.mSearchView.setOnSearchViewListener(new com.miguelcatalan.materialsearchview.MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                baseBinding.mSearchView.setVisibility(View.GONE);
                baseBinding.toolbarTitle.setVisibility(View.VISIBLE);
            }
        });

        baseBinding.lytNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
                    startActivity(new Intent(BaseActivity.this, NotificationTechnicianActivity.class));
                } else {
                    startActivity(new Intent(BaseActivity.this, NotificationActivity.class));
                }
            }
        });
    }

    public void setNotification() {
        baseBinding.lytNotification.setVisibility(View.VISIBLE);
        if (App.countNoti > 0) {
            baseBinding.tvCount.setVisibility(View.VISIBLE);
            baseBinding.tvCount.setText("" + App.countNoti);
        } else {
            baseBinding.tvCount.setVisibility(View.VISIBLE);
        }
    }

    public void setNotification_technician() {
        baseBinding.lytNotification.setVisibility(View.VISIBLE);
        String count = Settings.getString(App.countNoti_tech);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("Notificationcount", "0");
        if (!s1.equals("0")) {
            baseBinding.tvCount.setVisibility(View.VISIBLE);
            baseBinding.tvCount.setText("" + s1);
        } else {
            baseBinding.tvCount.setVisibility(View.GONE);
        }
    }

    protected void setToolBar(Toolbar mToolBar) {
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getWindow().setWindowAnimations(0);
            setSupportActionBar(mToolBar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            baseBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public void showSearch() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        baseBinding.toolbarTitle.setVisibility(View.GONE);
        baseBinding.mSearchView.setVisibility(View.VISIBLE);
        baseBinding.mSearchView.showSearch();
    }

    public void hideSearch() {
        baseBinding.mSearchView.closeSearch();
    }

    public void tonggleSearchView() {
        if (baseBinding.mSearchView.getVisibility() == View.VISIBLE) {
            hideSearch();
        } else {
            showSearch();
        }
    }

    /**
     * Setup toolbar and navigation drawer
     */
    protected void setNavigationView() {
        setDrawerData();
        DrawerLayout drawer = baseBinding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, baseBinding.mToolbar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        baseBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        baseBinding.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    }

    protected void setDrawerData() {

        drawerItemArrayList.clear();
        Profile profile = new Profile().setName("Admin").setImgRes(R.drawable.ic_launcher_background);
        drawerItemArrayList.add(profile);
        // drawerItemArrayList.add("version:0000");
        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
            getTechnicianMenu();
        } else if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")
                || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {
            getSalesMenu();
        } else {
            getMerchantMenu();
        }

        if (adapter == null) {
            adapter = new DrawerRecyclerAdapter();
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            baseBinding.drawerRecycler.setHasFixedSize(true);
            baseBinding.drawerRecycler.setLayoutManager(mLayoutManager);
            baseBinding.drawerRecycler.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void getTechnicianMenu() {
        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.tab_Home))
                .setDrawerEnum(DrawerEnum.TechDashboard)
                .setImgRes(R.drawable.dashboard));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_customer))
                .setDrawerEnum(DrawerEnum.TechCustomer)
                .setImgRes(R.drawable.jorney));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_chiller))
                .setDrawerEnum(DrawerEnum.ChillerList)
                .setImgRes(R.drawable.jorney));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_service_visit))
                .setDrawerEnum(DrawerEnum.TechService)
                .setImgRes(R.drawable.jorney));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_print))
                .setDrawerEnum(DrawerEnum.Print)
                .setImgRes(R.drawable.printer));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_pendingBD))
                .setDrawerEnum(DrawerEnum.Settings)
                .setImgRes(R.drawable.ticket));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_logout))
                .setDrawerEnum(DrawerEnum.Logout)
                .setImgRes(R.drawable.logout));
    }

    public void getMerchantMenu() {
        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.tab_Home))
                .setDrawerEnum(DrawerEnum.Dashboard)
                .setImgRes(R.drawable.dashboard));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_journey))
                .setDrawerEnum(DrawerEnum.Journey_Plan)
                .setImgRes(R.drawable.jorney));

       /* drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_deliveryList))
                .setDrawerEnum(DrawerEnum.Delivery)
                .setImgRes(R.drawable.ic_icon_delivery));*/

       /* drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_form))
                .setDrawerEnum(DrawerEnum.Forms)
                .setImgRes(R.drawable.forms));*/

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.tab_Competitor))
                .setDrawerEnum(DrawerEnum.Competitor_Information)
                .setImgRes(R.drawable.target));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.tab_promotional))
                .setDrawerEnum(DrawerEnum.Promotional_Accountablity)
                .setImgRes(R.drawable.career));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.tab_Sensor))
                .setDrawerEnum(DrawerEnum.Sensor_Evaluation)
                .setImgRes(R.drawable.sensor));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_print))
                .setDrawerEnum(DrawerEnum.Print)
                .setImgRes(R.drawable.printer));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_settings))
                .setDrawerEnum(DrawerEnum.Settings)
                .setImgRes(R.drawable.settings));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_logout))
                .setDrawerEnum(DrawerEnum.Logout)
                .setImgRes(R.drawable.logout));
    }

    public void getSalesMenu() {

//        if (Settings.getString(App.IS_ATTENDANCE_IN).equalsIgnoreCase("1")) {
//            drawerItemArrayList.add(new DrawerItem()
//                    .setName(getString(R.string.nav_attendanceOut))
//                    .setDrawerEnum(DrawerEnum.Attendance)
//                    .setImgRes(R.drawable.ic_attendance));
//        } else {
//
//            drawerItemArrayList.add(new DrawerItem()
//                    .setName(getString(R.string.nav_attendanceIn))
//                    .setDrawerEnum(DrawerEnum.Attendance)
//                    .setImgRes(R.drawable.ic_attendance));
//        }

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_home))
                .setDrawerEnum(DrawerEnum.Home)
                .setImgRes(R.drawable.home));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_inventory))
                .setDrawerEnum(DrawerEnum.Manage_Load)
                .setImgRes(R.drawable.inventory));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_journey))
                .setDrawerEnum(DrawerEnum.Journey_Plan)
                .setImgRes(R.drawable.journey_plan));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_payment))
                .setDrawerEnum(DrawerEnum.Payment)
                .setImgRes(R.drawable.payment));

      /*  drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_form))
                .setDrawerEnum(DrawerEnum.Forms)
                .setImgRes(R.drawable.forms));*/


        if (UtilApp.salesmanRole(Settings.getString(App.ROLE)).equalsIgnoreCase("Hariss salesman")) {
            drawerItemArrayList.add(new DrawerItem()
                    .setName(getString(R.string.nav_loadRequest))
                    .setDrawerEnum(DrawerEnum.Load_Request)
                    .setImgRes(R.drawable.inventory));

            drawerItemArrayList.add(new DrawerItem()
                    .setName(getString(R.string.nav_salesmanLoad))
                    .setDrawerEnum(DrawerEnum.Salesman_Load)
                    .setImgRes(R.drawable.inventory));

            drawerItemArrayList.add(new DrawerItem()
                    .setName(getString(R.string.tab_Competitor))
                    .setDrawerEnum(DrawerEnum.Competitor_Information)
                    .setImgRes(R.drawable.target));

            drawerItemArrayList.add(new DrawerItem()
                    .setName(getString(R.string.tab_Sensor))
                    .setDrawerEnum(DrawerEnum.Sensor_Evaluation)
                    .setImgRes(R.drawable.sensor));
        } else if (UtilApp.salesmanRole(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team")) {

            drawerItemArrayList.add(new DrawerItem()
                    .setName(getString(R.string.nav_salesmanLoad))
                    .setDrawerEnum(DrawerEnum.Salesman_Load)
                    .setImgRes(R.drawable.inventory));

            drawerItemArrayList.add(new DrawerItem()
                    .setName(getString(R.string.tab_Competitor))
                    .setDrawerEnum(DrawerEnum.Competitor_Information)
                    .setImgRes(R.drawable.target));

            drawerItemArrayList.add(new DrawerItem()
                    .setName(getString(R.string.tab_Sensor))
                    .setDrawerEnum(DrawerEnum.Sensor_Evaluation)
                    .setImgRes(R.drawable.sensor));
        }

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_print))
                .setDrawerEnum(DrawerEnum.Print)
                .setImgRes(R.drawable.printer));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_settings))
                .setDrawerEnum(DrawerEnum.Settings)
                .setImgRes(R.drawable.settings));

        drawerItemArrayList.add(new DrawerItem()
                .setName(getString(R.string.nav_logout))
                .setDrawerEnum(DrawerEnum.Logout)
                .setImgRes(R.drawable.logout));
    }

    private void closeDrawer() {
        baseBinding.drawerLayout.closeDrawers();
    }

    private class DrawerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    return new ViewHolderHeader(RowDrawerHeaderBinding.inflate(getLayoutInflater(), parent, false));
                default:
                    return new ViewHolder(RowDrawerListItemBinding.inflate(getLayoutInflater(), parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            Object obj = drawerItemArrayList.get(position);
            if (holder instanceof ViewHolderHeader) {
                DBManager db = new DBManager(BaseActivity.this);
                Salesman mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
                if (mSalesman != null) {
                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Merchandising")) {
                        ((ViewHolderHeader) holder).itemBinding.tvTitle.setText("Merchandiser Details :");
                        ((ViewHolderHeader) holder).itemBinding.txtSalemaneNo.setText("Merchandiser No: " + mSalesman.getSalesmanCode());
                        ((ViewHolderHeader) holder).itemBinding.txtRoute.setText("Route: " + mSalesman.getRouteName());
                        ((ViewHolderHeader) holder).itemBinding.txtVehicleNo.setText("Vehicle No: " + mSalesman.getVehicle_no());
                        ((ViewHolderHeader) holder).itemBinding.txtChannel.setVisibility(View.GONE);
                        ((ViewHolderHeader) holder).itemBinding.txtChannel.setText("Channel: " + mSalesman.getDist_channel());
                        ((ViewHolderHeader) holder).itemBinding.txtRoll.setText("Role: " + UtilApp.salesmanRole(mSalesman.getRole()));
                        ((ViewHolderHeader) holder).itemBinding.name.setText(mSalesman.getSalesmanName());
                        ((ViewHolderHeader) holder).itemBinding.toggleButton.setVisibility(View.GONE);
                    } else if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
                        ((ViewHolderHeader) holder).itemBinding.tvTitle.setText("Technician Details :");
                        ((ViewHolderHeader) holder).itemBinding.txtSalemaneNo.setText("Salesman No: " + mSalesman.getSalesmanCode());
                        ((ViewHolderHeader) holder).itemBinding.txtRoute.setVisibility(View.GONE);
                        ((ViewHolderHeader) holder).itemBinding.txtVehicleNo.setVisibility(View.GONE);
                        ((ViewHolderHeader) holder).itemBinding.txtChannel.setVisibility(View.GONE);
                        ((ViewHolderHeader) holder).itemBinding.txtChannel.setText("Channel: " + mSalesman.getDist_channel());
                        if (mSalesman.getRole() != null)
                            ((ViewHolderHeader) holder).itemBinding.txtRoll.setText("Role: " + UtilApp.salesmanRole(mSalesman.getRole()));
                        else
                            ((ViewHolderHeader) holder).itemBinding.txtRoll.setText("Role: " + UtilApp.salesmanRole(Settings.getString(App.ROLE)));
                        ((ViewHolderHeader) holder).itemBinding.toggleButton.setVisibility(View.GONE);
                        ((ViewHolderHeader) holder).itemBinding.name.setText(mSalesman.getSalesmanName());
                    } else {
                        ((ViewHolderHeader) holder).itemBinding.tvTitle.setText("Salesman Details :");
                        ((ViewHolderHeader) holder).itemBinding.txtSalemaneNo.setText("Salesman No: " + mSalesman.getSalesmanCode());
                        ((ViewHolderHeader) holder).itemBinding.txtRoute.setText("Route: " + mSalesman.getRouteName());
                        ((ViewHolderHeader) holder).itemBinding.txtVehicleNo.setText("Vehicle No: " + mSalesman.getVehicle_no());
                        ((ViewHolderHeader) holder).itemBinding.txtChannel.setVisibility(View.GONE);
                        ((ViewHolderHeader) holder).itemBinding.txtChannel.setText("Channel: " + mSalesman.getDist_channel());
                        if (mSalesman.getRole() != null) {
                            if (mSalesman.getRole().equals("6")) {
                                ((ViewHolderHeader) holder).itemBinding.txtRoll.setText("Role: " + Settings.getString(App.PROJECT_NAME));
                            } else {
                                ((ViewHolderHeader) holder).itemBinding.txtRoll.setText("Role: " + UtilApp.salesmanRole(mSalesman.getRole()));
                            }
                        } else {
                            if (Settings.getString(App.ROLE).equals("6")) {
                                ((ViewHolderHeader) holder).itemBinding.txtRoll.setText("Role: " + Settings.getString(App.PROJECT_NAME));
                            } else {
                                ((ViewHolderHeader) holder).itemBinding.txtRoll.setText("Role: " + UtilApp.salesmanRole(Settings.getString(App.ROLE)));
                            }
                        }

                        ((ViewHolderHeader) holder).itemBinding.name.setText(mSalesman.getSalesmanName());

                        ((ViewHolderHeader) holder).itemBinding.toggleButton.setVisibility(View.VISIBLE);
                        if (Settings.getString(App.IS_LOAD_VERIFY).equalsIgnoreCase("1") &&
                                Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("1")) {
                            if (Settings.getString(App.IS_ATTENDANCE_OUT).equalsIgnoreCase("1")) {
                                ((ViewHolderHeader) holder).itemBinding.toggleButton.setChecked(false);
                            } else {
                                ((ViewHolderHeader) holder).itemBinding.toggleButton.setChecked(true);
                            }
                        } else {
                            if (Settings.getString(App.IS_ATTENDANCE_OUT).equalsIgnoreCase("1")) {
                                ((ViewHolderHeader) holder).itemBinding.toggleButton.setChecked(false);
                            } else {
                                if (Settings.getString(App.IS_ATTENDANCE_IN).equalsIgnoreCase("1")) {
                                    ((ViewHolderHeader) holder).itemBinding.toggleButton.setChecked(true);
                                } else {
                                    ((ViewHolderHeader) holder).itemBinding.toggleButton.setChecked(false);
                                }
                            }

                        }

                        ((ViewHolderHeader) holder).itemBinding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                                if (Settings.getString(App.IS_ATTENDANCE_IN).equalsIgnoreCase("0")) {
                                    if (isChecked) {
                                        strAttendanceType = "In";
                                        dispatchTakeChillerPictureIntent();
                                    }
                                } else {
                                    if (Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("1")) {
                                        if (Settings.getString(App.IS_ATTENDANCE_OUT).equalsIgnoreCase("0")) {
                                            if (!isChecked) {
                                                strAttendanceType = "Out";
                                                dispatchTakeChillerPictureIntent();
                                            }
                                        }
                                    } else {
                                        ((ViewHolderHeader) holder).itemBinding.toggleButton.setChecked(true);
                                    }
                                }
                            }
                        });
                    }

                }
                ((ViewHolderHeader) holder).itemBinding.executePendingBindings();
            } else if (holder instanceof ViewHolder) {
                ((ViewHolder) holder).itemBinding.setDrawerItem((DrawerItem) obj);

                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")
                        || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {

//                    if (((DrawerItem) obj).getName().equalsIgnoreCase(getString(R.string.nav_attendanceOut))) {
//                        if (Settings.getString(App.IS_ATTENDANCE_OUT).equalsIgnoreCase("1")) {
//                            ((ViewHolder) holder).itemBinding.getRoot().setClickable(false);
//                            ((ViewHolder) holder).itemBinding.getRoot().setEnabled(false);
//                            ((ViewHolder) holder).itemBinding.getRoot().setAlpha(0.5f);
//                        } else {
//                            if (Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("1")) {
//                                ((ViewHolder) holder).itemBinding.getRoot().setClickable(true);
//                                ((ViewHolder) holder).itemBinding.getRoot().setEnabled(true);
//                                ((ViewHolder) holder).itemBinding.getRoot().setAlpha(1.0f);
//                            } else {
//                                ((ViewHolder) holder).itemBinding.getRoot().setClickable(false);
//                                ((ViewHolder) holder).itemBinding.getRoot().setEnabled(false);
//                                ((ViewHolder) holder).itemBinding.getRoot().setAlpha(0.5f);
//                            }
//
//                        }
//                    } else
                    if (((DrawerItem) obj).getName().equalsIgnoreCase(getString(R.string.nav_journey))) {
//                        if (Settings.getString(App.IS_ATTENDANCE_IN).equalsIgnoreCase("1") &&
//                                Settings.getString(App.IS_LOAD_VERIFY).equalsIgnoreCase("1") &&
//                                Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("0")) {
//                            ((ViewHolder) holder).itemBinding.getRoot().setClickable(true);
//                            ((ViewHolder) holder).itemBinding.getRoot().setEnabled(true);
//                            ((ViewHolder) holder).itemBinding.getRoot().setAlpha(1.0f);
//                        } else {
//                            ((ViewHolder) holder).itemBinding.getRoot().setClickable(false);
//                            ((ViewHolder) holder).itemBinding.getRoot().setEnabled(false);
//                            ((ViewHolder) holder).itemBinding.getRoot().setAlpha(0.5f);
//                        }

                        if (Settings.getString(App.IS_LOAD_VERIFY).equalsIgnoreCase("1") &&
                                Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("0")) {
                            ((ViewHolder) holder).itemBinding.getRoot().setClickable(true);
                            ((ViewHolder) holder).itemBinding.getRoot().setEnabled(true);
                            ((ViewHolder) holder).itemBinding.getRoot().setAlpha(1.0f);
                        } else {
                            ((ViewHolder) holder).itemBinding.getRoot().setClickable(false);
                            ((ViewHolder) holder).itemBinding.getRoot().setEnabled(false);
                            ((ViewHolder) holder).itemBinding.getRoot().setAlpha(0.5f);
                        }

                    } else if (((DrawerItem) obj).getName().equalsIgnoreCase(getString(R.string.nav_payment))) {
                        if (Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("1")) {
                            ((ViewHolder) holder).itemBinding.getRoot().setClickable(true);
                            ((ViewHolder) holder).itemBinding.getRoot().setEnabled(true);
                            ((ViewHolder) holder).itemBinding.getRoot().setAlpha(1.0f);
                        } else {
                            ((ViewHolder) holder).itemBinding.getRoot().setClickable(false);
                            ((ViewHolder) holder).itemBinding.getRoot().setEnabled(false);
                            ((ViewHolder) holder).itemBinding.getRoot().setAlpha(0.5f);
                        }
                    } else if (((DrawerItem) obj).getName().equalsIgnoreCase(getString(R.string.nav_inventory))) {
                        if (Settings.getString(App.IS_LOAD_VERIFY).equalsIgnoreCase("1") &&
                                Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("1")) {
                            ((ViewHolder) holder).itemBinding.getRoot().setClickable(false);
                            ((ViewHolder) holder).itemBinding.getRoot().setEnabled(false);
                            ((ViewHolder) holder).itemBinding.getRoot().setAlpha(0.5f);
                        } else {
                            //if (Settings.getString(App.IS_ATTENDANCE_IN).equalsIgnoreCase("1")) {
                            ((ViewHolder) holder).itemBinding.getRoot().setClickable(true);
                            ((ViewHolder) holder).itemBinding.getRoot().setEnabled(true);
                            ((ViewHolder) holder).itemBinding.getRoot().setAlpha(1.0f);
//                            } else {
//                                ((ViewHolder) holder).itemBinding.getRoot().setClickable(false);
//                                ((ViewHolder) holder).itemBinding.getRoot().setEnabled(false);
//                                ((ViewHolder) holder).itemBinding.getRoot().setAlpha(0.5f);
//                            }
                        }
                    }
                }

                ((ViewHolder) holder).itemBinding.ivSymbol.setBackgroundResource(((DrawerItem) obj).getImgRes());
                ((ViewHolder) holder).itemBinding.ivSymbol.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));

                ((ViewHolder) holder).itemBinding.executePendingBindings();
            }
        }

        @Override
        public int getItemCount() {
            return drawerItemArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return drawerItemArrayList.get(position) instanceof Profile ? 0 : position;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            RowDrawerListItemBinding itemBinding;

            ViewHolder(final RowDrawerListItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
                this.itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (itemBinding.getDrawerItem().getDrawerEnum()) {
//                            case Attendance:
//                                version();
//                                if (Settings.getString(App.IS_ATTENDANCE_IN).equalsIgnoreCase("0")) {
//                                    strAttendanceType = "In";
//                                    dispatchTakeChillerPictureIntent();
//                                } else {
//                                    strAttendanceType = "Out";
//                                    dispatchTakeChillerPictureIntent();
//                                }
//                                break;
                            case Home:
                                version();
                                startActivity(new Intent(me, DashboardFragment.class));
                                finish();
                                break;
                            case Manage_Load:
                                version();
                                startActivity(new Intent(me, ManageInventoryActivity.class));
                                finish();
                                break;
                            case Salesman_Load:
                                version();
                                startActivity(new Intent(me, SalesmanLoadActivity.class));
                                finish();
                                break;
                            case Load_Request:
                                version();
                                startActivity(new Intent(me, LoadRequestActivity.class));
                                finish();
                                break;
                            case Journey_Plan:
                                version();
                                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                                    startActivity(new Intent(me, FragmentJourneyPlan.class));
                                } else {
                                    startActivity(new Intent(me, JourneyPlanActivity.class));
                                }
                                finish();
                                break;
                            case Payment:
                                version();
                                startActivity(new Intent(me, PaymentActivity.class));
                                finish();
                                break;
                           /* case Forms:
                                version();
                                startActivity(new Intent(me, FormsActivity.class));
                                finish();
                                break;*/
                            case Print:
                                version();
                                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")
                                        || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {
                                    startActivity(new Intent(me, DataPostingActivity.class));
                                } else if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
                                    startActivity(new Intent(me, DataPostingActivity.class));
                                } else {
                                    startActivity(new Intent(me, MerchantDataPostingActivity.class));
                                }
                                finish();
                                break;
                            case TechDashboard:
                                version();
                                startActivity(new Intent(me, TechnicianDashboardFragment.class));
                                finish();
                                break;
                            case ChillerList:
                                version();
                                startActivity(new Intent(me, TechnicianChillerListFragment.class));
                                finish();
                                break;
                            case TechCustomer:
                                version();
                                startActivity(new Intent(me, TechnicianCustomerListFragment.class));
                                finish();
                                break;
                            case TechService:
                                version();
                                startActivity(new Intent(me, ServiceEquipmentActivity.class));
                                finish();
                                break;
                            case Dashboard:
                                version();
                                startActivity(new Intent(me, MerchantDashboardActivity.class));
                                finish();
                                break;
                            case Competitor_Information:
                                version();
                                startActivity(new Intent(me, CompetitorInformationActivity.class));
                                finish();
                                break;
                            case Sensor_Evaluation:
                                version();
                                startActivity(new Intent(me, SensoryListActivity.class));
                                finish();
                                break;
                            case Promotional_Accountablity:
                                version();
                                startActivity(new Intent(me, PromotionalListActivity.class));
                                finish();
                                break;
                            case Delivery:
                                version();
                                startActivity(new Intent(me, DeliveryListActivity.class));
                                finish();
                                break;
                            case Settings:
                                version();
                                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
                                    startActivity(new Intent(me, PendingBDActivity.class));
                                } else {
                                    startActivity(new Intent(me, SettingActivity.class));
                                }
                                finish();
                                break;
                            case Logout:

                                if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")
                                        || UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("School Team") ||
                                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Krystal Team") ||
                                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("MIT Team") ||
                                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Vendor Team") ||
                                        UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Managers Team")) {
                                    if (Settings.getString(App.IS_LOAD_VERIFY).equalsIgnoreCase("1") &&
                                            Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("0")) {
                                        UtilApp.displayAlert(BaseActivity.this, "Please reconcile before logout!");
                                    } else {
                                        if (db.isSyncCompleteTransaction()) {
                                            logOut();
                                        } else {
                                            // logOut();
                                            UtilApp.displayAlert(BaseActivity.this, "Please sync unload transaction first!");
                                        }
                                    }
                                } else if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Fridge Supervisor")) {
                                    if (db.getChillerTechnicianListcheck().size() == db.getChillerTechnicianList().size()) {
                                        if (db.isSyncCompleteTransaction()) {
                                            logOut();
                                        } else {
                                            // logOut();
                                            UtilApp.displayAlert(BaseActivity.this, "Please sync all transaction first!");
                                        }
                                    } else {
                                        UtilApp.displayAlert(BaseActivity.this, "Please sync the all the assign chiller.");
                                    }
                                } else {
                                    logOut();
                                }

                                break;

                        }
                        closeDrawer();
                    }
                });
            }
        }

        class ViewHolderHeader extends RecyclerView.ViewHolder {
            RowDrawerHeaderBinding itemBinding;

            ViewHolderHeader(final RowDrawerHeaderBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
                this.itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeDrawer();
                    }
                });
            }
        }


        class ViewHolderFooter extends RecyclerView.ViewHolder {
            RowDrawerFooterItemBinding itemBinding;

            ViewHolderFooter(final RowDrawerFooterItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();
                sb.append("App Ver: ");
                sb.append(pInfo.versionName);
                sb.append("\t \t");
                sb.append("Build: ");
                sb.append(App.ENVIRONMENT);
                this.itemBinding.txtVersion.setText(sb.toString());
                this.itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        version();
                        closeDrawer();
                    }
                });
            }
        }
    }

    public void version() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("App Ver: ");
        sb.append(pInfo.versionName);
        sb.append("\t \t");
        sb.append("Build: ");
        sb.append(App.ENVIRONMENT);

        baseBinding.txtVersion.setText(sb.toString());
    }

    public void logOut() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BaseActivity.this);
        alertDialogBuilder.setTitle(R.string.nav_logout)
                .setMessage(R.string.logout_msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        String loggId = Settings.getString(App.LOGGID);
                        getLogoutAPI(loggId);

                        //Clear all Preference
                        Settings.clearPreferenceStore();

                        File root = android.os.Environment.getExternalStorageDirectory();
                        File dir = new File(root.getAbsolutePath() + "/Riham/master");
                        if (dir.isDirectory()) {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++) {
                                new File(dir, children[i]).delete();
                            }
                        }

//                        //Delete Database
                        db.clearDatabase();
                       /* ArrayList<Transaction> arrData = new ArrayList<>();
                        arrData = new ArrayList<>();
                        arrData = db.getAllTransactions();
                        for (int i = 0; i < arrData.size(); i++) {
                            Transaction item = arrData.get(i);
                            if (item.tr_is_posted.equalsIgnoreCase("Yes")) {
                                db.clearInvoicePosted(arrData.get(i).getTr_invoice_id());
                                switch (item.tr_type) {
                                    case App.LoadConfirmation_TR:
                                        db.clearInvoicePosted(item.tr_invoice_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_SALES_CREATED:
                                        db.clearInvoicePosted(item.tr_invoice_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_RETURN_CREATED:
                                        db.clearInvoicePosted(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_OREDER_CREATED:
                                        db.clearInvoicePosted(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_CASH_COLLECTION:
                                        db.clearInvoicePosted(item.tr_collection_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_CREDIT_COLLECTION:
                                        db.clearInvoicePosted(item.tr_collection_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_LOAD_CREATE:
                                        db.clearInvoicePosted(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_UNLOAD:
                                        db.clearInvoicePosted(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_EXCHANGE_CREATED:
                                        db.clearInvoicePosted(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_CREATED:
                                        db.clearInvoicePosted(item.tr_order_id);
                                        break;
                                    case Constant.TRANSACTION_TYPES.TT_SENSURY_SURVEY:
                                        db.clearInvoicePosted(item.tr_order_id);
                                        break;
                                }
                            }
                        }*/
                        Intent in = new Intent(BaseActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

    }

    protected void setTitle(String title) {
        baseBinding.toolbarTitle.setText(title);
    }

    public void setFullScreen() {
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setFullScreenBack() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            baseBinding.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (baseBinding.mSearchView.isSearchOpen()) {
            baseBinding.mSearchView.closeSearch();
            return;
        }
        UtilApp.hideSoftKeyboard(me);
        super.onBackPressed();
    }

    private void getLogoutAPI(String loggId) {

        String time = UtilApp.getCurrent12Time();

        final Call<JsonObject> labelResponse = ApiClient.getService().getLogoutAPI(App.LOGOUT, time, App.Latitude, App.Longitude, loggId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Logout Response", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(BaseActivity.this, "Item Response: " + response.body().toString());
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Logout Fail", error.getMessage());
                UtilApp.logData(BaseActivity.this, "Material Failure: " + error.getMessage());
            }
        });
    }

    private void dispatchTakeChillerPictureIntent() {

        if (getFrontCameraId() == -1) {
            Toast.makeText(getApplicationContext(),
                    "Front Camera Not Detected", Toast.LENGTH_SHORT).show();
        } else {
            Intent cameraIntent = new Intent();
            cameraIntent.setClass(this, CameraActivity.class);
            startActivityForResult(cameraIntent, TAKE_PHOTO_CHILLER_REQUEST);

            // startActivity(new
            // Intent(MainActivity.this,CameraActivity.class));
        }

//        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            photoFile_chiller = createImageFile();
//            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_chiller));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                takePhotoIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
//            } else {
//                takePhotoIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//            }
//            // takePhotoIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//            startActivityForResult(takePhotoIntent, TAKE_PHOTO_CHILLER_REQUEST);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    int getFrontCameraId() {
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                return i;
        }
        return -1; // No front-facing camera found
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mFilePath = "Riham_" + timeStamp + ".png";
        File storageDir = Environment.getExternalStoragePublicDirectory("Riham");
        if (!storageDir.exists()) {

            if (!storageDir.mkdirs()) {


                return null;
            }
        }

        File image = new File(storageDir.getPath() + File.separator +
                mFilePath);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CHILLER_REQUEST && resultCode == RESULT_OK) {

            try {
                String filePat = data.getStringExtra("path");
                if (strAttendanceType.equals("In")) {
                    callAttendanceIn(filePat);
                } else {
                    callAttendanceOut(filePat);
                }

//                Bitmap bitmapFrontCam = (Bitmap) data
//                        .getParcelableExtra("BitmapImage");

            } catch (Exception e) {
            }

//            String filePat = UtilApp.compressImage(BaseActivity.this, mCurrentPhotoPath);
//            if (strAttendanceType.equals("In")) {
//                callAttendanceIn(filePat);
//            } else {
//                callAttendanceOut(filePat);
//            }
        } else if (requestCode == TAKE_PHOTO_CHILLER_REQUEST && resultCode == RESULT_CANCELED) {
            setNavigationView();
        }
    }

    private void callAttendanceIn(String filepath) {

        progressDialog.show();

        String time = UtilApp.formatTime(new Date(), "hh:mm a");
        String latitude = App.Latitude;
        String longitude = App.Longitude;
        String date = UtilApp.getCurrentDate();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.DAILY_ATTENDANCE);
        RequestBody salesmanId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody routeId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.ROUTEID));
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "in");
        RequestBody mDate = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody mTime = RequestBody.create(MediaType.parse("text/plain"), time);
        RequestBody mLatitude = RequestBody.create(MediaType.parse("text/plain"), latitude);
        RequestBody mLongitude = RequestBody.create(MediaType.parse("text/plain"), longitude);

        MultipartBody.Part mPart = null;

        if (!filepath.isEmpty()) {
            try {
                File file_bh = new File(filepath);
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_bh);
                mPart = MultipartBody.Part.createFormData("in_img", file_bh.getName(), reqFile);
            } catch (Exception e) {

            }
        }

        final Call<JsonObject> labelResponse = ApiClient.getService().postAttendanceIn(method, salesmanId, routeId,
                mTime, type, mLatitude, mLongitude, mDate, mPart);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("AttendanceIn Response:", response.toString());
                UtilApp.logData(BaseActivity.this, "AttendanceIn Response: " + response.toString());
                progressDialog.hide();
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                Log.e("Status", jsonObject.getString("STATUS"));
                                Settings.setString(App.IS_ATTENDANCE_IN, "1");
                                //UtilApp.displayAlert(BaseActivity.this, "Attendance In Successfully!");
                            } else {
                                Settings.setString(App.IS_ATTENDANCE_IN, "1");
                                // UtilApp.displayAlert(BaseActivity.this, "Attendance In Fail!");
                            }
                        }
                    } catch (Exception e) {
                    }
                } else {
                    Settings.setString(App.IS_ATTENDANCE_IN, "1");
                    //UtilApp.displayAlert(BaseActivity.this, "Attendance In fail!");
                }

                setNavigationView();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("AttendanceIn fail:", t.toString());
                setNavigationView();
                progressDialog.hide();
                Settings.setString(App.IS_ATTENDANCE_IN, "1");
                UtilApp.logData(BaseActivity.this, "AttendanceIn Fail: " + t.getMessage());
                // UtilApp.displayAlert(BaseActivity.this, "Attendance In Fail!");
            }
        });
    }

    private void callAttendanceOut(String filepath) {

        progressDialog.show();

        String time = UtilApp.formatTime(new Date(), "hh:mm a");
        String latitude = App.Latitude;
        String longitude = App.Longitude;
        String date = UtilApp.getCurrentDate();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.DAILY_ATTENDANCE);
        RequestBody salesmanId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody routeId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.ROUTEID));
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "out");
        RequestBody mDate = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody mTime = RequestBody.create(MediaType.parse("text/plain"), time);
        RequestBody mLatitude = RequestBody.create(MediaType.parse("text/plain"), latitude);
        RequestBody mLongitude = RequestBody.create(MediaType.parse("text/plain"), longitude);

        MultipartBody.Part mPart = null;

        if (!filepath.isEmpty()) {
            try {
                File file_bh = new File(filepath);
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_bh);
                mPart = MultipartBody.Part.createFormData("out_img", file_bh.getName(), reqFile);
            } catch (Exception e) {

            }
        }

        final Call<JsonObject> labelResponse = ApiClient.getService().postAttendanceOut(method, salesmanId, routeId,
                mTime, type, mLatitude, mLongitude, mDate, mPart);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("AttendanceOut Response:", response.toString());
                UtilApp.logData(BaseActivity.this, "AttendanceOut Response: " + response.toString());
                progressDialog.hide();
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                Log.e("Status", jsonObject.getString("STATUS"));
                                Settings.setString(App.IS_ATTENDANCE_OUT, "1");
                                // UtilApp.displayAlert(BaseActivity.this, "Attendance Out Successfully!");
                            } else {
                                Settings.setString(App.IS_ATTENDANCE_OUT, "1");
                                // UtilApp.displayAlert(BaseActivity.this, "Attendance Out Fail!");
                            }
                        }
                    } catch (Exception e) {
                    }
                } else {
                    Settings.setString(App.IS_ATTENDANCE_OUT, "1");
                    // UtilApp.displayAlert(BaseActivity.this, "Attendance Out fail!");
                }
                setNavigationView();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("AttendanceIn fail:", t.toString());
                progressDialog.hide();
                setNavigationView();
                Settings.setString(App.IS_ATTENDANCE_OUT, "1");
                UtilApp.logData(BaseActivity.this, "AttendanceOut Fail: " + t.getMessage());
                //UtilApp.displayAlert(BaseActivity.this, "Attendance Out Fail!");
            }
        });
    }
}
