package com.canbazdev.hmskitsproject1.data.repository

import android.content.Context
import com.canbazdev.hmskitsproject1.domain.repository.ProfileRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import javax.inject.Inject
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*
*   Created by hamzacanbaz on 8/5/2022
*/
class ProfileRepositoryImpl @Inject constructor(
    private val context: Context,
    private val remoteDataSource: RemoteDataSource
) : ProfileRepository {
    override suspend fun getTimeOfDay(): String {
        return suspendCoroutine { continuation ->
            remoteDataSource.getTimeOfDay(context).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }
}