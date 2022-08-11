package com.canbazdev.hmskitsproject1.domain.repository

import android.graphics.Bitmap
import com.huawei.hms.location.LocationSettingsResponse
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark
import com.huawei.hms.site.api.model.Site

/*
*   Created by hamzacanbaz on 7/25/2022
*/
interface LocationRepository {
    suspend fun getLocationOptions(): LocationSettingsResponse
    suspend fun getRecognizedLandmark(landmarkImage: Bitmap): List<MLRemoteLandmark>
    suspend fun getNearbySites(lat: Double, lng: Double): List<Site>
}