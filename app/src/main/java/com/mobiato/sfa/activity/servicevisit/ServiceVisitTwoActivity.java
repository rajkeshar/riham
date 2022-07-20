package com.mobiato.sfa.activity.servicevisit;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityServiceVisitTwoBinding;
import com.mobiato.sfa.utils.UtilApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ServiceVisitTwoActivity extends BaseActivity implements View.OnClickListener {

    public ActivityServiceVisitTwoBinding binding;
    public String mType = "", model = "", serail = "", assets = "", outletName = "", ownerName = "",
            landmark = "", roadStreet = "", town = "", contact = "", mStartTime = "", ticketNo = "",
            branding = "", contactNumber2 = "", contactPerson = "", scanImage = "", natureOfCall = "", natureOfCallId = "",
            ctsStatus = "", district = "",ctcComment = "";
    private File photoFile;
    public static String nation_id = "", cleanless_photo = "", coil_photo = "", gasket_photo = "",
            branding_photo = "", light_photo = "", ventila_photo = "", leveling_photo = "";
    private static final int TAKE_PHOTO_REQUEST = 1;
    private String mCurrentPhotoPath, mFilePath;
    public static ArrayList<String> arrImage = new ArrayList<>();
    public static ArrayList<String> arrImagePath = new ArrayList<>();

    private static final int TAKE_PHOTO_COOLER_REQUEST = 10;
    private File photoCoollerFile;
    public static ArrayList<String> arrCoolerImage = new ArrayList<>();
    public static ArrayList<String> arrCoolerImagePath = new ArrayList<>();

    private File photoFile_cleanless;
    private static final int TAKE_PHOTO_CLEANLESS_REQUEST = 2;
    public static ArrayList<String> arrImage_cleanless = new ArrayList<>();
    public static ArrayList<String> arrImagePath_cleanless = new ArrayList<>();

    private File photoFile_coil;
    private static final int TAKE_PHOTO_COIL_REQUEST = 3;
    public static ArrayList<String> arrImage_coil = new ArrayList<>();
    public static ArrayList<String> arrImagePath_coil = new ArrayList<>();

    private File photoFile_gasket;
    private static final int TAKE_PHOTO_GASKET_REQUEST = 4;
    public static ArrayList<String> arrImage_gasket = new ArrayList<>();
    public static ArrayList<String> arrImagePath_gasket = new ArrayList<>();

    private File photoFile_light;
    private static final int TAKE_PHOTO_LIGHT_REQUEST = 5;
    public static ArrayList<String> arrImage_light = new ArrayList<>();
    public static ArrayList<String> arrImagePath_light = new ArrayList<>();

    private File photoFile_branding;
    private static final int TAKE_PHOTO_BRANDING_REQUEST = 6;
    public static ArrayList<String> arrImage_branding = new ArrayList<>();
    public static ArrayList<String> arrImagePath_branding = new ArrayList<>();


    private File photoFile_ventilation;
    private static final int TAKE_PHOTO_VENTILATION_REQUEST = 7;
    public static ArrayList<String> arrImage_ventilation = new ArrayList<>();
    public static ArrayList<String> arrImagePath_ventilation = new ArrayList<>();

    private File photoFile_leveling;
    private static final int TAKE_PHOTO_LEVELING_REQUEST = 8;
    public static ArrayList<String> arrImage_leveling = new ArrayList<>();
    public static ArrayList<String> arrImagePath_leveling = new ArrayList<>();

    private File photoFile_stock;
    private static final int TAKE_PHOTO_STOCK_REQUEST = 9;
    public static ArrayList<String> arrImage_stock = new ArrayList<>();
    public static ArrayList<String> arrImagePath_stock = new ArrayList<>();

    RadioButton radioButton_Condition;
    RadioButton radioCleannes;
    RadioButton radioCoil;
    RadioButton radioGasket;
    RadioButton radioLight;
    RadioButton radioBranding;
    RadioButton radioVentiltion;
    RadioButton radioLeveling;
    RadioButton radioStock;
    public static String stock_photo = "", conditionPhoto = "No", cleanlessPhoto = "No", coilPhoto = "No", gasketPhoto = "No", brandingPhoto = "",
            lightPhoto = "No", ventilaPhoto = "No", levelingPhoto = "No", stockPhoto = "No";
    public String[] arrPercent = {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
    private AlertDialog.Builder builder;
    public static boolean isPrevisouClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceVisitTwoBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Equipment Condition");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = getIntent();
        try {
            mType = intent.getStringExtra("type");
            mStartTime = intent.getStringExtra("startTime");
            ticketNo = intent.getStringExtra("ticketNo");
            model = intent.getStringExtra("model");
            serail = intent.getStringExtra("serial");
            assets = intent.getStringExtra("asset");
            outletName = intent.getStringExtra("outletName");
            ownerName = intent.getStringExtra("ownername");
            landmark = intent.getStringExtra("landmark");
            roadStreet = intent.getStringExtra("road_street");
            town = intent.getStringExtra("town");
            contact = intent.getStringExtra("number");
            branding = intent.getStringExtra("branding");
            contactNumber2 = intent.getStringExtra("contactNo2");
            contactPerson = intent.getStringExtra("contactPerson");
            scanImage = intent.getStringExtra("scanImage");
            natureOfCall = intent.getStringExtra("natureOfCall");
            natureOfCallId = intent.getStringExtra("natureOfCallId");
            ctsStatus = intent.getStringExtra("ctsStatus");
            district = intent.getStringExtra("district");
            ctcComment = intent.getStringExtra("ctsComment");
        } catch (Exception e) {
        }

        setData();

        binding.ticketNo.setText("Ticket No:   " + ticketNo);

        binding.chckCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    conditionPhoto = "Yes";
                    binding.layConditionImg.setVisibility(View.VISIBLE);
                } else {
                    conditionPhoto = "No";
                    binding.layConditionImg.setVisibility(View.GONE);
                }
            }
        });

        binding.chckCleanless.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cleanlessPhoto = "Yes";
                    binding.layCleanlinessImg.setVisibility(View.VISIBLE);
                } else {
                    cleanlessPhoto = "No";
                    binding.layCleanlinessImg.setVisibility(View.GONE);
                }
            }
        });

        binding.chckCoil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    coilPhoto = "Yes";
                    binding.layCoilImg.setVisibility(View.VISIBLE);
                } else {
                    coilPhoto = "No";
                    binding.layCoilImg.setVisibility(View.GONE);
                }
            }
        });

        binding.chckGasket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gasketPhoto = "Yes";
                    binding.layGasketsImg.setVisibility(View.VISIBLE);
                } else {
                    gasketPhoto = "No";
                    binding.layGasketsImg.setVisibility(View.GONE);
                }
            }
        });

        binding.chckLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lightPhoto = "Yes";
                    binding.layLightImg.setVisibility(View.VISIBLE);
                } else {
                    lightPhoto = "No";
                    binding.layLightImg.setVisibility(View.GONE);
                }
            }
        });


        binding.chckBrand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    brandingPhoto = "Yes";
                    binding.layBrandingImg.setVisibility(View.VISIBLE);
                } else {
                    brandingPhoto = "No";
                    binding.layBrandingImg.setVisibility(View.GONE);
                }
            }
        });

        binding.chckVentilation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ventilaPhoto = "Yes";
                    binding.layVentilationImg.setVisibility(View.VISIBLE);
                } else {
                    ventilaPhoto = "No";
                    binding.layVentilationImg.setVisibility(View.GONE);
                }
            }
        });


        binding.chckLeveling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    levelingPhoto = "Yes";
                    binding.layLevelingImg.setVisibility(View.VISIBLE);
                } else {
                    levelingPhoto = "No";
                    binding.layLevelingImg.setVisibility(View.GONE);
                }
            }
        });

        binding.chckStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stockPhoto = "Yes";
                    binding.layStockImg.setVisibility(View.VISIBLE);
                } else {
                    stockPhoto = "No";
                    binding.layStockImg.setVisibility(View.GONE);
                }
            }
        });

        binding.btnTransferNext.setOnClickListener(this);
        binding.btnTransferPre.setOnClickListener(this);
        binding.btnAddCooler.setOnClickListener(this);
        binding.btnAddConditionId.setOnClickListener(this);
        binding.btnAddCleanliness.setOnClickListener(this);
        binding.btnAddCoil.setOnClickListener(this);
        binding.btnAddgaskets.setOnClickListener(this);
        binding.btnAddBranding.setOnClickListener(this);
        binding.btnAddLight.setOnClickListener(this);
        binding.btnAddVentilation.setOnClickListener(this);
        binding.btnAddleveling.setOnClickListener(this);
        binding.btnAddStock.setOnClickListener(this);
        binding.etStcokPer.setOnClickListener(this);
    }

    private void setData() {

        if (isPrevisouClick) {
            isPrevisouClick = false;

            if (conditionPhoto.equals("Yes")) {
                binding.chckCondition.setChecked(true);
                binding.layConditionImg.setVisibility(View.VISIBLE);
            } else {
                binding.chckCondition.setChecked(false);
            }

            if (nation_id.equals("Yes")) {

                binding.radioNational.check(R.id.ra_yes_id);
            } else {
                binding.radioNational.check(R.id.ra_no_id);
            }

            if (cleanlessPhoto.equals("Yes")) {
                binding.layCleanlinessImg.setVisibility(View.VISIBLE);
                binding.chckCleanless.setChecked(true);
            } else {
                binding.chckCleanless.setChecked(false);
            }

            if (cleanless_photo.equals("Yes")) {
                binding.radioCleanliness.check(R.id.ra_yes_cleanliness);
            } else {
                binding.radioCleanliness.check(R.id.ra_no_cleanliness);
            }

            if (coilPhoto.equals("Yes")) {
                binding.chckCoil.setChecked(true);
                binding.layCoilImg.setVisibility(View.VISIBLE);
            } else {
                binding.chckCoil.setChecked(false);
            }

            if (coil_photo.equals("Yes")) {
                binding.radioCoil.check(R.id.ra_yes_Coil);
            } else {
                binding.radioCoil.check(R.id.ra_no_Coil);
            }

            if (brandingPhoto.equals("Yes")) {
                binding.chckBrand.setChecked(true);
                binding.layBrandingImg.setVisibility(View.VISIBLE);
            } else {
                binding.chckBrand.setChecked(false);
            }

            if (branding_photo.equals("Yes")) {
                binding.radioBranding.check(R.id.ra_yes_Branding);
            } else {
                binding.radioBranding.check(R.id.ra_no_Branding);
            }

            if (lightPhoto.equals("Yes")) {
                binding.chckLight.setChecked(true);
                binding.layLightImg.setVisibility(View.VISIBLE);
            } else {
                binding.chckLight.setChecked(false);
            }

            if (light_photo.equals("Yes")) {
                binding.radioLight.check(R.id.ra_yes_Light);
            } else {
                binding.radioLight.check(R.id.ra_no_Light);
            }

            if (gasketPhoto.equals("Yes")) {
                binding.chckGasket.setChecked(true);
                binding.layGasketsImg.setVisibility(View.VISIBLE);
            } else {
                binding.chckGasket.setChecked(false);
            }

            if (gasket_photo.equals("Yes")) {
                binding.radiogaskets.check(R.id.ra_yes_gaskets);
            } else {
                binding.radiogaskets.check(R.id.ra_no_gaskets);
            }

            if (ventilaPhoto.equals("Yes")) {
                binding.chckVentilation.setChecked(true);
                binding.layVentilationImg.setVisibility(View.VISIBLE);
            } else {
                binding.chckVentilation.setChecked(false);
            }

            if (ventila_photo.equals("Yes")) {
                binding.radioVentilation.check(R.id.ra_yes_Ventilation);
            } else {
                binding.radioVentilation.check(R.id.ra_no_Ventilation);
            }

            if (levelingPhoto.equals("Yes")) {
                binding.chckLeveling.setChecked(true);
                binding.layLevelingImg.setVisibility(View.VISIBLE);
            } else {
                binding.chckLeveling.setChecked(false);
            }

            if (leveling_photo.equals("Yes")) {
                binding.radioleveling.check(R.id.ra_yes_leveling);
            } else {
                binding.radioleveling.check(R.id.ra_no_leveling);
            }

            binding.etStcokPer.setText(stock_photo);

            if (stockPhoto.equals("Yes")) {
                binding.chckStock.setChecked(true);
                binding.layStockImg.setVisibility(View.VISIBLE);
            } else {
                binding.chckStock.setChecked(false);
            }


            if (arrImage.size() > 0) {
                binding.layoutImage.setVisibility(View.GONE);
                binding.layoutPager.setVisibility(View.VISIBLE);
                binding.btnAddConditionId.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage);
                binding.viewPager.setAdapter(viewPagerAdapter);
                binding.dotsIndicators.setViewPager(binding.viewPager);
            }

            if (arrCoolerImage.size() > 0) {
                binding.layoutCoolerBeforeImage.setVisibility(View.GONE);
                binding.layoutCoolerPager.setVisibility(View.VISIBLE);
                binding.btnAddCooler.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrCoolerImage);
                binding.viewCoolerPager.setAdapter(viewPagerAdapter);
                binding.dotsCoolerIndicators.setViewPager(binding.viewCoolerPager);
            }

            if (arrImage_cleanless.size() > 0) {
                binding.layoutImagecleanliness.setVisibility(View.GONE);
                binding.layoutCleanliness.setVisibility(View.VISIBLE);
                binding.btnAddCleanliness.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_cleanless);
                binding.viewPagerCleanliness.setAdapter(viewPagerAdapter);
                binding.dotsIndicatorsCleanliness.setViewPager(binding.viewPagerCleanliness);
            }

            if (arrImage_coil.size() > 0) {
                binding.layoutImageCoil.setVisibility(View.GONE);
                binding.layoutCoil.setVisibility(View.VISIBLE);
                binding.btnAddCoil.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_coil);
                binding.viewPagerCoil.setAdapter(viewPagerAdapter);
                binding.dotsIndicatorsCoil.setViewPager(binding.viewPagerCoil);
            }

            if (arrImage_gasket.size() > 0) {
                binding.layoutImagegaskets.setVisibility(View.GONE);
                binding.layoutGaskets.setVisibility(View.VISIBLE);
                binding.btnAddgaskets.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_gasket);
                binding.viewPagerGaskets.setAdapter(viewPagerAdapter);
                binding.dotsIndicatorsGaskets.setViewPager(binding.viewPagerGaskets);
            }

            if (arrImage_branding.size() > 0) {
                binding.layoutImageBranding.setVisibility(View.GONE);
                binding.layoutBranding.setVisibility(View.VISIBLE);
                binding.btnAddBranding.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_branding);
                binding.viewPagerBranding.setAdapter(viewPagerAdapter);
                binding.dotsIndicatorsBranding.setViewPager(binding.viewPagerBranding);
            }

            if (arrImage_light.size() > 0) {
                binding.layoutImageLight.setVisibility(View.GONE);
                binding.layoutLight.setVisibility(View.VISIBLE);
                binding.btnAddLight.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_light);
                binding.viewPagerLight.setAdapter(viewPagerAdapter);
                binding.dotsIndicatorsLight.setViewPager(binding.viewPagerLight);
            }

            if (arrImage_ventilation.size() > 0) {
                binding.layoutImageVentilation.setVisibility(View.GONE);
                binding.layoutVentilation.setVisibility(View.VISIBLE);
                binding.btnAddVentilation.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_ventilation);
                binding.viewPagerVentilation.setAdapter(viewPagerAdapter);
                binding.dotsIndicatorsVentilation.setViewPager(binding.viewPagerVentilation);
            }

            if (arrImage_leveling.size() > 0) {
                binding.layoutImageleveling.setVisibility(View.GONE);
                binding.layoutLeveling.setVisibility(View.VISIBLE);
                binding.btnAddleveling.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_leveling);
                binding.viewPagerLeveling.setAdapter(viewPagerAdapter);
                binding.dotsIndicatorsLeveling.setViewPager(binding.viewPagerLeveling);
            }

            if (arrImage_stock.size() > 0) {
                binding.layoutImageStock.setVisibility(View.GONE);
                binding.layoutStock.setVisibility(View.VISIBLE);
                binding.btnAddStock.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_stock);
                binding.viewPagerStock.setAdapter(viewPagerAdapter);
                binding.dotsIndicatorsStock.setViewPager(binding.viewPagerStock);
            }
        }
    }

    public static void clearData() {
        cleanless_photo = "";
        nation_id = "";
        coil_photo = "";
        gasket_photo = "";
        branding_photo = "";
        light_photo = "";
        ventila_photo = "";
        leveling_photo = "";
        stockPhoto = "";
        stock_photo = "";

        arrCoolerImage.clear();
        arrCoolerImagePath.clear();

        arrImage.clear();
        arrImagePath.clear();

        arrImage_cleanless.clear();
        arrImagePath_cleanless.clear();

        arrImage_coil.clear();
        arrImagePath_coil.clear();

        arrImage_gasket.clear();
        arrImagePath_gasket.clear();

        arrImage_branding.clear();
        arrImagePath_branding.clear();

        arrImage_light.clear();
        arrImagePath_light.clear();

        arrImage_ventilation.clear();
        arrImagePath_ventilation.clear();

        arrImage_leveling.clear();
        arrImagePath_leveling.clear();

        arrImage_stock.clear();
        arrImagePath_stock.clear();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_stcokPer:
                builder = new AlertDialog.Builder(ServiceVisitTwoActivity.this);
                builder.setTitle("Select Percentage");
                builder.setItems(arrPercent, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etStcokPer.setText(arrPercent[which] + "%");
                        stock_photo = arrPercent[which];
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.btnTransferNext:

                int selectedID = binding.radioNational.getCheckedRadioButtonId();
                radioButton_Condition = findViewById(selectedID);
                if (radioButton_Condition != null) {
                    nation_id = radioButton_Condition.getText().toString();
                }

                int selectedID_radioPassport = binding.radioCleanliness.getCheckedRadioButtonId();
                radioCleannes = findViewById(selectedID_radioPassport);
                if (radioCleannes != null) {
                    cleanless_photo = radioCleannes.getText().toString();
                }

                //proof
                int selectedID_address = binding.radioCoil.getCheckedRadioButtonId();
                radioCoil = findViewById(selectedID_address);
                if (radioCoil != null) {
                    coil_photo = radioCoil.getText().toString();
                }

                int selectedID_gasket = binding.radiogaskets.getCheckedRadioButtonId();
                radioGasket = findViewById(selectedID_gasket);
                if (radioGasket != null) {
                    gasket_photo = radioGasket.getText().toString();
                }

                //Stemp
                int selectedID_stemp = binding.radioBranding.getCheckedRadioButtonId();
                radioBranding = findViewById(selectedID_stemp);
                if (radioBranding != null) {
                    branding_photo = radioBranding.getText().toString();
                }

                //LC Letter
                int selectedID_lc = binding.radioLight.getCheckedRadioButtonId();
                radioLight = findViewById(selectedID_lc);
                if (radioLight != null) {
                    light_photo = radioLight.getText().toString();
                }

                int selectedID_license = binding.radioVentilation.getCheckedRadioButtonId();
                radioVentiltion = findViewById(selectedID_license);
                if (radioVentiltion != null) {
                    ventila_photo = radioVentiltion.getText().toString();
                }

                int selectedID_leveling = binding.radioleveling.getCheckedRadioButtonId();
                radioLeveling = findViewById(selectedID_leveling);
                if (radioLeveling != null) {
                    leveling_photo = radioLeveling.getText().toString();
                }

                if (arrImage.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload working condition photo");
                } else if (arrCoolerImage.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload cooler condition before cleaning photo");
                } else if (arrImage_cleanless.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload Cleanliness photo");
                } else if (arrImage_coil.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload condensor Coil cleand photo");
                } else if (arrImage_stock.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload stock Availability in% photo");
                }else if (nation_id.isEmpty()) {
                    UtilApp.displayAlert(me, "Please select Working condition");
                }else if (cleanless_photo.isEmpty()) {
                    UtilApp.displayAlert(me, "Please select cleanliness");
                }else if (coil_photo.isEmpty()) {
                    UtilApp.displayAlert(me, "Please select condensor coil cleand");
                } else if (gasketPhoto.equals("Yes") && arrImage_gasket.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload Gasket photo");
                } else if (brandingPhoto.equals("Yes") && arrImage_branding.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload Branding photo");
                } else if (lightPhoto.equals("Yes") && arrImage_light.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload Light Working photo");
                } else if (ventilaPhoto.equals("Yes") && arrImage_ventilation.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload Propper Ventilation available photo");
                } else if (levelingPhoto.equals("Yes") && arrImage_leveling.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload leveling/positioning photo");
                } else if (stock_photo.isEmpty()) {
                    UtilApp.displayAlert(me, "Please Select Stock Availability Percentage");
                } else if (stockPhoto.equals("Yes") && arrImage_stock.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload stock image");
                } else {

                    Intent intent = new Intent(ServiceVisitTwoActivity.this, ServiceVisitThreeActivity.class);
                    intent.putExtra("type", mType);
                    intent.putExtra("startTime", mStartTime);
                    intent.putExtra("ticketNo", ticketNo);
                    intent.putExtra("outletName", outletName);
                    intent.putExtra("ownername", ownerName);
                    intent.putExtra("town", town);
                    intent.putExtra("landmark", landmark);
                    intent.putExtra("road_street", roadStreet);
                    intent.putExtra("number", contact);
                    intent.putExtra("asset", assets);
                    intent.putExtra("serial", serail);
                    intent.putExtra("model", model);
                    intent.putExtra("branding", branding);
                    intent.putExtra("contactNo2", contactNumber2);
                    intent.putExtra("contactPerson", contactPerson);
                    intent.putExtra("scanImage", scanImage);
                    intent.putExtra("natureOfCall", natureOfCall);
                    intent.putExtra("natureOfCallId", natureOfCallId);
                    intent.putExtra("ctsStatus", ctsStatus);
                    intent.putExtra("district", district);
                    intent.putExtra("ctsComment", ctcComment);

                    if (arrCoolerImagePath.size() > 0) {
                        intent.putExtra("coolerImage1", arrCoolerImagePath.get(0));
                    } else {
                        intent.putExtra("coolerImage1", "");
                    }

                    if (arrCoolerImagePath.size() > 1) {
                        intent.putExtra("coolerImage2", arrCoolerImagePath.get(1));
                    } else {
                        intent.putExtra("coolerImage2", "");
                    }

                    intent.putExtra("conditionId", nation_id);
                    intent.putExtra("conditionImage", arrImagePath.get(0));

                    intent.putExtra("cleanlessd", cleanless_photo);
                    intent.putExtra("cleanlessImage", arrImagePath_cleanless.get(0));

                    intent.putExtra("coilId", coil_photo);
                    intent.putExtra("coilImage", arrImagePath_coil.get(0));

                    intent.putExtra("stockId", stock_photo);
                    intent.putExtra("stockImage", arrImagePath_stock.get(0));

                    intent.putExtra("gasketId", gasket_photo);
                    if (arrImagePath_gasket.isEmpty()) {
                        intent.putExtra("gasketImage", "");
                    } else {
                        intent.putExtra("gasketImage", arrImagePath_gasket.get(0));
                    }

                    intent.putExtra("brandId", branding_photo);
                    if (arrImagePath_branding.isEmpty()) {
                        intent.putExtra("brandImage", "");
                    } else {
                        intent.putExtra("brandImage", arrImagePath_branding.get(0));
                    }

                    intent.putExtra("lightId", light_photo);
                    if (arrImagePath_light.isEmpty()) {
                        intent.putExtra("lightImage", "");
                    } else {
                        intent.putExtra("lightImage", arrImagePath_light.get(0));
                    }


                    intent.putExtra("ventId", ventila_photo);
                    if (arrImagePath_ventilation.isEmpty()) {
                        intent.putExtra("ventImage", "");
                    } else {
                        intent.putExtra("ventImage", arrImagePath_ventilation.get(0));
                    }

                    intent.putExtra("levelingId", leveling_photo);
                    if (arrImagePath_leveling.isEmpty()) {
                        intent.putExtra("levelingImage", "");
                    } else {
                        intent.putExtra("levelingImage", arrImagePath_leveling.get(0));
                    }
                    startActivity(intent);
                }

                break;
            case R.id.btnTransferPre:
                isPrevisouClick = true;

                int selectedPID = binding.radioNational.getCheckedRadioButtonId();
                radioButton_Condition = findViewById(selectedPID);
                if (radioButton_Condition != null) {
                    nation_id = radioButton_Condition.getText().toString();
                }

                int selectedPID_radioPassport = binding.radioCleanliness.getCheckedRadioButtonId();
                radioCleannes = findViewById(selectedPID_radioPassport);
                if (radioCleannes != null) {
                    cleanless_photo = radioCleannes.getText().toString();
                }

                //proof
                int selectedPID_address = binding.radioCoil.getCheckedRadioButtonId();
                radioCoil = findViewById(selectedPID_address);
                if (radioCoil != null) {
                    coil_photo = radioCoil.getText().toString();
                }

                int selectedPID_gasket = binding.radiogaskets.getCheckedRadioButtonId();
                radioGasket = findViewById(selectedPID_gasket);
                if (radioGasket != null) {
                    gasket_photo = radioGasket.getText().toString();
                }

                //Stemp
                int selectedPID_stemp = binding.radioBranding.getCheckedRadioButtonId();
                radioBranding = findViewById(selectedPID_stemp);
                if (radioBranding != null) {
                    branding_photo = radioBranding.getText().toString();
                }

                //LC Letter
                int selectedPID_lc = binding.radioLight.getCheckedRadioButtonId();
                radioLight = findViewById(selectedPID_lc);
                if (radioLight != null) {
                    light_photo = radioLight.getText().toString();
                }

                int selectedPID_license = binding.radioVentilation.getCheckedRadioButtonId();
                radioVentiltion = findViewById(selectedPID_license);
                if (radioVentiltion != null) {
                    ventila_photo = radioVentiltion.getText().toString();
                }

                int selectedPID_leveling = binding.radioleveling.getCheckedRadioButtonId();
                radioLeveling = findViewById(selectedPID_leveling);
                if (radioLeveling != null) {
                    leveling_photo = radioLeveling.getText().toString();
                }

                finish();
                break;
            case R.id.btnAddCooler:
                if (arrImage.size() < 2) {
                    dispatchTakeCoolerPictureIntent();
                }
                break;
            case R.id.btnAddConditionId:
                if (arrImage.size() < 1) {
                    dispatchTakePictureIntent();
                }
                break;
            case R.id.btnAddCoil:
                if (arrImage_coil.size() < 1) {
                    dispatchCoilTakePictureIntent();
                }
                break;
            case R.id.btnAddCleanliness:
                if (arrImage_cleanless.size() < 1) {
                    dispatchCleanlessTakePictureIntent();
                }
                break;
            case R.id.btnAddgaskets:
                if (arrImage_gasket.size() < 1) {
                    dispatchGasketTakePictureIntent();
                }
                break;
            case R.id.btnAddLight:
                if (arrImage_light.size() < 1) {
                    dispatchLightTakePictureIntent();
                }
                break;
            case R.id.btnAddBranding:
                if (arrImage_branding.size() < 1) {
                    dispatchBrandingTakePictureIntent();
                }
                break;
            case R.id.btnAddVentilation:
                if (arrImage_ventilation.size() < 1) {
                    dispatchVentilationTakePictureIntent();
                }
                break;
            case R.id.btnAddleveling:
                if (arrImage_leveling.size() < 1) {
                    dispatchLevelingTakePictureIntent();
                }
                break;
            case R.id.btnAddStock:
                if (arrImage_stock.size() < 1) {
                    dispatchStockTakePictureIntent();
                }
                break;
        }
    }

    private void dispatchTakeCoolerPictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoCoollerFile = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoCoollerFile));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_COOLER_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchCleanlessTakePictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_cleanless = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_cleanless));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_CLEANLESS_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchCoilTakePictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_coil = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_coil));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_COIL_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchGasketTakePictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_gasket = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_gasket));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_GASKET_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchBrandingTakePictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_branding = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_branding));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_BRANDING_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchLightTakePictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_light = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_light));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_LIGHT_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchVentilationTakePictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_ventilation = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_ventilation));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_VENTILATION_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchLevelingTakePictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_leveling = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_leveling));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_LEVELING_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchStockTakePictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_stock = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_stock));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_STOCK_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitTwoActivity.this, mCurrentPhotoPath);
            binding.layoutImage.setVisibility(View.GONE);
            binding.layoutPager.setVisibility(View.VISIBLE);
            binding.btnAddConditionId.setVisibility(View.VISIBLE);
            arrImage.add(filePat);
            arrImagePath.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage);
            binding.viewPager.setAdapter(viewPagerAdapter);
            binding.dotsIndicators.setViewPager(binding.viewPager);
        } else if (requestCode == TAKE_PHOTO_COOLER_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitTwoActivity.this, mCurrentPhotoPath);
            binding.layoutCoolerBeforeImage.setVisibility(View.GONE);
            binding.layoutCoolerPager.setVisibility(View.VISIBLE);
            binding.btnAddCooler.setVisibility(View.VISIBLE);
            arrCoolerImage.add(filePat);
            arrCoolerImagePath.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrCoolerImage);
            binding.viewCoolerPager.setAdapter(viewPagerAdapter);
            binding.dotsCoolerIndicators.setViewPager(binding.viewCoolerPager);
        } else if (requestCode == TAKE_PHOTO_CLEANLESS_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitTwoActivity.this, mCurrentPhotoPath);
            binding.layoutImagecleanliness.setVisibility(View.GONE);
            binding.layoutCleanliness.setVisibility(View.VISIBLE);
            binding.btnAddCleanliness.setVisibility(View.VISIBLE);
            arrImage_cleanless.add(filePat);
            arrImagePath_cleanless.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_cleanless);
            binding.viewPagerCleanliness.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsCleanliness.setViewPager(binding.viewPagerCleanliness);
        } else if (requestCode == TAKE_PHOTO_COIL_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitTwoActivity.this, mCurrentPhotoPath);
            binding.layoutImageCoil.setVisibility(View.GONE);
            binding.layoutCoil.setVisibility(View.VISIBLE);
            binding.btnAddCoil.setVisibility(View.VISIBLE);
            arrImage_coil.add(filePat);
            arrImagePath_coil.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_coil);
            binding.viewPagerCoil.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsCoil.setViewPager(binding.viewPagerCoil);
        } else if (requestCode == TAKE_PHOTO_LIGHT_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitTwoActivity.this, mCurrentPhotoPath);
            binding.layoutImageLight.setVisibility(View.GONE);
            binding.layoutLight.setVisibility(View.VISIBLE);
            binding.btnAddLight.setVisibility(View.VISIBLE);
            arrImage_light.add(filePat);
            arrImagePath_light.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_light);
            binding.viewPagerLight.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsLight.setViewPager(binding.viewPagerLight);
        } else if (requestCode == TAKE_PHOTO_GASKET_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitTwoActivity.this, mCurrentPhotoPath);
            binding.layoutImagegaskets.setVisibility(View.GONE);
            binding.layoutGaskets.setVisibility(View.VISIBLE);
            binding.btnAddgaskets.setVisibility(View.VISIBLE);
            arrImage_gasket.add(filePat);
            arrImagePath_gasket.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_gasket);
            binding.viewPagerGaskets.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsGaskets.setViewPager(binding.viewPagerGaskets);
        } else if (requestCode == TAKE_PHOTO_BRANDING_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitTwoActivity.this, mCurrentPhotoPath);
            binding.layoutImageBranding.setVisibility(View.GONE);
            binding.layoutBranding.setVisibility(View.VISIBLE);
            binding.btnAddBranding.setVisibility(View.VISIBLE);
            arrImage_branding.add(filePat);
            arrImagePath_branding.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_branding);
            binding.viewPagerBranding.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsBranding.setViewPager(binding.viewPagerBranding);
        } else if (requestCode == TAKE_PHOTO_VENTILATION_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitTwoActivity.this, mCurrentPhotoPath);
            binding.layoutImageVentilation.setVisibility(View.GONE);
            binding.layoutVentilation.setVisibility(View.VISIBLE);
            binding.btnAddVentilation.setVisibility(View.VISIBLE);
            arrImage_ventilation.add(filePat);
            arrImagePath_ventilation.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_ventilation);
            binding.viewPagerVentilation.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsVentilation.setViewPager(binding.viewPagerVentilation);
        } else if (requestCode == TAKE_PHOTO_LEVELING_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitTwoActivity.this, mCurrentPhotoPath);
            binding.layoutImageleveling.setVisibility(View.GONE);
            binding.layoutLeveling.setVisibility(View.VISIBLE);
            binding.btnAddleveling.setVisibility(View.VISIBLE);
            arrImage_leveling.add(filePat);
            arrImagePath_leveling.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_leveling);
            binding.viewPagerLeveling.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsLeveling.setViewPager(binding.viewPagerLeveling);
        } else if (requestCode == TAKE_PHOTO_STOCK_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitTwoActivity.this, mCurrentPhotoPath);
            binding.layoutImageStock.setVisibility(View.GONE);
            binding.layoutStock.setVisibility(View.VISIBLE);
            binding.btnAddStock.setVisibility(View.VISIBLE);
            arrImage_stock.add(filePat);
            arrImagePath_stock.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_stock);
            binding.viewPagerStock.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsStock.setViewPager(binding.viewPagerStock);
        }
    }
}