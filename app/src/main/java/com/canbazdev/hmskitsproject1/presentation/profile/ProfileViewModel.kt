package com.canbazdev.hmskitsproject1.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.usecase.posts.GetPostsByUserIdUseCase
import com.canbazdev.hmskitsproject1.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/29/2022
*/
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val getPostsByUserIdUseCase: GetPostsByUserIdUseCase
) : ViewModel() {
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private val _landmarks = MutableStateFlow<List<Post>>(listOf())
    val landmarks: StateFlow<List<Post>> = _landmarks

    init {
        updateUserInfo()
        getPosts()
    }

    private fun updateUserInfo() {
        viewModelScope.launch {
            dataStoreRepository.getCurrentUserId.collect {
                println(it)
                _userId.value = it
            }

        }
        viewModelScope.launch {
            dataStoreRepository.getCurrentUserEmail.collect {
                _userEmail.value = it
            }
        }
    }

    private fun getPosts() {
        viewModelScope.launch {
            getPostsByUserIdUseCase.invoke(userId.value).collect {
                when (it) {
                    is Resource.Loading -> println("profile posts loading")
                    is Resource.Success -> {
                        if (it.data?.isNotEmpty() == true) {
                            _landmarks.value = it.data
                        }
                        println("profile posts success ")
                    }
                    is Resource.Error -> println("profile posts error")
                }
            }
        }
    }
}