package com.canbazdev.hmskitsproject1.data.repository

import android.net.Uri
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.util.Work
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import java.util.*
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

    override fun getAllPostsFromFirebase(): Work<List<Post>> {
        val work = Work<List<Post>>()
        postsRef.get().addOnSuccessListener {
            if (!it.isEmpty) {
                val documents = it.documents
                val postsList = ArrayList<Post>()
                documents.forEach { d ->
                    postsList.add(
                        Post(
                            landmarkImage = d.data?.get("landmarkImage") as String,
                            landmarkInfo = d.data?.get("landmarkInfo") as String,
                            landmarkLocation = d.data?.get("landmarkLocation") as String,
                            landmarkLatitude = d.data?.get("landmarkLatitude") as Double,
                            landmarkLongitude = d.data?.get("landmarkLongitude") as Double,
                            landmarkName = d.data?.get("landmarkName") as String,
                        )
                    )
                    println(d.data?.get("landmarkImage"))
                }
                work.onSuccess(postsList)
            }
        }
        return work
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


