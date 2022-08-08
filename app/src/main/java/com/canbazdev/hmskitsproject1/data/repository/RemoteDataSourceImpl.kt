package com.canbazdev.hmskitsproject1.data.repository

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.util.SparseArray
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.canbazdev.hmskitsproject1.util.Work
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.EmailAuthProvider
import com.huawei.agconnect.auth.EmailUser
import com.huawei.agconnect.auth.VerifyCodeSettings
import com.huawei.hms.kit.awareness.Awareness
import com.huawei.hms.kit.awareness.barrier.TimeBarrier
import com.huawei.hms.kit.awareness.capture.TimeCategoriesResponse
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.LocationSettingsRequest
import com.huawei.hms.location.LocationSettingsResponse
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.model.*
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService
import java.io.IOException
import java.util.*
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/22/2022
*/
class RemoteDataSourceImpl @Inject constructor(
    private val postsRef: CollectionReference,
    private val searchService: SearchService,
    private val application: Application,
    private val firebase: FirebaseFirestore,
    private val context: Context

) :
    RemoteDataSource {

    override fun signOutWithHuawei(): Work<Any> {
        val work = Work<Any>()
        val authParams: AccountAuthParams =
            AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams()
        val service: AccountAuthService = AccountAuthManager.getService(context, authParams)
        val task = service.signOut()
        task.addOnSuccessListener {
            work.onSuccess(true)
        }
        task.addOnFailureListener {
            work.onFailure(it)
        }
        return work
    }

    override fun signInWithHuawei(): AccountAuthService {
        val work = Work<AccountAuthService>()
        val authParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .setUid()
            .setId()
            .setIdToken()
            .createParams()
        val x = AccountAuthManager.getService(context, authParam)
        x.silentSignIn().addOnSuccessListener {
            println(it.idToken)
        }
        work.onSuccess(x)

        return AccountAuthManager.getService(context, authParam)

    }

    override fun verifyEmailAccount(email: String): Work<Any> {

        val work = Work<Any>()
        val settings = VerifyCodeSettings.newBuilder()
            .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
            .sendInterval(30)
            .locale(Locale.GERMANY)
            .build()
        val task = AGConnectAuth.getInstance().requestVerifyCode(email, settings)
        task.addOnSuccessListener {
            work.onSuccess(it)
        }.addOnFailureListener {
            work.onFailure(it)
        }
        return work
    }

    override fun signUpWithEmail(
        email: String,
        password: String,
        verificationCode: String
    ): Work<String> {
        val work = Work<String>()
        val emailUser = EmailUser.Builder()
            .setEmail(email)
            .setVerifyCode(verificationCode)
            .setPassword(password)
            .build()
        AGConnectAuth.getInstance().createUser(emailUser).addOnSuccessListener {
            work.onSuccess(it.user.uid)
        }.addOnFailureListener {
            work.onFailure(it)
        }
        return work

    }

    override fun signInWithEmail(email: String, password: String): Work<String> {
        val work = Work<String>()
        val credential = EmailAuthProvider.credentialWithPassword(email, password)
        AGConnectAuth.getInstance().signIn(credential).addOnSuccessListener {
            work.onSuccess(it.user.uid)
        }.addOnFailureListener {
            work.onFailure(it)
        }
        return work
    }

    override fun insertUserToFirebase(userFirebase: UserFirebase): Work<UserFirebase> {

        val work = Work<UserFirebase>()
        val task = firebase.collection("users").document(userFirebase.id).set(userFirebase)
        val task2 =
            firebase.collection("users").document(userFirebase.id).collection("wishList").add("")

        firebase.collection("users").get().addOnSuccessListener {
            if (!it.isEmpty) {
                val documents = it.documents
                var isUserExist = false
                for (doc in documents) {
                    val id = doc.data?.get("id")?.let { p -> p as String }
                    val email = doc.data?.get("email")?.let { p -> p as String }
                    if (email?.equals(userFirebase.email) == true && id?.equals(userFirebase.id) == true) {
                        isUserExist = true
                    }
                }
                if (!isUserExist) {
                    task.addOnSuccessListener {
                        task2.addOnSuccessListener {
                            work.onSuccess(userFirebase)

                        }

                    }.addOnFailureListener { e ->
                        work.onFailure(e)

                    }
                }


            }
        }
        return work

    }

    override fun insertPost(post: Post): Work<Post> {
        val work = Work<Post>()
        println(postsRef)
        postsRef.document().set(post).addOnSuccessListener {
            work.onSuccess(post)

        }.addOnFailureListener {
            work.onFailure(it)

        }
        return work
    }


    override fun uploadPostImageToStorage(uri: Uri): Work<Uri> {
        val work = Work<Uri>()
        val storage = FirebaseStorage.getInstance()
        val location = "images/" + UUID.randomUUID().toString()
        storage.getReference(location)
            .putFile(uri)
            .addOnSuccessListener {
                storage.getReference(location).downloadUrl.addOnSuccessListener {
                    work.onSuccess(it)
                }
            }.addOnFailureListener { e ->
                work.onFailure(e)
            }
        return work
    }


    override fun uploadLandmarkQrCodeToStorage(uri: Uri, pathId: String): Work<Uri> {
        val work = Work<Uri>()
        val storage = FirebaseStorage.getInstance()
        val location = "qrCodes/$pathId"
        storage.getReference(location)
            .putFile(uri)
            .addOnSuccessListener {
                storage.getReference(location).downloadUrl.addOnSuccessListener {
                    work.onSuccess(it)
                }
            }.addOnFailureListener { e ->
                work.onFailure(e)
            }
        return work
    }

    override fun getAllPostsFromFirebase(): Work<List<Post>> {
        val work = Work<List<Post>>()
        postsRef.get().addOnSuccessListener {
            if (!it.isEmpty) {
                val documents = it.documents
                val postsList = ArrayList<Post>()
                documents.forEach { d ->
                    postsList.add(
                        Post(
                            landmarkImage = d.data?.get("landmarkImage")?.let { p -> p as String },
                            landmarkInfo = d.data?.get("landmarkInfo")?.let { p -> p as String },
                            landmarkLocation = d.data?.get("landmarkLocation")
                                ?.let { p -> p as String },
                            landmarkLatitude = d.data?.get("landmarkLatitude")
                                ?.let { p -> p as Double },
                            landmarkLongitude = d.data?.get("landmarkLongitude")
                                ?.let { p -> p as Double },
                            landmarkName = d.data?.get("landmarkName")?.let { p -> p as String },
                            authorId = d.data?.get("authorId")?.let { p -> p as String },
                            id = d.data?.get("id")?.let { p -> p as String },
                            qrUrl = d.data?.get("qrUrl")?.let { qr -> qr as String }

                        )
                    )
                    println(d.data?.get("landmarkImage"))
                }
                work.onSuccess(postsList)
            }
        }.addOnFailureListener { work.onFailure(it) }
        return work
    }

    override fun getNearbySites(lat: Double, lng: Double): Work<List<Site>> {
        val work = Work<List<Site>>()
        // Instantiate the SearchService object.


// Create a request body.
        val request = NearbySearchRequest()
        val location = Coordinate(lat, lng)
        request.apply {
            this.location = location
            hwPoiType = HwLocationType.RESTAURANT
            radius = 1000
            pageSize = 20
            pageIndex = 1
        }
// Create a search result listener.
        val resultListener: SearchResultListener<NearbySearchResponse> =
            object : SearchResultListener<NearbySearchResponse> {
                // Return search results upon a successful search.
                override fun onSearchResult(results: NearbySearchResponse?) {
                    if (results == null || results.totalCount <= 0) {
                        return
                    }
                    val sites: List<Site>? = results.sites
                    if (sites == null || sites.isEmpty()) {
                        return
                    }
                    for (site in sites) {
                        Log.i("TAG", "siteId: ${site.siteId}, name: ${site.name}")
                    }
                    work.onSuccess(sites)
                }

                // Return the result code and description upon a search exception.
                override fun onSearchError(status: SearchStatus) {
                    work.onFailure(Exception(status.errorMessage))
                    Log.i("TAG", "Error : ${status.errorCode}  ${status.errorMessage}")
                }
            }
// Call the nearby place search API.
        searchService.nearbySearch(request, resultListener)
        return work

    }


    override fun getPostsByUserId(userId: String): Work<List<Post>> {
        val work = Work<List<Post>>()
        postsRef.get().addOnSuccessListener {
            if (!it.isEmpty) {
                val documents = it.documents
                val postsList = ArrayList<Post>()
                documents.forEach { d ->
                    if (d.data?.get("authorId")?.equals(userId) == true) {
                        postsList.add(
                            Post(
                                landmarkImage = d.data?.get("landmarkImage")
                                    ?.let { p -> p as String },
                                landmarkInfo = d.data?.get("landmarkInfo")
                                    ?.let { p -> p as String },
                                landmarkLocation = d.data?.get("landmarkLocation")
                                    ?.let { p -> p as String },
                                landmarkLatitude = d.data?.get("landmarkLatitude")
                                    ?.let { p -> p as Double },
                                landmarkLongitude = d.data?.get("landmarkLongitude")
                                    ?.let { p -> p as Double },
                                landmarkName = d.data?.get("landmarkName")
                                    ?.let { p -> p as String },
                                authorId = d.data?.get("authorId")?.let { p -> p as String },
                                id = d.data?.get("id")?.let { p -> p as String },
                                qrUrl = d.data?.get("qrUrl")?.let { qr -> qr as String }

                            )
                        )

                    }
                    println(d.data?.get("landmarkImage"))
                }
                work.onSuccess(postsList)
            }

        }
            .addOnFailureListener {
                work.onFailure(it)
            }
        return work

    }

    override fun getLandmarkWithId(id: String): Work<Post> {
        val work = Work<Post>()
        postsRef.get().addOnSuccessListener {
            if (!it.isEmpty) {
                val documents = it.documents
                var post = Post()
                documents.forEach { d ->
                    if (d.data?.get("id")?.equals(id) == true) {
                        post = Post(
                            landmarkImage = d.data?.get("landmarkImage")?.let { p -> p as String },
                            landmarkInfo = d.data?.get("landmarkInfo")?.let { p -> p as String },
                            landmarkLocation = d.data?.get("landmarkLocation")
                                ?.let { p -> p as String },
                            landmarkLatitude = d.data?.get("landmarkLatitude")
                                ?.let { p -> p as Double },
                            landmarkLongitude = d.data?.get("landmarkLongitude")
                                ?.let { p -> p as Double },
                            landmarkName = d.data?.get("landmarkName")?.let { p -> p as String },
                            authorId = d.data?.get("authorId")?.let { p -> p as String },
                            id = d.data?.get("id")?.let { p -> p as String },
                            qrUrl = d.data?.get("qrUrl")?.let { qr -> qr as String }
                        )
                    }
                }
                work.onSuccess(post)
            }

        }
            .addOnFailureListener {
                work.onFailure(it)
            }
        return work

    }

    override fun insertLandmarkToWishList(id: String, post: Post): Work<Post> {
        val work = Work<Post>()
        firebase.collection("users").document(id).collection("wishList")
            .document(post.id ?: UUID.randomUUID().toString()).set(post)
            .addOnSuccessListener {
                work.onSuccess(post)

            }.addOnFailureListener {
                work.onFailure(it)

            }
        return work
    }

    override fun getAllWishListFromFirebase(id: String): Work<List<Post>> {
        val work = Work<List<Post>>()
        firebase.collection("users").document(id).collection("wishList").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    val documents = it.documents
                    val postsList = ArrayList<Post>()
                    documents.forEach { d ->
                        postsList.add(
                            Post(
                                landmarkImage = d.data?.get("landmarkImage")
                                    ?.let { p -> p as String },
                                landmarkInfo = d.data?.get("landmarkInfo")
                                    ?.let { p -> p as String },
                                landmarkLocation = d.data?.get("landmarkLocation")
                                    ?.let { p -> p as String },
                                landmarkLatitude = d.data?.get("landmarkLatitude")
                                    ?.let { p -> p as Double },
                                landmarkLongitude = d.data?.get("landmarkLongitude")
                                    ?.let { p -> p as Double },
                                landmarkName = d.data?.get("landmarkName")
                                    ?.let { p -> p as String },
                                authorId = d.data?.get("authorId")?.let { p -> p as String },
                                id = d.data?.get("id")?.let { p -> p as String },
                                qrUrl = d.data?.get("qrUrl")?.let { qr -> qr as String }

                            )
                        )


                        println(d.data?.get("landmarkImage"))
                    }
                    work.onSuccess(postsList)
                } else {
                    work.onSuccess(listOf())
                }

            }
            .addOnFailureListener {
                work.onFailure(it)
            }
        return work
    }

    override fun deleteLandmarkFromWishList(userId: String, landmarkId: String): Work<String> {
        val work = Work<String>()
        firebase.collection("users").document(userId).collection("wishList").document(landmarkId)
            .delete()
            .addOnSuccessListener {
                work.onSuccess(landmarkId)

            }.addOnFailureListener {
                work.onFailure(it)

            }
        return work
    }

    override fun getTimeOfDay(context: Context): Work<String> {
        val work = Work<String>()
        val TIME_DESCRIPTION_MAP = SparseArray<String>()
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_WEEKDAY, "Today is weekday.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_WEEKEND, "Today is weekend.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_HOLIDAY, "Today is holiday.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_NOT_HOLIDAY, "Today is not holiday.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_MORNING, "Good morning.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_AFTERNOON, "Good afternoon.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_EVENING, "Good evening.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_NIGHT, "Good night.")

        Awareness.getCaptureClient(context).timeCategories
            // Callback listener for execution success.
            .addOnSuccessListener { timeCategoriesResponse: TimeCategoriesResponse ->
                val categories = timeCategoriesResponse.timeCategories
                val timeInfo = categories.timeCategories
                for (timeCode in timeInfo) {
                    if (TIME_DESCRIPTION_MAP.get(timeCode).contains("Good")) {
                        work.onSuccess(TIME_DESCRIPTION_MAP.get(timeCode).removeSuffix(".") + "!")
                    }
                }
            }
            .addOnFailureListener { e: Exception? ->
                work.onFailure(e ?: java.lang.Exception(e?.localizedMessage))
            }
        return work


    }


    override fun checkLocationOptions(): Work<LocationSettingsResponse> {
        val work = Work<LocationSettingsResponse>()
        val locationSettingsRequest =
            LocationSettingsRequest.Builder().addLocationRequest(LocationRequest()).build()
        // Check the device location settings.
        val task = LocationServices.getSettingsClient(application)
            .checkLocationSettings(locationSettingsRequest)
        // Define the listener for success in calling the API for checking device location settings.
        task.addOnSuccessListener { locationSettingsResponse ->
            work.onSuccess(locationSettingsResponse)
        }
        // Define callback for failure in checking the device location settings.
        task.addOnFailureListener { e ->
            work.onFailure(e)
        }
        return work

    }

    override fun recognizeLandmark(landmarkImage: Bitmap): Work<List<MLRemoteLandmark>> {

        val work = Work<List<MLRemoteLandmark>>()
        val analyzer = MLAnalyzerFactory.getInstance().remoteLandmarkAnalyzer
        val mlFrame = MLFrame.Creator().setBitmap(landmarkImage).create()
        val task = analyzer!!.asyncAnalyseFrame(mlFrame)
        task.addOnSuccessListener {
            work.onSuccess(it)
        }
        task.addOnFailureListener {
            work.onFailure(it)
        }
        try {
            analyzer.stop()
        } catch (e: IOException) {
            println("analyzer stop error ${e.localizedMessage}")
        }
        return work
    }


}