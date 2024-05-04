package com.fatherofapps.androidbase.ui.notification

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.response.Notification
import com.google.android.material.card.MaterialCardView

class NotificationAdapter(
    private var NotificationList: List<Notification>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtContent: TextView = itemView.findViewById(R.id.txtContent)
        val notificationCard: MaterialCardView = itemView.findViewById(R.id.notificationCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return NotificationList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = NotificationList[position]
        holder.txtTitle.text = notification.title
        holder.txtContent.text = notification.message
        val cardBackgroundColor = if (notification.isRead) {
            ContextCompat.getColor(holder.itemView.context, R.color.color_notification_read)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.color_notification_unread)
        }
        holder.notificationCard.setCardBackgroundColor(cardBackgroundColor)
    }
}