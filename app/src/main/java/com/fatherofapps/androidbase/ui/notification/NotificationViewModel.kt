package com.fatherofapps.androidbase.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.AppointmentRepository
import com.fatherofapps.androidbase.data.repositories.PatientRepository
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.DayNotification
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val preferenceManager: PreferenceManager
):BaseViewModel() {

    private var _notificationResponse = MutableLiveData<ConfigResponse<List<DayNotification>>>()
    val notificationResponse: MutableLiveData<ConfigResponse<List<DayNotification>>>
        get() = _notificationResponse

    private var _countNotificationResponse = MutableLiveData<ConfigResponse<Int>?>()
    val countNotificationResponse : MutableLiveData<ConfigResponse<Int>?>
        get() = _countNotificationResponse

    private val _markAllNotificationAsReadResponse = MutableLiveData<ConfigResponse<List<DayNotification>>>()
    val markAllNotificationAsReadResponse: MutableLiveData<ConfigResponse<List<DayNotification>>>
        get() = _markAllNotificationAsReadResponse

    init {
        countNotification()
    }
    fun countNotification() {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = patientRepository.getUnreadNotifications()
            _countNotificationResponse.postValue(data)
        }
    }

    fun getNotifications() {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = patientRepository.getNotifications()
            _notificationResponse.postValue(response!!)
        }
        registerJobFinish()
    }

    fun markAllNotificationAsRead() {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = patientRepository.markAllNotificationAsRead()
            _markAllNotificationAsReadResponse.postValue(data!!)
        }
        registerJobFinish()
    }


}
