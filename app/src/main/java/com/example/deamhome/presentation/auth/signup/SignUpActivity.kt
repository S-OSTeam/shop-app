package com.example.deamhome.presentation.auth.signup

import android.os.Bundle
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.databinding.ActivitySignUpBinding

class SignUpActivity : BindingActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private val viewModel: SignUpViewModel by viewModels { SignUpViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.event.observe(this@SignUpActivity) { handleEvent(it) }
        viewModel.uiState.observe(this) {
            when {
                it.loading -> {}
                it.error -> {}
                else -> {}
            }
        }
    }

    private fun handleEvent(event: SignUpViewModel.Event) {
        when (event) {
            SignUpViewModel.Event.NetworkErrorEvent -> {
                Toaster.showShort(this@SignUpActivity, R.string.all_network_check_please_message)
            }
        }
    }
}
