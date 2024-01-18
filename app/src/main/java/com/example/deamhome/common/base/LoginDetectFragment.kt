package com.example.deamhome.common.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

abstract class LoginDetectFragment<T : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    BindingFragment<T>(layoutRes) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    // 액티비티의 콜백이 먼저 호출된 다음 프래그먼트의 이 콜백이 호출된다.
    // 이거는 화면의 UI 변경하는데만 사용할것. 로그인창으로 보내는거는 따로 직접 처리해야함.
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
}
