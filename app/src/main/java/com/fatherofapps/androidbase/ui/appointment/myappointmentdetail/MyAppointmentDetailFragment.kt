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
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
@AndroidEntryPoint
class MyAppointmentDetailFragment:BaseFragment() {
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
                if (response == null) showErrorMessage("Lỗi mạng")
                else showErrorMessage(response.checkTypeErr())
            }
        };
    }

    private fun setupAppointmentDetail(appointmentDetail: AppointmentDetail) {
        if (appointmentDetail.service == "online") {
            dataBinding.btnCallVideo.visibility = View.VISIBLE
        } else {
            dataBinding.btnCallVideo.visibility = View.GONE
        }
            dataBinding.txtDoctorName.text = appointmentDetail.doctorName
            Glide.with(requireContext())
            .load(convertImagePath(appointmentDetail.doctorImage))
            .into(dataBinding.doctorAvatar)
            dataBinding.txtService.text = appointmentDetail.service
            dataBinding.txtServiceCard.text = appointmentDetail.service
            dataBinding.txtStatus.text = " - "+ convertStatusToVietnamese(appointmentDetail.status)
            dataBinding.txtScheduleTime.text = appointmentDetail.scheduleTime
            dataBinding.txtScheduleTimeCard.text = appointmentDetail.scheduleTime

            if (appointmentDetail.service == "online") {
                dataBinding.imgService.setImageResource(R.drawable.ic_videocall)
            } else {
                dataBinding.imgService.setImageResource(R.drawable.chat_rounded)
            }
            dataBinding.appointmentCountTextView.text = appointmentDetail.totalPatients.toString() + " + "
            dataBinding.reviewCountTextView.text = appointmentDetail.totalReviews.toString() + " + "
            dataBinding.experienceCountTextView.text = appointmentDetail.experience.replace("năm", "+")
            dataBinding.txtScheduleDate.text = convertToVietNamDate(appointmentDetail.scheduleDate)
            dataBinding.txtPatientName.text = appointmentDetail.patientName
            dataBinding.txtPatientPhone.text = appointmentDetail.patientPhone
            dataBinding.txtPatientAge.text = appointmentDetail.patientAge
            if (appointmentDetail.status == AWAITING_PAYMENT)
                dataBinding.txtPayment.text = "" + convertToCurrencyFormat(appointmentDetail.fee) + " - Chờ thanh toán"
            if ( appointmentDetail.status == APPROVED)
                dataBinding.txtPayment.text = "" + convertToCurrencyFormat(appointmentDetail.fee) + " - Đã thanh toán"
                dataBinding.btnCallVideo.setText("Gọi video ngay (Bắt đầu lúc ${appointmentDetail.scheduleTime.split(" - ")[0]})")
        if (checkTime(appointmentDetail.scheduleTime, appointmentDetail.scheduleDate)) {
            if (appointmentDetail.status != COMPLETED) {
                dataBinding.btnCallVideo.visibility = View.VISIBLE
                setupVideoCallButton()
            } else {
                dataBinding.btnCallVideo.visibility = View.GONE
            }
        } else {
            dataBinding.btnCallVideo.visibility = View.GONE
        }

    }

    private fun checkTime(scheduleTime: String, scheduleDate: String): Boolean {
        val currentDateTime = LocalDateTime.now()
        val timeSlotEndTime = getTimeSlotStartTime(scheduleTime)
        val scheduleDateTime = LocalDateTime.parse("$scheduleDate $timeSlotEndTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        Log.d("MyAppointmentDetailFragment", "currentDateTime: $currentDateTime")
        Log.d("MyAppointmentDetailFragment", "scheduleDateTime: $scheduleDateTime")
        return currentDateTime.isBefore(scheduleDateTime)
    }
    private fun getTimeSlotStartTime(timeRange: String): LocalTime {
        val startTimeStr = timeRange.split(" - ")[1]
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return LocalTime.parse(startTimeStr, formatter)
    }

    private fun setupVideoCallButton() {
        dataBinding.btnCallVideo.setOnClickListener {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_JOINED.action)
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, intentFilter)

            try {
                val serverUrl = URL("https://call.daugiasodep.vn")
                val defaultOption = JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverUrl)
                    .build()
                JitsiMeet.setDefaultConferenceOptions(defaultOption)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(appointmentDetail?.appointmentId)
                .setFeatureFlag("invite.enabled", false)
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

    // Example for sending actions to JitsiMeetSDK
    private fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(hangupBroadcastIntent)
    }

}