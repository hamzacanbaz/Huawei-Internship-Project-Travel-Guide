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
    fun getAllPostsFromFirebase(): Work<List<Post>>

}