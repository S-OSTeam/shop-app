package com.example.deamhome.presentation.detail

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.deamhome.R
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.databinding.ActivityProductDetailBinding
import kotlinx.coroutines.launch

class ProductDetailActivity :
    BindingActivity<ActivityProductDetailBinding>(R.layout.activity_product_detail) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tv.setOnClickListener {
            lifecycleScope.launch {
                DeamHomeApplication.container.authRepository.removeToken()
            }
        }
    }
}
