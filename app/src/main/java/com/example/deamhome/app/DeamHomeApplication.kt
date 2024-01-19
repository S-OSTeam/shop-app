package com.example.deamhome.app

import android.app.Application
import com.example.deamhome.BuildConfig
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class DeamHomeApplication : Application() {
    private lateinit var container: DIContainer

    override fun onCreate() {
        super.onCreate()
        container = DIContainer(this)
        _instance = this
        _isLogin = container.isLogin
        // 디버그 모드로 빌드한 경우에만 팀버가 동작하도록
        if (BuildConfig.DEBUG_MODE) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        private lateinit var _instance: DeamHomeApplication

        // 안티 패턴이긴 하지만 빠른 개발 속도를 위해 열어놓음.
        val instance: DeamHomeApplication
            get() = _instance
        val container: DIContainer
            get() = instance.container

        // 만약 로그인 정보에 대한 관찰이 필요하다면, 꼭 AuthRepository를 주입받지 않아도 사용할 수 있도록 열어둠.
        private lateinit var _isLogin: Flow<Boolean>
        val isLogin: Flow<Boolean>
            get() = _isLogin
    }
}
