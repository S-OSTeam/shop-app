package com.example.deamhome.presentation.auth.login

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.databinding.ActivityLoginBinding
import com.example.deamhome.presentation.auth.signup.SignUpActivity
import com.example.deamhome.presentation.main.MainActivity

class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private var moveToMain: Boolean = false
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        moveToMain = intent.getBooleanExtra(
            MOVE_MAIN_AFTER_LOGIN,
            false,
        ) // 액티비티 시작시 전달받은 인텐트는 회전을 해도 그대로 받음.

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        viewModel.event.observe(this) { handleEvent(it) }
    }

    private fun handleEvent(event: LoginViewModel.Event) {
        when (event) {
            is LoginViewModel.Event.LoginFailed -> {
                binding.tilId.error = "에러"
                binding.tilPwd.error = "에러"
                binding.tvLoginError.text = event.message
            }

            LoginViewModel.Event.LoginSuccess -> {
                binding.tilId.error = ""
                binding.tilPwd.error = ""
                binding.tvLoginError.text = ""
                if (moveToMain) {
                    startActivity(MainActivity.getIntent(this@LoginActivity))
                }
                finish()
            }

            LoginViewModel.Event.InputBlank -> {
                binding.tilId.error = "에러"
                binding.tilPwd.error = "에러"
                binding.tvLoginError.text = "모두 입력해주세요"
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) { // 이 액티비티의 윈도우 안에 포커싱 받고 있는 뷰가 없다면 걍 넘김
            val rect = Rect()
            // 이 뷰가 보이는 사각 좌표 영역을 글로벌 좌표 기준으로 얻어옴
            // 여기서 글로벌 좌표 기준이란 것은 이 뷰가 속한 최상단 뷰그룹안에서의 좌푯값을 말한다.
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt() // 터치된 지점의 좌표 정보를 얻음.
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) { // 이 뷰의 좌표 사각 영역 안에 터치 지점이 포함되지 않는다면
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0) // 키보드를 숨기고
                focusView.clearFocus() // 포커싱을 해제해줘라.
            }
        }
        return super.dispatchTouchEvent(ev) // 따로 터치 처리와 관련된 것은 관여하지 않기 위해 그대로 부모를 호출해서 반환한다.
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
