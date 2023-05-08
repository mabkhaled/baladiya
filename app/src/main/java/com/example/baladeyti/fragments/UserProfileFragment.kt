package com.example.baladeyti.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.baladeyti.R
import com.example.baladeyti.activities.EditProfileActivity
import com.example.baladeyti.activities.MainActivity
import com.google.android.material.imageview.ShapeableImageView





class UserProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mSharedPref: SharedPreferences
    lateinit var idfullname: TextView
    lateinit var idUrlImg: ShapeableImageView
    private val claimsFragment = ClaimsFragment()
    private val extraitFragment = ExtraitFragment()
    lateinit var idEmail: TextView
    lateinit var idphone: TextView
    lateinit var idclaims: TextView
    lateinit var idArticles: TextView
    lateinit var idcin: TextView
    lateinit var ic_profileSettings: ImageButton
    lateinit var ic_profileDisconnect: ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_user_profile, container, false)

        mSharedPref = activity?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!

        idfullname = root.findViewById(R.id.idfullname)

        idUrlImg = root.findViewById(R.id.idUrlImg)

        idEmail = root.findViewById(R.id.idEmail)

        idphone = root.findViewById(R.id.idphone)

        idclaims = root.findViewById(R.id.idclaims)

        idArticles = root.findViewById(R.id.idArticles)

        idcin = root.findViewById(R.id.idcin)

        ic_profileDisconnect = root.findViewById(R.id.ic_profileDisconnect)

        ic_profileSettings = root.findViewById(R.id.ic_profileSettings)

        val email: String = mSharedPref.getString("emailAddress", "Ghazi@gmail.com").toString()
        val firstName: String = mSharedPref.getString("firstName", "Ghazi").toString()
        val phone: String = mSharedPref.getString("phoneNumber", "phone").toString()
        val cin: String = mSharedPref.getString("cin", "cin").toString()

        val picStr: String = mSharedPref.getString("photos", "my photos").toString()
        println(picStr)
        if (picStr != null) {

           // val picStrr = "http://192.168.1.3:3000/upload/" + picStr.split("/")[4]
            //Glide.with(this).load(Uri.parse(picStrr)).into(idUrlImg)
        } else {

        }

        idfullname.text = firstName
        idEmail.text = email
        idphone.text = phone
        idcin.text = cin

        ic_profileSettings.setOnClickListener {
            Intent(activity, EditProfileActivity::class.java).also {
                startActivity(it)
            }
        }
        ic_profileDisconnect.setOnClickListener {

            mSharedPref.edit().clear().commit()
            println(mSharedPref)
            Intent(activity, MainActivity::class.java).also {
                startActivity(it)
            }
        }

        idclaims.setOnClickListener {
            makeCurrentFragement(claimsFragment)
        }

        idArticles.setOnClickListener {
            makeCurrentFragement(extraitFragment)
        }

        return root
    }
    private fun makeCurrentFragement(fragment: Fragment) {
        if (fragment != null) {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(com.example.baladeyti.R.id.fl_wrapper, fragment)
            transaction?.addToBackStack(null);
            transaction?.commit()
        }

    }

}