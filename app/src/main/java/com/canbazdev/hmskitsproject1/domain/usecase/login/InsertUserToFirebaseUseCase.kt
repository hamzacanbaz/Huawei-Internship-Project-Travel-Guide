package com.canbazdev.hmskitsproject1.domain.usecase.login

import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.domain.repository.LoginRepository
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/29/2022
*/
class InsertUserToFirebaseUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(userFirebase: UserFirebase): Flow<Resource<UserFirebase>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(loginRepository.insertUserToFirebase(userFirebase)))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: e.message.toString()))
            }
        }
}