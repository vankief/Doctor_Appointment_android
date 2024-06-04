package com.fatherofapps.androidbase.ui.appointment.booking

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.models.TimeSlotInfo
import com.fatherofapps.androidbase.data.response.ListTime
import com.fatherofapps.androidbase.ui.appointment.doctordetail.ScheduleAdapter
import com.fatherofapps.androidbase.utils.convertToNormalTime
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class TimeSlotAdapter(
    private  var listTimeSlot: List<ListTime>
): RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {
    private var selectedPosition = RecyclerView.NO_POSITION

    var onItemClickListener: ((TimeSlotInfo) -> Unit)? = null

    private var isCardOnlineSelected: Boolean = false
    private var isCardOfflineSelected: Boolean = false

    inner class TimeSlotViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardTimeSlot : MaterialCardView = itemView.findViewById(R.id.cardTimeSlot)
        val txtTimeSlot: TextView = itemView.findViewById(R.id.txtTimeSlot)
        val txtTimeSlotAvailable: TextView = itemView.findViewById(R.id.txtTimeSlotAvailable)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = View.inflate(parent.context, R.layout.item_timeslot, null)

        return TimeSlotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTimeSlot.size
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val timeSlot = listTimeSlot[position]
        val normalTime = convertToNormalTime(timeSlot.timeSlot)
        holder.txtTimeSlot.text = normalTime
        holder.txtTimeSlotAvailable.text = "${timeSlot.currentPatient}-${timeSlot.maximumPatient}"
        val timeSlotInfo = TimeSlotInfo(normalTime, timeSlot.service, timeSlot.maximumPatient, timeSlot.currentPatient)
        val isSelected = position == selectedPosition

        if (isCardOfflineSelected && timeSlot.service == "Online") {
            holder.itemView.alpha = 0.5f
            holder.itemView.isEnabled = false
        } else if (isCardOnlineSelected && timeSlot.service == "Offline") {
            holder.itemView.alpha = 0.5f
            holder.itemView.isEnabled = false

        } else {
            // Kích hoạt lại item nếu không phù hợp với điều kiện vô hiệu hóa
            holder.itemView.alpha = 1.0f
            holder.itemView.isEnabled = true
        }
        updateColor(holder, isSelected)
        holder.itemView.setOnClickListener {
            // Cập nhật vị trí item được chọn
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()

            // Gọi hàm callback và truyền ngày đã chọn
            onItemClickListener?.invoke(timeSlotInfo)
        }
    }

    private fun updateColor(holder: TimeSlotViewHolder , isSelected: Boolean) {
        val context = holder.itemView.context
        // Cập nhật màu sắc cho nền của MaterialCardView
        val cardBackgroundColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.color_card_selected) // Màu nền khi item được chọn
        } else {
            ContextCompat.getColor(context, android.R.color.transparent) // Màu nền mặc định khi item không được chọn
        }
        holder.cardTimeSlot.setCardBackgroundColor(cardBackgroundColor)

        // Cập nhật màu sắc cho văn bản của TextView
        val textColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.color_text_selected) // Màu văn bản khi item được chọn
        } else {
            ContextCompat.getColor(context, R.color.color_text_unselected) // Màu văn bản mặc định khi item không được chọn
        }
        holder.txtTimeSlot.setTextColor(textColor)
    }
    // Phương thức để cập nhật trạng thái của CardOnline
    fun updateCardOnlineStatus(isSelected: Boolean) {
        isCardOnlineSelected = isSelected
        notifyDataSetChanged() // Cập nhật giao diện RecyclerView
    }

    // Phương thức để cập nhật trạng thái của CardOffline
    fun updateCardOfflineStatus(isSelected: Boolean) {
        isCardOfflineSelected = isSelected
        notifyDataSetChanged() // Cập nhật giao diện RecyclerView
    }
    fun resetItemStatus() {
        selectedPosition = RecyclerView.NO_POSITION // Đặt lại vị trí item được chọn
        notifyDataSetChanged() // Cập nhật giao diện RecyclerView
    }

}