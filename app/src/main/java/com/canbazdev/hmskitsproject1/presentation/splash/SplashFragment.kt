package com.canbazdev.hmskitsproject1.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
        viewModel.checkEnable()
        lifecycleScope.launch {
            viewModel.signedEnable.collect {
                println("signed enable: $it")
                if (it == true) {
                    viewModel.signInWithHuawei()
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.isUserSignedIn.collect {
                println("sgined in $it")
                if (it)
                    navigateToHomeFragment()

            }
        }

        // giriş yapmış kullanıcı var mı varsa home fragmenta gönder yoksa register'a gönder
        lifecycleScope.launchWhenCreated {
            viewModel.signedEnable.collect {
                if (it == false) {
                    if (viewModel.currentUser != null) {
                        navigateToHomeFragment()
                    } else {
                        navigateToRegisterFragment()
                    }
                }
            }
        }


        // TODO IMAGE RECOGNİTİON ACCESS TOKEN / API KEY

        super.onViewCreated(view, savedInstanceState)
    }

    private fun navigateToHomeFragment() {
        AGConnectAuth.getInstance()
            .signIn(activity, AGConnectAuthCredential.HMS_Provider)
            .addOnSuccessListener {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                println("email" + it.user.displayName)
            }.addOnFailureListener {
                navigateToRegisterFragment()
            }
    }

    private fun navigateToRegisterFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_registerFragment)
    }

    private fun navigateToLoginFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }

    override fun onDestroyView() {
        println("on destroy")
        super.onDestroyView()
    }


}