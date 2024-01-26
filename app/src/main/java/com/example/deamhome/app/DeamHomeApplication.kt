package com.example.deamhome.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.deamhome.BuildConfig
import com.example.deamhome.common.util.log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class DeamHomeApplication : Application() {
    private lateinit var container: DIContainer

    override fun onCreate() {
        super.onCreate()
        log("mendel", "application create")
        container = DIContainer(this)
        _instance = this
        log("mendel", "application create ${runBlocking { container.authRepository.isLogin.first() }}")
        // 디버그 모드로 빌드한 경우에만 팀버가 동작하도록
        if (BuildConfig.DEBUG_MODE) {
            Timber.plant(Timber.DebugTree())
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // 다크모드 일단은 방지
    }

    companion object {
        private lateinit var _instance: DeamHomeApplication

        // 안티 패턴이긴 하지만 빠른 개발 속도를 위해 열어놓음.
        val instance: DeamHomeApplication
            get() = _instance

        // 이건 나중에 힐트 적용하면 없어질 예정임.
        val container: DIContainer
            get() = instance.container
    }
}
