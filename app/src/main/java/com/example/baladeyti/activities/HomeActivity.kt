package com.example.baladeyti.activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.baladeyti.R
import com.example.baladeyti.databinding.ActivityHomeBinding
import com.example.baladeyti.fragments.FavouritesFragment
import com.example.baladeyti.fragments.HomeFragment
import com.example.baladeyti.fragments.SettingsFragment
import com.example.baladeyti.fragments.UserProfileFragment
import com.facebook.*
import com.facebook.GraphRequest.GraphJSONObjectCallback
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONException
import org.json.JSONObject


class HomeActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    lateinit var mSharedPref: SharedPreferences

    lateinit var imagedrawer: CircleImageView

    lateinit var usernamedrawer: TextView

    lateinit var emaildrawer: TextView


    private lateinit var binding: ActivityHomeBinding

    private val homeFragment = HomeFragment()

    private val favouritesFragment = FavouritesFragment()

    private val settingsFragment = SettingsFragment()

    private val profileFragment = UserProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS)
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { `object`, response ->
            // Application code
           /* var name = `object`.getString("name")

            var id =  `object`.getString("id")
            println("here is the response"+name  + id)*/
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email")
        request.parameters = parameters
        request.executeAsync()



        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        try {


            binding = ActivityHomeBinding.inflate(layoutInflater)

            setContentView(binding.root)

            val navView: ActivityHomeBinding = binding

            //Fragment !

            makeCurrentFragement(homeFragment)

            //Navigation Bottom !

            var nav_bottom = findViewById<NavigationBarView>(R.id.bottom_navigation)

            nav_bottom.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.ic_home -> makeCurrentFragement(homeFragment)
                    R.id.ic_claims -> makeCurrentFragement(favouritesFragment)
                    R.id.ic_setting -> makeCurrentFragement(settingsFragment)
                    R.id.ic_profile -> makeCurrentFragement(profileFragment)
                }
                true
            }

            //Drawer !


            val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)

            /*val drawerNavView: NavigationView = findViewById(R.id.nav_view)*/

            toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.hide()


        /*    drawerNavView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.ic_home -> {
                        Toast.makeText(applicationContext, "Clicked on Home", Toast.LENGTH_SHORT)
                            .show()
                        makeCurrentFragement(homeFragment)
                    }
                    R.id.ic_claims -> {
                        Toast.makeText(applicationContext, "Clicked on Claims", Toast.LENGTH_SHORT)
                            .show()
                        makeCurrentFragement(favouritesFragment)
                    }
                    R.id.ic_setting -> {
                        Toast.makeText(
                            applicationContext,
                            "Clicked on Settings",
                            Toast.LENGTH_SHORT
                        ).show()
                        makeCurrentFragement(settingsFragment)
                    }
                    R.id.nav_profile -> {
                        Toast.makeText(
                            applicationContext,
                            "Clicked on Profile",
                            Toast.LENGTH_SHORT
                        ).show()
                        Intent(this, UserProfileActivity::class.java).also {
                            startActivity(it)
                        }
                    }
                    R.id.nav_editProfile -> {
                        Toast.makeText(
                            applicationContext,
                            "Clicked on Edit Profile",
                            Toast.LENGTH_SHORT
                        ).show()
                        Intent(this, EditProfileActivity::class.java).also {
                            startActivity(it)
                        }
                    }

                    R.id.nav_disconnect -> {
                        Toast.makeText(
                            applicationContext,
                            "Clicked on Disconnect",
                            Toast.LENGTH_SHORT
                        ).show()
                        mSharedPref.edit().clear().commit()
                        println(mSharedPref)
                        Intent(this, MainActivity::class.java).also {
                            startActivity(it)
                        }
                    }
                    R.id.nav_who -> {
                        Toast.makeText(
                            applicationContext,
                            "Clicked on Who are we",
                            Toast.LENGTH_SHORT
                        ).show()
                        Intent(this, AboutUsActivity::class.java).also {
                            startActivity(it)
                        }
                    }
                    R.id.nav_terms -> Toast.makeText(
                        applicationContext,
                        "Clicked on Terms and Condition",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }*/


            //Getting from shared into drawer
     /*       val headerView: View = drawerNavView.getHeaderView(0)*/

//            usernamedrawer = headerView.findViewById(R.id.usernamedrawer)
//
//            imagedrawer = headerView.findViewById(R.id.imagedrawer)
//
//            emaildrawer = headerView.findViewById(R.id.emaildrawer)


            val email: String = mSharedPref.getString("emailAddress", "Ghazi@gmail.com").toString()
            val name: String = mSharedPref.getString("firstName", "Ghazi").toString()


            /*val picStr: String = mSharedPref.getString("photos", "my photos").toString()
            val picStrr = "https://baladeyti-application.herokuapp.com/upload/" + picStr.split("/")[4]
            Glide.with(this).load(Uri.parse(picStrr)).into(imagedrawer)
            usernamedrawer.text = name
            emaildrawer.text = email*/

            //Exceptions catch !

        } catch (e: Exception) {

            Log.e(TAG, "onCreateView", e);
            throw e;
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun makeCurrentFragement(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_wrapper, fragment)
            transaction.addToBackStack(null);
            transaction.commit()
        }

    }
}