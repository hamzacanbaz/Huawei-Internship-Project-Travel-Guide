package com.canbazdev.hmskitsproject1.presentation.home

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.domain.usecase.notification.SendNotificationUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.GetAllPostsFromFirebaseUseCase
import com.canbazdev.hmskitsproject1.util.ActionState
import com.canbazdev.hmskitsproject1.util.Constants
import com.canbazdev.hmskitsproject1.util.Resource
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
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
    private val savedStateHandle: SavedStateHandle,
    private val sendNotificationUseCase: SendNotificationUseCase
) : AndroidViewModel(application), PostsAdapter.OnItemClickedListener {

    private val _postsList: MutableStateFlow<List<Post>> = MutableStateFlow(listOf())
    val postsList: StateFlow<List<Post>> = _postsList

    private val _actionState = MutableStateFlow<ActionState?>(null)
    val actionState: StateFlow<ActionState?> = _actionState

    private val _userInfo = MutableStateFlow(UserFirebase())
    val userInfo: StateFlow<UserFirebase> = _userInfo
    var accessToken = ""
    var pushToken = ""


    init {
        getAllPostsFromFirebase()
        getUserInfo()
        getLandmarkError()
        // getToken()


    }

    fun setPushTokenFirst(token: String) {
        pushToken = token
        println("PUSH $pushToken")
        sendNotificationUseCase.invoke(pushToken)
    }

    private fun getUserInfo() {
        savedStateHandle.get<UserFirebase>("userInfo")?.let { user ->
            println("user geldi ${user.id}")
            _userInfo.value = user
            updateCurrentUserInfo()
        }
    }

    private fun getLandmarkError(){
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
        println(post)
        _actionState.value = ActionState.NavigateTDetailLandmark(post)
        _actionState.value = null
    }


    private fun getToken() {
        object : Thread() {
            override fun run() {
                try {
                    val appId: String =
                        AGConnectServicesConfig.fromContext(getApplication())
                            .getString("client/app_id")
                    pushToken = HmsInstanceId.getInstance(getApplication()).getToken(appId, "HCM")

                    if (!TextUtils.isEmpty(pushToken)) {
                        Log.i("PushActivity", "get token: $pushToken")
                        setPushTokenFirst("$pushToken")
                    }
                } catch (e: Exception) {
                    Log.i("PushActivity", "getToken failed, $e")
                }

            }
        }.start()


    }
}