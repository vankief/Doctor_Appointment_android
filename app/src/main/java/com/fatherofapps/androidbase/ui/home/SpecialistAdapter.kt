package com.fatherofapps.androidbase.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.data.models.Specialist
import com.fatherofapps.androidbase.data.response.SpecialistDoctor
import java.lang.Integer.min

class SpecialistAdapter(
    private val specialists: List<SpecialistDoctor>
): RecyclerView.Adapter<SpecialistAdapter.SpecialistViewHolder>() {
    inner class SpecialistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSpecialist: ImageView = itemView.findViewById(R.id.imgSpecializationIcon)
        val txtSpecialistName: TextView = itemView.findViewById(R.id.txtSpecializationName)
        val txtDoctorCount: TextView = itemView.findViewById(R.id.txtDoctorCount)

        init {
            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToSpecialistFragment()
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_specialization_card, parent, false)
        return SpecialistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return min(specialists.size, 5)
    }

    override fun onBindViewHolder(holder: SpecialistViewHolder, position: Int) {
        val specialist = specialists[position]
        //holder.imgSpecialist.setImageResource(specialist.imageUrl)
        holder.txtSpecialistName.text = specialist.name
        holder.txtDoctorCount.text = specialist.numberOfDoctors.toString()
    }

}