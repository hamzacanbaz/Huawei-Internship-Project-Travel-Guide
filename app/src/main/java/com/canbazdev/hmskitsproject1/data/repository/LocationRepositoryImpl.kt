package com.canbazdev.hmskitsproject1.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import com.canbazdev.hmskitsproject1.domain.repository.LocationRepository
import com.canbazdev.hmskitsproject1.util.Work
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.LocationSettingsRequest
import com.huawei.hms.location.LocationSettingsResponse
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.model.*
import java.io.IOException

/*
*   Created by hamzacanbaz on 7/25/2022
*/
class LocationRepositoryImpl(
    private val application: Application,
    private val searchService: SearchService
) : LocationRepository {

    override fun getLocationOptions(): Work<LocationSettingsResponse> {
        val work = Work<LocationSettingsResponse>()
        val locationSettingsRequest =
            LocationSettingsRequest.Builder().addLocationRequest(LocationRequest()).build()
        // Check the device location settings.
        val task = LocationServices.getSettingsClient(application)
            .checkLocationSettings(locationSettingsRequest)
        // Define the listener for success in calling the API for checking device location settings.
        task.addOnSuccessListener { locationSettingsResponse ->
            work.onSuccess(locationSettingsResponse)
        }
        // Define callback for failure in checking the device location settings.
        task.addOnFailureListener { e ->
            work.onFailure(e)
        }
        return work
    }

    override fun getRecognizedLandmark(landmarkImage: Bitmap): Work<List<MLRemoteLandmark>> {
        val work = Work<List<MLRemoteLandmark>>()
        val analyzer = MLAnalyzerFactory.getInstance().remoteLandmarkAnalyzer
        val mlFrame = MLFrame.Creator().setBitmap(landmarkImage).create()
        val task = analyzer!!.asyncAnalyseFrame(mlFrame)
        task.addOnSuccessListener {
            work.onSuccess(it)
        }
        task.addOnFailureListener {
            work.onFailure(it)
        }
        try {
            analyzer.stop()
        } catch (e: IOException) {
            println("analyzer stop error ${e.localizedMessage}")
        }
        return work
    }

    override fun getNearbySites(lat: Double, lng: Double): Work<List<Site>> {
        val work = Work<List<Site>>()
        // Instantiate the SearchService object.


// Create a request body.
        val request = NearbySearchRequest()
        val location = Coordinate(lat, lng)
        request.apply {
            this.location = location
            hwPoiType = HwLocationType.RESTAURANT
            radius = 1000
            pageSize = 20
            pageIndex = 1
        }
// Create a search result listener.
        val resultListener: SearchResultListener<NearbySearchResponse> =
            object : SearchResultListener<NearbySearchResponse> {
                // Return search results upon a successful search.
                override fun onSearchResult(results: NearbySearchResponse?) {
                    if (results == null || results.totalCount <= 0) {
                        return
                    }
                    val sites: List<Site>? = results.sites
                    if (sites == null || sites.isEmpty()) {
                        return
                    }
                    for (site in sites) {
                        Log.i("TAG", "siteId: ${site.siteId}, name: ${site.name}")
                    }
                    work.onSuccess(sites)
                }

                // Return the result code and description upon a search exception.
                override fun onSearchError(status: SearchStatus) {
                    work.onFailure(Exception(status.errorMessage))
                    Log.i("TAG", "Error : ${status.errorCode}  ${status.errorMessage}")
                }
            }
// Call the nearby place search API.
        searchService.nearbySearch(request, resultListener)
        return work
    }


}