package com.fatherofapps.androidbase.ui.smartcard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.SmartCardRepository
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SmartCardResponse
import com.fatherofapps.androidbase.data.services.SmartCardRemoteService
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmartCardViewModel @Inject constructor(
    private val smartCardRepository: SmartCardRepository,
    private val preferenceManager: PreferenceManager
): BaseViewModel() {

    private var _smartCardResponse = MutableLiveData<ConfigResponse<SmartCardResponse>?>()
    val smartCardResponse: MutableLiveData<ConfigResponse<SmartCardResponse>?>
        get() = _smartCardResponse

    private var _smartCardBlock = MutableLiveData<ConfigResponse<Any>?>()
    val smartCardBlock: MutableLiveData<ConfigResponse<Any>?>
        get() = _smartCardBlock

    private var _smartCardUnBlock = MutableLiveData<ConfigResponse<Any>?>()
    val smartCardUnBlock: MutableLiveData<ConfigResponse<Any>?>
        get() = _smartCardUnBlock

    fun getSmartCardInfo() {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = smartCardRepository.getSmartCardInfo()
            _smartCardResponse.postValue(data)
        }
        registerJobFinish()
    }

    fun blockSmartCard() {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = smartCardRepository.blockSmartCard()
            _smartCardBlock.postValue(data)
        }
        registerJobFinish()
    }

    fun unBlockSmartCard() {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = smartCardRepository.unBlockSmartCard()
            _smartCardUnBlock.postValue(data)
        }
        registerJobFinish()
    }
}