package com.abra.homework_four_dot_one;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ContactItemViewHolder> implements Filterable {
    private final ArrayList<ContactItem> contactItemList;
    private final ArrayList<ContactItem> contactItemListFull;
    private final Activity context;

    public ArrayList<ContactItem> getContactItemList() {
        return contactItemList;
    }

    public ContactItemAdapter(Activity context, ArrayList<ContactItem> contactItemList) {
        this.contactItemList = contactItemList;
        contactItemListFull = new ArrayList<>(contactItemList);
        this.context = context;
    }

    @NonNull
    @Override
    public ContactItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_info, parent, false);
        return new ContactItemViewHolder(view, context);
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
        // лист contactItemListFull нужен для фильтрации
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

    static class ContactItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final TextView name;
        private final TextView info;
        private final Activity context;
        private ContactItem contactItem;

        public ContactItemViewHolder(@NonNull View itemView, Activity context) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.tvName);
            info = itemView.findViewById(R.id.tvInfo);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        void bind(ContactItem contactItem) {
            this.contactItem = contactItem;
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
            name.setText(contactItem.getName());
            info.setText(contactItem.getInfo());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, EditContactActivity.class);
            intent.putExtra("position", getAdapterPosition());
            intent.putExtra("contactItem", contactItem);
            context.startActivityForResult(intent, 2);
        }
    }
}
