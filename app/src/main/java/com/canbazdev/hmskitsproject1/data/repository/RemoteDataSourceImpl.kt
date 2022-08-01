package com.canbazdev.hmskitsproject1.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.domain.repository.LocationRepository
import com.canbazdev.hmskitsproject1.domain.repository.LoginRepository
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.huawei.hms.location.LocationSettingsResponse
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark
import com.huawei.hms.site.api.model.Site
import com.huawei.hms.support.account.service.AccountAuthService
import javax.inject.Inject
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*
*   Created by hamzacanbaz on 7/22/2022
*/
class RemoteDataSourceImpl @Inject constructor(
    private val postsRepository: PostsRepository,
    private val loginRepository: LoginRepository,
    private val locationRepository: LocationRepository
) :
    RemoteDataSource {
    override suspend fun insertPost(post: Post): Post {
        return suspendCoroutine { continuation ->
            postsRepository.addPostToFirestore(post)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun signOutWithHuawei(): Int {
        return suspendCoroutine { continuation ->
            loginRepository.signOut()
                .addOnSuccessListener {
                    println("yar")
                    continuation.resumeWith(Result.success(1))
                }.addOnFailureListener {
                    println("dım")
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun signInWithHuawei(): AccountAuthService {
        return suspendCoroutine { continuation ->
            continuation.resumeWith(Result.success(loginRepository.signInWithHuawei()))
        }
    }

    override suspend fun verifyEmailAccount(email: String): Int {
        return suspendCoroutine { continuation ->
            loginRepository.verifyEmailAccount(email).addOnSuccessListener {
                continuation.resumeWith(Result.success(1))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        verificationCode: String
    ): String {
        return suspendCoroutine { continuation ->
            loginRepository.signUpWithEmail(email, password, verificationCode)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): String {
        return suspendCoroutine { continuation ->
            loginRepository.signInWithEmail(email, password).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun uploadPostImageToStorage(uri: Uri): Uri {
        return suspendCoroutine { continuation ->
            postsRepository.uploadPostImageToStorage(uri)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun getAllPostsFromFirebase(): List<Post> {
        return suspendCoroutine { continuation ->
            postsRepository.getAllPostsFromFirebase()
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun getNearbySites(lat: Double, lng: Double): List<Site> {
        return suspendCoroutine { continuation ->
            locationRepository.getNearbySites(lat, lng)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun insertUserToFirebase(userFirebase: UserFirebase): UserFirebase {
        return suspendCoroutine { continuation ->
            loginRepository.insertUserToFirebase(userFirebase)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun getPostsByUserId(userId: String): List<Post> {
        return suspendCoroutine { continuation ->
            postsRepository.getPostsByUserId(userId).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }


    override suspend fun checkLocationOptions(): LocationSettingsResponse {
        return suspendCoroutine { continuation ->
            locationRepository.getLocationOptions()
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun recognizeLandmark(landmarkImage: Bitmap): List<MLRemoteLandmark> {
        return suspendCoroutine { continuation ->
            locationRepository.getRecognizedLandmark(landmarkImage)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }


}