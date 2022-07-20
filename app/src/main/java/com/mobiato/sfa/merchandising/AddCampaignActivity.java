package com.mobiato.sfa.merchandising;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.databinding.ActivityAddCampaignBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Compaign;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddCampaignActivity extends BaseActivity {

    private ActivityAddCampaignBinding binding;
    Compaign mCompaign = new Compaign();
    DBManager db;
    ArrayList<String> arrImage = new ArrayList<>();
    ArrayList<String> arrImagePath = new ArrayList<>();
    private static final int TAKE_PHOTO_REQUEST = 1;
    String mCurrentPhotoPath, mFilePath;
    private File photoFile;
    String CustomerId = "";
    String Type = "";
    private ArrayList<String> arrPagerImage = new ArrayList<>();
    Compaign mComplainItem = new Compaign();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCampaignBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        setTitle("CAMPAIGN PICTURE CAPTURE");
        Type = getIntent().getStringExtra("type");
        CustomerId = getIntent().getStringExtra("CustomerId");
        db = new DBManager(this);
        arrImage = new ArrayList<>();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (Type.equals("Details")) {
            setTitle("CAMPAIGN PITURE DETAILS");
            String ID = getIntent().getStringExtra("id");
            mComplainItem = db.getCampaignListByID(ID);
            binding.btnAddCompagin.setVisibility(View.GONE);
            binding.layoutImage.setEnabled(false);
            binding.edtComment.setText(mComplainItem.getComment());

            binding.edtComment.setEnabled(false);

            if (!mComplainItem.getCompaign_Image1().equalsIgnoreCase("")) {

                String path = "/mnt/sdcard/Riham/" + mComplainItem.getCompaign_Image1();
                File image = new File(path);

                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                arrPagerImage.add(mCurrentPhotoPath);
            }
            if (!mComplainItem.getCompaign_Image2().equalsIgnoreCase("")) {
                String path = "/mnt/sdcard/Riham/" + mComplainItem.getCompaign_Image2();
                File image = new File(path);


                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                arrPagerImage.add(mCurrentPhotoPath);
            }
            if (!mComplainItem.getCompaign_Image3().equalsIgnoreCase("")) {
                String path = "/mnt/sdcard/Riham/" + mComplainItem.getCompaign_Image3();
                File image = new File(path);

                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                arrPagerImage.add(mCurrentPhotoPath);
            }
            if (!mComplainItem.getCompaign_Image4().equalsIgnoreCase("")) {
                String path = "/mnt/sdcard/Riham/" + mComplainItem.getCompaign_Image4();
                File image = new File(path);

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
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);

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

                String Comment = binding.edtComment.getText().toString();
                if (Comment.isEmpty()) {
                    binding.edtComment.requestFocus();
                    binding.edtComment.setError("Enter Comment");
                } else if (arrImage.size() == 0) {
                    UtilApp.displayAlert(AddCampaignActivity.this, "Please insert campaign images!");
                } else {

                    UtilApp.confirmationDialog("Are you sure you want to submit?",AddCampaignActivity.this, new OnSearchableDialog() {
                        @Override
                        public void onItemSelected(Object o) {
                            String selection = (String) o;
                            if (selection.equalsIgnoreCase("yes")) {
                                submitData();
                            }
                        }
                    });


                }
            }
        });

    }

    private void submitData() {
        mCompaign = new Compaign();
        String cmpNo = UtilApp.getCampiganNo();
        mCompaign.setCompaignId(cmpNo);
        mCompaign.setComment(binding.edtComment.getText().toString());
        mCompaign.setCustomerId(CustomerId);
        if (arrImagePath.size() == 1) {
            mCompaign.setCompaign_Image1(arrImagePath.get(0));
            mCompaign.setCompaign_Image2("");
            mCompaign.setCompaign_Image3("");
            mCompaign.setCompaign_Image4("");
        } else if (arrImagePath.size() == 2) {
            mCompaign.setCompaign_Image1(arrImagePath.get(0));
            mCompaign.setCompaign_Image2(arrImagePath.get(1));
            mCompaign.setCompaign_Image3("");
            mCompaign.setCompaign_Image4("");
        } else if (arrImagePath.size() == 3) {
            mCompaign.setCompaign_Image1(arrImagePath.get(0));
            mCompaign.setCompaign_Image2(arrImagePath.get(1));
            mCompaign.setCompaign_Image3(arrImagePath.get(2));
            mCompaign.setCompaign_Image4("");
        } else if (arrImagePath.size() == 4) {
            mCompaign.setCompaign_Image1(arrImagePath.get(0));
            mCompaign.setCompaign_Image2(arrImagePath.get(1));
            mCompaign.setCompaign_Image3(arrImagePath.get(2));
            mCompaign.setCompaign_Image4(arrImagePath.get(3));
        }

        db.insertCOMPAIGN(mCompaign);
        App.isCampaignSync = false;

        //INSERT TRANSACTION
        Transaction transaction = new Transaction();
        transaction.tr_type = Constant.TRANSACTION_TYPES.TT_COMPAIGN_FEEDBACK;
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

        new AlertDialog.Builder(AddCampaignActivity.this)
                .setMessage("Campaign added successfully")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(me, CampaignListActivity.class);
                        intent.putExtra("CustomerId", CustomerId);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();

        if (UtilApp.isNetworkAvailable(getApplicationContext())) {
            UtilApp.createBackgroundJob(getApplicationContext());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddCampaignActivity.this, mCurrentPhotoPath);
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
        String imageFileName = "JPEG_" + timeStamp + "_";
        mFilePath = "Riham_" + timeStamp + ".png";
        File storageDir = Environment.getExternalStoragePublicDirectory("Riham");
        if (!storageDir.exists()) {

            if (!storageDir.mkdirs()) {
                return null;
            }
        }

        File image = new File(storageDir.getPath() + File.separator +
                mFilePath);

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
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

}
