package com.canbazdev.hmskitsproject1.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/29/2022
*/
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    init {
        updateUserInfo()
    }

    private fun updateUserInfo() {
        viewModelScope.launch {
            dataStoreRepository.getCurrentUserId.collect {
                _userId.value = it
            }

        }
        viewModelScope.launch {
            dataStoreRepository.getCurrentUserEmail.collect {
                _userEmail.value = it
            }
        }
    }
}