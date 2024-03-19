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
            val selectedDate = adapter?.selectedDate
            if (!selectedDate.isNullOrEmpty()) {
                // Xử lý khi nút được click và có ngày tháng được chọn từ Adapter
                // Ví dụ: Chuyển đến Fragment đặt lịch và gửi ngày tháng năm
                val action = DoctorDetailFragmentDirections.actionFragmentDoctorDetailToFragmentBookingAppointment(selectedDate)
                findNavController().navigate(action)
            } else {
                // Xử lý khi không có ngày tháng được chọn
                Toast.makeText(requireContext(), "Vui lòng chọn ngày trước khi đặt lịch", Toast.LENGTH_SHORT).show()
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