package com.example.baladeyti.activities

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.baladeyti.R
import com.google.android.material.imageview.ShapeableImageView

class UserProfileActivity : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences
    lateinit var idfullname: TextView
    lateinit var idUrlImg: ShapeableImageView
    lateinit var idEmail: TextView
    lateinit var idphone: TextView
    lateinit var idcin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        supportActionBar?.hide()

        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        idfullname = findViewById(R.id.idfullname)

        idUrlImg= findViewById(R.id.idUrlImg)

        idEmail = findViewById(R.id.idEmail)

        idphone = findViewById(R.id.idphone)

        idcin = findViewById(R.id.idcin)

        val email: String = mSharedPref.getString("emailAddress", "KHALED12@gmail.com").toString()
        val firstName: String = mSharedPref.getString("firstName", "khaled").toString()
        val phone: String = mSharedPref.getString("phoneNumber", "phone").toString()
        val cin: String = mSharedPref.getString("cin", "cin").toString()


        val picStr: String = mSharedPref.getString("photos", "my photos").toString()
        val picStrr="http://192.168.1.3:3000/upload/"+picStr.split("/")[4]
        Glide.with(this).load(Uri.parse(picStrr)).into(idUrlImg)
        idfullname.text = firstName
        idEmail.text = email
        idphone.text = phone
        idcin.text = cin


    }
}