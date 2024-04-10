package com.fatherofapps.androidbase.ui.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.SmartCardRepository
import com.fatherofapps.androidbase.data.request.SmartCardRequest
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SmartCardResponse
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatCardViewModel @Inject constructor(
    private val smartCardRepository: SmartCardRepository,
    private val preferenceManager: PreferenceManager
): BaseViewModel() {

    private var _smartCardResponse = MutableLiveData<ConfigResponse<Any>?>()
    val smartCardResponse : MutableLiveData<ConfigResponse<Any>?>
        get() = _smartCardResponse

    fun createSmartCard(data: SmartCardRequest) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = smartCardRepository.createSmartCard(data)
            _smartCardResponse.postValue(data)
        }
        registerJobFinish()
    }
}