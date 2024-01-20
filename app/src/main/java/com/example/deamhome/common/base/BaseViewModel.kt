package com.example.deamhome.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel : ViewModel() {
    protected val _networkErrorEvent = MutableSharedFlow<Boolean>()
    val networkErrorEvent: SharedFlow<Boolean>
        get() = _networkErrorEvent.asSharedFlow()
}
