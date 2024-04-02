package com.fatherofapps.androidbase.ui.specialist

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.models.TimeSlotInfo
import com.fatherofapps.androidbase.data.response.SpecialistDoctor
import com.google.android.material.card.MaterialCardView
import java.lang.Integer.min

class SpecialistAdapter(
    private val specialists: List<SpecialistDoctor>
): RecyclerView.Adapter<SpecialistAdapter.SpecialistViewHolder>() {


    private val itemColors = listOf(
        R.color.color_specialist_01,
        R.color.color_specialist_02,
        R.color.color_specialist_03,
        // Thêm các màu khác nếu cần
    )
    inner class SpecialistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val materialCardView: MaterialCardView = itemView.findViewById(R.id.cardSpecialization)
        val imgSpecialist: ImageView = itemView.findViewById(R.id.imgSpecializationIcon)
        val txtSpecialistName: TextView = itemView.findViewById(R.id.txtSpecializationName)
        val txtDoctorCount: TextView = itemView.findViewById(R.id.txtDoctorCount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_specialization_card_specialist, parent, false)
        return SpecialistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return specialists.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: SpecialistViewHolder, position: Int) {
        val specialist = specialists[position]
        // Lấy màu tương ứng với vị trí của item
        val colorResId = itemColors[position % itemColors.size]
        val color = holder.itemView.context.getColor(colorResId)
        holder.materialCardView.setCardBackgroundColor(color)
        //holder.imgSpecialist.setImageResource(specialist.imageUrl)
        holder.txtSpecialistName.text = specialist.name
        holder.txtDoctorCount.text = specialist.numberOfDoctors.toString()

        holder.itemView.setOnClickListener() {
            val specialistId = specialist.id
            val action = SpecialistFragmentDirections.actionSpecialistFragmentToFragmentTopDoctor(specialistId)
            holder.itemView.findNavController().navigate(action)
        }
    }

}