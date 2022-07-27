package com.canbazdev.hmskitsproject1.presentation.post

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentPostBinding
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import com.canbazdev.hmskitsproject1.util.ActionState
import com.canbazdev.hmskitsproject1.util.PermissionUtils
import com.canbazdev.hmskitsproject1.util.Resource
import com.github.dhaval2404.imagepicker.ImagePicker
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(R.layout.fragment_post),
    EasyPermissions.PermissionCallbacks {

    private val viewModel: PostViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()
        binding.viewmodel = viewModel
        observe()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)


        binding.tvRecognizeLandmark.setOnClickListener {
            viewModel.recognizeLandmark()
        }

        // TODO UI state -1 gelirse dialog göster ve konumu açmasını veya hms core'u kontrol etmesini iste
        //  ui_state değişmezse sıçtın, değişirse dialog kaldır. Tamama basınca viewModel.checkLocationOptions() çalışsın


        val mLocationRequest = LocationRequest()
        mLocationRequest.apply {
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            this.numUpdates = 1
        }
        binding.ivImage.setOnClickListener {
            pickImage()
        }
        binding.btnPost.setOnClickListener {
            //viewModel.sharePost()
            viewModel.uploadPostImageBeforeShare()
        }

        /* val mLocationCallback: LocationCallback
         mLocationCallback = object : LocationCallback() {
             override fun onLocationResult(locationResult: LocationResult) {

                 println("LAT" + locationResult.locations[0].latitude)
                 println("LONG" + locationResult.locations[0].longitude)

                 viewModel.convertLatLangToAddress(
                     locationResult.locations[0].latitude,
                     locationResult.locations[0].longitude
                 )

             }
         }

         fusedLocationProviderClient.requestLocationUpdates(
             mLocationRequest,
             mLocationCallback,
             Looper.getMainLooper()
         )
             .addOnSuccessListener {

             }
             .addOnFailureListener {
                 println("failure")
             }*/


    }


    private fun observe() {

        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    1 -> {
                        binding.progressbar.visibility = View.VISIBLE
                        println("Loading")
                    }
                    1 -> {
                        println("Success")
                        binding.progressbar.visibility = View.GONE
                    }
                    -1 -> println("Fail")
                    else -> {}
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.actionState.collect { state ->
                when (state) {
                    ActionState.NavigateToHome -> navigateToHomeFragment()
                    else -> {}
                }
            }
        }
        // TODO HATA VARSA DIALOG GOSTER VE BELKI ILERIDE HATA TURU (LOCATION HMS) SOYLENEBILIR
        lifecycleScope.launchWhenCreated {
            viewModel.checkLocationOptions.collect { state ->
                when (state) {
                    is Resource.Loading -> println("Locations loading")
                    is Resource.Error -> println("Locations" + state.errorMessage)
                    is Resource.Success -> println("Locations Success")
                }
            }
        }


    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val uri: Uri = data?.data!!
                    //viewModel.imageToText(uri)
                    viewModel.setPostImage(uri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun pickImage() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent {
                startForProfileImageResult.launch(it)
            }
    }


    private fun requestPermissions() {
        if (PermissionUtils.hasLocationPermissions(requireContext())) {
            return
        }
        EasyPermissions.requestPermissions(
            this,
            "You need to accept location permissions to use this app.",
            PermissionUtils.LOCATION_REQUEST_CODE,
            PermissionUtils.ACCESS_COARSE_LOCATION,
            PermissionUtils.ACCESS_FINE_LOCATION
        )


    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun navigateToHomeFragment() {
        if (findNavController().currentDestination?.id == R.id.postFragment) {
            println("navigate to home")

        }
    }
}