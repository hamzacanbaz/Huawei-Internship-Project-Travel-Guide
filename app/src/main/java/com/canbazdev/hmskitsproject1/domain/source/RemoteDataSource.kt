package com.canbazdev.hmskitsproject1.domain.source

import android.graphics.Bitmap
import android.net.Uri
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.huawei.hms.location.LocationSettingsResponse
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark
import com.huawei.hms.site.api.model.Site
import com.huawei.hms.support.account.service.AccountAuthService

/*
*   Created by hamzacanbaz on 7/22/2022
*/interface RemoteDataSource {
    suspend fun signOutWithHuawei(): Int
    suspend fun signInWithHuawei(): AccountAuthService
    suspend fun verifyEmailAccount(email: String): Int
    suspend fun signUpWithEmail(email: String, password: String, verificationCode: String): String
    suspend fun signInWithEmail(email: String, password: String): String
    suspend fun checkLocationOptions(): LocationSettingsResponse
    suspend fun recognizeLandmark(landmarkImage: Bitmap): List<MLRemoteLandmark>
    suspend fun insertPost(post: Post): Post
    suspend fun uploadPostImageToStorage(uri: Uri): Uri
    suspend fun getAllPostsFromFirebase(): List<Post>
    suspend fun getNearbySites(lat: Double, lng: Double): List<Site>
    suspend fun insertUserToFirebase(userFirebase: UserFirebase): UserFirebase
    suspend fun getPostsByUserId(userId: String): List<Post>


}