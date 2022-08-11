package com.canbazdev.hmskitsproject1.domain.usecase.login

import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/24/2022
*/
class GetEnabledSilentSignInUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<Boolean> = flow {
        dataStoreRepository.getEnabled().collect {
            emit(it)
        }
    }
}