package com.fatherofapps.androidbase.ui.history.appointment

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.response.DateAppointment
import com.fatherofapps.androidbase.utils.convertToVietNamDate

class DayAdapter(
    private val scheduleDayList: List<DateAppointment>
): RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    inner class DayViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = View.inflate(parent.context, R.layout.item_day, null)
        return DayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return scheduleDayList.size
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val scheduleDay = scheduleDayList[position]
        holder.txtDate.text = convertToVietNamDate(scheduleDay.date)
        val appointmentAdapter = HistoryAppointmentAdapter(scheduleDay.appointments)

        holder.itemView.findViewById<RecyclerView>(R.id.recyclerDay).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = appointmentAdapter
        }
    }
}