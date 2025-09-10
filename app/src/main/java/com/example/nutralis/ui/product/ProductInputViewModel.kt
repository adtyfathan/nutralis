package com.example.nutralis.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutralis.data.model.ScannedProduct
import com.example.nutralis.data.repository.ScannedProductRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductInputViewModel @Inject constructor(
    private val scannedProductRepository: ScannedProductRepository,
    private val auth: FirebaseAuth
): ViewModel() {
    private val _scannedProducts = MutableStateFlow<List<ScannedProduct>>(emptyList())
    val scannedProducts: StateFlow<List<ScannedProduct>> = _scannedProducts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getProductsByUserId(){
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _scannedProducts.value = scannedProductRepository.getScannedProducts(userId)
            _isLoading.value = false
        }
    }
}