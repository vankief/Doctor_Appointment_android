package com.fatherofapps.androidbase.ui.appointment.myappointment

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.response.DateAppointment
import com.fatherofapps.androidbase.utils.convertToNormalDate
import com.fatherofapps.androidbase.utils.convertToVietNamDate

class ScheduleDayAdapter(
    private val scheduleDayList: List<DateAppointment>
):RecyclerView.Adapter<ScheduleDayAdapter.ScheduleDayViewHolder>() {

    inner class ScheduleDayViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleDayViewHolder {
        val view = View.inflate(parent.context, R.layout.item_day, null)
        return ScheduleDayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return scheduleDayList.size
    }

    override fun onBindViewHolder(holder: ScheduleDayViewHolder, position: Int) {
        val scheduleDay = scheduleDayList[position]
        holder.txtDate.text = convertToVietNamDate(scheduleDay.date)
        val appointmentAdapter = AppointmentAdapter(scheduleDay.appointments)

        holder.itemView.findViewById<RecyclerView>(R.id.recyclerDay).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = appointmentAdapter
        }
    }
}