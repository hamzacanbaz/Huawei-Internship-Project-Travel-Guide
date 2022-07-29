package com.canbazdev.hmskitsproject1.domain.usecase.posts

import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/26/2022
*/
class GetAllPostsFromFirebaseUseCase @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend operator fun invoke(): Flow<Resource<List<Post>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(remoteDataSource.getAllPostsFromFirebase()))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: e.message.toString()))
            }
        }
}