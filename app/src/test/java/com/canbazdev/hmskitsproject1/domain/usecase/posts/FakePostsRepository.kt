package com.canbazdev.hmskitsproject1.domain.usecase.posts

import android.net.Uri
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.util.Work

/*
*   Created by hamzacanbaz on 7/28/2022
*/
class FakePostsRepository : PostsRepository {

    private val posts = mutableListOf<Post>()

    override fun addPostToFirestore(post: Post): Work<Post> {
        val work = Work<Post>()
        posts.add(
            Post(
                landmarkName = "Eiffel Towe",
                landmarkLocation = "Paris",
                landmarkInfo = "Tower of tower"
            )
        )
        val post = posts.last()
        work.onSuccess(post)
        return work
    }

    override fun uploadPostImageToStorage(uri: Uri): Work<Uri> {
        TODO("Not yet implemented")
    }

    override fun getAllPostsFromFirebase(): Work<List<Post>> {
        TODO("Not yet implemented")
    }
}