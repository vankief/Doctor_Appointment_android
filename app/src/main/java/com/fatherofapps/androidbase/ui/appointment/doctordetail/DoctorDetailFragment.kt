package com.fatherofapps.androidbase.ui.appointment.doctordetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.DoctorInfo
import com.fatherofapps.androidbase.databinding.FragmentDoctorDetailBinding
import com.fatherofapps.androidbase.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DoctorDetailFragment  @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentDoctorDetailBinding
    private val args: DoctorDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<DoctorDetailViewModel>()
    private lateinit var doctorInfo: DoctorInfo
    private lateinit var adapter: ScheduleAdapter
    private var selectedDate: String = ""

    companion object {
        private const val TAG = "DoctorDetailFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDoctorDetail(args.doctorId)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentDoctorDetailBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        adapter = ScheduleAdapter()
        adapter.onItemClickListener = { selectedDate ->
            // Gán selectedDate vào biến trong Fragment
            this.selectedDate = convertDateFormat(selectedDate)
        }
        dataBinding.rvSchedule.apply {
            layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false) }
        dataBinding.rvSchedule.adapter = adapter
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenNavigation(false)
        setupObservers()
        dataBinding.bookAppointmentButton.setOnClickListener {
            if (selectedDate.isEmpty()) {
                Toast.makeText(context, "Vui lòng chọn ngày hẹn", Toast.LENGTH_SHORT).show()
            } else {
                val action = DoctorDetailFragmentDirections.actionFragmentDoctorDetailToFragmentBookingAppointment(
                    doctorInfo.id,
                    selectedDate
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun setupDoctorInfo(doctorInfo: DoctorInfo) {
        this.doctorInfo = doctorInfo
        dataBinding.doctorNameTextView.text = "Bác Sĩ ${doctorInfo.name}"
        dataBinding.specializationTextView.text = doctorInfo.specialist
        dataBinding.doctorDescriptionTextView.text = doctorInfo.description
        dataBinding.appointmentCountTextView.text = doctorInfo.totalPatients.toString()
        dataBinding.reviewTextView.text = "${doctorInfo.averageRating} (${doctorInfo.totalReviews})"
        dataBinding.experienceCountTextView.text = doctorInfo.experience
        dataBinding.reviewCountTextView.text = doctorInfo.totalReviews.toString()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObservers() {
        viewModel.doctorDetailResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response == null) {
                showErrorMessage("Network error")
            } else {
                if (response.isSuccess()) {
                    Log.d(TAG, "DoctorInfo: ${response.data} ")
                    response.data?.let { data ->
                        setupDoctorInfo(data)
                    }

                } else {
                    showErrorMessage(response.checkTypeErr())
                }
            }
        })
    }



}