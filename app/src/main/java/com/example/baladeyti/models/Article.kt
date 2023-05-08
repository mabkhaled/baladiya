package com.example.baladeyti.models

data class Article(
    val _id: String,
    val addresse: List<Double>,
    val author: String,
    val date: String,
    val designation: String,
    val photos: String,
    val text: String,
    val articles: List<Article>?=null
)
data class ArticleRequest(
    var articles: List<Article>?=null,
    var msg: String?=null,
    var succes: String?=null,
    var response: String?=null,
    var idUser: String?=null,
)