package com.abra.homework_5.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abra.homework_5.R
import com.abra.homework_5.data.CarInfo
import com.bumptech.glide.Glide

class CarInfoAdapter() : RecyclerView.Adapter<CarInfoAdapter.CarInfoViewHolder>(), Filterable {
    constructor(savedInfo: List<CarInfo>) : this() {
        carInfoList = savedInfo as ArrayList<CarInfo>
        carInfoListForFilter = ArrayList(carInfoList)
    }

    private var carInfoList: ArrayList<CarInfo> = arrayListOf()
    private var carInfoListForFilter: ArrayList<CarInfo> = arrayListOf()
    lateinit var onEditIconClickListener: (carInfo: CarInfo) -> Unit
    lateinit var onCarInfoShowWorkListClickListener: (carInfo: CarInfo) -> Unit

    class CarInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        constructor(itemView: View, onEditIconClickListener: (carInfo: CarInfo) -> Unit, onCarInfoClickListener: (carInfo: CarInfo) -> Unit) : this(itemView) {
            listenerCarInfo = onEditIconClickListener
            listenerShowWorkList = onCarInfoClickListener
        }

        lateinit var listenerShowWorkList: (carInfo: CarInfo) -> Unit
        lateinit var listenerCarInfo: (carInfo: CarInfo) -> Unit
        private val editImage: ImageView = itemView.findViewById(R.id.iv_edit_info)
        private val carImage: ImageView = itemView.findViewById(R.id.imageCar)
        private val textName: TextView = itemView.findViewById(R.id.tvName)
        private val textProducer: TextView = itemView.findViewById(R.id.tvProducer)
        private val textModel: TextView = itemView.findViewById(R.id.tvModel)
        fun bind(carInfo: CarInfo) {
            if (carInfo.pathToPicture == "") {
                carImage.setImageResource(R.drawable.default_icon)
            } else {
                Glide.with(itemView.context).load(carInfo.pathToPicture).into(carImage)
            }
            textName.text = carInfo.name
            textProducer.text = carInfo.producer
            textModel.text = carInfo.model
            editImage.setOnClickListener {
                listenerCarInfo.invoke(carInfo)
            }
            itemView.setOnClickListener {
                listenerShowWorkList.invoke(carInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_info_item, parent, false)
        return CarInfoViewHolder(view, onEditIconClickListener, onCarInfoShowWorkListClickListener)
    }

    override fun onBindViewHolder(holder: CarInfoViewHolder, position: Int) {
        holder.bind(carInfoList[position])
    }

    override fun getItemCount() = carInfoList.size

    private val filter: Filter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = arrayListOf<CarInfo>()
            if (p0 == null || p0.isEmpty()) {
                filteredList.addAll(carInfoListForFilter)
            } else {
                val filterPattern = p0.toString().toLowerCase().trim()
                carInfoListForFilter.forEach {
                    if (it.producer.toLowerCase().contains(filterPattern) || it.model.toLowerCase().contains(filterPattern)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            carInfoList.clear()
            carInfoList.addAll(p1?.values as ArrayList<CarInfo>);
            notifyDataSetChanged()
        }

    }

    override fun getFilter() = filter
    private fun selector(p: CarInfo): String = p.producer.toLowerCase()

    fun sortByCarName(list: List<CarInfo>) {
        carInfoList = ArrayList(list as ArrayList<CarInfo>)
        carInfoListForFilter = ArrayList(list)
        carInfoList.sortBy { selector(it) }
        carInfoListForFilter.sortBy { selector(it) }
        notifyDataSetChanged()
    }
}