package com.canbazdev.hmskitsproject1.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.domain.usecase.login.GetEnabledSilentSignInUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.login.SetEnabledSilentSignInUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.login.SignInWithHuaweiIdUseCase
import com.canbazdev.hmskitsproject1.util.Resource
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val signInWithHuaweiIdUseCase: SignInWithHuaweiIdUseCase,
    private val setEnabledSilentSignInUseCase: SetEnabledSilentSignInUseCase,
    private val getEnabledSilentSignInUseCase: GetEnabledSilentSignInUseCase
) : ViewModel() {

    private var _isUserSignedIn = MutableStateFlow(false)
    val isUserSignedIn: StateFlow<Boolean> = _isUserSignedIn

    val currentUser: AGConnectUser? = AGConnectAuth.getInstance().currentUser

    val signedEnable = MutableStateFlow<Boolean?>(null)


    fun checkEnable() {
        viewModelScope.launch {
            getEnabledSilentSignInUseCase.invoke().collect {
                signedEnable.value = it
            }
        }
    }


    fun signInWithHuawei() {
        viewModelScope.launch {
            signInWithHuaweiIdUseCase.invoke().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _isUserSignedIn.value = true
                        setSilentSigninEnabled()
                    }
                    else -> {}
                }
            }
        }

    }

    private fun setSilentSigninEnabled() {
        viewModelScope.launch {
            setEnabledSilentSignInUseCase.invoke(true)
        }
    }


}