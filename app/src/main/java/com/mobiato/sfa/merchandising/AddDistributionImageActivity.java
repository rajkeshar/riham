package com.mobiato.sfa.merchandising;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityAddDistributionImageBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.utils.UtilApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDistributionImageActivity extends BaseActivity {
    private ActivityAddDistributionImageBinding binding;

    private DBManager db;
    private static final int TAKE_PHOTO_REQUEST = 1;
    String mCurrentPhotoPath, mFilePath;
    private File photoFile;
    private String toolId = "", custId = "", catId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDistributionImageBinding.inflate(getLayoutInflater(),baseBinding.frmContainer,true);
        me = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        db = new DBManager(this);
        setTitle("Distribution Images");

        toolId = getIntent().getStringExtra("ToolId");
        custId = getIntent().getStringExtra("CustomerId");
        catId = getIntent().getStringExtra("catId");

        if (db.isDistributionImage(custId, toolId, catId)) {
            binding.btnAddImage.setVisibility(View.GONE);
            binding.btnSubmit.setVisibility(View.INVISIBLE);
            binding.btnSubmit.setEnabled(false);
            binding.btnSubmit.setClickable(false);
        } else {
            binding.btnAddImage.setVisibility(View.VISIBLE);
            binding.btnSubmit.setVisibility(View.VISIBLE);
        }
        binding.layoutPager.setVisibility(View.VISIBLE);

        ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, DiskAreaCheck.arrImageS);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.dotsIndicators.setViewPager(binding.viewPager);

        binding.btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DiskAreaCheck.arrImageS.size() < 4) {
                    dispatchTakePictureIntent();
                }
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DiskAreaCheck.arrImageDisk.size() > 0) {
                    db.insertDistributionItem(DiskAreaCheck.arrImageDisk, toolId, custId, catId);
                }

                Toast.makeText(AddDistributionImageActivity.this, "Distribution image submitted successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddDistributionImageActivity.this, mCurrentPhotoPath);

            DiskAreaCheck.arrImageS.add(filePat);
            DiskAreaCheck.arrImageDisk.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, DiskAreaCheck.arrImageS);
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

}
