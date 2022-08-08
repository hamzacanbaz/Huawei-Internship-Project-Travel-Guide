package com.canbazdev.hmskitsproject1.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentLoginBinding
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        viewModel.checkUserLogin()
        viewModel.signOutHuawei()

        binding.huaweiIdAuthButton.setOnClickListener {
            viewModel.signInWithHuawei()
        }

        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {
                println("ui state $it")
            }


        }

        binding.tvNoAccountButton.setOnClickListener {
            goToRegisterFragment()
        }
        binding.btnSignIn.setOnClickListener {
            viewModel.signInWithEmail()
        }
        lifecycleScope.launchWhenCreated {
            viewModel.isUserSignedIn.collect {
                if (it) {
                    val bundle = Bundle()
                    bundle.putSerializable(
                        "userInfo",
                        UserFirebase(viewModel.userId.value, viewModel.userEmail.value)
                    )
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment, bundle)
                }
            }

        }
    }

    private fun goToRegisterFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }


}