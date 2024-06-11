package com.fatherofapps.androidbase.ui.review.all

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.google.android.material.card.MaterialCardView

class StarAdapter: RecyclerView.Adapter<StarAdapter.StarViewHolder>() {
    private val starList = listOf("all", "5", "4", "3", "2", "1")
    var selectedPosition = RecyclerView.NO_POSITION

    var onItemClickListener: ((Int) -> Unit)? = null
    inner class StarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val starTextView: TextView = itemView.findViewById(R.id.txtStar)
        val starCardView: MaterialCardView = itemView.findViewById(R.id.cardStar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarViewHolder {
        val view = View.inflate(parent.context, R.layout.item_star, null)
        return StarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return starList.size
    }

    override fun onBindViewHolder(holder: StarViewHolder, position: Int) {
        val star = starList[position]
        holder.starTextView.text = star
        val isSelected = position == selectedPosition
        updateColor(holder, isSelected)
        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            if(star == "all")
                onItemClickListener?.invoke(0)
            else
                onItemClickListener?.invoke(star.toInt())
        }
    }

    private fun updateColor(holder: StarViewHolder, isSelected: Boolean) {
        val context = holder.itemView.context
        val cardBackgroundColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.color_card_selected)
        } else {
            ContextCompat.getColor(context, android.R.color.transparent)
        }
        holder.starCardView.setCardBackgroundColor(cardBackgroundColor)
        val textColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.color_text_selected)
        } else {
            ContextCompat.getColor(context, R.color.color_text_unselected)
        }
        holder.starTextView.setTextColor(textColor)
    }
}