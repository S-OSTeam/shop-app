package com.example.deamhome.presentation.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.deamhome.R
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.data.model.response.Token
import com.example.deamhome.databinding.ActivityLoginBinding
import com.example.deamhome.presentation.auth.signup.SignUpActivity
import com.example.deamhome.presentation.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private var moveToMain: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moveToMain = intent.getBooleanExtra(
            MOVE_MAIN_AFTER_LOGIN,
            false,
        ) // 액티비티 시작시 전달받은 인텐트는 회전을 해도 그대로 받음.
        binding.btnLoginSuccess.setOnClickListener {
            lifecycleScope.launch {
                // 나중에 뷰모델로 분리할거임.
                DeamHomeApplication.container.authRepository.updateToken(Token("tmp", "tmp"))
                // 로그인이 성공한다면,
                if (moveToMain) {
                    startActivity(MainActivity.getIntent(this@LoginActivity))
                }
                finish()
            }
        }

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }
    }

    companion object {
        private const val MOVE_MAIN_AFTER_LOGIN = "move_main_after_login_tag"
        fun getIntent(context: Context, moveToMain: Boolean = false): Intent {
            return Intent(context, LoginActivity::class.java).apply {
                putExtra(MOVE_MAIN_AFTER_LOGIN, moveToMain)
            }
        }
    }
}
