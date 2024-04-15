package com.fatherofapps.androidbase.base.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.activities.BaseActivity
import com.fatherofapps.androidbase.base.network.BaseNetworkException
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.common.EventObserver
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class BaseFragment : Fragment() {
    protected fun hideOpenNavigation(isShow: Boolean = false) {
        if (isShow) {
            val bottomNavigationView =
                requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            val bottomNavigationView =
                requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.visibility = View.GONE
        }
    }

    protected fun convertDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date = inputFormat.parse(inputDate) ?: Date()
        return outputFormat.format(date)
    }

    protected fun hideOpenTopAppBar(isShow: Boolean) {
        if (isShow) {
            val toolbar =
                requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
            toolbar.visibility = View.VISIBLE
        } else {
            val toolbar =
                requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
            toolbar.visibility = View.GONE
        }
    }

    protected fun hideAll() {
        val toolbar =
            requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.visibility = View.GONE
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
    }

    protected fun openAll() {
        val toolbar =
            requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.visibility = View.GONE
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
    }

    protected fun navigateToPage(action: NavDirections) {
        findNavController().navigate(action)
    }


    protected fun showLoading(isShow: Boolean) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showLoading(isShow)
        }
    }

    protected fun showErrorMessage(e: BaseNetworkException) {
        showDialogMaterial("Lỗi từ mạng", e.mainMessage)
    }

    protected fun showErrorMessage(messageId: Int) {
        val message = requireContext().getString(messageId)
//        showErrorMessage(message)
        showDialogMaterial("Thông báo lỗi", message)
    }

    protected fun showErrorMessage(message: String) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
//            activity.showErrorDialog(message)
            showDialogMaterial("Thông báo lỗi", message)
        }
    }


    protected fun showNotify(title: String?, message: String) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
//            activity.showNotifyDialog(title ?: getDefaultNotifyTitle(), message)
            showDialogMaterial(title ?: getDefaultNotifyTitle(), message)
        }
    }

    protected fun showNotify(titleId: Int = R.string.default_notify_title, messageId: Int) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
//            activity.showNotifyDialog(titleId, messageId)
            showDialogMaterial(getString(titleId), getString(messageId))
        }
    }

    protected fun registerObserverExceptionEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.baseNetworkException.observe(viewLifecycleOwner, EventObserver {
            showErrorMessage(it)
        })
    }

    protected fun registerObserverNetworkExceptionEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.networkException.observe(viewLifecycleOwner, EventObserver {
            showNotify(getDefaultNotifyTitle(), it.message ?: "Lỗi mạng")
        })
    }

    protected fun registerObserverMessageEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.errorMessageResourceId.observe(viewLifecycleOwner, EventObserver { message ->
            showErrorMessage(message)
        })
    }

    protected fun registerObserverLoadingMoreEvent(viewModel: BaseViewModel,
    viewLifecycleOwner: LifecycleOwner){
        viewModel.isLoadingMore.observe(viewLifecycleOwner,EventObserver{
            isShow->
            showLoadingMore(isShow)
        })
    }

    protected fun showLoadingMore(isShow: Boolean){

    }


    private fun getDefaultNotifyTitle(): String {
        return getString(R.string.default_notify_title)
    }

    protected fun registerAllExceptionEvent( viewModel: BaseViewModel,
                                             viewLifecycleOwner: LifecycleOwner){
        registerObserverExceptionEvent(viewModel,viewLifecycleOwner)
        registerObserverNetworkExceptionEvent(viewModel,viewLifecycleOwner)
        registerObserverMessageEvent(viewModel,viewLifecycleOwner)
    }

    protected fun registerObserverLoadingEvent(viewModel: BaseViewModel,viewLifecycleOwner: LifecycleOwner){
        viewModel.isLoading.observe(viewLifecycleOwner,EventObserver{
                isShow ->
            showLoading(isShow)
        })
    }

    protected fun showDialogMaterial(title: String, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Chấp nhận") { dialog, which ->
                // hide dialog
                dialog.cancel()
            }
            .show()
    }

    fun convertToCurrencyFormat(number: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        return format.format(number.toLong())
    }
    fun convertCurrencyStringToInteger(currencyString: String): Int {
        val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        try {
            val number = format.parse(currencyString)
            return number.toInt()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

}