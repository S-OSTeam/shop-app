package com.example.deamhome.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.deamhome.R
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.data.model.Token
import com.example.deamhome.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnLoginSuccess.setOnClickListener {
            lifecycleScope.launch {
                DeamHomeApplication.container.localAuthDataSource.updateToken(Token("tmp", "tmp"))
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
