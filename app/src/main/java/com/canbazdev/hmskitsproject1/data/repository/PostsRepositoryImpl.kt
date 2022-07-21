package com.canbazdev.hmskitsproject1.data.repository

import com.canbazdev.hmskitsproject1.domain.model.Post
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.util.Resource
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/21/2022
*/
class PostsRepositoryImpl @Inject constructor(
    private val postsRef: CollectionReference
) : PostsRepository {
    override suspend fun addPostToFirestore(text: String, image: String): Flow<Resource<Post>> = flow {
        var result: Resource<Post> = Resource.Loading()
        val post = Post(1,"hamza","asd")
        println(postsRef)
        postsRef.document().set(post).addOnCompleteListener {
            println("sadasasdasd")
            result = if (it.isSuccessful){
                println("sadsadsadsadsa")
                Resource.Success(post)
            } else{
                println(it.exception?.localizedMessage)
                Resource.Error(it.exception?.localizedMessage ?: it.exception.toString())
            }
        }
    emit(result)
    }
}