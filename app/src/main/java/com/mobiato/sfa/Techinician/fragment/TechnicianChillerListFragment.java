package com.mobiato.sfa.Techinician.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mobiato.sfa.Adapter.ChillerTechnicianAdapter;
import com.mobiato.sfa.Adapter.CustomerTechnicianAdapter;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.CustomerDetailActivity;
import com.mobiato.sfa.activity.FragmentJourneyPlan;
import com.mobiato.sfa.activity.forms.TransferForms.ChillerTransferFormActivity;
import com.mobiato.sfa.databinding.FragmentDashboardBinding;
import com.mobiato.sfa.databinding.FragmentTechnicianChillerBinding;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.merchandising.AddCampaignActivity;
import com.mobiato.sfa.model.ChillerTechnician;
import com.mobiato.sfa.model.CustomerTechnician;
import com.mobiato.sfa.model.RecentCustomer;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.rest.BackgroundSync;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.target.ViewTarget;

/**
 * A simple {@link Fragment} subclass.
 */
public class TechnicianChillerListFragment extends BaseActivity {

    FragmentTechnicianChillerBinding binding;
    private MaterialShowcaseSequence sequence;

    private DBManager db;
    private ArrayList<ChillerTechnician> arrRecent = new ArrayList<>();
    private ArrayList<ChillerTechnician> arrRecent_check = new ArrayList<>();
    private ArrayList<CustomerTechnician> arrcustomer = new ArrayList<>();
    private ArrayList<CustomerTechnician> arrcustomerR = new ArrayList<>();
    private ArrayList<CustomerTechnician> arrcustomer_ids = new ArrayList<>();
    private ChillerTechnicianAdapter mAdapter;
    // private ArrayList<String> names = new ArrayList<String>();
    List<String> names = new ArrayList<String>();
    List<String> ids = new ArrayList<String>();
    List<String> ids_id = new ArrayList<String>();
    List<String> chiller_ids = new ArrayList<String>();
    List<String> agreeId_ids = new ArrayList<String>();
    List<String> chiller_size = new ArrayList<String>();
    String str_chiller_seprate = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTechnicianChillerBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        //setNotification();

        setNavigationView();
        setTitle(getString(R.string.nav_chiller));

