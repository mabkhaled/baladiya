package com.example.baladeyti.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.baladeyti.R

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        supportActionBar?.hide()
    }
}