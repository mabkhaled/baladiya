package com.example.baladeyti.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.baladeyti.R
import com.example.baladeyti.models.userUpdateResponse
import com.example.baladeyti.services.ApiUser
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class EditProfileActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null

    private lateinit var fab: FloatingActionButton

    private lateinit var _id: String
    private lateinit var lastName: EditText
    private lateinit var firstName: EditText
    private lateinit var emailAddress: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var cin: EditText
    private lateinit var profileimage: ImageView
    lateinit var buttonUpdate: MaterialButton
    lateinit var mSharedPref: SharedPreferences

    var imagePicker: ImageView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        supportActionBar?.hide()


        mSharedPref = getSharedPreferences("UserPref", MODE_PRIVATE)

        profileimage = findViewById(R.id.profileimage)
        val picStr: String = mSharedPref.getString("photos", "my photos").toString()
        println(picStr)
        if(picStr != "my photos"){
           val picStrr="http://192.168.1.3:3000/upload/"+picStr.split("/")[4]
          Glide.with(this).load(Uri.parse(picStrr)).into(profileimage)
        }





        val mail: String = mSharedPref.getString("emailAddress", "mail").toString()
        emailAddress = findViewById(R.id.Email)
        emailAddress.setText(mail)

        val first: String = mSharedPref.getString("firstName", "firstname").toString()
        firstName = findViewById(R.id.firstName)
        firstName.setText(first)

        val last: String = mSharedPref.getString("lastName", "lastname").toString()
        lastName = findViewById(R.id.lastName)
        lastName.setText(last)

        val phone: String = mSharedPref.getString("phoneNumber", "telephone number").toString()
        phoneNumber = findViewById(R.id.Number)
        phoneNumber.setText(phone)

        val numberNational: String = mSharedPref.getString("cin", "cin").toString()
        cin = findViewById(R.id.cin)
        cin.setText(numberNational)

        buttonUpdate = findViewById(R.id.buttonUpdate)
        imagePicker = findViewById(R.id.profileimage)
        fab = findViewById(R.id.fab)

        fab.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                .crop()                 //Crop image(Optional), Check Customization for more option
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        })


        buttonUpdate.setOnClickListener {

            val myEmailAddress = emailAddress.text.toString().trim()
            val myFirstName = firstName.text.toString().trim()
            val myLastName = lastName.text.toString().trim()
            val myPhoneNumber = phoneNumber.text.toString().trim()
            val myCin = cin.text.toString().trim()

            if (myEmailAddress.isEmpty()) {
                emailAddress.error = "Email required"
                emailAddress.requestFocus()
                return@setOnClickListener
            }

            if (myFirstName.isEmpty()) {
                firstName.error = "Name required"
                firstName.requestFocus()
                return@setOnClickListener
            }
            if (myLastName.isEmpty()) {
                lastName.error = "Last name required"
                lastName.requestFocus()
                return@setOnClickListener
            }
            if (myPhoneNumber.isEmpty()) {
                phoneNumber.error = "Num required"
                phoneNumber.requestFocus()
                return@setOnClickListener
            }

            if (myCin.isEmpty()) {
                phoneNumber.error = "cin required"
                phoneNumber.requestFocus()
                return@setOnClickListener
            }
           /* if (selectedImageUri == null) {
                applicationContext.snackbar("Select an Image First")
                return@setOnClickListener
            }*/

            if(selectedImageUri != null){
                println(selectedImageUri)
                val parcelFileDescriptor =
                    contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)
                        ?: return@setOnClickListener

            }

            _id = mSharedPref.getString("id", "my id").toString()

            update(
                myEmailAddress,
                myFirstName,
                myLastName,
                myPhoneNumber,
                myCin,
                _id
            )

        }




    }


    private fun update(myEmailAddress: String, myFirstName: String, myLastName: String, myPhoneNumber: String, myCin: String,id: String){

        if(selectedImageUri == null){
            MotionToast.darkColorToast(
                this,
                "Error update :",
                "Image should be changed",
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_TOP,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(
                    this,
                    www.sanju.motiontoast.R.font.helvetica_regular
                )
            )

            return
        }

        val id1 = _id;
        //id1 = mSharedPref.getString("id", "user.id").toString();

        println(id1);
        val stream = contentResolver.openInputStream(selectedImageUri!!)
        val request =
            stream?.let { RequestBody.create("image/png".toMediaTypeOrNull(), it.readBytes()) } // read all bytes using kotlin extension
        val photos = request?.let {
            MultipartBody.Part.createFormData(
                "photos",
                "file.png",
                it
            )
        }


        Log.d("MyActivity", "on finish upload file")


        val apiInterface = ApiUser.create()
        val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

        data["lastName"] = RequestBody.create(MultipartBody.FORM, myLastName)
        data["firstName"] = RequestBody.create(MultipartBody.FORM, myFirstName)
        data["emailAddress"] = RequestBody.create(MultipartBody.FORM, myEmailAddress)
        data["phoneNumber"] = RequestBody.create(MultipartBody.FORM, myPhoneNumber)
        data["cin"] = RequestBody.create(MultipartBody.FORM, myCin)

        if (photos != null) {
            println("Ahla ena image : $photos")
            apiInterface.userUpdate(_id,data,photos).enqueue(object:
                Callback<userUpdateResponse> {
                override fun onResponse(
                    call: Call<userUpdateResponse>,
                    response: Response<userUpdateResponse>
                ) {
                    if(response.isSuccessful){
                        Log.i("onResponse goooood", response.body().toString())
                        MotionToast.darkColorToast(
                            this@EditProfileActivity,
                            "Success ",
                            "Account updated!",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_TOP,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(
                                this@EditProfileActivity,
                                www.sanju.motiontoast.R.font.helvetica_regular
                            )
                        )


                        mSharedPref.edit().apply{

                            putString("photos", response.body()?.user?.photos.toString())
                            putString("id", response.body()?.user?._id.toString())
                            putString("lastName", response.body()?.user?.lastName.toString())
                            putString("emailAddress", response.body()?.user?.emailAddress)
                            putString("password", response.body()?.user?.password)
                            putString("phoneNumber", response.body()?.user?.phoneNumber.toString())
                            putString("firstName", response.body()?.user?.firstName.toString())
                            putString("cin", response.body()?.user?.cin.toString())



                            println(response.body()?.user?.password.toString())
                            //putBoolean("session", true)
                        }.apply()

                        finish()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        Log.i("OnResponse not good", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<userUpdateResponse>, t: Throwable) {

                    println("Failed to do request")
                }

            })
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE){
            selectedImageUri = data?.data
            profileimage?.setImageURI(selectedImageUri)

        }
    }
}