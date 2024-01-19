package com.example.deamhome.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BindingActivity<T : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    AppCompatActivity() {
    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this // 이거 없으면 xml이 데이터바인딩으로 관찰할 수 없음.
    }

    protected fun <T : Any> Flow<T>.observe(owner: LifecycleOwner, event: (T) -> Unit) {
        lifecycleScope.launch {
            this@observe.flowWithLifecycle(owner.lifecycle, Lifecycle.State.STARTED)
                .collect { event(it) }
        }
    }
}
