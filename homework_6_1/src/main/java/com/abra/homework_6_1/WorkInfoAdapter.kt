package com.abra.homework_6_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkInfoAdapter: RecyclerView.Adapter<WorkInfoAdapter.WorkInfoViewHolder>() {
    private var workInfoList: ArrayList<WorkInfo> = arrayListOf()
    class WorkInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageWork: ImageView = itemView.findViewById(R.id.ivWorkIcon)
        private val workStatus: TextView = itemView.findViewById(R.id.tvWorkStatus)
        private val workName: TextView = itemView.findViewById(R.id.tvWorkNameInList)
        private val workDate: TextView = itemView.findViewById(R.id.tvWorkDate)
        private val workCost: TextView = itemView.findViewById(R.id.tvCostInWorkList)
        fun bind(workInfo: WorkInfo) {
            when (workInfo.status) {
                itemView.resources.getString(R.string.pending) -> {
                    imageWork.setImageResource(R.drawable.ic_baseline_handyman_48_pending)
                    workStatus.setTextColor(itemView.resources.getColor(R.color.work_status_pending))
                }
                itemView.resources.getString(R.string.in_progress_lowe_case) -> {
                    imageWork.setImageResource(R.drawable.ic_baseline_handyman_48_in_progress)
                    workStatus.setTextColor(itemView.resources.getColor(R.color.work_status_in_progress))
                }
                itemView.resources.getString(R.string.completed_in_lower_case) -> {
                    imageWork.setImageResource(R.drawable.ic_baseline_handyman_48_completed)
                    workStatus.setTextColor(itemView.resources.getColor(R.color.work_status_completed))
                }
            }
            workStatus.text = workInfo.status
            workName.text = workInfo.workName
            workDate.text = workInfo.date
            workCost.text = "$${workInfo.cost}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.work_info_item, parent, false)
        return WorkInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkInfoViewHolder, position: Int) {
        holder.bind(workInfoList[position])
    }

    override fun getItemCount() = workInfoList.size


    fun updateLists(list: ArrayList<WorkInfo>) {
        workInfoList = ArrayList(list)
        notifyDataSetChanged()
    }
}