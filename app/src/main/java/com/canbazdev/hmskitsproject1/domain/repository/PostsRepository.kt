package com.canbazdev.hmskitsproject1.domain.repository

import android.net.Uri
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post

/*
*   Created by hamzacanbaz on 7/21/2022
*/
interface PostsRepository {
    suspend fun addPostToFirestore(post: Post): Post
    suspend fun uploadPostImageToStorage(uri: Uri): Uri
    suspend fun uploadLandmarkQrCodeToStorage(uri: Uri, pathId: String): Uri
    suspend fun getAllPostsFromFirebase(): List<Post>
    suspend fun getPostsByUserId(userId: String): List<Post>
    suspend fun getLandmarkWithId(id: String): Post
    suspend fun uploadLandmarkToWishList(id: String, post: Post): Post
    suspend fun getAllWishListFromFirebase(id: String): List<Post>
    suspend fun deleteLandmarkFromWishList(userId: String, landmarkId: String): String

}