package com.example.goodmerchant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
       moveTaskToBack(true)
    }

}