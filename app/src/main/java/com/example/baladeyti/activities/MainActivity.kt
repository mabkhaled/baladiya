package com.example.baladeyti.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.content.res.ResourcesCompat
import com.example.baladeyti.R
import com.example.baladeyti.databinding.ActivityMainBinding
import com.example.baladeyti.models.*
import com.example.baladeyti.services.ApiUser
import com.facebook.CallbackManager
import com.google.android.material.button.MaterialButton
import com.marcoscg.dialogsheet.DialogSheet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookException

import com.facebook.login.LoginResult

import com.facebook.FacebookCallback

import com.facebook.login.LoginManager
import java.util.*
import com.facebook.AccessToken





class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {

    lateinit var username: TextView
    lateinit var password: TextView
    lateinit var button: MaterialButton
    lateinit var buttonSingUp: MaterialButton
    lateinit var forgotpass: TextView
    lateinit var fb_button: ImageFilterView


    lateinit var customEmail: String
    lateinit var mycode: String

    lateinit var callbackManager : CallbackManager



    lateinit var mSharedPref: SharedPreferences

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        //Facebook

         callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                 println(loginResult.toString())
                    startActivity(Intent(applicationContext,HomeActivity::class.java))
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })

        supportActionBar?.hide()


        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        forgotpass = findViewById(R.id.forgotpass)
        fb_button = findViewById(R.id.fb_button)

        //On click FB connect
        fb_button.setOnClickListener {


            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }


        button = findViewById(R.id.loginbtn)



        buttonSingUp = findViewById(R.id.signupbtn)

        // admin and admin

