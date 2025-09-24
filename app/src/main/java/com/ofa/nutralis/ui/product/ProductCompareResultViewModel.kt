package com.ofa.nutralis.ui.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofa.nutralis.data.remote.ProductResponse
import com.ofa.nutralis.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CompareUiState(
    val product1: ProductResponse? = null,
    val product2: ProductResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProductCompareResultViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(CompareUiState(isLoading = true))
    val uiState: StateFlow<CompareUiState> = _uiState

    fun fetchProducts(barcode1: String, barcode2: String){
        _uiState.value = CompareUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val product1Response = productRepository.getProduct(barcode1)
                val product2Response = productRepository.getProduct(barcode2)
                _uiState.value = CompareUiState(
                    product1 = product1Response,
                    product2 = product2Response,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = CompareUiState(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}