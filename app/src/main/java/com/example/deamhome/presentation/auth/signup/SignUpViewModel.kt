package com.example.deamhome.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.domain.model.SignUpInfo
import com.example.deamhome.domain.repository.AuthRepository
import com.example.deamhome.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event>
        get() = _event

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    val id: MutableStateFlow<String> = MutableStateFlow("")
    val isValidateId: StateFlow<Boolean> =
        id.map {
            kotlin.runCatching { SignUpInfo.validateIdFormat(it) }
                .getOrNull() ?: false
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun fetchSignUp() {
        viewModelScope.launch {
            if (_uiState.value.loading) return@launch
            _uiState.update { it.copy(loading = true) }
            if (!checkInputInfo()) return@launch _uiState.update { it.copy(loading = false) }
        }
    }

    private fun checkInputInfo(): Boolean {
        return true
    }

    data class UiState(
        val loading: Boolean = false,
        val error: Boolean = false,
        val signUpSuccess: Boolean = false,
    )

    sealed interface Event {
        data object NetworkErrorEvent : Event
        data object SignUpErrorEvent : Event
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return SignUpViewModel(
                    DeamHomeApplication.container.productRepository,
                    DeamHomeApplication.container.authRepository,
                ) as T
            }
        }
    }
}
