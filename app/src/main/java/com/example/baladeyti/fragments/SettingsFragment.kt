package com.example.baladeyti.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baladeyti.R
import com.example.baladeyti.activities.DetailArticle
import com.example.baladeyti.components.ArticleAdapter
import com.example.baladeyti.components.MunicipalityAdapter
import com.example.baladeyti.models.Article
import com.example.baladeyti.models.Municipality
import com.example.baladeyti.services.ApiArticle
import com.example.baladeyti.services.ApiMunicipality
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsFragment  : Fragment(), ArticleAdapter.OnItemClickListener,MunicipalityAdapter.OnItemClickListener {
    lateinit var mSharedPref: SharedPreferences

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerView_b: RecyclerView
    var municipalityList: MutableList<Municipality> = arrayListOf()
    var articlesDispo: MutableList<Article> = arrayListOf()
    lateinit var test: ArticleAdapter.OnItemClickListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var searcha = ""
        test = this


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        recyclerView = view.findViewById(R.id.recycler_viewArticleList)
        recyclerView_b = view.findViewById(R.id.recycler_viewMunicipalityList)


        recyclerView.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        getNewsData { newss: List<Article> ->
            articlesDispo = newss as MutableList<Article>

            recyclerView.adapter = ArticleAdapter(newss, this)
        }
        mSharedPref = view.context!!.getSharedPreferences("UserPref", Context.MODE_PRIVATE)


        recyclerView_b.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.HORIZONTAL,false)
        recyclerView_b.setHasFixedSize(true)
        getNewsData_Municipality { newss: List<Municipality> ->
            municipalityList = newss as MutableList<Municipality>

            recyclerView_b.adapter = MunicipalityAdapter(newss, this)
        }
        mSharedPref = view.context!!.getSharedPreferences("UserPref", Context.MODE_PRIVATE)

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
    private fun getNewsData(callback: (List<Article>) -> Unit) {
        val apiInterface = ApiArticle.create()

        apiInterface.getArticles().enqueue(object: Callback<Article> {
            override fun onResponse(call: Call<Article>, response: Response<Article>) {
                if(response.code() == 200){
                    return callback(response.body()!!.articles!!)
                    Log.i("yessss", response.body().toString())
                    //}
                }else if(response.code() == 201){

                }else {
                    Log.i("nooooo", response.body().toString())
                }
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
    }


    private fun getNewsData_Municipality(callback: (List<Municipality>) -> Unit) {
        val apiInterface = ApiMunicipality.create()

        apiInterface.getMunicipalitys().enqueue(object: Callback<Municipality> {
            override fun onResponse(call: Call<Municipality>, response: Response<Municipality>) {
                if(response.code() == 200){
                    return callback(response.body()!!.municipalitys!!)
                    Log.i("yessss", response.body().toString())
                    //}
                }else if(response.code() == 201){

                }else {
                    Log.i("nooooo", response.body().toString())
                }
            }

            override fun onFailure(call: Call<Municipality>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
    }

    override fun onItemClick_a(position: Int, property: List<Article>) {

        val intent = Intent(activity, DetailArticle::class.java)
        intent.putExtra("designation",property[position].designation)
         intent.putExtra("text",property[position].text)
         intent.putExtra("_id",property[position]._id)
         intent.putExtra("date",property[position].date)
         intent.putExtra("photos",property[position].photos)
        startActivity(intent)

    }

    override fun onItemClick(position: Int, property: List<Municipality>) {
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