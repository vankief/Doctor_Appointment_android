package com.fatherofapps.androidbase.ui.appointment.doctordetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentDoctorDetailBinding

class FragmentDoctorDetail: BaseFragment() {
    private lateinit var dataBinding: FragmentDoctorDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentDoctorDetailBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.bookAppointmentButton.setOnClickListener {
            val action = FragmentDoctorDetailDirections.actionFragmentDoctorDetailToFragmentBookingAppointment()
            navigateToPage(action)
        }
    }
}