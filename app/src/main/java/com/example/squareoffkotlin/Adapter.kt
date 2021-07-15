package com.example.squareoffkotlin

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class Adapter(private val modelList: List<Model>) :
    RecyclerView.Adapter<Adapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val name: String = modelList[position].name
        val slug: String = modelList[position].slug
        val img: String = modelList[position].img
        val status: Long = modelList[position].status
        var even = false
        if (position % 2 == 0) {
            even = true
        }
        holder.setData(name, slug, img, status, even)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var nameText: TextView = itemView.findViewById(R.id.name)
        private var slugText: TextView = itemView.findViewById(R.id.slug)
        private var statusText: TextView =  itemView.findViewById(R.id.status)
        private var image: ImageView = itemView.findViewById(R.id.image)
        private var layout: ConstraintLayout =  itemView.findViewById(R.id.item_background)


        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun setData(name: String, slug: String, img: String, status: Long, even: Boolean) {
            if (itemView.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                nameText.text = "Name:- $name Slug:- $slug"
                val slu = slug.split("-").toTypedArray()
                if (even) {
                    layout.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#9EE8B365" ))
                }
                slugText.text = "Year:- " + slu[slu.size - 1]
                statusText.text = "No of Dash Character " + (slu.size - 1).toString()
                if (img != "") {
                    Glide.with(itemView.context).load(img).into(image)
                } else {
                    image.visibility = View.GONE
                }
            } else if (itemView.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                nameText.text = name
                if (img != "") {
                    Glide.with(itemView.context).load(img).into(image)
                } else {
                    image.visibility = View.INVISIBLE
                    itemView.visibility = View.GONE
                }
            }
        }


    }
}
