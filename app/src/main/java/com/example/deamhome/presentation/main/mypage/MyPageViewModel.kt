package com.example.deamhome.presentation.main.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.util.log
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.domain.repository.AuthRepository
import com.example.deamhome.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event.asSharedFlow()

    // uiState가 위에 와야한다.
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    // 여기 별도로 저장해놔야 액티비티가 재개될때 profileState가 다시 별도의 네트워크 요청없이 수집할 수 있음.
    private val profile: MutableStateFlow<UserProfile?> = MutableStateFlow(null)

    // 로그인 여부가 바뀌면 자동으로 profileState가 변경된다.
    private val profileState =
        combine(profile, authRepository.isLogin) { profile, isLogin ->
            log("mendel", "combine $profile, $isLogin")
            delay(3000L)
            if (profile == null) return@combine
            when (isLogin) {
                true -> _uiState.update { uiState ->
                    uiState.copy(data = UserProfile(profile.name, profile.profile), isLogin = true)
                }

                false -> {
                    _uiState.update { uiState ->
                        uiState.copy(
                            data = ANONYMOUS_USER_PROFILE,
                            isLogin = false,
                        )
                    }
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, Unit)
    // Eagerly라서 관찰자가 없어도 계속 관찰한다, 어차피 얘가 uiState를 바꿔도, 얘를 관찰하는 액티비티가 onStop이후엔 처리안하고 돌아와서 처리한다.

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            if (_uiState.value.isLoading && profile.value != null) return@launch
            _uiState.update { it.copy(isLoading = true) }
            delay(2000L)
            when (val response = userRepository.fetchUserProfile()) {
                is ApiResponse.Success -> {
                    if (response.body != null) {
                        profile.update { response.body }
                    }
                    _uiState.update { it.copy(isLoading = false, isError = false) }
                }

                is ApiResponse.Failure -> {
                    if (response.responseCode == 401) {
                        // 로그인창으로 보내기. 필요하다면,
                        _event.emit(Event.NavigateToLogin)
                    }
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }

                is ApiResponse.NetworkError -> {
                    _event.emit(Event.NetworkErrorEvent) // 네트워크 에러는 UI의 상태가 변경된다기 보다, 단발적으로 유저에게 알린다.
                    _uiState.update { it.copy(isLoading = false) }
                }

                is ApiResponse.Unexpected -> _uiState.update {
                    it.copy(isLoading = false, isError = true)
                }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val isLogin: Boolean = false,
        val data: UserProfile = UserProfile("", ""),
    )

    data class UserProfileUiState(
        val isLoading: Boolean,
        val isError: Boolean,
        val isLogin: Boolean,
        val data: UserProfile,
    )

    sealed interface Event {
        data object ChangeAsLoginUser : Event
        data object ChangeAsAnonymousUser : Event
        data object NetworkErrorEvent : Event
        data object NavigateToLogin : Event
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return MyPageViewModel(
                    DeamHomeApplication.container.userRepository,
                    DeamHomeApplication.container.authRepository,
                ) as T
            }
        }

        private val ANONYMOUS_USER_PROFILE = UserProfile(
            "익명유저",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkJt3zpLdarPYrYfM6NTLnyvj1BxrUOgh3GgwBRTggwA&s",
        )
    }
}
