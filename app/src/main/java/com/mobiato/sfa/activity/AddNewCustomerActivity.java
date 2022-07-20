package com.mobiato.sfa.activity;

import android.Manifest;
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
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityAddNewCustomerBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;

public class AddNewCustomerActivity extends BaseActivity implements View.OnClickListener {

    public ActivityAddNewCustomerBinding binding;
    public String[] arrLanguage = {"English", "Swahili", "Luganda", "Nyoro", "Tooro", "Lungole", "Longo", "Runyankole"};
    public String[] arrFridgeType = {"Single Door", "Double Door", "Slider"};
    private String strFridge = "", custNum = "", strType = "", strCustTypeId = "", strCustCategoryId = "",
            strCustSubCategoryId = "", strCustChannelId = "",
            strCType = "", sunday = "0", monday = "0", tues = "0", wed = "0", thur = "0", frid = "0", Sat = "0";
    private FusedLocationProviderClient fusedLocationClient;
    private Location mCurrentLC;
    private Salesman mSalesman;
    private DBManager db;
    private ArrayList<String> arrFridge = new ArrayList<>();
    private AlertDialog.Builder builder;
    private String[] arrCusType, arrCusTypeId, arrCusCategory, arrCusCatId, arrChannel, arrChannelId, arrSubCategory, arrSubcategoryId;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewCustomerBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;


        strType = getIntent().getStringExtra("Type");

        db = new DBManager(AddNewCustomerActivity.this);

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

        custNum = UtilApp.getLastIndex("Customer");
        System.out.println("pp0099-->" + Settings.getString(App.INVOICE_CODE));

        mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
        arrCusType = db.getCustomerTypeList();
        arrCusTypeId = db.getCustomerTypeIdList();

        arrCusCategory = db.getCustomerCategoryList();
        arrCusCatId = db.getCustomerCategoryId();

        arrChannel = db.getCustomerChannelList();
        arrChannelId = db.getCustomerChannelId();

        arrSubCategory = db.getCustomerSubCategoryList();
        arrSubcategoryId = db.getCustomerSubCategoryId();

        if (strType.equalsIgnoreCase("Sales")) {
            strCType = getIntent().getStringExtra("CusType");
            setTitle("Customer Detail");
            strCustTypeId = "3";
            binding.titleCustomerType.setVisibility(View.GONE);
            binding.cardCustType.setVisibility(View.GONE);
            binding.titleCustomerCategory.setVisibility(View.VISIBLE);
            binding.cardCustCategory.setVisibility(View.VISIBLE);
        } else {
            setTitle(getString(R.string.add_customer));
            strCustTypeId = "4";
            binding.titleCustomerType.setVisibility(View.GONE);
            binding.cardCustType.setVisibility(View.GONE);
            binding.titleCustomerCategory.setVisibility(View.VISIBLE);
            binding.cardCustCategory.setVisibility(View.VISIBLE);
        }

        binding.edtCustomerId.setText(custNum);
        binding.titleOutletName.setText(getBuilder(getResources().getString(R.string.outlet_name)));
        binding.titleAddress1.setText(getBuilder("Road/Street Name"));
        binding.titleAddress2.setText(getBuilder("Landmark"));
        binding.titleAddresstown.setText(getBuilder("Town/Village"));
        binding.titleAddressdistrict.setText(getBuilder("District"));
        binding.titleCustOwner.setText(getBuilder("Owner Name"));
        binding.titlePhone.setText(getBuilder(getResources().getString(R.string.telephone)));
        //binding.titleVat.setText(getBuilder("TIN No"));
        binding.titleLanguage.setText(getBuilder("Language"));
        binding.titleCustomerCategory.setText(getBuilder("Customer Category"));

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

        binding.btnAdd.setOnClickListener(this);
        binding.etLanguage.setOnClickListener(this);
        binding.cardPersonalType.setOnClickListener(this);
        binding.cardCocType.setOnClickListener(this);
        binding.cardPepsiType.setOnClickListener(this);
        binding.cardRihamType.setOnClickListener(this);
        binding.cardCustType.setOnClickListener(this);
        binding.cardCustCategory.setOnClickListener(this);
        binding.cardCustChannel.setOnClickListener(this);
        binding.cardCustSubCategory.setOnClickListener(this);
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

