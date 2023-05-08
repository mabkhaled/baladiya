package com.example.baladeyti.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.baladeyti.R

class SplashScreenActivity : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        supportActionBar?.hide();

        mSharedPref = getSharedPreferences("UserPref", MODE_PRIVATE);

        if (mSharedPref.getBoolean("keep", false) == true) {
            val mainIntent = Intent(this, HomeActivity::class.java)
            startActivity(mainIntent)
        }else{
            //splash screen 3s
            var handler: Handler = Handler()
            handler.postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }





    }
}