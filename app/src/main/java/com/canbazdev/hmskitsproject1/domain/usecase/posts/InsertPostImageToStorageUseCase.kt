package com.canbazdev.hmskitsproject1.domain.usecase.posts

import android.net.Uri
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/21/2022
*/
class InsertPostImageToStorageUseCase @Inject constructor(
    private val userDataSource: RemoteDataSource
) {

    suspend operator fun invoke(uri: Uri): Flow<Resource<Uri>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(userDataSource.uploadPostImageToStorage(uri)))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: e.message.toString()))
            }
        }
}