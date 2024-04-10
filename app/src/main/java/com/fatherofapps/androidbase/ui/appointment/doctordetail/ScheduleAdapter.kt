package com.fatherofapps.androidbase.ui.appointment.doctordetail

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.utils.DayOfWeek
import com.fatherofapps.androidbase.utils.convertDateFormatAndDayOfWeek
import com.fatherofapps.androidbase.utils.convertToVietnameseDayOfWeek
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class ScheduleAdapter(
    private val doctorScheduleDay : List<String>
): RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
    var selectedPosition = RecyclerView.NO_POSITION

    var onItemClickListener: ((String) -> Unit)? = null


    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardSchedule)
        val dayTextView: TextView = itemView.findViewById(R.id.tvDay)
        val dateTextView: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = View.inflate(parent.context, R.layout.item_schedule, null)
        return ScheduleViewHolder(view)
    }

    override fun getItemCount(): Int {
       return 6
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val dateString = doctorScheduleDay[position]
        val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        val dayofWeekString = convertToVietnameseDayOfWeek(DayOfWeek.valueOf(dayOfWeek.toUpperCase()))
        val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM"))
        holder.dayTextView.text = dayofWeekString
        holder.dateTextView.text = formattedDate

        val isSelected = position == selectedPosition

        updateColor(holder, isSelected)

        holder.itemView.setOnClickListener {
            // Cập nhật vị trí item được chọn
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            // Gọi hàm callback và truyền ngày đã chọn
            val selectedDate = doctorScheduleDay[selectedPosition]
            onItemClickListener?.invoke(selectedDate)

        }
    }
    private fun updateColor(holder: ScheduleViewHolder, isSelected: Boolean) {
        val context = holder.itemView.context
        // Cập nhật màu sắc cho nền của MaterialCardView
        val cardBackgroundColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.color_card_selected) // Màu nền khi item được chọn
        } else {
            ContextCompat.getColor(context, android.R.color.transparent) // Màu nền mặc định khi item không được chọn
        }
        holder.cardView.setCardBackgroundColor(cardBackgroundColor)

        // Cập nhật màu sắc cho văn bản của TextView
        val textColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.color_text_selected) // Màu văn bản khi item được chọn
        } else {
            ContextCompat.getColor(context, R.color.color_text_unselected) // Màu văn bản mặc định khi item không được chọn
        }
        holder.dayTextView.setTextColor(textColor)
        holder.dateTextView.setTextColor(textColor)
    }

}