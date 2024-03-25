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
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.ListTime
import com.fatherofapps.androidbase.databinding.FragmentBookAppointmentStep2Binding
import com.fatherofapps.androidbase.utils.convertToVietNamDate
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class BookingAppointmentFragment @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentBookAppointmentStep2Binding
    private var timeSlotAdapter: TimeSlotAdapter? = null
    private val args by navArgs<BookingAppointmentFragmentArgs>()

    private val viewModel by viewModels<BookingAppointmentViewModel>()

    private var timeSlot: List<ListTime> = emptyList()

    private var selectedTime: String = ""
    companion object {
        private const val TAG = "BookingAppointmentFragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDoctorTimeSlot(args.doctorId, args.day)
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
        hideOpenNavigation(false)
        setupObservers()
        setupCardOnline()
        setupCardOffline()
        dataBinding.TxtDateSchedule.text = convertToVietNamDate(args.day)
        timeSlotAdapter?.onItemClickListener = { selectedTime ->
            this.selectedTime = selectedTime
            Log.d(TAG, "Selected time: $selectedTime")
        }

        dataBinding.nextButton.setOnClickListener {
            if (selectedTime.isNotEmpty()) {
            Toast.makeText(context, "Selected time: $selectedTime", Toast.LENGTH_SHORT).show()
            } else {
                showErrorMessage("Vui lòng chọn thời gian hẹn")
            }
        }
    }

    private fun setupRecyclerView() {
        timeSlotAdapter = TimeSlotAdapter(timeSlot, dataBinding.rvTimeSlot)
        dataBinding.rvTimeSlot.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        dataBinding.rvTimeSlot.adapter = timeSlotAdapter
    }
    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObservers() {
        viewModel.doctorTimeSlottResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response == null) {
                showErrorMessage("Network error")
            } else {
                if (response.isSuccess()) {
                    Log.d(TAG, "ListTime: ${response.data} ")
                    response.data?.let { data ->
                        timeSlot = data
                        setupRecyclerView()
                    }

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
}