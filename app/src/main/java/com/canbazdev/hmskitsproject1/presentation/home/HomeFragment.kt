package com.canbazdev.hmskitsproject1.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentHomeBinding
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.service.AccountAuthService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mAuthParam: AccountAuthParams
    private lateinit var mAuthService: AccountAuthService


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.fabOpenPostScreen.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_postFragment)
        }
        binding.btnSignOut.setOnClickListener {
            AGConnectAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
        }

        /*mAuthParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setIdToken()
            .createParams()

        mAuthService = AccountAuthManager.getService(activity, mAuthParam)

        binding.btnSignOut.setOnClickListener {
            signOut()
        }*/
        super.onViewCreated(view, savedInstanceState)
    }

    /* private fun signOut() {
         val signOutTask = mAuthService.signOut()
         signOutTask.addOnSuccessListener {
             println("success")
             binding.status.text = "Logged Out"
         }.addOnFailureListener {
             println("error")
         }
     }*/
}