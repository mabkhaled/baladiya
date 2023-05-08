package com.example.baladeyti.models

data class User(
    var _id: String? = null,
    var lastName: String? = null,
    var firstName: String? = null,
    var emailAddress: String? = null,
    var birthdate: String? = null,
    val gender: String? = null,
    var civilStatus: String? = null,
    var cin: Number? = 100000,
    var password: String? = null,
    var photos: String? = null,
    var address: String? = null,
    var claims: List<Any>? = null,
    var Municipalitys: List<Any>? = null,
    var role: String? = null,
    var token : String? = null,
    var message : String? = null,
    var phoneNumber : String? = null
)

data class UserAndToken(
    var user: User? = null,
    var token: String? = null

)
data class userSignUpResponse(
    var user : User? = null,
)
data class userUpdateResponse(
    var user : User? = null,
)

data class UserResetResponse(
    var token: String?=null,
    var msg: String?=null,
    var succes: String?=null,
    var response: String?=null,
    var user : User? = null,
)

data class UserRequest(
    var token: String?=null,
    var msg: String?=null,
    var succes: String?=null,
    var response: String?=null,
    var emailAddress: String?=null,
    var password: String?=null,
)
data class UserReset(
    var emailAddress: String?=null,

)