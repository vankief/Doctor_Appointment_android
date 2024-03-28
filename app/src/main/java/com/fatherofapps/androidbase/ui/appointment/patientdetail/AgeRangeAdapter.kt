package com.fatherofapps.androidbase.ui.appointment.patientdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.google.android.material.card.MaterialCardView

class AgeRangeAdapter(
    private val ageRange: List<String>,
) : RecyclerView.Adapter<AgeRangeAdapter.AgeRangeViewHolder>() {
    var selectedPosition = RecyclerView.NO_POSITION

    var onItemClickListener: ((String) -> Unit)? = null

    inner class AgeRangeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val tvAgeRange: TextView = itemView.findViewById(R.id.txtAgeRange)
         val cvAgeRange: MaterialCardView = itemView.findViewById(R.id.cardAgeRange)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeRangeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_age_range, parent, false)
        return AgeRangeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ageRange.size
    }

    override fun onBindViewHolder(holder: AgeRangeViewHolder, position: Int) {
        holder.tvAgeRange.text = ageRange[position]
        val isSelected = position == selectedPosition
        updateColor(holder, isSelected)

        holder.itemView.setOnClickListener() {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onItemClickListener?.invoke(ageRange[position])
        }
    }
    private fun updateColor(holder: AgeRangeViewHolder, isSelected: Boolean) {
        val context = holder.itemView.context
        // Cập nhật màu sắc cho nền của MaterialCardView
        val cardBackgroundColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.color_card_selected) // Màu nền khi item được chọn
        } else {
            ContextCompat.getColor(context, android.R.color.transparent) // Màu nền mặc định khi item không được chọn
        }
        holder.cvAgeRange.setCardBackgroundColor(cardBackgroundColor)

        // Cập nhật màu sắc cho văn bản của TextView
        val textColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.color_text_selected) // Màu văn bản khi item được chọn
        } else {
            ContextCompat.getColor(context, R.color.color_text_unselected) // Màu văn bản mặc định khi item không được chọn
        }
        holder.tvAgeRange.setTextColor(textColor)
    }
}