package com.abra.homework_6_2

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageUriAdapter: RecyclerView.Adapter<ImageUriAdapter.ImageUriViewHolder>() {
    private var listUri = arrayListOf<Uri>()
    lateinit var showWholeImageListener:(Uri) -> Unit
    class ImageUriViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        constructor(itemView: View, showWholeImageListener:(Uri) -> Unit):this(itemView){
            this.showWholeImageListener = showWholeImageListener
        }
        private lateinit var showWholeImageListener:(Uri) -> Unit
        private val image = itemView.findViewById<ImageView>(R.id.ivPhoto)
        fun bind(path: Uri){
                Glide.with(itemView.context).load(path).into(image)
            itemView.setOnClickListener {
                showWholeImageListener.invoke(path)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageUriViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ImageUriViewHolder(view, showWholeImageListener)
    }

    override fun onBindViewHolder(holder: ImageUriViewHolder, position: Int) {
        holder.bind(listUri[position])
    }

    override fun getItemCount() = listUri.size
    fun updateList(list: ArrayList<Uri>){
        listUri = ArrayList(list)
        notifyDataSetChanged()
    }
}