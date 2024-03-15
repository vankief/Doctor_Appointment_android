package com.fatherofapps.androidbase.ui.authencation.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.request.SignUpRequest
import com.fatherofapps.androidbase.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {
    private lateinit var dataBinding: FragmentSignupBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentSignupBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }
    companion object {
        private const val TAG = "RegisterFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        hideAll()
        dataBinding.btnSignup.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val userRequest = getUserRequest()
                viewModel.doSignUp(userRequest)
            } else {
                showValidationErrors(validationResult.second)
            }
            bindObservers()
        }

        dataBinding.txtLogin.setOnClickListener {
            navigationToLoginFragment()
        }
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val emailAddress = dataBinding.edtEmail.text.toString()
        val password = dataBinding.edtPassword.text.toString()
        val confirmPassword = dataBinding.edtConfirmPassword.text.toString()
        return viewModel.validateCredentials(emailAddress, password, confirmPassword)
    }

    private fun getUserRequest(): SignUpRequest {
        val emailAddress = dataBinding.edtEmail.text.toString()
        val password = dataBinding.edtPassword.text.toString()
        return SignUpRequest( emailAddress, password)
    }

    private fun showValidationErrors(message: String) {
        dataBinding.edtConfirmPassword.text?.clear();
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        //showErrorDialog(it.message)
    }
    private fun bindObservers() {
        viewModel.registerResponse.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isSuccess()) {
                it.data?.accessToken?.let { it1 -> viewModel.saveAccessAndRefreshToken(it1,
                    it.data.refreshToken
                ) }

                navigationToProfileFragment()
            } else {
                if(it?.message != null) {
                    showErrorMessage(it.message)
                } else {
                    showErrorMessage("Network error")
                }
            }
        })
    }

    private fun navigationToLoginFragment() {
        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
    }

    private fun navigationToProfileFragment(){
        val action = RegisterFragmentDirections.actionRegisterFragmentToFragementCreateProfile()
        findNavController().navigate(action)
    }

}