package com.canbazdev.hmskitsproject1.presentation.detail_post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentLandmarkDetailBinding
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LandmarkDetailFragment :
    BaseFragment<FragmentLandmarkDetailBinding>(R.layout.fragment_landmark_detail) {

    private val viewModel: LandmarkDetailViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewmodel = viewModel
        binding.btnAddWishList.setOnClickListener {
            goToHome()
        }


        super.onViewCreated(view, savedInstanceState)
    }


    private fun goToHome() {
        findNavController().navigate(R.id.action_landmarkDetailFragment_to_homeFragment)
    }

}