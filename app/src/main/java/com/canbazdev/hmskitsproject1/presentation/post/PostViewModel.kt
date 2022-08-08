package com.canbazdev.hmskitsproject1.presentation.post

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.location.Geocoder
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.usecase.location.CheckLocationOptionsUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.location.RecognizeLandmarkUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.notification.SendNotificationUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.InsertLandmarkQrCodeToStorageUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.InsertPostImageToStorageUseCase
import com.canbazdev.hmskitsproject1.domain.usecase.posts.InsertPostUseCase
import com.canbazdev.hmskitsproject1.util.ActionState
import com.canbazdev.hmskitsproject1.util.Constants
import com.canbazdev.hmskitsproject1.util.Resource
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.hmsscankit.WriterException
import com.huawei.hms.ml.scan.HmsScan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject


/*
*   Created by hamzacanbaz on 7/19/2022
*/
@HiltViewModel
class PostViewModel @Inject constructor(
    application: Application,
    private val insertPostUseCase: InsertPostUseCase,
    private val checkLocationOptionsUseCase: CheckLocationOptionsUseCase,
    private val recognizeLandmarkUseCase: RecognizeLandmarkUseCase,
    private val insertPostImageToStorageUseCase: InsertPostImageToStorageUseCase,
    private val sendNotificationUseCase: SendNotificationUseCase,
    private val insertLandmarkQrCodeToStorageUseCase: InsertLandmarkQrCodeToStorageUseCase,
    private val dataStoreRepository: DataStoreRepository

) : AndroidViewModel(application) {


    private val _post = MutableStateFlow(Post())
    val post: StateFlow<Post> = _post

    private val _isImageUploaded = MutableStateFlow(false)
    val isImageUploaded = _isImageUploaded

    private val _postImage = MutableStateFlow(Uri.EMPTY)
    val postImage: StateFlow<Uri> = _postImage

    private val _checkLocationOptions = MutableStateFlow<Resource<Any>>(Resource.Loading())
    val checkLocationOptions: StateFlow<Resource<Any>> = _checkLocationOptions

    private val _actionState = MutableStateFlow<ActionState?>(null)
    val actionState: StateFlow<ActionState?> = _actionState

    private val _uiState = MutableStateFlow(0)
    val uiState: StateFlow<Int> = _uiState

    private val _recognizeState = MutableStateFlow(-1)
    val recognizeState: StateFlow<Int> = _recognizeState

    private val _uploadState = MutableStateFlow(-1)
    val uploadState: StateFlow<Int> = _uploadState

    var pushToken = ""

    private val postId = UUID.randomUUID().toString()

    lateinit var currentUserId: String


    init {
        checkLocationOptions()
        viewModelScope.launch {
            dataStoreRepository.getCurrentUserId.collect {
                currentUserId = it
            }
        }

    }

    fun updateLandmarkName(s: Editable) {
        _post.value = post.value.copy(landmarkName = s.toString())
    }

    fun updateLandmarkLocation(s: Editable) {
        _post.value = post.value.copy(landmarkLocation = s.toString())
    }

    fun updateLandmarkInfo(s: Editable) {
        _post.value = post.value.copy(landmarkInfo = s.toString())
    }

    fun setPostImage(imageUri: Uri) {
        _post.value = post.value.copy(landmarkImage = convertUriToBitmap(imageUri).toString())
        _postImage.value = imageUri
        println(isImageUploaded.value)
        _isImageUploaded.value = true
    }

    // repository'den author id ve author name al bunları ekle

    private fun sharePost() {
        viewModelScope.launch {
            val post = this@PostViewModel.post.value.copy(
                id = postId,
                authorId = currentUserId
            )

            insertPostUseCase.invoke(post).collect { result ->
                println("vm collect")
                when (result) {
                    is Resource.Loading -> {
                        _uploadState.value = 0
                        println("vm Loading")
                    }
                    is Resource.Error -> {
                        _uploadState.value = -2
                        println("vm Error + ${result.errorMessage}")
                    }
                    is Resource.Success -> {
                        _uploadState.value = 1
                        println("vm successsss" + result.data)
                        getToken()
                        _uiState.value = 1
                        _actionState.value = ActionState.NavigateToHome


                    }
                }
            }
        }
    }

    fun uploadPostImageBeforeShare() {
        viewModelScope.launch {
            _uploadState.value = 0

            insertPostImageToStorageUseCase.invoke(postImage.value).collect {
                when (it) {
                    is Resource.Success -> {
                        println("post success")
                        post.value.landmarkImage = it.data.toString()
                        generateQrCode(postId)

                    }
                    is Resource.Error -> {
                        println("post error")
                        generateQrCode(postId)
                    }
                    else -> {}
                }
            }
        }
    }

    // TODO HATA OLURSA HATA MESAJI DONDUR

    fun recognizeLandmark() {
        viewModelScope.launch {
            recognizeLandmarkUseCase.invoke(convertUriToBitmap(postImage.value)).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _recognizeState.value = 0
                    }
                    is Resource.Success -> {
                        println("recognize success ${result.data}")
                        for (landmark in result.data!!) {
                            _post.value = post.value.copy(landmarkName = landmark.landmark)
                            println("${landmark.landmark} -------- ${landmark.landmarkIdentity} --------")
                            for (c in landmark.positionInfos) {
                                convertLatLangToAddress(c.lat, c.lng)
                                _post.value = post.value.copy(landmarkLatitude = c.lat)
                                _post.value = post.value.copy(landmarkLongitude = c.lng)
                            }
                        }
                        _recognizeState.value = 1
                    }
                    is Resource.Error -> {
                        _recognizeState.value = -2
                        println("recognize error ${result.errorMessage}")
                    }
                }
            }
        }
    }

    private fun convertUriToBitmap(imageUri: Uri): Bitmap {
        var bitmap: Bitmap? = null
        val contentResolver: ContentResolver = getApplication<Application>().contentResolver
        try {
            val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, imageUri)
            bitmap = ImageDecoder.decodeBitmap(source)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap!!
    }


    private fun checkLocationOptions() {
        viewModelScope.launch {
            checkLocationOptionsUseCase.invoke().collect {
                when (it) {
                    // TODO SONUCA GORE UI STATE GUNCELLE
                    is Resource.Loading -> _checkLocationOptions.value = Resource.Loading()
                    is Resource.Success -> _checkLocationOptions.value = Resource.Success(true)
                    is Resource.Error -> _checkLocationOptions.value =
                        Resource.Error(it.errorMessage ?: it.errorMessage.toString())
                }
            }
        }
    }

    private fun convertLatLangToAddress(latitude: Double, longitude: Double) {
        val geocode = Geocoder(getApplication(), Locale.getDefault())


        try {
            val addresses = geocode.getFromLocation(
                latitude, longitude,
                1
            )
            if (addresses != null && addresses.size > 0) {

                Log.i(Constants.TAG, "addresses=" + addresses.size)
                val address = addresses[0]
                val landmarkAddress =
                    address.thoroughfare + ", " + address.postalCode + " " + address.subAdminArea + "/" + address.adminArea + ", " + address.countryName
                println(landmarkAddress)
                _post.value = post.value.copy(
                    landmarkLocation = landmarkAddress.replace("null", "").trim()
                )

            }
        } catch (e: IOException) {
            Log.e(Constants.TAG, "reverseGeocode wrong ")
        }

    }

    fun setPushTokenFirst(token: String) {
        pushToken = token
        println("PUSH $pushToken")
        sendNotificationUseCase.invoke(pushToken)
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

    private fun generateQrCode(content: String) {
        println("generate qr code works")
        val type = HmsScan.QRCODE_SCAN_TYPE
        val width = 400
        val height = 400
        try {
            // If the HmsBuildBitmapOption object is not constructed, set options to null.
            val qrBitmap = ScanUtil.buildBitmap(content, type, width, height, null)
            getImageUri(getApplication(), qrBitmap)

        } catch (e: WriterException) {
            Log.w("buildBitmap", e)
        }

    }

    private fun getImageUri(inContext: Context, inImage: Bitmap) {
        println("get Image Uri  works")
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        uploadLandmarkQrCodeToStorage(Uri.parse(path))
    }

    private fun uploadLandmarkQrCodeToStorage(uri: Uri) {
        viewModelScope.launch {
            println("upload qr code works")
            insertLandmarkQrCodeToStorageUseCase.invoke(uri, postId)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> println("qr loading")
                        is Resource.Success -> {
                            post.value.qrUrl = result.data.toString()
                            println("qr success")
                            sharePost()
                        }
                        is Resource.Error -> println("qr error ${result.errorMessage}")
                    }
                }

        }
    }


    /* fun imageToText(imageUri: Uri) {
     val bitmap = convertUriToBitmap(imageUri)
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
 }*/


}