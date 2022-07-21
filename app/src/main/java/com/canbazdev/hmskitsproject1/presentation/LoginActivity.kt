package com.canbazdev.hmskitsproject1.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.canbazdev.hmskitsproject1.databinding.ActivityLoginBinding
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.api.AGConnectApi
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.VerifyCodeSettings
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import dagger.hilt.android.AndroidEntryPoint
import java.security.InvalidParameterException
import java.util.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    //private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goToMainActivity("hamza")
        AGConnectApi.getInstance().activityLifecycle().onCreate(this)
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(applicationContext);
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // authService = AuthService(this)

        val settings = VerifyCodeSettings.newBuilder()
            .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
            .sendInterval(50)
            .locale(Locale.getDefault())
            .build()


        // FOR MOBILE NUMBER
        binding.btnSignIn.setOnClickListener {


            try {
                val task =
                    AGConnectAuth.getInstance().requestVerifyCode("90", "5056953929", settings)
                task.addOnSuccessListener {
                    println(it)
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            } catch (e: InvalidParameterException) {
                println(e.localizedMessage)
            }


            /* val phoneUser = PhoneUser.Builder()
                 .setCountryCode("90")
                 .setPhoneNumber("5056953929")
                 .setVerifyCode("verify code")
                 .setPassword("123456")
                 .build()
             try {
                 AGConnectAuth.getInstance().createUser(phoneUser).addOnSuccessListener {
                     // A newly created user account is automatically signed in to your app.
                 }.addOnFailureListener {
                     // onFail
                 }
             } catch (e: InvalidParameterException) {
                 println(e.localizedMessage)
             }*/

        }

        binding.btnSignUp.setOnClickListener {
        }

        //     java.security.InvalidParameterException: url is null
        binding.btnAnonymous.setOnClickListener {
            try {
                AGConnectAuth.getInstance().signInAnonymously().addOnSuccessListener {
                    println(it.user.uid)
                }.addOnFailureListener {
                    println(it.localizedMessage)
                }
            } catch (e: InvalidParameterException) {
                println(e.localizedMessage)
            }
        }

        binding.HuaweiIdAuthButton.setOnClickListener {
            try {
                AGConnectAuth.getInstance().signIn(this, AGConnectAuthCredential.HMS_Provider)
                    .addOnSuccessListener {
                        println(it.user.email)
                    }.addOnFailureListener {
                        println(it.localizedMessage)
                    }
            } catch (e: InvalidParameterException) {
                println(e.localizedMessage)
            }

        }
    }


    val HUAWEI_AUTH_CODE = 8888 // check for


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AGConnectApi.getInstance().activityLifecycle()
            .onActivityResult(requestCode, resultCode, data)

        if (requestCode == HUAWEI_AUTH_CODE) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {
                val authAccount = authAccountTask.result
                println(authAccount.accessToken)
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