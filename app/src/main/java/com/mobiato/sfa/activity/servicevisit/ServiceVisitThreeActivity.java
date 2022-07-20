package com.mobiato.sfa.activity.servicevisit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.JsonObject;
import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.Techinician.fragment.CustomerAgreementActivity;
import com.mobiato.sfa.Techinician.fragment.TechnicianChillerListFragment;
import com.mobiato.sfa.Techinician.fragment.TechnicianDashboardFragment;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.activity.LoadVerifyActivity;
import com.mobiato.sfa.activity.forms.RequestForms.ChillerRequestFormThreeActivity;
import com.mobiato.sfa.databinding.ActivityServiceVisitThreeBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.ServiceVisitPost;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.printer.PrintLog;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ServiceVisitThreeActivity extends BaseActivity implements View.OnClickListener {

    public ActivityServiceVisitThreeBinding binding;
    public static String mType = "", pmType = "", bmType = "",
            bmSpareType = "", auditId = "", assetId = "", model = "", serail = "", assets = "", outletName = "",
            ownerName = "", conditionId = "", conditionImage = "", cleanlessId = "", cleanlessImage = "", coilId = "", coilImage = "",
            gasketId = "", gasketImage = "", brandId = "", brandImage = "", lightId = "", lightImage = "", ventiId = "", ventiImage = "",
            levelingId = "", levelingImage = "", stockId = "", stockImage = "",
            landmark = "", roadStreet = "", town = "", contact = "", mStartTime = "", ticketNo = "", timeOut = "",
            latitude = "", longitude = "", branding = "", bmPendingSpareType = "", bmPendingDoneSpareType = "",
            contactPerson = "", contactNumber2 = "", scanImage = "", qalityRating = "", techinRating = "",
            currentVolatge = "", amps = "", cabinTemp = "", workstatus = "Closed", district = "",
            reasonPending = "", natureOfCall = "", natureOfCallId = "", ctsStatus = "", coolerImage1 = "",
            coolerImage2 = "", techMRate = "", qualityRate = "", pmComment = "", bmComment = "", bmCommentSpare = "",
            otherComment = "", ctcComment = "";
    public String[] arrPMType = {"cleaning & polishing"};
    public String[] arrBMType = {"Completed - Service Adjustment Call", "Completed - Spare Replacement"};
    public String[] arrBMPendingType = {"Completed - Service Adjustment Call", "Completed - Spare Replacement", "Spare Pending"};
    public String[] arrBMSpareType = {"Fan motor", "Thermostate", "PCB", "Stabilizer", "Power cord/power cable", "lights",
            "LED drive/choke", "door bush", "Glass Door", "Gasket", "Shelves", "Shelve Clip", "Relay /olp/Capacitor",
            "Compressor", "Gas/Charging valve /Drier-filter", "Condensor", "Evaporator", "Capillary tube", "Solar Pannel", "Solar Battery",
            "Others-specify"};
    public String[] arrBMSparePendingType = {"Fan motor", "Thermostate", "PCB", "Stabilizer", "Power cord/power cable", "lights",
            "LED drive/choke", "door bush", "Glass Door", "Gasket", "Shelves", "Shelve Clip", "Relay /olp/Capacitor",
            "Compressor", "Gas/Charging valve /Drier-filter", "Condensor", "Evaporator", "Capillary tube", "Solar Pannel", "Solar Battery",
            "Others-specify"};
    AlertDialog.Builder builder;
    RadioButton radioButton_Audit;
    RadioButton radioButton_Asset;
    LocationManager locationManager;
    ArrayList<Integer> spareList = new ArrayList<>();
    boolean[] selectedSpare;
    ArrayList<Integer> sparePendingList = new ArrayList<>();
    boolean[] selectedPendingSpare;
    ArrayList<Integer> sparePendingDoneList = new ArrayList<>();
    boolean[] selectedPendingDoneSpare;
    public static Bitmap bmpSign_bh;
    public static File file_bh;
    public static String strDigitSign = "";

    private File photoFile_chiller;
    private String mCurrentPhotoPath, mFilePath;
    private static final int TAKE_PHOTO_CHILLER_REQUEST = 7;
    public static ArrayList<String> arrImage_chiller = new ArrayList<>();
    public static ArrayList<String> arrImagePath_chiller = new ArrayList<>();
    private DBManager db;
    public static boolean isPrevisouClick = false;
    private LoadingSpinner progressDialog;
    private ServiceVisitPost mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceVisitThreeBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Work Details");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        db = new DBManager(this);

        selectedSpare = new boolean[arrBMSpareType.length];
        selectedPendingSpare = new boolean[arrBMSparePendingType.length];
        selectedPendingDoneSpare = new boolean[arrBMSparePendingType.length];

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
            natureOfCall = intent.getStringExtra("natureOfCall");
            natureOfCallId = intent.getStringExtra("natureOfCallId");
            branding = intent.getStringExtra("branding");
            contactNumber2 = intent.getStringExtra("contactNo2");
            contactPerson = intent.getStringExtra("contactPerson");
            scanImage = intent.getStringExtra("scanImage");
            ctsStatus = intent.getStringExtra("ctsStatus");
            coolerImage1 = intent.getStringExtra("coolerImage1");
            coolerImage2 = intent.getStringExtra("coolerImage2");
            district = intent.getStringExtra("district");
            ctcComment = intent.getStringExtra("ctsComment");

            conditionId = intent.getStringExtra("conditionId");
            conditionImage = intent.getStringExtra("conditionImage");

            cleanlessId = intent.getStringExtra("cleanlessd");
            cleanlessImage = intent.getStringExtra("cleanlessImage");

            coilId = intent.getStringExtra("coilId");
            coilImage = intent.getStringExtra("coilImage");

            gasketId = intent.getStringExtra("gasketId");
            gasketImage = intent.getStringExtra("gasketImage");

            brandId = intent.getStringExtra("brandId");
            brandImage = intent.getStringExtra("brandImage");

            lightId = intent.getStringExtra("lightId");
            lightImage = intent.getStringExtra("lightImage");

            ventiId = intent.getStringExtra("ventId");
            ventiImage = intent.getStringExtra("ventImage");

            levelingId = intent.getStringExtra("levelingId");
            levelingImage = intent.getStringExtra("levelingImage");

            stockId = intent.getStringExtra("stockId");
            stockImage = intent.getStringExtra("stockImage");

        } catch (Exception e) {

        }

        binding.ticketNo.setText("Ticket No:   " + ticketNo);


        if (mType.equals("PM")) {
            binding.lytPM.setVisibility(View.VISIBLE);
            binding.etPmNatureCall.setText("PM Service");
            binding.viewObservation.setVisibility(View.VISIBLE);
            binding.viewWorkStatus.setVisibility(View.VISIBLE);
            binding.viewPhotoDetail.setVisibility(View.GONE);
        } else if (mType.equals("BD")) {
            binding.lytPM.setVisibility(View.GONE);
            binding.lytBM.setVisibility(View.VISIBLE);
            binding.etPmNatureCall.setText(natureOfCall);
            binding.viewObservation.setVisibility(View.VISIBLE);
            binding.viewWorkStatus.setVisibility(View.VISIBLE);
            binding.viewPhotoDetail.setVisibility(View.VISIBLE);
        } else if (mType.equals("Inspection")) {
            binding.lytPM.setVisibility(View.GONE);
            binding.lytInspection.setVisibility(View.VISIBLE);
            binding.etPmNatureCall.setText("Service Audit");
            binding.viewObservation.setVisibility(View.GONE);
            binding.viewPhotoDetail.setVisibility(View.VISIBLE);
        } else {
            binding.lytPM.setVisibility(View.GONE);
            binding.lytAudit.setVisibility(View.VISIBLE);
            binding.etPmNatureCall.setText("Cooler Audit");
            binding.viewObservation.setVisibility(View.GONE);
            binding.viewPhotoDetail.setVisibility(View.VISIBLE);
        }

        binding.raYesId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtCommentInspectYes.setVisibility(View.VISIBLE);
            }
        });

        binding.raNoId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtCommentInspectYes.setVisibility(View.GONE);
            }
        });

        binding.raYesAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtCommentAssetYes.setVisibility(View.VISIBLE);
            }
        });

        binding.raNoAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtCommentAssetYes.setVisibility(View.GONE);
            }
        });

        binding.raYesPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workstatus = "pending";
                binding.viewPendingReason.setVisibility(View.VISIBLE);
                if (mType.equals("PM")) {
                    binding.viewPendingDrop.setVisibility(View.GONE);
                } else {
                    binding.viewPendingDrop.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.raNoPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workstatus = "Closed";
                binding.viewPendingReason.setVisibility(View.GONE);
                binding.viewPendingDrop.setVisibility(View.GONE);
            }
        });

        setData();

        binding.etSelectPM.setOnClickListener(this);
        binding.etSelectBM.setOnClickListener(this);
        binding.etSelectBMSpare.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.btnTransferPre.setOnClickListener(this);
        binding.etPendingSPare.setOnClickListener(this);
        binding.btnAddChiller.setOnClickListener(this);

        binding.etBhSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //str_signature = "1";
                showCustomerSignCapture();
            }
        });
    }

    private void setData() {

        if (isPrevisouClick) {
            isPrevisouClick = false;

            binding.edtPmVoltage.setText(currentVolatge);
            binding.edtPmAmp.setText(amps);
            binding.edtPmTemperature.setText(cabinTemp);

            if (!techMRate.equals("0.0")) {
                binding.ratingBarBehavior.setRating(Float.parseFloat(techMRate));
            }

            if (!qualityRate.equals("0.0")) {
                binding.ratingBarQuality.setRating(Float.parseFloat(qualityRate));
            }

            binding.edtReasonPending.setText(reasonPending);
            if (workstatus.equals("pending")) {
                binding.raYesPm.setChecked(true);
                binding.viewPendingReason.setVisibility(View.VISIBLE);
                if (mType.equals("PM")) {
                    binding.viewPendingDrop.setVisibility(View.GONE);
                } else {
                    binding.viewPendingDrop.setVisibility(View.VISIBLE);
                }
            }

            if (mType.equals("PM")) {
                if (!pmType.isEmpty()) {
                    binding.etSelectPM.setText(pmType);
                    binding.edtCommentPM.setVisibility(View.VISIBLE);
                    binding.edtCommentPM.setText(pmComment);
                }
            } else if (mType.equals("BD")) {
                if (!bmType.isEmpty()) {
                    binding.edtCommentBMCall.setText(bmComment);
                    if (bmType.equals("Completed - Service Adjustment Call")) {
                        binding.edtCommentBMCall.setVisibility(View.VISIBLE);
                        binding.lytBMSpare.setVisibility(View.GONE);
                    } else if (bmType.equals("Spare Pending")) {
                        binding.edtCommentBMCall.setVisibility(View.VISIBLE);
                        binding.lytBMSpare.setVisibility(View.GONE);
                    } else {
                        binding.edtCommentBMCall.setVisibility(View.GONE);
                        binding.lytBMSpare.setVisibility(View.VISIBLE);
                        if (!bmSpareType.isEmpty()) {
                            binding.etSelectBMSpare.setText(bmSpareType);
                        }
                        binding.edtCommentBMSpare.setText(bmCommentSpare);
                    }
                }

                binding.etSelectBM.setText(bmType);

                if (!bmPendingSpareType.isEmpty()) {
                    binding.etPendingSPare.setText(bmPendingSpareType);

                    if (bmPendingSpareType.contains("Others-specify")) {
                        binding.edtOtherPendingSpare.setText(otherComment);
                        binding.edtOtherPendingSpare.setVisibility(View.VISIBLE);
                    } else {
                        binding.edtOtherPendingSpare.setVisibility(View.GONE);
                    }
                }

            } else if (mType.equals("Inspection")) {
                binding.edtCommentInspectYes.setVisibility(View.VISIBLE);
                binding.edtCommentInspectYes.setText(pmComment);
            } else {
                binding.edtCommentAssetYes.setVisibility(View.VISIBLE);
                binding.edtCommentAssetYes.setText(pmComment);
            }

            if (bmpSign_bh != null) {
                binding.etBhSignature.setImageBitmap(bmpSign_bh);
            }

            if (arrImage_chiller.size() > 0) {
                binding.layoutImagechiller.setVisibility(View.GONE);
                binding.layoutPagerChiller.setVisibility(View.VISIBLE);
                binding.btnAddChiller.setVisibility(View.VISIBLE);
                ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_chiller);
                binding.viewPagerChiller.setAdapter(viewPagerAdapter);
                binding.dotsIndicatorsChiller.setViewPager(binding.viewPagerChiller);
            }

        }
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double mlatitude = location.getLatitude();
            double mlongitude = location.getLongitude();
            latitude = String.valueOf(mlatitude);
            longitude = String.valueOf(mlongitude);
            String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
            //Toast.makeText(CustomerDetailActivity.this, msg, Toast.LENGTH_LONG).show();
            // insertVisit();
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

    public static void clearData() {
        pmType = "";
        bmType = "";
        bmSpareType = "";
        auditId = "";
        assetId = "";
        model = "";
        serail = "";
        assets = "";
        outletName = "";
        ownerName = "";
        conditionId = "";
        conditionImage = "";
        cleanlessId = "";
        cleanlessImage = "";
        coilId = "";
        coilImage = "";
        gasketId = "";
        gasketImage = "";
        brandId = "";
        brandImage = "";
        lightId = "";
        lightImage = "";
        ventiId = "";
        ventiImage = "";
        levelingId = "";
        levelingImage = "";
        stockId = "";
        stockImage = "";
        landmark = "";
        roadStreet = "";
        town = "";
        contact = "";
        mStartTime = "";
        ticketNo = "";
        timeOut = "";
        latitude = "";
        longitude = "";
        branding = "";
        bmPendingSpareType = "";
        bmPendingDoneSpareType = "";
        contactPerson = "";
        contactNumber2 = "";
        scanImage = "";
        qalityRating = "";
        techinRating = "";
        currentVolatge = "";
        amps = "";
        cabinTemp = "";
        workstatus = "Closed";
        district = "";
        reasonPending = "";
        natureOfCall = "";
        natureOfCallId = "";
        ctsStatus = "";
        coolerImage1 = "";
        coolerImage2 = "";
        pmComment = "";
        bmComment = "";
        bmCommentSpare = "";
        arrImage_chiller.clear();
        arrImagePath_chiller.clear();
        otherComment = "";
        bmpSign_bh = null;
        file_bh = null;
        ctcComment = "";
        strDigitSign = "";

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddChiller:
                if (arrImage_chiller.size() < 2) {
                    dispatchTakeChillerPictureIntent();
                }
                break;
            case R.id.btnTransferPre:

                currentVolatge = binding.edtPmVoltage.getText().toString();
                amps = binding.edtPmAmp.getText().toString();
                cabinTemp = binding.edtPmTemperature.getText().toString();

                techMRate = String.valueOf(binding.ratingBarBehavior.getRating());
                qualityRate = String.valueOf(binding.ratingBarQuality.getRating());

                if (mType.equals("PM")) {
                    reasonPending = binding.edtReasonPending.getText().toString();
                    pmComment = binding.edtCommentPM.getText().toString();
                } else if (mType.equals("BD")) {
                    reasonPending = binding.edtReasonPending.getText().toString();
                    bmComment = binding.edtCommentBMCall.getText().toString();
                    bmCommentSpare = binding.edtCommentBMSpare.getText().toString();
                    otherComment = binding.edtOtherPendingSpare.getText().toString();
                } else if (mType.equals("Inspection")) {
                    reasonPending = binding.edtReasonPending.getText().toString();
                    pmComment = binding.edtCommentInspectYes.getText().toString();
                } else {
                    reasonPending = binding.edtReasonPending.getText().toString();
                    pmComment = binding.edtCommentAssetYes.getText().toString();
                }

                isPrevisouClick = true;
                finish();
                break;
            case R.id.etSelectPM:
                builder = new android.app.AlertDialog.Builder(ServiceVisitThreeActivity.this);
                builder.setTitle("Choose Type");
                builder.setItems(arrPMType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etSelectPM.setText(arrPMType[which]);
                        pmType = arrPMType[which];
                        binding.edtCommentPM.setVisibility(View.VISIBLE);
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog12 = builder.create();
                dialog12.show();
                break;
            case R.id.etSelectBM:
                if (workstatus.equals("pending")) {
                    builder = new android.app.AlertDialog.Builder(ServiceVisitThreeActivity.this);
                    builder.setTitle("Choose Type");
                    builder.setItems(arrBMPendingType, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            binding.etSelectBM.setText(arrBMPendingType[which]);
                            bmType = arrBMPendingType[which];

                            if (bmType.equals("Completed - Service Adjustment Call")) {
                                binding.edtCommentBMCall.setVisibility(View.VISIBLE);
                                binding.lytBMSpare.setVisibility(View.GONE);
                            } else if (bmType.equals("Spare Pending")) {
                                binding.edtCommentBMCall.setVisibility(View.VISIBLE);
                                binding.lytBMSpare.setVisibility(View.GONE);
                            } else {
                                binding.edtCommentBMCall.setVisibility(View.GONE);
                                binding.lytBMSpare.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    // create and show the alert dialog
                    AlertDialog dialogBM = builder.create();
                    dialogBM.show();
                } else {
                    builder = new android.app.AlertDialog.Builder(ServiceVisitThreeActivity.this);
                    builder.setTitle("Choose Type");
                    builder.setItems(arrBMType, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            binding.etSelectBM.setText(arrBMType[which]);
                            bmType = arrBMType[which];

                            if (bmType.equals("Completed - Service Adjustment Call")) {
                                binding.edtCommentBMCall.setVisibility(View.VISIBLE);
                                binding.lytBMSpare.setVisibility(View.GONE);
                            } else {
                                binding.edtCommentBMCall.setVisibility(View.GONE);
                                binding.lytBMSpare.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    // create and show the alert dialog
                    AlertDialog dialogBM = builder.create();
                    dialogBM.show();
                }

                break;
            case R.id.etSelectBMSpare:
                builder = new android.app.AlertDialog.Builder(ServiceVisitThreeActivity.this);
                builder.setTitle("Choose Spare Type");
                // set dialog non cancelable
                builder.setCancelable(false);
                builder.setMultiChoiceItems(arrBMSpareType, selectedSpare, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            spareList.add(i);
                            // Sort array list
                            Collections.sort(spareList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            spareList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < spareList.size(); j++) {
                            // concat array value
                            stringBuilder.append(arrBMSpareType[spareList.get(j)]);
                            // check condition
                            if (j != spareList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        binding.etSelectBMSpare.setText(stringBuilder.toString());
                        bmSpareType = stringBuilder.toString();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedSpare.length; j++) {
                            // remove all selection
                            selectedSpare[j] = false;
                            // clear language list
                            spareList.clear();
                            // clear text view value
                            binding.etSelectBMSpare.setText("");
                            bmSpareType = "";
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialogBMSpare = builder.create();
                dialogBMSpare.show();
                break;
            case R.id.etPendingSPare:
                builder = new android.app.AlertDialog.Builder(ServiceVisitThreeActivity.this);
                builder.setTitle("Select Spare Request");
                // set dialog non cancelable
                builder.setCancelable(false);
                builder.setMultiChoiceItems(arrBMSparePendingType, selectedPendingSpare, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            sparePendingList.add(i);
                            // Sort array list
                            Collections.sort(sparePendingList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            sparePendingList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < sparePendingList.size(); j++) {
                            // concat array value
                            stringBuilder.append(arrBMSparePendingType[sparePendingList.get(j)]);
                            // check condition
                            if (j != sparePendingList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }

                        // set text on textView
                        binding.etPendingSPare.setText(stringBuilder.toString());
                        bmPendingSpareType = stringBuilder.toString();

                        if (bmPendingSpareType.contains("Others-specify")) {
                            binding.edtOtherPendingSpare.setVisibility(View.VISIBLE);
                        } else {
                            binding.edtOtherPendingSpare.setVisibility(View.GONE);
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedPendingSpare.length; j++) {
                            // remove all selection
                            selectedPendingSpare[j] = false;
                            // clear language list
                            sparePendingList.clear();
                            // clear text view value
                            binding.etPendingSPare.setText("");
                            bmPendingSpareType = "";
                            binding.edtOtherPendingSpare.setVisibility(View.GONE);
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialogSPESpare = builder.create();
                dialogSPESpare.show();
                break;
            case R.id.btnSubmit:
                currentVolatge = binding.edtPmVoltage.getText().toString();
                amps = binding.edtPmAmp.getText().toString();
                cabinTemp = binding.edtPmTemperature.getText().toString();

                String techRating = String.valueOf(binding.ratingBarBehavior.getRating());

                if (mType.equals("PM")) {
                    reasonPending = binding.edtReasonPending.getText().toString();
                    if (currentVolatge.isEmpty()) {
                        Toast.makeText(me, "Please enter current voltage", Toast.LENGTH_SHORT).show();
                    } else if (amps.isEmpty()) {
                        Toast.makeText(me, "Please enter Amps AMP", Toast.LENGTH_SHORT).show();
                    } else if (cabinTemp.isEmpty()) {
                        Toast.makeText(me, "Please enter cabin temperature", Toast.LENGTH_SHORT).show();
                    } else if (pmType.isEmpty()) {
                        Toast.makeText(me, "Please select Type", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtCommentPM.getText().toString().isEmpty()) {
                        Toast.makeText(me, "Please enter comment", Toast.LENGTH_SHORT).show();
                    } else if (techRating.equals("0.0")) {
                        Toast.makeText(me, "Please give customer Rating on Technician Behaviour", Toast.LENGTH_SHORT).show();
                    } else if (bmpSign_bh == null) {
                        Toast.makeText(me, "Please upload customer signature.", Toast.LENGTH_SHORT).show();
                    } else {
                        postServiceVisit(pmType, binding.edtCommentPM.getText().toString(), "No");
                    }
                } else if (mType.equals("BD")) {
                    reasonPending = binding.edtReasonPending.getText().toString();
                    if (currentVolatge.isEmpty()) {
                        Toast.makeText(me, "Please enter current voltage", Toast.LENGTH_SHORT).show();
                    } else if (amps.isEmpty()) {
                        Toast.makeText(me, "Please enter Amps AMP", Toast.LENGTH_SHORT).show();
                    } else if (cabinTemp.isEmpty()) {
                        Toast.makeText(me, "Please enter cabin temperature", Toast.LENGTH_SHORT).show();
                    } else if (bmType.isEmpty()) {
                        Toast.makeText(me, "Please select Type", Toast.LENGTH_SHORT).show();
                    } else if (techRating.equals("0.0")) {
                        Toast.makeText(me, "Please give customer Rating on Technician Behaviour", Toast.LENGTH_SHORT).show();
                    } else if (bmpSign_bh == null) {
                        Toast.makeText(me, "Please upload customer signature.", Toast.LENGTH_SHORT).show();
                    } else if (bmType.equals("Completed - Service Adjustment Call")) {
                        if (binding.edtCommentBMCall.getText().toString().isEmpty()) {
                            Toast.makeText(me, "Please enter comment", Toast.LENGTH_SHORT).show();
                        } else {
                            postServiceVisit(bmType, binding.edtCommentBMCall.getText().toString(), "No");
                        }
                    } else if (bmType.equals("Spare Pending")) {
                        if (binding.edtCommentBMCall.getText().toString().isEmpty()) {
                            Toast.makeText(me, "Please enter comment", Toast.LENGTH_SHORT).show();
                        } else {
                            postServiceVisit(bmType, binding.edtCommentBMCall.getText().toString(), "No");
                        }
                    } else if (bmSpareType.isEmpty()) {
                        Toast.makeText(me, "Please select Spare Type", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtCommentBMSpare.getText().toString().isEmpty()) {
                        Toast.makeText(me, "Please enter comment", Toast.LENGTH_SHORT).show();
                    } else {
                        postServiceVisit(bmType, binding.edtCommentBMSpare.getText().toString(), "No");
                    }
                } else if (mType.equals("Inspection")) {
                    reasonPending = binding.edtReasonPending.getText().toString();
                    int selectedID = binding.radioNational.getCheckedRadioButtonId();
                    radioButton_Audit = findViewById(selectedID);
                    auditId = radioButton_Audit.getText().toString();

                    if (binding.etSelectInspServAudit.getText().toString().isEmpty()) {
                        Toast.makeText(me, "Please enter Service Ticket Audit", Toast.LENGTH_SHORT).show();
                    } else if (techRating.equals("0.0")) {
                        Toast.makeText(me, "Please give customer Rating on Technician Behaviour", Toast.LENGTH_SHORT).show();
                    } else if (bmpSign_bh == null) {
                        Toast.makeText(me, "Please upload customer signature.", Toast.LENGTH_SHORT).show();
                    } else if (auditId.equals("Yes")) {
                        if (binding.edtCommentInspectYes.getText().toString().isEmpty()) {
                            Toast.makeText(me, "Please enter comment", Toast.LENGTH_SHORT).show();
                        } else if (bmpSign_bh == null) {
                            Toast.makeText(me, "Please upload customer signature.", Toast.LENGTH_SHORT).show();
                        } else {
                            postServiceVisit(binding.etSelectInspServAudit.getText().toString(), binding.edtCommentInspectYes.getText().toString(), auditId);
                        }
                    } else {
                        postServiceVisit(binding.etSelectInspServAudit.getText().toString(), "", auditId);
                    }
                } else {
                    reasonPending = binding.edtReasonPending.getText().toString();
                    int selectedID = binding.radioAsset.getCheckedRadioButtonId();
                    radioButton_Asset = findViewById(selectedID);
                    assetId = radioButton_Asset.getText().toString();

                    if (binding.etSelectAssetNumber.getText().toString().isEmpty()) {
                        Toast.makeText(me, "Please enter asset number", Toast.LENGTH_SHORT).show();
                    } else if (assetId.equals("Yes")) {
                        if (binding.edtCommentAssetYes.getText().toString().isEmpty()) {
                            Toast.makeText(me, "Please enter comment", Toast.LENGTH_SHORT).show();
                        } else if (techRating.equals("0.0")) {
                            Toast.makeText(me, "Please give customer Rating on Technician Behaviour", Toast.LENGTH_SHORT).show();
                        } else if (bmpSign_bh == null) {
                            Toast.makeText(me, "Please upload customer signature.", Toast.LENGTH_SHORT).show();
                        } else {
                            postServiceVisit(binding.etSelectAssetNumber.getText().toString(), binding.edtCommentAssetYes.getText().toString(), assetId);
                        }
                    } else if (techRating.equals("0.0")) {
                        Toast.makeText(me, "Please give customer Rating on Technician Behaviour", Toast.LENGTH_SHORT).show();
                    } else if (bmpSign_bh == null) {
                        Toast.makeText(me, "Please upload customer signature.", Toast.LENGTH_SHORT).show();
                    } else {
                        postServiceVisit(binding.etSelectAssetNumber.getText().toString(), "", assetId);
                    }
                }
                break;
        }
    }

    private void postServiceVisit(String compainTYpe, String comment, String dispute) {

        currentVolatge = binding.edtPmVoltage.getText().toString();
        amps = binding.edtPmAmp.getText().toString();
        cabinTemp = binding.edtPmTemperature.getText().toString();

        timeOut = UtilApp.getCurrent24TimeVisit();

        String techRating = String.valueOf(binding.ratingBarBehavior.getRating());
        String qalityRating = String.valueOf(binding.ratingBarQuality.getRating());

        mService = new ServiceVisitPost();
        mService.serviceType = mType;
        mService.ticketNo = ticketNo;
        mService.timeIn = mStartTime;
        mService.timeOut = timeOut;
        mService.latitude = latitude;
        mService.longitude = longitude;
        mService.serialNo = serail;
        mService.modelNo = model;
        mService.assetNo = assets;
        mService.brand = branding;
        mService.serialImage = scanImage;
        mService.outletName = outletName;
        mService.ownerName = ownerName;
        mService.landmark = landmark;
        mService.location = roadStreet;
        mService.district = district;
        mService.natureOfCall = natureOfCallId;
        mService.townVillage = town;
        mService.contactNumber = contact;
        mService.contactNumber2 = contactNumber2;
        mService.contactPerson = contactPerson;
        mService.techRating = techRating;
        mService.qualityRate = qalityRating;
        mService.currentVolt = currentVolatge;
        mService.amps = amps;
        mService.temprature = cabinTemp;
        mService.workstatus = workstatus;
        mService.pendingReason = reasonPending;
        mService.pendingSpare = bmPendingSpareType;
        mService.workDoneType = compainTYpe;
        mService.workDoneComment = comment;
        mService.anyDispute = dispute;
        mService.otherSpecific = otherComment;
        mService.workSpare = bmSpareType;
        mService.ctsStatus = ctsStatus;
        mService.ctcComment = ctcComment;
        mService.coolerImage1 = coolerImage1;
        mService.coolerImage2 = coolerImage2;


        mService.workingId = conditionId;
        mService.conditionImage = conditionImage;
        mService.cleanlessId = cleanlessId;
        mService.cleanlessImage = cleanlessImage;
        mService.coilId = coilId;
        mService.coilImage = coilImage;
        mService.stockPer = stockId;
        mService.stockImage = stockImage;
        mService.gasketId = gasketId;
        mService.gasketImage = gasketImage;
        mService.brandingId = brandId;
        mService.brandingImage = brandImage;
        mService.lightId = lightId;
        mService.lightImage = lightImage;
        mService.ventilationId = ventiId;
        mService.ventilationImage = ventiImage;
        mService.levelingId = levelingId;
        mService.levelingImage = levelingImage;

        if (arrImagePath_chiller.size() > 0) {
            mService.disputeImage1 = arrImagePath_chiller.get(0);
        } else {
            mService.disputeImage1 = "";
        }

        if (arrImagePath_chiller.size() > 1) {
            mService.disputeImage2 = arrImagePath_chiller.get(1);
        } else {
            mService.disputeImage2 = "";
        }

        new addServicePost().execute();


    }

    public void displayMessage(Context mContext, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Alert")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(me, TechnicianDashboardFragment.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void showCustomerSignCapture() {
        final Dialog dialog = new Dialog(ServiceVisitThreeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cust_sign);

        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        int width;
        width = manager.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
        Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        final SignaturePad viewSign = dialog.findViewById(R.id.signature);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final Handler handler = new Handler();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (!viewSign.isEmpty()) {
                    bmpSign_bh = viewSign.getSignatureBitmap();
                    binding.etBhSignature.setImageBitmap(bmpSign_bh);
                    //   saveImage(bmpSign_bh);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            // Do something after 5s = 5000ms
//                            saveImage(bmpSign_bh);
//                        }
//                    }, 1000);
                    //saveImage(bmpSign_bh);
                } else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ServiceVisitThreeActivity.this);
                    alertDialogBuilder
                            .setMessage("Please add signature before proceed!")
                            .setCancelable(false)
                            .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
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
            }
        });

    }

    private class addServicePost extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new LoadingSpinner(ServiceVisitThreeActivity.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //Save Signature
            saveImage(bmpSign_bh);

            mService.customerSignature = strDigitSign;

            db.insertServiceVisitPost(mService);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }

            if (mType.equals("PM")) {
                int mPM = Integer.parseInt(Settings.getString(App.PM_ServiceLast));
                mPM++;
                Settings.setString(App.PM_ServiceLast, String.valueOf(mPM));
            } else if (mType.equals("BD")) {
                if (!workstatus.equals("pending")) {
                    db.updateNatureOfTicket(ticketNo);
                }
                if (workstatus.equals("pending")) {
                    int mPM = Integer.parseInt(Settings.getString(App.BD_Pending_ServiceLast));
                    mPM++;
                    Settings.setString(App.BD_Pending_ServiceLast, String.valueOf(mPM));
                } else {
                    int mPM = Integer.parseInt(Settings.getString(App.BD_Done_ServiceLast));
                    mPM++;
                    Settings.setString(App.BD_Done_ServiceLast, String.valueOf(mPM));
                }
                int mPM = Integer.parseInt(Settings.getString(App.BD_ServiceLast));
                mPM++;
                Settings.setString(App.BD_ServiceLast, String.valueOf(mPM));
                App.counttech_BD_MAIN_service++;
            } else if (mType.equals("Inspection")) {
                int mPM = Integer.parseInt(Settings.getString(App.SA_ServiceLast));
                mPM++;
                Settings.setString(App.SA_ServiceLast, String.valueOf(mPM));
            } else {
                int mPM = Integer.parseInt(Settings.getString(App.CA_ServiceLast));
                mPM++;
                Settings.setString(App.CA_ServiceLast, String.valueOf(mPM));
            }

            //INSERT TRANSACTION
            Transaction transaction = new Transaction();
            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_SERVICE_VISIT;
            transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
            transaction.tr_customer_num = "";
            transaction.tr_customer_name = "";
            transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
            transaction.tr_invoice_id = "";
            transaction.tr_order_id = ticketNo;
            transaction.tr_collection_id = "";
            transaction.tr_pyament_id = "";
            transaction.tr_is_posted = "No";
            transaction.tr_printData = "";
            db.insertTransaction(transaction);

            displayMessage(me, "Ticket No: " + ticketNo + " is generated for Service visit successfully!");

            ServiceVisitTwoActivity.clearData();
            clearData();

            if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                UtilApp.createBackgroundJob(getApplicationContext());
            }
        }
    }

    final void saveImage(Bitmap signature) {

        String root = Environment.getExternalStorageDirectory().toString();

        // the directory where the signature will be saved
        File myDir = new File(root + "/saved_signature");

        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // set the file name of your choice
        String fname = "sa_signature.png";

        // in our case, we delete the previous file, you can remove this
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }

        //Store Path
        strDigitSign = file.getAbsolutePath();

        try {
            // save the signature
            FileOutputStream out = new FileOutputStream(file);
            signature.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void dispatchTakeChillerPictureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_chiller = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_chiller));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_CHILLER_REQUEST);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CHILLER_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(ServiceVisitThreeActivity.this, mCurrentPhotoPath);
            binding.layoutImagechiller.setVisibility(View.GONE);
            binding.layoutPagerChiller.setVisibility(View.VISIBLE);
            binding.btnAddChiller.setVisibility(View.VISIBLE);
            arrImage_chiller.add(filePat);
            arrImagePath_chiller.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_chiller);
            binding.viewPagerChiller.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsChiller.setViewPager(binding.viewPagerChiller);
        }

    }

}