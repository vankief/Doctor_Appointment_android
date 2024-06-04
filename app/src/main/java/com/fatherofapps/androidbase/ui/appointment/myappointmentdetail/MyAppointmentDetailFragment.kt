package com.fatherofapps.androidbase.ui.appointment.myappointmentdetail

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.AppointmentDetail
import com.fatherofapps.androidbase.databinding.FragmentMyAppointmentDetailBinding
import com.fatherofapps.androidbase.utils.EStatus
import com.fatherofapps.androidbase.utils.EStatus.*
import com.fatherofapps.androidbase.utils.convertImagePath
import com.fatherofapps.androidbase.utils.convertStatusToVietnamese
import com.fatherofapps.androidbase.utils.convertToVietNamDate
import dagger.hilt.android.AndroidEntryPoint
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.BroadcastIntentHelper
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class MyAppointmentDetailFragment  @Inject constructor():BaseFragment() {
        private lateinit var dataBinding: FragmentMyAppointmentDetailBinding
        private val viewModel by viewModels<MyAppointmentDetailViewModel>()
        private val args by navArgs<MyAppointmentDetailFragmentArgs>()
        private var appointmentDetail : AppointmentDetail? = null
        private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPatientAppointments(args.appointmentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentMyAppointmentDetailBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        (activity as MainActivity).setTitle("Chi tiết cuộc hẹn")
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenTopAppBar(true)
        hideOpenNavigation(false)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        registerForBroadcastMessages()
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

        // Kiểm tra nếu dịch vụ là "online" và thời gian hẹn đã đến
        if (appointmentDetail.service == "Online") {
            dataBinding.imgService.setImageResource(R.drawable.ic_videocall)
        } else {
            dataBinding.imgService.setImageResource(R.drawable.chat_rounded)
        }

        dataBinding.appointmentCountTextView.text = appointmentDetail.totalPatients.toString() + " + "
        dataBinding.reviewCountTextView.text = appointmentDetail.totalReviews.toString() + " + "
        dataBinding.experienceCountTextView.text = appointmentDetail.experience + "+"
        dataBinding.txtScheduleDate.text = convertToVietNamDate(appointmentDetail.scheduleDate)
        dataBinding.txtPatientName.text = appointmentDetail.patientName
        dataBinding.txtPatientPhone.text = appointmentDetail.patientPhone
        dataBinding.txtPatientAge.text = appointmentDetail.patientAge

        // Set payment text dựa trên status của appointment
        when (appointmentDetail.status) {
            AWAITING_PAYMENT -> dataBinding.txtPayment.text = "" + convertToCurrencyFormat(appointmentDetail.fee) + " - Chờ thanh toán"
            APPROVED -> {
                if (appointmentDetail.service == "Online") {
                    dataBinding.txtPayment.text = "" + convertToCurrencyFormat(appointmentDetail.fee) + " - Đã thanh toán"
                    dataBinding.btnCallVideo.visibility = if (checkTime(appointmentDetail.scheduleTime, appointmentDetail.scheduleDate)) View.VISIBLE else View.GONE
                    dataBinding.btnCallVideo.setText("Gọi video ngay (Bắt đầu lúc ${appointmentDetail.scheduleTime.split(" - ")[0]})")
                    setupVideoCallButton()

                } else {
                    dataBinding.txtPayment.text = "" + convertToCurrencyFormat(appointmentDetail.fee) + " - Đã thanh toán"
                }
            }
            else -> {
                dataBinding.txtPayment.text = "" + convertToCurrencyFormat(appointmentDetail.fee) + " - Đã bị hủy"}
        }
    }
    private fun checkTime(scheduleTime: String, scheduleDate: String): Boolean {
        val currentDateTime = LocalDateTime.now()
        val timeSlotEndTime = getTimeSlotEndTime(scheduleTime)  // Hàm này trả về thời gian kết thúc
        val scheduleDateTime = LocalDateTime.parse("$scheduleDate $timeSlotEndTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        return currentDateTime.isBefore(scheduleDateTime)
    }

    private fun getTimeSlotEndTime(timeRange: String): LocalTime {
        val endTimeStr = timeRange.split(" - ")[1]  // Giả sử chuỗi có định dạng "start - end"
        return LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
    }


    private fun setupVideoCallButton() {
        dataBinding.btnCallVideo.setOnClickListener {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_JOINED.action)
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, intentFilter)
            val serverUrl = "https://8x8.vc/vpaas-magic-cookie-81ab7c0a26124f9c9a85e4294b6cd2ac" // Sử dụng URL tương tự như trên FE
            val roomId = "/roomId${appointmentDetail?.appointmentId}" // Sử dụng room ID tương tự như trên FE
            val token = "eyJraWQiOiJ2cGFhcy1tYWdpYy1jb29raWUtODFhYjdjMGEyNjEyNGY5YzlhODVlNDI5NGI2Y2QyYWMvNjVhMjRiLVNBTVBMRV9BUFAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJqaXRzaSIsImlzcyI6ImNoYXQiLCJpYXQiOjE3MTY0NDc5MDQsImV4cCI6MTcxNjQ1NTEwNCwibmJmIjoxNzE2NDQ3ODk5LCJzdWIiOiJ2cGFhcy1tYWdpYy1jb29raWUtODFhYjdjMGEyNjEyNGY5YzlhODVlNDI5NGI2Y2QyYWMiLCJjb250ZXh0Ijp7ImZlYXR1cmVzIjp7ImxpdmVzdHJlYW1pbmciOnRydWUsIm91dGJvdW5kLWNhbGwiOnRydWUsInNpcC1vdXRib3VuZC1jYWxsIjpmYWxzZSwidHJhbnNjcmlwdGlvbiI6dHJ1ZSwicmVjb3JkaW5nIjp0cnVlfSwidXNlciI6eyJoaWRkZW4tZnJvbS1yZWNvcmRlciI6ZmFsc2UsIm1vZGVyYXRvciI6dHJ1ZSwibmFtZSI6InVvbmd0aGl2YW5raWV1MDkwNCIsImlkIjoiZ29vZ2xlLW9hdXRoMnwxMTQ5Mzc5NTc0NTM5OTc5MDE2NzgiLCJhdmF0YXIiOiIiLCJlbWFpbCI6InVvbmd0aGl2YW5raWV1MDkwNEBnbWFpbC5jb20ifX0sInJvb20iOiIqIn0.XYY-QYzgRSdZAA3OXtwOo7QmTfEy8roK4NV4AlIfXFCjPEu7tmNW5u6g-5L-otRVdNuZuK_yxVH6f-Da1ip8N7KvCHon-Ti29G7YJ6Mwhc_-NDNTPRNsbGT3nShLP7NKGW-yzbqlmS1_1278kjsGYshN3Tu92ombcUqwu0l4yZZrjLI8P1pf2fzvvkEbI5-xZcylCikWY_TQCL_t1iusq83lkw5m3IlMTMqZbavX5cH8tru3wSgnwhEcLNmRlBe-dYPxV8oYCX53Ra-5aoCw8mLIS88CkfHABZ2ZV5ieSIXrWVWoRaffPAac9xVoihTUEZB5DYjd_jA7a-XDZj-IvQ"

            try {
                val url = URL(serverUrl)
                val defaultOption = JitsiMeetConferenceOptions.Builder()
                    .setServerURL(url)
                    .setToken(token)
                    .build()

                JitsiMeet.setDefaultConferenceOptions(defaultOption)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            val userInfo = JitsiMeetUserInfo()
            userInfo.displayName = appointmentDetail?.patientName
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(roomId)
                .setFeatureFlag("invite.enabled", false)
                .setUserInfo(userInfo)
                .build()
            JitsiMeetActivity.launch(requireActivity(), options)
        }
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.action);
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action);
                ... other events
         */
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, intentFilter)
    }

    // Example for handling different JitsiMeetSDK events
    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.type) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> Timber.i("Conference Joined with url%s", event.getData().get("url"))
                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i("Participant joined%s", event.getData().get("name"))
                else -> Timber.i("Received event: %s", event.type)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
    }

    // Example for sending actions to JitsiMeetSDK
    private fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(hangupBroadcastIntent)
    }

}