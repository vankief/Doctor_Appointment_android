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
import com.bumptech.glide.Glide
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.DoctorInfo
import com.fatherofapps.androidbase.databinding.FragmentDoctorDetailBinding
import com.fatherofapps.androidbase.ui.home.HomeFragment
import com.fatherofapps.androidbase.utils.convertImagePath
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DoctorDetailFragment  @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentDoctorDetailBinding
    private val args: DoctorDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<DoctorDetailViewModel>()
    private lateinit var doctorInfo: DoctorInfo
    private lateinit var adapter: ScheduleAdapter
    private var doctorScheduleList: List<String> = emptyList()
    private var selectedDate: String = ""

    companion object {
        private const val TAG = "DoctorDetailFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDoctorDetail(args.doctorId)
        viewModel.getDoctorScheduleDay(args.doctorId)
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

    private fun setupRecyclerView() {
        adapter = ScheduleAdapter(doctorScheduleList)
        adapter.onItemClickListener = { selectedDate ->
            // Gán selectedDate vào biến trong Fragment
            this.selectedDate = selectedDate
        }
        dataBinding.rvSchedule.apply {
            layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false) }
        dataBinding.rvSchedule.adapter = adapter
    }

    private fun setupDoctorInfo(doctorInfo: DoctorInfo) {
        this.doctorInfo = doctorInfo
        dataBinding.doctorNameTextView.text = "Bác Sĩ ${doctorInfo.name}"
        dataBinding.specializationTextView.text = doctorInfo.specialist
        dataBinding.doctorDescriptionTextView.text = doctorInfo.description
        dataBinding.appointmentCountTextView.text = doctorInfo.totalPatients.toString()
        var totalRating = String.format("%.1f", doctorInfo.averageRating)
        dataBinding.reviewTextView.text = "$totalRating (${doctorInfo.totalReviews})"
        dataBinding.experienceCountTextView.text = doctorInfo.experience
        dataBinding.reviewCountTextView.text = doctorInfo.totalReviews.toString()
        // Load image using Glide
        Glide.with(this)
            .load(convertImagePath(doctorInfo.img)) // Đường dẫn của ảnh
            .centerCrop() // Hiển thị ảnh theo tỉ lệ ảnh gốc
            .placeholder(R.drawable.ic_avt_doctor) // Ảnh placeholder
            .error(R.drawable.ic_avt_doctor) // Ảnh khi load lỗi
            .into(dataBinding.doctorImageView) // ImageView để hiển thị ảnh
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObservers() {
        viewModel.doctorDetailResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response !== null && response.isSuccess()) {
                Log.d(TAG, "DoctorDetail: ${response.data} ")
                response.data?.let { data ->
                    setupDoctorInfo(data)
                }
            } else {
                if (response == null) {
                    showErrorMessage("Lỗi mạng")
                } else {
                    showErrorMessage(response.checkTypeErr())
                }
            }
        })

        viewModel.doctorScheduleDay.observe(viewLifecycleOwner, Observer { response ->
            if (response !== null && response.isSuccess()) {
                Log.d(TAG, "DoctorScheduleDay: ${response.data} ")
                response.data?.let { data ->
                    doctorScheduleList = data
                    setupRecyclerView()
                }
            } else {
                if (response == null) {
                    showErrorMessage("Lỗi mạng")
                } else {
                    showErrorMessage(response.checkTypeErr())
                }
            }
        })
    }



}