package com.example.deamhome.presentation.login

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.deamhome.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var splashScreen: SplashScreen
    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            for (i in 1..100) {
                delay(1000)
            }
            // 버전 정보 체크, 토큰 유효성 검사
            isReady = true
        }
        // Set up an OnPreDrawListener to the root view.
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (isReady) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        // 버전 불통과면 여기서 버전 관련 다이얼로그 띄우기.
                        // 버전 통과인데 토큰 불통과면 메인으로 넘기지 않음.
                        if (isReady) {
                        }
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            },
        )

//        findViewById<TextView>(R.id.tv_main).setOnClickListener {
//            GlobalScope.launch {
//                val response =
//                    ApolloClient.Builder().serverUrl(BuildConfig.SERVER_URL)
//                        .build()
//                        .mutation(TestMutation()).execute()
//                runOnUiThread {
//                    if (!response.hasErrors()) {
//                        findViewById<TextView>(R.id.tv_main).text = response.data?.test
//                    }
//                }
//            }
//        }
    }
}
