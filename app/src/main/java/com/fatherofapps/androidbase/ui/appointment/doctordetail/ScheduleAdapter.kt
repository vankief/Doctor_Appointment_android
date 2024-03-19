package com.fatherofapps.androidbase.ui.appointment.doctordetail

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleAdapter: RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
    var selectedDate: String? = null
    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
        val dayTextView: TextView = itemView.findViewById(R.id.tvDay)
        val DateTextView: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = View.inflate(parent.context, R.layout.item_schedule, null)
        return ScheduleViewHolder(view)
    }

    override fun getItemCount(): Int {
       return 6
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val calendar = Calendar.getInstance() // Lấy thời điểm hiện tại
        val sdfDay = SimpleDateFormat("EEEE", Locale.getDefault()) // Định dạng ngày trong tuần
        val sdfDate = SimpleDateFormat("dd/MM", Locale.getDefault()) // Định dạng ngày/tháng

        calendar.add(Calendar.DAY_OF_MONTH, position)

        val dayOfWeek = sdfDay.format(calendar.time)
        val date = sdfDate.format(calendar.time)
        holder.dayTextView.text = dayOfWeek
        holder.DateTextView.text = date

        val isSelected = position == selectedPosition

        updateColor(holder, isSelected)

        holder.itemView.setOnClickListener {
            // Cập nhật vị trí item được chọn
            selectedPosition = holder.adapterPosition
            // Lấy năm hiện tại
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            // Tạo full date từ ngày đã chọn và năm hiện tại
            val selectedDate = "$date/$currentYear"
            // Cập nhật lại giao diện sau khi item được chọn
            notifyDataSetChanged()
        }
    }
    private fun updateColor(holder: ScheduleViewHolder, isSelected: Boolean) {
        val context = holder.itemView.context
        // Cập nhật màu sắc cho nền của MaterialCardView
        val cardBackgroundColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.md_theme_light_onPrimary) // Màu nền khi item được chọn
        } else {
            ContextCompat.getColor(context, android.R.color.transparent) // Màu nền mặc định khi item không được chọn
        }
        holder.cardView.setCardBackgroundColor(cardBackgroundColor)

        // Cập nhật màu sắc cho văn bản của TextView
        val textColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.white) // Màu văn bản khi item được chọn
        } else {
            ContextCompat.getColor(context, R.color.md_theme_light_onPrimary) // Màu văn bản mặc định khi item không được chọn
        }
        holder.dayTextView.setTextColor(textColor)
        holder.DateTextView.setTextColor(textColor)
    }
}