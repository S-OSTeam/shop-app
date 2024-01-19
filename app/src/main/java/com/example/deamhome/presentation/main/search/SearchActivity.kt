package com.example.deamhome.presentation.main.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.databinding.ActivitySearchBinding

class SearchActivity : BindingActivity<ActivitySearchBinding>(R.layout.activity_search) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }
}
