package com.mobiato.sfa.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.RowJourneyListBinding;
import com.mobiato.sfa.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MerchanCustomerAdapter extends RecyclerView.Adapter<MerchanCustomerAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Customer> contactList;
    private List<Customer> contactListFiltered;
    private ContactsAdapterListener listener;
    private LayoutInflater layoutInflater;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RowJourneyListBinding binding;

        public MyViewHolder(final RowJourneyListBinding itemBinding) {
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

    public MerchanCustomerAdapter(Context context, List<Customer> contactList, ContactsAdapterListener listener) {
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
        RowJourneyListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.row_journey_list, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Customer mCus = contactListFiltered.get(position);
        holder.binding.setItem(contactListFiltered.get(position));
        if (contactListFiltered.get(position).getVisibility().equals("0")) {
            holder.binding.imgVisited.setVisibility(View.GONE);
        } else {
            holder.binding.imgVisited.setVisibility(View.VISIBLE);
        }

        holder.binding.executePendingBindings();
        Pattern p = Pattern.compile("\\b[a-zA-Z]");
        Matcher m = p.matcher(contactListFiltered.get(position).getCustomerName());
        if (m.find())
            System.out.print(m.group());
        holder.binding.txtShortName.setText(m.group());

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
                    List<Customer> filteredList = new ArrayList<>();
                    for (Customer row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())) {
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
                contactListFiltered = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Customer contact);
    }
}