            if (binding.etCustmerName.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please enter Customer name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustownerName.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please enter Owner name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerAddress1.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please enter Road/Street Name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerAddress2.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please enter Landmark", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTwon.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please enter Town/Village", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerDistrict.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please enter District", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please enter Telephone", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().length() < 10) {
                Toast.makeText(AddNewCustomerActivity.this, "Please enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustChannel.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please select Channel", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustCategory.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please select Category", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustSubCategory.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please select Sub Category", Toast.LENGTH_SHORT).show();
            } else if (binding.etLanguage.getText().toString().matches("")) {
                Toast.makeText(AddNewCustomerActivity.this, "Please select Language", Toast.LENGTH_SHORT).show();
            } else if (!binding.etCrNo.getText().toString().equalsIgnoreCase("") && binding.etCrNo.getText().length() < 10) {
                Toast.makeText(AddNewCustomerActivity.this, "Please enter 10 digit TIN Number", Toast.LENGTH_SHORT).show();
            } else if (arrFridge.size() == 0) {
                Toast.makeText(AddNewCustomerActivity.this, "Please select Fridge", Toast.LENGTH_SHORT).show();
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
            case R.id.btnAdd:
                if (checkNullValues()) {
                    addCustomer();
                }
                break;
            case R.id.et_language:
                // setup the alert builder
                builder = new AlertDialog.Builder(AddNewCustomerActivity.this);
                builder.setTitle("Choose an language");
                builder.setItems(arrLanguage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etLanguage.setText(arrLanguage[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.card_pepsiType:
                builder = new AlertDialog.Builder(AddNewCustomerActivity.this);
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
                builder = new AlertDialog.Builder(AddNewCustomerActivity.this);
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
                builder = new AlertDialog.Builder(AddNewCustomerActivity.this);
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
                builder = new AlertDialog.Builder(AddNewCustomerActivity.this);
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
            case R.id.cardCustChannel:
                builder = new AlertDialog.Builder(AddNewCustomerActivity.this);
                builder.setTitle("Select Customer Channel");
                builder.setItems(arrChannel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        binding.etCustChannel.setText(arrChannel[which]);
                        strCustChannelId = arrChannelId[which];

                        strCustCategoryId = "";
                        binding.etCustCategory.setText("");
                        arrCusCategory = new String[0];
                        arrCusCategory = db.getChannelCategoryList(strCustChannelId);
                        arrCusCatId = new String[0];
                        arrCusCatId = db.getChannelCategoryId(strCustChannelId);

                    }
                });

                // create and show the alert dialog
                AlertDialog dialogChan = builder.create();
                dialogChan.show();
                break;
            case R.id.cardCustSubCategory:
                builder = new AlertDialog.Builder(AddNewCustomerActivity.this);
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
                break;
            case R.id.cardCustCategory:

                if (strCustChannelId.isEmpty()) {
                    UtilApp.displayAlert(AddNewCustomerActivity.this, "Please select channel first!!");
                } else {
                    builder = new AlertDialog.Builder(AddNewCustomerActivity.this);
                    builder.setTitle("Select Customer Category");
                    builder.setItems(arrCusCategory, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            binding.etCustCategory.setText(arrCusCategory[which]);
                            strCustCategoryId = arrCusCatId[which];
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialogCat = builder.create();
                    dialogCat.show();
                }
                break;
            case R.id.cardCustType:
                builder = new AlertDialog.Builder(AddNewCustomerActivity.this);
                builder.setTitle("Select Customer Type");
                builder.setItems(arrCusType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etCustType.setText(arrCusType[which]);
                        strCustTypeId = arrCusTypeId[which];
                    }
                });

                // create and show the alert dialog
                AlertDialog dialogType = builder.create();
                dialogType.show();
                break;
            default:
                break;
        }
    }

    private void addCustomer() {

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
        mCustomer.setCustomerName(binding.etCustmerName.getText().toString());
        mCustomer.setCustomerName2(binding.etCustownerName.getText().toString());
        mCustomer.setAddress(binding.etCustomerAddress1.getText().toString());
        mCustomer.setAddress2(binding.etCustomerAddress2.getText().toString());
        mCustomer.setCustomerstate(binding.etCustomerTwon.getText().toString());
        mCustomer.setCustomercity(binding.etCustomerDistrict.getText().toString());
        mCustomer.setCustDivison("");
        mCustomer.setCustChannel("");
        mCustomer.setCustOrg("");
        mCustomer.setCustEmail("");
        mCustomer.setCustPhone(binding.etCustomerTelephone.getText().toString());
        mCustomer.setCustomerphone1(binding.etCustomerTelephoneOther.getText().toString());
        mCustomer.setTinNo(binding.etCrNo.getText().toString());
        mCustomer.setLanguage(binding.etLanguage.getText().toString());
        mCustomer.setCustomerzip(binding.etZip.getText().toString());
        mCustomer.setFridge(strFridge);
        // if (mCurrentLC != null) {
        mCustomer.setLatitude("" + App.Latitude);
        mCustomer.setLongitude("" + App.Longitude);
        //} else {
//        mCustomer.setLatitude("0.000");
//        mCustomer.setLongitude("0.000");
        // }

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
        mCustomer.setCustomerchannel(strCustChannelId);
        mCustomer.setCustomerSubCategory(strCustSubCategoryId);
        mCustomer.setMonSqu(monday);
        mCustomer.setTueSqu(tues);
        mCustomer.setWedSqu(wed);
        mCustomer.setThuSqu(thur);
        mCustomer.setFriSqu(frid);
        mCustomer.setSatSqu(Sat);
        mCustomer.setSunSqu(sunday);


        db.addCustomer(mCustomer);

        //store Last Invoice Number
        Settings.setString(App.CUSTOMER_LAST, custNum);

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
        startActivity(new Intent(AddNewCustomerActivity.this, FragmentJourneyPlan.class));
        //Toast.makeText(getApplicationContext(), "Customer added successfully!", Toast.LENGTH_SHORT).show();
        finish();

    }

}
