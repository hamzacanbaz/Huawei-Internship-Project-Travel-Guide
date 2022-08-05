package com.canbazdev.hmskitsproject1.domain.usecase.location

import android.graphics.Bitmap
import com.canbazdev.hmskitsproject1.domain.repository.LocationRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.canbazdev.hmskitsproject1.util.Resource
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/25/2022
*/
class RecognizeLandmarkUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(landmarkImage: Bitmap): Flow<Resource<List<MLRemoteLandmark>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(locationRepository.getRecognizedLandmark(landmarkImage)))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: e.message.toString()))
            }
        }

}