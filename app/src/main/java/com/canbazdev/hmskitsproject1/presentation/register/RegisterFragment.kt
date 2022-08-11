package com.canbazdev.hmskitsproject1.presentation.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentRegisterBinding
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import com.huawei.hms.analytics.HiAnalytics
import dagger.hilt.android.AndroidEntryPoint


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
            val bundle = Bundle()
            bundle.putString("email", viewModel.userEmail.value)
            HiAnalytics.getInstance(context).onEvent("SendActivationCode", bundle)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeIsUserSignedUpSuccessfully() {
        lifecycleScope.launchWhenCreated {
            viewModel.isUserSignedUp.collect { isSignedUp ->
                if (isSignedUp) {
                    val bundle = Bundle()
                    bundle.putSerializable(
                        "userInfo",
                        UserFirebase(id = viewModel.userId.value, email = viewModel.userEmail.value)
                    )
                    val eventBundle = Bundle()
                    eventBundle.putString("\$AcountType", "email")
                    eventBundle.putString("\$RegistMethod", "email")
                    HiAnalytics.getInstance(context).onEvent("\$RegisterAccount", eventBundle)
                    findNavController().navigate(
                        R.id.action_registerFragment_to_homeFragment,
                        bundle
                    )
                }
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