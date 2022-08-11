package com.canbazdev.hmskitsproject1.domain.repository

import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.huawei.hms.support.account.service.AccountAuthService

/*
*   Created by hamzacanbaz on 7/22/2022
*/interface LoginRepository {
    suspend fun signInWithHuawei(): AccountAuthService
    suspend fun signOut(): Int
    suspend fun verifyEmailAccount(email: String): Int
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        verificationCode: String
    ): String

    suspend fun signInWithEmail(email: String, password: String): String
    suspend fun insertUserToFirebase(user: UserFirebase): UserFirebase
    fun silentSignIn(
        onSuccess: (() -> Unit)? = null,
        onFail: ((e: Exception) -> Unit)? = null
    )


}