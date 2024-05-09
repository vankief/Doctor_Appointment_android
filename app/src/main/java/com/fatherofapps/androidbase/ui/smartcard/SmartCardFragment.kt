package com.fatherofapps.androidbase.ui.smartcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.SmartCardResponse
import com.fatherofapps.androidbase.databinding.FragmentSmartcardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmartCardFragment @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentSmartcardBinding
    private val viewModel by viewModels<SmartCardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSmartCardInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentSmartcardBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        (activity as MainActivity).setTitle("Thẻ Khám Bệnh")
        (activity as MainActivity).setNavigationBackIcon()
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenTopAppBar(true)
        hideOpenNavigation(true)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        setupObserver()
        handleBlockSmartCard()
        handleUnBlockSmartCard()
    }

    private fun setupObserver() {
        viewModel.smartCardResponse.observe(viewLifecycleOwner) { smartcardResponse ->
            smartcardResponse?.let {
                if (it.isSuccess() && it.data != null) {
                    // Hiển thị thông tin thẻ nếu nhận được dữ liệu thành công từ server
                    setupSmartCardInfo(it.data)
                    setupButton()
                } else {
                    // Xử lý các trường hợp lỗi hoặc dữ liệu rỗng từ server
                    when (it.statusCode) {
                        404 -> {
                            // Người dùng không tồn tại
                            showErrorMessage("Không tìm thấy người dùng")
                        }
                        400 -> {
                            // Người dùng chưa có thẻ
                            dataBinding.layoutCard.visibility = View.GONE
                            dataBinding.layoutNoCard.visibility = View.VISIBLE
                            setupButtonCreateCard()
                        }
                        405 -> {
                            dataBinding.layoutCard.visibility = View.GONE
                            dataBinding.layoutNoCard.visibility = View.VISIBLE
                            dataBinding.txtNoCard.text = "Thẻ của bạn chưa được kích hoạt \n vui lòng chờ quản trị viên kích hoạt thẻ"
                            dataBinding.btnCreateSmartCard.visibility = View.GONE
                        }
                        else -> {
                            // Xử lý các trường hợp khác
                            showErrorMessage("Đã xảy ra lỗi")
                        }
                    }
                }
            }
        }
    }

    private fun setupSmartCardInfo(smartCardResponse: SmartCardResponse) {
        val cardNumber = "BN" + String.format("%03d", smartCardResponse.id)
        dataBinding.txtCardNumber.text = cardNumber
        dataBinding.txtName.text = smartCardResponse.name
        dataBinding.txtDob.text = smartCardResponse.dob
        dataBinding.txtGender.text = if (smartCardResponse.gender) "Nam" else "Nữ"
        dataBinding.txtAddress.text = smartCardResponse.address
        dataBinding.txtPhoneNumber.text = smartCardResponse.phone
        dataBinding.txtBalance.text = convertToCurrencyFormat(smartCardResponse.balance)
        if (smartCardResponse.isBlocked) {
            unBlockSmartCard()
        } else {
            blockSmartCard()
        }
    }

    private fun setupButtonCreateCard() {
        dataBinding.btnCreateSmartCard.setOnClickListener {
            val action = SmartCardFragmentDirections.actionSmartCardFragmentToCreatCardFragment()
            navigateToPage(action)
        }
    }

    private fun setupButton() {
        dataBinding.btnLockCard.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận khóa thẻ")
                .setMessage("Bạn có chắc chắn muốn khóa thẻ không?")
                .setNegativeButton("Hủy bỏ") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Đồng ý") { dialog, _ ->
                    viewModel.blockSmartCard()
                    dialog.dismiss()

                }
                .show()
        }

        dataBinding.btnOpenCard.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận mở khóa thẻ")
                .setMessage("Bạn có chắc chắn muốn mở khóa thẻ không?")
                .setNegativeButton("Hủy bỏ") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Đồng ý") { dialog, which ->
                    viewModel.unBlockSmartCard()
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun handleBlockSmartCard() {
        viewModel.smartCardBlock.observe(viewLifecycleOwner) { smartCardBlock ->
            smartCardBlock?.let {
                if (it.isSuccess()&& it.data != null) {
                    showNotify("Thông báo", "Khóa thẻ thành công")
                    unBlockSmartCard()
                } else {
                    showErrorMessage(it.message ?: "Lỗi không xác định")
                }
            }
        }
    }

    private fun handleUnBlockSmartCard() {
        viewModel.smartCardUnBlock.observe(viewLifecycleOwner) { smartCardUnBlock ->
            smartCardUnBlock?.let {
                if (it.isSuccess() && it.data != null) {
                    showNotify("Thông báo", "Mở khóa thẻ thành công")
                    blockSmartCard()
                } else {
                    showErrorMessage(it.message ?: "Lỗi không xác định")
                }
            }
        }
    }

    private fun blockSmartCard() {
        dataBinding.btnLockCard.visibility = View.VISIBLE
        dataBinding.btnOpenCard.visibility = View.GONE
    }

    private fun unBlockSmartCard() {
        dataBinding.btnLockCard.visibility = View.GONE
        dataBinding.btnOpenCard.visibility = View.VISIBLE
    }

}