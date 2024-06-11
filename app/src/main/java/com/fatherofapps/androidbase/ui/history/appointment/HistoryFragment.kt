package com.fatherofapps.androidbase.ui.history.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.DateAppointment
import com.fatherofapps.androidbase.databinding.FragmentHistoryAppointmentBinding
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment @Inject constructor(): BaseFragment(){
    private lateinit var dataBinding: FragmentHistoryAppointmentBinding
    private val viewModel by viewModels<HistoryViewModel>()
    private var appointmentOnlList: List<DateAppointment> = emptyList()
    private var appointmentOffList: List<DateAppointment> = emptyList()
    private var dayAdapter: DayAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAppointments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        dataBinding = FragmentHistoryAppointmentBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        setupObservers()
        setupCardOnl()
        setupCardOff()
        selectCard(dataBinding.cardOnline)
        selectText(dataBinding.txtOnline)
        dataBinding.btnBookNow.setOnClickListener {
            val action = HistoryFragmentDirections.actionHistoryFragmentToFragmentTopDoctor("all")
            navigateToPage(action)
        }
    }

    private fun setupObservers() {
        viewModel.AppointmentsResponse.observe(viewLifecycleOwner) { response ->
            response?.let { appointmentResponse ->
                if (appointmentResponse.data != null && appointmentResponse.isSuccess()) {
                    val appointmentData = appointmentResponse.data
                    appointmentOnlList = appointmentData.online
                    appointmentOffList = appointmentData.offline
                    if (appointmentOnlList.isNotEmpty()) {
                        dataBinding.rvHistory.visibility = View.VISIBLE
                        dataBinding.layoutnoAppointment.visibility = View.GONE
                        setupRecyclerView(appointmentOnlList)
                    } else {
                        dataBinding.rvHistory.visibility = View.GONE
                        dataBinding.layoutnoAppointment.visibility = View.VISIBLE

                    }
                } else {
                    showErrorMessage(appointmentResponse.message ?: "Lỗi không xác định")

                }
            }
        }
    }

    private fun setupCardOnl() {
        dataBinding.cardOnline.setOnClickListener {
            selectCard(dataBinding.cardOnline)
            selectText(dataBinding.txtOnline)
            deselectCard(dataBinding.cardOffline)
            deselectText(dataBinding.txtOffline)
            if (appointmentOnlList.isNotEmpty()) {
                dataBinding.rvHistory.visibility = View.VISIBLE
                dataBinding.layoutnoAppointment.visibility = View.GONE
                setupRecyclerView(appointmentOnlList)
            } else {
                dataBinding.rvHistory.visibility = View.GONE
                dataBinding.layoutnoAppointment.visibility = View.VISIBLE
            }
        }
    }
    private fun setupCardOff() {
        dataBinding.cardOffline.setOnClickListener {
            selectCard(dataBinding.cardOffline)
            selectText(dataBinding.txtOffline)
            deselectCard(dataBinding.cardOnline)
            deselectText(dataBinding.txtOnline)
            if (appointmentOffList.isNotEmpty()) {
                dataBinding.rvHistory.visibility = View.VISIBLE
                dataBinding.layoutnoAppointment.visibility = View.GONE
                setupRecyclerView(appointmentOffList)
            } else {
                dataBinding.rvHistory.visibility = View.GONE
                dataBinding.layoutnoAppointment.visibility = View.VISIBLE
            }

        }
    }
    private fun setupRecyclerView(appointment: List<DateAppointment>) {
        dayAdapter = DayAdapter(appointment)
        dataBinding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = dayAdapter
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
}