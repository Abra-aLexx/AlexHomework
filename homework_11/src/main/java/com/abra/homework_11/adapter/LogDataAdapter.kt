package com.abra.homework_11.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abra.homework_11.databinding.ItemEventBinding
import com.abra.homework_11.json_structure.LogData

class LogDataAdapter : RecyclerView.Adapter<LogDataAdapter.LogDataViewHolder>() {
    private var mutableLogsList = mutableListOf<LogData>()
    lateinit var onItemClickListener: (LogData) -> Unit
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogDataViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogDataViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: LogDataViewHolder, position: Int) {
        holder.bind(mutableLogsList[position])
    }

    override fun getItemCount() = mutableLogsList.size

    fun updateList(mutableList: MutableList<LogData>) {
        mutableLogsList = mutableList
        notifyDataSetChanged()
    }

    fun sortByDate() {
        mutableLogsList.sortBy { it.date }
        notifyDataSetChanged()
    }

    fun sortByTime() {
        mutableLogsList.sortBy { it.time }
        notifyDataSetChanged()
    }

    class LogDataViewHolder(private val binding: ItemEventBinding,
                            private val onItemClickListener: (LogData) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LogData) {
            with(binding) {
                tvTime.text = data.time
                tvDate.text = data.date
                tvEvent.text = data.actionName
                itemView.setOnClickListener {
                    onItemClickListener.invoke(data)
                }
            }
        }
    }
}