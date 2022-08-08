package com.canbazdev.hmskitsproject1.domain.source

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.util.Work
import com.huawei.hms.location.LocationSettingsResponse
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark
import com.huawei.hms.site.api.model.Site
import com.huawei.hms.support.account.service.AccountAuthService

/*
*   Created by hamzacanbaz on 7/22/2022
*/interface RemoteDataSource {
    fun signOutWithHuawei(): Work<Any>
    fun verifyEmailAccount(email: String): Work<Any>
    fun signUpWithEmail(email: String, password: String, verificationCode: String): Work<String>
    fun signInWithEmail(email: String, password: String): Work<String>
    fun insertUserToFirebase(userFirebase: UserFirebase): Work<UserFirebase>
    fun signInWithHuawei(): AccountAuthService

    fun insertPost(post: Post): Work<Post>
    fun uploadPostImageToStorage(uri: Uri): Work<Uri>
    fun uploadLandmarkQrCodeToStorage(uri: Uri, pathId: String): Work<Uri>
    fun getAllPostsFromFirebase(): Work<List<Post>>
    fun getPostsByUserId(userId: String): Work<List<Post>>
    fun getLandmarkWithId(id: String): Work<Post>
    fun insertLandmarkToWishList(id: String, post: Post): Work<Post>
    fun getAllWishListFromFirebase(id: String): Work<List<Post>>
    fun deleteLandmarkFromWishList(userId:String, landmarkId:String): Work<String>

    fun checkLocationOptions(): Work<LocationSettingsResponse>
    fun recognizeLandmark(landmarkImage: Bitmap): Work<List<MLRemoteLandmark>>
    fun getNearbySites(lat: Double, lng: Double): Work<List<Site>>

    fun getTimeOfDay(context: Context): Work<String>


}