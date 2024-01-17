package com.example.deamhome.presentation.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.base.BaseViewModel
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.Test
import com.example.deamhome.domain.repository.ProductRepository
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val productRepository: ProductRepository,
) : BaseViewModel() {

    data class UiState(
        val loading: Boolean = true,
        val error: Boolean = false,
        val test: Test = EMPTY_TEST,
    ) {
        companion object {
            val EMPTY_TEST = Test("")
        }
    }

    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState())
    val uiState: LiveData<UiState>
        get() = _uiState

    fun fetchTest() {
        viewModelScope.launch {
            if (_uiState.value?.loading == true) return@launch
            _uiState.value = _uiState.value?.copy(loading = true)
            when (val response = productRepository.test()) {
                is ApiResponse.Success -> {
                    Log.d("mendel", "success: $${response.body}")
                    _uiState.value = _uiState.value?.copy(
                        loading = false,
                        test = response.body ?: UiState.EMPTY_TEST,
                    )
                }

                is ApiResponse.Failure -> {
                    Log.d("mendel", "success: $${response.responseCode}. ${response.error}")
                    _uiState.value = _uiState.value?.copy(loading = false, error = true)
                }

                is ApiResponse.NetworkError -> {
                    Log.d("mendel", "network: $${response.message}")
                    _uiState.value = _uiState.value?.copy(loading = false, error = true)
                }

                is ApiResponse.Unexpected -> {
                    Log.d("mendel", "unexpected: $${response.t}")
                    _uiState.value = _uiState.value?.copy(loading = false, error = true)
                }
            }
        }
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
                ) as T
            }
        }
    }
}
