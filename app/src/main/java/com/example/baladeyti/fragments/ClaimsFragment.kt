package com.example.baladeyti.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.baladeyti.R
import com.example.baladeyti.components.ClaimAdapter
import com.example.baladeyti.models.Claim
import com.example.baladeyti.services.ApiClaim
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClaimsFragment  : Fragment(), ClaimAdapter.OnItemClickListener {
    //lateinit var mSharedPref: SharedPreferences
    private lateinit var _id: String
    lateinit var animationView: LottieAnimationView
    lateinit var textNotFound: TextView
    lateinit var recyclerView: RecyclerView
    var claimList: MutableList<Claim> = arrayListOf()
    lateinit var test: ClaimAdapter.OnItemClickListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        test = this
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_claims, container, false)
        recyclerView = view.findViewById(R.id.recycler_viewClaimList)
        animationView = view.findViewById(R.id.animationNoreponse)

        //mSharedPref = view.context!!.getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        //_id = mSharedPref.getString("id", "my id").toString()

        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        getNewsData { newss: List<Claim> ->
            claimList = newss as MutableList<Claim>
            textNotFound.text = "nothing was found"
           if (newss.isEmpty()){
                animationView.visibility = View.VISIBLE
                animationView.playAnimation()
                animationView.loop(true)

            }else {
                recyclerView.adapter = ClaimAdapter(newss, this,savedInstanceState,view.context)
            }

        }

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
    private fun getNewsData(callback: (List<Claim>) -> Unit) {
        val apiInterface = ApiClaim.create()
        apiInterface.getClaims().enqueue(object: Callback<Claim> {
            override fun onResponse(call: Call<Claim>, response: Response<Claim>) {
                if(response.code() == 200){
                    return callback(response.body()!!.claims!!)
                    Log.i("yessss", response.body().toString())
                    }
                else if(response.code() == 201){
                    Log.i("ahhhh", response.body().toString())
                }else {
                    Log.i("nooooo", response.body().toString())
                }
            }

            override fun onFailure(call: Call<Claim>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
    }



    override fun onItemClick(position: Int, property: List<Claim>) {
        println("yo")
    }
    /*    private fun showAlertDialog(){
            val builder = AlertDialog.Builder(view!!.context)
            builder.setTitle("Alert")
            builder.setMessage("Verifier d'abord votre compte avec l'email envoyé!")
            builder.show()
        }*/
//    override fun onItemClick(position: Int, articles: List<Article>) {
//        //Intent
//        /* val intent = Intent(activity, DetailArticle::class.java)*/
//        /* intent.putExtra("nom",articles[position].nom)
//         intent.putExtra("addresse",articles[position].addresse)
//         intent.putExtra("_id",articles[position]._id)
//         intent.putExtra("description",articles[position].description)
//         intent.putExtra("type",articles[position].type)
//         intent.putExtra("photo",articles[position].photo)
//         intent.putExtra("userArticleNom", articles[position].user?.nom)
//         intent.putExtra("userArticlePrenom", articles[position].user?.prenom)
//         intent.putExtra("userArticlePhoto", articles[position].user?.photoProfil)
//         intent.putExtra("userArticleEmail", articles[position].user?.email)
//         intent.putExtra("userDetail", articles[position].user?._id)
//         intent.putExtra("question", articles[position].question?._id)
//         intent.putExtra("questionTitle", articles[position].question?.titre)*/
//        /*   startActivity(intent)*/
//    }
}