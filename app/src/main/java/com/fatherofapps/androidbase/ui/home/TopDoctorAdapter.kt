package com.fatherofapps.androidbase.ui.home

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.response.TopDoctor
import java.lang.Math.min

class TopDoctorAdapter(
    private val topDoctor: List<TopDoctor>
): RecyclerView.Adapter<TopDoctorAdapter.TopDoctorViewHolder>() {

    inner class TopDoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDoctor: ImageView = itemView.findViewById(R.id.imgAvatarDoctor)
        val txtDoctorName: TextView = itemView.findViewById(R.id.txtdoctorName)
        val txtDoctorSpecialization: TextView = itemView.findViewById(R.id.txtDoctorSpecialization)
        init {
            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToFragmentDoctorDetail(
                    topDoctor[adapterPosition].id
                )
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopDoctorViewHolder {
        val view = View.inflate(parent.context, R.layout.item_top_doctor, null)
        return TopDoctorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return min(topDoctor.size, 7)
    }

    override fun onBindViewHolder(holder: TopDoctorViewHolder, position: Int) {
        val doctor = topDoctor[position]
        // Sử dụng Glide để preload hình ảnh từ đường dẫn URL
        val preloadRequest: RequestBuilder<Drawable> = Glide.with(holder.itemView.context)
            .load(doctor.img)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .override(160, 160)
            .centerCrop()
        preloadRequest.preload()

        // Sau khi preload, hiển thị hình ảnh trong onBindViewHolder
        Glide.with(holder.itemView.context)
            .load(doctor.img)
            .override(160, 160)
            .centerCrop()
            .into(holder.imgDoctor)
        holder.txtDoctorName.text = doctor.name
        holder.txtDoctorSpecialization.text = doctor.specialist
    }
}