package com.example.deamhome.presentation.main.mypage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.deamhome.R
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.base.LoginDetectFragment
import com.example.deamhome.common.view.showSnackbar
import com.example.deamhome.databinding.FragmentMyPageBinding
import com.example.deamhome.presentation.auth.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MyPageFragment : LoginDetectFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            if (DeamHomeApplication.isLogin.first()) {
                initLoginUser()
            } else {
                initAnonymousUser()
            }
        }
    }

    private fun initLoginUser() {
        binding.tvUserName.text = "고명진"
        binding.tvUserName.setOnClickListener {
            binding.tvUserName.showSnackbar("로그아웃됨", "확인")
            viewLifecycleOwner.lifecycleScope.launch {
                DeamHomeApplication.container.authRepository.removeToken()
            }
        }
    }

    private fun initAnonymousUser() {
        binding.tvUserName.text = "로그인해주세요"
        binding.tvUserName.setOnClickListener {
            startActivity(LoginActivity.getIntent(requireContext()))
        }
    }

    override fun onChangeAsAnonymousUser() {
        super.onChangeAsAnonymousUser()
        initAnonymousUser()
    }

    override fun onChangeAsLoginUser() {
        super.onChangeAsLoginUser()
        initLoginUser()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyPageFragment()
    }
}
