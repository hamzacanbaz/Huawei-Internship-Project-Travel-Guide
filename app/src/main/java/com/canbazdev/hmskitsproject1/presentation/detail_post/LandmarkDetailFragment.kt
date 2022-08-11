package com.canbazdev.hmskitsproject1.presentation.detail_post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.DialogQrBinding
import com.canbazdev.hmskitsproject1.databinding.FragmentLandmarkDetailBinding
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import com.canbazdev.hmskitsproject1.util.ActionState
import com.canbazdev.hmskitsproject1.util.Constants.LANDMARK_NO_FOUND
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LandmarkDetailFragment :
    BaseFragment<FragmentLandmarkDetailBinding>(R.layout.fragment_landmark_detail) {

    private val viewModel: LandmarkDetailViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewmodel = viewModel

        binding.btnAddWishList.setOnClickListener {
            goToHome()
        }

        binding.btnOpenMap.setOnClickListener {
            goToPetalMaps()
        }

        binding.tvShowQrCode.setOnClickListener {
            println("show qr code")
            setupDialog(viewModel.landmark.value.qrUrl.toString())
        }

        lifecycleScope.launchWhenCreated {
            viewModel.actionState.collect {
                when (it) {
                    ActionState.NavigateToHome -> goToHome("Landmark No Found")
                    else -> {}
                }
            }

        }
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setupDialog(qrUrl: String?) {
        val binding =
            DataBindingUtil.inflate<DialogQrBinding>(
                LayoutInflater.from(context),
                R.layout.dialog_qr,
                null,
                false
            )
        println(qrUrl)
        binding.qrCode = qrUrl
        val builder = android.app.AlertDialog.Builder(context)
            .setView(binding.root)

        val dialog = builder.create()
        dialog.show()

    }


    private fun goToHome(errorMessage: String? = null) {
        val bundle = Bundle()
        bundle.putString(LANDMARK_NO_FOUND, errorMessage)
        findNavController().navigate(R.id.action_landmarkDetailFragment_to_homeFragment, bundle)
    }

    private fun goToPetalMaps() {
        val uriString =
            "petalmaps://textSearch?text=" + "${viewModel.landmark.value.landmarkName}"
        val contentUrl: Uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_VIEW, contentUrl)
        if (activity?.let { it1 -> intent.resolveActivity(it1.packageManager) } != null) {
            startActivity(intent)
        }
    }


}