package com.canbazdev.hmskitsproject1.domain.usecase.profile

import com.canbazdev.hmskitsproject1.domain.repository.ProfileRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 8/5/2022
*/
class GetTimesOfDayUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(profileRepository.getTimeOfDay()))
        } catch (e: Exception) {
            emit(Resource.Error(errorMessage = e.localizedMessage ?: e.message!!))
        }


    }

}