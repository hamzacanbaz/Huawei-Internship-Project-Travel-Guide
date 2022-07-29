package com.canbazdev.hmskitsproject1.domain.repository

import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.util.Work
import com.huawei.hms.support.account.service.AccountAuthService

/*
*   Created by hamzacanbaz on 7/22/2022
*/interface LoginRepository {
    fun signInWithHuawei(): AccountAuthService
    fun silentSignIn(
        onSuccess: (() -> Unit)? = null,
        onFail: ((e: Exception) -> Unit)? = null
    )

    fun signOut(): Work<Any>
    fun verifyEmailAccount(email: String): Work<Any>
    fun signUpWithEmail(email: String, password: String, verificationCode: String): Work<String>
    fun signInWithEmail(email: String, password: String): Work<String>
    fun insertUserToFirebase(user: UserFirebase): Work<UserFirebase>


}