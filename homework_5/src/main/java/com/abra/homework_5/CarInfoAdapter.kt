package com.abra.homework_5

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CarInfoAdapter() : RecyclerView.Adapter<CarInfoAdapter.CarInfoViewHolder>(), Filterable {
    constructor(c: Activity):this(){
        context = c
        carInfoList = arrayListOf()
        carInfoListForFilter = arrayListOf()
    }
    constructor(c: Activity, savedInfo: List<CarInfo>):this(){
        context = c
        carInfoList = savedInfo as ArrayList<CarInfo>
        carInfoListForFilter = ArrayList(carInfoList)
    }

    private lateinit var context: Activity
    private lateinit var carInfoList : ArrayList<CarInfo>
    private lateinit var carInfoListForFilter : ArrayList<CarInfo>
    private lateinit var onEditIconClickListener: OnEditIconClickListener
    private lateinit var onCarInfoClickListener: OnCarInfoClickListener

    class CarInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        constructor(itemView: View, c: Activity, onEditIconClickListener: OnEditIconClickListener, onCarInfoClickListener: OnCarInfoClickListener) : this(itemView) {
            listener = onEditIconClickListener
            listenerCommon = onCarInfoClickListener
            context = c
        }

        private lateinit var listener: OnEditIconClickListener
        private lateinit var listenerCommon: OnCarInfoClickListener
        private lateinit var context: Activity
        private val editImage: ImageView = itemView.findViewById(R.id.iv_edit_info)
        private val carImage: ImageView = itemView.findViewById(R.id.imageCar)
        private val textName: TextView = itemView.findViewById(R.id.tvName)
        private val textProducer: TextView = itemView.findViewById(R.id.tvProducer)
        private val textModel: TextView = itemView.findViewById(R.id.tvModel)
        fun bind(carInfo: CarInfo) {
            if (carInfo.pathToPicture == "") {
                carImage.setImageResource(R.drawable.default_icon)
            } else {
                Glide.with(context).load(carInfo.pathToPicture).into(carImage)
            }
            textName.text = carInfo.name
            textProducer.text = carInfo.producer
            textModel.text = carInfo.model
            editImage.setOnClickListener {
                listener.onEditIconClick(carInfo)
            }
            itemView.setOnClickListener {
                listenerCommon.onCarInfoClick(carInfo)
            }
        }
    }

    fun add(carInfo: CarInfo) {
        carInfoList.add(carInfo)
        carInfoListForFilter.add(carInfo)
        notifyItemChanged(carInfoList.indexOf(carInfo))
    }

    fun edit(carInfo: CarInfo, position: Int) {
        carInfoList[position] = carInfo
        carInfoListForFilter[position] = carInfo
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_info_item, parent, false)
        return CarInfoViewHolder(view, context, onEditIconClickListener, onCarInfoClickListener)
    }

    override fun onBindViewHolder(holder: CarInfoViewHolder, position: Int) {
        holder.bind(carInfoList[position])
    }

    override fun getItemCount() = carInfoList.size

    interface OnEditIconClickListener {
        fun onEditIconClick(carInfo: CarInfo)
    }

    fun setOnEditIconClickListener(listener: OnEditIconClickListener) {
        onEditIconClickListener = listener
    }

    interface OnCarInfoClickListener {
        fun onCarInfoClick(carInfo: CarInfo)
    }

    fun setOnCarInfoClickListener(listener: OnCarInfoClickListener) {
        onCarInfoClickListener = listener
    }
    private val filter: Filter = object: Filter(){
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = arrayListOf<CarInfo>()
            if (p0 == null || p0.isEmpty()) {
                filteredList.addAll(carInfoListForFilter)
            }else{
                val filterPattern = p0.toString().toLowerCase().trim()
                carInfoListForFilter.forEach {
                    if (it.name?.toLowerCase()?.contains(filterPattern)!!||it.model?.toLowerCase()?.contains(filterPattern)!!) {
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
    private fun selector(p: CarInfo): String = p.name

    fun sortByCarName(list: List<CarInfo>) {
        carInfoList = ArrayList(list as ArrayList<CarInfo>)
        carInfoListForFilter = ArrayList(list)
        carInfoList.sortBy { selector(it) }
        carInfoListForFilter.sortBy { selector(it) }
        notifyDataSetChanged()
    }
}