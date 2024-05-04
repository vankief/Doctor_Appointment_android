package com.fatherofapps.androidbase.ui.notification

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.response.DayNotification
import com.fatherofapps.androidbase.utils.convertToVietNamDate

class DayAdapter(
    private val dayList: List<DayNotification>
): RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    inner class DayViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtDay: TextView = itemView.findViewById(R.id.txtDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = View.inflate(parent.context, R.layout.item_day, null)
        return DayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = dayList[position]
        holder.txtDay.text = convertToVietNamDate(day.day)

        val notificationAdapter = NotificationAdapter(day.notifications)

        holder.itemView.findViewById<RecyclerView>(R.id.recyclerDay).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = notificationAdapter
        }
    }
}