        db = new DBManager(this);
        setData();

    }

    private void setData() {
        arrRecent = new ArrayList<>();
        arrRecent = db.getChillerTechnicianList();
        arrcustomer = db.getChillerCutomerTechnicianList();
        // System.out.println("chiller--> "+arrRecent.get(0).getManufacturer());

        ids = new ArrayList<>();
        ids_id = new ArrayList<>();
        chiller_size = new ArrayList<>();
        for (int i = 0; i < arrcustomer.size(); i++) {
            // names.add("CRF0000"+arrcustomer.get(i).getCrf_id()+"-"+arrcustomer.get(i).getCustomername());
            ids.add(arrcustomer.get(i).getCrf_id());
            ids_id.add(arrcustomer.get(i).getCustomer_id());
            String currentString = arrcustomer.get(i).getChiller_size_requested();
            String[] separated = currentString.split("-");
            chiller_size.add(separated[0]);
        }

        //System.out.println("chek-->"+db.getChillerTechnicianListcheck().get(0).getIr_id());
        if (db.getChillerTechnicianListcheck().size() != 0) {
            arrRecent_check = db.getChillerTechnicianListcheck();
            System.out.println("chek-->" + arrRecent_check.size() + "-->" + db.getChillerTechnicianListcheck().get(0).getIr_id());
            if (arrRecent_check.size() == arrRecent.size()) {
                for (int i = 0; i < db.getChillerTechnicianListcheck().size(); i++) {
                    callAcceptAPI1(db.getChillerTechnicianListcheck().get(i).getIr_id());
                }
            }
        }

        setMainAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // setNotification();
    }

    AlertDialog.Builder builder;
    ArrayAdapter<String> adapter;
    public String[] arrOutletType = {"Retail", "Whsale", "Eatery", "Bar", "Supermarket", "Other"};

    private void setMainAdapter() {

        mAdapter = new ChillerTechnicianAdapter(this, arrRecent, new ChillerTechnicianAdapter.ContactsAdapterListener() {

            @Override
            public void onContactSelected(ChillerTechnician contact) {

                if (db.getChillerTechnicianListcheck().size() != 0) {
                    arrRecent_check.clear();
                    arrRecent_check = db.getChillerTechnicianListcheck();

                    agreeId_ids.clear();
                    chiller_ids.clear();
                    names.clear();
                    ids = new ArrayList<>();
                    ids_id = new ArrayList<>();
                    for (int i = 0; i < arrRecent_check.size(); i++) {
                        chiller_ids.add(arrRecent_check.get(i).getFridge_id());
                        agreeId_ids.add(arrRecent_check.get(i).getAgreementID());
                    }
                    for (int i = 0; i < arrcustomer.size(); i++) {
                        // names.add("CRF0000"+arrcustomer.get(i).getCrf_id()+"-"+arrcustomer.get(i).getCustomername());

                        String currentString = arrcustomer.get(i).getChiller_size_requested();
                        String[] separated = currentString.split("-");
                        chiller_size.add(currentString);
                        if (contact.getModel_number().equals(currentString)) {
                            ids.add(arrcustomer.get(i).getCrf_id());
                            ids_id.add(arrcustomer.get(i).getCustomer_id());
                            names.add("CRF0000" + arrcustomer.get(i).getCrf_id() + "-" + arrcustomer.get(i).getCustomername());
                        }
                    }


                    //for (int i = 0; i < arrRecent_check.size(); i++) {
                    /* if (contact.getFridge_id().contains(db.getChillerTechnicianListcheck().get(i).getFridge_id())) {*/
                    if (chiller_ids.contains(contact.getFridge_id())) {
                        // System.out.println("chek-->"+db.getChillerTechnicianListcheck().get(i).getFridge_id()+"-->"+contact.getFridge_id());
                        int index = chiller_ids.indexOf(contact.getFridge_id());
                        String agreementId = agreeId_ids.get(index);

                        if(agreementId.equals("0"))
                        {
                            builder = new AlertDialog.Builder(TechnicianChillerListFragment.this);
                            builder.setMessage("This Chiller Refused by customer. Please select another chiller.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog1, int id) {
                                            //finish();
                                            dialog1.cancel();
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("Already Refused");
                            alert.show();
                        }else
                        {
                            builder = new AlertDialog.Builder(TechnicianChillerListFragment.this);
                            builder.setMessage("This chiller already assign to other customer. Please select another chiller.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog1, int id) {
                                            //finish();
                                            dialog1.cancel();
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("Already Assign");
                            alert.show();
                        }

                        //break;
                    } else {
                        if (names.isEmpty()) {

                        } else {
                            final Dialog dialog = new Dialog(TechnicianChillerListFragment.this);
                            /*dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
                            dialog.setContentView(R.layout.dialog_chiller_customer);
                            dialog.setCancelable(true);

                            // set the custom dialog components - text, image and button
                            // final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
                            final Spinner spinner = (Spinner) dialog.findViewById(R.id.et_chiller_customer);
                            final Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
                            final RadioGroup radioSexGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TechnicianChillerListFragment.this, android.R.layout.simple_spinner_item, names);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                            spinner.setAdapter(spinnerArrayAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    // TODO Auto-generated method stub
                                    String m = names.get(position);
                                    arrcustomerR = db.getChillerCutomerTechnicianListR(ids.get(position));
                                    arrcustomer_ids = db.getChillerCutomerTechnicianListR(ids_id.get(position));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // TODO Auto-generated method stub

                                }
                            });

                            btn_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                    int selectedId = radioSexGroup.getCheckedRadioButtonId();
                                    RadioButton radioSexButton = (RadioButton) dialog.findViewById(selectedId);

                                    if (radioSexButton.getText().toString().equals("Create")) {
                                        Intent in = new Intent(TechnicianChillerListFragment.this, CustomerAgreementActivity.class);
                                        in.putExtra("fridge", contact);
                                        in.putExtra("customer", arrcustomer_ids);
                                        startActivity(in);
                                    } else {
                                        UtilApp.confirmationDialog("Are you sure you want to reject?", TechnicianChillerListFragment.this, new OnSearchableDialog() {
                                            @Override
                                            public void onItemSelected(Object o) {
                                                String selection = (String) o;
                                                if (selection.equalsIgnoreCase("yes")) {
                                                    callRejectAPI(arrcustomer_ids.get(0).getCrf_id(), contact.getFridge_id(), contact.getIr_id());
                                                }
                                            }
                                        });


                                    }
                                }
                            });

                            dialog.show();
                            //break;
                        }
                    }
                    //}

                } else {
                    names.clear();
                    ids = new ArrayList<>();
                    ids_id = new ArrayList<>();
                    for (int i = 0; i < arrcustomer.size(); i++) {
                        // names.add("CRF0000"+arrcustomer.get(i).getCrf_id()+"-"+arrcustomer.get(i).getCustomername());

                        String currentString = arrcustomer.get(i).getChiller_size_requested();
                        /*String[] separated = currentString.split("-");*/
                        chiller_size.add(currentString);
                        if (contact.getModel_number().equals(currentString)) {
                            ids.add(arrcustomer.get(i).getCrf_id());
                            ids_id.add(arrcustomer.get(i).getCustomer_id());
                            names.add("CRF0000" + arrcustomer.get(i).getCrf_id() + "-" + arrcustomer.get(i).getCustomername());
                        }

                    }
                    if (names.isEmpty()) {

                    } else {
                        final Dialog dialog = new Dialog(TechnicianChillerListFragment.this);
                        /*dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
                        dialog.setContentView(R.layout.dialog_chiller_customer);
                        dialog.setCancelable(true);

                        // set the custom dialog components - text, image and button
                        // final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
                        final Spinner spinner = (Spinner) dialog.findViewById(R.id.et_chiller_customer);
                        final Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
                        final RadioGroup radioSexGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TechnicianChillerListFragment.this, android.R.layout.simple_spinner_item, names);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        spinner.setAdapter(spinnerArrayAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // TODO Auto-generated method stub
                                String m = names.get(position);
                                arrcustomerR = db.getChillerCutomerTechnicianListR(ids.get(position));
                                arrcustomer_ids = db.getChillerCutomerTechnicianListR(ids_id.get(position));

                     /*   Toast.makeText(getApplicationContext(),
                                contact.getAsset_number(),
                                Toast.LENGTH_SHORT).show();*/
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // TODO Auto-generated method stub

                            }
                        });

                        btn_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                                RadioButton radioSexButton = (RadioButton) dialog.findViewById(selectedId);

                                if (radioSexButton.getText().toString().equals("Create")) {
                                    Intent in = new Intent(TechnicianChillerListFragment.this, CustomerAgreementActivity.class);
                                    in.putExtra("fridge", contact);
                                    in.putExtra("customer", arrcustomer_ids);
                                    startActivity(in);
                                } else {
                                    UtilApp.confirmationDialog("Are you sure you want to reject?", TechnicianChillerListFragment.this, new OnSearchableDialog() {
                                        @Override
                                        public void onItemSelected(Object o) {
                                            String selection = (String) o;
                                            if (selection.equalsIgnoreCase("yes")) {
                                                callRejectAPI(arrcustomer_ids.get(0).getCrf_id(), contact.getFridge_id(), contact.getIr_id());
                                            }
                                        }
                                    });
                                }

                            }
                        });


                        dialog.show();
                    }
                }

            }
        });

        binding.rvChillerList.setAdapter(mAdapter);
    }

    private void callRejectAPI(String crfId, String fridgeId, String crId) {

        ProgressDialog progress = new ProgressDialog(TechnicianChillerListFragment.this);
        progress.setMessage("Please wait...");
        //  progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        // progress.setProgress(0);
        progress.show();


        final Call<JsonObject> labelResponse = ApiClient.getService().getRejectChillerClose(App.CHILLER_REJECT, crfId, fridgeId);
        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Add Cus Response:", response.toString());
                UtilApp.logData(TechnicianChillerListFragment.this, "CHiller Reject Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(TechnicianChillerListFragment.this, "CHiller Reject Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        progress.dismiss();
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            Toast.makeText(getApplicationContext(), "Chiller reject  successfully!!", Toast.LENGTH_SHORT).show();
                            String agreement_id = "0";
                            db.deleteChillerCustomer(String.valueOf(crfId));
                            db.insertChillerTachniciancheck(crId, fridgeId, agreement_id);
                            setData();
                        } else {
                            // Fail to Post
                            Toast.makeText(getApplicationContext(), "Chiller reject  fail!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Chiller reject  fail!!", Toast.LENGTH_SHORT).show();
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Add Cust Fail", error.getMessage());
                //  removeItem(position);
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Chiller reject  fail!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callAcceptAPI1(String s) {
        ProgressDialog progress = new ProgressDialog(TechnicianChillerListFragment.this);
        progress.setMessage("Please wait...");
        //  progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        // progress.setProgress(0);
        progress.show();
        JsonObject jObj = new JsonObject();
        jObj.addProperty("method", App.CHILLER_CLOSE);
        jObj.addProperty("ir_id", s);
        System.out.println("id->" + s);

        final Call<JsonObject> labelResponse = ApiClient.getService().getUpdateChillerClose(App.CHILLER_CLOSE, s);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Add Cus Response:", response.toString());
                UtilApp.logData(TechnicianChillerListFragment.this, "Add Customer Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(TechnicianChillerListFragment.this, "Add Customer Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        progress.dismiss();
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {

                            App.isAddRequestSync = false;

                           /* int count = Integer.parseInt(str_notification);
                            count=count-1;
                            System.out.println("count1-->"+count);
                            //App.countNoti=count;
                            Settings.setString(App.countNoti_tech, String.valueOf(count));
                            SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            // write all the data entered by the u ser in SharedPreference and apply
                            myEdit.putString("Notificationcount",String.valueOf(count));
                            myEdit.apply();*/

                        } else {
                            // Fail to Post
                            App.isAddRequestSync = false;
                        }
                    }
                } else {
                    App.isAddRequestSync = false;
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isAddRequestSync = false;
                Log.e("Add Cust Fail", error.getMessage());
                //  removeItem(position);
                progress.dismiss();
            }
        });
    }

}
