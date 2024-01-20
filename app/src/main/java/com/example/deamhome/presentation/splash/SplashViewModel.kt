package com.example.deamhome.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.base.BaseViewModel
import com.example.deamhome.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import java.io.IOException

class SplashViewModel(authRepository: AuthRepository) : BaseViewModel() {
    val event: StateFlow<Event> =
        authRepository.isLogin.zip(authRepository.isLogin) { isLogin, isLatestVersion -> // 최신버전 정보를 위한 레포지토리 플로우 아직 안만들었음.
            delay(2000L)
            return@zip when {
                !isLatestVersion -> { // 로그인 여부보다 최신버전인지가 제알 중요한 정보임.
                    Event.NavigateToAppStore
                }

                !isLogin -> {
                    Event.NavigateToLogin
                }

                else -> {
                    Event.NavigateToMain
                }
            }
        }.catch {
            if (it is IOException || it is ApolloNetworkException) {
                _networkErrorEvent.emit(true)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, Event.None)

    val slashTimeOut: StateFlow<Boolean> =
        flowOf(true).map {
            delay(300L)
            it
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    sealed interface Event {
        data object None : Event
        data object NavigateToMain : Event
        data object NavigateToLogin : Event
        data object NavigateToAppStore : Event
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return SplashViewModel(
                    DeamHomeApplication.container.authRepository,
                ) as T
            }
        }
    }
}
