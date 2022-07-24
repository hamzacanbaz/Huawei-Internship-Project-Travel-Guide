package com.canbazdev.hmskitsproject1.data.repository

import com.canbazdev.hmskitsproject1.domain.model.Post
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.util.Resource
import com.canbazdev.hmskitsproject1.util.Work
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/21/2022
*/
class PostsRepositoryImpl @Inject constructor(
    private val postsRef: CollectionReference
) : PostsRepository {
    override fun addPostToFirestore(post: Post): Work<Post> {
        val work = Work<Post>()
            println(postsRef)
            postsRef.document().set(post).addOnSuccessListener {
                println("sadasasdasd")
                println("sadsadsadsadsa" + post)
                callUserEnd(work,post)

            }.addOnFailureListener {
                work.onFailure(it)

            }
            return work
        }

    private fun callUserEnd(
        work: Work<Post>,
        post: Post
    ) {
        postsRef.document("a").set(post)
            .addOnSuccessListener {
                work.onSuccess(post)
            }.addOnFailureListener {
                work.onFailure(
                    it
                )
            }
    }
}


