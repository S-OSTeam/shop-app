package com.example.deamhome.presentation.signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.databinding.ActivitySignUpBinding

class SignUpActivity : BindingActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private val viewModel: SignUpViewModel by viewModels { SignUpViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeUiState()
    }

    private fun observeUiState() {
        viewModel.networkErrorEvent.observe(this@SignUpActivity) {
            Toast.makeText(
                this@SignUpActivity,
                R.string.network_check_please_message,
                Toast.LENGTH_SHORT,
            ).show()
        }
        viewModel.uiState.observe(this) {
            when {
                it.loading -> {}
                it.error -> {}
                else -> {}
            }
        }
    }
}
