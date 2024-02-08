package com.example.deamhome.app

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import com.example.deamhome.BuildConfig
import com.example.deamhome.common.util.log
import timber.log.Timber

class DeamHomeApplication : Application() {
    private lateinit var container: DIContainer

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        container = DIContainer(this, deviceId)
        _instance = this
        // 디버그 모드로 빌드한 경우에만 팀버가 동작하도록
        if (BuildConfig.DEBUG_MODE) {
            Timber.plant(Timber.DebugTree())
            log(message = "deviceId: $deviceId")
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
