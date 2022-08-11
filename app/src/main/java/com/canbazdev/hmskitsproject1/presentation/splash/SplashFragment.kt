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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.checkEnable()
        lifecycleScope.launch {
            viewModel.signedEnable.collect {
                if (it == true) {
                    viewModel.signInWithHuawei()
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.isUserSignedIn.collect {
                if (it)
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)

            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.signedEnable.collect {
                if (it == false) {
                    if (viewModel.currentUser != null) {
                        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    } else {
                        navigateToRegisterFragment()
                    }
                }
            }
        }



        super.onViewCreated(view, savedInstanceState)
    }

    private fun navigateToRegisterFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_registerFragment)
    }


}