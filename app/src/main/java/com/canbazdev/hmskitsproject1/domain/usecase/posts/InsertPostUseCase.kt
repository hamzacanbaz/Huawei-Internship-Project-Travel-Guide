package com.canbazdev.hmskitsproject1.domain.usecase.posts

import com.canbazdev.hmskitsproject1.domain.model.Post
import com.canbazdev.hmskitsproject1.domain.repository.LandMarksRepository
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/21/2022
*/
class InsertPostUseCase @Inject constructor(
    private val landMarksRepository: LandMarksRepository
) {
    /* operator fun invoke(): Flow<Resource<Post>> = flow {
         try {
             emit(Resource.Loading())
             val work =  postsRepository.addPostToFirestore("asd", "asdsa")
             work.addOnSuccessListener {
                 println("work success" + it)
             }.addOnFailureListener {
                 println("work failure")
             }


         } catch (e: HttpException) {
             emit(Resource.Error(e.localizedMessage.orEmpty()))
         } catch (e: IOException) {
             emit(Resource.Error(e.localizedMessage.orEmpty()))
         }
     }*/
    operator fun invoke(post: Post): Flow<Resource<Post>> {
        return landMarksRepository.insertUser(post)
    }
}