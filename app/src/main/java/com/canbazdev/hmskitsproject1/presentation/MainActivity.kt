package com.canbazdev.hmskitsproject1.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.canbazdev.hmskitsproject1.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(intent.getStringExtra("accountName"))
        setContentView(R.layout.activity_main)
    }
}