package com.abra.homework_5

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.zip.Inflater

class WorkInfoAdapter() : RecyclerView.Adapter<WorkInfoAdapter.WorkInfoViewHolder>(), Filterable {
    constructor(c: Activity):this(){
        context = c
        workInfoList = arrayListOf()
        workInfoListForFilter = arrayListOf()
    }
    constructor(c: Activity, savedInfo: List<WorkInfo>):this(){
        context = c
        workInfoList = savedInfo as ArrayList<WorkInfo>
        workInfoListForFilter = ArrayList(workInfoList)
    }
    private lateinit var context: Activity
    private lateinit var workInfoList: ArrayList<WorkInfo>
    private lateinit var workInfoListForFilter: ArrayList<WorkInfo>
    private lateinit var listener: OnWorkInfoItemClickListener

    class WorkInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        constructor(itemView: View, c: Activity, l: OnWorkInfoItemClickListener) : this(itemView) {
            context = c
            listener = l
        }
        private lateinit var context: Activity
        private lateinit var listener: OnWorkInfoItemClickListener
        private val imageWork: ImageView = itemView.findViewById(R.id.ivWorkIcon)
        private val workStatus: TextView = itemView.findViewById(R.id.tvWorkStatus)
        private val workName: TextView = itemView.findViewById(R.id.tvWorkNameInList)
        private val workDate: TextView = itemView.findViewById(R.id.tvWorkDate)
        private val workCost: TextView = itemView.findViewById(R.id.tvCostInWorkList)
        fun bind(workInfo: WorkInfo) {
            when (workInfo.status) {
                "pending" -> {
                    imageWork.setImageResource(R.drawable.ic_baseline_handyman_48_pending)
                    workStatus.setTextColor(context.resources.getColor(R.color.work_status_pending))
                }
                "in progress" -> {
                    imageWork.setImageResource(R.drawable.ic_baseline_handyman_48_in_progress)
                    workStatus.setTextColor(context.resources.getColor(R.color.work_status_in_progress))
                }
                "completed" -> {
                    imageWork.setImageResource(R.drawable.ic_baseline_handyman_48_completed)
                    workStatus.setTextColor(context.resources.getColor(R.color.work_status_completed))
                }
            }
            workStatus.text = workInfo.status
            workName.text = workInfo.workName
            workDate.text = workInfo.date
            workCost.text = "$${workInfo.cost}"
            itemView.setOnClickListener {
                listener.onWorkInfoItemClick(workInfo, adapterPosition)
            }
        }
    }

    fun add(workInfo: WorkInfo) {
        workInfoList.add(workInfo)
        workInfoListForFilter.add(workInfo)
        notifyItemChanged(workInfoList.indexOf(workInfoList))
    }
    fun edit(workInfo: WorkInfo, position: Int) {
        workInfoList[position] = workInfo
        workInfoListForFilter[position] = workInfo
        notifyItemChanged(position)
    }
    fun remove(position: Int) {
        workInfoList.removeAt(position)
        workInfoListForFilter.removeAt(position)
        notifyItemRemoved(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.work_info_item, parent, false)
        return WorkInfoViewHolder(view,context,listener)
    }

    override fun onBindViewHolder(holder: WorkInfoViewHolder, position: Int) {
        holder.bind(workInfoList[position])
    }

    override fun getItemCount() = workInfoList.size

    interface OnWorkInfoItemClickListener{
        fun onWorkInfoItemClick(workInfo: WorkInfo, position: Int)
    }
    fun setOnWorkInfoItemClickListener(listener: OnWorkInfoItemClickListener){
        this.listener = listener
    }
    private val filter: Filter = object: Filter() {
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
    fun updateLists(list: List<WorkInfo>){
        workInfoList = ArrayList(list as ArrayList<WorkInfo>)
        workInfoListForFilter = ArrayList(list)
        notifyDataSetChanged()
    }
}