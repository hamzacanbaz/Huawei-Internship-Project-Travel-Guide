package com.canbazdev.hmskitsproject1.presentation.map

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentMapBinding
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import com.huawei.hms.location.*
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {

    private var hMap: HuaweiMap? = null
    private var mMapView: MapView? = null
    private lateinit var cameraPosition: CameraPosition
    private lateinit var cameraUpdate: CameraUpdate

    private val viewmodel: MapViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val mLocationRequest = LocationRequest()
        mLocationRequest.apply {
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            this.numUpdates = 1
        }
        initializeLocationCallback()
        getLocation(mLocationRequest, mLocationCallback)

        initializeMap(savedInstanceState)


    }

    private fun initializeMap(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey")
        }
        mMapView = binding.mapView
        mMapView?.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@MapFragment)
        }
    }

    override fun onMapReady(map: HuaweiMap?) {
        Log.d("TAG", "onMapReady: ")
        hMap = map

        hMap?.isMyLocationEnabled = true
        hMap?.uiSettings?.isMyLocationButtonEnabled = true

        addMarkersToLandmarks()
        hMap?.uiSettings?.isMyLocationButtonEnabled
        updateCamera()

    }

    private fun addMarkersToLandmarks() {
        lifecycleScope.launchWhenCreated {
            viewmodel.postsList.collect {
                it.forEach { landmark ->
                    hMap?.addMarker(
                        MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker())
                            .title(landmark.landmarkName)
                            .position(
                                landmark.landmarkLongitude?.let { it1 ->
                                    landmark.landmarkLatitude?.let { it2 ->
                                        LatLng(
                                            it2,
                                            it1
                                        )
                                    }
                                }
                            )

                    )
                }
            }
        }
    }

    private fun updateCamera() {
        lifecycleScope.launchWhenCreated {
            viewmodel.latLng.collect {
                cameraPosition = CameraPosition.builder()
                    .target(it)
                    .zoom(10f)
                    .bearing(2.0f)
                    .tilt(2.5f).build()
                cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
                hMap?.moveCamera(cameraUpdate)

            }
        }
    }

    private fun getLocation(
        mLocationRequest: LocationRequest,
        mLocationCallback: LocationCallback
    ) {
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.getMainLooper()
        )
            .addOnSuccessListener {
                println("ah")
            }
            .addOnFailureListener {
                println("failure")
            }
    }

    private fun initializeLocationCallback() {
        mLocationCallback =
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    println("got")
                    viewmodel.latLng.value = LatLng(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude
                    )


                }
            }
    }


    override fun onStart() {
        super.onStart()
        mMapView?.onStart();
    }

    override fun onStop() {
        super.onStop()
        mMapView?.onStop();
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy();
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause();
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }
}