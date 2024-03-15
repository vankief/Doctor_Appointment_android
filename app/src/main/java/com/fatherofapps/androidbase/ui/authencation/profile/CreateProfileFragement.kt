package com.fatherofapps.androidbase.ui.authencation.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.request.updatePatient
import com.fatherofapps.androidbase.databinding.FragmentProfileBinding
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileFragement @Inject constructor(): BaseFragment() {

    private lateinit var dataBinding: FragmentProfileBinding
    private val viewModel by viewModels<CreateProfileViewModel>()

    @Inject
    lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        private const val TAG = "FragementCreateProfile"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentProfileBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenTopAppBar(false)
        hideOpenNavigation(false)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        dataBinding.inputDoB.setStartIconOnClickListener(View.OnClickListener {
            showDatePickerDialog()
        })
        dataBinding.btnComfirm.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first) {
                viewModel.updateProfile(getUserRequest())
            } else {
                showErrorMessage(validationResult.second)
            }
        }
        bindObservers()

    }
    fun validateUserInput(): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (dataBinding.edtName.text.toString().isEmpty()
            || dataBinding.edtDoB.text.toString().isEmpty()
            || dataBinding.edtAdress.text.toString().isEmpty()
            || dataBinding.edtPhone.text.toString().isEmpty()
            || dataBinding.edtName.text.toString().isEmpty()
            || dataBinding.radioGroupGender.checkedRadioButtonId == -1) {
            result = Pair(false, "Vui lòng điền đầy đủ thông tin và chọn giới tính")
        }
        return result
    }

    private fun getUserRequest(): updatePatient {
        val name = dataBinding.edtName.text.toString()
        val dob = convertDateFormat(dataBinding.edtDoB.text.toString())
        val img = ""
        val gender = dataBinding.radioButtonMale.isChecked
        val phone = dataBinding.edtPhone.text.toString()
        val address = dataBinding.edtAdress.text.toString()
        return dataBinding.run {
            updatePatient(img, name, phone, gender, dob, address)
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun showDatePickerDialog() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày sinh của bạn")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.setTimeInMillis(selection)
            val format = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate: String = format.format(calendar.getTime())

            Log.d("TAG", "showDatePickerDialog: $formattedDate")

            dataBinding.edtDoB.setText(formattedDate)
            
        }
        datePicker.show(childFragmentManager, "date_picker")

    }

    private fun navigateToHome() {
        findNavController().navigate(CreateProfileFragementDirections.actionFragementCreateProfileToHomeFragment())
    }

    private fun bindObservers() {
        viewModel.createProfileResponse.observe(viewLifecycleOwner, {
            if (it != null && it.isSuccess()) {
                viewModel.clearIsFirstTime()
                navigateToHome()
            } else {
                if (it == null) showErrorMessage("Error network")
                else showErrorMessage(it.checkTypeErr())
            }
        })
    }

}