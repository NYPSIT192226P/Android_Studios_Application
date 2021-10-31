package com.nyp.sit.movieviewer_intermediate_starter_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.results.SignUpResult
import kotlinx.android.synthetic.main.activity_verification_code.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class VerificationCode : AppCompatActivity() {


    var appCoroutineScope: CoroutineScope? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)

        var loginName = intent.getStringExtra("LoginName")

        appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

        //Initialize AWS Mobile Client
        AWSMobileClient.getInstance().initialize(this, object : Callback<UserStateDetails> {

            override fun onResult(result: UserStateDetails?) {
                Log.d("CognitoLab", result?.userState?.name.toString())
            }

            override fun onError(e: Exception?) {
                Log.d("CognitoLab", "There is an error - ${e.toString()}")
            }
        })
        verifyBtn.setOnClickListener {
            //Run verification code using AWSMobileClient and the code that was emailed to registered account
            appCoroutineScope?.launch {
                AWSMobileClient.getInstance().confirmSignUp(
                    loginName,
                    verificationET.text.toString(),
                    object : Callback<SignUpResult> {

                        override fun onResult(result: com.amazonaws.mobile.client.results.SignUpResult?) {
                            Log.d("CognitoLab", "Sign up result = ${result?.confirmationState}")
                            var ReturntoLoginIntent = Intent(this@VerificationCode, Login::class.java)
                            startActivity(ReturntoLoginIntent)

                        }

                        override fun onError(e: Exception?) {
                            Log.d("CognitoLab", "There is an error - ${e.toString()}")
                        }

                    })
            }
        }

        resendBtn.setOnClickListener {
            //Trigger a resend of verification code by doing a re-signup again.
            appCoroutineScope?.launch {
                AWSMobileClient.getInstance()
                    .resendSignUp(loginName,
                        object : Callback<com.amazonaws.mobile.client.results.SignUpResult> {

                            override fun onResult(result: com.amazonaws.mobile.client.results.SignUpResult?) {
                                Log.d(
                                    "CognitoLab",
                                    "Resend Verification success to ${result?.userCodeDeliveryDetails?.destination}"
                                )
                            }

                            override fun onError(e: Exception?) {
                                Log.d(
                                    "CognitoLab",
                                    "Resend Verification exception : ${e.toString()}"
                                )
                            }

                        })
            }
        }
    }
}