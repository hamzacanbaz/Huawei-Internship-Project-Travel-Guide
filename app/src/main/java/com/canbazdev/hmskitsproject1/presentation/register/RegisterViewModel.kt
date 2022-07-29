package com.canbazdev.hmskitsproject1.presentation.register

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.domain.usecase.login.GetEmailVerificationCodeUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.login.InsertUserToFirebaseUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.login.SignUpWithEmailUseCase
import com.canbazdev.hmskitsproject1.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/24/2022
*/
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val getEmailVerificationCodeUseCase: GetEmailVerificationCodeUseCase,
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val insertUserToFirebaseUseCase: InsertUserToFirebaseUseCase
) : ViewModel() {

    private var _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private var _userPassword = MutableStateFlow("")
    val userPassword: StateFlow<String> = _userPassword

    private var _userVerificationCode = MutableStateFlow("")
    val userVerificationCode: StateFlow<String> = _userVerificationCode

    private var _isUserSignedUp = MutableStateFlow(false)
    val isUserSignedUp: StateFlow<Boolean> = _isUserSignedUp

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    fun getVerificationCode() {
        viewModelScope.launch {
            getEmailVerificationCodeUseCase.invoke(_userEmail.value).collect { result ->
                when (result) {
                    is Resource.Loading -> println("loading")
                    is Resource.Success -> println(result.data)
                    is Resource.Error -> println(result.errorMessage)
                }
            }
        }

    }

    fun signUpWithEmail() {
        viewModelScope.launch {
            signUpWithEmailUseCase.invoke(
                _userEmail.value,
                _userPassword.value,
                _userVerificationCode.value
            )
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> println("loading")
                        is Resource.Success -> {
                            insertUserToFirebase(result.data.toString())
                            _userId.value = result.data.toString()
                            _isUserSignedUp.value = true
                        }
                        is Resource.Error -> println(result.errorMessage)
                    }

                }
        }
    }

    private fun insertUserToFirebase(id: String) {
        var userFirebase = UserFirebase()
        userFirebase = userFirebase.copy(email = userEmail.value, id = id)
        viewModelScope.launch {
            insertUserToFirebaseUseCase.invoke(userFirebase).collect {
                when (it) {
                    is Resource.Success -> println("added to fb")
                    is Resource.Loading -> println("loading fb")
                    is Resource.Error -> println("failed to fb")
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

    fun updateVerificationCode(s: Editable) {
        _userVerificationCode.value = s.toString()
    }

}