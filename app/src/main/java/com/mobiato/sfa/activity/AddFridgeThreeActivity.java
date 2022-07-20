package com.mobiato.sfa.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.mobiato.sfa.Adapter.ComplaintPagerAdaptor;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityAddFridgeThreeBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Chiller_Model;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddFridgeThreeActivity extends BaseActivity implements View.OnClickListener {

    public ActivityAddFridgeThreeBinding binding;
    private String strFridge = "", custNum = "", strType = "", strCustTypeId = "", strCustCategoryId = "", strCType = "";
    private Salesman mSalesman;
    private DBManager db;
    private ArrayList<String> arrFridge = new ArrayList<>();
    private Customer mCustomer;
    public String route_id = "0";
    public String sales_id = "0";
    public String depot_id = "0";
    public Bitmap bmpSign_bh;
    public Bitmap bmpSign_bh_sa;
    File file_bh, file_sals;

    private File photoFile;
    private static final int TAKE_PHOTO_REQUEST = 1;
    private String mCurrentPhotoPath, mFilePath;
    private ArrayList<String> arrImage = new ArrayList<>();
    private ArrayList<String> arrImagePath = new ArrayList<>();

    private File photoFile_passport;
    private static final int TAKE_PHOTO_PASSPORT_REQUEST = 2;
    private ArrayList<String> arrImage_Passport = new ArrayList<>();
    private ArrayList<String> arrImagePath_Passport = new ArrayList<>();

    private File photoFile_proof;
    private static final int TAKE_PHOTO_PROOF_REQUEST = 3;
    private ArrayList<String> arrImage_Proof = new ArrayList<>();
    private ArrayList<String> arrImagePath_Proof = new ArrayList<>();

    private File photoFile_outlook;
    private static final int TAKE_PHOTO_OUTLOOK_REQUEST = 4;
    private ArrayList<String> arrImage_Outlook = new ArrayList<>();
    private ArrayList<String> arrImagePath_Outlook = new ArrayList<>();

    private File photoFile_lc;
    private static final int TAKE_PHOTO_LC_REQUEST = 5;
    private ArrayList<String> arrImage_lc = new ArrayList<>();
    private ArrayList<String> arrImagePath_lc = new ArrayList<>();

    private File photoFile_treading;
    private static final int TAKE_PHOTO_TREADING_REQUEST = 6;
    private ArrayList<String> arrImage_treading = new ArrayList<>();
    private ArrayList<String> arrImagePath_treading = new ArrayList<>();

    private File photoFile_chiller;
    private static final int TAKE_PHOTO_CHILLER_REQUEST = 7;
    private ArrayList<String> arrImage_chiller = new ArrayList<>();
    private ArrayList<String> arrImagePath_chiller = new ArrayList<>();

    Chiller_Model mPromotion = new Chiller_Model();

    RadioButton radioButton_Nation;
    RadioButton radioPassport;
    RadioButton radioProof;
    RadioButton radioOutletStemp;
    RadioButton radioLCletter;
    RadioButton radioTrading;

    String ownername, outlettype, existing_cooler, stock, currentsale, expectedsale, chillersize, grill, specify_if_other_type, address, location;
    String number = "";
    String landmark = "";
    String display_location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFridgeThreeBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Add Chiller Form");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        db = new DBManager(this);
        mCustomer = (Customer) getIntent().getSerializableExtra("customer");
        custNum = UtilApp.getLastIndex("Customer");
//        binding.layChillerImg.setVisibility(View.GONE);
//        binding.layQrname.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            //  return;
        }

        Intent intent = getIntent();
        try {
            outlettype = intent.getStringExtra("outlettype");
        } catch (Exception e) {
        }

        try {
            ownername = intent.getStringExtra("ownername");
        } catch (Exception e) {
        }
        try {
            existing_cooler = intent.getStringExtra("existing_cooler");
        } catch (Exception e) {
        }

        try {
            stock = intent.getStringExtra("stock");
        } catch (Exception e) {
        }

        try {
            specify_if_other_type = intent.getStringExtra("specify_if_other_type");
        } catch (Exception e) {
        }

        try {
            currentsale = intent.getStringExtra("currentsale");
        } catch (Exception e) {
        }

        try {
            expectedsale = intent.getStringExtra("expectedsale");
        } catch (Exception e) {
        }

        try {
            chillersize = intent.getStringExtra("chillersize");
        } catch (Exception e) {
        }

        try {
            grill = intent.getStringExtra("grill");
        } catch (Exception e) {
        }

        try {
            address = intent.getStringExtra("address");
        } catch (Exception e) {
        }

        try {
            location = intent.getStringExtra("location");
        } catch (Exception e) {
        }

        try {
            number = intent.getStringExtra("number");
        } catch (Exception e) {
        }

        try {
            landmark = intent.getStringExtra("landmark");
        } catch (Exception e) {
        }
        try {
            display_location = intent.getStringExtra("display_location");
        } catch (Exception e) {

        }


        baseBinding.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        route_id = Settings.getString(App.ROUTEID);
        sales_id = Settings.getString(App.SALESMANID);
        depot_id = Settings.getString(App.DEPOTID);

        binding.etBhSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //str_signature = "1";
                showCustomerSignCapture("Customer");
            }
        });

        binding.etBhSaSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //str_signature = "1";
                showCustomerSignCapture("Salesman");
            }
        });


        //mSalesman = db.getSalesmanDetail(Settings.getString(App.SALESMANID));
        binding.btnTransferNext.setOnClickListener(this);
        binding.btnTransferPre.setOnClickListener(this);
        binding.btnAddNationalId.setOnClickListener(this);
        binding.btnAddPassport.setOnClickListener(this);
        binding.btnAddProof.setOnClickListener(this);
        binding.btnAddOutlook.setOnClickListener(this);
        binding.btnAddLC.setOnClickListener(this);
        binding.btnAddTreading.setOnClickListener(this);
        binding.btnAddChiller.setOnClickListener(this);

        binding.raYesId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layNationalImg.setVisibility(View.VISIBLE);
            }
        });
        binding.raNoId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layNationalImg.setVisibility(View.GONE);
            }
        });

        binding.raYesPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layPassportImg.setVisibility(View.VISIBLE);
            }
        });
        binding.raNoPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layPassportImg.setVisibility(View.GONE);
            }
        });

        binding.raYesProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layProofImg.setVisibility(View.VISIBLE);
            }
        });
        binding.raNoProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layProofImg.setVisibility(View.GONE);
            }
        });

        binding.raYesStemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layOutletImg.setVisibility(View.VISIBLE);
            }
        });
        binding.raNoStemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layOutletImg.setVisibility(View.GONE);
            }
        });

        binding.raYesLc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layLetterImg.setVisibility(View.VISIBLE);
            }
        });
        binding.raNoLc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layLetterImg.setVisibility(View.GONE);
            }
        });

        binding.raYesLc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layLetterImg.setVisibility(View.VISIBLE);
            }
        });
        binding.raNoLc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layLetterImg.setVisibility(View.GONE);
            }
        });

        binding.raYesTreding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layLicenceImg.setVisibility(View.VISIBLE);
            }
        });
        binding.raNoTreding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layLicenceImg.setVisibility(View.GONE);
            }
        });
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

    private void dispatchTakePassportIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_passport = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_passport));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_PASSPORT_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakeProofIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_proof = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_proof));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_PROOF_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakeOutlookIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_outlook = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_outlook));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_OUTLOOK_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakeLCIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_lc = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_lc));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_LC_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakeTreadingIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile_treading = createImageFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile_treading));
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_TREADING_REQUEST);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddChiller:
                if (arrImage_chiller.size() < 1) {
                    dispatchTakeChillerPictureIntent();
                }
                break;
            case R.id.btnAddNationalId:
                if (arrImage.size() < 2) {
                    dispatchTakePictureIntent();
                }
                //  dispatchTakePictureIntent();
                break;
            case R.id.btnAddPassport:
                if (arrImage_Passport.size() < 2) {
                    dispatchTakePassportIntent();
                }
                break;
            case R.id.btnAddProof:
                if (arrImage_Proof.size() < 2) {
                    dispatchTakeProofIntent();
                }
                break;
            case R.id.btnAddOutlook:
                if (arrImage_Outlook.size() < 2) {
                    dispatchTakeOutlookIntent();
                }
                break;
            case R.id.btnAddLC:
                if (arrImage_lc.size() < 2) {
                    dispatchTakeLCIntent();
                }
                break;
            case R.id.btnAddTreading:
                if (arrImage_treading.size() < 2) {
                    dispatchTakeTreadingIntent();
                }
                break;
            case R.id.btnTransferPre:
                onBackPressed();
                break;
            case R.id.btnTransferNext:
               /* startActivity(new Intent(ChillerRequestFormThreeActivity.this, ChillerRequestFormFourActivity.class));
                finish();*/
                String serionalNo = binding.edtSaleSerial.getText().toString();

                int selectedID = binding.radioNational.getCheckedRadioButtonId();
                radioButton_Nation = findViewById(selectedID);
                String nation_id = radioButton_Nation.getText().toString();

                int selectedID_radioPassport = binding.radioPassport.getCheckedRadioButtonId();
                radioPassport = findViewById(selectedID_radioPassport);
                String password_photo = radioPassport.getText().toString();

                //proof
                int selectedID_address = binding.radioProof.getCheckedRadioButtonId();
                radioProof = findViewById(selectedID_address);
                String outlet_address_proof = radioProof.getText().toString();
                //Stemp
                int selectedID_stemp = binding.radioOutletStemp.getCheckedRadioButtonId();
                radioOutletStemp = findViewById(selectedID_stemp);
                String outlet_stamp = radioOutletStemp.getText().toString();
                //LC Letter
                int selectedID_lc = binding.radioLCletter.getCheckedRadioButtonId();
                radioLCletter = findViewById(selectedID_lc);
                String lc_letter = radioLCletter.getText().toString();

                int selectedID_license = binding.radioTrading.getCheckedRadioButtonId();
                radioTrading = findViewById(selectedID_license);
                String trading_licence = radioTrading.getText().toString();

                if (serionalNo.isEmpty()) {
                    UtilApp.displayAlert(me, "Please enter serial no.");
                } else if (arrImagePath_chiller.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload chiller Image.");
                }
               /* else if (arrImagePath.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload National Id Image.");
                } else if (arrImagePath.size() != 2) {
                    UtilApp.displayAlert(me, "Please upload atleast 2 National Id Image.");
                } else if (arrImagePath_Passport.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload Passport Image.");
                } else if (arrImagePath_Outlook.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload Outlet Photo.");
                } else if (arrImagePath_lc.isEmpty() && arrImagePath_treading.isEmpty()) {
                    UtilApp.displayAlert(me, "Please upload LC Letter/Treding Licence Image.");
                } */
                else if (bmpSign_bh == null) {
                    UtilApp.displayAlert(me, "Please upload customer signature.");
                } else if (bmpSign_bh_sa == null) {
                    UtilApp.displayAlert(me, "Please upload salesman signature.");
                } else {

                    if (radioButton_Nation.getText().equals("")) {
                        nation_id = "NO";
                    }

                    if (radioPassport.getText().equals("")) {
                        password_photo = "NO";
                    }

                    if (radioProof.getText().equals("")) {
                        outlet_address_proof = "NO";
                    }

                    if (radioOutletStemp.getText().equals("")) {
                        outlet_stamp = "NO";
                    }

                    if (radioLCletter.getText().equals("")) {
                        lc_letter = "NO";
                    }

                    if (radioTrading.getText().equals("")) {
                        trading_licence = "NO";
                    }

                    mPromotion = new Chiller_Model();
                    mPromotion.setSerialNo(serionalNo);
                    mPromotion.setRoute_id(Settings.getString(App.ROUTEID));
                    mPromotion.setSalesman_id(Settings.getString(App.SALESMANID));
                    mPromotion.setCustomer_id(mCustomer.getCustomerId());
                    mPromotion.setdepot_id(depot_id);
                    mPromotion.setContact_number(number);
                    mPromotion.setPostal_address(address);
                    mPromotion.setLandmark(landmark);
                    mPromotion.setLocation(location);
                    mPromotion.setOutlet_type(outlettype);
                    mPromotion.setOwner_name(ownername);
                    mPromotion.setSpecify_if_other_type(specify_if_other_type);
                    mPromotion.setExisting_coolers(existing_cooler);
                    mPromotion.setStock_share_with_competitor(stock);
                    mPromotion.setOutlet_weekly_sale_volume(currentsale);
                    mPromotion.setOutlet_weekly_sales(expectedsale);
                    mPromotion.setChiller_size_requested(chillersize);
                    mPromotion.setChiller_safty_grill(grill);
                    mPromotion.setDisplay_location(display_location);

                    mPromotion.setNational_id(nation_id);
                    mPromotion.setPassword_photo(password_photo);
                    mPromotion.setOutlet_address_proof(outlet_address_proof);
                    mPromotion.setLc_letter(lc_letter);
                    mPromotion.setTrading_licence(trading_licence);
                    mPromotion.setOutlet_stamp(outlet_stamp);

                    if (arrImagePath.size() > 0) {
                        try {
                            mPromotion.setNational_id_file(arrImagePath.get(0));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setNational_id_file("");
                    }

                    if (arrImagePath.size() > 1) {
                        try {
                            mPromotion.setNational_id1_file(arrImagePath.get(1));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setNational_id1_file("");
                    }

                    if (arrImagePath_Passport.size() > 0) {
                        try {
                            mPromotion.setPassword_photo_file(arrImagePath_Passport.get(0));
                        } catch (Exception e) {
                        }

                    } else {
                        mPromotion.setPassword_photo_file("");
                    }

                    if (arrImagePath_Passport.size() > 1) {
                        try {
                            mPromotion.setPassword_photo1_file(arrImagePath_Passport.get(1));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setPassword_photo1_file("");
                    }

                    if (arrImagePath_Proof.size() > 0) {
                        try {
                            mPromotion.setOutlet_address_proof_file(arrImagePath_Proof.get(0));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setOutlet_address_proof_file("");
                    }


                    if (arrImagePath_Proof.size() > 1) {
                        try {
                            mPromotion.setOutlet_address_proof1_file(arrImagePath_Proof.get(1));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setOutlet_address_proof1_file("");
                    }

                    if (arrImagePath_Outlook.size() > 0) {
                        try {
                            mPromotion.setOutlet_stamp_file(arrImagePath_Outlook.get(0));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setOutlet_stamp_file("");
                    }


                    if (arrImagePath_Outlook.size() > 1) {
                        try {
                            mPromotion.setOutlet_stamp1_file(arrImagePath_Outlook.get(1));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setOutlet_stamp1_file("");
                    }

                    if (arrImagePath_lc.size() > 0) {
                        try {
                            mPromotion.setLc_letter_file(arrImagePath_lc.get(0));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setLc_letter_file("");
                    }

                    if (arrImagePath_lc.size() > 1) {
                        try {
                            mPromotion.setLc_letter1_file(arrImagePath_lc.get(1));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setLc_letter1_file("");
                    }

                    if (arrImagePath_treading.size() > 0) {
                        try {
                            mPromotion.setTrading_licence_file(arrImagePath_treading.get(0));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setTrading_licence_file("");
                    }

                    if (arrImagePath_treading.size() > 1) {
                        try {
                            mPromotion.setTrading_licence1_file(arrImagePath_treading.get(1));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setTrading_licence1_file("");
                    }

                    if (file_bh != null) {
                        mPromotion.setSign__customer_file(file_bh.getPath());
                    }

                    if (file_sals != null) {
                        mPromotion.setSalesmanSignature(file_sals.getPath());
                    }

                    if (arrImagePath_chiller.size() > 0) {
                        try {
                            mPromotion.setChillerImage(arrImagePath_chiller.get(0));
                        } catch (Exception e) {
                        }
                    } else {
                        mPromotion.setChillerImage("");
                    }

//                    MultipartBody.Part body1 = null;
//                    try {
//                        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_bh);
//                        mPromotion.setSign__customer_file(file_bh.getPath());
//                        body1 = MultipartBody.Part.createFormData("sign__customer_file", file_bh.getName(), reqFile);
//                    } catch (Exception e) {
//
//                    }

                    custNum = UtilApp.getChillerRequestNo();
                    db.insertChillerAddRequest(mCustomer, custNum, number, landmark, mPromotion);

                    Transaction transaction = new Transaction();
                    transaction.tr_type = Constant.TRANSACTION_TYPES.TT_CHILER_ADD;
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

                    //db.updateCustomerTransaction(custNum, "1", "");
                    //db.updateCustomerAddStatus(custNum, "1");

                    Intent intent = new Intent(me, CustomerDetailActivity.class);
                    intent.putExtra("custmer", mCustomer);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddFridgeThreeActivity.this, mCurrentPhotoPath);
            binding.layoutImage.setVisibility(View.GONE);
            binding.layoutPager.setVisibility(View.VISIBLE);
            binding.btnAddNationalId.setVisibility(View.VISIBLE);
            arrImage.add(filePat);
            arrImagePath.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage);
            binding.viewPager.setAdapter(viewPagerAdapter);
            binding.dotsIndicators.setViewPager(binding.viewPager);
        } else if (requestCode == TAKE_PHOTO_PASSPORT_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddFridgeThreeActivity.this, mCurrentPhotoPath);
            binding.layoutImagepassport.setVisibility(View.GONE);
            binding.layoutPassport.setVisibility(View.VISIBLE);
            binding.btnAddPassport.setVisibility(View.VISIBLE);
            arrImage_Passport.add(filePat);
            arrImagePath_Passport.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_Passport);
            binding.viewPagerPassport.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsPassport.setViewPager(binding.viewPager);
        } else if (requestCode == TAKE_PHOTO_PROOF_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddFridgeThreeActivity.this, mCurrentPhotoPath);
            binding.layoutImageproof.setVisibility(View.GONE);
            binding.layoutProof.setVisibility(View.VISIBLE);
            binding.btnAddProof.setVisibility(View.VISIBLE);
            arrImage_Proof.add(filePat);
            arrImagePath_Proof.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_Proof);
            binding.viewPagerProof.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsProof.setViewPager(binding.viewPager);
        } else if (requestCode == TAKE_PHOTO_OUTLOOK_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddFridgeThreeActivity.this, mCurrentPhotoPath);
            binding.layoutImageoutlook.setVisibility(View.GONE);
            binding.layoutOutlook.setVisibility(View.VISIBLE);
            binding.btnAddOutlook.setVisibility(View.VISIBLE);
            arrImage_Outlook.add(filePat);
            arrImagePath_Outlook.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_Outlook);
            binding.viewPagerOutlook.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsOutlook.setViewPager(binding.viewPager);
        } else if (requestCode == TAKE_PHOTO_LC_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddFridgeThreeActivity.this, mCurrentPhotoPath);
            binding.layoutImagelc.setVisibility(View.GONE);
            binding.layoutLc.setVisibility(View.VISIBLE);
            binding.btnAddLC.setVisibility(View.VISIBLE);
            arrImage_lc.add(filePat);
            arrImagePath_lc.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_lc);
            binding.viewPagerLc.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsLc.setViewPager(binding.viewPager);
        } else if (requestCode == TAKE_PHOTO_TREADING_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddFridgeThreeActivity.this, mCurrentPhotoPath);
            binding.layoutImagetreading.setVisibility(View.GONE);
            binding.layoutTreading.setVisibility(View.VISIBLE);
            binding.btnAddTreading.setVisibility(View.VISIBLE);
            arrImage_treading.add(filePat);
            arrImagePath_treading.add(filePat.substring(filePat.lastIndexOf("/") + 1));
            ComplaintPagerAdaptor viewPagerAdapter = new ComplaintPagerAdaptor(me, arrImage_treading);
            binding.viewPagerTreading.setAdapter(viewPagerAdapter);
            binding.dotsIndicatorsTreading.setViewPager(binding.viewPager);
        } else if (requestCode == TAKE_PHOTO_CHILLER_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddFridgeThreeActivity.this, mCurrentPhotoPath);
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

    private void showCustomerSignCapture(String type) {
        final Dialog dialog = new Dialog(AddFridgeThreeActivity.this);
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

                    if (type.equals("Customer")) {
                        bmpSign_bh = viewSign.getSignatureBitmap();
                        binding.etBhSignature.setImageBitmap(bmpSign_bh);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                saveImage(bmpSign_bh);
                            }
                        }, 1000);
                    } else {
                        bmpSign_bh_sa = viewSign.getSignatureBitmap();
                        binding.etBhSaSignature.setImageBitmap(bmpSign_bh_sa);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                saveSalesmanImage(bmpSign_bh_sa);
                            }
                        }, 1000);
                    }

                    //saveImage(bmpSign_bh);
                } else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddFridgeThreeActivity.this);
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

    final void saveImage(Bitmap signature) {

        String root = Environment.getExternalStorageDirectory().toString();

        // the directory where the signature will be saved
        File myDir = new File(root + "/saved_signature");

        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        //create a file to write bitmap data
        String fname_bh = "sa_signature_bh.png";
        file_bh = new File(getApplicationContext().getCacheDir(), fname_bh);
        try {
            file_bh.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        Bitmap bitmap = signature;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file_bh);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    final void saveSalesmanImage(Bitmap signature) {

        String root = Environment.getExternalStorageDirectory().toString();

        // the directory where the signature will be saved
        File myDir = new File(root + "/saved_signature");

        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        //create a file to write bitmap data
        String fname_bh = "sam_signature_bh.png";
        file_sals = new File(getApplicationContext().getCacheDir(), fname_bh);
        try {
            file_sals.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        Bitmap bitmap = signature;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file_sals);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}