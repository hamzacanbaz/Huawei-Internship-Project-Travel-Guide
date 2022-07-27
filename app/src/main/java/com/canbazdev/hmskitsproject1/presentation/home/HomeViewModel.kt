package com.canbazdev.hmskitsproject1.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import com.canbazdev.hmskitsproject1.domain.model.Post
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
    private val getAllPostsFromFirebaseUseCase: GetAllPostsFromFirebaseUseCase
) : ViewModel(), PostsAdapter.OnItemClickedListener {

    private val _postsList: MutableStateFlow<List<Post>> = MutableStateFlow(listOf())
    val postsList: StateFlow<List<Post>> = _postsList

    private val _actionState = MutableStateFlow<ActionState?>(null)
    val actionState: StateFlow<ActionState?> = _actionState


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

    fun unableSilentSignIn() {
        viewModelScope.launch {
            repository.setSilentSignInEnabled(false)
        }
    }

    override fun onItemClicked(position: Int, post: Post) {
        _actionState.value = ActionState.NavigateTDetailLandmark(post)
        _actionState.value = null
    }
}