package com.fatherofapps.androidbase.ui.appointment.booking

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.models.AppointmentInfo
import com.fatherofapps.androidbase.data.models.TimeSlotInfo
import com.fatherofapps.androidbase.data.response.ListTime
import com.fatherofapps.androidbase.data.response.doctorPrice
import com.fatherofapps.androidbase.databinding.FragmentBookAppointmentStep2Binding
import com.fatherofapps.androidbase.utils.convertToNormalTime
import com.fatherofapps.androidbase.utils.convertToVietNamDate
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
@AndroidEntryPoint
class BookingAppointmentFragment @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentBookAppointmentStep2Binding
    private var timeSlotAdapter: TimeSlotAdapter? = null
    private val args by navArgs<BookingAppointmentFragmentArgs>()
    private val viewModel by viewModels<BookingAppointmentViewModel>()
    private var timeSlot: List<ListTime> = emptyList()

    private var timeSlotInfo: TimeSlotInfo? = null
    private var appointmentInfo: AppointmentInfo? = null
    companion object {
        private const val TAG = "BookingAppointmentFragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDoctorTimeSlot(args.doctorId, args.day)
        viewModel.getDoctorPrice(args.doctorId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentBookAppointmentStep2Binding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        (activity as MainActivity).setTitle("Đặt Lịch Hẹn")
        (activity as MainActivity).setNavigationBackIcon()
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenNavigation(false)
        setupObservers()
        setupPrice()
        setupCardOnline()
        setupCardOffline()

        dataBinding.TxtDateSchedule.text = convertToVietNamDate(args.day)
        dataBinding.nextButton.setOnClickListener {
            if (timeSlotInfo == null) {
                Toast.makeText(context, "Vui lòng chọn thời gian", Toast.LENGTH_SHORT).show()
            } else {
                val price = if (timeSlotInfo!!.service == "Online") {
                    convertCurrencyStringToInteger(dataBinding.txtPriceOnline.text.toString())
                } else {
                    convertCurrencyStringToInteger(dataBinding.tvPrice.text.toString())
                }

                appointmentInfo = AppointmentInfo(
                    doctorId = args.doctorId,
                    day = args.day,
                    time = timeSlotInfo!!.normalTime,
                    price = price,
                    service = timeSlotInfo!!.service
                )

                val action = BookingAppointmentFragmentDirections.actionFragmentBookingAppointmentToPatientDetailFragment(appointmentInfo!!)
                navigateToPage(action)

                Log.d(TAG, "appointmentInfo: $appointmentInfo")
        }
        }
    }

    private fun setupRecyclerView() {
        timeSlotAdapter = TimeSlotAdapter(timeSlot)
        timeSlotAdapter?.onItemClickListener = { timeSlotInfo ->
            this.timeSlotInfo = timeSlotInfo
        }
        dataBinding.rvTimeSlot.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        dataBinding.rvTimeSlot.adapter = timeSlotAdapter

    }
    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObservers() {
        viewModel.doctorTimeSlotResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.isSuccess()) {
                Log.d(TAG, "DoctorTimeSlot: ${response.data} ")
                response.data?.let { data ->
                    timeSlot = filterTimeSlots(args.day, data)
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

    private fun setupCardOnline() {
        dataBinding.cardOnline.setOnClickListener {
            timeSlotAdapter?.updateCardOnlineStatus(true)
            timeSlotAdapter?.updateCardOfflineStatus(false)
            timeSlotAdapter?.resetItemStatus()
            selectCard(dataBinding.cardOnline)
            selectText(dataBinding.txtOnline)
            deselectCard(dataBinding.cardOffline)
            deselectText(dataBinding.txtOffline)
            selectCard(dataBinding.VideoCardView)
            selectText(dataBinding.txtVideo)
            selectText(dataBinding.txtDescription1)
            selectText(dataBinding.txtPriceOnline)
            deselectCard(dataBinding.OfflineCardView)
            blackText(dataBinding.tvOffline)
            blackText(dataBinding.tvDescription)
            deselectText(dataBinding.tvPrice)
        }
    }
    private fun setupCardOffline() {
        dataBinding.cardOffline.setOnClickListener{
            timeSlotAdapter?.updateCardOfflineStatus(true)
            timeSlotAdapter?.updateCardOnlineStatus(false)
            timeSlotAdapter?.resetItemStatus()
            selectCard(dataBinding.cardOffline)
            selectText(dataBinding.txtOffline)
            deselectCard(dataBinding.cardOnline)
            deselectText(dataBinding.txtOnline)
            selectCard(dataBinding.OfflineCardView)
            selectText(dataBinding.tvOffline)
            selectText(dataBinding.tvDescription)
            selectText(dataBinding.tvPrice)
            deselectCard(dataBinding.VideoCardView)
            blackText(dataBinding.txtVideo)
            blackText(dataBinding.txtDescription1)
            deselectText(dataBinding.txtPriceOnline)
        }
    }


    private fun selectCard(cardView: MaterialCardView) {
        cardView.setCardBackgroundColor(resources.getColor(R.color.color_card_selected))
    }

    private fun deselectCard(cardView: MaterialCardView) {
        cardView.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
    }

    private fun selectText(textView: TextView) {
        textView.setTextColor(resources.getColor(R.color.color_text_selected))
    }
    private fun deselectText(textView: TextView) {
        textView.setTextColor(resources.getColor(R.color.color_text_unselected))
    }

    private fun blackText(textView: TextView) {
        textView.setTextColor(resources.getColor(R.color.black))
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupPrice() {
        viewModel.doctorPriceResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.isSuccess()) {
                Log.d(TAG, "DoctorPrice: ${response.data} ")
                response.data?.let { data ->
                    dataBinding.tvPrice.text = convertToCurrencyFormat(data.offlinePrice)
                    dataBinding.txtPriceOnline.text = convertToCurrencyFormat(data.onlinePrice)
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
    private fun isToday(day: String): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val inputDate = dateFormat.parse(day)
        calendar.time = inputDate
        val inputDay = calendar.get(Calendar.DAY_OF_MONTH)
        val inputMonth = calendar.get(Calendar.MONTH)
        val inputYear = calendar.get(Calendar.YEAR)
        return today == inputDay && month == inputMonth && year == inputYear
    }
    private fun getTimeSlotEndTime(timeRange: String): LocalTime {
        val startTimeStr = timeRange.split(" - ")[0]
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return LocalTime.parse(startTimeStr, formatter)
    }

    private fun filterTimeSlots(day: String, listTime: List<ListTime>): List<ListTime> {
        val filteredList = mutableListOf<ListTime>()
        val isToday = isToday(day)
        Log.d(TAG, "isToday: $isToday")

        if (isToday) {
            val currentTime = LocalTime.now()
            for (timeSlot in listTime) {
                val startTime = getTimeSlotEndTime(convertToNormalTime(timeSlot.timeSlot))
                Log.d(TAG, "startTime: $startTime")
                if (currentTime.isBefore(startTime)) {
                    filteredList.add(timeSlot)
                }
            }
        } else {
            filteredList.addAll(listTime)
        }
        Log.d(TAG, "filteredList: $filteredList")
        return filteredList
    }

}
