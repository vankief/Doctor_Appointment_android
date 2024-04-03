package com.fatherofapps.androidbase.ui.doctor.topdoctor

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.response.Specialist
import com.google.android.material.card.MaterialCardView

class SpecialistAdapter(
    private val specialist: List<Specialist>,
    private var specialistId: String?
): RecyclerView.Adapter<SpecialistAdapter.SpecialistViewHolder>() {

    var selectedPosition = RecyclerView.NO_POSITION

    var onItemClickListener: ((String) -> Unit)? = null
    inner class SpecialistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardSpecialist)
        val txtSpecialistName: TextView = itemView.findViewById(R.id.txtSpecialist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialistViewHolder {
        val view = View.inflate(parent.context, R.layout.item_specialist, null)
        return SpecialistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return specialist.size
    }

    override fun onBindViewHolder(holder: SpecialistViewHolder, position: Int) {
        val specialist = specialist[position]
        holder.txtSpecialistName.text = specialist.name
        val isSelected = specialist.id == specialistId // Kiểm tra xem chuyên khoa hiện tại có phải là chuyên khoa được chọn không
        updateColor(holder, isSelected)
        holder.itemView.setOnClickListener {
            specialistId = specialist.id // Cập nhật ID của chuyên khoa được chọn
            notifyDataSetChanged() // Cập nhật giao diện người dùng
            onItemClickListener?.invoke(specialist.id)
        }
    }
    private fun updateColor(holder: SpecialistAdapter.SpecialistViewHolder, isSelected: Boolean) {
        val context = holder.itemView.context
        // Cập nhật màu sắc cho nền của MaterialCardView
        val cardBackgroundColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.color_card_selected)
        } else {
            ContextCompat.getColor(context, android.R.color.transparent)
        }
        holder.cardView.setCardBackgroundColor(cardBackgroundColor)

        // Cập nhật màu sắc cho văn bản của TextView
        val textColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.color_text_selected)
        } else {
            ContextCompat.getColor(context, R.color.color_text_unselected)
        }
        holder.txtSpecialistName.setTextColor(textColor)
    }
}