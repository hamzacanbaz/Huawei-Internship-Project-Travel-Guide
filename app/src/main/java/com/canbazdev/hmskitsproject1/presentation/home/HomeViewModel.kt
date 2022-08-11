package com.canbazdev.hmskitsproject1.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.GetAllPostsFromFirebaseUseCase
import com.canbazdev.hmskitsproject1.util.ActionState
import com.canbazdev.hmskitsproject1.util.Constants
import com.canbazdev.hmskitsproject1.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    val repository: DataStoreRepository,
    private val getAllPostsFromFirebaseUseCase: GetAllPostsFromFirebaseUseCase,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application), PostsAdapter.OnItemClickedListener {

    private val _postsList: MutableStateFlow<List<Post>> = MutableStateFlow(listOf())
    val postsList: StateFlow<List<Post>> = _postsList

    private val _actionState = MutableStateFlow<ActionState?>(null)
    val actionState: StateFlow<ActionState?> = _actionState

    private val userInfo = MutableStateFlow(UserFirebase())


    init {
        getAllPostsFromFirebase()
        getUserInfo()
        getLandmarkError()


    }


    private fun getUserInfo() {
        savedStateHandle.get<UserFirebase>("userInfo")?.let { user ->
            userInfo.value = user
            updateCurrentUserInfo()
        }
    }

    private fun getLandmarkError() {
        savedStateHandle.get<String>(Constants.LANDMARK_NO_FOUND)?.let { error ->
            _actionState.value = ActionState.ShowToast(error)
        }

    }

    private fun updateCurrentUserInfo() {
        viewModelScope.launch {
            repository.setCurrentUserEmail(userInfo.value.email)
            repository.setCurrentUserId(userInfo.value.id)

        }
    }


    private fun getAllPostsFromFirebase() {
        viewModelScope.launch {
            getAllPostsFromFirebaseUseCase.invoke().collect {
                when (it) {
                    is Resource.Success -> {
                        Log.i("Get All Landmarks", "Success -> ${it.data ?: listOf()} ")
                        _postsList.value = it.data ?: listOf()
                    }
                    is Resource.Loading -> Log.i("Get All Landmarks", "Loading")
                    is Resource.Error -> Log.i(
                        "Get All Landmarks",
                        "Error -> ${it.errorMessage.toString()}"
                    )
                }
            }
        }
    }


    override fun onItemClicked(position: Int, post: Post) {
        _actionState.value = ActionState.NavigateTDetailLandmark(post)
        _actionState.value = null
    }

}