package com.mobiato.sfa.Adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.DownloadingDataActivity;
import com.mobiato.sfa.databinding.RowNotificationTechnicianBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.ChillerTechnician;
import com.mobiato.sfa.model.CustomerTechnician;
import com.mobiato.sfa.model.NotificationTechnician;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

import static android.content.Context.MODE_PRIVATE;

public class NotificationTechnicianAdapter extends RecyclerView.Adapter<NotificationTechnicianAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<NotificationTechnician> contactList;
    private List<NotificationTechnician> contactListFiltered;
    private ContactsAdapterListener listener;
    private LayoutInflater layoutInflater;
    String str_notification="0";
    private DBManager db;

    //Date
    private DatePickerDialog datePickerDialog;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RowNotificationTechnicianBinding binding;

        public MyViewHolder(final RowNotificationTechnicianBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public NotificationTechnicianAdapter(Context context, List<NotificationTechnician> contactList, NotificationTechnicianAdapter.ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        RowNotificationTechnicianBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.row_notification_technician, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        NotificationTechnician mCus = contactListFiltered.get(position);
        holder.binding.setItem(contactListFiltered.get(position));
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        str_notification = sh.getString("Notificationcount", "0");
        db = new DBManager(context);
        holder.binding.tvCustomerOutlet.setText("Number of Fridge: " + contactList.get(position).getNumber_fridge());

        holder.binding.tvCustomername.setText("Schedule Date: " + contactList.get(position).getSchudule_date());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onContactSelected(contactListFiltered.get(position));
                }
            }
        });
        holder.binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(str_notification);
                if(str_notification!="0") {
                    count = count - 1;
                }else {
                    count = count + 1;
                }
                Settings.setString(App.countNoti_tech,String.valueOf(count));
                SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                // write all the data entered by the user in SharedPreference and apply
                myEdit.putString("Notificationcount",String.valueOf(count));
                myEdit.apply();

                callAcceptAPI1("1", contactListFiltered.get(position).getSchudule_date(), contactListFiltered.get(position).getId(), contactListFiltered.get(position).getIro_id(), contactListFiltered.get(position).getSalesman_id(), position);
            }
        });

        holder.binding.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Position-->" + contactListFiltered.get(position).getSchudule_date());
                int count = Integer.parseInt(str_notification);
                if(str_notification!="0") {
                    count = count - 1;
                }else {
                    count = count + 1;
                }
                System.out.println("count1-->"+count);
                //App.countNoti=count;
                Settings.setString(App.countNoti_tech, String.valueOf(count));
                SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                // write all the data entered by the user in SharedPreference and apply
                myEdit.putString("Notificationcount",String.valueOf(count));
                myEdit.apply();
                callAcceptAPI1("2", contactListFiltered.get(position).getSchudule_date(), contactListFiltered.get(position).getId(), contactListFiltered.get(position).getIro_id(), contactListFiltered.get(position).getSalesman_id(), position);
            }
        });
        holder.binding.btnReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Position-->" + contactListFiltered.get(position).getSchudule_date());
                //callAcceptAPI("2",contactListFiltered.get(position).getSchudule_date(),contactListFiltered.get(position).getId(),contactListFiltered.get(position).getIro_id(),contactListFiltered.get(position).getSalesman_id(),position);
                // Get Current Date
                int count = Integer.parseInt(str_notification);
                if(str_notification!="0") {
                    count = count - 1;
                }else {
                    count = count + 1;
                }
                System.out.println("count1-->"+count);
                //App.countNoti=count;
                Settings.setString(App.countNoti_tech,String.valueOf(count));
//                Toast.makeText(context,count,Toast.LENGTH_LONG).show();
                Settings.setString(App.countNoti_tech, String.valueOf(count));
                SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                // write all the data entered by the user in SharedPreference and apply
                myEdit.putString("Notificationcount",String.valueOf(count));
                myEdit.apply();
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                /*  txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);*/
                                holder.binding.tvCustomername.setText("Schedule Date: " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                callAcceptAPI1("1", date, contactListFiltered.get(position).getId(), contactListFiltered.get(position).getIro_id(), contactListFiltered.get(position).getSalesman_id(), position);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    public void removeItem(int pos) {
        contactListFiltered.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<NotificationTechnician> filteredList = new ArrayList<>();
                    for (NotificationTechnician row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSchudule_date().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<NotificationTechnician>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(NotificationTechnician contact);
    }


    private void callAcceptAPI1(String s, String schudule_date, String id, String ir_code, String salesman_id, int position) {
        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please wait...");
        //  progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        // progress.setProgress(0);
        progress.show();
        JsonObject jObj = new JsonObject();
        jObj.addProperty("method", App.CHILLER_SCHEDULE);
        jObj.addProperty("status", s);
        jObj.addProperty("schudule_date", schudule_date);
        jObj.addProperty("id", id);
        jObj.addProperty("iro_id", ir_code);

        final Call<JsonObject> labelResponse = ApiClient.getService().getUpdateChiller1(App.CHILLER_SCHEDULE,s,schudule_date,id,ir_code);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Add Cus Response:", response.toString());
                UtilApp.logData(context, "Add Customer Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(context, "Add Customer Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        removeItem(position);
                        progress.dismiss();
                        getCustomer_technician();
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {

                            App.isAddRequestSync = false;
                           // getCustomer_technician();
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

    private void getCustomer_technician(){
        db.deleteChillerCustomerAll();
        String custId = Settings.getString(App.SALESMANID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getCustomerTechnician(App.CUSTOMER_TECHNICIAN, custId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Customer Response technician", response.toString());
                if (response.body() != null) {
                    //UtilApp.logData(DownloadingDataActivity.this, "Customer Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<CustomerTechnician> arrCustomer = new ArrayList<>();
                        arrCustomer.clear();
                        arrCustomer = new Gson().fromJson(response.body().get("Result").getAsJsonArray().toString(),
                                new TypeToken<List<CustomerTechnician>>() {
                                }.getType());
                        //db.deleteTable(DBManager.TABLE_CUSTOMER);
                        db.insertCustomerTachnician(arrCustomer);
                        db.insertChillerCustomerTachnician(arrCustomer);
                    }
                } else {
                    UtilApp.logData(context, "Customer Response: Blank");
                }

                getChiller_technician();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                UtilApp.logData(context, "Customer Failure: " + error.getMessage());
                getChiller_technician();
            }
        });
    }

    private void getChiller_technician() {
        String custId = Settings.getString(App.SALESMANID);
        final Call<JsonObject> labelResponse = ApiClient.getService().getChillerTechnician(App.CHILLER_APPROVAL_TECHNICIAN, custId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Chiller Response technician", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(context, "Chiller Response: " + response.body().toString());
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<ChillerTechnician> arrCustomer_tech = new ArrayList<>();
                        arrCustomer_tech.clear();
                        arrCustomer_tech = new Gson().fromJson(response.body().get("Result").getAsJsonArray().toString(),
                                new TypeToken<List<ChillerTechnician>>() {
                                }.getType());
                        //db.deleteTable(DBManager.TABLE_CUSTOMER);
                        db.insertChillerTachnician(arrCustomer_tech);
                    }
                } else {
                    UtilApp.logData(context, "Customer Response: Blank");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Customer Fail", error.getMessage());
                UtilApp.logData(context, "Customer Failure: " + error.getMessage());
            }
        });
    }

}


