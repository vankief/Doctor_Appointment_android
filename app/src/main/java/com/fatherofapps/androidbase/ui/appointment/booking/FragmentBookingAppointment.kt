package com.fatherofapps.androidbase.ui.appointment.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.models.TimeSlot
import com.fatherofapps.androidbase.databinding.FragmentBookAppointmentStep2Binding
import com.fatherofapps.androidbase.databinding.FragmentDoctorDetailBinding

class FragmentBookingAppointment: BaseFragment() {
    private lateinit var dataBinding: FragmentBookAppointmentStep2Binding
    private var adapter: TimeSlotAdapter? = null
    private var timeSlotList: List<TimeSlot> = mutableListOf(
        TimeSlot( "09:00 - 10:00"),
        TimeSlot( "10:00 - 11:00"),
        TimeSlot( "11:00 - 12:00"),
        TimeSlot( "13:00 - 14:00"),
        TimeSlot( "14:00 - 15:00"),
        TimeSlot( "15:00 - 16:00"),
        TimeSlot( "16:00 - 17:00"),
        TimeSlot( "17:00 - 18:00"),
    )


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
        adapter = TimeSlotAdapter(timeSlotList)
        dataBinding.rvTimeSlot.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        dataBinding.rvTimeSlot.adapter = adapter
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