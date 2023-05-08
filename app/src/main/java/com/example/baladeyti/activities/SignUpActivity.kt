package com.example.baladeyti.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.baladeyti.R
import com.example.baladeyti.databinding.ActivitySignUpBinding
import com.example.baladeyti.models.Claim
import com.example.baladeyti.models.User
import com.example.baladeyti.models.UserRequest
import com.example.baladeyti.models.UserResetResponse
import com.example.baladeyti.services.ApiClaim
import com.example.baladeyti.services.ApiUser
import com.google.android.material.button.MaterialButton
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences
    lateinit var signupbtn: MaterialButton
    lateinit var animationNoreponse: LottieAnimationView
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    private lateinit var mBinding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        mBinding = ActivitySignUpBinding.inflate(LayoutInflater.from(this))

        setContentView(mBinding.root)
        supportActionBar?.hide()

        signupbtn = findViewById(R.id.signupbtn)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        animationNoreponse = findViewById(R.id.animationNoreponse)

        animationNoreponse.playAnimation()
        animationNoreponse.loop(true)

        signupbtn.setOnClickListener {
            if ( validateEmail() && validatePassword() && validateConfirmPassword()&& validatePasswordAndConfirmPassword()){

                val email = email.text.toString().trim()
                val pass = password.text.toString().trim()
                val confirm= confirmPassword.text.toString().trim()

                val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()


                data["emailAddress"] = RequestBody.create(MultipartBody.FORM, email)
                data["password"] = RequestBody.create(MultipartBody.FORM, pass)

                val apiUser = ApiUser.create().userSignUp(data)
                apiUser.enqueue(object : Callback<User> {
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@SignUpActivity, "good", Toast.LENGTH_LONG).show()
                            Log.i("Create User", response.body()!!.toString())




                            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)




                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Error creating User",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.i("API RESPONSE", response.toString())
                            Log.i("Claim response", response.body().toString())
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "SERVER ERROR", Toast.LENGTH_LONG).show()
                    }


                })

            }
        }
    }


    private fun validatePassword(): Boolean {
        var error: String? = null
        val value: String = mBinding.password.text.toString()
        if (value.isEmpty()) {
            error = "Password is required"
        } else if (value.length < 6) {
            error = "Password must be 6 characters long"
        }

        return error == null
    }

    private fun validateConfirmPassword(): Boolean {
        var error: String? = null
        val value: String = mBinding.confirmPassword.text.toString()
        if (value.isEmpty()) {
            error = "Confirm Password is required"
        } else if (value.length < 6) {
            error = "Confirm Password must be 6 characters long"
        }

        return error == null
    }

    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.email.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Username name must be a valid Email"
        }

        if (errorMessage != null) {
            mBinding.email.apply {

                error = errorMessage
            }
        }


        return errorMessage == null
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        var error: String? = null
        val password = mBinding.password.text.toString()
        val confimPassword = mBinding.confirmPassword.text.toString()
        if (password != confimPassword) {
            error = "Confirm password doesn't match with password"
        }
        return error == null
    }


//    override fun onFocusChange(view: View?, hasFocus: Boolean) {
//        if (view != null) {
//            when (view.id) {
//                R.id.username -> {
//                    if (hasFocus) {
//                        if (mBinding.username.isCounterEnabled)
//                    } else {
//                        validateEmail()
//                    }
//                }
//                R.id.password -> {
//                    if (hasFocus) {
//
//                    } else {
//                        validatePassword()
//                    }
//                }
//                R.id.confirmPassword -> {
//                    if (hasFocus) {
//
//                    } else {
//                        validateConfirmPassword()
//                    }
//                }
//            }
//        }
//    }


}