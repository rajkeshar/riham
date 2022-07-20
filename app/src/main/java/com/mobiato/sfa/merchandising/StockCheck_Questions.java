package com.mobiato.sfa.merchandising;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityStockCheckQuestionsBinding;
import com.mobiato.sfa.databinding.RowStockQuestionsBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Stock_Questions;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StockCheck_Questions extends BaseActivity {

    private ActivityStockCheckQuestionsBinding binding;
    private ArrayList<Stock_Questions> arrData = new ArrayList<>();
    ArrayList<Stock_Questions> arrAnswer = new ArrayList<>();
    private DBManager db;
    private List<View> allViewInstance = new ArrayList<>();
    private String custId;
    String Title = "", distributionType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockCheckQuestionsBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;

        Title = getIntent().getStringExtra("title");
        custId = getIntent().getStringExtra("customerId");
        setTitle(Title);

        db = new DBManager(this);
        if (Title.equals("Sensory Evaluation")) {
            arrData = db.getAllSensorQuestions();
        } else if (Title.equals("Consumer Surveys")) {
            arrData = db.getAllConsumerQuestions(custId);
            if (db.isConsumerSurvey(custId)) {
                binding.layoutSubmit.setVisibility(View.GONE);
            }
        } else if (Title.equals("Assets Surveys")) {
            arrData = db.getAllAssetsQuestions();
            if (db.isAssetsSurvey(custId)) {
                binding.layoutSubmit.setVisibility(View.GONE);
            }
        } else {
            distributionType = getIntent().getStringExtra("toolId");
            arrData = db.getAllQuestions(distributionType);
            if (db.isToolSurvey(custId, distributionType)) {
                binding.layoutSubmit.setVisibility(View.GONE);
            }
        }

        if (arrData.size() < 0) {
            binding.layoutSubmit.setVisibility(View.GONE);
        }

        setMainAdapter();

        binding.layoutSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(StockCheck_Questions.this)
                        .setMessage("Are you sure you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getSubmitData();
                            }
                        }).setNegativeButton("No", null)
                        .show();

            }
        });

    }

    private void getSubmitData() {

        arrAnswer = new ArrayList<>();

        if (Title.equals("Sensory Evaluation")) {

            final Dialog dialog = new Dialog(StockCheck_Questions.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.alert_customer);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.95);
            dialog.getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);


            TextView btnSubmit = (TextView) dialog.findViewById(R.id.btnSubmit);

            EditText edtCustomerName = (EditText) dialog.findViewById(R.id.edtCustomerName);
            EditText edtCustomerEmail = (EditText) dialog.findViewById(R.id.edtCustomerEmail);
            EditText edtCustomerPhone = (EditText) dialog.findViewById(R.id.edtCustomerPhone);
            EditText edtCustomerAddress = (EditText) dialog.findViewById(R.id.edtCustomerAddress);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    arrAnswer = new ArrayList<>();

                    for (int i = 0; i < arrData.size(); i++) {

                        if (arrData.get(i).getQuestion_type().equals("radiobtn")) {

                            RadioGroup radioGroup = (RadioGroup) allViewInstance.get(i);
                            RadioButton selectedRadioBtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                            Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                            Stock_Questions quest = new Stock_Questions();
                            quest.setCustomerEmail(edtCustomerEmail.getText().toString());
                            quest.setCustomerName(edtCustomerName.getText().toString());
                            quest.setCustomerPhone(edtCustomerPhone.getText().toString());
                            quest.setCustomerAddress(edtCustomerAddress.getText().toString());
                            quest.setQuestionId(arrData.get(i).getQuestionId());
                            quest.setServeyId(arrData.get(i).getServeyId());
                            quest.setCustomerId(custId);
                            quest.setAnswer(selectedRadioBtn.getTag().toString());
                            arrAnswer.add(quest);
                        } else if (arrData.get(i).getQuestion_type().equals("checkbox")) {
                            LinearLayout lytView = (LinearLayout) allViewInstance.get(i);
                            String anse = "";
                            for (int j = 0; j < lytView.getChildCount(); j++) {
                                View nextChild = lytView.getChildAt(j);
                                if (nextChild instanceof CheckBox) {
                                    if (((CheckBox) nextChild).isChecked()) {
                                        Log.d("variant_name", ((CheckBox) nextChild).getTag().toString() + "");

                                        if (anse.equalsIgnoreCase("")) {
                                            anse = ((CheckBox) nextChild).getTag().toString();
                                        } else {
                                            anse = anse + "," + ((CheckBox) nextChild).getTag().toString();
                                        }
                                    }
                                }
                            }
                            Stock_Questions quest = new Stock_Questions();
                            quest.setCustomerEmail(edtCustomerEmail.getText().toString());
                            quest.setCustomerName(edtCustomerName.getText().toString());
                            quest.setCustomerPhone(edtCustomerPhone.getText().toString());
                            quest.setCustomerAddress(edtCustomerAddress.getText().toString());
                            quest.setQuestionId(arrData.get(i).getQuestionId());
                            quest.setServeyId(arrData.get(i).getServeyId());
                            quest.setCustomerId(custId);
                            quest.setAnswer(anse);
                            if (!anse.equalsIgnoreCase("")) {
                                arrAnswer.add(quest);
                            }
                        } else if (arrData.get(i).getQuestion_type().equals("commentbox")) {
                            EditText tempEditBox = (EditText) allViewInstance.get(i);
                            Log.d("variant_name", tempEditBox.getText().toString() + "");
                            Stock_Questions quest = new Stock_Questions();
                            quest.setCustomerEmail(edtCustomerEmail.getText().toString());
                            quest.setCustomerName(edtCustomerName.getText().toString());
                            quest.setCustomerPhone(edtCustomerPhone.getText().toString());
                            quest.setCustomerAddress(edtCustomerAddress.getText().toString());
                            quest.setQuestionId(arrData.get(i).getQuestionId());
                            quest.setServeyId(arrData.get(i).getServeyId());
                            quest.setCustomerId(custId);
                            quest.setAnswer(tempEditBox.getText().toString());
                            if (!tempEditBox.getText().toString().isEmpty()) {
                                arrAnswer.add(quest);
                            }
                        } else if (arrData.get(i).getQuestion_type().equals("selectbox")) {
                            Spinner tempEditBox = (Spinner) allViewInstance.get(i);
                            Log.d("variant_name", tempEditBox.getSelectedItem().toString() + "");
                            Stock_Questions quest = new Stock_Questions();
                            quest.setCustomerEmail(edtCustomerEmail.getText().toString());
                            quest.setCustomerName(edtCustomerName.getText().toString());
                            quest.setCustomerPhone(edtCustomerPhone.getText().toString());
                            quest.setCustomerAddress(edtCustomerAddress.getText().toString());
                            quest.setQuestionId(arrData.get(i).getQuestionId());
                            quest.setServeyId(arrData.get(i).getServeyId());
                            quest.setCustomerId(custId);
                            quest.setAnswer(tempEditBox.getSelectedItem().toString());
                            arrAnswer.add(quest);
                        } else {
                            EditText tempEditBox = (EditText) allViewInstance.get(i);
                            Log.d("variant_name", tempEditBox.getText().toString() + "");
                            Stock_Questions quest = new Stock_Questions();
                            quest.setCustomerEmail(edtCustomerEmail.getText().toString());
                            quest.setCustomerName(edtCustomerName.getText().toString());
                            quest.setCustomerPhone(edtCustomerPhone.getText().toString());
                            quest.setCustomerAddress(edtCustomerAddress.getText().toString());
                            quest.setQuestionId(arrData.get(i).getQuestionId());
                            quest.setServeyId(arrData.get(i).getServeyId());
                            quest.setCustomerId(custId);
                            quest.setAnswer(tempEditBox.getText().toString());
                            if (!tempEditBox.getText().toString().isEmpty()) {
                                arrAnswer.add(quest);
                            }
                        }
                    }
                    if (arrAnswer.size() > 0) {

                        if (edtCustomerName.getText().toString().isEmpty()) {
                            edtCustomerName.requestFocus();
                            edtCustomerName.setError("Enter Customer Name");
                        } else if (edtCustomerEmail.getText().toString().isEmpty()) {
                            edtCustomerEmail.requestFocus();
                            edtCustomerEmail.setError("Enter Customer Email");
                        } else if (edtCustomerPhone.getText().toString().isEmpty()) {
                            edtCustomerPhone.requestFocus();
                            edtCustomerPhone.setError("Enter Customer Phone");
                        } else {
                            String cmpNo = UtilApp.getSurvNo();
                            //INSERT TRANSACTION
                            Transaction transaction = new Transaction();
                            transaction.tr_type = Constant.TRANSACTION_TYPES.TT_SENSURY_SURVEY;
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

                            db.insertSensorSurveyHeader(cmpNo, custId, arrData.get(0).getServeyId(), edtCustomerEmail.getText().toString(),
                                    edtCustomerName.getText().toString(), edtCustomerPhone.getText().toString(), edtCustomerAddress.getText().toString());
                            db.insertSensoryPost(arrAnswer, cmpNo);

                            if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                                UtilApp.createBackgroundJob(getApplicationContext());
                            }

                            dialog.dismiss();
                            new AlertDialog.Builder(StockCheck_Questions.this)
                                    .setMessage("Sensory Evaluation submitted successfully!")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }

                                    })
                                    .show();
                        }

                    } else {

                        dialog.dismiss();

                        UtilApp.displayAlert(StockCheck_Questions.this, "Please select atleast one answer");
                    }

                }
            });

            dialog.show();


        } else if (Title.equals("Consumer Surveys")) {
            for (int i = 0; i < arrData.size(); i++) {

                if (arrData.get(i).getQuestion_type().equals("radiobtn")) {
                    RadioGroup radioGroup = (RadioGroup) allViewInstance.get(i);
                    RadioButton selectedRadioBtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setAnswer(selectedRadioBtn.getTag().toString());
                    arrAnswer.add(quest);
                } else if (arrData.get(i).getQuestion_type().equals("checkbox")) {
                    LinearLayout lytView = (LinearLayout) allViewInstance.get(i);
                    String anse = "";
                    for (int j = 0; j < lytView.getChildCount(); j++) {
                        View nextChild = lytView.getChildAt(j);
                        if (nextChild instanceof CheckBox) {
                            if (((CheckBox) nextChild).isChecked()) {
                                Log.d("variant_name", ((CheckBox) nextChild).getTag().toString() + "");

                                if (anse.equalsIgnoreCase("")) {
                                    anse = ((CheckBox) nextChild).getTag().toString();
                                } else {
                                    anse = anse + "," + ((CheckBox) nextChild).getTag().toString();
                                }
                            }
                        }
                    }
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setAnswer(anse);
                    if (!anse.equalsIgnoreCase("")) {
                        arrAnswer.add(quest);
                    }
                } else if (arrData.get(i).getQuestion_type().equals("commentbox")) {
                    EditText tempEditBox = (EditText) allViewInstance.get(i);
                    Log.d("variant_name", tempEditBox.getText().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setAnswer(tempEditBox.getText().toString());
                    if (!tempEditBox.getText().toString().isEmpty()) {
                        arrAnswer.add(quest);
                    }
                } else if (arrData.get(i).getQuestion_type().equals("selectbox")) {
                    Spinner tempEditBox = (Spinner) allViewInstance.get(i);
                    Log.d("variant_name", tempEditBox.getSelectedItem().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setAnswer(tempEditBox.getSelectedItem().toString());
                    arrAnswer.add(quest);
                } else {
                    EditText tempEditBox = (EditText) allViewInstance.get(i);
                    Log.d("variant_name", tempEditBox.getText().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setAnswer(tempEditBox.getText().toString());
                    if (!tempEditBox.getText().toString().isEmpty()) {
                        arrAnswer.add(quest);
                    }
                }
            }

            if (arrAnswer.size() > 0) {

                String cmpNo = UtilApp.getSurvNo();
                //INSERT TRANSACTION
                Transaction transaction = new Transaction();
                transaction.tr_type = Constant.TRANSACTION_TYPES.TT_CONS_SURVEY;
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

                db.insertConsumerPost(arrAnswer, cmpNo);

                if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                    UtilApp.createBackgroundJob(getApplicationContext());
                }

                new AlertDialog.Builder(StockCheck_Questions.this)
                        .setMessage("Consumer Surveys submitted successfully!")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .show();
            } else {
                UtilApp.displayAlert(StockCheck_Questions.this, "Please select atleast one answer");
            }
        } else if (Title.equals("Assets Surveys")) {
            for (int i = 0; i < arrData.size(); i++) {

                if (arrData.get(i).getQuestion_type().equals("radiobtn")) {
                    RadioGroup radioGroup = (RadioGroup) allViewInstance.get(i);
                    RadioButton selectedRadioBtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setAnswer(selectedRadioBtn.getTag().toString());
                    arrAnswer.add(quest);
                } else if (arrData.get(i).getQuestion_type().equals("checkbox")) {
                    LinearLayout lytView = (LinearLayout) allViewInstance.get(i);
                    String anse = "";
                    for (int j = 0; j < lytView.getChildCount(); j++) {
                        View nextChild = lytView.getChildAt(j);
                        if (nextChild instanceof CheckBox) {
                            if (((CheckBox) nextChild).isChecked()) {
                                Log.d("variant_name", ((CheckBox) nextChild).getTag().toString() + "");

                                if (anse.equalsIgnoreCase("")) {
                                    anse = ((CheckBox) nextChild).getTag().toString();
                                } else {
                                    anse = anse + "," + ((CheckBox) nextChild).getTag().toString();
                                }
                            }
                        }
                    }
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setAnswer(anse);
                    if (!anse.equalsIgnoreCase("")) {
                        arrAnswer.add(quest);
                    }
                } else if (arrData.get(i).getQuestion_type().equals("commentbox")) {
                    EditText tempEditBox = (EditText) allViewInstance.get(i);
                    Log.d("variant_name", tempEditBox.getText().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setAnswer(tempEditBox.getText().toString());
                    if (!tempEditBox.getText().toString().isEmpty()) {
                        arrAnswer.add(quest);
                    }
                } else if (arrData.get(i).getQuestion_type().equals("selectbox")) {
                    Spinner tempEditBox = (Spinner) allViewInstance.get(i);
                    Log.d("variant_name", tempEditBox.getSelectedItem().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setAnswer(tempEditBox.getSelectedItem().toString());
                    arrAnswer.add(quest);
                } else {
                    EditText tempEditBox = (EditText) allViewInstance.get(i);
                    Log.d("variant_name", tempEditBox.getText().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setAnswer(tempEditBox.getText().toString());
                    if (!tempEditBox.getText().toString().isEmpty()) {
                        arrAnswer.add(quest);
                    }
                }
            }

            if (arrAnswer.size() > 0) {

                String cmpNo = UtilApp.getSurvNo();
                //INSERT TRANSACTION
                Transaction transaction = new Transaction();
                transaction.tr_type = Constant.TRANSACTION_TYPES.TT_ASSETS_SURVEY;
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

                db.insertAssetsSurveyPost(arrAnswer, cmpNo);


                if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                    UtilApp.createBackgroundJob(getApplicationContext());
                }

                new AlertDialog.Builder(StockCheck_Questions.this)
                        .setMessage("Assets Surveys submitted successfully!")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .show();
            } else {
                UtilApp.displayAlert(StockCheck_Questions.this, "Please select atleast one answer");
            }
        } else {

            for (int i = 0; i < arrData.size(); i++) {

                if (arrData.get(i).getQuestion_type().equals("radiobtn")) {
                    RadioGroup radioGroup = (RadioGroup) allViewInstance.get(i);
                    RadioButton selectedRadioBtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setDistributionType(distributionType);
                    quest.setAnswer(selectedRadioBtn.getTag().toString());
                    arrAnswer.add(quest);
                } else if (arrData.get(i).getQuestion_type().equals("checkbox")) {
                    LinearLayout lytView = (LinearLayout) allViewInstance.get(i);
                    String anse = "";
                    for (int j = 0; j < lytView.getChildCount(); j++) {
                        View nextChild = lytView.getChildAt(j);
                        if (nextChild instanceof CheckBox) {
                            if (((CheckBox) nextChild).isChecked()) {
                                Log.d("variant_name", ((CheckBox) nextChild).getTag().toString() + "");

                                if (anse.equalsIgnoreCase("")) {
                                    anse = ((CheckBox) nextChild).getTag().toString();
                                } else {
                                    anse = anse + "," + ((CheckBox) nextChild).getTag().toString();
                                }
                            }
                        }
                    }
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setDistributionType(distributionType);
                    quest.setAnswer(anse);
                    if (!anse.equalsIgnoreCase("")) {
                        arrAnswer.add(quest);
                    }
                } else if (arrData.get(i).getQuestion_type().equals("commentbox")) {
                    EditText tempEditBox = (EditText) allViewInstance.get(i);
                    Log.d("variant_name", tempEditBox.getText().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setDistributionType(distributionType);
                    quest.setAnswer(tempEditBox.getText().toString());
                    if (!tempEditBox.getText().toString().isEmpty()) {
                        arrAnswer.add(quest);
                    }
                } else if (arrData.get(i).getQuestion_type().equals("selectbox")) {
                    Spinner tempEditBox = (Spinner) allViewInstance.get(i);
                    Log.d("variant_name", tempEditBox.getSelectedItem().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setDistributionType(distributionType);
                    quest.setAnswer(tempEditBox.getSelectedItem().toString());
                    arrAnswer.add(quest);
                } else {
                    EditText tempEditBox = (EditText) allViewInstance.get(i);
                    Log.d("variant_name", tempEditBox.getText().toString() + "");
                    Stock_Questions quest = new Stock_Questions();
                    quest.setQuestionId(arrData.get(i).getQuestionId());
                    quest.setServeyId(arrData.get(i).getServeyId());
                    quest.setCustomerId(custId);
                    quest.setDistributionType(distributionType);
                    quest.setAnswer(tempEditBox.getText().toString());
                    if (!tempEditBox.getText().toString().isEmpty()) {
                        arrAnswer.add(quest);
                    }
                }
            }
            if (arrAnswer.size() > 0) {

                String cmpNo = UtilApp.getSurvNo();
                //INSERT TRANSACTION
                Transaction transaction = new Transaction();
                transaction.tr_type = Constant.TRANSACTION_TYPES.TT_DISTRIBUTION_SURVEY;
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

                db.insertSurveyPost(arrAnswer, cmpNo);

                if (UtilApp.isNetworkAvailable(getApplicationContext())) {
                    UtilApp.createBackgroundJob(getApplicationContext());
                }

                new AlertDialog.Builder(StockCheck_Questions.this)
                        .setMessage(Title + " submitted successfully!")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .show();
            } else {
                UtilApp.displayAlert(StockCheck_Questions.this, "Please select atleast one answer");
            }

        }


    }


    private void setMainAdapter() {

        for (int position = 0; position < arrData.size(); position++) {

            RowStockQuestionsBinding RowBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.row_stock_questions, null, false);
            View v = RowBinding.getRoot();

            RowBinding.setItem(arrData.get(position));

            if (arrData.get(position).getQuestion_type().equals("radiobtn")) {

                RadioGroup rg = new RadioGroup(StockCheck_Questions.this);
                allViewInstance.add(rg);
                ArrayList<String> elephantList = new ArrayList<String>(Arrays.asList(arrData.get(position).getQuestion_type_based().split(",")));
                for (int i = 0; i < elephantList.size(); i++) {

                    RadioButton rb = new RadioButton(StockCheck_Questions.this);
                    rg.addView(rb);
                    if (i == 0)
                        rb.setChecked(true);
                    rb.setTag(elephantList.get(i));
                    rb.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    rb.setText(elephantList.get(i));

                }

                RowBinding.layoutOptions.addView(rg);


            } else if (arrData.get(position).getQuestion_type().equals("checkbox")) {
                String texts = arrData.get(position).getQuestion_type_based();
                ArrayList<String> elephantLists = new ArrayList<String>(Arrays.asList(texts.split(",")));

                for (int i = 0; i < elephantLists.size(); i++) {

                    CheckBox chk = new CheckBox(StockCheck_Questions.this);
                    chk.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    chk.setTag(elephantLists.get(i));
                    chk.setText(elephantLists.get(i));
                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                chk.setChecked(true);
                            } else {
                                chk.setChecked(false);
                            }
                        }
                    });

                    RowBinding.layoutOptions.addView(chk);
                }
                allViewInstance.add(RowBinding.layoutOptions);
            } else if (arrData.get(position).getQuestion_type().equals("selectbox")) {
                String texts = arrData.get(position).getQuestion_type_based();
                ArrayList<String> elephantLists = new ArrayList<String>(Arrays.asList(texts.split(",")));

                ArrayAdapter<String> spinnerArrayAdapter = null;
                spinnerArrayAdapter = new ArrayAdapter<String>(StockCheck_Questions.this, android.R.layout.simple_spinner_item, elephantLists);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                Spinner spinner = new Spinner(StockCheck_Questions.this);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setSelection(0, false);
                spinner.setLayoutParams(p);
                allViewInstance.add(spinner);
                RowBinding.layoutOptions.addView(spinner);
            } else if (arrData.get(position).getQuestion_type().equals("commentbox")) {
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                EditText et = new EditText(StockCheck_Questions.this);
                et.setLayoutParams(p);
                allViewInstance.add(et);
                RowBinding.layoutOptions.addView(et);
            } else {
                EditText et = new EditText(StockCheck_Questions.this);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                et.setLayoutParams(p);
                et.setImeOptions(EditorInfo.IME_ACTION_DONE);
                allViewInstance.add(et);
                RowBinding.layoutOptions.addView(et);
            }

            binding.rvList.addView(v);
        }

    }
}
