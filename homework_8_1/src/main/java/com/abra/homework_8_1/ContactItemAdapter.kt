package com.abra.homework_8_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale
import kotlin.collections.ArrayList

class ContactItemAdapter : RecyclerView.Adapter<ContactItemAdapter.ContactItemViewHolder>(), Filterable {
    private var contactItemList = arrayListOf<ContactItem>()
    private var contactItemListFull = arrayListOf<ContactItem>()
    private var contactItemListFullBeforeUsingSearchView = arrayListOf<ContactItem>()
    lateinit var itemClickListener: (ContactItem, Int) -> Unit

    /*
    * Эти функции мне понадобились для получения актуального листа,
    * вторая нужна, для восстановления листа после использования SearchView,
    * так как с ним возникали багги.
    *
    * P.S Честно скажу, что в домашке 4.1 я так и
    * не исправил эти баги:), но тут все же решился разобраться.
    * */
    fun getList() = contactItemList
    fun getListBeforeUsingSearchView() = contactItemList

    class ContactItemViewHolder(private val itemView: View, private val itemClickListener: (ContactItem, Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private var imageView = itemView.findViewById<ImageView>(R.id.imageView)
        private var name = itemView.findViewById<TextView>(R.id.tvName)
        private var info = itemView.findViewById<TextView>(R.id.tvInfo)
        fun bind(item: ContactItem) {
            imageView.setImageResource(item.iconId)
            when (item.typeInfo) {
                "phone" -> imageView.setBackgroundResource(R.drawable.shape2)

                "email" -> imageView.setBackgroundResource(R.drawable.shape1)
            }
            itemView.setOnClickListener {
                itemClickListener.invoke(item, adapterPosition)
            }
            name.text = item.name
            info.text = item.info
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact_info, parent, false)
        return ContactItemViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ContactItemViewHolder, position: Int) {
        holder.bind(contactItemList[position])
    }

    override fun getItemCount() = contactItemList.size

    private val filter: Filter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = arrayListOf<ContactItem>()
            if (p0 == null || p0.isEmpty()) {
                filteredList.addAll(contactItemListFull)
            } else {
                val filterPattern = p0.toString().toLowerCase(Locale.ROOT).trim()
                contactItemListFull.forEach {
                    if (it.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            contactItemList.clear()
            contactItemList.addAll(p1?.values as ArrayList<ContactItem>);
            notifyDataSetChanged()
        }
    }

    override fun getFilter() = filter

    /*
    * Решил поступить таким способом, что мне кажется выглядит красивее,
    * нежели я бы добавил методы по удалению, редактированию и добавлению.
    * К тому же пришлось бы туда-сюда передавать лишнюю информацию.
    * */
    fun updateList(list: ArrayList<ContactItem>) {
        contactItemList = list
        contactItemListFull = ArrayList(contactItemList)
        contactItemListFullBeforeUsingSearchView = ArrayList(contactItemList)
        notifyDataSetChanged()
    }
}