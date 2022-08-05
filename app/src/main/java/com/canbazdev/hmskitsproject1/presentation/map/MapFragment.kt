package com.canbazdev.hmskitsproject1.presentation.map

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentMapBinding
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import com.huawei.hms.location.*
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint

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

        val mLocationRequest = LocationRequest().apply {
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
        listenMarkers()

    }

    private fun addMarkersToLandmarks() {
        lifecycleScope.launchWhenCreated {
            viewmodel.postsList.collect { postList ->
                postList.forEach { landmark ->
                    hMap?.addMarker(
                        MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker())
                            .title(landmark.landmarkName)
                            .position(
                                landmark.landmarkLongitude?.let { lng ->
                                    landmark.landmarkLatitude?.let { ltd ->
                                        LatLng(
                                            ltd,
                                            lng
                                        )
                                    }
                                }
                            )

                    )
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewmodel.nearbyLandmarkList.collect { landmarkList ->
                landmarkList.forEach { landmark ->

                    hMap?.addMarker(
                        MarkerOptions()
                            .icon(
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_food_bank_24)
                            )
                            .title(landmark.name)
                            .position(
                                LatLng(landmark.latitude, landmark.longitude)
                            )
                    )

                }

            }
        }
    }

    private fun listenMarkers() {
        hMap?.setOnMarkerClickListener { marker ->
            println("marker clicked")
            Toast.makeText(context, marker.title, Toast.LENGTH_SHORT).show()
            viewmodel.updateClickedMarkerName(marker.title)
            setupDialog(marker)

            false
        }
    }

    private fun setupDialog(marker: Marker) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_ask, null)
        view.findViewById<TextView>(R.id.tvTitle).text = marker.title
        val builder = android.app.AlertDialog.Builder(context)
            .setView(view)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.show()
        view.findViewById<ImageView>(R.id.ivShowNearby).setOnClickListener {
            viewmodel.setNearbyLocationTitle(marker.position)
            dialog.dismiss()
        }
        view.findViewById<ImageView>(R.id.ivGoToMap).setOnClickListener {
            goToPetalMaps()
            dialog.dismiss()
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

    private fun goToPetalMaps() {
        val uriString =
            "petalmaps://textSearch?text=" + viewmodel.clickedMarkerName.value
        val contentUrl: Uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_VIEW, contentUrl)
        if (activity?.let { it1 -> intent.resolveActivity(it1.packageManager) } != null) {
            startActivity(intent)
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