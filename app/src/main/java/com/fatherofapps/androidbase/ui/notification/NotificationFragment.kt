package com.fatherofapps.androidbase.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.DayNotification
import com.fatherofapps.androidbase.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class NotificationFragment @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentNotificationBinding
    private val viewModel by viewModels<NotificationViewModel>()
    private var dayList: List<DayNotification> = emptyList()
    private var dayAdapter: DayAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.markAllNotificationAsRead()
        viewModel.getNotifications()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentNotificationBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenTopAppBar(true)
        hideOpenNavigation(true)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.markAllNotificationAsReadResponse.observe(viewLifecycleOwner) { response ->
            response?.let { notificationResponse ->
                if (notificationResponse != null && notificationResponse.isSuccess()) {
                    dayList = notificationResponse.data!!
                    setupRecyclerView(dayList)
                    (requireActivity() as MainActivity).countNotification = 0 // Đặt lại số thông báo
                    (requireActivity() as MainActivity).updateBadge()
                } else {
                    showErrorMessage(notificationResponse.message ?: "Lỗi không xác định")
                }
            }
        }

        viewModel.notificationResponse.observe(viewLifecycleOwner) { response ->
            response?.let { notificationResponse ->
                if (notificationResponse != null && notificationResponse.isSuccess()) {
                    dayList = notificationResponse.data!!
                    setupRecyclerView(dayList)
                } else {
                    showErrorMessage(notificationResponse.message ?: "Lỗi không xác định")
                }
            }
        }
    }
    private fun setupRecyclerView(notification: List<DayNotification>) {
        dayAdapter = DayAdapter(notification)
        dataBinding.recyclerDay.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = dayAdapter
        }

    }

}