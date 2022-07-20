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

import com.mobiato.sfa.Adapter.ViewPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.databinding.ActivityAddPlanogramBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.PlanoImages;
import com.mobiato.sfa.model.PlanogramList;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddPlanogramActivity extends BaseActivity {

    public ActivityAddPlanogramBinding binding;
    DBManager db;
    private ArrayList<PlanoImages> arrData = new ArrayList<>();
    private ArrayList<String> arrPagerImage = new ArrayList<>();
    PlanogramList mPlanogramImages;
    private static final int TAKE_PHOTO_REQUEST = 1;
    String mCurrentPhotoPath, mFilePath;
    private File photoFile;
    ArrayList<String> arrImagePath = new ArrayList<>();
    private boolean isBefore = false;
    private String strBeforeImage = "", strAfterImage = "", ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPlanogramBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        setTitle("PLANOGRAM");
        ID = getIntent().getStringExtra("id");

        //get Customer List
        db = new DBManager(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        arrData = new ArrayList<>();
        mPlanogramImages = db.getPlanogramItem(ID);
        if (!mPlanogramImages.getImage1().equalsIgnoreCase("")) {
            arrPagerImage.add(mPlanogramImages.getImage1());
        }
        if (!mPlanogramImages.getImage2().equalsIgnoreCase("")) {
            arrPagerImage.add(mPlanogramImages.getImage2());
        }
        if (!mPlanogramImages.getImage3().equalsIgnoreCase("")) {
            arrPagerImage.add(mPlanogramImages.getImage3());
        }
        if (!mPlanogramImages.getImage4().equalsIgnoreCase("")) {
            arrPagerImage.add(mPlanogramImages.getImage4());
        }

        if (!mPlanogramImages.getFront_image().equalsIgnoreCase("") &&
                !mPlanogramImages.getBack_image().equalsIgnoreCase("")) {
            binding.layoutSave.setVisibility(View.GONE);
            binding.layoutImage1.setVisibility(View.GONE);
            binding.image1.setVisibility(View.VISIBLE);
            File storageDir = Environment.getExternalStoragePublicDirectory("Riham");
            strBeforeImage = storageDir.getPath() + "/" + mPlanogramImages.getBack_image();
            binding.image1.setImageURI(Uri.parse(strBeforeImage));

            strAfterImage = storageDir.getPath() + "/" + mPlanogramImages.getFront_image();
            binding.layoutImage2.setVisibility(View.GONE);
            binding.image2.setVisibility(View.VISIBLE);
            binding.image2.setImageURI(Uri.parse(strAfterImage));

            binding.edtPlanogramComment.setText(mPlanogramImages.getComment());
            binding.layoutSave.setVisibility(View.GONE);
        }

        binding.layoutImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBefore = true;
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    photoFile = createImageFile();
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.layoutImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBefore = false;
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    photoFile = createImageFile();
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ViewPagerAdaptor viewPagerAdapter = new ViewPagerAdaptor(me, arrPagerImage);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.dotsIndicators.setViewPager(binding.viewPager);
        binding.layoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strBeforeImage.equalsIgnoreCase("") || strAfterImage.equalsIgnoreCase("")) {
                    UtilApp.displayAlert(AddPlanogramActivity.this, "Please capture before and after image first!");
                } else {

                    UtilApp.confirmationDialog("Are you sure you want to submit?",AddPlanogramActivity.this, new OnSearchableDialog() {
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
        String fileAfter = strAfterImage.substring(strAfterImage.lastIndexOf("/") + 1);
        String fileBefore = strBeforeImage.substring(strBeforeImage.lastIndexOf("/") + 1);
        mPlanogramImages.setFront_image(fileAfter);
        mPlanogramImages.setBack_image(fileBefore);
        mPlanogramImages.setComment(binding.edtPlanogramComment.getText().toString());
        App.isPlanogramSync = false;
        db.updatePlanogram(ID, mPlanogramImages);

        if (UtilApp.isNetworkAvailable(getApplicationContext())) {
            UtilApp.createBackgroundJob(getApplicationContext());
        }

        String strPlanoId = "PLN" + mPlanogramImages.getPlanogramId() + mPlanogramImages.getDistribution_tool_id();
        //INSERT TRANSACTION
        Transaction transaction = new Transaction();
        transaction.tr_type = Constant.TRANSACTION_TYPES.TT_PLANOGRAM;
        transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
        transaction.tr_customer_num = "";
        transaction.tr_customer_name = "";
        transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
        transaction.tr_invoice_id = "";
        transaction.tr_order_id = strPlanoId;
        transaction.tr_collection_id = "";
        transaction.tr_pyament_id = "";
        transaction.tr_printData = "";
        transaction.tr_is_posted = "No";

        db.insertTransaction(transaction);

        new AlertDialog.Builder(AddPlanogramActivity.this)
                .setMessage("Planogram added successfully")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {

            if (isBefore) {
                isBefore = false;
                String filePat = UtilApp.compressImage(AddPlanogramActivity.this, mCurrentPhotoPath);
                binding.layoutImage1.setVisibility(View.GONE);
                binding.image1.setVisibility(View.VISIBLE);
                binding.image1.setImageURI(Uri.parse(filePat));
                strBeforeImage = filePat;
            } else {
                String filePat = UtilApp.compressImage(AddPlanogramActivity.this, mCurrentPhotoPath);
                binding.layoutImage2.setVisibility(View.GONE);
                binding.image2.setVisibility(View.VISIBLE);
                binding.image2.setImageURI(Uri.parse(filePat));
                strAfterImage = filePat;
            }
        }

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mFilePath = "Riham_" + timeStamp + ".jpg";
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

}
