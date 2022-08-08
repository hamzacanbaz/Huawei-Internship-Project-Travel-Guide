package com.canbazdev.hmskitsproject1.data.repository

import android.net.Uri
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*
*   Created by hamzacanbaz on 7/21/2022
*/
class PostsRepositoryImpl @Inject constructor(
    private val firebase: FirebaseFirestore,
    private val remoteDataSource: RemoteDataSource

) : PostsRepository {
    override suspend fun addPostToFirestore(post: Post): Post {
        return suspendCoroutine { continuation ->
            remoteDataSource.insertPost(post)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    }

    override suspend fun uploadPostImageToStorage(uri: Uri): Uri {
        return suspendCoroutine { continuation ->
            remoteDataSource.uploadPostImageToStorage(uri)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    }

    override suspend fun uploadLandmarkQrCodeToStorage(uri: Uri, pathId: String): Uri {
        return suspendCoroutine { continuation ->
            println("uri -> $uri pathId -> $pathId")
            remoteDataSource.uploadLandmarkQrCodeToStorage(uri, pathId)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    }

    override suspend fun getAllPostsFromFirebase(): List<Post> {
        return suspendCoroutine { continuation ->
            remoteDataSource.getAllPostsFromFirebase()
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    }

    override suspend fun getPostsByUserId(userId: String): List<Post> {
        return suspendCoroutine { continuation ->
            remoteDataSource.getPostsByUserId(userId).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun getLandmarkWithId(id: String): Post {
        return suspendCoroutine { continuation ->
            remoteDataSource.getLandmarkWithId(id).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun uploadLandmarkToWishList(id: String, post: Post): Post {
        return suspendCoroutine { continuation ->
            remoteDataSource.insertLandmarkToWishList(id, post).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }

    }

    override suspend fun getAllWishListFromFirebase(id: String): List<Post> {
        return suspendCoroutine { continuation ->
            remoteDataSource.getAllWishListFromFirebase(id).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }


    }

    override suspend fun deleteLandmarkFromWishList(userId: String, landmarkId: String): String {
        return suspendCoroutine { continuation ->
            remoteDataSource.deleteLandmarkFromWishList(userId, landmarkId).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }


    /* private fun callUserEnd(
         work: Work<Post>,
         post: Post
     ) {
         postsRef.document().set(post)
             .addOnSuccessListener {
                 work.onSuccess(post)
             }.addOnFailureListener {
                 work.onFailure(
                     it
                 )
             }
     }*/
}


