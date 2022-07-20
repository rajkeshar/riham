package com.mobiato.sfa.merchandising;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.mobiato.sfa.Adapter.CommonAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityAddPromotionalBinding;
import com.mobiato.sfa.databinding.RowItemCategoryBinding;
import com.mobiato.sfa.databinding.RowItemPramotionBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Category;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Promotion;
import com.mobiato.sfa.model.Promotion_Item;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class AddPromotionalActivity extends BaseActivity implements View.OnClickListener {

    private ActivityAddPromotionalBinding binding;
    private DBManager db;
    public ArrayList<Category> arrCategory = new ArrayList<>();
    public ArrayList<Item> arrCSD = new ArrayList<>();
    public ArrayList<Item> arrBiscuit = new ArrayList<>();
    public ArrayList<Item> arrJuice = new ArrayList<>();
    public ArrayList<Item> arrWater = new ArrayList<>();
    public ArrayList<Item> arrItem = new ArrayList<>();
    public ArrayList<Item> arrSales = new ArrayList<>();

    private CommonAdapter<Category> mCategoryAdapter;
    private CommonAdapter<Item> mAdapter;

    int catPosition = 0;
    private int countPlus = 0;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private static final int TAKE_PHOTO_REQUEST = 1;
    String mCurrentPhotoPath, mFilePath, strImagePath = "";
    private File photoFile;
    private double invAmt = 0;
    int itemPosition = 0;
    Promotion_Item mPromotionItem = new Promotion_Item();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPromotionalBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("PROMOTIONAL ACCOUNTABILITY");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        db = new DBManager(AddPromotionalActivity.this);

        binding.stepView.setStepsNumber(3);
        binding.stepView.go(countPlus, true);

        registerReceiver(broadcastReceiver, new IntentFilter(InputDialogPromotionActivity.BROADCAST_ACTION));

        setCategory();
        setCategoryAdapter();

        arrCSD = new ArrayList<>();
        arrBiscuit = new ArrayList<>();
        arrJuice = new ArrayList<>();
        arrWater = new ArrayList<>();

        arrCSD = db.getItemListByCategory("2");
        arrBiscuit = db.getItemListByCategory("1");
        arrJuice = db.getItemListByCategory("4");
        arrWater = db.getItemListByCategory("3");
        getItemList(catPosition);


        binding.btnNext.setOnClickListener(this);
        binding.btnPrevious.setOnClickListener(this);
        binding.layoutImage.setOnClickListener(this);

    }

    private void setCategory() {
        arrCategory = new ArrayList<>();
        Category c1 = new Category();
        c1.name = "CSD";
        c1.catId = "2";
        c1.is_click = "1";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Juice";
        c1.catId = "4";
        c1.is_click = "0";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Water";
        c1.catId = "3";
        c1.is_click = "0";
        arrCategory.add(c1);

        c1 = new Category();
        c1.name = "Biscuit";
        c1.catId = "1";
        c1.is_click = "0";
        arrCategory.add(c1);

    }

    private void getItemList(int catPosition) {
        arrItem = new ArrayList<>();
        switch (catPosition) {
            case 0:
                arrItem = arrCSD;
                break;
            case 1:
                arrItem = arrJuice;
                break;
            case 2:
                arrItem = arrWater;
                break;
            case 3:
                arrItem = arrBiscuit;
                break;
        }

        setAdapter();


    }

    private void setCategoryAdapter() {

        mCategoryAdapter = new CommonAdapter<Category>(arrCategory) {

            @Override
            public void onBind(CommonViewHolder holder, int position) {

                if (holder.binding instanceof RowItemCategoryBinding) {
                    ((RowItemCategoryBinding) holder.binding).tvCategory.setText(arrCategory.get(position).name);

                    if (arrCategory.get(position).is_click.equals("1")) {
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setBackgroundResource(R.drawable.rounded_rect_blue_capsule);
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setBackgroundResource(R.drawable.rounded_rect_white_capsule);
                        ((RowItemCategoryBinding) holder.binding).tvCategory.setTextColor(Color.parseColor("#606AA9"));
                    }

                    ((RowItemCategoryBinding) holder.binding).tvCategory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            arrCategory.get(position).is_click = "1";
                            arrCategory.get(catPosition).is_click = "0";
                            catPosition = position;
                            notifyDataSetChanged();

                            getItemList(catPosition);
                        }
                    });
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_category;
            }
        };

        binding.rvCategory.setAdapter(mCategoryAdapter);
    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<Item>(arrItem) {
            @Override
            public void onBind(CommonViewHolder holder, int position) {
                if (holder.binding instanceof RowItemPramotionBinding) {
                    ((RowItemPramotionBinding) holder.binding).setItem(arrItem.get(position));
                    holder.binding.executePendingBindings();

                    ((RowItemPramotionBinding) holder.binding).imageView2.setLetter(arrItem.get(position).getItemName());
                    mMaterialColors = getResources().getIntArray(R.array.colors);
                    ((RowItemPramotionBinding) holder.binding).imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

                    ((RowItemPramotionBinding) holder.binding).tvQty.setText("Qty : " + arrItem.get(position).getQty());
                    ((RowItemPramotionBinding) holder.binding).tvAlterPrice.setText("Price : " + arrItem.get(position).getPrice());

                    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemPosition = position;
                            Intent i = new Intent(AddPromotionalActivity.this, InputDialogPromotionActivity.class);
                            i.putExtra("item", arrItem.get(position));
                            startActivity(i);
                        }
                    });
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_item_pramotion;
            }
        };

        binding.rvList.setAdapter(mAdapter);
    }

    private void updateItem(Item mItem) {
        switch (Integer.parseInt(arrCategory.get(catPosition).catId)) {
            case 2:
                arrCSD.set(itemPosition, mItem);
                getItemList(catPosition);
                break;
            case 4:
                arrJuice.set(itemPosition, mItem);
                getItemList(catPosition);
                break;
            case 3:
                arrWater.set(itemPosition, mItem);
                getItemList(catPosition);
                break;
            case 1:
                arrBiscuit.set(itemPosition, mItem);
                getItemList(catPosition);
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (countPlus == 2) {
                    if (binding.edtInvoiceNo.getText().toString().isEmpty()) {
                        UtilApp.displayAlert(me, "Please insert invoice number!");
                    } else if (strImagePath.equalsIgnoreCase("")) {
                        UtilApp.displayAlert(me, "Please upload invoice receipt!");
                    } else {
                        new addPromotionalData().execute();
                    }
                } else {
                    if (countPlus == 0) {
                        if (binding.edtCustomerNmae.getText().toString().isEmpty()) {
                            UtilApp.displayAlert(me, "Please enter customer name!");
                        } else if (binding.edtPhone.getText().toString().isEmpty()) {
                            UtilApp.displayAlert(me, "Please enter customer phone number!");
                        } else if (binding.edtLocation.getText().toString().isEmpty()) {
                            UtilApp.displayAlert(me, "Please enter customer location!");
                        } else {
                            countPlus++;
                            binding.stepView.go(countPlus, true);
                            updateData(countPlus);
                        }
                    } else {
                        countPlus++;
                        binding.stepView.go(countPlus, true);
                        updateData(countPlus);
                    }

                }
                break;
            case R.id.btnPrevious:
                if (countPlus != 0) {
                    countPlus--;
                    binding.stepView.go(countPlus, true);
                    updateData(countPlus);
                }
                break;
            case R.id.layoutImage:
                dispatchTakePictureIntent();
                break;
            default:
                break;
        }
    }

    private class addPromotionalData extends AsyncTask<Void, Void, Boolean> {

        private LoadingSpinner mDialog;
        boolean isSuccess = false;
        private String strItemId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new LoadingSpinner(AddPromotionalActivity.this);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            for (int i = 0; i < arrSales.size(); i++) {
                if (Integer.parseInt(arrSales.get(i).getQty()) > 0) {
                    if (strItemId.equalsIgnoreCase("")) {
                        strItemId = arrSales.get(i).getItemId();
                    } else {
                        strItemId = strItemId + "," + arrSales.get(i).getItemId();
                    }

                    invAmt += Double.parseDouble(arrSales.get(i).getPrice());
                }
            }

           /* for (int i = 0; i < arrBiscuit.size(); i++) {
                if (Integer.parseInt(arrBiscuit.get(i).getSaleQty()) > 0) {
                    if (strItemId.equalsIgnoreCase("")) {
                        strItemId = arrBiscuit.get(i).getItemId();
                    } else {
                        strItemId = strItemId + "," + arrBiscuit.get(i).getItemId();
                    }

                    invAmt += Double.parseDouble(arrBiscuit.get(i).getPrice());
                }
            }


            for (int i = 0; i < arrJuice.size(); i++) {
                if (Integer.parseInt(arrJuice.get(i).getSaleQty()) > 0) {
                    if (strItemId.equalsIgnoreCase("")) {
                        strItemId = arrJuice.get(i).getItemId();
                    } else {
                        strItemId = strItemId + "," + arrJuice.get(i).getItemId();
                    }

                    invAmt += Double.parseDouble(arrJuice.get(i).getPrice());
                }
            }


            for (int i = 0; i < arrWater.size(); i++) {
                if (Integer.parseInt(arrWater.get(i).getSaleQty()) > 0) {
                    if (strItemId.equalsIgnoreCase("")) {
                        strItemId = arrWater.get(i).getItemId();
                    } else {
                        strItemId = strItemId + "," + arrWater.get(i).getItemId();
                    }

                    invAmt += Double.parseDouble(arrWater.get(i).getPrice());
                }
            }*/

            if (invAmt > 0) {
                isSuccess = true;
            }
            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (mDialog.isShowing())
                mDialog.hide();

            if (isSuccess) {
                mPromotionItem = new Promotion_Item();
                mPromotionItem = db.getPromotionalItemDetail("" + invAmt);

                if (mPromotionItem != null) {
                    if (mPromotionItem.getItem_Name() != null && !mPromotionItem.getItem_Name().equalsIgnoreCase("")) {
                        showDerDialog(me, mPromotionItem.getItem_Name());
                    } else {
                        showDerDialog(me, "");
                    }
                } else {
                    showDerDialog(me, "");
                }

                String cmpNo = UtilApp.getPramotionNo();
                Promotion mPromotion = new Promotion();
                mPromotion.setPromotionId(cmpNo);
                mPromotion.setPromotioncustomerName(binding.edtCustomerNmae.getText().toString());
                mPromotion.setPromotioncustPhone(binding.edtPhone.getText().toString());
                mPromotion.setAmount("" + invAmt);
                mPromotion.setPromotionItemId(strItemId);
                mPromotion.setPromotionItemName(mPromotionItem.getItem_Name());
                mPromotion.setInvoiceNo(binding.edtInvoiceNo.getText().toString());
                mPromotion.setInvoiceImage(strImagePath);

                db.insertPromotion(mPromotion);
                App.isPromotionalSync = false;

                if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                    UtilApp.createBackgroundJob(getApplicationContext());
                }

                //INSERT TRANSACTION
                Transaction transaction = new Transaction();
                transaction.tr_type = Constant.TRANSACTION_TYPES.TT_PRAMOTION;
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
        }
    }

    private void updateData(int position) {
        switch (position) {
            case 0:
                binding.lytDetailView.setVisibility(View.VISIBLE);
                binding.lytItemView.setVisibility(View.GONE);
                break;
            case 1:
                binding.lytDetailView.setVisibility(View.GONE);
                binding.lytItemView.setVisibility(View.VISIBLE);
                binding.lytInvoiceView.setVisibility(View.GONE);
                binding.btnNext.setImageResource(R.drawable.ic_arrow_next);
                break;
            case 2:
                binding.lytDetailView.setVisibility(View.GONE);
                binding.lytItemView.setVisibility(View.GONE);
                binding.lytInvoiceView.setVisibility(View.VISIBLE);
                binding.btnNext.setImageResource(R.drawable.ic_check_white);
                break;
        }

        if (position == 0) {
            binding.btnPrevious.setVisibility(View.GONE);
        } else {
            binding.btnPrevious.setVisibility(View.VISIBLE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            String filePat = UtilApp.compressImage(AddPromotionalActivity.this, mCurrentPhotoPath);
            binding.layoutImage.setVisibility(View.GONE);
            binding.layoutPager.setVisibility(View.VISIBLE);
            strImagePath = filePat.substring(filePat.lastIndexOf("/") + 1);
            binding.ivImage.setImageURI(Uri.parse(filePat));
        }

    }

    public void showDerDialog(Activity activity, String itemName) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_gift);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.95);
        dialog.getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView txtTitle = dialog.findViewById(R.id.tvTitle);
        TextView btnDismiss = dialog.findViewById(R.id.btnDismiss);

        if (!itemName.equalsIgnoreCase("")) {
            txtTitle.setText("Congratulation you are won " + itemName + "!");
        } else {
            txtTitle.setText("Sorry Better luck Next Time!");
        }

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            Item item = (Item) intent.getSerializableExtra("item");

            if (arrSales.size() == 0) {
                arrSales.add(item);
            } else {

                boolean flag = false;
                for (int i = 0; i < arrSales.size(); i++) {
                    if (arrSales.get(i).getItemId().equalsIgnoreCase(item.getItemId())) {
                        flag = true;
                        arrSales.set(i, item);
                        break;
                    }
                }

                if (!flag)
                    arrSales.add(item);
            }

            //Update Item
            updateItem(item);
        }
    };


}
