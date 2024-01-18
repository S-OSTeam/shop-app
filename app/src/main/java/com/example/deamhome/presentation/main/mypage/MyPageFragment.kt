package com.example.deamhome.presentation.main.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.deamhome.R
import com.example.deamhome.common.base.LoginDetectFragment
import com.example.deamhome.databinding.FragmentMyPageBinding

class MyPageFragment : LoginDetectFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPageFragment()
    }
}
