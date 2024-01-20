package com.example.deamhome.common.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.util.log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class LoginDetectFragment<T : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    BindingFragment<T>(layoutRes) {
    private var _isLatestLoginUser: Boolean? = null // UI가 기억하고 있는 마지막 로그인 여부 상태
    private val isLatestLoginUser: Boolean
        get() = _isLatestLoginUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState.containsKey(LOGIN_STATE_KEY)) {
            _isLatestLoginUser = savedInstanceState.getBoolean(LOGIN_STATE_KEY)
        }
    }

    // 프래그먼트 뷰가 처음 초기화되는 상황에서의 분기점 역할만 해주면 됨. 즉, 이 프래그먼트가 생기고 딱 처음에만
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (_isLatestLoginUser == null) {
            viewLifecycleOwner.lifecycleScope.launch {
                _isLatestLoginUser = DeamHomeApplication.container.isLogin.first()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (_isLatestLoginUser != null) {
            outState.putBoolean(LOGIN_STATE_KEY, isLatestLoginUser)
        }
    }

    // 프래그먼트가 변화를 감지한다는 것은 곧 이 프래그먼트의 부모도 변화를 감지한다는 것임.
    open fun onChangeAsLoginUser() {
        log("onChangeAsLoginUser")
        _isLatestLoginUser = true
        childFragmentManager.fragments.mapNotNull { it as? LoginDetectFragment<*> }
            .forEach { it.onChangeAsLoginUser() }
    }

    open fun onChangeAsAnonymousUser() {
        log("onChangeAsAnonymousUser")
        _isLatestLoginUser = false
        childFragmentManager.fragments.mapNotNull { it as? LoginDetectFragment<*> }
            .forEach { it.onChangeAsAnonymousUser() }
    }

    companion object {
        private const val LOGIN_STATE_KEY = "latest_login_state_key"
    }
}
