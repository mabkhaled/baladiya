package com.example.baladeyti.fragments

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.res.ResourcesCompat
import com.example.baladeyti.R
import com.example.baladeyti.activities.EditProfileActivity
import com.example.baladeyti.activities.HomeActivity
import com.example.baladeyti.activities.MapActivity
import com.example.baladeyti.activities.SignUpActivity
import com.example.baladeyti.models.Claim
import com.example.baladeyti.models.ClaimRequest
import com.example.baladeyti.models.User
import com.example.baladeyti.models.UserAndToken
import com.example.baladeyti.services.ApiClaim
import com.example.baladeyti.services.ApiUser
import com.facebook.FacebookSdk
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.marcoscg.dialogsheet.DialogSheet
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class FavouritesFragment : Fragment() {

    lateinit var mSharedPref: SharedPreferences
    private var selectedImageUri: Uri? = null
/*    private var cropActivityResultContract: ActivityResultContract<Any?,> = object*/
    lateinit var fab : FloatingActionButton
    lateinit var btnImage: ImageButton
    lateinit var btnAdd: MaterialButton
    lateinit var customEditTextSubject: TextInputEditText
    lateinit var customEditTextTopic: TextInputEditText
    private val claimsFragment = ClaimsFragment()

    var imagePicker: ImageView?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

         fab = view.findViewById(R.id.mapBottom)

        imagePicker = view.findViewById(R.id.complaint_form)

        btnImage = view.findViewById(R.id.btnImage)

        btnAdd = view.findViewById(R.id.btnAdd)

        customEditTextSubject = view.findViewById(R.id.customEditTextSubject)

        customEditTextTopic = view.findViewById(R.id.customEditTextTopic)

        mSharedPref = view.context!!.getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        var _id = mSharedPref.getString("id", "my id").toString()

        btnImage.setOnClickListener {
            Intent(activity, MapActivity::class.java).also {
                startActivity(it)
            }
        }

        btnAdd.setOnClickListener {
            val subject = customEditTextSubject.text.toString().trim()
            val topic = customEditTextTopic.text.toString().trim()
            val longitude: String = mSharedPref.getString("long", "").toString()
            val laltitude: String = mSharedPref.getString("lat", "").toString()



            if (topic.isEmpty()) {
                customEditTextTopic.error = "Topic required"
                customEditTextTopic.requestFocus()
                return@setOnClickListener
            }
            if (subject.isEmpty()) {
                customEditTextSubject.error = "Content required"
                customEditTextSubject.requestFocus()
                return@setOnClickListener
            }


           /* //Confirm Password Dialog


            val dialogSheetPassword = DialogSheet(view.context, true)
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

            *//* .setPositiveButton(android.R.string.ok) {
                 println("elcode+----------------"+code1.text.toString())
                 Toast.makeText(this@LoginPro, "code have been sent", Toast.LENGTH_SHORT).show()
             }
             .setNegativeButton(android.R.string.cancel)
             .setNeutralButton("Neutral")
         *//*
            dialogSheetPassword.setIconResource(R.drawable.building)

            val inflatedViewPassword = dialogSheetPassword.inflatedView

            val newinflatedView = dialogSheetPassword.inflatedView
            val sendPassword = inflatedViewPassword?.findViewById<Button>(R.id.sendPassword)
            val customPassword = inflatedViewPassword?.findViewById<EditText>(R.id.custom_password)
            val customConfirmPassword =
                inflatedViewPassword?.findViewById<EditText>(R.id.custom_confirm_password)*/





            /*        val parcelFileDescriptor =
                        contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)
                            ?: return@setOnClickListener*/

            val parcelFileDescriptor =
                context?.contentResolver!!.openFileDescriptor(selectedImageUri!!, "r", null)
                    ?: return@setOnClickListener


            val stream = context?.contentResolver!!.openInputStream(selectedImageUri!!)
            val request =
                stream?.let { RequestBody.create("image/png".toMediaTypeOrNull(), it.readBytes()) } // read all bytes using kotlin extension
            val photos = request?.let {
                MultipartBody.Part.createFormData(
                    "photos",
                    "file.png",
                    it
                )
            }
/*          var myClaim = Claim()*/

            val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

            data["name"] = RequestBody.create(MultipartBody.FORM, topic)
            data["text"] = RequestBody.create(MultipartBody.FORM, subject)
            data["laltitude"] = RequestBody.create(MultipartBody.FORM, laltitude)
            data["longitude"] = RequestBody.create(MultipartBody.FORM, longitude)
            data["author"] = RequestBody.create(MultipartBody.FORM, _id)
      /*
            myClaim.name = topic
            myClaim.text = subject
            myClaim.author = _id
            myClaim.photos = _id*/
           /* println(myClaim.toString())*/
            println("Ahla ena data ! "+RequestBody.toString())
            val apiClaim = ApiClaim.create().createClaim(data,photos!!)
            apiClaim.enqueue(object : Callback<Claim> {
                override fun onResponse(
                    call: Call<Claim>,
                    response: Response<Claim>
                ) {
                    if (response.isSuccessful) {
                        Log.i("Create Claim", response.body()!!.toString())
                        MotionToast.darkColorToast(
                            activity!!,
                            "Success ",
                            "Claim sent!",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_TOP,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(
                                activity!!,
                                www.sanju.motiontoast.R.font.helvetica_regular
                            )
                        )

                        makeCurrentFragement(claimsFragment)

                        customEditTextTopic.text?.clear()
                        customEditTextSubject.text?.clear()
                        mSharedPref.edit().remove("long").remove("lat").commit()
                        println(mSharedPref.getString("lat","").toString())

                    } else {
                        Toast.makeText(
                            view.context,
                            "Error creating claim",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.i("API RESPONSE", response.toString())
                        Log.i("Claim response", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<Claim>, t: Throwable) {
                    Toast.makeText(view.context, "SERVER ERROR", Toast.LENGTH_LONG).show()
                }


            })

        }


/*
        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(llBottomSheet)*/


//        fab.setOnClickListener {
//            println("Ahla bik")
//            val llBottomSheet: View = inflater.inflate(R.layout.bottom_sheet,null)
//
//            val dialog = BottomSheetDialogFragment()
//            dialog.show(requireFragmentManager(),tag)
//
//        }



        fab.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                               //Crop image(Optional), Check Customization for more option
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    960,
                    1500
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        })



        return /*inflater.inflate(R.layout.fragment_favourites, container, false)*/ view
    }


    private fun makeCurrentFragement(fragment: Fragment) {
        if (fragment != null) {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fl_wrapper, fragment)
            transaction?.addToBackStack(null);
            transaction?.commit()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE){
            selectedImageUri = data?.data
            imagePicker?.setImageURI(selectedImageUri)

        }
    }
}