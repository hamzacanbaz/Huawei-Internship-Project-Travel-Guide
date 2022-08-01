package com.canbazdev.hmskitsproject1.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.domain.usecase.login.CheckUserLoginUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.login.GetEnabledSilentSignInUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.login.SetEnabledSilentSignInUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.login.SignInWithHuaweiIdUseCase
import com.canbazdev.hmskitsproject1.util.Resource
import com.canbazdev.hmskitsproject1.util.SilentSignInStatus
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUserLoginUseCase: CheckUserLoginUseCase,
    private val signInWithHuaweiIdUseCase: SignInWithHuaweiIdUseCase,
    private val setEnabledSilentSignInUseCase: SetEnabledSilentSignInUseCase,
    private val getEnabledSilentSignInUseCase: GetEnabledSilentSignInUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow(0)
    val uiState: StateFlow<Int>
        get() = _uiState

    private var _isUserSignedIn = MutableStateFlow(false)
    val isUserSignedIn: StateFlow<Boolean> = _isUserSignedIn


    val currentUser: AGConnectUser? = AGConnectAuth.getInstance().currentUser

    val signedEnable = MutableStateFlow<Boolean?>(null)

    init {

        //checkUserLogin()
    }

    /*fun checkEnable(){
        viewModelScope.launch {
            repo.getEnabled().collect {
                signedEnable.value = it
            }
        }
    }*/

    fun checkEnable() {
        viewModelScope.launch {
            getEnabledSilentSignInUseCase.invoke().collect {
                println("aaaaa")
                signedEnable.value = it
            }
        }
    }

    fun checkUserLogin() {
        viewModelScope.launch {
            // TODO CHANGE THIS USE CASE
            // TODO NOW, IT IS WORKING WRONG
            checkUserLoginUseCase.invoke(this).collect { status ->
                when (status) {
                    SilentSignInStatus.SUCCESS -> {
                        signInWithHuawei()
                        _uiState.value = 1
                    }
                    SilentSignInStatus.FAIL -> _uiState.value = -1
                    SilentSignInStatus.LOADING -> _uiState.value = 0
                    SilentSignInStatus.DISALLOWED -> _uiState.value = -2
                }
            }
        }
    }

    fun signInWithHuawei() {
        println("asdaasdasdasd")
        viewModelScope.launch {
            println("asd")
            signInWithHuaweiIdUseCase.invoke().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.value = 5
                        println("giriş yapılıypr")
                        // _huaweiSignIn.value = result.data
                        _isUserSignedIn.value = true
                        /*  result.data?.silentSignIn()?.addOnSuccessListener {
                              _userName.value = it.displayName
                          }*/
                        setSilentSigninEnabled(true)
                        _uiState.value = 1

                    }
                    else -> {
                        println("oh")
                    }
                }
            }
        }

    }

    private fun setSilentSigninEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            setEnabledSilentSignInUseCase.invoke(isEnabled)
        }
    }


}