package com.example.nutralis.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutralis.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun register(email: String, password: String){
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = repository.register(email, password)
            _uiState.value = if (result.isSuccess){
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun login(email: String, password: String){
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = repository.login(email, password)
            _uiState.value = if (result.isSuccess){
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun logout() = repository.logout()

    fun userIsLoggedIn(): Boolean = repository.isUserLoggedIn()
}