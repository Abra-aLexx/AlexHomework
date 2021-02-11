package com.abra.homework_9.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abra.homework_9.database.CityData
import com.abra.homework_9.databinding.ItemCityNameBinding
import com.abra.homework_9.repositories.SharedPrefRepository

class CityDataAdapter : RecyclerView.Adapter<CityDataAdapter.CityDataViewHolder>() {
    private var citiesList = arrayListOf<CityData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityDataViewHolder {
        val binding = ItemCityNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityDataViewHolder, position: Int) {
        holder.bind(citiesList[position])
    }

    override fun getItemCount() = citiesList.size

    class CityDataViewHolder(private val binding: ItemCityNameBinding) :
            RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cityData: CityData) {
            with(binding) {
                tvCityName1.text = "${cityData.name}, ${cityData.country}"
                root.setOnClickListener {
                    // передаю ImageView, чтобы потом убрать галочку с неё
                    ViewChecker.getInstance().setVisibility(ivCheck)
                    SharedPrefRepository(root.context).writeId(cityData.id)
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