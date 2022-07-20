package com.mobiato.sfa.merchandising;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityDiskAreaCheckBinding;
import com.mobiato.sfa.databinding.RowDiskareaBinding;
import com.mobiato.sfa.databinding.RowExpiryBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.Distribution;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DiskAreaCheck extends BaseActivity {

    private ActivityDiskAreaCheckBinding binding;
    private ArrayList<Distribution> arrData = new ArrayList<>();
    private CommonAdapter<Distribution> mAdapter;
    private CommonAdapter<Distribution> mAdapter1;
    ArrayList<Distribution> arrExpiry = new ArrayList<>();
    ArrayList<Distribution> arrShowExpiry = new ArrayList<>();
    ArrayList<Distribution> arrDamage = new ArrayList<>();
    private DBManager db;
    Customer customer;
    Item item;
    boolean IsClick = false, IsExpiry = false, IsImageClick = false;
    String ID = "", catId = "", customerId = "", Item_Id = "";
    int mDay, mMonth, mYear;
    int SalableQty = 0;
    private File photoFile;
    private static final int TAKE_PHOTO_REQUEST = 1;
    String mCurrentPhotoPath, mFilePath, strImageDisk;
    public static ArrayList<String> arrImageDisk = new ArrayList<>();
    public static ArrayList<String> arrImageS = new ArrayList<>();
    RecyclerView rvExpryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiskAreaCheckBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        String Title = getIntent().getStringExtra("title");
        ID = getIntent().getStringExtra("id");
        catId = getIntent().getStringExtra("item_id");
        customerId = getIntent().getStringExtra("customer_id");
        setTitle(Title);

        db = new DBManager(this);
        customer = db.getCustomerDetail(customerId);
        System.out.println("Customer Details: " + customer);

        arrData = new ArrayList<>();
        arrExpiry = new ArrayList<>();
        arrData = db.getDistributionListByName(ID, catId, customer.getCustomerId());

        setMainAdapter();

        String code = customer.getCustomerCode();
        binding.txtTitleId.setText("[" + code + "] ");
        binding.txtTitle.setText(customer.getCustomerName());

        boolean isFill = false;
        for (int i = 0; i < arrData.size(); i++) {
            if (!arrData.get(i).getFillQty().equalsIgnoreCase("")) {
                isFill = true;
                break;
            }
        }

        if (isFill) {
            binding.layoutSubmit.setVisibility(View.GONE);
        }

        binding.layoutSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!IsClick) {
                    new AlertDialog.Builder(DiskAreaCheck.this)
                            .setMessage("Please insert alteast one item")
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .show();
                } else {

                    new AlertDialog.Builder(DiskAreaCheck.this)
                            .setMessage("Are you sure you want to Submit?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    new submitData().execute();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }

            }
        });


    }

    private void setMainAdapter() {

        mAdapter = new CommonAdapter<Distribution>(arrData) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowDiskareaBinding) {
                    ((RowDiskareaBinding) holder.binding).setItem(arrData.get(position));
                    ((RowDiskareaBinding) holder.binding).txtTitle.setText(arrData.get(position).getItem_Name());
                    ((RowDiskareaBinding) holder.binding).txtTitleCode.setText("[" + arrData.get(position).getAlternate_Code() + "]");
                    ((RowDiskareaBinding) holder.binding).edtUom.setText(arrData.get(position).getUOM());
                    ((RowDiskareaBinding) holder.binding).edtModel.setText(arrData.get(position).getQTY());

                    holder.setIsRecyclable(false);

                    if (!arrData.get(position).getGoodSaleQty().equalsIgnoreCase("")) {
                        ((RowDiskareaBinding) holder.binding).edtSaleable.setText("" + arrData.get(position).getGoodSaleQty());
                    }

                    holder.binding.executePendingBindings();
                    ((RowDiskareaBinding) holder.binding).edtSaleable.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //yourFunction();
                            int modQty = Integer.parseInt(((RowDiskareaBinding) holder.binding).edtModel.getText().toString());

                            if (((RowDiskareaBinding) holder.binding).edtSaleable.getText().toString().equals("")) {
                                IsClick = false;
                                //((RowDiskareaBinding) holder.binding).edtFill.setText("");
                            }
                            if (!s.toString().equalsIgnoreCase("")) {
                                IsClick = true;
                                arrData.get(position).setFillQty(((RowDiskareaBinding) holder.binding).edtSaleable.getText().toString());
                                arrData.get(position).setGoodSaleQty(((RowDiskareaBinding) holder.binding).edtSaleable.getText().toString());
                            } else {
                                arrData.get(position).setFillQty(((RowDiskareaBinding) holder.binding).edtSaleable.getText().toString());
                                arrData.get(position).setGoodSaleQty(((RowDiskareaBinding) holder.binding).edtSaleable.getText().toString());
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    ((RowDiskareaBinding) holder.binding).layoutExeDt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!((RowDiskareaBinding) holder.binding).edtSaleable.getText().toString().equalsIgnoreCase("")) {
                                if (Integer.valueOf(((RowDiskareaBinding) holder.binding).edtSaleable.getText().toString()) == 0) {
                                    UtilApp.displayAlert(DiskAreaCheck.this, "Please insert good saleable qty");
                                } else {
                                    SalableQty = Integer.parseInt(((RowDiskareaBinding) holder.binding).edtSaleable.getText().toString());
                                    Item_Id = arrData.get(position).getItem_Id();
                                    ShowExpiryDate(me, ((RowDiskareaBinding) holder.binding).edtUom.getText().toString(), arrData.get(position).getItem_Name(), "[" + arrData.get(position).getAlternate_Code() + "]");
                                }
                            } else {
                                UtilApp.displayAlert(DiskAreaCheck.this, "Please first insert good saleable qty");
                            }
                        }
                    });

                    ((RowDiskareaBinding) holder.binding).layoutDer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Item_Id = arrData.get(position).getItem_Id();
                            showDerDialog(me, arrData.get(position).getItem_Name(), "[" + arrData.get(position).getAlternate_Code() + "]");
                        }
                    });

                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_diskarea;
            }
        };

        binding.rvdiskList.setAdapter(mAdapter);
    }

    private class submitData extends AsyncTask<Void, Void, Boolean> {
        private LoadingSpinner mDialog;
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new LoadingSpinner(DiskAreaCheck.this);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            ArrayList<Distribution> arrFillData = new ArrayList<>();
            for (int i = 0; i < arrData.size(); i++) {
                Distribution mItem = arrData.get(i);
                if (!mItem.getFillQty().equalsIgnoreCase("")) {
                    arrFillData.add(mItem);
                }
            }

            if (arrFillData.size() == arrData.size()) {
                isSuccess = true;

                if (arrExpiry.size() > 0) {

                    String cmpNo = "EXP" + customer.getCustomerId() + UtilApp.getTimeStamp();

                    System.out.println(arrExpiry);
                    db.insertExpiryItemList(arrExpiry, cmpNo, customer.getCustomerId(), catId);

                    //INSERT TRANSACTION
                    Transaction transaction = new Transaction();
                    transaction.tr_type = Constant.TRANSACTION_TYPES.TT_EXPIRYITEM;
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

                }
                if (arrDamage.size() > 0) {

                    String cmpNo = "DER" + customer.getCustomerId() + UtilApp.getTimeStamp();

                    System.out.println(arrDamage);
                    db.insertDamagedItemList(arrDamage, cmpNo, customer.getCustomerId(), catId);

                    //INSERT TRANSACTION
                    Transaction transaction = new Transaction();
                    transaction.tr_type = Constant.TRANSACTION_TYPES.TT_DERITEM;
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
                }

                //update distribution Data
                db.updateDISTRIBUTION(arrData, customer.getCustomerId());

            } else {
                isSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (mDialog.isShowing()) {
                mDialog.hide();
            }

            if (isSuccess) {

                IsClick = true;
                binding.layoutSubmit.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();

                if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                    UtilApp.createBackgroundJob(getApplicationContext());
                }

                Toast.makeText(getApplicationContext(), "Data submitted successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                UtilApp.displayAlert(DiskAreaCheck.this, "Please fill data in all items!");
            }
        }
    }


    public void ShowExpiryDate(Activity activity, String UOM, String Name, String Code) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_expiray);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.95);
        dialog.getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout layoutAdd = (LinearLayout) dialog.findViewById(R.id.layoutAdd);
        LinearLayout layoutCancel = (LinearLayout) dialog.findViewById(R.id.layoutCancel);
        LinearLayout layoutShow = (LinearLayout) dialog.findViewById(R.id.layoutShow);
        TextView edtUom = (TextView) dialog.findViewById(R.id.edtUom);
        TextView txtTitleId = (TextView) dialog.findViewById(R.id.txtTitleId);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        EditText edtPC = (EditText) dialog.findViewById(R.id.edtPC);
        TextView edtExpiry = (TextView) dialog.findViewById(R.id.edtExpiry);
        LinearLayout layoutExp = (LinearLayout) dialog.findViewById(R.id.lytExpiry);
        rvExpryList = (RecyclerView) dialog.findViewById(R.id.rvExpiryList);
        LinearLayout layoutShowItem = (LinearLayout) dialog.findViewById(R.id.layoutShowItem);
        FrameLayout frameExpiry = (FrameLayout) dialog.findViewById(R.id.frameExpiry);
        TextView btnAdd_Expry = (TextView) dialog.findViewById(R.id.btnAddExpiry);

        edtUom.setText(UOM);
        txtTitleId.setText(Code);
        txtTitle.setText(Name);
        btnAdd_Expry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutAdd.setEnabled(true);
                layoutAdd.setAlpha(1f);
                frameExpiry.setVisibility(View.GONE);
                layoutShowItem.setVisibility(View.VISIBLE);

            }
        });

        edtPC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //yourFunction();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtPC.getText().toString().equals("")) {

                } else {
//                    if (Integer.valueOf(edtPC.getText().toString()) > SalableQty) {
//                        Toast.makeText(getApplicationContext(), "Please insert less than value from" + SalableQty, Toast.LENGTH_LONG).show();
//                        edtPC.setText("");
//                    } else {
//
//                    }
                }


            }
        });
        layoutExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd1 = new DatePickerDialog(DiskAreaCheck.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String dayOfMonths = "";
                                String monthOfYears = "";
                                if (dayOfMonth < 10) {
                                    dayOfMonths = "0" + String.valueOf(dayOfMonth);
                                } else {
                                    dayOfMonths = String.valueOf(dayOfMonth);
                                }
                                if (monthOfYear < 9) {
                                    monthOfYears = "0" + String.valueOf(monthOfYear + 1);
                                } else {
                                    monthOfYears = String.valueOf(monthOfYear + 1);
                                }
                                String strDob = year + "/" + monthOfYears + "/" + (dayOfMonths);
                                edtExpiry.setText(strDob);
                            }
                        }, mYear, mMonth, mDay);

                dpd1.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dpd1.show();

            }
        });

        layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameExpiry.setVisibility(View.GONE);
                layoutShowItem.setVisibility(View.VISIBLE);
                if (edtPC.getText().toString().equals("")) {
                    Toast.makeText(DiskAreaCheck.this, "Please insert PC", Toast.LENGTH_SHORT).show();
                }  else {
                    IsExpiry = true;
                    Distribution mdistribution = new Distribution();
                    mdistribution.setDistribution_Tool_Id(ID);
                    mdistribution.setItem_Id(Item_Id);
                    mdistribution.setSalesmanId(Settings.getString(App.SALESMANID));
                    mdistribution.setUOM(edtUom.getText().toString());
                    mdistribution.setPC(edtPC.getText().toString());
                    mdistribution.setExpiryDate(edtExpiry.getText().toString());
                    arrExpiry.add(mdistribution);

                    Toast.makeText(DiskAreaCheck.this, "Expiry added successfully!", Toast.LENGTH_SHORT).show();
                    edtPC.setText("");
                    edtExpiry.setText("");
                }

            }
        });
        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        layoutShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutShowItem.setVisibility(View.GONE);
                rvExpryList.setVisibility(View.VISIBLE);
                frameExpiry.setVisibility(View.VISIBLE);
                layoutAdd.setEnabled(false);
                layoutAdd.setAlpha(0.5f);
                arrShowExpiry = new ArrayList<>();
                arrShowExpiry = getItemExpiry(Item_Id);
                setMainAdapter1();

            }
        });
        dialog.show();

    }

    private ArrayList<Distribution> getItemExpiry(String itemid) {

        ArrayList<Distribution> arrData = new ArrayList<>();
        for (int i = 0; i < arrExpiry.size(); i++) {

            if (arrExpiry.get(i).getItem_Id().equalsIgnoreCase(itemid))
                arrData.add(arrExpiry.get(i));
        }

        return arrData;
    }

    private void setMainAdapter1() {

        mAdapter1 = new CommonAdapter<Distribution>(arrShowExpiry) {
            @Override
            public void onBind(CommonAdapter.CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowExpiryBinding) {
                    ((RowExpiryBinding) holder.binding).setItem(arrShowExpiry.get(position));
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_expiry;
            }
        };

        rvExpryList.setAdapter(mAdapter1);
    }

    public void showDerDialog(Activity activity, String Name, String Code) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_der);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.95);
        dialog.getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);


        LinearLayout layoutAdd = (LinearLayout) dialog.findViewById(R.id.layoutAdd);
        LinearLayout layoutCancel = (LinearLayout) dialog.findViewById(R.id.layoutCancel);
        LinearLayout layoutShow = (LinearLayout) dialog.findViewById(R.id.layoutShow);
        TextView txtTitleId = (TextView) dialog.findViewById(R.id.txtTitleId);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        EditText edtUom = (EditText) dialog.findViewById(R.id.edtDamegedItem);
        EditText edtPC = (EditText) dialog.findViewById(R.id.edtExpiredItem);
        EditText edtSaleableItem = (EditText) dialog.findViewById(R.id.edtSaleableItem);
        txtTitleId.setText(Code);
        txtTitle.setText(Name);
        layoutShow.setVisibility(View.GONE);

        if (arrDamage.size() > 0) {
            edtUom.setText(arrDamage.get(0).getUOM());
            edtPC.setText(arrDamage.get(0).getPC());
            edtSaleableItem.setText(arrDamage.get(0).getExpiryDate());
        }

        layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (arrDamage.size() > 0) {
                    arrDamage.clear();
                }
                Distribution mdistribution = new Distribution();
                mdistribution.setDistribution_Tool_Id(ID);
                mdistribution.setItem_Id(Item_Id);
                mdistribution.setSalesmanId(Settings.getString(App.SALESMANID));
                mdistribution.setUOM(edtUom.getText().toString());
                mdistribution.setPC(edtPC.getText().toString());
                mdistribution.setExpiryDate(edtSaleableItem.getText().toString());
                arrDamage.add(mdistribution);

            }
        });
        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


            }
        });
        layoutShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


            }
        });
        dialog.show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            IsImageClick = true;
            String filePat = UtilApp.compressImage(DiskAreaCheck.this, mCurrentPhotoPath);
            strImageDisk = filePat;
            arrImageS.add(filePat);
            arrImageDisk.add(strImageDisk.substring(strImageDisk.lastIndexOf("/") + 1));
            Intent intent1 = new Intent(DiskAreaCheck.this, AddDistributionImageActivity.class);
            intent1.putExtra("ToolId", ID);
            intent1.putExtra("CustomerId", customer.getCustomerId());
            intent1.putExtra("catId", catId);
            startActivity(intent1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_disk_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent intent = new Intent(DiskAreaCheck.this, StockCheck_Questions.class);
                intent.putExtra("title", "Distribution Surveys");
                intent.putExtra("toolId", ID);
                intent.putExtra("customerId", customer.getCustomerId());
                startActivity(intent);
                return true;
            case R.id.action_image:

                if (arrImageDisk.size() < 4) {
                    if (IsImageClick) {
                        Intent intent1 = new Intent(DiskAreaCheck.this, AddDistributionImageActivity.class);
                        intent1.putExtra("ToolId", ID);
                        intent1.putExtra("CustomerId", customer.getCustomerId());
                        intent1.putExtra("catId", catId);
                        startActivity(intent1);
                    } else {
                        dispatchTakePictureIntent();

                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
