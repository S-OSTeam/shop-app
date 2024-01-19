package com.example.deamhome.presentation.main

import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

@BindingAdapter("onNavigationItemSelected")
fun BottomNavigationView.bindOnNavigationItemSelectedListener(
    onFragmentChange: (ScreenType) -> Unit,
) {
    this.setOnItemSelectedListener { menuItem ->
        val fragmentType = ScreenType.of(menuItem.itemId)
        onFragmentChange(fragmentType)

        // 이게 참이어야 바텀탭 선택된 아이템 요소가 바뀐다.
        return@setOnItemSelectedListener fragmentType.isChangeBottomTab
    }
}
