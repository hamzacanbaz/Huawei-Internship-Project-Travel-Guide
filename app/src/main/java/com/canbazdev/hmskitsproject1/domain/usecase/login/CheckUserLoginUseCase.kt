package com.canbazdev.hmskitsproject1.domain.usecase.login

import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import com.canbazdev.hmskitsproject1.domain.repository.LoginRepository
import com.canbazdev.hmskitsproject1.util.SilentSignInStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/24/2022
*/
class CheckUserLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(coroutineScope: CoroutineScope) = channelFlow {

        dataStoreRepository.getSilentSignInEnabled.collect { isEnabled ->
            println("is enabled $isEnabled")
            if (isEnabled) {

                loginRepository.silentSignIn(onSuccess = {
                    coroutineScope.launch {
                        send(SilentSignInStatus.SUCCESS)
                    }
                }, onFail = {
                    coroutineScope.launch {
                        send(SilentSignInStatus.FAIL)
                    }
                })
            } else {
                coroutineScope.launch {
                    send(SilentSignInStatus.DISALLOWED)
                }
            }
        }
    }
}