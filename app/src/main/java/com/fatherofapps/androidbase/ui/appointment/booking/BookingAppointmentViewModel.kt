package com.fatherofapps.androidbase.ui.appointment.booking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.DoctorRepository
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.ListTime
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingAppointmentViewModel @Inject constructor(
    private val repository: DoctorRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel(){
    private var _doctorTimeSlottResponse = MutableLiveData<ConfigResponse<List<ListTime>>?>()
    val doctorTimeSlottResponse: MutableLiveData<ConfigResponse<List<ListTime>>?>
        get() = _doctorTimeSlottResponse

    fun getDoctorTimeSlot(doctorId: String?,day: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = repository.getDoctorTimeSlot(doctorId ?: "",day)
            _doctorTimeSlottResponse.postValue(data)
        }
        registerJobFinish()
    }

}