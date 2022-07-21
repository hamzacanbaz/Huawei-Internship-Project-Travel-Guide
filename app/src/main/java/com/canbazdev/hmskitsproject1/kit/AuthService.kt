package com.canbazdev.hmskitsproject1.kit

import android.app.Activity
import android.content.Context
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import com.huawei.hms.support.account.service.AccountAuthService

/*
*   Created by hamzacanbaz on 7/7/2022
*/
class AuthService(context: Context) {

    companion object {
        const val HUAWEI_ID_LOGIN_CODE = 8888
    }

    private var service: AccountAuthService? = null
    private var authParams: AccountAuthParams? = null

    init {
        authParams =
            AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME).setAuthorizationCode()
                .createParams()

    }

    fun silentSignIn(
        activity: Activity,
        onSuccess: (() -> Unit)? = null,
        onFail: ((e: Exception) -> Unit)? = null
    ) {
        val authParams: AccountAuthParams =
            AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME).createParams()
        val service: AccountAuthService = AccountAuthManager.getService(activity, authParams)
        val task: Task<AuthAccount> = service.silentSignIn()
        task.addOnSuccessListener {
            println(it.idToken)
            // Obtain the user's ID information.
            //Log.i(TAG, "displayName:" + authAccount.displayName)
            // Obtain the ID type (0: HUAWEI ID; 1: AppTouch ID).
            //Log.i(TAG, "accountFlag:" + authAccount.accountFlag);
            onSuccess?.invoke()
        }
        task.addOnFailureListener { e -> // The sign-in failed. Try to sign in explicitly using getSignInIntent().
            if (e is ApiException) {
                val apiException = e as ApiException
                //Log.i(TAG, "sign failed status:" + apiException.statusCode)
                onFail?.invoke(apiException)
            }
        }
    }


}