package com.mobiato.sfa.merchandising;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityAddComplaintFeedbackBinding;
import com.mobiato.sfa.databinding.ListviewItemBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Complain;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddComplaintFeedbackActivity extends BaseActivity {

    public ActivityAddComplaintFeedbackBinding binding;
    private String Type = "";
    Complain mComplain = new Complain();
    DBManager db;
    ArrayList<String> arrImage = new ArrayList<>();
    ArrayList<String> arrImagePath = new ArrayList<>();
    ArrayList<Item> arrSubItem = new ArrayList<>();
    private static final int TAKE_PHOTO_REQUEST = 1;
    String mCurrentPhotoPath, mFilePath;
    private File photoFile;
    private ArrayList<String> arrPagerImage = new ArrayList<>();
    Complain mComplainItem = new Complain();
    Dialog listDialog;
    RecyclerView list1;
    String ItemID = "";
    private CommonAdapter<Item> mAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddComplaintFeedbackBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("ADD COMPLAINT/FEEDBACK");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        db = new DBManager(AddComplaintFeedbackActivity.this);
        Type = getIntent().getStringExtra("type");

        if (Type.equals("Details")) {
            setTitle("COMPLAINT/FEEDBACK DETAILS");
            String ID = getIntent().getStringExtra("id");
            mComplainItem = db.getComplainListByID(ID);
            binding.btnAddCompagin.setVisibility(View.GONE);
            binding.layoutImage.setEnabled(false);
            binding.edtAbout.setText(mComplainItem.getComplain_Feedback());
            binding.edtComplaint.setText(mComplainItem.getComplanin_Note());
            binding.txtBrand.setText(mComplainItem.getComplain_brand());
            binding.edtAbout.setEnabled(false);
            binding.edtComplaint.setEnabled(false);
            binding.txtBrand.setEnabled(false);
            if (!mComplainItem.getComplain_Image1().equalsIgnoreCase("")) {

                String path = "/mnt/sdcard/Riham/" + mComplainItem.getComplain_Image1();
                File image = new File(path);

                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                arrPagerImage.add(mCurrentPhotoPath);
            }
            if (!mComplainItem.getComplain_Image2().equalsIgnoreCase("")) {
                String path = "/mnt/sdcard/Riham/" + mComplainItem.getComplain_Image2();
                File image = new File(path);

                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                arrPagerImage.add(mCurrentPhotoPath);
            }
            if (!mComplainItem.getComplain_Image3().equalsIgnoreCase("")) {
                String path = "/mnt/sdcard/Riham/" + mComplainItem.getComplain_Image3();
                File image = new File(path);

                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                arrPagerImage.add(mCurrentPhotoPath);
            }
            if (!mComplainItem.getComplain_Image4().equalsIgnoreCase("")) {
                String path = "/mnt/sdcard/Riham/" + mComplainItem.getComplain_Image4();
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
        binding.txtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrSubItem.size() > 0) {
                    showdialog();
                }
            }
        });

        binding.txtBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddComplaintFeedbackActivity.this);
                builder.setTitle("Choose an Brand");

                String[] animals = {"CSD", "Juice", "Water", "Biscuit"};
                builder.setItems(animals, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // CSD
                                arrSubItem = new ArrayList<>();
                                arrSubItem = db.getSubItemList("2");
                                binding.txtBrand.setText(animals[which]);
                                break;
                            case 1: // Juice
                                arrSubItem = new ArrayList<>();
                                arrSubItem = db.getSubItemList("4");
                                binding.txtBrand.setText(animals[which]);
                                break;
                            case 2: // Water
                                arrSubItem = new ArrayList<>();
                                arrSubItem = db.getSubItemList("3");
                                binding.txtBrand.setText(animals[which]);
                                break;
                            case 3: // Biscuit
                                arrSubItem = new ArrayList<>();
                                arrSubItem = db.getSubItemList("1");
                                binding.txtBrand.setText(animals[which]);
                                break;

                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                    UtilApp.createBackgroundJob(getApplicationContext());
                }
                String About = binding.edtAbout.getText().toString();
                String Complain = binding.edtComplaint.getText().toString();
                if (About.isEmpty()) {
                    binding.edtAbout.requestFocus();
                    binding.edtAbout.setError("Enter About");
                } else if (Complain.isEmpty()) {
                    binding.edtComplaint.requestFocus();
                    binding.edtComplaint.setError("Enter Complain");
                } else {
                    mComplain = new Complain();
                    String cmpNo = UtilApp.getComplaintNo();
                    mComplain.setComplainId(cmpNo);
                    mComplain.setComplain_Feedback(binding.edtAbout.getText().toString());
                    mComplain.setComplain_brand(binding.txtBrand.getText().toString());
                    mComplain.setComplanin_Note(binding.edtComplaint.getText().toString());
                    mComplain.setItemId(ItemID);

                    if (arrImagePath.size() == 1) {
                        mComplain.setComplain_Image1(arrImagePath.get(0));
                        mComplain.setComplain_Image2("");
                        mComplain.setComplain_Image3("");
                        mComplain.setComplain_Image4("");
                    } else if (arrImagePath.size() == 2) {
                        mComplain.setComplain_Image1(arrImagePath.get(0));
                        mComplain.setComplain_Image2(arrImagePath.get(1));
                        mComplain.setComplain_Image3("");
                        mComplain.setComplain_Image4("");
                    } else if (arrImagePath.size() == 3) {
                        mComplain.setComplain_Image1(arrImagePath.get(0));
                        mComplain.setComplain_Image2(arrImagePath.get(1));
                        mComplain.setComplain_Image3(arrImagePath.get(2));
                        mComplain.setComplain_Image4("");
                    } else if (arrImagePath.size() == 4) {
                        mComplain.setComplain_Image1(arrImagePath.get(0));
                        mComplain.setComplain_Image2(arrImagePath.get(1));
                        mComplain.setComplain_Image3(arrImagePath.get(2));
                        mComplain.setComplain_Image4(arrImagePath.get(3));
                    }
                    db.insertCOMPLAIN(mComplain);
                    App.isComplaintSync = false;

                    //INSERT TRANSACTION
                    Transaction transaction = new Transaction();
                    transaction.tr_type = Constant.TRANSACTION_TYPES.TT_COMPLAINT_FEEDBACK;
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

                    new AlertDialog.Builder(AddComplaintFeedbackActivity.this)
                            .setMessage("Feedback added successfully")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(me, ComplaintListActivity.class);
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
            String filePat = UtilApp.compressImage(AddComplaintFeedbackActivity.this, mCurrentPhotoPath);
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

    private void showdialog() {
        listDialog = new Dialog(this);
        listDialog.setTitle("Select Item");
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.distribution_display_lit, null, false);
        listDialog.setContentView(v);
        listDialog.setCancelable(true);
        listDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        list1 = (RecyclerView) listDialog.findViewById(R.id.mylistview);
        TextView txtTitle = (TextView) listDialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Select Item");

        setMainAdapter1();

        listDialog.show();
    }

    private void setMainAdapter1() {

        mAdapter1 = new CommonAdapter<Item>(arrSubItem) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof ListviewItemBinding) {
                    ((ListviewItemBinding) holder.binding).tvtextItem.setText(arrSubItem.get(position).getItemName());
                    holder.binding.executePendingBindings();
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listDialog.dismiss();

                        binding.txtItem.setText(arrSubItem.get(position).getItemName());
                        ItemID = arrSubItem.get(position).getItemId();

                    }
                });
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.listview_item;
            }
        };

        list1.setAdapter(mAdapter1);
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
