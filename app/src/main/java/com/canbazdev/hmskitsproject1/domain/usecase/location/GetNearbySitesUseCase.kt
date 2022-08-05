package com.canbazdev.hmskitsproject1.domain.usecase.location

import com.canbazdev.hmskitsproject1.domain.model.landmark.NearbyLandmark
import com.canbazdev.hmskitsproject1.domain.model.landmark.toNearbyLandmark
import com.canbazdev.hmskitsproject1.domain.repository.LocationRepository
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/27/2022
*/
class GetNearbySitesUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(lat: Double, lng: Double): Flow<Resource<List<NearbyLandmark>>> =
        flow {
            emit(Resource.Loading())
            try {
                val nearbyLandmarkList = locationRepository.getNearbySites(lat, lng).map { site ->
                    println(site.toNearbyLandmark())
                    site.toNearbyLandmark()
                }
                emit(Resource.Success(nearbyLandmarkList))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: e.message.toString()))
            }
        }
}