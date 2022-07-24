package com.canbazdev.hmskitsproject1.presentation.post

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.domain.model.Post
import com.canbazdev.hmskitsproject1.domain.usecase.posts.InsertPostUseCase
import com.canbazdev.hmskitsproject1.util.Constants
import com.canbazdev.hmskitsproject1.util.Resource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.LocationSettingsRequest
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/19/2022
*/
@HiltViewModel
class PostViewModel @Inject constructor(
    application: Application,
    private val insertPostUseCase: InsertPostUseCase
) : AndroidViewModel(application) {

    private val _postText = MutableStateFlow("")
    val postText: StateFlow<String> = _postText

    private val _postImage = MutableStateFlow(Uri.EMPTY)
    val postImage: StateFlow<Uri> = _postImage

    private val _uiState = MutableStateFlow(0)
    val uiState: StateFlow<Int> = _uiState


    private val settingsClient by lazy { LocationServices.getSettingsClient(application) }

    init {
        checkLocationOptions()
        viewModelScope.launch {
            insertPostUseCase.invoke(Post(32,"esmanur canbaz","hamza")).collect {
                println("vm collect")
                when (it) {
                    is Resource.Loading -> {
                        println("vm Loading")
                    }
                    is Resource.Error -> {
                        println("vm Error + ${it.errorMessage}")
                    }
                    is Resource.Success -> {
                        println("vm successsss"+it.data)
                    }
                }
            }
        }

        Firebase.firestore.collection("posts").document("a").set(Post(1, "asd", "sadsad"))
            .addOnSuccessListener {
                println(it)
            }.addOnFailureListener {
            println(it)
        }

    }

    fun checkLocationOptions() {

        val locationSettingsRequest =
            LocationSettingsRequest.Builder().addLocationRequest(LocationRequest()).build()
        // Check the device location settings.
        settingsClient.checkLocationSettings(locationSettingsRequest)
            // Define the listener for success in calling the API for checking device location settings.
            .addOnSuccessListener { locationSettingsResponse ->
                val locationSettingsStates = locationSettingsResponse.locationSettingsStates
                val stringBuilder = StringBuilder()
                stringBuilder.append("isLocationUsable=")
                    .append(locationSettingsStates.isLocationUsable)
                stringBuilder.append(",\nisHMSLocationUsable=")
                    .append(locationSettingsStates.isHMSLocationUsable)
                _uiState.value = 1
                Log.i(Constants.TAG, "checkLocationSetting onComplete:$stringBuilder")
            }
            // Define callback for failure in checking the device location settings.
            .addOnFailureListener { e ->
                _uiState.value = -1

                Log.i(Constants.TAG, "checkLocationSetting onFailure:")
            }
    }


    fun imageToText(imageUri: Uri) {
        var bitmap: Bitmap? = null
        val contentResolver: ContentResolver = getApplication<Application>().contentResolver
        try {
            val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, imageUri)
            bitmap = ImageDecoder.decodeBitmap(source)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val analyzer = MLAnalyzerFactory.getInstance().localTextAnalyzer
        val frame = MLFrame.fromBitmap(bitmap)
        val task = analyzer.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
            _postText.value = it.stringValue.toString().trim()

        }.addOnFailureListener {
            println(it.localizedMessage)
        }

        try {
            analyzer?.stop()
        } catch (e: IOException) {
            // Exception handling.
        }
    }

    fun setPostImage(imageUri: Uri) {
        _postImage.value = imageUri
    }


}