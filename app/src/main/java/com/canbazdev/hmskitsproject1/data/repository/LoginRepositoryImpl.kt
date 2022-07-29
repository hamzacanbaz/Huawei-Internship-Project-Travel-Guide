package com.canbazdev.hmskitsproject1.data.repository

import android.content.Context
import com.canbazdev.hmskitsproject1.domain.model.login.UserFirebase
import com.canbazdev.hmskitsproject1.domain.repository.LoginRepository
import com.canbazdev.hmskitsproject1.util.Work
import com.google.firebase.firestore.FirebaseFirestore
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.EmailAuthProvider
import com.huawei.agconnect.auth.EmailUser
import com.huawei.agconnect.auth.VerifyCodeSettings
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import com.huawei.hms.support.account.service.AccountAuthService
import java.util.*
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 7/22/2022
*/
class LoginRepositoryImpl @Inject constructor(
    private val context: Context,
    private val firebase: FirebaseFirestore
) : LoginRepository {

    override fun signInWithHuawei(): AccountAuthService {
        val work = Work<AccountAuthService>()
        val authParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setIdToken()
            .createParams()
        work.onSuccess(AccountAuthManager.getService(context, authParam))

        return AccountAuthManager.getService(context, authParam)
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
            AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams()
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

    override fun signOut(): Work<Any> {
        val work = Work<Any>()
        val authParams: AccountAuthParams =
            AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams()
        val service: AccountAuthService = AccountAuthManager.getService(context, authParams)
        val task = service.signOut()
        task.addOnSuccessListener {
            work.onSuccess(true)
        }
        task.addOnFailureListener {
            work.onFailure(it)
        }
        return work
    }

    override fun verifyEmailAccount(email: String): Work<Any> {
        val work = Work<Any>()
        val settings = VerifyCodeSettings.newBuilder()
            .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
            .sendInterval(30)
            .locale(Locale.GERMANY)
            .build()
        val task = AGConnectAuth.getInstance().requestVerifyCode(email, settings)
        task.addOnSuccessListener {
            work.onSuccess(it)
        }.addOnFailureListener {
            work.onFailure(it)
        }
        return work
    }

    override fun signUpWithEmail(
        email: String,
        password: String,
        verificationCode: String
    ): Work<String> {
        val work = Work<String>()
        val emailUser = EmailUser.Builder()
            .setEmail(email)
            .setVerifyCode(verificationCode)
            .setPassword(password)
            .build()
        AGConnectAuth.getInstance().createUser(emailUser).addOnSuccessListener {
            work.onSuccess(it.user.uid)
        }.addOnFailureListener {
            work.onFailure(it)
        }
        return work
    }

    override fun signInWithEmail(email: String, password: String): Work<String> {
        val work = Work<String>()
        val credential = EmailAuthProvider.credentialWithPassword(email, password)
        AGConnectAuth.getInstance().signIn(credential).addOnSuccessListener {
            work.onSuccess(it.user.uid)
        }.addOnFailureListener {
            work.onFailure(it)
        }
        return work
    }

    override fun insertUserToFirebase(user: UserFirebase): Work<UserFirebase> {
        val work = Work<UserFirebase>()
        val task = firebase.collection("users").document().set(user)

        task.addOnSuccessListener {
            work.onSuccess(user)

        }.addOnFailureListener {
            work.onFailure(it)

        }
        return work
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