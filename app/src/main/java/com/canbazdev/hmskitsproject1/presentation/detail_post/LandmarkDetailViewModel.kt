package com.canbazdev.hmskitsproject1.presentation.detail_post

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.usecase.location.GetNearbySitesUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.login.InsertUserToFirebaseUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.GetLandmarkWithIdUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.GetWishListFromFirebaseUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.InsertLandmarkToWishListUseCase
import com.canbazdev.hmskitsproject1.util.ActionState
import com.canbazdev.hmskitsproject1.util.Constants
import com.canbazdev.hmskitsproject1.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/*
*   Created by hamzacanbaz on 7/27/2022
*/
@HiltViewModel
class LandmarkDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNearbySitesUseCase: GetNearbySitesUseCase,
    private val insertUserToFirebaseUseCase: InsertUserToFirebaseUseCase,
    private val getLandmarkWithIdUseCase: GetLandmarkWithIdUseCase,
    private val insertLandmarkToWishListUseCase: InsertLandmarkToWishListUseCase,
    private val getWishListUseCase: GetWishListFromFirebaseUseCase,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _landmark = MutableStateFlow(Post())
    val landmark: StateFlow<Post> = _landmark

    private val _actionState = MutableStateFlow<ActionState?>(null)
    val actionState: StateFlow<ActionState?> = _actionState

    lateinit var currentUserId: String


    init {
        getDetailLandmarkFromBundle()
        initializeUserId()
    }

    private fun initializeUserId() {
        viewModelScope.launch {
            dataStoreRepository.getCurrentUserId.collect {
                currentUserId = it
            }
        }
    }

    private fun getDetailLandmarkFromBundle() {
        savedStateHandle.get<Post>("landmark")?.let { post ->
            _landmark.value = post
            getNearbyPlaces()
        }
        savedStateHandle.get<String>(Constants.SCAN_UUID)?.let { id ->
            viewModelScope.launch {
                getLandmarkWithIdUseCase.invoke(id).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            if (result.data != null && result.data != Post()) {
                                _landmark.value = result.data
                            } else {
                                _actionState.value = ActionState.NavigateToHome
                            }

                        }
                        is Resource.Loading -> {}
                        is Resource.Error -> println("DETAIL ERROR")
                    }
                }
            }
        }
    }

    private fun getNearbyPlaces() {
        viewModelScope.launch {
            landmark.value.landmarkLatitude
                ?.let {
                    landmark.value.landmarkLongitude?.let { it1 ->
                        getNearbySitesUseCase.invoke(it, it1)
                    }
                }?.collect { resource ->
                    when (resource) {
                        is Resource.Success -> println("successssss" + resource.data?.size)
                        is Resource.Error -> println("erollllllll" + resource.errorMessage)
                        is Resource.Loading -> println("loaddddd")
                    }
                }
        }
    }

    // TODO UID KISMINI DEGISTIR
    private fun insertLandmarkToWishList() {
        viewModelScope.launch {
            println("WISH LISH $currentUserId ${landmark.value}")
            insertLandmarkToWishListUseCase.invoke(
                currentUserId,
                landmark.value
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> println("wish list loading")
                    is Resource.Success -> {
                        Toasty.success(getApplication(), "Added to To-Go List", Toast.LENGTH_SHORT)
                            .show()
                        println("wish list success")
                    }
                    is Resource.Error -> println("wish list error : ${result.errorMessage.toString()}")
                }
            }
        }
    }

    fun checkLandmarkAddedBefore() {
        viewModelScope.launch {
            getWishListUseCase.invoke(currentUserId)
                .collect { result ->
                    println("WISH")
                    when (result) {
                        is Resource.Loading -> println("wish loading")
                        is Resource.Success -> {
                            if (result.data != null ) {
                                var isAddedBefore = false
                                for (post in result.data) {
                                    if (landmark.value == post) {
                                        isAddedBefore = true
                                    }
                                }
                                if (!isAddedBefore) insertLandmarkToWishList()
                                else Toasty.error(
                                    getApplication(),
                                    "Already added",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            println("wish success ${result.data}")
                        }
                        is Resource.Error -> println("wish error ${result.errorMessage}")
                    }
                }

        }
    }

}