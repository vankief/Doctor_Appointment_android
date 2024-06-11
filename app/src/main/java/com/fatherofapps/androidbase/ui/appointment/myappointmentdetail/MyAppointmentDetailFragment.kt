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
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.AppointmentDetail
import com.fatherofapps.androidbase.databinding.FragmentMyAppointmentDetailBinding
import com.fatherofapps.androidbase.ui.appointment.patientdetail.PatientDetailFragmentDirections
import com.fatherofapps.androidbase.utils.EStatus
import com.fatherofapps.androidbase.utils.EStatus.*
import com.fatherofapps.androidbase.utils.convertImagePath
import com.fatherofapps.androidbase.utils.convertStatusToVietnamese
import com.fatherofapps.androidbase.utils.convertToVietNamDate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
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
         lateinit var paymentSheet: PaymentSheet
        lateinit var customerConfig: PaymentSheet.CustomerConfiguration
        lateinit var paymentIntentClientSecret: String
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

        // Initialize Stripe PaymentSheet
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
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
        dataBinding.txtReason.text = appointmentDetail.reason

        // Set payment text dựa trên status của appointment
        when (appointmentDetail.status) {
            AWAITING_PAYMENT -> {
                dataBinding.txtPayment.text = "" + convertToCurrencyFormat(appointmentDetail.fee) + " - Chờ thanh toán"
                if(appointmentDetail.service == "Online" && checkTime(appointmentDetail.scheduleTime, appointmentDetail.scheduleDate)) {
                    dataBinding.btnPayment.visibility = View.VISIBLE
                    dataBinding.btnPayment.setOnClickListener {
                        viewModel.getPaymentDetailOnline(appointmentDetail?.appointmentId!!)
                        fetchPaymentIntent()
                    }
                } else {
                    dataBinding.btnPayment.visibility = View.GONE
                }
            }
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
//            val serverUrl = "https://8x8.vc/vpaas-magic-cookie-81ab7c0a26124f9c9a85e4294b6cd2ac" // Sử dụng URL tương tự như trên FE
            val roomId = "Room Id ${appointmentDetail?.appointmentId}" // Sử dụng room ID tương tự như trên FE
            //val token = "eyJraWQiOiJ2cGFhcy1tYWdpYy1jb29raWUtODFhYjdjMGEyNjEyNGY5YzlhODVlNDI5NGI2Y2QyYWMvNjVhMjRiLVNBTVBMRV9BUFAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJqaXRzaSIsImlzcyI6ImNoYXQiLCJpYXQiOjE3MTgwMzU3MTYsImV4cCI6MTcxODA0MjkxNiwibmJmIjoxNzE4MDM1NzExLCJzdWIiOiJ2cGFhcy1tYWdpYy1jb29raWUtODFhYjdjMGEyNjEyNGY5YzlhODVlNDI5NGI2Y2QyYWMiLCJjb250ZXh0Ijp7ImZlYXR1cmVzIjp7ImxpdmVzdHJlYW1pbmciOnRydWUsIm91dGJvdW5kLWNhbGwiOnRydWUsInNpcC1vdXRib3VuZC1jYWxsIjpmYWxzZSwidHJhbnNjcmlwdGlvbiI6dHJ1ZSwicmVjb3JkaW5nIjp0cnVlfSwidXNlciI6eyJoaWRkZW4tZnJvbS1yZWNvcmRlciI6ZmFsc2UsIm1vZGVyYXRvciI6dHJ1ZSwibmFtZSI6InVvbmd0aGl2YW5raWV1MDkwNCIsImlkIjoiZ29vZ2xlLW9hdXRoMnwxMTQ5Mzc5NTc0NTM5OTc5MDE2NzgiLCJhdmF0YXIiOiIiLCJlbWFpbCI6InVvbmd0aGl2YW5raWV1MDkwNEBnbWFpbC5jb20ifX0sInJvb20iOiIqIn0.IBoch_HU-X0SpjMQxZvtCeFw_v_EgwdxqlSm1v0hDt_XiG_YQ8W2ydNuRQxN7HfK60WtILpPiEbZcY82wFqFRXoOqkqH_muD_I-FVjpwkG8T0dTNog1Q6PLKlHZMKvx5gDnveF3jrdxGoSpFNziUwJNP00PY3Mjo125bRHap-qH2bi4ScOvfC2DqHaZ8hR2YUNO1uhc5X96vnhk6p-HC1H8ugfTP1WoK-xiMB_3nGAF9OyH8zoM8HsOpsWWMA7cAHu32rfVYV4hvO2mdkbjLZTVgbrkwxKoJgPo9KGI3EjzujuLnWYlvIKkJ4rxnnhV77PlwUHJ7ZJnAnGxRv4819w"
            try {
                val url = URL("https://meet.jit.si")
                val defaultOption = JitsiMeetConferenceOptions.Builder()
                    .setServerURL(url)
//                    .setToken(token)
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
    private fun fetchPaymentIntent() {
        viewModel.paymentDetailOnlineResponse.observe(viewLifecycleOwner) { response ->
            if (response.data != null && response.isSuccess()) {
                val paymentDetail = response.data
                val paymentIntent = paymentDetail.paymentIntent
                val ephemeralKey = paymentDetail.ephemeralKey
                val customer = paymentDetail.customer
                val publishableKeyFromServer = paymentDetail.publishableKey
                paymentIntentClientSecret = paymentIntent
                customerConfig = PaymentSheet.CustomerConfiguration(customer, ephemeralKey)
                val publishableKey = publishableKeyFromServer
                PaymentConfiguration.init(requireContext(), publishableKey)
                if (paymentIntentClientSecret != null) {
                    presentPaymentSheet()
                } else {
                    Toast.makeText(
                        context,
                        "Lỗi: Không thể tạo phiếu thanh toán",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else {
                showErrorMessage(response?.message ?: "Lỗi không xác định")
            }
        }


    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
                Toast.makeText(context, "Thanh toán bị hủy", Toast.LENGTH_LONG).show()
            }

            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
                Toast.makeText(context, "Thanh toán thất bại", Toast.LENGTH_LONG).show()

            }

            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
                Toast.makeText(context, "Thanh toán thành công", Toast.LENGTH_LONG).show()
                viewModel.getPatientAppointments(args.appointmentId)
                dataBinding.btnPayment.visibility = View.GONE
            }
        }
    }
    private fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret!!,
            PaymentSheet.Configuration(
                merchantDisplayName = "Example",
                customer = customerConfig,
            )
        )
    }
}