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
import com.mobiato.sfa.activity.CustomerLocationActivity;
import com.mobiato.sfa.databinding.RowCustomerMainBinding;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Customer> contactList;
    private List<Customer> contactListFiltered;
    private ContactsAdapterListener listener;
    private LayoutInflater layoutInflater;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RowCustomerMainBinding binding;

        public MyViewHolder(final RowCustomerMainBinding itemBinding) {
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

    public CustomerAdapter(Context context, List<Customer> contactList, ContactsAdapterListener listener) {
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
        RowCustomerMainBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.row_customer_main, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Customer mCus = contactListFiltered.get(position);
        holder.binding.setItem(contactListFiltered.get(position));
        holder.binding.imageView2.setLetter(mCus.getCustomerName());

        mMaterialColors = context.getResources().getIntArray(R.array.colors);
        holder.binding.imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

        if (mCus.getCustType().equalsIgnoreCase("1")) {
            holder.binding.imgColor.setBackgroundColor(context.getResources().getColor(R.color.progress1));
        } else if (mCus.getCustType().equalsIgnoreCase("2")) {
            holder.binding.imgColor.setBackgroundColor(context.getResources().getColor(R.color.creditCustomer));
        } else {
            holder.binding.imgColor.setBackgroundColor(context.getResources().getColor(R.color.tcCustomer));
        }

        if (mCus.getIsSale().equals("1")) {
            holder.binding.imgSmallSales.setVisibility(View.VISIBLE);
        } else {
            holder.binding.imgSmallSales.setVisibility(View.GONE);
        }

        if (mCus.getIsOrder().equals("1")) {
            holder.binding.imgSmallOrder.setVisibility(View.VISIBLE);
        } else {
            holder.binding.imgSmallOrder.setVisibility(View.GONE);
        }

        if (mCus.getIsColl().equals("1")) {
            holder.binding.imgSmallCollection.setVisibility(View.VISIBLE);
        } else {
            holder.binding.imgSmallCollection.setVisibility(View.GONE);
        }

        if (mCus.getIsNew().equals("1")) {
            holder.binding.rlNew.setVisibility(View.VISIBLE);
        } else {
            holder.binding.rlNew.setVisibility(View.GONE);
        }

        holder.binding.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilApp.logData(context, "Click On Customer Map Icons");
                Intent in = new Intent(context, CustomerLocationActivity.class);
                in.putExtra("custmer", contactListFiltered.get(position));
                context.startActivity(in);

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
                    List<Customer> filteredList = new ArrayList<>();
                    for (Customer row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }else if (row.getCustPhone().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }else if (row.getAddress().toLowerCase().contains(charString.toLowerCase())) {
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
