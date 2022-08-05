package com.canbazdev.hmskitsproject1.domain.usecase.posts

import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 8/4/2022
*/
class GetWishListFromFirebaseUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {

    suspend operator fun invoke(id: String): Flow<Resource<List<Post>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(postsRepository.getAllWishListFromFirebase(id)))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: e.message.toString()))
            }
        }
}