package com.example.nutralis.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutralis.data.model.User
import com.example.nutralis.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadUserProfile(){
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            authRepository.getUserData()
                .onSuccess { _user.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun updateUser(username: String){
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            authRepository.updateUserData(username)
                .onSuccess { loadUserProfile() }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun deleteUser(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.deleteUserData()
            result.onSuccess {
                _user.value = null
                onDeleted()
            }.onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun logout(){
        authRepository.logout()
        _user.value = null
    }
}