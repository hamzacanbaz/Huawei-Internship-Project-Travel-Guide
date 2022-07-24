package com.canbazdev.hmskitsproject1.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.huawei.agconnect.auth.AGConnectAuth

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // giriş yapmış kullanıcı var mı varsa home fragmenta gönder yoksa register'a gönder
        if (AGConnectAuth.getInstance().currentUser != null) {
            navigateToHomeFragment()
        } else {
            navigateToRegisterFragment()

        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
    }

    private fun navigateToRegisterFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_registerFragment)
    }

    private fun navigateToLoginFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }

}