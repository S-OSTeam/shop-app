package com.example.deamhome.presentation.detail

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.deamhome.R
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.base.LoginDetectActivity
import com.example.deamhome.databinding.ActivityProductDetailBinding
import com.example.deamhome.presentation.auth.login.LoginActivity
import kotlinx.coroutines.launch

class ProductDetailActivity :
    LoginDetectActivity<ActivityProductDetailBinding>(R.layout.activity_product_detail) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tv.setOnClickListener {
            lifecycleScope.launch {
                DeamHomeApplication.container.authRepository.removeToken()
            }
        }
    }

    override fun onChangeAsAnonymousUser() {
        super.onChangeAsAnonymousUser()
        binding.tv.text = "익명임"
        Thread.sleep(3000L)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onChangeAsLoginUser() {
        super.onChangeAsLoginUser()
        binding.tv.text = "로그인 유저임"
    }
}
