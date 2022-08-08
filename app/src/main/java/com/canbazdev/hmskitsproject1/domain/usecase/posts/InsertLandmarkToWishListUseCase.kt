package com.canbazdev.hmskitsproject1.domain.usecase.posts

import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 8/4/2022
*/
class InsertLandmarkToWishListUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {
    operator fun invoke(id: String, post: Post): Flow<Resource<Post>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(postsRepository.uploadLandmarkToWishList(id, post)))
        } catch (e: Exception) {
            emit(Resource.Error(errorMessage = e.localizedMessage ?: e.message!!))
        }


    }

}