//        button.setOnClickListener {
//            if (username.text.toString() == "admin" && password.text.toString() == "admin"
//            ) {
//                //correct
//                Toast.makeText(this, "Login SUCCESSFULL", Toast.LENGTH_SHORT).show()
//                Intent(this, HomeActivity::class.java).also {
//                    startActivity(it)
//                }
//            } else {
//                //false
//                Toast.makeText(this, "Login FAILED", Toast.LENGTH_SHORT).show()
//            }
//        }


        button.setOnClickListener {
            val email = username.text.toString().trim()
            val mypassword = password.text.toString().trim()
            if (email.isEmpty()) {
                username.error = "Email required"
                username.requestFocus()
                return@setOnClickListener
            }

            if (mypassword.isEmpty()) {
                password.error = "Password required"
                password.requestFocus()
                return@setOnClickListener
            }

            var myUser = User()

            myUser.emailAddress = username.text.toString()
            myUser.password = password.text.toString()
            val apiUser = ApiUser.create().userLogin(myUser)
            apiUser.enqueue(object : Callback<UserAndToken> {
                override fun onResponse(
                    call: Call<UserAndToken>,
                    response: Response<UserAndToken>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "good", Toast.LENGTH_LONG).show()
                        Log.i("Login User", response.body()!!.toString())
                        mSharedPref.edit().apply {
                            putString("id", response.body()?.user?._id.toString())
                            putString("firstName", response.body()?.user?.firstName.toString())
                            putString("lastName", response.body()?.user?.lastName.toString())
                            putString(
                                "emailAddress", username.text.toString()
                            )
                            putString(
                                "password", password.text.toString()
                            )
                            putString(
                                "token",
                                response.body()?.token.toString()
                            )
                            putString("gender", response.body()?.user?.gender.toString())
                            putString("photos", response.body()?.user?.photos.toString())
                            putString("cin", response.body()?.user?.cin.toString())
                            putString("phoneNumber", response.body()?.user?.phoneNumber.toString())
                            putBoolean("keep",true)

                        }.apply()
                        finish()

                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        println(
                            "My shaaaaaaaaaaaaaaaaaared prefs email ! " + mSharedPref.getString(
                                "emailAddress",
                                "hey"
                            )
                        );
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Incorrect email or password",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.i("API RESPONSE", response.toString())
                        Log.i("login response", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<UserAndToken>, t: Throwable) {
                    Toast.makeText(applicationContext, "SERVER ERROR", Toast.LENGTH_LONG).show()
                }
            })

        }


        buttonSingUp.setOnClickListener {
            Intent(this, SignUpActivity::class.java).also {
                startActivity(it)
            }
        }


        //Confirm Password Dialog


        val dialogSheetPassword = DialogSheet(this@MainActivity, true)
        dialogSheetPassword.setView(R.layout.reset_password_dialog_view)
        val dialogSheetPasswordfactory = layoutInflater
        val passwordView: View =
            dialogSheetPasswordfactory.inflate(R.layout.reset_password_dialog_view, null)

        // you can also use DialogSheet2 if you want the new style
        //.setNewDialogStyle() // You can also set new style by this method, but put it on the first line
        dialogSheetPassword.setTitle("Change Your Password")
            .setMessage("You should enter password and confirm your password :")
            .setSingleLineTitle(true)
            .setColoredNavigationBar(true)
        //.setButtonsColorRes(R.color.colorAccent) // You can use dialogSheetAccent style attribute instead

        /* .setPositiveButton(android.R.string.ok) {
             println("elcode+----------------"+code1.text.toString())
             Toast.makeText(this@LoginPro, "code have been sent", Toast.LENGTH_SHORT).show()
         }
         .setNegativeButton(android.R.string.cancel)
         .setNeutralButton("Neutral")
     */
        dialogSheetPassword.setIconResource(R.drawable.building)

        val inflatedViewPassword = dialogSheetPassword.inflatedView

        val newinflatedView = dialogSheetPassword.inflatedView
        val sendPassword = inflatedViewPassword?.findViewById<Button>(R.id.sendPassword)
        val customPassword = inflatedViewPassword?.findViewById<EditText>(R.id.custom_password)
        val customConfirmPassword =
            inflatedViewPassword?.findViewById<EditText>(R.id.custom_confirm_password)

        sendPassword?.setOnClickListener {

            var verificationRequest = UserRequest()
            verificationRequest.token = mycode

            verificationRequest.emailAddress = customEmail
            verificationRequest.password = customPassword?.text.toString()
            /*verification.user?.emailAddress = customEmail*/
            println("ena zok om email !! : " + customEmail)
            println("ena zok om verification !! : " + verificationRequest)
            println("ena zok om code !! : " + mycode)
            println("ena zok om pass !! : " + customPassword?.text.toString())
            val apiuser = ApiUser.create().resetPassword(
                verificationRequest
            )
            apiuser.enqueue(object : Callback<UserRequest> {
                override fun onResponse(
                    call: Call<UserRequest>,
                    response: Response<UserRequest>
                ) {
                    if (response.isSuccessful ) {


                        if (response.body()?.msg.toString() == "Your verification link may have expired. Please click on resend for verify your Email.") {
                            MotionToast.darkColorToast(
                                this@MainActivity,
                                "Failed ",
                                "Wrong email !",
                                MotionToastStyle.WARNING,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this@MainActivity,
                                    www.sanju.motiontoast.R.font.helvetica_regular
                                )
                            )
                        }

                        dialogSheetPassword.dismiss()
                        if (response.body()?.msg.toString() == "Your password has been successfully reset") {
                            Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Log.i("PASSSSS_API_RESPONSE", response.body().toString())

                    } else {

                        Toast.makeText(applicationContext, "Incorrect email ", Toast.LENGTH_LONG)
                            .show()
                        Log.i("PASSSSS_API_RESPONSE", response.toString())
                    }
                }

                override fun onFailure(call: Call<UserRequest>, t: Throwable) {
                    Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
                }

            })


        }


        //Custom dialog sheet

        val dialogSheet = DialogSheet(this@MainActivity, true)
        dialogSheet.setView(R.layout.custom_dialog_view)
        val factory = layoutInflater
        val view: View = factory.inflate(R.layout.custom_dialog_view, null)

        // you can also use DialogSheet2 if you want the new style
        //.setNewDialogStyle() // You can also set new style by this method, but put it on the first line
        dialogSheet.setTitle("Reset Password")
            .setMessage("Verification code will be sent to the mail")
            .setSingleLineTitle(true)
            .setColoredNavigationBar(true)
        //.setButtonsColorRes(R.color.colorAccent) // You can use dialogSheetAccent style attribute instead

        /* .setPositiveButton(android.R.string.ok) {
             println("elcode+----------------"+code1.text.toString())
             Toast.makeText(this@LoginPro, "code have been sent", Toast.LENGTH_SHORT).show()
         }
         .setNegativeButton(android.R.string.cancel)
         .setNeutralButton("Neutral")
     */
        dialogSheet.setIconResource(R.drawable.building)

        val inflatedView = dialogSheet.inflatedView
        val sendcode = inflatedView?.findViewById<Button>(R.id.sendCode)
        val customEditTextemail = inflatedView?.findViewById<EditText>(R.id.customEditTextemail)

        val code1 = inflatedView?.findViewById<EditText>(R.id.code1)
        val code2 = inflatedView?.findViewById<EditText>(R.id.code2)
        val code3 = inflatedView?.findViewById<EditText>(R.id.code3)
        val code4 = inflatedView?.findViewById<EditText>(R.id.code4)



        code1?.isEnabled = false
        code2?.isEnabled = false
        code3?.isEnabled = false
        code4?.isEnabled = false





        sendcode?.setOnClickListener {
            var userReset = UserReset()
            userReset.emailAddress = customEditTextemail?.text.toString()
            customEmail = customEditTextemail?.text.toString()
            val apiuser = ApiUser.create().sendResetCode(userReset)

            apiuser.enqueue(object : Callback<UserResetResponse> {
                override fun onResponse(
                    call: Call<UserResetResponse>,
                    response: Response<UserResetResponse>
                ) {
                    println("++++++++++++++response" + response.body()?.msg.toString())
                    if (response.isSuccessful) {

                        if (response.body()?.msg.toString() == "false") {
                            MotionToast.darkColorToast(
                                this@MainActivity,
                                "Failed ",
                                "Wrong email !",
                                MotionToastStyle.WARNING,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this@MainActivity,
                                    www.sanju.motiontoast.R.font.helvetica_regular
                                )
                            )
                            code1?.isEnabled = false
                            code2?.isEnabled = false
                            code3?.isEnabled = false
                            code4?.isEnabled = false
                        }

                        if (response.body()?.succes.toString() == "true") {


                            MotionToast.darkColorToast(
                                this@MainActivity,
                                "Success ",
                                "code sent!",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this@MainActivity,
                                    www.sanju.motiontoast.R.font.helvetica_regular
                                )
                            )
                            code1?.isEnabled = true
                            code2?.isEnabled = true
                            code3?.isEnabled = true
                            code4?.isEnabled = true

                            code1?.addTextChangedListener(object : TextWatcher {
                                override fun beforeTextChanged(
                                    s: CharSequence,
                                    start: Int,
                                    count: Int,
                                    after: Int
                                ) {

                                }

                                override fun onTextChanged(
                                    s: CharSequence,
                                    start: Int,
                                    before: Int,
                                    count: Int
                                ) {
                                    if (s.length == 1) {
                                        println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                                        code2?.requestFocus()
                                    }
                                    code2?.addTextChangedListener(object : TextWatcher {
                                        override fun beforeTextChanged(
                                            s: CharSequence,
                                            start: Int,
                                            count: Int,
                                            after: Int
                                        ) {

                                        }

                                        override fun onTextChanged(
                                            s: CharSequence,
                                            start: Int,
                                            before: Int,
                                            count: Int
                                        ) {
                                            if (s.length == 1) {
                                                println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                                                code3?.requestFocus()
                                            }
                                        }

                                        override fun afterTextChanged(s: Editable) {


                                        }
                                    })
                                    code3?.addTextChangedListener(object : TextWatcher {
                                        override fun beforeTextChanged(
                                            s: CharSequence,
                                            start: Int,
                                            count: Int,
                                            after: Int
                                        ) {

                                        }

                                        override fun onTextChanged(
                                            s: CharSequence,
                                            start: Int,
                                            before: Int,
                                            count: Int
                                        ) {
                                            if (s.length == 1) {
                                                println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                                                code4?.requestFocus()
                                            }
                                        }

                                        override fun afterTextChanged(s: Editable) {


                                        }
                                    })
                                    code4?.addTextChangedListener(object : TextWatcher {
                                        override fun beforeTextChanged(
                                            s: CharSequence,
                                            start: Int,
                                            count: Int,
                                            after: Int
                                        ) {

                                        }

                                        override fun onTextChanged(
                                            s: CharSequence,
                                            start: Int,
                                            before: Int,
                                            count: Int
                                        ) {
                                            if (s.length == 1) {
                                                println("Ena code -------------")
                                                mycode =
                                                    code1?.text.toString() + code2?.text.toString() + code3?.text.toString() + code4?.text.toString()
                                                println(mycode)
                                                println("Ena token ----------------------------")
                                                println(response.body()?.token.toString())
                                                if (response.body()?.token.toString() == mycode) {
                                                    dialogSheet.dismiss()
                                                    dialogSheetPassword.show()
                                                } else {
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Invalid Code",
                                                        Toast.LENGTH_LONG
                                                    )
                                                        .show()
                                                }


                                            }
                                        }

                                        override fun afterTextChanged(s: Editable) {


                                        }
                                    })
                                }

                                override fun afterTextChanged(s: Editable) {


                                }
                            })

                        }


                        //Toast.makeText(applicationContext, "Code Sent Successfully", Toast.LENGTH_LONG).show()


                    } else {

                        Toast.makeText(applicationContext, "Failed to Login", Toast.LENGTH_LONG)
                            .show()

                    }
                }

                override fun onFailure(call: Call<UserResetResponse>, t: Throwable) {

                    MotionToast.darkColorToast(
                        this@MainActivity,
                        "Failed ",
                        "Server problem",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_TOP,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this@MainActivity,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }


            })




            println("elcode222+----------------" + customEditTextemail?.text.toString())
        }









        forgotpass.setOnClickListener { dialogSheet.show() }


    }


    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*override fun onStart() {
        super.onStart()

        if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }*/
}