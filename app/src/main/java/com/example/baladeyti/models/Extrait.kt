package com.example.baladeyti.models

data class Extrait(
    var lastName: String? = null,
    var firstName: String? = null,
    var birthdate: String? = null,
    val gender: String? = null,
    var civilStatus: String? = null,
    var cin: Number? = 1000000,
    var address: String? = null,
    var phoneNumber : String? = null

)
