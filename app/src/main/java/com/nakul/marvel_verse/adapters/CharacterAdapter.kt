package com.nakul.marvel_verse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nakul.marvel_verse.R
import com.nakul.marvel_verse.models.CharacterModel

class CharacterAdapter(val list: ArrayList<CharacterModel>) :
    RecyclerView.Adapter<CharacterAdapter.VH>() {
    class VH(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.item_text).text = list[position].name
            Glide.with(this).load(list[position].url).into(findViewById(R.id.item_image))
        }
    }

    override fun getItemCount(): Int = list.size
}