package com.abra.homework_four_dot_one;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ContactItemViewHolder> implements Filterable {
    private final ArrayList<ContactItem> contactItemList;
    private final ArrayList<ContactItem> contactItemListFull;
    private OnItemClickListener onItemClickListener;

    public ArrayList<ContactItem> getContactItemList() {
        return contactItemList;
    }

    public ContactItemAdapter(ArrayList<ContactItem> contactItemList) {
        this.contactItemList = contactItemList;
        contactItemListFull = new ArrayList<>(contactItemList);
    }

    @NonNull
    @Override
    public ContactItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_info, parent, false);
        return new ContactItemViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactItemViewHolder holder, int position) {
        holder.bind(contactItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactItemList != null ? contactItemList.size() : 0;
    }

    public void add(ContactItem contactItem) {
        contactItemList.add(contactItem);
        contactItemListFull.add(contactItem);
        notifyItemChanged(contactItemList.indexOf(contactItem));
    }

    public void edit(int position, ContactItem contactItem) {
        contactItemList.set(position, contactItem);
        contactItemListFull.set(position, contactItem);
        notifyItemChanged(position);
    }

    public void remove(int position) {
        contactItemList.remove(position);
        contactItemListFull.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ContactItem> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(contactItemListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (ContactItem item : contactItemListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            contactItemList.clear();
            contactItemList.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    static class ContactItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView name;
        private final TextView info;
        private final OnItemClickListener onItemClickListener;

        public ContactItemViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.tvName);
            info = itemView.findViewById(R.id.tvInfo);
            this.onItemClickListener = onItemClickListener;
        }

        void bind(ContactItem contactItem) {
            imageView.setImageResource(contactItem.getIconId());
            switch (contactItem.getTypeInfo()) {
                case "phone": {
                    imageView.setBackgroundResource(R.drawable.shape2);
                    break;
                }
                case "email": {
                    imageView.setBackgroundResource(R.drawable.shape1);
                    break;
                }
            }
            if (onItemClickListener != null)
                itemView.setOnClickListener(view -> {
                    onItemClickListener.onItemClick(contactItem, getAdapterPosition());
                });
            name.setText(contactItem.getName());
            info.setText(contactItem.getInfo());
        }
    }

    interface OnItemClickListener {
        void onItemClick(ContactItem contactItem, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
