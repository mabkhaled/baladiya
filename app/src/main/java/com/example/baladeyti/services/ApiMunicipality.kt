package com.example.baladeyti.services

import com.example.baladeyti.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiMunicipality {

  /* if needed query params @Query("key") key: String*/



    @GET("municipalitys")
    fun getMunicipalitys(): Call<Municipality>



    companion object {
        fun create(): ApiMunicipality {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.30.3:3000")
                .build()
            return retrofit.create(ApiMunicipality::class.java)

        }
    }
/*    @POST("/users")
    fun createUser(@Body user: User): Response<CreateUserResponse>*/
}