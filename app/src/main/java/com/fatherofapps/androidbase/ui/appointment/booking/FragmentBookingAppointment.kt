package com.fatherofapps.androidbase.ui.appointment.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentBookAppointmentStep2Binding
import com.fatherofapps.androidbase.databinding.FragmentDoctorDetailBinding

class FragmentBookingAppointment: BaseFragment() {
    private lateinit var dataBinding: FragmentBookAppointmentStep2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentBookAppointmentStep2Binding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.nextButton.setOnClickListener {
            val action = FragmentBookingAppointmentDirections.actionFragmentBookingAppointmentToFragmentBookingAppointmentStep3()
            navigateToPage(action)
        }
    }
}