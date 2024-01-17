package com.example.deamhome.presentation.splash

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.deamhome.R
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.presentation.login.LoginActivity
import com.example.deamhome.presentation.main.MainActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var splashScreen: SplashScreen
    private var hasLoginToken: Boolean? = null
    private var isLatestVersion: Boolean? = null
    lateinit var content: View

    private val viewTreeObserver = object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            return if (hasLoginToken != null && isLatestVersion != null) {
                content.viewTreeObserver.removeOnPreDrawListener(this)
                if (!isLatestVersion!!) {
                    // 앱 다운 받는 곳으로 보내버리기 위한 문구와 함께 다이얼로그 띄워주기
                    // 다이얼로그에서 확인이나 취소 누르면 이 스플래시 액티비티는 종료시켜야 한다.
                } else {
                    if (!hasLoginToken!!) { // 토큰 불통과돼서 로그인 해야 하는 경우.
                        startActivity(LoginActivity.getIntent(this@SplashActivity))
                    } else {
                        startActivity(MainActivity.getIntent(this@SplashActivity))
                    }
                    finish()
                }
                true
            } else {
                // The content is not ready; suspend.
                false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        setContentView(R.layout.activity_splash)
        content = findViewById(android.R.id.content)
        checkAppState()
        content.viewTreeObserver.addOnPreDrawListener(viewTreeObserver)
    }

    private fun checkAppState() {
        // 앱 초기 검사 요소들은 병렬적으로 수행시키기
        lifecycleScope.launch {
            isLatestVersion = async { checkAppVersion() }.await()
            hasLoginToken = async { checkLoginToken() }.await()
        }
    }

    private suspend fun checkAppVersion(): Boolean {
        delay(2000L) // 임시로 줬음. 나중에 제거할 예정
        return true
    }

    private suspend fun checkLoginToken(): Boolean {
        // 로그인 여부만 검사
        return DeamHomeApplication.isLogin.first() // 지금은 그냥 토큰 있으면 바로 메인으로 보내버림. 나중엔 유효성 검사 추가해야함.
    }
}
