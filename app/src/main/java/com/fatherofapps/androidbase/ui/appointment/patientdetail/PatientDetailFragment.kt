package com.fatherofapps.androidbase.ui.appointment.patientdetail

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.request.AppointmentRequest
import com.fatherofapps.androidbase.data.response.PaymentDetailOnlineResponse
import com.fatherofapps.androidbase.databinding.FragmentPatientDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PatientDetailFragment @Inject constructor() : BaseFragment() {
    private lateinit var dataBinding: FragmentPatientDetailBinding
    private val viewModel by viewModels<PatientDetailViewModel>()
    private lateinit var ageRangeAdapter: AgeRangeAdapter
    private var ageRange: String = ""
    private val args by navArgs<PatientDetailFragmentArgs>()
    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String

    companion object{
        const val TAG = "PatientDetailFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentPatientDetailBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        ageRangeAdapter = AgeRangeAdapter()
        ageRangeAdapter.onItemClickListener = { ageRange ->
            this.ageRange = ageRange
        }
        dataBinding.recyclerAge.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        dataBinding.recyclerAge.adapter = ageRangeAdapter

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenNavigation(false)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        validatePatientDetail()
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        dataBinding.btnContinue.setOnClickListener {
            val validationResult = validatePatientDetail()
            if (validationResult.first) {
                viewModel.createAppointment(getAppointmentRequest())
            } else {
                showErrorMessage(validationResult.second)
            }
            fetchPaymentIntent()
        }

    }

    private fun getAppointmentRequest(): AppointmentRequest {
        val doctorId = args.appointmentInfo.doctorId
        val patientName = dataBinding.edtName.text.toString()
        val patientPhone = dataBinding.edtPhone.text.toString()
        val patientGender = dataBinding.radioButtonMale.isChecked
        val patientAge = ageRange
        val reason = dataBinding.edtReason.text.toString()
        val fee = args.appointmentInfo.price
        val scheduledTime = args.appointmentInfo.time
        val scheduledDate = args.appointmentInfo.day
        val service = args.appointmentInfo.service
        return dataBinding.run {
            AppointmentRequest(
                doctorId, scheduledDate, scheduledTime, patientName,
                patientPhone, patientAge, patientGender, reason, fee, service
            )
        }
    }

    private fun validatePatientDetail(): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (ageRange.isEmpty() ||
            dataBinding.edtName.text.toString().isEmpty() ||
            dataBinding.edtPhone.text.toString().isEmpty() ||
            dataBinding.radioGroupGender.checkedRadioButtonId == -1 ||
            dataBinding.edtReason.text.toString().isEmpty()
        ) {
            result = Pair(false, "Vui lòng điền đầy đủ thông tin")
        }
        return result
    }

    private fun fetchPaymentIntent() {
        viewModel.createAppointmentResponse.observe(viewLifecycleOwner) { response ->
            if ( response.data != null && response.isSuccess()) {
                val appointmentPaymentDetail = response.data
                Log.d(TAG, "fetchPaymentIntent: $appointmentPaymentDetail")
                when {
                    appointmentPaymentDetail.paymentDetailOnlineResponse != null -> {
                        val paymentOnline = appointmentPaymentDetail.paymentDetailOnlineResponse
                        val paymentIntent = paymentOnline.paymentIntent
                        val customer = paymentOnline.customer
                        val ephemeralKey = paymentOnline.ephemeralKey
                        val publishableKeyFromServer = paymentOnline.publishableKey
                        paymentIntentClientSecret = paymentIntent
                        customerConfig = PaymentSheet.CustomerConfiguration(customer, ephemeralKey)
                        val publishableKey = publishableKeyFromServer
                        PaymentConfiguration.init(requireContext(), publishableKey)
                        if (paymentIntentClientSecret != null) {
                            presentPaymentSheet()
                        } else {
                            Toast.makeText(
                                context,
                                "Lỗi: Không thể tạo phiếu thanh toán",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    appointmentPaymentDetail.paymentDetailOfflineResponse != null -> {
                        showDialogOffline()
                    }

                    else -> {
                        Toast.makeText(
                            context,
                            "Lỗi: Không thể tạo phiếu thanh toán",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                if (response == null) {
                    showErrorMessage("Lỗi mạng")
                } else {
                    showErrorMessage(response.checkTypeErr())
                }
            }
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
                Toast.makeText(context, "Thanh toán bị hủy", Toast.LENGTH_LONG).show()
            }

            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
                Toast.makeText(context, "Thanh toán thất bại", Toast.LENGTH_LONG).show()

            }

            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
                showDialogOnline()
            }
        }
    }
    private fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret!!,
            PaymentSheet.Configuration(
                merchantDisplayName = "Example",
                customer = customerConfig,
            )
        )
    }
    private fun showDialogOnline(){
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
         val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_book_online_sucess, null)
            dialogBuilder.setView(dialogView)
                .setPositiveButton("Xác Nhận") { dialog, _ ->
                    dialog.dismiss()
                    val action = PatientDetailFragmentDirections.actionPatientDetailFragmentToHomeFragment()
                    navigateToPage(action)
                }
                .show()
    }

    private fun showDialogOffline(){
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_book_offline_sucess, null)
        dialogBuilder.setView(dialogView)
            .setPositiveButton("Xác Nhận") { dialog, _ ->
                dialog.dismiss()
                val action = PatientDetailFragmentDirections.actionPatientDetailFragmentToHomeFragment()
                navigateToPage(action)
            }
            .show()
    }


}


