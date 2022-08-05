package com.canbazdev.hmskitsproject1.data.repository

import android.graphics.Bitmap
import com.canbazdev.hmskitsproject1.domain.repository.LocationRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.huawei.hms.location.LocationSettingsResponse
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark
import com.huawei.hms.site.api.model.Site
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*
*   Created by hamzacanbaz on 7/25/2022
*/
class LocationRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : LocationRepository {

    override suspend fun getLocationOptions(): LocationSettingsResponse {
        return suspendCoroutine { continuation ->
            remoteDataSource.checkLocationOptions()
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    }

    override suspend fun getRecognizedLandmark(landmarkImage: Bitmap): List<MLRemoteLandmark> {
        return suspendCoroutine { continuation ->
            remoteDataSource.recognizeLandmark(landmarkImage)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    }

    override suspend fun getNearbySites(lat: Double, lng: Double): List<Site> {

        return suspendCoroutine { continuation ->
            remoteDataSource.getNearbySites(lat, lng)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }


}