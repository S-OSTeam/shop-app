package com.example.deamhome.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.deamhome.R
import com.example.deamhome.common.base.LoginDetectActivity
import com.example.deamhome.databinding.ActivityMainBinding

class MainActivity : LoginDetectActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
