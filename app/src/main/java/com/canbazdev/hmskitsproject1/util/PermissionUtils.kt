package com.canbazdev.hmskitsproject1.util

import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

/*
*   Created by hamzacanbaz on 7/20/2022
*/
object PermissionUtils {

    const val ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION
    const val ACCESS_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION
    const val LOCATION_REQUEST_CODE = 100


    fun hasLocationPermissions(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )


    /*fun checkSelfPermissionForLocation(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            context,
            ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermissions(
        fragment: Fragment
    ) {
        ActivityCompat.requestPermissions(
            fragment.requireActivity(), arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            ), LOCATION_REQUEST_CODE
        )
    }
    fun verifyPermissions(grantResults: IntArray): Boolean {
        if (grantResults.isEmpty()) {
            return false
        }

        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun shouldShowRequestForLocationPermission(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            ACCESS_FINE_LOCATION
        ) || ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            ACCESS_COARSE_LOCATION
        )
    }


     private const val LOCATION_REQUEST_CODE = 100

     private fun requestLocationPermissions(activity: Activity) {
         ActivityCompat.requestPermissions(
             activity,
             arrayOf(
                 Manifest.permission.ACCESS_COARSE_LOCATION,
                 Manifest.permission.ACCESS_FINE_LOCATION
             ),
             LOCATION_REQUEST_CODE
         )
     }

     private fun checkLocationPermissions(context: Context): Boolean {
         if (ActivityCompat.checkSelfPermission(
                 context,
                 Manifest.permission.ACCESS_COARSE_LOCATION
             ) == PackageManager.PERMISSION_GRANTED &&
             ActivityCompat.checkSelfPermission(
                 context,
                 Manifest.permission.ACCESS_FINE_LOCATION
             ) == PackageManager.PERMISSION_GRANTED
         ) {
             return true
         }
         return false
     }

     fun getLocation(activity: Activity, onSuccessCallback: (Double, Double) -> Unit,) {
         if (checkLocationPermissions(activity)) {
             LocationRequest.create().apply {
                 priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                 interval = 0
                 fastestInterval = 0
                 numUpdates = 1

                 if (ActivityCompat.checkSelfPermission(
                         activity.applicationContext,
                         Manifest.permission.ACCESS_FINE_LOCATION
                     ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                         activity.applicationContext,
                         Manifest.permission.ACCESS_COARSE_LOCATION
                     ) != PackageManager.PERMISSION_GRANTED
                 ) {
                     return
                 } else {
                     val client = LocationServices.getFusedLocationProviderClient(activity)
                     client.lastLocation.addOnSuccessListener { location: Location? ->
                         location?.let { loc ->
                             onSuccessCallback(loc.latitude, loc.longitude)
                         }
                     }
                 }
             }
         } else {
             requestLocationPermissions(activity)
         }
     }*/


}