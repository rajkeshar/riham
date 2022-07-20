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
import com.mobiato.sfa.databinding.RowItemVanstockBinding;
import com.mobiato.sfa.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VanStockItemAdapter extends RecyclerView.Adapter<VanStockItemAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Item> contactList;
    private List<Item> contactListFiltered;
    private LayoutInflater layoutInflater;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RowItemVanstockBinding binding;

        public MyViewHolder(final RowItemVanstockBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public VanStockItemAdapter(Context context, List<Item> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.contactListFiltered = contactList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        RowItemVanstockBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.row_item_vanstock, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Item mItem = contactListFiltered.get(position);
        holder.binding.setItem(contactListFiltered.get(position));

        holder.binding.imageView2.setLetter(mItem.getItemName());
        mMaterialColors = context.getResources().getIntArray(R.array.colors);
        holder.binding.imageView2.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

        if (mItem.getAltrUOM() != null) {
            holder.binding.tvAlterUOM.setText(mItem.getAlterUOMName() + ": " + mItem.getAlterUOMQty());
            if (mItem.getBaseUOM() != null) {
                holder.binding.dividerUOM.setVisibility(View.VISIBLE);
                holder.binding.tvBaseUOM.setVisibility(View.VISIBLE);
                holder.binding.tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
            } else {
                holder.binding.dividerUOM.setVisibility(View.GONE);
                holder.binding.tvBaseUOM.setVisibility(View.GONE);
            }
        } else {
            holder.binding.dividerUOM.setVisibility(View.GONE);
            holder.binding.tvAlterUOM.setVisibility(View.GONE);
            holder.binding.tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getBaseUOMQty());
        }

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
                    List<Item> filteredList = new ArrayList<>();
                    for (Item row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getItemName().toLowerCase().contains(charString.toLowerCase())) {
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
                contactListFiltered = (ArrayList<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
