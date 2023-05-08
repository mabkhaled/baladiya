package com.example.baladeyti.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.baladeyti.R

class DetailArticle : AppCompatActivity() {

    lateinit var topic: TextView
    lateinit var photos: ImageView
    lateinit var content: TextView
    lateinit var date: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)

        supportActionBar?.hide()
        topic = findViewById(R.id.topic)
        content = findViewById(R.id.content)
        photos = findViewById(R.id.imageView)
        date = findViewById(R.id.date)
        topic.text = intent.getStringExtra("designation")
        content.text = intent.getStringExtra("text")
        date.text = intent.getStringExtra("date")!!.split("T")[0]

        val picStr: String = intent.getStringExtra("photos")!!
        println(picStr)
        val picStrr="http://192.168.1.3:3000/upload/"+picStr.split("/")[4]
        Glide.with(this).load(Uri.parse(picStrr)).into(photos)

    }
}