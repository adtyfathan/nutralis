package com.ofa.nutralis.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofa.nutralis.data.remote.ProductItem
import com.ofa.nutralis.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductUiState(
    val products: List<ProductItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val lastQuery: String = ""
)

@HiltViewModel
class ProductInputViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState

    fun searchProducts(query: String, reset: Boolean = true) {
        viewModelScope.launch {
            try {
                val page = if (reset) 1 else _uiState.value.currentPage + 1

                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val response = productRepository.searchProducts(query, page)

                _uiState.value = _uiState.value.copy(
                    products = if (reset) response.products else _uiState.value.products + response.products,
                    isLoading = false,
                    currentPage = response.page,
                    hasMore = (_uiState.value.products.size + response.products.size) < response.count,
                    lastQuery = query
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }
}