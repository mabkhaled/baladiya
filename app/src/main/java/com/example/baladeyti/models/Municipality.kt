package com.example.baladeyti.models

data class Municipality(
    val _id: String?=null,
    val address: String?=null,
    val emailAddress: String?=null,
    val name: String?=null,
    val phoneNumber: Int?=null,
    val workSchedule: Any?=null,
    val photos: String?=null,
    val municipalitys: List<Municipality>?=null
)