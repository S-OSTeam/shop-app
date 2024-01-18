package com.example.deamhome.presentation.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.deamhome.R
import com.example.deamhome.common.base.LoginDetectFragment
import com.example.deamhome.databinding.FragmentHomeBinding

class HomeFragment : LoginDetectFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
