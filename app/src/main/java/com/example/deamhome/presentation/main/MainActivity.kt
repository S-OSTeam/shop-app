package com.example.deamhome.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.deamhome.R
import com.example.deamhome.common.base.LoginDetectActivity
import com.example.deamhome.databinding.ActivityMainBinding
import com.example.deamhome.presentation.detail.ProductDetailActivity

class MainActivity : LoginDetectActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tv.setOnClickListener {
            startActivity(Intent(this, ProductDetailActivity::class.java))
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
