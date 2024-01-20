package com.example.deamhome.presentation.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.databinding.ActivitySplashBinding
import com.example.deamhome.presentation.auth.login.LoginActivity
import com.example.deamhome.presentation.main.MainActivity

class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels { SplashViewModel.Factory }
    private var isSplashOut = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { isSplashOut }
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.slashTimeOut.observe(this) {
            isSplashOut = it
        }
        viewModel.event.observe(this) {
            when (it) {
                SplashViewModel.Event.NavigateToAppStore -> {
                    finish()
                }

                SplashViewModel.Event.NavigateToLogin -> {
                    startActivity(LoginActivity.getIntent(this@SplashActivity, true))
                    finish()
                }

                SplashViewModel.Event.NavigateToMain -> {
                    startActivity(MainActivity.getIntent(this@SplashActivity))
                    finish()
                }

                SplashViewModel.Event.None -> {}
            }
        }
    }
}
