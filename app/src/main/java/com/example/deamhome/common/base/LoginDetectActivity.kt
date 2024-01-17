package com.example.deamhome.common.base

import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.deamhome.app.DeamHomeApplication
import kotlinx.coroutines.launch

abstract class LoginDetectActivity<T : ViewDataBinding>(@LayoutRes layoutId: Int) :
    BindingActivity<T>(layoutId) {
    private var _isLatestLoginUser: Boolean? = null // UI가 기억하고 있는 마지막 로그인 여부 상태
    private val isLatestLoginUser: Boolean
        get() = _isLatestLoginUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState.containsKey(LOGIN_STATE_KEY)) {
            _isLatestLoginUser = savedInstanceState.getBoolean(LOGIN_STATE_KEY)
            Log.d("mendel", "복구 onCreate: $_isLatestLoginUser")
        }
        observeLoginState()
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { // onStart때 수집을 다시 시작, onStop때 수집 멈춤.
                DeamHomeApplication.isLogin.collect {
                    if (_isLatestLoginUser == null) { // 로그인 정보를 처음 받아오는 경우
                        when (it) {
                            true -> onStartAsLoginUser() // 이 액티비티가 시작 후 가장 먼저 로그인 여부를 조회했더니 로그인유저인 경우
                            false -> onStartAsAnonymousUser()
                        }
                    } else if (it != isLatestLoginUser) {
                        when (it) {
                            true -> onChangeAsLoginUser() // 기존에 익명유저 상태였는데 로그인상태로 변경된 경우
                            false -> onChangeAsAnonymousUser() // 기존에 로그인유저 상태였는데 익명상태로 변경된 경우
                        }
                    }
                    _isLatestLoginUser = it
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (_isLatestLoginUser != null) {
            outState.putBoolean(LOGIN_STATE_KEY, isLatestLoginUser)
        }
        super.onSaveInstanceState(outState)
    }

    // 이거는 그 화면의 UI 구성을 변경하는데만 사용할것. 로그인창으로 보내는거는 따로 직접 처리해야함.
    open fun onStartAsLoginUser() {
        Log.d("mendel", "${this::class.java}: onStartAsLoginUser")
    }

    open fun onStartAsAnonymousUser() {
        Log.d("mendel", "${this::class.java}: onStartAsAnonymousUser")
    }

    open fun onChangeAsLoginUser() {
        Log.d("mendel", "${this::class.java}: onChangeAsLoginUser")
    }

    open fun onChangeAsAnonymousUser() {
        Log.d("mendel", "${this::class.java}: onChangeAsAnonymousUser")
    }

    companion object {
        private const val LOGIN_STATE_KEY = "latest_login_state_key"
    }
}
