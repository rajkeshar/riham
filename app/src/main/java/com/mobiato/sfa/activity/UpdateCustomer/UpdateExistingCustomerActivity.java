package com.mobiato.sfa.activity.UpdateCustomer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.Techinician.fragment.CustomerAgreementActivity;
import com.mobiato.sfa.activity.AddNewCustomerActivity;
import com.mobiato.sfa.activity.CustomerDetailActivity;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.databinding.ActivityAddNewCustomerBinding;
import com.mobiato.sfa.databinding.ActivityUpdateNewCustomerBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.rest.SyncData;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Query;

public class UpdateExistingCustomerActivity extends BaseActivity implements View.OnClickListener {

    public ActivityUpdateNewCustomerBinding binding;
    public String[] arrLanguage = {"English", "Swahili", "Luganda", "Nyoro", "Tooro", "Lungole", "Longo", "Runyankole"};
    private String strFridge = "", custNum = "", strType = "", strCustTypeId = "", strCustCategoryId = "", strCType = "",
            strCustSubCategoryId = "", strCustChannelId = "", sunday = "0", monday = "0", tues = "0", wed = "0", thur = "0",
            frid = "0", Sat = "0";
    private Location mCurrentLC;
    private Salesman mSalesman;
    private DBManager db;
    private Customer mCustomer;
    private String[] arrCusType, arrCusTypeId, arrCusCategory, arrCusCatId, arrChannel, arrChannelId, arrSubCategory, arrSubcategoryId;
    private ArrayList<String> arrFridge = new ArrayList<>();

    private AlertDialog.Builder builder;
    String str_fridge_code, str_available_fridge;
    public String[] arrFridgeType = {"Single Door", "Double Door", "Slider"};
    RadioButton radioButton;
    String is_fredge;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateNewCustomerBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Customer Update");

        strType = getIntent().getStringExtra("Type");

