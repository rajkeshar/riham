package com.mobiato.sfa.activity.servicevisit;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.AddFridgeActivity;
import com.mobiato.sfa.activity.AddFridgeThreeActivity;
import com.mobiato.sfa.activity.AddNewCustomerActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormActivity;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormTwoActivity;
import com.mobiato.sfa.databinding.ActivityChilleraddBinding;
import com.mobiato.sfa.databinding.ActivityServiceEquipmentBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.FridgeMaster;
import com.mobiato.sfa.model.NatureMaster;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ServiceEquipmentActivity extends BaseActivity implements View.OnClickListener {

    public ActivityServiceEquipmentBinding binding;
    public String[] arrPurpose = {"PM", "BD", "Inspection", "Cooler Audit"};
    private AlertDialog.Builder builder;
    private String mType = "", mStartTime = "", mTicketNo = "", natureOfCall = "No cooling Door broken",
            natureOfCallId = "", ctStatus = "";
    int year, month, day;
    String mYear, mMonth, mDay;
    private ArrayList<FridgeMaster> arrFridge = new ArrayList<>();
    private ArrayList<NatureMaster> arrNatature = new ArrayList<>();
    private DBManager db;
    private String[] arrModel, arrTicket;
    public String[] arrCoolerSize = {"CH200X", "CHC200", "CHC400X", "eKoCool-35", "eKoCool-JJUMBO-X", "SAC120", "SPE0253", "SPE0403", "SRC220", "SRC650", "SRC850", "Steca 200", "Venus", "W200", "SRC1000SD-GL", "SRC1100-XGLI", "SRC350-XGLI", "SRC550-XGLI", "SRC60", "FR170", "FR240"};

    public String[] arrCTSStatus = {"Same Outlet", "Mismatch Outlet"};

    private File photoFile_chiller;
    private String mCurrentPhotoPath, mFilePath;
    private static final int TAKE_PHOTO_CHILLER_REQUEST = 7;
    private ArrayList<String> arrImage_chiller = new ArrayList<>();
    private ArrayList<String> arrImagePath_chiller = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceEquipmentBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setNavigationView();
        setTitle("Equipment Details");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        db = new DBManager(this);
        arrFridge = db.getFridgeData();
        arrNatature = db.getNatureData();
        getModelMasterData();
        getTickeNoData();

        ArrayAdapter adapter = new
                ArrayAdapter(this, android.R.layout.simple_list_item_1, arrModel);


        mStartTime = UtilApp.getCurrent24TimeVisit();
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        mYear = String.valueOf(year).substring(2, 4);
        month = c.get(Calendar.MONTH);
        if ((month + 1) > 10) {
            mMonth = String.valueOf((month + 1));
        } else {
            mMonth = "0" + String.valueOf((month + 1));
        }
        day = c.get(Calendar.DAY_OF_MONTH);
        if (day > 10) {
            mDay = String.valueOf(day);
        } else {
            mDay = "0" + String.valueOf(day);
        }

        binding.etSerial.setAdapter(adapter);

        binding.etSerial.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                setModelData(pos);

            }
        });

        binding.btnTransferNext.setOnClickListener(this);
        binding.etPurpose.setOnClickListener(this);
        binding.etModel.setOnClickListener(this);
        binding.btnAddChiller.setOnClickListener(this);
        binding.etBDticketNo.setOnClickListener(this);
        binding.etStatus.setOnClickListener(this);

    }

    public void getModelMasterData() {
        arrModel = new String[arrFridge.size()];
        for (int i = 0; i < arrFridge.size(); i++) {
            arrModel[i] = arrFridge.get(i).getSerial_number();
        }
    }

    public void getTickeNoData() {
        arrTicket = new String[arrNatature.size()];
        for (int i = 0; i < arrNatature.size(); i++) {
            arrTicket[i] = arrNatature.get(i).getTicket_no();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTransferNext:
                if (checkNullValues()) {

                    Intent intent = new Intent(ServiceEquipmentActivity.this, ServiceVisitTwoActivity.class);
                    intent.putExtra("type", mType);
                    intent.putExtra("startTime", mStartTime);
                    intent.putExtra("ticketNo", mTicketNo);
                    intent.putExtra("outletName", binding.etCustomerName.getText().toString());
                    intent.putExtra("branding", binding.etBranding.getText().toString());
                    intent.putExtra("ownername", binding.etOwnerName.getText().toString());
                    intent.putExtra("town", binding.etLocation.getText().toString());
                    intent.putExtra("landmark", binding.etLandmark.getText().toString());
                    intent.putExtra("road_street", binding.etPostalAddress.getText().toString());
                    intent.putExtra("number", binding.etCustomerTelephone.getText().toString());
                    intent.putExtra("asset", binding.etAsset.getText().toString());
                    intent.putExtra("serial", binding.etSerial.getText().toString());
                    intent.putExtra("model", binding.etModel.getText().toString());
                    intent.putExtra("contactNo2", binding.etCustomerTelephone2.getText().toString());
                    intent.putExtra("contactPerson", binding.etContactPerson.getText().toString());
                    intent.putExtra("scanImage", arrImagePath_chiller.get(0));
                    intent.putExtra("natureOfCall", natureOfCall);
                    intent.putExtra("natureOfCallId", natureOfCallId);
                    intent.putExtra("ctsStatus", ctStatus);
                    intent.putExtra("ctsComment", binding.etCtcComment.getText().toString());
                    intent.putExtra("district", binding.etDistrict.getText().toString());
                    startActivity(intent);

                }
                break;
            case R.id.et_purpose:
                builder = new AlertDialog.Builder(ServiceEquipmentActivity.this);
                builder.setTitle("Select Purpose of visit");
                builder.setItems(arrPurpose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mType = arrPurpose[which];
                        binding.etPurpose.setText(arrPurpose[which]);

                        setClearData();
                        if (mType.equals("PM")) {
                            String mPmNo = "";
                            if (Integer.parseInt(Settings.getString(App.PM_ServiceLast)) < 10) {
                                mPmNo = "0" + Integer.parseInt(Settings.getString(App.PM_ServiceLast));
                            } else {
                                mPmNo = String.valueOf(Integer.parseInt(Settings.getString(App.PM_ServiceLast)));
                            }
                            mTicketNo = "PM" + mYear + mMonth + mDay + Settings.getString(App.SALESMANID) + mPmNo;
                            binding.viewNormal.setVisibility(View.VISIBLE);
                            binding.viewBDNormal.setVisibility(View.GONE);
                        } else if (mType.equals("BD")) {
                            String mPmNo = "";
                            if (Integer.parseInt(Settings.getString(App.BD_ServiceLast)) < 10) {
                                mPmNo = "0" + Settings.getString(App.BD_ServiceLast);
                            } else {
                                mPmNo = Settings.getString(App.BD_ServiceLast);
                            }
                            mTicketNo = "BD" + mYear + mMonth + mDay + Settings.getString(App.SALESMANID) + mPmNo;
                            binding.viewNormal.setVisibility(View.GONE);
                            binding.viewBDNormal.setVisibility(View.VISIBLE);
                        } else if (mType.equals("Inspection")) {
                            String mPmNo = "";
                            if (Integer.parseInt(Settings.getString(App.SA_ServiceLast)) < 10) {
                                mPmNo = "0" + Settings.getString(App.SA_ServiceLast);
                            } else {
                                mPmNo = Settings.getString(App.SA_ServiceLast);
                            }
                            mTicketNo = "SA" + mYear + mMonth + mDay + Settings.getString(App.SALESMANID) + mPmNo;
                            binding.viewNormal.setVisibility(View.VISIBLE);
                            binding.viewBDNormal.setVisibility(View.GONE);
                        } else {
                            String mPmNo = "";
                            if (Integer.parseInt(Settings.getString(App.CA_ServiceLast)) < 10) {
                                mPmNo = "0" + Settings.getString(App.CA_ServiceLast);
                            } else {
                                mPmNo = Settings.getString(App.CA_ServiceLast);
                            }
                            mTicketNo = "CA" + mYear + mMonth + mDay + Settings.getString(App.SALESMANID) + mPmNo;
                            binding.viewNormal.setVisibility(View.VISIBLE);
                            binding.viewBDNormal.setVisibility(View.GONE);
                        }

                        binding.etTicketNo.setText(mTicketNo);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.et_model:
                builder = new AlertDialog.Builder(ServiceEquipmentActivity.this);
                builder.setTitle("Select Model");
                builder.setItems(arrCoolerSize, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etModel.setText(arrCoolerSize[which]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog1 = builder.create();
                dialog1.show();
                break;

            case R.id.et_status:
                builder = new AlertDialog.Builder(ServiceEquipmentActivity.this);
                builder.setTitle("Select CTS Status");
                builder.setItems(arrCTSStatus, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etStatus.setText(arrCTSStatus[which]);
                        if (arrCTSStatus[which].equals("Same Outlet")) {
                            ctStatus = "1";
                        } else {
                            ctStatus = "2";
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialogS = builder.create();
                dialogS.show();
                break;
            case R.id.et_BDticketNo:
                builder = new AlertDialog.Builder(ServiceEquipmentActivity.this);
                builder.setTitle("Select Ticket No");
                builder.setItems(arrTicket, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.etBDticketNo.setText(arrTicket[which]);
                        mTicketNo = arrTicket[which];
                        setTicketData(which);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog2 = builder.create();
                dialog2.show();
                break;
            case R.id.btnAddChiller:
                if (arrImage_chiller.size() < 1 || arrImage_chiller.size() > 2) {
                    dispatchTakeChillerPictureIntent();
                }
                break;

        }
    }

    private void setTicketData(int position) {

        NatureMaster mFridge = arrNatature.get(position);
        String ticketNo = binding.etBDticketNo.getText().toString();
        for (int i = 0; i < arrNatature.size(); i++) {
            if (ticketNo.equals(arrNatature.get(i).getTicket_no())) {
                mFridge = arrNatature.get(i);
                break;
            }
        }

        natureOfCallId = mFridge.getId();
        natureOfCall = mFridge.getNature_of_call();
        binding.etSerial.setText(mFridge.getSerial_number());
        binding.etBranding.setText(mFridge.getBranding());
        binding.etModel.setText(mFridge.getModel_number());
        binding.etAsset.setText(mFridge.getAsset_number());
        binding.etCustomerName.setText(mFridge.getOutlet_name());
        binding.etOwnerName.setText(mFridge.getOwner_name());
        binding.etLandmark.setText(mFridge.getLandmark());
        binding.etLocation.setText(mFridge.getTown());
        binding.etCustomerTelephone.setText(mFridge.getCustomerphone());
        binding.etCustomerTelephone2.setText(mFridge.getCustomer_phone2());
        binding.etPostalAddress.setText(mFridge.getRoad_street());
        binding.etDistrict.setText(mFridge.getDistrict());

    }

    private void setClearData() {

        natureOfCall = "No cooling Door broken";
        natureOfCallId = "";
        binding.etStatus.setText("");
        binding.etSerial.setText("");
        binding.etBranding.setText("");
        binding.etModel.setText("");
        binding.etAsset.setText("");
        binding.etCustomerName.setText("");
        binding.etOwnerName.setText("");
        binding.etLandmark.setText("");
        binding.etLocation.setText("");
        binding.etCustomerTelephone.setText("");
        binding.etCustomerTelephone2.setText("");
        binding.etPostalAddress.setText("");
        binding.etDistrict.setText("");

    }

    private void setModelData(int position) {

        FridgeMaster mFridge = arrFridge.get(position);
        String serialNo = binding.etSerial.getText().toString();
        for (int i = 0; i < arrFridge.size(); i++) {
            if (serialNo.equals(arrFridge.get(i).getSerial_number())) {
                mFridge = arrFridge.get(i);
                break;
            }
        }

        binding.etBranding.setText(mFridge.getBranding());
        binding.etModel.setText(mFridge.getModel_number());
        binding.etAsset.setText(mFridge.getAsset_number());
        binding.etCustomerName.setText(mFridge.getCustomername());
        binding.etOwnerName.setText(mFridge.getOwner_name());
        binding.etLandmark.setText(mFridge.getLandmark());
        binding.etLocation.setText(mFridge.getDistrict());
        binding.etCustomerTelephone.setText(mFridge.getCustomerphone());
        binding.etPostalAddress.setText(mFridge.getRoad_street());
        binding.etDistrict.setText(mFridge.getDistrict());

    }

    private boolean checkNullValues() {
        boolean returnvalue = false;
        try {

            if (mType.matches("")) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please select Purpose of Visit", Toast.LENGTH_SHORT).show();
            }  else if (ctStatus.matches("")) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please select CTS Status", Toast.LENGTH_SHORT).show();
            } else if (binding.etModel.getText().toString().matches("")) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please enter Model", Toast.LENGTH_SHORT).show();
            } else if (binding.etSerial.getText().toString().matches("")) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please enter Serial number", Toast.LENGTH_SHORT).show();
            } else if (binding.etOwnerName.getText().toString().matches("")) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please enter Owner name", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerName.getText().toString().matches("")) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please enter Outlet name", Toast.LENGTH_SHORT).show();
            } else if (binding.etLandmark.getText().toString().matches("")) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please enter Landmark", Toast.LENGTH_SHORT).show();
            } else if (binding.etPostalAddress.getText().toString().matches("")) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please enter Road/Street", Toast.LENGTH_SHORT).show();
            } else if (binding.etDistrict.getText().toString().matches("")) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please enter District", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone.getText().toString().length() < 10) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
            } else if (binding.etCustomerTelephone2.getText().toString().length() < 10) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please enter 10 digit Contact Number 2", Toast.LENGTH_SHORT).show();
            } else if (binding.etContactPerson.getText().toString().matches("")) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please enter contact person name", Toast.LENGTH_SHORT).show();
            } else if (arrImage_chiller.size() == 0) {
                Toast.makeText(ServiceEquipmentActivity.this, "Please upload Asset Plate/Serial Number Image", Toast.LENGTH_SHORT).show();
            } else {
                returnvalue = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnvalue;
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
            String filePat = UtilApp.compressImage(ServiceEquipmentActivity.this, mCurrentPhotoPath);
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

    @Override
    public void onBackPressed() {

        //  super.onBackPressed();
    }

}