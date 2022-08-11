package com.canbazdev.hmskitsproject1.domain.usecase.login

import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/24/2022
*/
class SetEnabledSilentSignInUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        dataStoreRepository.setSilentSignInEnabled(isEnabled)
    }
}