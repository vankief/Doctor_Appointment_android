package com.fatherofapps.androidbase.ui.review.all

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.response.ReViewResponse
import com.fatherofapps.androidbase.utils.convertToNormalDate
import de.hdodenhof.circleimageview.CircleImageView

class ReviewAdapter(
    private val review: List<ReViewResponse>
): RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAvatar: CircleImageView = itemView.findViewById(R.id.imgAvatar)
        val txtName: TextView = itemView.findViewById(R.id.txtPatientName)
        val txtDay: TextView = itemView.findViewById(R.id.txtDay)
        val txtReView: TextView = itemView.findViewById(R.id.txtReView1)
        val txtStar: TextView = itemView.findViewById(R.id.txtStar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = View.inflate(parent.context, R.layout.item_review_detail, null)
        return ReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return review.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val currentItem = review[position]
        holder.txtName.text = currentItem.patientName
        holder.txtDay.text = convertToNormalDate(currentItem.createdDate)
        holder.txtReView.text = currentItem.comment
        holder.txtStar.text = currentItem.rating.toString()
        Glide.with(holder.itemView.context)
            .load(currentItem.patientImage)
            .fitCenter()
            .into(holder.imgAvatar)
    }
}