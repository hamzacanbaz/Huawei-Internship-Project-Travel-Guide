package com.canbazdev.hmskitsproject1.presentation.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.usecase.posts.DeleteLandmarkFromWishListUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.GetPostsByUserIdUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.GetWishListFromFirebaseUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.profile.GetTimesOfDayUseCase
import com.canbazdev.hmskitsproject1.util.ActionState
import com.canbazdev.hmskitsproject1.util.Resource
import com.huawei.agconnect.auth.AGConnectAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dmoral.toasty.Toasty
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
    private val getPostsByUserIdUseCase: GetPostsByUserIdUseCase,
    private val getWishListFromFirebaseUseCase: GetWishListFromFirebaseUseCase,
    private val getTimesOfDayUseCase: GetTimesOfDayUseCase,
    private val deleteLandmarkFromWishListUseCase: DeleteLandmarkFromWishListUseCase,
    application: Application
) : AndroidViewModel(application) {
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private val _landmarks = MutableStateFlow<List<Post>>(listOf())
    val landmarks: StateFlow<List<Post>> = _landmarks

    private val _wishListLandmarks = MutableStateFlow<List<Post>>(listOf())
    private val wishListLandmarks: StateFlow<List<Post>> = _wishListLandmarks

    private val _currentLandmarks = MutableStateFlow<List<Post>>(listOf())
    val currentLandmarks: StateFlow<List<Post>> = _currentLandmarks

    private val _actionState = MutableStateFlow<ActionState?>(null)
    val actionState: StateFlow<ActionState?> = _actionState

    private val _dayTime = MutableStateFlow("")
    val dayTime: StateFlow<String> = _dayTime

    private lateinit var currentUserId: String


    init {
        updateUserInfo()
        getPosts()
        getWishListPosts()
        getLocationCurrentWeather()
        initializeUserId()
    }

    private fun initializeUserId() {
        viewModelScope.launch {
            dataStoreRepository.getCurrentUserId.collect {
                currentUserId = it
            }
        }
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

    fun getPosts() {
        viewModelScope.launch {
            getPostsByUserIdUseCase.invoke(userId.value).collect {
                when (it) {
                    is Resource.Loading -> println("profile posts loading")
                    is Resource.Success -> {
                        if (it.data?.isNotEmpty() == true) {
                            _landmarks.value = it.data
                            setSharedLandmarks()
                        }
                        println("profile posts success ")
                    }
                    is Resource.Error -> println("profile posts error")
                }
            }
        }
    }

    private fun getWishListPosts() {
        viewModelScope.launch {
            getWishListFromFirebaseUseCase.invoke(userId.value).collect {
                when (it) {
                    is Resource.Loading -> println("wishlist posts loading")
                    is Resource.Success -> {
                        if (it.data?.isNotEmpty() == true) {
                            _wishListLandmarks.value = it.data
                        }
                        println("wishlist posts success ")
                    }
                    is Resource.Error -> println("wishlist posts error")
                }
            }
        }
    }

    fun removeFromWishList(postId: String) {
        viewModelScope.launch {
            deleteLandmarkFromWishListUseCase.invoke(currentUserId, postId)
                .collect {
                    if (it is Resource.Success) {
                        Toasty.success(
                            getApplication(),
                            "Landmark Removed Successfully",
                            Toasty.LENGTH_SHORT
                        ).show()
                        getWishListPosts()
                        setWishListLandmarks()
                    }
                }
        }
    }


    fun setSharedLandmarks() {
        _currentLandmarks.value = listOf()
        _currentLandmarks.value = landmarks.value
    }

    fun setWishListLandmarks() {
        getWishListPosts()
        _currentLandmarks.value = listOf()
        _currentLandmarks.value = wishListLandmarks.value
    }

    fun logOut() {
        clearUserInfo()
        AGConnectAuth.getInstance().signOut()
        unableSilentSignIn()

    }

    private fun clearUserInfo() {
        viewModelScope.launch {
            dataStoreRepository.setCurrentUserEmail("")
            dataStoreRepository.setCurrentUserId("")
        }
    }

    private fun unableSilentSignIn() {
        viewModelScope.launch {
            dataStoreRepository.setSilentSignInEnabled(false)
            _actionState.value = ActionState.NavigateToRegister
        }
    }

    private fun getLocationCurrentWeather() {

        viewModelScope.launch {
            getTimesOfDayUseCase.invoke().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        println("loading")
                    }
                    is Resource.Success -> {
                        _dayTime.value = result.data.toString()
                    }
                    is Resource.Error -> {
                        _dayTime.value = "Good Day!"
                    }
                }
            }
        }

    }


}