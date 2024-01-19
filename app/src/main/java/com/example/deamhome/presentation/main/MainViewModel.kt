package com.example.deamhome.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.deamhome.common.livedata.SingleLiveEvent

class MainViewModel : ViewModel() {
    private val _curScreen: MutableLiveData<ScreenType.ChangeScreenType> =
        MutableLiveData(ScreenType.ChangeScreenType.Home)
    val curScreen: LiveData<ScreenType.ChangeScreenType>
        get() = _curScreen

    private val _curPopScreenEvent: SingleLiveEvent<ScreenType.PopScreenType> = SingleLiveEvent()
    val curPopScreenEvent: LiveData<ScreenType.PopScreenType>
        get() = _curPopScreenEvent

    val changeScreen = { screenType: ScreenType ->
        if (screenType is ScreenType.ChangeScreenType) {
            if (_curScreen.value != screenType) _curScreen.value = screenType
        } else if (screenType is ScreenType.PopScreenType) {
            _curPopScreenEvent.value = screenType
        }
    }
}
