package com.canbazdev.hmskitsproject1.presentation.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentRegisterBinding
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import com.huawei.agconnect.auth.AGConnectAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.viewModel = viewModel
        observeIsUserSignedUpSuccessfully()

        binding.tvAlreadyHaveAccountButton.setOnClickListener {
            goToLoginFragment()
        }

        binding.btnSignUp.setOnClickListener {
            viewModel.signUpWithEmail()
        }
        binding.tvSendVerificationCode.setOnClickListener {
            viewModel.getVerificationCode()
            showSuccessDialog(
                "Verification Code",
                "Okay",
                "Verification Code has sent to ${viewModel.userEmail.value}"
            )
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeIsUserSignedUpSuccessfully() {
        lifecycleScope.launchWhenCreated {
            viewModel.isUserSignedUp.collect { isSignedUp ->
                println(AGConnectAuth.getInstance().currentUser)
                if (isSignedUp) findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            }
        }
    }

    private fun goToLoginFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private fun showSuccessDialog(
        title: String?,
        buttonText: String?,
        message: String?,
        callback: () -> Unit = {}
    ) {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton(buttonText) { _, _ -> callback.invoke() }
            }.show()
        }
    }
}