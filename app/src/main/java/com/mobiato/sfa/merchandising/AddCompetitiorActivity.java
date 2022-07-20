package com.mobiato.sfa.merchandising;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.databinding.ActivityAddCompetitiorBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Compititor;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddCompetitiorActivity extends BaseActivity {

    public ActivityAddCompetitiorBinding binding;
    private DBManager db;
    private ArrayList<String> arrImage = new ArrayList<>();
    private ArrayList<String> arrImagePath = new ArrayList<>();
    private static final int TAKE_PHOTO_REQUEST = 1;
    private String mCurrentPhotoPath, mFilePath;
    private File photoFile;
    private String Type = "";
    private ArrayList<String> arrPagerImage = new ArrayList<>();
    private Compititor mComplainItem = new Compititor();
    Compititor mPromotion = new Compititor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCompetitiorBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        Type = getIntent().getStringExtra("type");
        setTitle("COMPITITOR INFORMATION");

        db = new DBManager(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Type.equals("Details")) {
            String ID = getIntent().getStringExtra("id");
            mComplainItem = db.getCompititorListByID(ID);
            binding.btnAddCompagin.setVisibility(View.GONE);
            binding.layoutImage.setEnabled(false);
            binding.edtCompanyName.setText(mComplainItem.getCompititorCompanyName());
            binding.edtItemName.setText(mComplainItem.getCompititor_ItemName());
            binding.edtNotes.setText(mComplainItem.getCompititor_Notes());
            binding.edtPrice.setText(mComplainItem.getCOMPITITOR_Price());
            binding.edtPromotion.setText(mComplainItem.getCompititor_Promotion());
            binding.txtBrand.setText(mComplainItem.getCompititor_brand());
            binding.edtCompanyName.setEnabled(false);
            binding.edtItemName.setEnabled(false);
            binding.edtNotes.setEnabled(false);
            binding.edtPrice.setEnabled(false);
            binding.edtPromotion.setEnabled(false);
            binding.txtBrand.setEnabled(false);
            if (!mComplainItem.getCompititor_Image1().equalsIgnoreCase("")) {

                String path = "/mnt/sdcard/Riham/" + mComplainItem.getCompititor_Image1();
                File image = new File(path);

                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                arrPagerImage.add(mCurrentPhotoPath);
            }
            if (!mComplainItem.getCompititor_Image2().equalsIgnoreCase("")) {
                String path = "/mnt/sdcard/Riham/" + mComplainItem.getCompititor_Image2();
                File image = new File(path);


                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                arrPagerImage.add(mCurrentPhotoPath);
            }
            if (!mComplainItem.getCompititor_Image3().equalsIgnoreCase("")) {
                String path = "/mnt/sdcard/Riham/" + mComplainItem.getCompititor_Image3();
                File image = new File(path);

//                mFilePath = image.getAbsolutePath();
                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                arrPagerImage.add(mCurrentPhotoPath);
            }
            if (!mComplainItem.getCompititor_Image4().equalsIgnoreCase("")) {
                String path = "/mnt/sdcard/Riham/" + mComplainItem.getCompititor_Image4();
                File image = new File(path);

//                mFilePath = image.getAbsolutePath();
                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                arrPagerImage.add(mCurrentPhotoPath);
            }
            binding.layoutImage.setVisibility(View.GONE);
            binding.layoutPager.setVisibility(View.VISIBLE);
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrPagerImage);
            binding.viewPager.setAdapter(viewPagerAdapter);
            binding.dotsIndicators.setViewPager(binding.viewPager);
            binding.btnSubmit.setVisibility(View.GONE);
        }

        binding.layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        binding.btnAddCompagin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrImage.size() < 4) {
                    dispatchTakePictureIntent();
                }

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Item_Name = binding.edtItemName.getText().toString();
                String CompanyName = binding.edtCompanyName.getText().toString();
                String Notes = binding.edtNotes.getText().toString();
                String Price = binding.edtPrice.getText().toString();
                String Promotion = binding.edtPromotion.getText().toString();
                if (Item_Name.isEmpty()) {
                    binding.edtItemName.requestFocus();
                    binding.edtItemName.setError("Enter Item Name");
                } else if (CompanyName.isEmpty()) {
                    binding.edtCompanyName.requestFocus();
                    binding.edtCompanyName.setError("Enter Company Name");
                } else if (Notes.isEmpty()) {
                    binding.edtNotes.requestFocus();
                    binding.edtNotes.setError("Enter Notes ");
                } else if (Price.isEmpty()) {
                    binding.edtPrice.requestFocus();
                    binding.edtPrice.setError("Enter Price ");
                } else if (Promotion.isEmpty()) {
                    binding.edtPromotion.requestFocus();
                    binding.edtPromotion.setError("Enter Promotion ");
                } else {
                    mPromotion = new Compititor();
                    String cmpNo = UtilApp.getCompititorNo();
                    mPromotion.setCompititorId(cmpNo);
                    mPromotion.setCompititorCompanyName(binding.edtCompanyName.getText().toString());
                    mPromotion.setCompititor_brand(binding.txtBrand.getText().toString());
                    mPromotion.setCompititor_ItemName(binding.edtItemName.getText().toString());
                    mPromotion.setCOMPITITOR_Price(binding.edtPrice.getText().toString());
                    mPromotion.setCompititor_Promotion(binding.edtPromotion.getText().toString());
                    mPromotion.setCompititor_Notes(binding.edtNotes.getText().toString());
                    if (arrImagePath.size() == 1) {
                        mPromotion.setCompititor_Image1(arrImagePath.get(0));
                        mPromotion.setCompititor_Image2("");
                        mPromotion.setCompititor_Image3("");
                        mPromotion.setCompititor_Image4("");
                    } else if (arrImagePath.size() == 2) {
                        mPromotion.setCompititor_Image1(arrImagePath.get(0));
                        mPromotion.setCompititor_Image2(arrImagePath.get(1));
                        mPromotion.setCompititor_Image3("");
                        mPromotion.setCompititor_Image4("");
                    } else if (arrImagePath.size() == 3) {
                        mPromotion.setCompititor_Image1(arrImagePath.get(0));
                        mPromotion.setCompititor_Image2(arrImagePath.get(1));
                        mPromotion.setCompititor_Image3(arrImagePath.get(2));
                        mPromotion.setCompititor_Image4("");
                    } else if (arrImagePath.size() == 4) {
                        mPromotion.setCompititor_Image1(arrImagePath.get(0));
                        mPromotion.setCompititor_Image2(arrImagePath.get(1));
                        mPromotion.setCompititor_Image3(arrImagePath.get(2));
                        mPromotion.setCompititor_Image4(arrImagePath.get(3));
                    }
                    db.insertCOMPITITOR(mPromotion);

                    //INSERT TRANSACTION
                    Transaction transaction = new Transaction();
                    transaction.tr_type = Constant.TRANSACTION_TYPES.TT_COMPATITOR;
                    transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
                    transaction.tr_customer_num = "";
                    transaction.tr_customer_name = "";
                    transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
                    transaction.tr_invoice_id = "";
                    transaction.tr_order_id = cmpNo;
                    transaction.tr_collection_id = "";
                    transaction.tr_pyament_id = "";
                    transaction.tr_printData = "";
                    transaction.tr_is_posted = "No";

                    db.insertTransaction(transaction);

                    if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                        UtilApp.createBackgroundJob(getApplicationContext());
                    }


                    new AlertDialog.Builder(AddCompetitiorActivity.this)
                            .setMessage("Compititor Information added successfully")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(me, CompetitorInformationActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddCompetitiorActivity.this, mCurrentPhotoPath);
            binding.layoutImage.setVisibility(View.GONE);
            binding.layoutPager.setVisibility(View.VISIBLE);
            binding.btnAddCompagin.setVisibility(View.VISIBLE);
            arrImage.add(filePat);
            arrImagePath.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage);
            binding.viewPager.setAdapter(viewPagerAdapter);
            binding.dotsIndicators.setViewPager(binding.viewPager);
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


    private void dispatchTakePictureIntent() {

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
            Log.e("Patah",photoFile.toString());
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
