package com.canbazdev.hmskitsproject1.domain.usecase.login

import com.canbazdev.hmskitsproject1.domain.repository.LoginRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/24/2022
*/
class SignInWithEmailUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<Resource<String>> = flow {
        println("email $email password $password")
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(
                    loginRepository.signInWithEmail(
                        email,
                        password
                    )
                )
            )
        } catch (e: Exception) {
            println(e)
            emit(Resource.Error(e.localizedMessage ?: e.message.toString()))
        }
    }
}