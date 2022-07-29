package com.canbazdev.hmskitsproject1.presentation.detail_post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.domain.usecase.location.GetNearbySitesUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.login.InsertUserToFirebaseUseCase
import com.canbazdev.hmskitsproject1.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/27/2022
*/
@HiltViewModel
class LandmarkDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNearbySitesUseCase: GetNearbySitesUseCase,
    private val insertUserToFirebaseUseCase: InsertUserToFirebaseUseCase
) : ViewModel() {

    private val _landmark = MutableStateFlow(Post())
    val landmark: StateFlow<Post> = _landmark

    init {
        getDetailLandmarkFromBundle()
        viewModelScope.launch {
            insertUserToFirebaseUseCase.invoke(
                UserFirebase(
                    "12312",
                    "jasndjsa@asdasd.com",
                    "adsasdassa"
                )
            ).collect {
                when(it){
                    is Resource.Success-> println("successs")
                    is Resource.Loading-> println("loadingg")
                    is Resource.Error-> println(it.errorMessage)
                }
            }
        }
    }

    private fun getDetailLandmarkFromBundle() {
        savedStateHandle.get<Post>("landmark")?.let { post ->
            _landmark.value = post
            getNearbyPlaces()
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
}