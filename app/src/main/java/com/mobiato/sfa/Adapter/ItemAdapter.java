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
import com.mobiato.sfa.databinding.RowItemSaleBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Item> contactList;
    private List<Item> contactListFiltered;
    private ItemsAdapterListener listener;
    private LayoutInflater layoutInflater;
    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    private String type, customerCode, routeId;
    private DBManager db;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RowItemSaleBinding binding;

        public MyViewHolder(final RowItemSaleBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemSelected(contactListFiltered.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }

    public ItemAdapter(Context context, List<Item> contactList, String type, String customerCode, String routeId,
                       ItemsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.type = type;
        this.customerCode = customerCode;
        this.contactList = contactList;
        this.routeId = routeId;
        this.contactListFiltered = contactList;

        db = new DBManager(this.context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        RowItemSaleBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.row_item_sale, parent, false);
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
            holder.binding.tvAlterUOM.setText(mItem.getAlterUOMName() + ": " + mItem.getSaleAltQty());
            if (mItem.getBaseUOM() != null) {
                holder.binding.dividerUOM.setVisibility(View.VISIBLE);
                holder.binding.tvBaseUOM.setVisibility(View.VISIBLE);
                holder.binding.tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getSaleBaseQty());
            } else {
                holder.binding.dividerUOM.setVisibility(View.GONE);
                holder.binding.tvBaseUOM.setVisibility(View.GONE);
            }
        } else {
            holder.binding.dividerUOM.setVisibility(View.GONE);
            holder.binding.tvAlterUOM.setVisibility(View.GONE);
            holder.binding.tvBaseUOM.setText(mItem.getBaseUOMName() + ": " + mItem.getSaleBaseQty());
        }

        double itemBasePrice = 0, itemAlterPrice = 0;

        if (type.equalsIgnoreCase("Load")) {
            itemBasePrice = db.getItemPrice(mItem.getItemId());
            itemAlterPrice = db.getItemAlterPrice(mItem.getItemId());
        } else if (type.equalsIgnoreCase("SalesmanLoad")) {
            itemBasePrice = db.getItemPrice(mItem.getItemId());
            itemAlterPrice = db.getItemAlterPrice(mItem.getItemId());
        } else {
            if (db.isPricingItems(customerCode, mItem.getItemId())) {
                Item pItem = db.getCustPricingItems(customerCode, mItem.getItemId());
                itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
                itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
            } else {
                if (db.isAgentPricingItems(mItem.getItemId(), routeId)) {
                    Item pItem = db.getAgentPricingItems(mItem.getItemId());
                    itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
                    itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                } else {
                    itemBasePrice = db.getItemPrice(mItem.getItemId());
                    itemAlterPrice = db.getItemAlterPrice(mItem.getItemId());
                }
            }
        }

        if (type.equalsIgnoreCase("SalesmanLoad")) {
            holder.binding.lytUnitPrice.setVisibility(View.GONE);
            holder.binding.lytPrice.setVisibility(View.GONE);
        } else {
            holder.binding.lytUnitPrice.setVisibility(View.VISIBLE);
            holder.binding.tvUnitAlterPrice.setText("Unit Price: " + UtilApp.getNumberFormate((itemAlterPrice)) + " UGX");
            holder.binding.dividerUnitPrice.setVisibility(View.VISIBLE);
            holder.binding.tvUnitBaseUOMPrice.setVisibility(View.VISIBLE);
            holder.binding.tvUnitBaseUOMPrice.setText(UtilApp.getNumberFormate((itemBasePrice)) + " UGX");

            if (mItem.getSaleAltPrice() != null) {
                holder.binding.lytPrice.setVisibility(View.VISIBLE);
                holder.binding.tvAlterPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleAltPrice())) + " UGX");
                if (mItem.getSaleBasePrice() != null) {
                    holder.binding.dividerPrice.setVisibility(View.VISIBLE);
                    holder.binding.tvBaseUOMPrice.setVisibility(View.VISIBLE);
                    holder.binding.tvBaseUOMPrice.setText(UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleBasePrice())) + " UGX");
                } else {
                    holder.binding.dividerPrice.setVisibility(View.GONE);
                    holder.binding.tvBaseUOMPrice.setVisibility(View.GONE);
                }
            } else {
                if (mItem.getSaleBasePrice() != null) {
                    holder.binding.lytPrice.setVisibility(View.VISIBLE);
                    holder.binding.dividerPrice.setVisibility(View.GONE);
                    holder.binding.tvAlterPrice.setVisibility(View.GONE);
                    holder.binding.tvBaseUOMPrice.setText("Price: " + UtilApp.getNumberFormate(Double.parseDouble(mItem.getSaleBasePrice())) + " UGX");
                } else {
                    holder.binding.lytPrice.setVisibility(View.GONE);
                }
            }
        }


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemSelected(contactListFiltered.get(position), position);
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

    public interface ItemsAdapterListener {
        void onItemSelected(Item contact, int position);
    }
}
