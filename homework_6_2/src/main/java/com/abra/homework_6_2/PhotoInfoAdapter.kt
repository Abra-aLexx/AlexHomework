package com.abra.homework_6_2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PhotoInfoAdapter: RecyclerView.Adapter<PhotoInfoAdapter.PhotoInfoViewHolder>() {
    private var listPhoto = arrayListOf<PhotoInfo>()
    class PhotoInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.ivPhoto)
        fun bind(info: PhotoInfo){
                Glide.with(itemView.context).load(info.path).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return PhotoInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoInfoViewHolder, position: Int) {
        holder.bind(listPhoto[position])
    }

    override fun getItemCount() = listPhoto.size
    fun updateList(list: ArrayList<PhotoInfo>){
        listPhoto = ArrayList(list)
        notifyDataSetChanged()
    }
}