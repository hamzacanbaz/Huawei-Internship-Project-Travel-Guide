package com.canbazdev.hmskitsproject1.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.canbazdev.hmskitsproject1.databinding.ActivityLoginBinding
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.HuaweiIdAuthButton.setOnClickListener {
            val authParams: AccountAuthParams =
                AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setIdToken()
                    .createParams()

            val service: AccountAuthService = AccountAuthManager.getService(this, authParams)
            startActivityForResult(service.signInIntent, HUAWEI_AUTH_CODE)
        }
    }


    val HUAWEI_AUTH_CODE = 8888 // check for


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HUAWEI_AUTH_CODE) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {
                val authAccount = authAccountTask.result

                goToMainActivity(authAccount.displayName)

            } else {

                Log.e(
                    "TAG",
                    "sign in failed : " + (authAccountTask.exception as ApiException).statusCode
                )
            }
        }
    }

    private fun goToMainActivity(name: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("accountName", name)
        startActivity(intent)
        finish()
    }

}