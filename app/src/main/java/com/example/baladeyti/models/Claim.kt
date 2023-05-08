package com.example.baladeyti.models

data class Claim(
    var _id: String? = null,
    var addresse: List<Double>? = null,
    var author: String? = null,
    var date: String? = null,
    var designation: String? = null,
    var name: String? = null,
    var photos: String? = null,
    var text: String? = null,
    var laltitude: String? = null,
    var longitude: String? = null,
    val claims: List<Claim>?=null
)
data class ClaimRequest(
    var claim: Claim?=null,
    var msg: String?=null,
    var succes: String?=null,
    var response: String?=null,
    var idUser: String?=null,
)