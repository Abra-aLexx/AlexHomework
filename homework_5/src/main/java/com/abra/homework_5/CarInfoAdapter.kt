package com.abra.homework_5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarInfoAdapter : RecyclerView.Adapter<CarInfoAdapter.CarInfoViewHolder>() {
    private val carInfoList = arrayListOf<CarInfo>()
    private lateinit var onEditIconClickListener: OnEditIconClickListener

    class CarInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        constructor(itemView: View, onEditIconClickListener: OnEditIconClickListener) : this(itemView){
            listener = onEditIconClickListener
        }
        private lateinit var listener: OnEditIconClickListener
        private val editImage: ImageView = itemView.findViewById(R.id.iv_edit_info)
        private val carImage: ImageView = itemView.findViewById(R.id.imageCar)
        private val textName: TextView = itemView.findViewById(R.id.tvName)
        private val textProducer: TextView = itemView.findViewById(R.id.tvProducer)
        private val textModel: TextView = itemView.findViewById(R.id.tvModel)
        fun bind(carInfo: CarInfo) {
            if (carInfo.carBitmap == null) {
                carImage.setImageResource(R.drawable.default_icon)
            } else {
                carImage.setImageBitmap(carInfo.carBitmap)
            }
            textName.text = carInfo.name
            textProducer.text = carInfo.producer
            textModel.text = carInfo.model
            editImage.setOnClickListener {
                listener.onEditIconClick(carInfo, adapterPosition)
            }
        }
    }

    fun add(carInfo: CarInfo) {
        carInfoList.add(carInfo)
        notifyItemChanged(carInfoList.indexOf(carInfo))
    }
    fun edit(carInfo: CarInfo, position: Int) {
        carInfoList[position] = carInfo
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_info_item, parent, false)
        return CarInfoViewHolder(view, onEditIconClickListener)
    }

    override fun onBindViewHolder(holder: CarInfoViewHolder, position: Int) {
        holder.bind(carInfoList[position])
    }

    override fun getItemCount() = carInfoList.size
    fun getList(): ArrayList<CarInfo> {
        return carInfoList
    }
    interface OnEditIconClickListener {
        fun onEditIconClick(carInfo: CarInfo, position: Int)
    }
    fun setOnEditIconClickListener(listener: OnEditIconClickListener){
        onEditIconClickListener = listener
    }
}