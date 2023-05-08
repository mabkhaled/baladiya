package com.example.baladeyti.services

import com.example.baladeyti.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiUser {

  /* if needed query params @Query("key") key: String*/

    @POST("/users/forgotPassword")
    fun sendResetCode(
        @Body emailAddress: UserReset
    ):Call<UserResetResponse>



    @POST("users/resetPassword")
    fun resetPassword(
        @Body user: UserRequest
    ):Call<UserRequest>

    @GET("users")
    fun getUsers(): Response<List<User>>

    @POST("users/login")
    fun userLogin(
        @Body user: User
    ): Call<UserAndToken>


    @Multipart
    @POST("users")
    fun userSignUp(
        @PartMap data : LinkedHashMap<String, RequestBody>,
    ) : Call<User>

    @Multipart
    @PATCH("users/{id}")
    fun userUpdate(
        @Path("id") id:String,
        @PartMap data : LinkedHashMap<String, RequestBody>,
        @Part profilePicture: MultipartBody.Part
    ) : Call<userUpdateResponse>

    companion object {
        fun create(): ApiUser {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.1.3:3000")
                .build()
            return retrofit.create(ApiUser::class.java)

        }
    }
/*    @POST("/users")
    fun createUser(@Body user: User): Response<CreateUserResponse>*/
}