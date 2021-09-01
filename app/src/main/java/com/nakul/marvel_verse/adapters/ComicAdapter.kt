package com.nakul.marvel_verse.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nakul.marvel_verse.R
import com.nakul.marvel_verse.models.ComicModel
import com.nakul.marvel_verse.utils.toUrl

class ComicAdapter(val list: ArrayList<ComicModel>) :
    RecyclerView.Adapter<ComicAdapter.VH>() {
    class VH(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.item_text).text = list[position].title
            Log.d("image",list[position].thumbnail.toUrl())
            Glide.with(this).load(list[position].thumbnail.toUrl())
                .into(findViewById(R.id.item_image))
        }
    }

    override fun getItemCount(): Int = list.size
}