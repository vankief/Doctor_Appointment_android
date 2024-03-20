package com.fatherofapps.androidbase.ui.appointment.booking

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.models.TimeSlot
import com.fatherofapps.androidbase.ui.appointment.doctordetail.ScheduleAdapter
import com.google.android.material.card.MaterialCardView
import java.util.Calendar

class TimeSlotAdapter(
    private val timeSlot: List<TimeSlot>
): RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {
    var selectedPosition = RecyclerView.NO_POSITION

    var onItemClickListener: ((String) -> Unit)? = null
    inner class TimeSlotViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardTimeSlot : MaterialCardView = itemView.findViewById(R.id.cardTimeSlot)
        val txtTimeSlot: TextView = itemView.findViewById(R.id.txtTimeSlot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = View.inflate(parent.context, R.layout.item_timeslot, null)
        return TimeSlotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return timeSlot.size
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val timeSlot = timeSlot[position]
        holder.txtTimeSlot.text = timeSlot.timeRange // Đặt văn bản cho TextView từ đối tượng TimeSlot

        val isSelected = position == selectedPosition
        updateColor(holder, isSelected)

        holder.itemView.setOnClickListener {
            // Cập nhật vị trí item được chọn
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            // Gọi hàm callback và truyền ngày đã chọn
            onItemClickListener?.invoke(timeSlot.timeRange)
        }
    }

    private fun updateColor(holder: TimeSlotAdapter.TimeSlotViewHolder, isSelected: Boolean) {
        val context = holder.itemView.context
        // Cập nhật màu sắc cho nền của MaterialCardView
        val cardBackgroundColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.md_theme_light_primary) // Màu nền khi item được chọn
            Log.d("TimeSlotAdapter", "updateColor: ${R.color.md_theme_light_primary}")
        } else {
            ContextCompat.getColor(context, android.R.color.transparent) // Màu nền mặc định khi item không được chọn

        }
        holder.cardTimeSlot.setCardBackgroundColor(cardBackgroundColor)

        // Cập nhật màu sắc cho văn bản của TextView
        val textColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.white) // Màu văn bản khi item được chọn
        } else {
            ContextCompat.getColor(context, R.color.md_theme_light_primary) // Màu văn bản mặc định khi item không được chọn
        }
        holder.txtTimeSlot.setTextColor(textColor)
    }

}