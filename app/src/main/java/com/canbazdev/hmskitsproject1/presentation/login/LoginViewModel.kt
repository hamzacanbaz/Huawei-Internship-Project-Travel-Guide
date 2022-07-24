package com.canbazdev.hmskitsproject1.presentation.login

import android.app.Application
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.domain.usecase.login.*
import com.canbazdev.hmskitsproject1.util.Resource
import com.canbazdev.hmskitsproject1.util.SilentSignInStatus
import com.huawei.hms.support.account.service.AccountAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/6/2022
*/
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val setEnabledSilentSignInUseCase: SetEnabledSilentSignInUseCase,
    private val checkUserLoginUseCase: CheckUserLoginUseCase,
    private val signInWithHuaweiIdUseCase: SignInWithHuaweiIdUseCase,
    private val signOutWithHuaweiUseCase: SignOutWithHuaweiUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    application: Application
) :
    AndroidViewModel(application) {
    private var _huaweiSignIn = MutableStateFlow<AccountAuthService?>(null)
    val huaweiSignIn: StateFlow<AccountAuthService?>
        get() = _huaweiSignIn

    private var _uiState = MutableStateFlow(0)
    val uiState: StateFlow<Int>
        get() = _uiState

    private var _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private var _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private var _userPassword = MutableStateFlow("")
    val userPassword: StateFlow<String> = _userPassword

    private var _isUserSignedIn = MutableStateFlow(false)
    val isUserSignedIn: StateFlow<Boolean> = _isUserSignedIn


    init {
        _uiState.value = -1
    }

    fun signInWithHuawei() {
        viewModelScope.launch {
            println("asd")
            signInWithHuaweiIdUseCase.invoke().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _huaweiSignIn.value = result.data
                        _isUserSignedIn.value = true
                        result.data?.silentSignIn()?.addOnSuccessListener {
                            _userName.value = it.displayName
                        }
                        setSilentSigninEnabled(true)
                        _uiState.value = 1

                    }
                    else -> {}
                }
            }
        }

    }

    fun checkUserLogin() {
        viewModelScope.launch {
            // TODO CHANGE THIS USECASE
            // TODO NOW, IT IS WORKING WRONG
            checkUserLoginUseCase.invoke(this).collect { status ->
                when (status) {
                    SilentSignInStatus.SUCCESS -> {
                        //signInWithHuawei()
                        _uiState.value = 1
                    }
                    SilentSignInStatus.FAIL -> _uiState.value = -1
                    SilentSignInStatus.LOADING -> _uiState.value = 0
                    SilentSignInStatus.DISALLOWED -> _uiState.value = -2
                }
            }
        }
    }

    fun signInWithEmail() {
        viewModelScope.launch {
            signInWithEmailUseCase.invoke(
                userEmail.value,
                userPassword.value
            )
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> println("loading")
                        is Resource.Success -> {
                            _isUserSignedIn.value = true
                        }
                        is Resource.Error -> println(result.errorMessage)
                    }

                }
        }

    }

    private fun setSilentSigninEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            setEnabledSilentSignInUseCase.invoke(isEnabled)
        }
    }

    /* fun signOutHuawei() {

         viewModelScope.launch {
             loginRepository.signOut(onSuccess = {
                 setSilentSigninEnabled(false)
                 _uiState.value = -1
                 println(huaweiSignIn.value)
             }, onFail = { _uiState.value = 1 })
         }
     }*/


    fun signOutHuawei() {
        viewModelScope.launch {
            signOutWithHuaweiUseCase.invoke().collect { result ->
                when (result) {
                    is Resource.Loading -> println("sign out loading")
                    is Resource.Success -> {
                        setSilentSigninEnabled(false)
                        println("sign out success")
                        _uiState.value = -1
                        _uiState.value = -2
                        _isUserSignedIn.value = false
                    }
                    is Resource.Error -> {
                        println("sign out error")
                        _uiState.value = 1
                        println(result.errorMessage.toString())
                    }
                }
            }
        }
    }

    fun updateEmail(s: Editable) {
        _userEmail.value = s.toString()
    }

    fun updatePassword(s: Editable) {
        _userPassword.value = s.toString()
    }


}