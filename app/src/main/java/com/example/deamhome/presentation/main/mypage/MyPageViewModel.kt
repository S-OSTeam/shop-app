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

    // uiState가 위에 와야 아래 코드에서 널 포인터 예외 안뜸.
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState(isLoading = true))
    val uiState: StateFlow<UiState>
        get() = _uiState

    // 유저 정보 조회에 대한 UiState
    private val _profileUiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(
        ProfileUiState(
            isLoading = false,
            isError = false,
            data = UserProfile.EMPTY,
        ),
    )

    // 화면 전체에 대한 UiState인 _uiState 변수를 업데이트 해주는 녀석
    // 로그인 여부가 바뀌면 자동으로 profileState가 변경된다.
    // 화면의 모든 요소들의 변화를 받고 처리해서 UiState를 업데이트해주는 단일점임.
    private val combineState =
        combine(_profileUiState, authRepository.isLogin) { profileUiState, isLogin ->
            log("mendel", "combine $profileUiState, $isLogin")
            if (profileUiState.isLoading) return@combine _uiState.update { it.copy(isLoading = true) }
            if (profileUiState.isError) {
                return@combine _uiState.update { it.copy(isLoading = false, isError = true) }
            }
            _uiState.update { uiState ->
                when (isLogin) {
                    true -> uiState.copy(
                        isLoading = false,
                        isError = false,
                        isLogin = true,
                        profile = profileUiState.data,
                    )

                    false -> uiState.copy(
                        isLoading = false,
                        isError = false,
                        isLogin = false,
                        profile = ANONYMOUS_USER_PROFILE,
                    )
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, Unit)
    // Eagerly라서 관찰자가 없어도 계속 관찰한다, 어차피 얘가 uiState를 바꿔도, 얘를 관찰하는 액티비티가 onStop이후엔 처리안하고 돌아와서 처리한다.

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            if (_profileUiState.value.isLoading) return@launch
            _profileUiState.update { it.copy(isLoading = true) }
            delay(1000L)
            when (val response = userRepository.fetchUserProfile()) {
                is ApiResponse.Success -> {
                    response.body?.let { data ->
                        _profileUiState.update {
                            it.copy(isLoading = false, isError = false, data = data)
                        }
                    }
                }

                is ApiResponse.Failure -> {
                    _profileUiState.update { it.copy(isLoading = false, isError = false) }
                }

                is ApiResponse.HttpFailure -> {
                    if (response.responseCode == 401) {
                        // 로그인창으로 보내기. 필요하다면,
                        _event.emit(Event.NavigateToLogin)
                    }
                    _profileUiState.update { it.copy(isLoading = false, isError = true) }
                }

                is ApiResponse.NetworkError -> {
                    _event.emit(Event.NetworkErrorEvent) // 네트워크 에러는 UI의 상태가 변경된다기 보다, 단발적으로 유저에게 알린다.
                    _profileUiState.update { it.copy(isLoading = false, isError = false) }
                }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isLogin: Boolean = false,
        val profile: UserProfile = UserProfile.EMPTY,
    )

    data class ProfileUiState(
        val isLoading: Boolean,
        val isError: Boolean,
        val data: UserProfile,
    )

    data class ListUiState(
        val isLoading: Boolean,
        val isError: Boolean,
        val data: List<Any>,
    )

    sealed interface Event {
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
