package com.nyp.sit.movieviewer_intermediate_starter_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import kotlinx.android.synthetic.main.activity_register_form.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterForm : AppCompatActivity() {


    var appCoroutineScope: CoroutineScope? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_form)

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

        RegisterSubmitBtn.setOnClickListener {
            var loginName = registerLoginNameET.text.toString()
            var password = registerPasswordET.text.toString()
            var email = registerEmailET.text.toString()
            var admin = registerAdminET.text.toString()
            var pem = registerPemET.text.toString()


            appCoroutineScope?.launch(Dispatchers.IO) {

                //Assign the user details to the respective cognito attributes

                var userPool = CognitoUserPool(this@RegisterForm, AWSMobileClient.getInstance().configuration)

                var userAttributes = CognitoUserAttributes()
                userAttributes.addAttribute("email", email)
                userAttributes.addAttribute("custom:AdminNumber", admin)
                userAttributes.addAttribute("custom:PemGrp", pem)

                //Sign up user with the attributes and listen for result
                userPool.signUp(
                    loginName,
                    password,
                    userAttributes,
                    null, object : SignUpHandler {

                        override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                            Log.d("CognitoLab", "Sign up sucess ${signUpResult?.userConfirmed}")
                            var VerifyCodeIntent = Intent(this@RegisterForm, VerificationCode::class.java)
                            VerifyCodeIntent.putExtra("LoginName",loginName)
                            startActivity(VerifyCodeIntent)
                        }

                        override fun onFailure(exception: Exception?) {
                            Log.d("CognitoLab", "Exception: ${exception?.message}")
                        }
                    }
                )

            }
        }
    }

    fun displayToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}