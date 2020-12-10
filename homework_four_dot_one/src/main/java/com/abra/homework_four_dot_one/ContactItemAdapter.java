package com.abra.homework_four_dot_one;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ContactItemViewHolder> implements Filterable {
    private static ContactItemAdapter contactItemAdapter = new ContactItemAdapter();
    private List<ContactItem> contactItemList;
    private List<ContactItem> contactItemListFull;

    private ContactItemAdapter() {
        contactItemList = new ArrayList<>();
        contactItemListFull = new ArrayList<>(contactItemList);
    }

    public static ContactItemAdapter getContactItemAdapter() {
        return contactItemAdapter;
    }

    @NonNull
    @Override
    public ContactItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_info, parent, false);
        return new ContactItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactItemViewHolder holder, int position) {
        holder.bind(contactItemList.get(position));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), EditContactActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("name", holder.getTextName());
            intent.putExtra("info", holder.getTextInfo());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactItemList != null ? contactItemList.size() : 0;
    }

    public void add(ContactItem contactItem) {
        /* при добавлении новых контактов бывает баг
         * с добавлением 2-ух элементов сразу, но не всегда,
         * подскажите как решить, я так и не додумался */
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

    public ContactItem getContactItem(int position) {
        return contactItemList.get(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
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
            contactItemList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    static class ContactItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private TextView info;
        private String textName;
        private String textInfo;

        public ContactItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.tvName);
            info = itemView.findViewById(R.id.tvInfo);
        }

        void bind(ContactItem contactItem) {
            imageView.setImageResource(contactItem.getIconId());
            imageView.setBackgroundResource(contactItem.getIconBackground());
            name.setText(contactItem.getName());
            info.setText(contactItem.getInfo());
            textName = contactItem.getName();
            textInfo = contactItem.getInfo();
        }

        public String getTextName() {
            return textName;
        }

        public String getTextInfo() {
            return textInfo;
        }
    }
}
