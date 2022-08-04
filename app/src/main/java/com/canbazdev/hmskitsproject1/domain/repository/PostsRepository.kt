package com.canbazdev.hmskitsproject1.domain.repository

import android.net.Uri
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.util.Work

/*
*   Created by hamzacanbaz on 7/21/2022
*/
interface PostsRepository {
    fun addPostToFirestore(post: Post): Work<Post>
    fun uploadPostImageToStorage(uri: Uri): Work<Uri>
    fun uploadLandmarkQrCodeToStorage(uri: Uri, pathId: String): Work<Uri>
    fun getAllPostsFromFirebase(): Work<List<Post>>
    fun getPostsByUserId(userId: String): Work<List<Post>>
    fun getLandmarkWithId(id: String): Work<Post>
    fun uploadLandmarkToWishList(id: String, post: Post): Work<Post>
    fun getAllWishListFromFirebase(id: String): Work<List<Post>>


}