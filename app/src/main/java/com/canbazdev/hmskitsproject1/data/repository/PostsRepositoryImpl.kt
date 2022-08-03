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

    override fun uploadLandmarkQrCodeToStorage(uri: Uri, pathId: String): Work<Uri> {
        val work = Work<Uri>()
        val storage = FirebaseStorage.getInstance()
        val location = "qrCodes/$pathId"
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
                            landmarkImage = d.data?.get("landmarkImage")?.let { p -> p as String },
                            landmarkInfo = d.data?.get("landmarkInfo")?.let { p -> p as String },
                            landmarkLocation = d.data?.get("landmarkLocation")
                                ?.let { p -> p as String },
                            landmarkLatitude = d.data?.get("landmarkLatitude")
                                ?.let { p -> p as Double },
                            landmarkLongitude = d.data?.get("landmarkLongitude")
                                ?.let { p -> p as Double },
                            landmarkName = d.data?.get("landmarkName")?.let { p -> p as String },
                            authorId = d.data?.get("authorId")?.let { p -> p as String },
                            id = d.data?.get("id")?.let { p -> p as String },
                            qrUrl = d.data?.get("qrUrl")?.let { qr -> qr as String }

                        )
                    )
                    println(d.data?.get("landmarkImage"))
                }
                work.onSuccess(postsList)
            }
        }.addOnFailureListener { work.onFailure(it) }
        return work
    }

    override fun getPostsByUserId(userId: String): Work<List<Post>> {
        val work = Work<List<Post>>()
        postsRef.get().addOnSuccessListener {
            if (!it.isEmpty) {
                val documents = it.documents
                val postsList = ArrayList<Post>()
                documents.forEach { d ->
                    if (d.data?.get("authorId")?.equals(userId) == true) {
                        postsList.add(
                            Post(
                                landmarkImage = d.data?.get("landmarkImage")
                                    ?.let { p -> p as String },
                                landmarkInfo = d.data?.get("landmarkInfo")
                                    ?.let { p -> p as String },
                                landmarkLocation = d.data?.get("landmarkLocation")
                                    ?.let { p -> p as String },
                                landmarkLatitude = d.data?.get("landmarkLatitude")
                                    ?.let { p -> p as Double },
                                landmarkLongitude = d.data?.get("landmarkLongitude")
                                    ?.let { p -> p as Double },
                                landmarkName = d.data?.get("landmarkName")
                                    ?.let { p -> p as String },
                                authorId = d.data?.get("authorId")?.let { p -> p as String },
                                id = d.data?.get("id")?.let { p -> p as String },
                                qrUrl = d.data?.get("qrUrl")?.let { qr -> qr as String }

                            )
                        )

                    }
                    println(d.data?.get("landmarkImage"))
                }
                work.onSuccess(postsList)
            }

        }
            .addOnFailureListener {
                work.onFailure(it)
            }
        return work
    }

    override fun getLandmarkWithId(id: String): Work<Post> {
        val work = Work<Post>()
        postsRef.get().addOnSuccessListener {
            if (!it.isEmpty) {
                val documents = it.documents
                var post = Post()
                documents.forEach { d ->
                    if (d.data?.get("id")?.equals(id) == true) {
                        post = Post(
                            landmarkImage = d.data?.get("landmarkImage")?.let { p -> p as String },
                            landmarkInfo = d.data?.get("landmarkInfo")?.let { p -> p as String },
                            landmarkLocation = d.data?.get("landmarkLocation")
                                ?.let { p -> p as String },
                            landmarkLatitude = d.data?.get("landmarkLatitude")
                                ?.let { p -> p as Double },
                            landmarkLongitude = d.data?.get("landmarkLongitude")
                                ?.let { p -> p as Double },
                            landmarkName = d.data?.get("landmarkName")?.let { p -> p as String },
                            authorId = d.data?.get("authorId")?.let { p -> p as String },
                            id = d.data?.get("id")?.let { p -> p as String },
                            qrUrl = d.data?.get("qrUrl")?.let { qr -> qr as String }
                        )
                    }
                }
                work.onSuccess(post)
            }

        }
            .addOnFailureListener {
                work.onFailure(it)
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


