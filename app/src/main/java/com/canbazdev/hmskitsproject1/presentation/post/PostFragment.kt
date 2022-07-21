package com.canbazdev.hmskitsproject1.presentation.post

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentPostBinding
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import com.canbazdev.hmskitsproject1.util.Constants.TAG
import com.canbazdev.hmskitsproject1.util.PermissionUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.huawei.agconnect.AGConnectOptions
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.hms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException
import java.util.*

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

        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    0 -> println("Loading")
                    1 -> println("Success")
                    -1 -> println("Fail")
                }
            }
        }

        // TODO UI state -1 gelirse dialog göster ve konumu açmasını veya hms core'u kontrol etmesini iste
        //  ui_state değişmezse sıçtın, değişirse dialog kaldır. Tamama basınca viewModel.checkLocationOptions() çalışsın


        val mLocationRequest = LocationRequest()
        mLocationRequest.apply {
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            this.numUpdates = 1
        }
        binding.btnPickImage.setOnClickListener {
            pickImage()
        }

        val mLocationCallback: LocationCallback
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {

                println("LAT" + locationResult.locations[0].latitude)
                println("LONG" + locationResult.locations[0].longitude)
                val geocode = Geocoder(context, Locale.SIMPLIFIED_CHINESE)
                // Enable subthread to call reverse geocoding to obtain the location information.

                try {
                    val addresses = geocode.getFromLocation(
                        locationResult.locations[0].latitude,
                        locationResult.locations[0].longitude,
                        1
                    )
                    if (addresses != null && addresses.size > 0) {
                        // Use handler to update UI after the address is updated successfully.
                        Log.i(TAG, "addresses=" + addresses.size)
                        for (address in addresses) {
                            println(address)
                        }
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "reverseGeocode wrong ")
                }


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
            }


    }


    private fun observe() {


    }



    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val uri: Uri = data?.data!!
                    viewModel.imageToText(uri)
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
}