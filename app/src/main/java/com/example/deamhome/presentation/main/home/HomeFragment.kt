package com.example.deamhome.presentation.main.home

import android.os.Bundle
import android.view.View
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingFragment
import com.example.deamhome.databinding.FragmentHomeBinding

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
