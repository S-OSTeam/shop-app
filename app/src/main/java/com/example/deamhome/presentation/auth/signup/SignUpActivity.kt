package com.example.deamhome.presentation.auth.signup

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.util.log
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.databinding.ActivitySignUpBinding

class SignUpActivity : BindingActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private val viewModel: SignUpViewModel by viewModels { SignUpViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserve()
        binding.etvId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: return
                if (text.length == 20) {
                    binding.tilId.error = "최대 길이 도달"
                } else {
                    binding.tilId.error = null
                }
            }
        })

        binding.etvPwdConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: return
                if (binding.etvPwd.text.toString() != text && text.isNotBlank()) {
                    binding.tilPwdConfirm.error = "비밀번호를 다시 한 번 확인해주세요"
                } else {
                    binding.tilPwdConfirm.error = null
                }
            }
        })

        if (savedInstanceState == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.tilNickname.requestFocus()
                val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(binding.tilNickname, 0)
            }, 100)
        }

        binding.etvEmailConfirm.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                log(tag = "mendel", "hi1")
//                binding.etvEmailConfirm.setText(binding.etvEmailConfirm.toString() + "a")
//                Handler(Looper.getMainLooper()).postDelayed({
//                    binding.etvEmailConfirm.setText(
//                        binding.etvEmailConfirm.toString().removeSuffix("a"),
//                    )
//                }, 100)
            }
        }
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

            else -> {}
        }
    }
}
