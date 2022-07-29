package com.canbazdev.hmskitsproject1.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.usecase.posts.GetAllPostsFromFirebaseUseCase
import com.canbazdev.hmskitsproject1.util.Resource
import com.huawei.hms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/28/2022
*/
@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllPostsFromFirebaseUseCase: GetAllPostsFromFirebaseUseCase
) : ViewModel() {

    private val _postsList: MutableStateFlow<List<Post>> = MutableStateFlow(listOf())
    val postsList: StateFlow<List<Post>> = _postsList

    val latLng: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0,0.0))

    init {
        getAllPostsFromFirebase()
    }

    private fun getAllPostsFromFirebase() {
        viewModelScope.launch {
            getAllPostsFromFirebaseUseCase.invoke().collect {
                when (it) {
                    is Resource.Success -> {
                        println("retrieving succes")
                        println("xx ${it.data?.size}")
                        _postsList.value = it.data ?: listOf()
                    }
                    is Resource.Loading -> println("retrieving loading")
                    is Resource.Error -> println("retrieving error")
                }
            }
        }
    }
}