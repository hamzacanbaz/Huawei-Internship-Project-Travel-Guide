package com.canbazdev.hmskitsproject1.presentation.register

import android.text.Editable
import android.util.Log
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

    private var userPassword = MutableStateFlow("")

    private var userVerificationCode = MutableStateFlow("")

    private var _isUserSignedUp = MutableStateFlow(false)
    val isUserSignedUp: StateFlow<Boolean> = _isUserSignedUp

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    fun getVerificationCode() {
        viewModelScope.launch {
            getEmailVerificationCodeUseCase.invoke(_userEmail.value).collect { result ->
                when (result) {
                    is Resource.Loading -> Log.i("Get Verification Code", "Loading")
                    is Resource.Success -> Log.i(
                        "Get Verification Code",
                        "Success -> ${result.data}"
                    )
                    is Resource.Error -> Log.i(
                        "Get Verification Code",
                        "Error ->  ${result.errorMessage.toString()}"
                    )
                }
            }
        }

    }

    fun signUpWithEmail() {
        viewModelScope.launch {
            signUpWithEmailUseCase.invoke(
                _userEmail.value,
                userPassword.value,
                userVerificationCode.value
            )
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            insertUserToFirebase(result.data.toString())
                            _userId.value = result.data.toString()
                            _isUserSignedUp.value = true
                        }
                        is Resource.Error -> {}
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
                    is Resource.Success -> Log.i("Insert User to Firebase", "Success -> ${it.data}")
                    is Resource.Loading -> Log.i("Insert User to Firebase", "Loading")
                    is Resource.Error -> Log.i(
                        "Insert User to Firebase",
                        "Error -> ${it.errorMessage.toString()}"
                    )
                }
            }
        }
    }

    fun updateEmail(s: Editable) {
        _userEmail.value = s.toString()
    }

    fun updatePassword(s: Editable) {
        userPassword.value = s.toString()
    }

    fun updateVerificationCode(s: Editable) {
        userVerificationCode.value = s.toString()
    }

}