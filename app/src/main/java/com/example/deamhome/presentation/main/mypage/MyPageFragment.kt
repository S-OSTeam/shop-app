package com.example.deamhome.presentation.main.mypage

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.deamhome.R
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.base.BindingFragment
import com.example.deamhome.common.util.log
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.common.view.showSnackbar
import com.example.deamhome.databinding.FragmentMyPageBinding
import com.example.deamhome.presentation.auth.login.LoginActivity
import kotlinx.coroutines.launch

class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels { MyPageViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.uiState.observe(this) {
            log("mendel", "uiState observe $it")
            // UI State로는 UI 데이터변경 혹은 리스너 수정을 해주면 된다. 토스트메시지를 띄우거나 화면을 넘기거나 이런 팝업 같이 돌아왔을때 실행될 이유 없는 애들은 Event로 빼라
            binding.pb.isVisible = it.isLoading
            binding.tvError.isVisible = it.isError
            binding.tvUserName.text = it.data.name
            if (it.isLogin) {
                binding.tvUserName.setOnClickListener {
                    binding.tvUserName.showSnackbar("로그아웃됨", "확인")
                    viewLifecycleOwner.lifecycleScope.launch {
                        // 이벤트 실행시킨거임.
                        DeamHomeApplication.container.authRepository.removeToken()
                    }
                }
            } else {
                binding.tvUserName.setOnClickListener {
                    startActivity(LoginActivity.getIntent(requireContext()))
                }
            }
        }

        binding.tvMayPageState.setOnClickListener {
            viewModel.fetchUserProfile()
        }

        viewModel.event.observe(this) {
            when (it) {
                MyPageViewModel.Event.ChangeAsAnonymousUser -> {}
                MyPageViewModel.Event.ChangeAsLoginUser -> {
                    Toaster.showShort(requireContext(), "환영합니다")
                }

                MyPageViewModel.Event.NavigateToLogin -> {
                    startActivity(LoginActivity.getIntent(requireContext()))
                }

                MyPageViewModel.Event.NetworkErrorEvent -> {}
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyPageFragment()
    }
}
