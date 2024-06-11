package com.fatherofapps.androidbase.ui.history.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.AppointmentDetail
import com.fatherofapps.androidbase.databinding.FragmentHistoryDetailBinding
import com.fatherofapps.androidbase.utils.convertImagePath
import com.fatherofapps.androidbase.utils.convertStatusToVietnamese
import com.fatherofapps.androidbase.utils.convertToVietNamDate
import com.fatherofapps.androidbase.utils.extractParagraphTags
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class HistoryDetailFragment @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentHistoryDetailBinding
    private val viewModel by viewModels<HistoryDetailViewModel>()
    private val args by navArgs<HistoryDetailFragmentArgs>()
    private var appointmentDetail : AppointmentDetail? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPatientAppointments(args.appointmentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        dataBinding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenTopAppBar(true)
        hideOpenNavigation(false)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.AppointmentsResponse.observe(viewLifecycleOwner) { response ->
            if (response != null && response.isSuccess()) {
                appointmentDetail = response.data
                appointmentDetail?.let { setupAppointmentDetail(it) }
            } else {
                showErrorMessage(response?.message ?: "Lỗi không xác định")
            }
        };
    }
    private fun setupAppointmentDetail(appointmentDetail: AppointmentDetail) {
        dataBinding.txtDoctorName.text = appointmentDetail.doctorName
        Glide.with(requireContext())
            .load(convertImagePath(appointmentDetail.doctorImage))
            .into(dataBinding.doctorAvatar)
        dataBinding.txtService.text = appointmentDetail.service
        dataBinding.txtServiceCard.text = appointmentDetail.service
        dataBinding.txtStatus.text = " - " + convertStatusToVietnamese(appointmentDetail.status)
        dataBinding.txtScheduleTime.text = appointmentDetail.scheduleTime
        dataBinding.txtScheduleTimeCard.text = appointmentDetail.scheduleTime

        dataBinding.imgService.setImageResource(
            if (appointmentDetail.service == "Online") R.drawable.ic_videocall else R.drawable.chat_rounded
        )

        dataBinding.appointmentCountTextView.text = appointmentDetail.totalPatients.toString() + " + "
        dataBinding.reviewCountTextView.text = appointmentDetail.totalReviews.toString() + " + "
        dataBinding.experienceCountTextView.text = appointmentDetail.experience + "+"
        dataBinding.txtScheduleDate.text = convertToVietNamDate(appointmentDetail.scheduleDate)
        dataBinding.txtPatientName.text = appointmentDetail.patientName
        dataBinding.txtPatientPhone.text = appointmentDetail.patientPhone
        dataBinding.txtPatientAge.text = appointmentDetail.patientAge
        dataBinding.txtReason.text = appointmentDetail.reason
        dataBinding.txtResult.text = extractParagraphTags(appointmentDetail.conclusion)

            // Kiểm tra xem có thể đánh giá cuộc hẹn không (trong vòng 7 ngày kể từ ngày hẹn)
            val canRate = checkReviewValidity(appointmentDetail.scheduleDate, appointmentDetail.scheduleTime)
            // Nếu có thể đánh giá và chưa đánh giá, hiện nút đánh giá
            dataBinding.btnReview.visibility = if (canRate && !appointmentDetail.isRate) View.VISIBLE else View.GONE
            setupReviewButton()
        }

    private fun checkReviewValidity(scheduleDate: String, scheduleTime: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val timeSlotEndTime = getTimeSlotStartTime(scheduleTime)
        val scheduleDateTime = LocalDateTime.parse("$scheduleDate $timeSlotEndTime", formatter)
        val now = LocalDateTime.now()
        return now.isBefore(scheduleDateTime.plusDays(7))
    }

    // Lấy thời gian bắt đầu của khoảng thời gian hẹn
    private fun getTimeSlotStartTime(timeRange: String): LocalTime {
        val startTimeStr = timeRange.split(" - ")[1]
        return LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
    }

    private fun setupReviewButton() {
        dataBinding.btnReview.setOnClickListener {
            appointmentDetail?.let {
                val action = HistoryDetailFragmentDirections.actionHistoryDetailFragmentToReviewFragment(args.appointmentId)
                navigateToPage(action)
            }
        }
    }
}