package com.mobiato.sfa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.mobiato.sfa.R;
import com.mobiato.sfa.Techinician.CustomerTechnicianLocationActivity;
import com.mobiato.sfa.Techinician.fragment.TechnicianCustomerListFragment;
import com.mobiato.sfa.activity.CustomerLocationActivity;
import com.mobiato.sfa.databinding.RowCustomerMainBinding;
import com.mobiato.sfa.databinding.RowCustomerTechnicianBinding;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerTechnician;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerTechnicianAdapter extends RecyclerView.Adapter<CustomerTechnicianAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<CustomerTechnician> contactList;
    private List<CustomerTechnician> contactListFiltered;
    private ContactsAdapterListener listener;
    private LayoutInflater layoutInflater;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RowCustomerTechnicianBinding binding;

        public MyViewHolder(final RowCustomerTechnicianBinding itemBinding) {
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

    public CustomerTechnicianAdapter(Context context, List<CustomerTechnician> contactList, CustomerTechnicianAdapter.ContactsAdapterListener listener) {
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
        RowCustomerTechnicianBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.row_customer_technician, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        CustomerTechnician mCus = contactListFiltered.get(position);
        holder.binding.setItem(contactListFiltered.get(position));
        holder.binding.tvChillerSize.setText("Chiller Size:  "+contactList.get(position).getChiller_size_requested());
       // holder.binding.imageView2.setLetter(mCus.getCustomerName());


       /* holder.binding.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilApp.logData(context, "Click On Customer Map Icons");
                Intent in = new Intent(context, CustomerTechnicianLocationActivity.class);
                in.putExtra("custmer", contactListFiltered.get(position));
                context.startActivity(in);

            }
        });*/
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
                    List<CustomerTechnician> filteredList = new ArrayList<>();
                    for (CustomerTechnician row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomername().toLowerCase().contains(charString.toLowerCase())) {
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
                contactListFiltered = (ArrayList<CustomerTechnician>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(CustomerTechnician contact);
    }
}
