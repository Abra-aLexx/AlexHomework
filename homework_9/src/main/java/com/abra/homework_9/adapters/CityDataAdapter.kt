package com.abra.homework_9.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.abra.homework_9.database.CityData
import com.abra.homework_9.databinding.ItemCityNameBinding

class CityDataAdapter : RecyclerView.Adapter<CityDataAdapter.CityDataViewHolder>() {
    private var citiesList = arrayListOf<CityData>()
    lateinit var onItemClickListener: (CityData, ImageView) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityDataViewHolder {
        val binding = ItemCityNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityDataViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: CityDataViewHolder, position: Int) {
        holder.bind(citiesList[position])
    }

    override fun getItemCount() = citiesList.size

    class CityDataViewHolder(private val binding: ItemCityNameBinding,
                             private val onItemClickListener: (CityData, ImageView) -> Unit) :
            RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cityData: CityData) {
            with(binding) {
                tvCityName1.text = "${cityData.name}, ${cityData.country}"
                root.setOnClickListener {
                    // передаю ImageView, чтобы потом убрать галочку с неё
                    onItemClickListener(cityData, ivCheck)
                    ivCheck.visibility = View.VISIBLE
                }
            }
        }
    }

    fun updateList(list: List<CityData>) {
        citiesList = ArrayList(list)
        notifyDataSetChanged()
    }
}