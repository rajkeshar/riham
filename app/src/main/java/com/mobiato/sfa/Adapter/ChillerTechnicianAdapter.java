package com.mobiato.sfa.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.RowChillerTechnicianBinding;
import com.mobiato.sfa.databinding.RowCustomerTechnicianBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.ChillerTechnician;
import com.mobiato.sfa.model.CustomerTechnician;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChillerTechnicianAdapter extends RecyclerView.Adapter<ChillerTechnicianAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<ChillerTechnician> contactList;
    private List<ChillerTechnician> contactListFiltered;
    private ContactsAdapterListener listener;
    private LayoutInflater layoutInflater;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private DBManager db;
    private ArrayList<ChillerTechnician> arrRecent = new ArrayList<>();
    private ArrayList<ChillerTechnician> arrRecent_check = new ArrayList<>();
    private ArrayList<CustomerTechnician> arrcustomer = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RowChillerTechnicianBinding binding;

        public MyViewHolder(final RowChillerTechnicianBinding itemBinding) {
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

    public ChillerTechnicianAdapter(Context context, List<ChillerTechnician> contactList, ChillerTechnicianAdapter.ContactsAdapterListener listener) {
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
        RowChillerTechnicianBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.row_chiller_technician, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ChillerTechnician mCus = contactListFiltered.get(position);
        holder.binding.setItem(contactListFiltered.get(position));
        // holder.binding.imageView2.setLetter(mCus.getCustomerName());

        db = new DBManager(context);
        arrRecent = new ArrayList<>();
        arrRecent = db.getChillerTechnicianList();
        arrcustomer = db.getChillerCutomerTechnicianList();
        arrRecent_check.clear();

        holder.binding.tvAssestsnumber.setText("Assestsnumber: " + contactListFiltered.get(position).getAsset_number());
        holder.binding.tvfridgecode.setText("Branding: " + contactListFiltered.get(position).getBranding());
        holder.binding.tvmodelno.setText("Model Number: " + contactListFiltered.get(position).getModel_number());
        holder.binding.tvSerianlnumber.setText("Serial Number: " + contactListFiltered.get(position).getSerial_number());
        holder.binding.tvfridgetype.setText("Fridge Type: " + contactListFiltered.get(position).getFridge_type());

        if (db.getChillerTechnicianListcheck().size() == 0) {
            holder.binding.txtPreview.setVisibility(View.GONE);
        } else if (db.getChillerTechnicianListcheck().size() != 0) {
            arrRecent_check = db.getChillerTechnicianListcheck();
//            if (contactListFiltered.size() == db.getChillerTechnicianListcheck().size()) {
//                holder.binding.txtPreview.setVisibility(View.VISIBLE);
//            } else {
            for (int i = 0; i < arrRecent_check.size(); i++) {
                if (contactListFiltered.get(position).getFridge_id().contains(db.getChillerTechnicianListcheck().get(i).getFridge_id())) {
                    System.out.println("pp--> " + contactListFiltered.get(position).getFridge_id() + " , " + db.getChillerTechnicianListcheck().get(i).getFridge_id());
                    if (db.getChillerTechnicianListcheck().get(i).getAgreementID().equals("0")) {
                        holder.binding.txtPreview.setVisibility(View.GONE);
                    } else {
                        holder.binding.txtPreview.setVisibility(View.VISIBLE);
                    }
                } /*else {
                        holder.binding.txtPreview.setVisibility(View.GONE);
                    }*/
            }
            //}
        } else {
            holder.binding.txtPreview.setVisibility(View.VISIBLE);
        }
        holder.binding.txtPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getChillerTechnicianListcheck().size() != 0) {
                    arrRecent_check.clear();
                    arrRecent_check = db.getChillerTechnicianListcheck();

                    for (int i = 0; i < arrRecent_check.size(); i++) {
                        if (contactListFiltered.get(position).getFridge_id().equals(db.getChillerTechnicianListcheck().get(i).getFridge_id())) {
                            System.out.println("chek-->" + db.getChillerTechnicianListcheck().get(i).getAgreementID());
                            if (db.getChillerTechnicianListcheck().get(i).getAgreementID().equals("0")) {

                            } else {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(App.BASE_URL + "agreement.php?vid=" + db.getChillerTechnicianListcheck().get(i).getAgreementID()));
                                context.startActivity(browserIntent);
                            }

                        }

                        //AgreementID
                    }

                }
            }
        });

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onContactSelected(contactListFiltered.get(position));
                }
            }
        });
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
                    List<ChillerTechnician> filteredList = new ArrayList<>();
                    for (ChillerTechnician row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getManufacturer().toLowerCase().contains(charString.toLowerCase())) {
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
                contactListFiltered = (ArrayList<ChillerTechnician>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(ChillerTechnician contact);
    }
}
