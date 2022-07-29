package com.canbazdev.hmskitsproject1.presentation.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.GetAllPostsFromFirebaseUseCase
import com.canbazdev.hmskitsproject1.util.ActionState
import com.canbazdev.hmskitsproject1.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: DataStoreRepository,
    private val getAllPostsFromFirebaseUseCase: GetAllPostsFromFirebaseUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), PostsAdapter.OnItemClickedListener {

    private val _postsList: MutableStateFlow<List<Post>> = MutableStateFlow(listOf())
    val postsList: StateFlow<List<Post>> = _postsList

    private val _actionState = MutableStateFlow<ActionState?>(null)
    val actionState: StateFlow<ActionState?> = _actionState

    private val _userInfo = MutableStateFlow(UserFirebase())
    val userInfo: StateFlow<UserFirebase> = _userInfo


    init {
        getAllPostsFromFirebase()
        getUserInfo()
    }

    private fun getUserInfo() {
        savedStateHandle.get<UserFirebase>("userInfo")?.let { user ->
            println("user geldi ${user.id}")
            _userInfo.value = user
            updateCurrentUserInfo()
        }
    }

    private fun updateCurrentUserInfo() {
        viewModelScope.launch {
            repository.setCurrentUserEmail(userInfo.value.email)
            repository.setCurrentUserId(userInfo.value.id)

        }
    }

    fun clearUserInfo() {
        viewModelScope.launch {
            repository.setCurrentUserEmail("")
            repository.setCurrentUserId("")
        }
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

    fun unableSilentSignIn() {
        viewModelScope.launch {
            repository.setSilentSignInEnabled(false)
            _actionState.value = ActionState.NavigateToRegister
        }
    }

    override fun onItemClicked(position: Int, post: Post) {
        _actionState.value = ActionState.NavigateTDetailLandmark(post)
        _actionState.value = null
    }
}