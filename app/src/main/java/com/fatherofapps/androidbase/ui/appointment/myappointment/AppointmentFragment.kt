package com.fatherofapps.androidbase.ui.appointment.myappointment

import android.os.Bundle
import android.util.Log
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
import com.fatherofapps.androidbase.databinding.FragmentMyAppointmentBinding
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppointmentFragment: BaseFragment() {
    private lateinit var dataBinding: FragmentMyAppointmentBinding
    private val viewModel by viewModels<AppointmentViewModel>()
    private var appointmentPastList: List<DateAppointment> = emptyList()
    private var appointmentUpComingList: List<DateAppointment> = emptyList()
    private var scheduleDayAdapter: ScheduleDayAdapter? = null
    private val selecUpComing = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAppointments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentMyAppointmentBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenTopAppBar(true)
        hideOpenNavigation(true)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        setupObservers()
        setupCardUpComing()
        setupCardPast()
            selectCard(dataBinding.cardUpComing)
            selectText(dataBinding.txtUpComing)
        dataBinding.btnBookNow.setOnClickListener {
            val action = AppointmentFragmentDirections.actionAppointmentFragmentToFragmentTopDoctor("all")
            navigateToPage(action)
        }
    }

    private fun setupObservers() {
        viewModel.AppointmentsResponse.observe(viewLifecycleOwner) { response ->
            response?.let { appointmentResponse ->
                if (appointmentResponse.data != null && appointmentResponse.isSuccess()) {
                    val appointmentData = appointmentResponse.data
                    appointmentPastList = appointmentData.past
                    appointmentUpComingList = appointmentData.upcoming
                    if (appointmentUpComingList.isNotEmpty()) {
                        dataBinding.recyclerView.visibility = View.VISIBLE
                        dataBinding.layoutnoAppointment.visibility = View.GONE
                        setupRecyclerView(appointmentUpComingList)
                    } else {
                        dataBinding.recyclerView.visibility = View.GONE
                        dataBinding.layoutnoAppointment.visibility = View.VISIBLE
                    }
                } else {
                    if (appointmentResponse == null) showErrorMessage("Lỗi mạng")
                    else showErrorMessage(appointmentResponse.checkTypeErr())
                }
            }
        }

        Log.d("AppointmentFragment", "appointmentPastList: $appointmentPastList")
        Log.d("AppointmentFragment", "appointmentUpComingList: $appointmentUpComingList")
    }

    private fun setupRecyclerView(appointment: List<DateAppointment>) {
        scheduleDayAdapter = ScheduleDayAdapter(appointment)
        dataBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = scheduleDayAdapter
        }
    }
    private fun setupCardUpComing(){
        dataBinding.cardUpComing.setOnClickListener {
            selectCard(dataBinding.cardUpComing)
            deselectCard(dataBinding.cardPast)
            selectText(dataBinding.txtUpComing)
            deselectText(dataBinding.txtPast)
            if (appointmentUpComingList.isNotEmpty()) {
                dataBinding.recyclerView.visibility = View.VISIBLE
                dataBinding.layoutnoAppointment.visibility = View.GONE
                setupRecyclerView(appointmentUpComingList)
            } else {
                dataBinding.recyclerView.visibility = View.GONE
                dataBinding.layoutnoAppointment.visibility = View.VISIBLE
            }
        }
    }
    private fun setupCardPast(){
        dataBinding.cardPast.setOnClickListener {
            selectCard(dataBinding.cardPast)
            deselectCard(dataBinding.cardUpComing)
            selectText(dataBinding.txtPast)
            deselectText(dataBinding.txtUpComing)
            if (appointmentPastList.isNotEmpty()) {
                dataBinding.recyclerView.visibility = View.VISIBLE
                dataBinding.layoutnoAppointment.visibility = View.GONE
                setupRecyclerView(appointmentPastList)
            } else {
                dataBinding.recyclerView.visibility = View.GONE
                dataBinding.layoutnoAppointment.visibility = View.VISIBLE
            }
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