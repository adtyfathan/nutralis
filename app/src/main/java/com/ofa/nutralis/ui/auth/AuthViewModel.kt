package com.ofa.nutralis.ui.auth

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofa.nutralis.R
import com.ofa.nutralis.data.model.User
import com.ofa.nutralis.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val repository: AuthRepository,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {
    private val _currentUser = MutableStateFlow(firebaseAuth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData

    private val _showAvatarPicker = MutableStateFlow<Boolean>(false)
    val showAvatarPicker: StateFlow<Boolean> = _showAvatarPicker

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _currentUser.value = auth.currentUser
            if (auth.currentUser != null) {
                viewModelScope.launch {
                    _uiState.value = AuthUiState(isLoading = true)
                    val result = repository.getUserData()
                    _userData.value = result.getOrNull()
                    _uiState.value = AuthUiState(isLoading = false)
                }
            } else {
                _userData.value = null
                _uiState.value = AuthUiState(isLoading = false)
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = repository.signInWithGoogle(idToken)
            if (result.isSuccess) {
                repository.getUserData()
                    .onSuccess { _userData.value = it }
                _uiState.value = AuthUiState(isSuccess = true)
            } else {
                _uiState.value = AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun getWebClientId(context: Context): String {
        return context.getString(R.string.default_web_client_id)
    }

    fun register(email: String, password: String, username: String){
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = repository.register(email, password, username, avatar = "avatar1")
            if (result.isSuccess) {
                repository.getUserData()
                    .onSuccess { _userData.value = it }
                _uiState.value = AuthUiState(isSuccess = true)
            } else {
                _uiState.value = AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun login(email: String, password: String){
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = repository.login(email, password)
            if (result.isSuccess) {
                repository.getUserData()
                    .onSuccess { _userData.value = it }
                _uiState.value = AuthUiState(isSuccess = true)
            } else {
                _uiState.value = AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun updateUser(username: String, avatar: String){
        viewModelScope.launch {
            _uiState.value = AuthUiState(
                error = null,
                isLoading = true
            )
            repository.updateUserData(username, avatar)
                .onSuccess {
                    _userData.value = _userData.value?.copy(
                        username = username,
                        avatar = avatar
                    )
                }
                .onFailure { _uiState.value = AuthUiState(error = it.message) }
            _uiState.value = AuthUiState(isLoading = false)
        }
    }

    fun deleteUser(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val result = repository.deleteUserData()
            result.onSuccess {
                _userData.value = null
                onDeleted()
            }.onFailure { _uiState.value = AuthUiState(error = it.message) }
            _uiState.value = AuthUiState(isLoading = false)
        }
    }

    fun logout(){
        repository.logout()
        _userData.value = null
    }

    fun getAvatarResource(avatar: String?): Int {
        return when (avatar) {
            "avatar1" -> R.drawable.avatar1
            "avatar2" -> R.drawable.avatar2
            "avatar3" -> R.drawable.avatar3
            "avatar4" -> R.drawable.avatar4
            "avatar5" -> R.drawable.avatar5
            else -> R.drawable.avatar1
        }
    }

    fun openAvatarPicker() {
        _showAvatarPicker.value = true
    }

    fun closeAvatarPicker() {
        _showAvatarPicker.value = false
    }
}