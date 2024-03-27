package com.fatherofapps.androidbase.ui.appointment.doctordetail

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.utils.DayOfWeek
import com.fatherofapps.androidbase.utils.convertToVietnameseDayOfWeek
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleAdapter(
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
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, position)

        val dayOfWeekInt = calendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeek = when (dayOfWeekInt) {
            Calendar.SUNDAY -> DayOfWeek.SUNDAY
            Calendar.MONDAY -> DayOfWeek.MONDAY
            Calendar.TUESDAY -> DayOfWeek.TUESDAY
            Calendar.WEDNESDAY -> DayOfWeek.WEDNESDAY
            Calendar.THURSDAY -> DayOfWeek.THURSDAY
            Calendar.FRIDAY -> DayOfWeek.FRIDAY
            Calendar.SATURDAY -> DayOfWeek.SATURDAY
            else -> throw IllegalArgumentException("Ngày trong tuần không hợp lệ: $dayOfWeekInt")
        }

        val date = SimpleDateFormat("dd/MM", Locale.getDefault()).format(calendar.time)

        holder.dayTextView.text = convertToVietnameseDayOfWeek(dayOfWeek)
        holder.dateTextView.text = date

        holder.dayTextView.text = convertToVietnameseDayOfWeek(dayOfWeek) // Chuyển đổi ngày trong tuần sang tiếng Việt
        holder.dateTextView.text = date

        val isSelected = position == selectedPosition

        updateColor(holder, isSelected)

        holder.itemView.setOnClickListener {
            // Cập nhật vị trí item được chọn
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            // Lấy năm hiện tại
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            // Tạo full date từ ngày đã chọn và năm hiện tại
            val selectedDate = "$date/$currentYear"
            // Gọi hàm callback và truyền ngày đã chọn
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