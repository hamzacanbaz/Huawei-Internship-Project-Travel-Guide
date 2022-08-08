package com.canbazdev.hmskitsproject1.data.repository

import android.content.Context
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.domain.repository.LoginRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.canbazdev.hmskitsproject1.util.Work
import com.google.firebase.firestore.FirebaseFirestore
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import com.huawei.hms.support.account.service.AccountAuthService
import javax.inject.Inject
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*
*   Created by hamzacanbaz on 7/22/2022
*/
class LoginRepositoryImpl @Inject constructor(
    private val context: Context,
    private val remoteDataSource: RemoteDataSource
) : LoginRepository {

    override suspend fun signInWithHuawei(): AccountAuthService {
        return suspendCoroutine { continuation ->
            continuation.resumeWith(Result.success(remoteDataSource.signInWithHuawei()))
        }
    }

    /* override fun signInWithHuawei(): AccountAuthService {
         println("xxxx")
         val authParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
             .setIdToken()
             .createParams()
         return AccountAuthManager.getService(context, authParam)
     }*/

    override fun silentSignIn(
        onSuccess: (() -> Unit)?,
        onFail: ((e: Exception) -> Unit)?
    ) {
        val authParams: AccountAuthParams =
            AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setIdToken().createParams()
        val service: AccountAuthService = AccountAuthManager.getService(context, authParams)
        val task: Task<AuthAccount> = service.silentSignIn()
        task.addOnSuccessListener {
            println(it.idToken)
            onSuccess?.invoke()
        }
        task.addOnFailureListener { e ->
            if (e is ApiException) {
                println(e)
                onFail?.invoke(e)
            }

        }
    }

    override suspend fun signOut(): Int {

        return suspendCoroutine { continuation ->
            remoteDataSource.signOutWithHuawei()
                .addOnSuccessListener {
                    println("yar")
                    continuation.resumeWith(Result.success(1))
                }.addOnFailureListener {
                    println("dım")
                    continuation.resumeWithException(it)
                }
        }

    }

    override suspend fun verifyEmailAccount(email: String): Int {
        return suspendCoroutine { continuation ->
            remoteDataSource.verifyEmailAccount(email).addOnSuccessListener {
                continuation.resumeWith(Result.success(1))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }

    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        verificationCode: String
    ): String {

        return suspendCoroutine { continuation ->
            remoteDataSource.signUpWithEmail(email, password, verificationCode)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): String {
        return suspendCoroutine { continuation ->
            remoteDataSource.signInWithEmail(email, password).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }

    }

    override suspend fun insertUserToFirebase(user: UserFirebase): UserFirebase {
        return suspendCoroutine { continuation ->
            remoteDataSource.insertUserToFirebase(user)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    /*  override suspend fun signOut(onSuccess: (() -> Unit)?, onFail: ((e: Exception) -> Unit)?) {
        val authParams: AccountAuthParams =
            AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams()
        val service: AccountAuthService = AccountAuthManager.getService(context, authParams)
        val task = service.signOut()
        task.addOnSuccessListener {
            onSuccess?.invoke()
        }
        task.addOnFailureListener { e ->
            if (e is ApiException) {
                println(e)
                onFail?.invoke(e)
            }

        }
    }*/
}