package com.example.deamhome.presentation.main.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.databinding.ActivityCartBinding

class CartActivity : BindingActivity<ActivityCartBinding>(R.layout.activity_cart) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, CartActivity::class.java)
        }
    }
}