        mCustomer = (Customer) getIntent().getSerializableExtra("customer");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, locationListenerGPS);


        binding.chkPepsi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrFridge.add("Pepsi");
                    binding.cardPepsiType.setVisibility(View.VISIBLE);
                } else {
                    arrFridge.remove("Pepsi");
                    binding.cardPepsiType.setVisibility(View.GONE);
                }
            }
        });

        binding.chkCoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrFridge.add("Coca-Cola");
                    binding.cardCocType.setVisibility(View.VISIBLE);
                } else {
                    arrFridge.remove("Coca-Cola");
                    binding.cardCocType.setVisibility(View.GONE);
                }
            }
        });

        binding.chkRiham.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrFridge.add("Riham");
                    binding.cardRihamType.setVisibility(View.VISIBLE);
                } else {
                    arrFridge.remove("Riham");
                    binding.cardRihamType.setVisibility(View.GONE);
                }
            }
        });

        binding.chkPersonal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrFridge.add("Personal");
                    binding.cardPersonalType.setVisibility(View.VISIBLE);
                } else {
                    arrFridge.remove("Personal");
                    binding.cardPersonalType.setVisibility(View.GONE);
                }
            }
        });

        binding.chkNoFridge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrFridge.add("Not Available");
                } else {
                    arrFridge.remove("Not Available");
                }
            }
        });

        /*try {
            str_fridge_code = Settings.getString(App.SCANSERIALNUMBER);
            if(!mCustomer.getIs_fridge_assign().equals("1")){
                str_available_fridge = "0";
                binding.scanQrCodeCard.setVisibility(View.GONE);
                binding.layFridgeserial.setVisibility(View.GONE);
                binding.raYesId.setChecked(false);
                binding.raNoId.setChecked(true);
            }else {
                str_available_fridge = "1";
                binding.scanQrCodeCard.setVisibility(View.VISIBLE);
                binding.layFridgeserial.setVisibility(View.VISIBLE);
                binding.raYesId.setChecked(true);
                binding.raNoId.setChecked(false);
            }
          //  str_fridge_code = getIntent().getStringExtra("code");
            binding.etFridgeSerial.setText(str_fridge_code);
        } catch (Exception e) {
            e.toString();
        }*/

        try {

        } catch (Exception e) {

        }

        try {
            str_available_fridge = mCustomer.getIs_fridge_assign();
            // str_available_fridge = getIntent().getStringExtra("available");
        } catch (Exception e) {
            e.toString();
        }

        binding.raYesId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layFridgeserial.setVisibility(View.VISIBLE);
                binding.scanQrCodeCard.setVisibility(View.VISIBLE);
            }
        });
        binding.raNoId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.scanQrCodeCard.setVisibility(View.GONE);
                binding.layFridgeserial.setVisibility(View.GONE);
            }
        });

        db = new DBManager(UpdateExistingCustomerActivity.this);
        mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));

        custNum = mCustomer.getCustomerCode();
        binding.cardPersonalType.setOnClickListener(this);
        binding.cardCocType.setOnClickListener(this);
        binding.cardPepsiType.setOnClickListener(this);
        binding.cardRihamType.setOnClickListener(this);

        binding.edtCustomerId.setText(custNum);
        binding.titleOutletName.setText(getBuilder(getResources().getString(R.string.outlet_name)));
        binding.titleAddress1.setText(getBuilder("Road/Street Name"));
        binding.titleAddress2.setText(getBuilder("Landmark"));
        binding.titlestrret.setText(getBuilder("Town/Village"));
        binding.titleCity.setText(getBuilder("District"));
        binding.titleCustOwnerupdate.setText(getBuilder("Owner Name"));
        binding.titlePhone.setText(getBuilder(getResources().getString(R.string.telephone)));
        //binding.titleTin.setText(getBuilder("TIN No"));
        binding.titleLanguageupdate.setText(getBuilder("Language"));
        binding.titleCategory.setText(getBuilder("Customer Category"));


        binding.edtCustomerId.setText(mCustomer.getCustomerCode());
        binding.edtCustomerLat.setText(mCustomer.getLatitude());
        binding.edtCustomerlong.setText(mCustomer.getLongitude());
        binding.edtCustomerroute.setText(mCustomer.getRouteId());
        binding.etOwnerName.setText(mCustomer.getCustomerName());
        binding.etCustomerAddress1.setText(mCustomer.getAddress());
        binding.etCustomerAddress2.setText(mCustomer.getAddress2());
        binding.etCustomerTelephoneOtherupdate.setText(mCustomer.getCustomerphone1());
        binding.etStreet.setText(mCustomer.getCustomerstate());
        binding.etCity.setText(mCustomer.getCustomercity());
        binding.etZip.setText(mCustomer.getCustomerzip());
        binding.etCustomerTelephone.setText(mCustomer.getCustPhone());
        binding.etCategory.setText(mCustomer.getCustomerCategory());
        binding.etFridgeSerial.setText(mCustomer.getFridger_code());
        binding.etCustownerUpdatename.setText(mCustomer.getCustomerName2());
        binding.etCrTin.setText(mCustomer.getTinNo());
        binding.etCustType.setText(mCustomer.getCustomerType());
        binding.etCategory.setText(mCustomer.getCustomerCategory());
        binding.etLanguageupdate.setText(mCustomer.getLanguage());

        arrCusCategory = db.getCustomerCategoryList();
        arrCusCatId = db.getCustomerCategoryId();
        arrChannel = db.getCustomerChannelList();
        arrChannelId = db.getCustomerChannelId();
        arrSubCategory = db.getCustomerSubCategoryList();
        arrSubcategoryId = db.getCustomerSubCategoryId();

        for (int i = 0; i < arrCusCatId.length; i++) {
            if (arrCusCatId[i].equals(mCustomer.getCustomerCategory())) {
                binding.etCategory.setText(arrCusCategory[i]);
            }
        }

        for (int i = 0; i < arrChannelId.length; i++) {
            if (arrChannelId[i].equals(mCustomer.getCustomerchannel())) {
                binding.etCustChannel.setText(arrChannelId[i]);
            }
        }

        for (int i = 0; i < arrSubcategoryId.length; i++) {
            if (arrSubcategoryId[i].equals(mCustomer.getCustomerSubCategory())) {
                binding.etCustSubCategory.setText(arrSubCategory[i]);
            }
        }

        binding.etScanQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(me, SimpleUpdateScannerActivity.class).putExtra("customer", mCustomer).putExtra("available", "No"));
            }
        });

        binding.chkSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sunday = "1";
                } else {
                    sunday = "0";
                }
            }
        });

        binding.chkMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    monday = "1";
                } else {
                    monday = "0";
                }
            }
        });

        binding.chkTues.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tues = "1";
                } else {
                    tues = "0";
                }
            }
        });

        binding.chkWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wed = "1";
                } else {
                    wed = "0";
                }
            }
        });

        binding.chkThur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    thur = "1";
                } else {
                    thur = "0";
                }
            }
        });

        binding.chkFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    frid = "1";
                } else {
                    frid = "0";
                }
            }
        });

        binding.chkSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Sat = "1";
                } else {
                    Sat = "0";
                }
            }
        });

        setVisitData();

        binding.btnAdd.setOnClickListener(this);
        binding.etLanguageupdate.setOnClickListener(this);

        binding.etCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(UpdateExistingCustomerActivity.this);
                builder.setTitle("Select Customer Category");
                builder.setItems(arrCusCategory, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etCategory.setText(arrCusCategory[which]);
                        strCustCategoryId = arrCusCatId[which];
                    }
                });

                // create and show the alert dialog
                AlertDialog dialogCat = builder.create();
                dialogCat.show();
            }
        });

        binding.etCustChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(UpdateExistingCustomerActivity.this);
                builder.setTitle("Select Customer Channel");
                builder.setItems(arrChannel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etCustChannel.setText(arrChannel[which]);
                        strCustChannelId = arrChannelId[which];

                        strCustCategoryId = "";
                        binding.etCategory.setText("");
                        arrCusCategory = new String[0];
                        arrCusCategory = db.getChannelCategoryList(strCustChannelId);
                        arrCusCatId = new String[0];
                        arrCusCatId = db.getChannelCategoryId(strCustChannelId);

                    }
                });

                // create and show the alert dialog
                AlertDialog dialogChan = builder.create();
                dialogChan.show();
            }
        });

        binding.etCustSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(UpdateExistingCustomerActivity.this);
                builder.setTitle("Select Customer Sub Category");
                builder.setItems(arrSubCategory, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etCustSubCategory.setText(arrSubCategory[which]);
                        strCustSubCategoryId = arrSubcategoryId[which];
                    }
                });

                // create and show the alert dialog
                AlertDialog dialogSubCat = builder.create();
                dialogSubCat.show();
            }
        });
    }

    public void setVisitData() {
        if (mCustomer.getMonSqu().equals("1")) {
            monday = "1";
            binding.chkMonday.setChecked(true);
        }

        if (mCustomer.getThuSqu().equals("1")) {
            tues = "1";
            binding.chkTues.setChecked(true);
        }

        if (mCustomer.getWedSqu().equals("1")) {
            wed = "1";
            binding.chkWed.setChecked(true);
        }

        if (mCustomer.getThuSqu().equals("1")) {
            thur = "1";
            binding.chkThur.setChecked(true);
        }

        if (mCustomer.getFriSqu().equals("1")) {
            frid = "1";
            binding.chkFriday.setChecked(true);
        }

        if (mCustomer.getSatSqu().equals("1")) {
            Sat = "1";
            binding.chkSat.setChecked(true);
        }

        if (mCustomer.getSunSqu().equals("1")) {
            sunday = "1";
            binding.chkSun.setChecked(true);
        }
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            App.Latitude = String.valueOf(latitude);
            App.Longitude = String.valueOf(longitude);
            String msg = "New Latitude: " + App.Latitude + "New Longitude: " + App.Longitude;
            mCurrentLC = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    public SpannableStringBuilder getBuilder(String actualText) {
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(actualText);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }


    private boolean checkNullValues() {
        boolean returnvalue = false;
        try {

            if (binding.etOwnerName.getText().toString().matches("")) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please enter Customer name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustownerUpdatename.getText().toString().matches("")) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please enter Owner name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerAddress1.getText().toString().matches("")) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please enter Road/Street Name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerAddress2.getText().toString().matches("")) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please enter Landmark", Toast.LENGTH_SHORT).show();
            } else if (binding.etStreet.getText().toString().matches("")) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please enter Town/Village", Toast.LENGTH_SHORT).show();
            } else if (binding.etCity.getText().toString().matches("")) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please enter District", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().matches("")) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please enter Telephone", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().length() < 10) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            }/* else if (!binding.etCrTin.getText().toString().equalsIgnoreCase("") && binding.etCrTin.getText().length() < 10) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please enter 10 digit TIN Number", Toast.LENGTH_SHORT).show();
            }*/ else if (binding.etLanguageupdate.getText().toString().matches("")) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please enter Language", Toast.LENGTH_SHORT).show();
            } else if (binding.etCategory.getText().toString().matches("")) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please select Category", Toast.LENGTH_SHORT).show();
            } else if (arrFridge.size() == 0) {
                Toast.makeText(UpdateExistingCustomerActivity.this, "Please select Fridge", Toast.LENGTH_SHORT).show();
            } else {
                returnvalue = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnvalue;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.scan_qr_code_card:
                startActivity(new Intent(me, SimpleUpdateScannerActivity.class).putExtra("customer", mCustomer).putExtra("available","No"));
                break;*/
            case R.id.btnAdd:
                if (checkNullValues()) {
                    // addCustomer();
                    int selectedID = binding.radioGender.getCheckedRadioButtonId();
                    radioButton = findViewById(selectedID);
                    str_available_fridge = radioButton.getText().toString();
                    callAddCustomerAPI();
                }
                break;
            case R.id.et_languageupdate:
                // setup the alert builder
                builder = new AlertDialog.Builder(UpdateExistingCustomerActivity.this);
                builder.setTitle("Choose an language");
                builder.setItems(arrLanguage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etLanguageupdate.setText(arrLanguage[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.et_category:
                builder = new AlertDialog.Builder(UpdateExistingCustomerActivity.this);
                builder.setTitle("Select Customer Category");
                builder.setItems(arrCusCategory, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etCategory.setText(arrCusCategory[which]);
                        strCustCategoryId = arrCusCatId[which];
                    }
                });

                // create and show the alert dialog
                AlertDialog dialogCat = builder.create();
                dialogCat.show();
                break;
            case R.id.card_pepsiType:
                builder = new AlertDialog.Builder(UpdateExistingCustomerActivity.this);
                builder.setTitle("Select Fridge Type");
                builder.setItems(arrFridgeType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etPepsiType.setText(arrFridgeType[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog1 = builder.create();
                dialog1.show();
                break;
            case R.id.card_cocType:
                builder = new AlertDialog.Builder(UpdateExistingCustomerActivity.this);
                builder.setTitle("Select Fridge Type");
                builder.setItems(arrFridgeType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etCocType.setText(arrFridgeType[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialogCoc = builder.create();
                dialogCoc.show();
                break;
            case R.id.card_rihamType:
                builder = new AlertDialog.Builder(UpdateExistingCustomerActivity.this);
                builder.setTitle("Select Fridge Type");
                builder.setItems(arrFridgeType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etRihamType.setText(arrFridgeType[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialogRiham = builder.create();
                dialogRiham.show();
                break;
            case R.id.card_personalType:
                builder = new AlertDialog.Builder(UpdateExistingCustomerActivity.this);
                builder.setTitle("Select Fridge Type");
                builder.setItems(arrFridgeType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etPersonalType.setText(arrFridgeType[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialogPer = builder.create();
                dialogPer.show();
                break;
            default:
                break;
        }
    }

   /* private void addCustomer() {

        if (binding.chkPepsi.isChecked()) {
            if (strFridge.equalsIgnoreCase("")) {
                strFridge = "Pepsi-" + binding.etPepsiType.getText().toString();
            } else {
                strFridge = strFridge + "," + "Pepsi-" + binding.etPepsiType.getText().toString();
            }
        }

        if (binding.chkCoc.isChecked()) {
            if (strFridge.equalsIgnoreCase("")) {
                strFridge = "Coca-Cola-" + binding.etCocType.getText().toString();
            } else {
                strFridge = strFridge + "," + "Coca-Cola-" + binding.etCocType.getText().toString();
            }
        }

        if (binding.chkRiham.isChecked()) {
            if (strFridge.equalsIgnoreCase("")) {
                strFridge = "Riham-" + binding.etRihamType.getText().toString();
            } else {
                strFridge = strFridge + "," + "Riham-" + binding.etRihamType.getText().toString();
            }
        }

        if (binding.chkPersonal.isChecked()) {
            if (strFridge.equalsIgnoreCase("")) {
                strFridge = "Personal-" + binding.etPersonalType.getText().toString();
            } else {
                strFridge = strFridge + "," + "Personal-" + binding.etPersonalType.getText().toString();
            }
        }

        if (binding.chkNoFridge.isChecked()) {
            if (strFridge.equalsIgnoreCase("")) {
                strFridge = "Not Available";
            } else {
                strFridge = strFridge + "," + "Not Available";
            }
        }


        Customer mCustomer = new Customer();
        mCustomer.setCustomerId(custNum);
        mCustomer.setCustomerCode(custNum);
        mCustomer.setCustomerName(binding.etOwnerName.getText().toString());
        mCustomer.setCustomerName2("");
        mCustomer.setAddress(binding.etCustomerAddress1.getText().toString());
        mCustomer.setCustDivison("");
        mCustomer.setCustChannel("");
        mCustomer.setCustOrg("");
        mCustomer.setCustEmail("");
        mCustomer.setCustPhone(binding.etCustomerTelephone.getText().toString());
        mCustomer.setTinNo(binding.etCrNo.getText().toString());
        mCustomer.setLanguage(binding.etLanguage.getText().toString());
        mCustomer.setFridge(strFridge);
        if (mCurrentLC != null) {
            mCustomer.setLatitude("" + mCurrentLC.getLatitude());
            mCustomer.setLongitude("" + mCurrentLC.getLongitude());
        } else {
            mCustomer.setLatitude("0.000");
            mCustomer.setLongitude("0.000");
        }

        mCustomer.setRadius("20");
        mCustomer.setCustRegion(mSalesman.getRegion());
        mCustomer.setCustSubRegion(mSalesman.getSubRegion());
        mCustomer.setCustType("1");
        mCustomer.setPaymentTerm("0");
        mCustomer.setBarcode("");
        mCustomer.setBalance("0");
        mCustomer.setCreditLimit("0");
        mCustomer.setRouteId(Settings.getString(App.ROUTEID));
        mCustomer.setCustomerType(strCustTypeId);
        mCustomer.setCustomerCategory(strCustCategoryId);

        db.addCustomer(mCustomer);

        //store Last Invoice Number
        Settings.setString(App.CUSTOMER_LAST, custNum);

//        //Add Customer in Recent
//        RecentCustomer recentCustomer = new RecentCustomer();
//        recentCustomer.setCustomer_id(mCustomer.getCustomerId());
//        recentCustomer.setCustomer_name(mCustomer.getCustomerName());
//        recentCustomer.setDate_time(UtilApp.getCurrentDate());
//        db.insertRecentCustomer(recentCustomer);

        //INSERT TRANSACTION
        Transaction transaction = new Transaction();
        transaction.tr_type = Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_CREATED;
        transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
        transaction.tr_customer_num = mCustomer.getCustomerId();
        transaction.tr_customer_name = mCustomer.getCustomerName();
        transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
        transaction.tr_invoice_id = "";
        transaction.tr_order_id = custNum;
        transaction.tr_collection_id = "";
        transaction.tr_pyament_id = "";
        transaction.tr_is_posted = "No";
        transaction.tr_printData = "";
        db.insertTransaction(transaction);

        if (UtilApp.isNetworkAvailable(getApplicationContext())) {
            UtilApp.createBackgroundJob(getApplicationContext());
        }

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        startActivity(new Intent(UpdateExistingCustomerActivity.this, FragmentJourneyPlan.class));
        //Toast.makeText(getApplicationContext(), "Customer added successfully!", Toast.LENGTH_SHORT).show();
        finish();

    }*/

    //Call Add Customer API
    private void callAddCustomerAPI() {

//        ProgressDialog progress = new ProgressDialog(UpdateExistingCustomerActivity.this);
//           progress.setMessage("Please wait...");
//           //  progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//           progress.setIndeterminate(true);
//           // progress.setProgress(0);
//        try {
//            progress.show();
//        }catch (Exception e){
//            e.toString();
//        }


        if (binding.chkPepsi.isChecked()) {
            if (strFridge.equalsIgnoreCase("")) {
                strFridge = "Pepsi-" + binding.etPepsiType.getText().toString();
            } else {
                strFridge = strFridge + "," + "Pepsi-" + binding.etPepsiType.getText().toString();
            }
        }

        if (binding.chkCoc.isChecked()) {
            if (strFridge.equalsIgnoreCase("")) {
                strFridge = "Coca-Cola-" + binding.etCocType.getText().toString();
            } else {
                strFridge = strFridge + "," + "Coca-Cola-" + binding.etCocType.getText().toString();
            }
        }

        if (binding.chkRiham.isChecked()) {
            if (strFridge.equalsIgnoreCase("")) {
                strFridge = "Riham-" + binding.etRihamType.getText().toString();
            } else {
                strFridge = strFridge + "," + "Riham-" + binding.etRihamType.getText().toString();
            }
        }

        if (binding.chkPersonal.isChecked()) {
            if (strFridge.equalsIgnoreCase("")) {
                strFridge = "Personal-" + binding.etPersonalType.getText().toString();
            } else {
                strFridge = strFridge + "," + "Personal-" + binding.etPersonalType.getText().toString();
            }
        }

        if (binding.chkNoFridge.isChecked()) {
            if (strFridge.equalsIgnoreCase("")) {
                strFridge = "Not Available";
            } else {
                strFridge = strFridge + "," + "Not Available";
            }
        }

        String str_name = binding.etOwnerName.getText().toString();
        String cust_add1 = binding.etCustomerAddress1.getText().toString();
        String cust_add2 = binding.etCustomerAddress2.getText().toString();
        String cust_state = binding.etStreet.getText().toString();
        String cust_city = binding.etCity.getText().toString();
        String cust_zip = binding.etZip.getText().toString();
        String cust_phone = binding.etCustomerTelephone.getText().toString();
        String seraial_no = binding.etFridgeSerial.getText().toString();

        String lat = "" + mCurrentLC.getLatitude();
        String str_longitude = "" + mCurrentLC.getLongitude();

        for (int in = 0; in < arrCusCategory.length; in++) {
            if (binding.etCategory.getText().toString().equals(arrCusCategory[in])) {
                strCustCategoryId = arrCusCatId[in];
            }
        }

        mCustomer.setCustomerId(mCustomer.getCustomerId());
        mCustomer.setCustomerName(str_name);
        mCustomer.setCustomerName2(binding.etCustownerUpdatename.getText().toString());
        mCustomer.setAddress(cust_add1);
        mCustomer.setAddress2(cust_add2);
        mCustomer.setCustomerstate(cust_state);
        mCustomer.setCustomercity(cust_city);
        mCustomer.setCustomerzip(cust_zip);
        mCustomer.setCustPhone(cust_phone);
        mCustomer.setCustomerphone1(binding.etCustomerTelephoneOtherupdate.getText().toString());
        mCustomer.setCustomerCategory(binding.etCategory.getText().toString());
        mCustomer.setLanguage(binding.etLanguageupdate.getText().toString());
        //Change
        mCustomer.setIs_fridge_assign(str_available_fridge);
        mCustomer.setFridger_code(binding.etFridgeSerial.getText().toString());
        mCustomer.setLatitude(App.Latitude);
        mCustomer.setLongitude(App.Longitude);
        mCustomer.setCustomerCategory(strCustCategoryId);
        mCustomer.setCustomerchannel(strCustChannelId);
        mCustomer.setCustomerSubCategory(strCustSubCategoryId);
        mCustomer.setFridge(strFridge);
        mCustomer.setTinNo(binding.etCrTin.getText().toString());
        mCustomer.setMonSqu(monday);
        mCustomer.setTueSqu(tues);
        mCustomer.setWedSqu(wed);
        mCustomer.setThuSqu(thur);
        mCustomer.setFriSqu(frid);
        mCustomer.setSatSqu(Sat);
        mCustomer.setSunSqu(sunday);

        String reg = mCustomer.getCustRegion();
        if (reg != null) {
            if (reg.isEmpty()) {
                reg = mSalesman.getRegion();
            }
        } else {
            reg = mSalesman.getRegion();
        }

        String sub_reg = mCustomer.getCustSubRegion();
        if (sub_reg != null) {
            if (sub_reg.isEmpty()) {
                sub_reg = mSalesman.getSubRegion();
            }
        } else {
            sub_reg = mSalesman.getSubRegion();
        }
        mCustomer.setCustRegion(reg);
        mCustomer.setCustSubRegion(sub_reg);


        if (db.checkIsCustomerExist(mCustomer.getCustomerId())) {
            db.updateCustomer(mCustomer.getCustomerId(), mCustomer);

            Transaction transaction = new Transaction();
            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_UPDATE;
            transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
            transaction.tr_customer_num = mCustomer.getCustomerId();
            transaction.tr_customer_name = mCustomer.getCustomerName();
            transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
            transaction.tr_invoice_id = "";
            transaction.tr_order_id = custNum;
            transaction.tr_collection_id = "";
            transaction.tr_pyament_id = "";
            transaction.tr_is_posted = "No";
            transaction.tr_printData = "";
            db.insertTransaction(transaction);

        } else {

            db.updateDepotCustomer(mCustomer.getCustomerId(), mCustomer);

            Transaction transaction = new Transaction();
            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_ADD_DEPT_CUSTOEMR_UPDATE;
            transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
            transaction.tr_customer_num = mCustomer.getCustomerId();
            transaction.tr_customer_name = mCustomer.getCustomerName();
            transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
            transaction.tr_invoice_id = "";
            transaction.tr_order_id = custNum;
            transaction.tr_collection_id = "";
            transaction.tr_pyament_id = "";
            transaction.tr_is_posted = "No";
            transaction.tr_printData = "";
            db.insertTransaction(transaction);
        }

        startActivity(new Intent(me, CustomerDetailActivity.class).putExtra("custmer", mCustomer));
        finish();

//        JsonObject jObj = new JsonObject();
//        jObj.addProperty("method", App.UPDATE_CUST_POST);
//        jObj.addProperty("cust_code",  mCustomer.getCustomerCode());
//        jObj.addProperty("customername",  str_name);
//        jObj.addProperty("cust_name2",  binding.etCustownerUpdatename.getText().toString());
//        jObj.addProperty("customeraddress1",  cust_add1);
//        jObj.addProperty("customeraddress2",  cust_add2);
//        jObj.addProperty("street",  binding.etStreet.getText().toString());
//        jObj.addProperty("customercity",  cust_city);
//        jObj.addProperty("cctype",  mCustomer.getCustomerType());
//        //jObj.addProperty("customerstate",  cust_state);
//        jObj.addProperty("customerzip",  cust_zip);
//        jObj.addProperty("trn_no", binding.etCrTin.getText().toString());
//        jObj.addProperty("customerphone",  cust_phone);
//        jObj.addProperty("customerphone1",  binding.etCustomerTelephoneOtherupdate.getText().toString());
//        jObj.addProperty("language ",  binding.etLanguageupdate.getText().toString());
//        jObj.addProperty("fridge",  strFridge);
//        jObj.addProperty("longitude",  str_longitude);
//        jObj.addProperty("latitude",  lat);
//
//        String reg=mCustomer.getCustRegion();
//        String sub_reg=mCustomer.getCustSubRegion();
//        System.out.println("po-->"+reg);
//
//        jObj.addProperty("invoice_code", Settings.getString(App.INVOICE_CODE));
//        jObj.addProperty("customer_id", mCustomer.getCustomerId());
//        jObj.addProperty("category",  strCustCategoryId);
//        jObj.addProperty("salesman_id",  Settings.getString(App.SALESMANID));
//        jObj.addProperty("route_id",  Settings.getString(App.ROUTEID));
//        jObj.addProperty("region_id", ""+reg);
//        jObj.addProperty("sub_region_id", ""+sub_reg);
//
//        final Call<JsonObject> labelResponse = ApiClient.getService().getUpdateCustomer(jObj);
//
//        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                Log.e("Add Cus Response:", response.toString());
//                UtilApp.logData(UpdateExistingCustomerActivity.this, "Add Customer Response: " + response.toString());
//                if (response.body() != null) {
//                    UtilApp.logData(UpdateExistingCustomerActivity.this, "Add Customer Body Response: " + response.body().toString());
//                    if (response.body().has("STATUS")) {
//                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
//                            //store Last Invoice Number
//                            Settings.setString(App.CUSTOMER_LAST, custNum);
//
//                            //INSERT TRANSACTION
//                            Transaction transaction = new Transaction();
//                            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_ADD_CUSTOEMR_UPDATE;
//                            transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
//                            transaction.tr_customer_num = mCustomer.getCustomerId();
//                            transaction.tr_customer_name = mCustomer.getCustomerName();
//                            transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
//                            transaction.tr_invoice_id = "";
//                            transaction.tr_order_id = custNum;
//                            transaction.tr_collection_id = "";
//                            transaction.tr_pyament_id = "";
//                            transaction.tr_is_posted = "No";
//                            transaction.tr_printData = "";
//                            db.insertTransaction(transaction);
//                           // Toast.makeText(UpdateExistingCustomerActivity.this, "Successfully Updated.", Toast.LENGTH_SHORT).show();
//                            progress.hide();
//                            App.isAddRequestSync = true;
//                            Settings.setString(App.SCANSERIALNUMBER, "");
//                            startActivity(new Intent(me, CustomerDetailActivity.class).putExtra("custmer", mCustomer));
//                            finish();
//                        } else {
//                            // Fail to Post
//                            App.isAddRequestSync = false;
//                            progress.hide();
//                        }
//                    }
//                } else {
//                    App.isAddRequestSync = false;
//                    progress.hide();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable error) {
//                App.isAddRequestSync = false;
//                Log.e("Add Cust Fail", error.getMessage());
//                progress.hide();
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        try {
            str_fridge_code = Settings.getString(App.SCANSERIALNUMBER);
            binding.etFridgeSerial.setText(str_fridge_code);
        } catch (Exception e) {
            e.toString();
        }

    }


}
