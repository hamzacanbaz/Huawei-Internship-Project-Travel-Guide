package com.canbazdev.hmskitsproject1.domain.usecase.posts

import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 8/5/2022
*/
class DeleteLandmarkFromWishListUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {
    operator fun invoke(userId: String, landmarkId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(postsRepository.deleteLandmarkFromWishList(userId, landmarkId)))
        } catch (e: Exception) {
            emit(Resource.Error(errorMessage = e.localizedMessage ?: e.message!!))
        }


    }

}