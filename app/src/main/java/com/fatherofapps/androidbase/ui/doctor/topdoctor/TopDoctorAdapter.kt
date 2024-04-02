package com.fatherofapps.androidbase.ui.doctor.topdoctor

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.response.TopDoctorBySpecialist
import com.google.android.material.card.MaterialCardView

class TopDoctorAdapter(
    private val topDoctorList: List<TopDoctorBySpecialist>,
): RecyclerView.Adapter<TopDoctorAdapter.TopDoctorViewHolder>(){
    inner class TopDoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDoctorName: TextView = itemView.findViewById(R.id.txtDoctorName)
        val txtDoctorSpecialist: TextView = itemView.findViewById(R.id.txtSpecialistName)
        val txtDoctorRating: TextView = itemView.findViewById(R.id.txtTotalRating)
        val txtDoctorReview: TextView = itemView.findViewById(R.id.txtTotalReview)
        val favorite: ImageView = itemView.findViewById(R.id.favoriteIcon)
        val avtarDoctor: ImageView = itemView.findViewById(R.id.doctorAvatar)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopDoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_doctor_item, parent, false)
        return TopDoctorViewHolder(view)
    }
    override fun getItemCount(): Int {
        return topDoctorList.size
    }
    override fun onBindViewHolder(holder: TopDoctorViewHolder, position: Int) {
        val currentItem = topDoctorList[position]
        holder.txtDoctorName.text = currentItem.name
        val preloadRequest: RequestBuilder<Drawable> = Glide.with(holder.itemView.context)
            .load(currentItem.img)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .override(80, 80)
            .centerCrop()
        preloadRequest.preload()

        // Sau khi preload, hiển thị hình ảnh trong onBindViewHolder
        Glide.with(holder.itemView.context)
            .load(currentItem.img)
            .override(80, 80)
            .centerCrop()
            .into(holder.avtarDoctor)
        holder.txtDoctorSpecialist.text = currentItem.specialist
        holder.txtDoctorRating.text = currentItem.averageRating.toString()
        holder.txtDoctorReview.text = currentItem.totalReviews.toString()
        if (currentItem.isFavorite) {
        } else {
        }
    }
}
