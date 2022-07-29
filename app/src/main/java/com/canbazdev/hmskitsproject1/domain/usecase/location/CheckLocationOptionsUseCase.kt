package com.canbazdev.hmskitsproject1.domain.usecase.location

import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.canbazdev.hmskitsproject1.util.Resource
import com.huawei.hms.location.LocationSettingsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/25/2022
*/
class CheckLocationOptionsUseCase @Inject constructor(
    private val userDataSource: RemoteDataSource
) {
    suspend operator fun invoke(): Flow<Resource<LocationSettingsResponse>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(userDataSource.checkLocationOptions()))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: e.message.toString()))
            }
        }
}