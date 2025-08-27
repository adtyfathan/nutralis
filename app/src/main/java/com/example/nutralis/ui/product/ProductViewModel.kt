package com.example.nutralis.ui.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutralis.data.remote.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    var state by mutableStateOf(ProductState())
        private set

    fun fetchProduct(barcode: String){
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, product = null)
            try {
                val response = repository.getProduct(barcode)
                state = state.copy(product = response.product, isLoading = false)
            } catch (e: Exception){
                state = state.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }
}