package com.mobiato.sfa.merchandising;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityAssetsDetailBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.ASSETS_MODEL;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.PhotoFullPopupWindow;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AssetsDetailActivity extends BaseActivity {

    private ActivityAssetsDetailBinding binding;
    private DBManager db;
    private ArrayList<String> arrImage = new ArrayList<>();
    private ArrayList<String> arrImagePath = new ArrayList<>();
    private static final int TAKE_PHOTO_REQUEST = 1;
    private String mCurrentPhotoPath, mFilePath;
    private File photoFile;
    private ASSETS_MODEL mAssets = new ASSETS_MODEL();
    private Customer customer;
    private String assetsId = "", customerId = "";
    private ASSETS_MODEL assetsModel = new ASSETS_MODEL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssetsDetailBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Assets");

        assetsId = getIntent().getStringExtra("assetsId");
        customerId = getIntent().getStringExtra("customerId");
        db = new DBManager(this);
        customer = db.getCustomerDetail(customerId);
        assetsModel = db.getAssetsModel(assetsId);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (!assetsModel.getAssetsImage().equalsIgnoreCase("")) {
            Glide.with(me).load(assetsModel.getAssetsImage()).into(binding.viewPager);
        }

        binding.viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!assetsModel.getAssetsImage().equalsIgnoreCase("")) {
                    new PhotoFullPopupWindow(AssetsDetailActivity.this, R.layout.popup_photo_full, v, assetsModel.getAssetsImage(), null);
                }
            }
        });

        File storageDir = Environment.getExternalStoragePublicDirectory("Riham");
        arrImage = new ArrayList<>();
        if (!assetsModel.getAssetsImage1().equals("")) {
            arrImage.add(storageDir.getPath() + "/" + assetsModel.getAssetsImage1());
        }
        if (!assetsModel.getAssetsImage2().equals("")) {
            arrImage.add(storageDir.getPath() + "/" + assetsModel.getAssetsImage2());
        }
        if (!assetsModel.getAssetsImage3().equals("")) {
            arrImage.add(storageDir.getPath() + "/" + assetsModel.getAssetsImage3());
        }
        if (!assetsModel.getAssetsImage4().equals("")) {
            arrImage.add(storageDir.getPath() + "/" + assetsModel.getAssetsImage4());
        }

        if (!assetsModel.getAssetsFeedback().equals("")) {
            binding.edtFeedback.setText(assetsModel.getAssetsFeedback());
            binding.btnSubmit.setVisibility(View.GONE);
        }

        if (arrImage.size() > 0) {
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage);
            binding.viewPagerDot.setAdapter(viewPagerAdapter);
            binding.dotsImg.setViewPager(binding.viewPagerDot);

        }
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
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

                String Feedback = binding.edtFeedback.getText().toString();
                if (Feedback.isEmpty()) {
                    binding.edtFeedback.requestFocus();
                    binding.edtFeedback.setError("Enter Feedback");
                } else if (arrImage.size() == 0) {
                    UtilApp.displayAlert(AssetsDetailActivity.this, "Please insert assets images!");
                } else {

                    new AlertDialog.Builder(AssetsDetailActivity.this)
                            .setMessage("Are you sure you want to Submit?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();

                                    mAssets.setAssetsFeedback(binding.edtFeedback.getText().toString());
                                    if (arrImagePath.size() == 1) {
                                        mAssets.setAssetsImage1(arrImagePath.get(0));
                                        mAssets.setAssetsImage2("");
                                        mAssets.setAssetsImage3("");
                                        mAssets.setAssetsImage4("");
                                    } else if (arrImagePath.size() == 2) {
                                        mAssets.setAssetsImage1(arrImagePath.get(0));
                                        mAssets.setAssetsImage2(arrImagePath.get(1));
                                        mAssets.setAssetsImage3("");
                                        mAssets.setAssetsImage4("");
                                    } else if (arrImagePath.size() == 3) {
                                        mAssets.setAssetsImage1(arrImagePath.get(0));
                                        mAssets.setAssetsImage2(arrImagePath.get(1));
                                        mAssets.setAssetsImage3(arrImagePath.get(2));
                                        mAssets.setAssetsImage4("");
                                    } else if (arrImagePath.size() == 4) {
                                        mAssets.setAssetsImage1(arrImagePath.get(0));
                                        mAssets.setAssetsImage2(arrImagePath.get(1));
                                        mAssets.setAssetsImage3(arrImagePath.get(2));
                                        mAssets.setAssetsImage4(arrImagePath.get(3));
                                    }
                                    db.updateAssets(mAssets, customer.getCustomerId(), assetsId);
                                    App.isAssetsSync = false;

                                    String asstNo = "AST" + assetsId;
                                    //INSERT TRANSACTION
                                    Transaction transaction = new Transaction();
                                    transaction.tr_type = Constant.TRANSACTION_TYPES.TT_ASSETS;
                                    transaction.tr_date_time = UtilApp.getCurrentDate() + " " + UtilApp.getCurrentTime();
                                    transaction.tr_customer_num = "";
                                    transaction.tr_customer_name = "";
                                    transaction.tr_salesman_id = Settings.getString(App.SALESMANID);
                                    transaction.tr_invoice_id = "";
                                    transaction.tr_order_id = asstNo;
                                    transaction.tr_collection_id = "";
                                    transaction.tr_pyament_id = "";
                                    transaction.tr_printData = "";
                                    transaction.tr_is_posted = "No";

                                    db.insertTransaction(transaction);

                                    if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                                        UtilApp.createBackgroundJob(getApplicationContext());
                                    }

                                    new AlertDialog.Builder(AssetsDetailActivity.this)
                                            .setMessage("Assets added successfully")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finish();
                                                }
                                            })
                                            .show();


                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {

            String filePat = UtilApp.compressImage(AssetsDetailActivity.this, mCurrentPhotoPath);
            arrImage.add(filePat);
            arrImagePath.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage);
            binding.viewPagerDot.setAdapter(viewPagerAdapter);
            binding.dotsImg.setViewPager(binding.viewPagerDot);

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

//                mFilePath = image.getAbsolutePath();
        // Save a file: path for use with ACTION_VIEW intents
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assets_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent intent = new Intent(me, StockCheck_Questions.class);
                intent.putExtra("title", "Assets Surveys");
                intent.putExtra("customerId", customer.getCustomerId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
