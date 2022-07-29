package com.canbazdev.hmskitsproject1.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentProfileBinding
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewmodel = viewModel
        super.onViewCreated(view, savedInstanceState)
    }

}