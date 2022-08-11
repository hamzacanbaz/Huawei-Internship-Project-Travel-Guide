package com.canbazdev.hmskitsproject1.presentation.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.domain.model.landmark.NearbyLandmark
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.usecase.location.GetNearbySitesUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.GetAllPostsFromFirebaseUseCase
import com.canbazdev.hmskitsproject1.util.Resource
import com.huawei.hms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/28/2022
*/
@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllPostsFromFirebaseUseCase: GetAllPostsFromFirebaseUseCase,
    private val getNearbySitesUseCase: GetNearbySitesUseCase
) : ViewModel() {

    private val _postsList: MutableStateFlow<List<Post>> = MutableStateFlow(listOf())
    val postsList: StateFlow<List<Post>> = _postsList

    val latLng: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))

    private val nearbyLocationLatLng: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))

    private val _clickedMarkerName: MutableStateFlow<String> = MutableStateFlow("")
    val clickedMarkerName: StateFlow<String> = _clickedMarkerName

    private val _nearbyLandmarksList: MutableStateFlow<List<NearbyLandmark>> = MutableStateFlow(
        listOf()
    )
    val nearbyLandmarkList: StateFlow<List<NearbyLandmark>> = _nearbyLandmarksList

    init {
        getAllPostsFromFirebase()
    }

    fun updateClickedMarkerName(title: String) {
        _clickedMarkerName.value = title
    }

    private fun getAllPostsFromFirebase() {
        viewModelScope.launch {
            getAllPostsFromFirebaseUseCase.invoke().collect {
                when (it) {
                    is Resource.Success -> {
                        Log.i("Map ViewModel", "Success -> " + "${it.data}")
                        _postsList.value = it.data ?: listOf()
                    }
                    is Resource.Loading -> Log.i("Map ViewModel", "Loading")

                    is Resource.Error -> Log.i(
                        "Map ViewModel",
                        "Error -> ${it.errorMessage.toString()}"
                    )
                }
            }
        }
    }

    fun setNearbyLocationTitle(location: LatLng) {
        nearbyLocationLatLng.value = location
        getNearbyLocations()
    }

    private fun getNearbyLocations() {
        viewModelScope.launch {
            getNearbySitesUseCase.invoke(
                nearbyLocationLatLng.value.latitude,
                nearbyLocationLatLng.value.longitude
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> Log.i("Get Nearby Locations", "Loading")
                    is Resource.Success -> {
                        Log.i("Get Nearby Locations", "Success -> ${result.data}")
                        _nearbyLandmarksList.value = result.data ?: listOf()
                    }
                    is Resource.Error -> Log.i(
                        "Get Nearby Locations",
                        "Error -> ${result.errorMessage.toString()}"
                    )
                }
            }
        }
    }
}