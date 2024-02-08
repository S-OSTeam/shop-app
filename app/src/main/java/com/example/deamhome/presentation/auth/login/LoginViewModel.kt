package com.example.deamhome.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.util.log
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.AuthRepository
import com.example.deamhome.model.type.SNS
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    val id: MutableStateFlow<String> = MutableStateFlow("")
    val pwd: MutableStateFlow<String> = MutableStateFlow("")

    init {
        viewModelScope.launch {
            id.collect {
                log(tag = "id", message = "id: ${id.value}")
            }
        }

        viewModelScope.launch {
            pwd.collect {
                log(tag = "id", message = "pwd: ${pwd.value}")
            }
        }
    }

    fun login() {
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        val id = id.value.trim()
        val pwd = pwd.value.trim()
        viewModelScope.launch {
            if (id.isBlank() || pwd.isBlank()) {
                _event.emit(Event.InputBlank)
                _isLoading.value = false
                return@launch
            }

            when (val response = authRepository.login(id, pwd, SNS.NORMAL)) {
                is ApiResponse.Success -> {
                    _event.emit(Event.LoginSuccess)
                }

                is ApiResponse.Failure -> {
                    _event.emit(
                        Event.LoginFailed(
                            response.errors?.firstOrNull()?.message ?: "로그인 에러",
                        ),
                    )
                }

                else -> {}
            }
            _isLoading.value = false
        }
    }

    sealed interface Event {
        data class LoginFailed(val message: String) : Event
        data object LoginSuccess : Event
        data object InputBlank : Event
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return LoginViewModel(
                    DeamHomeApplication.container.authRepository,
                ) as T
            }
        }
    }
}
