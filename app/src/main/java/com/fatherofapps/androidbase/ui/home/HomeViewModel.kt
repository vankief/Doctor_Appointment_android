package com.fatherofapps.androidbase.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.SpecialistRepository
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SpecialistDoctor
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val specialistRepository: SpecialistRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    private var _specialistResponse = MutableLiveData<ConfigResponse<List<SpecialistDoctor>>?>()
    val specialistResponse: MutableLiveData<ConfigResponse<List<SpecialistDoctor>>?>
        get() = _specialistResponse


    fun getDoctorsBySpecialist() {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = specialistRepository.getDoctorsBySpecialist()
            _specialistResponse.postValue(data)
        }
        registerJobFinish()
    }
}