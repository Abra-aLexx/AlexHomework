package com.abra.homework_5.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abra.homework_5.R
import com.abra.homework_5.data.WorkInfo
import com.abra.homework_5.functions.setImageStatus

class WorkInfoAdapter() : RecyclerView.Adapter<WorkInfoAdapter.WorkInfoViewHolder>(), Filterable {
    constructor(savedInfo: List<WorkInfo>) : this() {
        workInfoList = savedInfo as ArrayList<WorkInfo>
        workInfoListForFilter = ArrayList(workInfoList)
        workInfoListCopyForOrder = ArrayList(workInfoList)
    }

    private var workInfoList: ArrayList<WorkInfo> = arrayListOf()
    private var workInfoListForFilter: ArrayList<WorkInfo> = arrayListOf()
    private var workInfoListCopyForOrder: ArrayList<WorkInfo> = arrayListOf()
    lateinit var onWorkInfoItemClickListener: (workInfo: WorkInfo) -> Unit

    class WorkInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        constructor(itemView: View, onWorkInfoItemClickListener: (workInfo: WorkInfo) -> Unit) : this(itemView) {
            listener = onWorkInfoItemClickListener
        }

        private lateinit var listener: (workInfo: WorkInfo) -> Unit
        private val imageWork: ImageView = itemView.findViewById(R.id.ivWorkIcon)
        private val workStatus: TextView = itemView.findViewById(R.id.tvWorkStatus)
        private val workName: TextView = itemView.findViewById(R.id.tvWorkNameInList)
        private val workDate: TextView = itemView.findViewById(R.id.tvWorkDate)
        private val workCost: TextView = itemView.findViewById(R.id.tvCostInWorkList)
        fun bind(workInfo: WorkInfo) {
            setImageStatus(workInfo.status, itemView.resources, imageWork, workStatus)
            workStatus.text = workInfo.status
            workName.text = workInfo.workName
            workDate.text = workInfo.date
            workCost.text = "$${workInfo.cost}"
            itemView.setOnClickListener {
                listener.invoke(workInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.work_info_item, parent, false)
        return WorkInfoViewHolder(view, onWorkInfoItemClickListener)
    }

    override fun onBindViewHolder(holder: WorkInfoViewHolder, position: Int) {
        holder.bind(workInfoList[position])
    }

    override fun getItemCount() = workInfoList.size

    private val filter: Filter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = arrayListOf<WorkInfo>()
            if (p0 == null || p0.isEmpty()) {
                filteredList.addAll(workInfoListForFilter)
            } else {
                val filterPattern = p0.toString().toLowerCase().trim()
                workInfoListForFilter.forEach {
                    if (it.workName?.toLowerCase()?.contains(filterPattern)!!) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            workInfoList.clear()
            workInfoList.addAll(p1?.values as ArrayList<WorkInfo>);
            notifyDataSetChanged()
        }
    }

    override fun getFilter() = filter
    fun updateLists(list: List<WorkInfo>) {
        workInfoList = ArrayList(list as ArrayList<WorkInfo>)
        workInfoListForFilter = ArrayList(list)
        workInfoListCopyForOrder = ArrayList(list)
        notifyDataSetChanged()
    }

    fun showByOrder(context: Context, order: String) {
        var list = arrayListOf<WorkInfo>()
        when (order) {
            context.resources.getString(R.string.pending) -> {
                workInfoList = workInfoListCopyForOrder
                list = getListByOrder(order)
            }
            context.resources.getString(R.string.in_progress_lowe_case) -> {
                workInfoList = workInfoListCopyForOrder
                list = getListByOrder(order)
            }
            context.resources.getString(R.string.completed_in_lower_case) -> {
                workInfoList = workInfoListCopyForOrder
                list = getListByOrder(order)
            }
            "all" -> {
                list = workInfoListCopyForOrder
            }
        }
        workInfoList = ArrayList(list)
        workInfoListForFilter = ArrayList(list)
        notifyDataSetChanged()
    }

    private fun getListByOrder(order: String): ArrayList<WorkInfo> {
        val list = arrayListOf<WorkInfo>()
        workInfoList.forEach {
            if (it.status.equals(order)) list.add(it)
        }
        return list
    }
}