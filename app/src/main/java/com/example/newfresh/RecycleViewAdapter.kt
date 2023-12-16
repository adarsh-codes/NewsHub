package com.example.newfresh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecycleViewAdapter(private val handleItemClick : NewsItemClicked): RecyclerView.Adapter<NewsViewHolder>() {
    private val items:ArrayList<News> = ArrayList()

    public fun updateItems(newItems : ArrayList<News>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener{
            handleItemClick.itemClickedFun(items[viewHolder.bindingAdapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val curItem = items[position]
        holder.titleItem.text = curItem.title
        if(curItem.author.isNotEmpty())holder.subItem1.text = curItem.author
        if(curItem.category.isNotEmpty())holder.subItem2.text = curItem.category.joinToString(",")

        Picasso.get()
            .load(curItem.imageurl)
//            .placeholder() // Optional placeholder image
//            .error(R.drawable.error) // Optional error image
            .into(holder.imageItem)
    }
}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val titleItem : TextView = itemView.findViewById(R.id.list_title)
    val imageItem: ImageView = itemView.findViewById(R.id.list_image)
    val subItem1 : TextView = itemView.findViewById(R.id.list_subscript1)
    val subItem2 : TextView = itemView.findViewById(R.id.list_subscript2)
}

interface NewsItemClicked{
    fun itemClickedFun(item: News)
}