package com.fatherofapps.androidbase.ui.authencation.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.common.Utils
import com.fatherofapps.androidbase.data.request.AuthRequest
import com.fatherofapps.androidbase.databinding.FragmentLoginBinding
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment @Inject constructor()  : BaseFragment() {
    private lateinit var dataBinding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    private var isLogin: Boolean = false

    @Inject
    lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLogin = viewModel.checkLogin()
        Log.d("LoginFragment", "onCreate: $isLogin")
        if (isLogin) {
            navigateToHome()
        }

    }

    private fun navigateToHome() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isLogin = viewModel.checkLogin()
        dataBinding = FragmentLoginBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenTopAppBar(false)
        hideOpenNavigation(false)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        dataBinding.btnLogin.setOnClickListener {
            Utils.hideSoftKeyboard(requireView(), requireContext())
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val userRequest = getUserRequest()
                viewModel.doLogin(userRequest)
            } else {
                Log.d("LoginFragment", "onViewCreated: ${validationResult.second}")
                showValidationErrors(validationResult.second)
            }
            bindObservers()
        }

        dataBinding.textSignUp.setOnClickListener {
            navigationToSignUpForm()
        }

    }

    private fun navigationToSignUpForm() {
        navigateToPage(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val emailAddress = dataBinding.edtEmail.text.toString()
        val password = dataBinding.edtPassword.text.toString()
        return viewModel.validateCredentials(emailAddress, password)
    }

    private fun getUserRequest(): AuthRequest {
        return dataBinding.run {
            AuthRequest(
                edtEmail.text.toString(),
                edtPassword.text.toString()
            )
        }
    }

    private fun showValidationErrors(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun bindObservers() {
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isSuccess()) {
                it.data?.accessToken?.let { it1 ->
                    viewModel.saveAccessTokenAndRefreshToken(
                        it1,
                        it.data.refreshToken
                    )
                }
            } else {
                if(it == null) showErrorMessage("Error network")
                else showErrorMessage(it.checkTypeErr())

            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        dataBinding.unbind()
    }


}