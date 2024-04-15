package com.fatherofapps.androidbase.ui.appointment.myappointment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.response.AppointmentDetail
import com.fatherofapps.androidbase.utils.convertImagePath

class AppointmentAdapter(
    private val appointmentList: List<AppointmentDetail>
): RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {


    inner class AppointmentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val doctorImg: ImageView = itemView.findViewById(R.id.doctorAvatar)
        val txtDoctorNam:TextView = itemView.findViewById(R.id.txtDoctorName)
        val txtService:TextView = itemView.findViewById(R.id.txtService)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)
        val txtScheduleTime: TextView = itemView.findViewById(R.id.txtScheduleTime)
        val imgService: ImageView = itemView.findViewById(R.id.imgService)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment_card, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointmentList[position]
        holder.txtDoctorNam.text = appointment.doctorName
        Glide.with(holder.itemView.context)
            .load(convertImagePath(appointment.doctorImage))
            .into(holder.doctorImg)
        holder.txtService.text = appointment.service
        holder.txtStatus.text = appointment.status
        holder.txtScheduleTime.text = appointment.scheduleTime
        if (appointment.service == "online") {
            holder.imgService.setImageResource(R.drawable.ic_videocall)
        } else {
            holder.imgService.setImageResource(R.drawable.chat_rounded)
        }
        holder.itemView.setOnClickListener(){
            val action = AppointmentFragmentDirections.actionAppointmentFragmentToMyAppointmentDetailFragment()
            holder.itemView.findNavController().navigate(action)
        }



    }
}