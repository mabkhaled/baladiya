package com.example.baladeyti.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.baladeyti.R
import com.example.baladeyti.activities.AboutUsActivity
import com.example.baladeyti.activities.EditProfileActivity
import com.example.baladeyti.components.MunicipalityAdapter
import com.example.baladeyti.models.Municipality
import com.example.baladeyti.services.ApiMunicipality
import com.example.baladeyti.services.ApiUser
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment()/*,MunicipalityAdapter.OnItemClickListener */{
    lateinit var mSharedPref: SharedPreferences
    lateinit var animationView: LottieAnimationView
    private val favouritesFragment = FavouritesFragment()
    private val settingsFragment = SettingsFragment()
    lateinit var recyclerView: RecyclerView
    lateinit var complaintbtn: MaterialButton
    lateinit var newsbtn: MaterialButton
    lateinit var nav_who: MaterialButton
    var filtredArticle: MutableList<Municipality> = arrayListOf()
    var articlesDispo: MutableList<Municipality> = arrayListOf()
    lateinit var test: MunicipalityAdapter.OnItemClickListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var searcha = ""
        /*test = this*/


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        complaintbtn = view.findViewById(R.id.complaintbtn)
        newsbtn = view.findViewById(R.id.newsbtn)
        nav_who = view.findViewById(R.id.nav_who)
        animationView = view.findViewById(R.id.animationNoreponse)


        animationView.playAnimation()
        animationView.loop(true)
        animationView.visibility = View.VISIBLE

        complaintbtn.setOnClickListener {
            makeCurrentFragement(favouritesFragment)
        }
        newsbtn.setOnClickListener {
            makeCurrentFragement(settingsFragment)
        }
        nav_who.setOnClickListener {
            Intent(activity, AboutUsActivity::class.java).also {
                startActivity(it)
            }
        }
//        recyclerView = view.findViewById(R.id.recycler_viewArticleList)
//
//        recyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
//        recyclerView.setHasFixedSize(true)
//        getNewsData { newss: List<Municipality> ->
//            articlesDispo = newss as MutableList<Municipality>
//
//            recyclerView.adapter = MunicipalityAdapter(newss, this)
//        }
//        mSharedPref = view.context!!.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
//
//        if (!mSharedPref.getBoolean("isVerified", false)) {
//            val apiInterfacee = ApiMunicipality.create()
//
//            apiInterfacee.getMunicipalitys()
//                .enqueue(object : Callback<Municipality> {
//                    override fun onResponse(call: Call<Municipality>, response: Response<Municipality>) {
//                        if (response.isSuccessful) {
//                            println("Ena list"+response);
//                            //}
//                        } else {
//                            Log.i("nooooo", response.body().toString())
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Municipality>, t: Throwable) {
//                        t.printStackTrace()
//                        println("OnFailure")
//                    }
//
//                })
//
//        }





        /*keyword.setOnFocusChangeListener(View.OnFocusChangeListener { view, focused ->
            val keyboard =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (focused) keyboard.showSoftInput(keyword, 0) else keyboard.hideSoftInputFromWindow(
                keyword.getWindowToken(),
                0
            )
        })*/
       /* keyword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    filtredArticle.clear()
                    articlesDispo.forEach {
                        if (it.nom!!.contains(s)) {
                            filtredArticle.add(it)
                        }
                    }
                    view.recyclerView.adapter = ArticleViewAdapter(filtredArticle, test)
                }
                if(s.isEmpty()){
                    view.recyclerView.adapter = ArticleViewAdapter(articlesDispo, test)
                }
                searcha = s.toString()
            }
        })*/


        return view
    }
//    private fun getNewsData(callback: (List<Municipality>) -> Unit) {
//        val apiInterface = ApiMunicipality.create()
//
//        apiInterface.getMunicipalitys().enqueue(object: Callback<Municipality> {
//            override fun onResponse(call: Call<Municipality>, response: Response<Municipality>) {
//                if(response.code() == 200){
//                    return callback(response.body()!!.municipalitys!!)
//                    Log.i("yessss", response.body().toString())
//                    //}
//                }else if(response.code() == 201){
//
//                }else {
//                    Log.i("nooooo", response.body().toString())
//                }
//            }
//
//            override fun onFailure(call: Call<Municipality>, t: Throwable) {
//                t.printStackTrace()
//                println("OnFailure")
//            }
//
//        })
//    }
///*    private fun showAlertDialog(){
//
//        val builder = AlertDialog.Builder(view!!.context)
//        builder.setTitle("Alert")
//        builder.setMessage("Verifier d'abord votre compte avec l'email envoyé!")
//
//        builder.show()
//    }*/
//    override fun onItemClick(position: Int, articles: List<Municipality>) {
//     //Intent
//       /* val intent = Intent(activity, DetailArticle::class.java)*/
//       /* intent.putExtra("nom",articles[position].nom)
//        intent.putExtra("addresse",articles[position].addresse)
//        intent.putExtra("_id",articles[position]._id)
//        intent.putExtra("description",articles[position].description)
//        intent.putExtra("type",articles[position].type)
//        intent.putExtra("photo",articles[position].photo)
//        intent.putExtra("userArticleNom", articles[position].user?.nom)
//        intent.putExtra("userArticlePrenom", articles[position].user?.prenom)
//        intent.putExtra("userArticlePhoto", articles[position].user?.photoProfil)
//        intent.putExtra("userArticleEmail", articles[position].user?.email)
//        intent.putExtra("userDetail", articles[position].user?._id)
//        intent.putExtra("question", articles[position].question?._id)
//        intent.putExtra("questionTitle", articles[position].question?.titre)*/
//     /*   startActivity(intent)*/
//    }



    private fun makeCurrentFragement(fragment: Fragment) {
        if (fragment != null) {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fl_wrapper, fragment)
            transaction?.addToBackStack(null);
            transaction?.commit()
        }

    }
}