package com.example.baladeyti.fragments

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.baladeyti.R
import com.example.baladeyti.components.Pdf
import com.example.baladeyti.models.Extrait
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class ExtraitFragment : Fragment() {

    lateinit var mSharedPref: SharedPreferences
    lateinit var animationView: LottieAnimationView
    lateinit var animationViewArrow: LottieAnimationView
    lateinit var gender : String
    lateinit var lastName : String
    lateinit var firstName : String
    lateinit var address : String
    lateinit var civilStatus : String
    lateinit var phone : String
    lateinit var birthdate : String
    lateinit var cin : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_extrait, container, false)
        mSharedPref = view.context!!.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        gender = mSharedPref.getString("gender", "khaled@gmail.com").toString()
        lastName = mSharedPref.getString("lastName", "lastName").toString()
        firstName= mSharedPref.getString("firstName", "mabrouk").toString()
        birthdate= mSharedPref.getString("birthdate", "birthdate").toString()
        address= mSharedPref.getString("address", "address").toString()
        civilStatus = mSharedPref.getString("civilStatus", "address").toString()
        phone= mSharedPref.getString("phoneNumber", "phone").toString()
        cin = mSharedPref.getString("cin", "10000000").toString()
        val confirm = view.findViewById<Button>(R.id.confirm)

        animationView = view.findViewById(R.id.animationNoreponse)
        animationViewArrow = view.findViewById(R.id.animationArrow)

        animationView.playAnimation()
        animationView.loop(true)
        animationView.visibility = View.VISIBLE

        animationViewArrow.playAnimation()
        animationViewArrow.loop(true)
        animationViewArrow.visibility = View.VISIBLE

        confirm.setOnClickListener {
            requestStoragePermission()
            MotionToast.darkColorToast(
                requireActivity(),
                "Success ",
                "Birth Certificate Generated",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_TOP,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(
                    requireActivity(),
                    www.sanju.motiontoast.R.font.helvetica_regular
                )
            )
        }

        return view
    }


    private fun reportPDF () {
        //Dummy data
        var modelUmroh = Extrait (
            lastName,
            firstName,
            birthdate,
            gender,
            civilStatus,
            cin.toInt(),
            address,
            phone,

            )

        var pdf = Pdf(modelUmroh,requireContext());

        pdf.savePDF();

        Toast.makeText(context, "File generated", Toast.LENGTH_SHORT).show()

    }


    private fun requestStoragePermission()  {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        reportPDF()
                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) { // show alert dialog navigating to Settings

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(requireContext(), "Error occurred! ", Toast.LENGTH_SHORT)
                    .show()
            }
            .onSameThread()
            .check()
    }


}