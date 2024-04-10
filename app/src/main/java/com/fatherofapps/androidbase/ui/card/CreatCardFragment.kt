package com.fatherofapps.androidbase.ui.card

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.request.SmartCardRequest
import com.fatherofapps.androidbase.databinding.FragmentCreateSmartCardBinding
import com.fatherofapps.androidbase.utils.convertDate
import com.fatherofapps.androidbase.utils.splitData
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreatCardFragment @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentCreateSmartCardBinding
    private val viewModel by viewModels<CreatCardViewModel>()

    private val TAG = "CreatCardFragment"

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted: Boolean ->
            if (isGranted) {
                showCamera()
            } else {
                Toast.makeText(requireContext(), "Permission needed", Toast.LENGTH_SHORT).show()
            }
        }

    private val scanLauncher =
        registerForActivityResult(ScanContract()){ result: ScanIntentResult ->
            run {
                if (result.contents != null) {
                    Log.d(TAG, "onActivityResult: ${result.contents}")
                    setResult(result.contents)
                } else {
                    Toast.makeText(requireContext(), "Scan failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun setResult(contents: String) {
        val data = splitData(contents)
        Log.d(TAG, "setResult: ${data?.joinToString()}") // Sử dụng joinToString() để hiển thị dữ liệu trong mảng

        if (data != null && data.size >= 6) { // Đảm bảo rằng có đủ phần tử trong mảng
            dataBinding.txtCccd.text = data[0]
            dataBinding.txtName.text = data[1]
            dataBinding.txtDob.text = convertDate(data[2])
            dataBinding.txtGender.text = data[3]
            dataBinding.txtAddress.text = data[4]
            dataBinding.txtDayCCCD.text = convertDate(data[5])
        } else {
            Toast.makeText(requireContext(), "QR Code invalid", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showCamera() {
        val option = ScanOptions()
        option.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        option.setPrompt("Scan QR Code")
        option.setCameraId(0)
        option.setBeepEnabled(false)
        option.setBarcodeImageEnabled(true)
        option.setOrientationLocked(false)
        scanLauncher.launch(option)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentCreateSmartCardBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        hideOpenTopAppBar(false)
        hideOpenNavigation(false)
        validatePatientDetail()
        dataBinding.fabQrCode.setOnClickListener {
            checkPermissionCamera(requireContext())
        }

        dataBinding.btnCreateSmartCard.setOnClickListener() {
            val validationResult = validatePatientDetail()
            if (validationResult.first) {
                viewModel.createSmartCard(getSmartCardRequest())
            } else {
                showErrorMessage(validationResult.second)
            }
            bindObservers()
        }

    }

    private fun bindObservers() {
        viewModel.smartCardResponse.observe(viewLifecycleOwner) {
            if (it != null && it.isSuccess()) {
                Toast.makeText(
                    requireContext(),
                    "Thẻ đã được đăng kí, vui lòng chờ xác nhận từ quản trị viên",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Đã có lỗi xảy ra, vui lòng thử lại",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validatePatientDetail(): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (dataBinding.txtCccd.text.toString().isEmpty() ||
            dataBinding.txtName.text.toString().isEmpty() ||
            dataBinding.txtDob.text.toString().isEmpty() ||
            dataBinding.txtGender.text.toString().isEmpty() ||
            dataBinding.txtAddress.text.toString().isEmpty() ||
            dataBinding.txtDayCCCD.text.toString().isEmpty()||
            dataBinding.edtPhone.text.toString().isEmpty()
        ) {
            result = Pair(false, "Vui lòng điền đầy đủ thông tin")
        }
        return result
    }
    private fun resetData() {
        dataBinding.txtCccd.text = ""
        dataBinding.txtName.text = ""
        dataBinding.txtDob.text = ""
        dataBinding.txtGender.text = ""
        dataBinding.txtAddress.text = ""
        dataBinding.txtDayCCCD.text = ""
    }
    
    private fun checkPermissionCamera(context: Context) {
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            showCamera()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(context, "Permission needed", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }

    }

    private fun getSmartCardRequest(): SmartCardRequest {
        val cicNumber= dataBinding.txtCccd.text.toString()
        val name = dataBinding.txtName.text.toString()
        val gender = dataBinding.txtGender.text.toString() == "Nam"
        val dob = dataBinding.txtDob.text.toString()
        val address = dataBinding.txtAddress.text.toString()
        val phone = dataBinding.edtPhone.text.toString()
        return dataBinding.run {
            SmartCardRequest(cicNumber,name, gender, dob, address, phone)
        }
    }
}